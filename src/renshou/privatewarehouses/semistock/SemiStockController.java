package renshou.privatewarehouses.semistock;

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
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import renshou.interceptor.FrontInterceptor;
import renshou.interceptor.ManageInterceptor;
import renshou.privatewarehouses.semiin.SemiInService;
import renshou.utils.ExcelKit;
/**
 * @desc 
 * @author xuhui
 */
@Before(ManageInterceptor.class)
public class SemiStockController extends Controller {

	/**
	 * @desc 展示页面
	 * @author xuhui
	 */
	public void index(){
		render("semistock.html");
	}
	
	/**
	 * @desc 展示清单页数据
	 * @author xuhui
	 */
	public void getJson(){
		String semimanufactures_number = getPara("semimanufactures_number");
		String trade_name = getPara("trade_name");
    	Integer	pageindex = 0;
    	Integer pagelimit = getParaToInt("limit")==null? 12 :getParaToInt("limit");
    	Integer offset = getParaToInt("offset")==null?0:getParaToInt("offset");
    	if(offset!=0){
    		pageindex = offset/pagelimit;
    	}
    	pageindex += 1;
    	Map<String, Object> map = new HashMap<String,Object>(); 	
    	List<Record> dictionaryList = SemiStockService.getStock(pageindex, pagelimit,semimanufactures_number,trade_name).getList();
    	map.put("rows", dictionaryList);
    	map.put("total",SemiStockService.getStock(pageindex, pagelimit,semimanufactures_number,trade_name).getTotalRow());
    	renderJson(map);	
	}
	
	/**
	 * @desc 查看明细
	 * @author xuhui
	 */
	public void getStockDetail(){
		Integer id = getParaToInt(0);
		Record record = SemiStockService.getStockById(id);
		//根据仓库库存id查询该id下存放的产品id
		if(record!=null){
			Integer SemiproductId = record.getInt("semimanufactures_id");
			List<Record> list = SemiStockService.getStockDetailById(SemiproductId);
			setAttr("SemiproductId", SemiproductId);
			setAttr("im", list);
		}	
		List<Record> noRepeatList = SemiStockService.getNoRepeat();
		setAttr("noRepeatList", noRepeatList);
		setAttr("record", record);
		render("semistock_checkdetail.html");
	}
	
	/**
	 * @desc:批量删除
	 * @author xuhui
	 */
	public void delete(){
		String ids = getPara(0);
		String[] allid = ids.split(",");
		Boolean flag = true;
		flag = SemiStockService.delete(ids);
		renderJson(flag);
	}
	
	/**
	 * @desc 打开新增存储细节页面
	 * @author xuhui
	 */
	public void getDetail(){
		Integer semimanufactures_id = getParaToInt("semimanufactures_id");
		Integer warehouse_id = getParaToInt("warehouse_id");
		String numStr = getPara("num");	
		Integer index = getParaToInt("index");
		//同时为空表示新增数据
		if(warehouse_id!=null&&numStr!=null){
			Double num = Double.valueOf(numStr);
			Record record = new Record();
			record.set("semimanufactures_id", semimanufactures_id);
			record.set("warehouse_id", warehouse_id);
			record.set("num", num);
			record.set("index", index);
			setAttr("record", record);
		}
		Integer SemiproductId = getParaToInt("SemiproductId");
		if(SemiproductId!=null){
			setAttr("SemiproductId", SemiproductId);
		}
		List<Record> list =SemiStockService.getSublevel();
		setAttr("im", list);
		render("semistock_checkdetail_add.html");
	}
	
	public void getProMessage(){
		Integer code = getParaToInt("code");
		Record record = SemiStockService.getMeimes(code);
		renderJson(record);
	}
	
	/**
	 * @desc 保存数据
	 * @author xuhui
	 */
	public void saveSemiStock(){
		Integer id = getParaToInt("id");
		Integer semimanufactures_id = getParaToInt("semimanufactures_id");
		String list = getPara("list");
		String semimanufactures_id_num= getPara("semimanufactures_id_num");
		boolean flag = SemiStockService.saveStrock(id, semimanufactures_id, list, semimanufactures_id_num);
		renderJson(flag);
	}
	
	/**
	 * @desc 打开导入Excel页面
	 * @author xuhui
	 */
	public void showimportdesk(){
		render("semistock_import.html");
	}
	
	/**
	 * @desc 导入文件
	 * @author xuhui
	 */
	public void importExcel(){
		Map<String,Object> map = new HashMap<String,Object>();
		List countWrongList = new ArrayList();
		boolean flag=Db.tx(new IAtom() {		
					@Override
					public boolean run() throws SQLException {
						// TODO Auto-generated method stub
						UploadFile file = getFile("file");
						List<String[]> list = ExcelKit.getExcelData(file.getFile());
						//导入excel返回结果，true导入正确，false导入错误
						boolean result = true;
						boolean lag = false;
						//该Excel导入列数应为31列，不符合31列即为错误导入excel文件
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
									String sqlpara="select * from semimanufactures where semimanufactures_number='"+strings[1]+"'";
									Integer sid = Db.findFirst(sqlpara).getInt("id");						
									record.set("semimanufactures_id", sid);//产品id
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
									lag = Db.save("semimanufactures_stock_detail", record);
								}catch(Exception e){
									//countWrongList记录错误行数，不重复显示错误行数；
									//指定一个判定对象，如果countWrongList已有该行数则返回false，否则返回true；
									e.printStackTrace();
									countWrongList.add(i+2+"行"+"存在数据异常，请校验");
									result = false;
								}
							}
							if(lag){
								List<Record> listSum = SemiStockService.getSemiStocklistBySemiid();
								System.out.println("listSum:"+listSum.size());
								for(Record rec:listSum){
									Record reco = new Record();
									reco.set("semimanufactures_id", rec.get("semimanufactures_id"));
									reco.set("semimanufactures_stock_num", rec.get("semimanufactures_stock_num"));
									Db.save("semimanufactures_stock", reco);
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
	 * @author xuhui
	 */
	public boolean match(String str){
		Pattern pattern = Pattern.compile("[`~!@#$%^&*+=|{}':;',\\[\\]<>?~！@#￥%……&*——+|{}【】‘；：”“’。，、？]");
		Matcher matcher = pattern.matcher(str);
		return matcher.find(); 
	}
	
	/**
	 * @desc 将错误行数或者文件错误显示在页面上
	 * @author xuhui
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
		System.out.println("5555555");
		boolean result = SemiStockService.getExcel(getResponse());
		if(true){
			renderNull();
		}else{
			renderText("导出失败");
		}	
	}
}
