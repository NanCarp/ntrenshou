package renshou.privatewarehouses.finishedstock;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import renshou.interceptor.ManageInterceptor;
import renshou.utils.ExcelKit;

/**
 * @desc 
 * @author liyu
 */
@Before(ManageInterceptor.class)
public class FinishedStockController extends Controller {

	/**
	 * @desc 展示页面
	 * @author liyu
	 */
	public void index(){
		render("finishedstock.html");
	}
	
	/**
	 * @desc 展示清单页数据
	 * @author liyu
	 */
	public void getJson(){
		String finished_number = getPara("finished_number"); // 成品编码
		String trade_name = getPara("trade_name"); // 成品名称
		
    	Integer	pageindex = 0;
    	Integer pagelimit = getParaToInt("limit")==null? 12 :getParaToInt("limit");
    	Integer offset = getParaToInt("offset")==null?0:getParaToInt("offset");
    	if(offset!=0){
    		pageindex = offset/pagelimit;
    	}
    	pageindex += 1;
    	Map<String, Object> map = new HashMap<String,Object>(); 	
    	Page<Record> page = FinishedStockService.getStock(pageindex, pagelimit,finished_number,trade_name);
    	map.put("rows", page.getList());
    	map.put("total", page.getTotalRow());
    	renderJson(map);	
	}
	
	/**
	 * @desc 查看明细
	 * @author liyu
	 */
	public void getStockDetail(){
        // 库存 id
        Integer id = getParaToInt(0);
        // 库存对应产品信息
        Record record = FinishedStockService.getStockById(id);
        setAttr("record", record);
        // 根据仓库库存id查询该id下存放的产品id
        if (record != null) {
            Integer finishedProductId = record.getInt("finished_product_id"); // 产品 id
            List<Record> list = FinishedStockService.getStockDetailById(finishedProductId); // 根据id查询产品在库存中的位置
            setAttr("finishedProductId", finishedProductId);
            setAttr("im", list);
        }
        // 筛选出尚未添加到库存里的数据
        List<Record> noRepeatList = FinishedStockService.getNoRepeat();
        setAttr("noRepeatList", noRepeatList);
        
        render("finishedstock_checkdetail.html");
	}
	
	/**
	 * @desc:批量删除
	 * @author liyu
	 */
	public void delete(){
		String ids = getPara(0);
		Boolean flag = true;
		flag = FinishedStockService.delete(ids);
		renderJson(flag);
	}
	
	/**
	 * @desc 打开新增存储细节页面
	 * @author liyu
	 */
	public void getDetail(){
		Integer finished_product_id = getParaToInt("finished_product_id");
		Integer warehouse_id = getParaToInt("warehouse_id");
		String numStr = getPara("num");	
		Integer index = getParaToInt("index");
		//同时为空表示新增数据
		if(warehouse_id!=null&&numStr!=null){
			Double num = Double.valueOf(numStr);
			Record record = new Record();
			record.set("finished_product_id", finished_product_id);
			record.set("warehouse_id", warehouse_id);
			record.set("num", num);
			record.set("index", index);
			setAttr("record", record);
		}
		Integer finishedProductId = getParaToInt("finishedProductId");
		if(finishedProductId!=null){
			setAttr("finishedProductId", finishedProductId);
		}
		List<Record> list =FinishedStockService.getSublevel();
		setAttr("im", list);
		render("finishedstock_checkdetail_add.html");
	}
	
	/** 
	* @Title: getProMessage 
	* @Description: 根据成品id查询成品信息
	* @author liyu
	*/
	public void getProMessage(){
		Integer code = getParaToInt("code");
		Record record = FinishedStockService.getMeimes(code);
		renderJson(record);
	}
	
	/**
	 * @desc 保存数据
	 * @author liyu
	 */
	public void saveFinishedStock(){
		Integer id = getParaToInt("id");
		Integer finished_product_id = getParaToInt("finished_product_id");
		String list = getPara("list");
		String finished_product_id_num= getPara("finished_product_id_num");
		boolean flag = FinishedStockService.saveStrock(id, finished_product_id, list, finished_product_id_num);
		renderJson(flag);
	}
	
	/**
	 * @desc 打开导入Excel页面
	 * @author liyu
	 */
	public void showimportdesk(){
		render("finishedstock_import.html");
	}
	
	/**
	 * @desc 导入文件
	 * @author liyu
	 */
	public void importExcel(){
		Map<String,Object> map = new HashMap<String,Object>();
		List countWrongList = new ArrayList();
		boolean flag=Db.tx(new IAtom() {		
					@Override
					public boolean run() throws SQLException {
						UploadFile file = getFile("file");
						List<String[]> list = ExcelKit.getExcelData(file.getFile());
						//导入excel返回结果，true导入正确，false导入错误
						boolean result = true;
						boolean lag = false;
						//该Excel导入列数应为4列，不符合4列即为错误导入excel文件
						if(list.get(0).length!=4){
							getSession().setAttribute("ErrorFile", true);
							result = false;
						}else{
							getSession().setAttribute("ErrorFile", false);
							//检测所有被导入的excel数据是否含有特殊字符
							for(int i=0;i<=list.size()-1;i++){
								String[] strings = list.get(i);	
								for(int k=0;k<=strings.length-1;k++){
									if(match(strings[k])){
										countWrongList.add(i+2+"排"+(k+1)+"列");
										result = false;
									}
								}
								try{
									Record record = new Record();
									String sqlpara="select * from finished_product where finished_number='"+strings[1]+"'";
									Integer sid = Db.findFirst(sqlpara).getInt("id");						
									record.set("finished_product_id", sid);//产品id
									String sql ="SELECT a.id,CONCAT(c.warehouse_name,b.warehouse_name,a.warehouse_name) AS warehouse_name"
												+" FROM `warehouse` AS a"
												+" INNER JOIN warehouse AS b"
												+" ON a.pid = b.id"
												+" INNER JOIN warehouse AS c"
												+" ON b.pid = c.id"
												+" WHERE CONCAT(c.warehouse_name,b.warehouse_name,a.warehouse_name) =  '"+strings[2]+"'";
									Integer warse_houseid = Db.findFirst(sql).getInt("id");
									record.set("warehouse_id",warse_houseid);//仓库id					
									record.set("num", strings[3]);//数量
									lag = Db.save("finished_product_stock_detail", record);
								}catch(Exception e){
									//countWrongList记录错误行数，不重复显示错误行数；
									//指定一个判定对象，如果countWrongList已有该行数则返回false，否则返回true；
									e.printStackTrace();
									countWrongList.add(i+2+"行"+"存在数据异常，请校验");
									result = false;
								}
							}
							if(lag){
								List<Record> listSum = FinishedStockService.getFinishedStocklistByFinishedid();
								System.out.println("listSum:"+listSum.size());
								for(Record rec:listSum){
									Record reco = new Record();
									reco.set("finished_product_id", rec.get("finished_product_id"));
									reco.set("finished_product_stock_num", rec.get("finished_product_stock_num"));
									Db.save("finished_product_stock", reco);
								}
							}
						}	
					return result;
					}
				});
		map.put("flag", flag);
		getSession().setAttribute("countWrongList", countWrongList);
		renderJson(map);
	}
	
	
	/**
	 * @desc 验证导入excel输入的正则验证
	 * @author liyu
	 */
	public boolean match(String str){
		Pattern pattern = Pattern.compile("[`~!@#$%^&*+=|{}':;',\\[\\]<>?~！@#￥%……&*——+|{}【】‘；：”“’。，、？]");
		Matcher matcher = pattern.matcher(str);
		return matcher.find(); 
	}
	
	/**
	 * @desc 将错误行数或者文件错误显示在页面上
	 * @author liyu
	 */
	public void showErrorExcelMessage(){
	List<Integer> countlist = getSessionAttr("countWrongList");
	boolean ErrorFile = getSessionAttr("ErrorFile");
	setAttr("countlist", countlist);
	setAttr("ErrorFile", ErrorFile);
	render("wrong_message.html");
	}
	
	/**
	 * @desc 导出安全管理检查记录excel表格
	 */
	public  void exportExcel(){
		boolean result = FinishedStockService.getExcel(getResponse());
		if(result){
			renderNull();
		}else{
			renderText("导出失败");
		}	
	}
}
