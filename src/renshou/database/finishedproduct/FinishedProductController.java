package renshou.database.finishedproduct;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;


/**
 * @author xihui
 * @desc 成品信息管理
 */
public class FinishedProductController extends Controller {

	public  void index(){
		render("finishedproduct.html");
	}
	
	/**
	 * @desc 展示清单页数据
	 * @author xuhui
	 */
	public void getJson(){
		String trade_name = getPara("trade_name");
    	Integer	pageindex = 0;
    	Integer pagelimit = getParaToInt("limit")==null? 12 :getParaToInt("limit");
    	Integer offset = getParaToInt("offset")==null?0:getParaToInt("offset");
    	if(offset!=0){
    		pageindex = offset/pagelimit;
    	}
    	pageindex += 1;
    	Map<String, Object> map = new HashMap<String,Object>(); 	
    	List<Record> dictionaryList = FinishedProductService.getFinishedProduct(pageindex, pagelimit,trade_name).getList();
    	map.put("rows", dictionaryList);
    	map.put("total",FinishedProductService.getFinishedProduct(pageindex, pagelimit,trade_name).getTotalRow());
    	renderJson(map);	
	}
	
	/**
	 * @author xuhui
	 * @desc 新增成品信息管理
	 */
	public void getfinishedproduct(){
		Integer id = getParaToInt();
		if(id!=null){
			Record record = FinishedProductService.getSingleFinishedProduct(id);
			setAttr("im", record);
		}
		render("finishedproduct_detail.html");
	}
	
	
	/**
	 * @desc:批量删除
	 * @author xuhui
	 */
	public void delete(){
		String ids = getPara(0);
		boolean result = FinishedProductService.delete(ids);
		renderJson(result);
	}
	
	/**
	 * @desc:保存数据
	 * @author xuhui
	 */
	public void saveFinishedProduct(){
		boolean result = false;
		Integer id = getParaToInt("id");
		Record record = new Record();
		record.set("trade_name",getPara("trade_name"));
		record.set("specifications", getPara("specifications"));
		record.set("measurement_unit", getPara("measurement_unit"));
		record.set("remark", getPara("remark"));
		if(id!=null){
			record.set("id", getParaToInt("id"));
			result = Db.update("finished_product", record);
		}else{
			//自动生成成品编码
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String year = sdf.format(date);
			String prefix = "CP";
			String finished_number = "";
			if(FinishedProductService.getMaxNum().getStr("num")!=null){
				System.out.println(FinishedProductService.getMaxNum());
				String countstring = FinishedProductService.getMaxNum().getStr("num").substring(6);						
				Integer countp = Integer.parseInt(countstring) + 10001;
				System.out.println(countstring);
				String countn = String.valueOf(countp).substring(1);
				finished_number = prefix + year +countn;
			}else{
				finished_number = prefix + year +"0001";
			}
			record.set("finished_number", finished_number);
			result = Db.save("finished_product", record);
		}
		renderJson(result);
	}
}
