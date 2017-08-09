package renshou.database.semimanufactures;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * @desc 半成品信息管理
 * @author xuhui
 */
public class SemimanufacturesController extends Controller {

	/**
	 * @desc 展示清单页面
	 * @author xuhui
	 */ 
	public void index(){
		render("semimanufactures.html");
	}
	
	/**
	 * @desc 展示清单页数据
	 * @author xuhui
	 */
	public void getJson(){
		String trade_name = getPara("trade_name");
		System.out.println(trade_name);
    	Integer	pageindex = 0;
    	Integer pagelimit = getParaToInt("limit")==null? 12 :getParaToInt("limit");
    	Integer offset = getParaToInt("offset")==null?0:getParaToInt("offset");
    	if(offset!=0){
    		pageindex = offset/pagelimit;
    	}
    	pageindex += 1;
    	Map<String, Object> map = new HashMap<String,Object>(); 	
    	List<Record> dictionaryList = SemimanufactureService.getSemimanufacture(pageindex, pagelimit,trade_name).getList();
    	map.put("rows", dictionaryList);
    	map.put("total",SemimanufactureService.getSemimanufacture(pageindex, pagelimit,trade_name).getTotalRow());
    	renderJson(map);	
	}
	
	/**
	 * @author xuhui
	 * @desc 新增半成品信息管理
	 */
	public void getsemimanufactures(){
		Integer id = getParaToInt();
		if(id!=null){
			Record record = SemimanufactureService.getSingleSemimanufactures(id);
			setAttr("im", record);
		}
		render("semimanufactures_detail.html");
	}
	
	/**
	 * @desc:批量删除
	 * @author xuhui
	 */
	public void delete(){
		String ids = getPara(0);
		boolean result = SemimanufactureService.delete(ids);
		renderJson(result);
	}
	
	/**
	 * @desc:保存数据
	 * @author xuhui
	 */
	public void saveSemimanufacture(){
		boolean result = false;
		Integer id = getParaToInt("id");
		Record record = new Record();
		record.set("trade_name",getPara("trade_name"));
		record.set("specifications", getPara("specifications"));
		record.set("measurement_unit", getPara("measurement_unit"));
		record.set("remark", getPara("remark"));
		if(id!=null){
			record.set("id", getParaToInt("id"));
			result = Db.update("semimanufactures", record);
		}else{
			//自动生成成品编码
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String year = sdf.format(date);
			String prefix = "BP";
			String finished_number = "";
			if(SemimanufactureService.getMaxNum().getStr("num")!=null){
				System.out.println(SemimanufactureService.getMaxNum());
				String countstring = SemimanufactureService.getMaxNum().getStr("num").substring(6);						
				Integer countp = Integer.parseInt(countstring) + 10001;
				System.out.println(countstring);
				String countn = String.valueOf(countp).substring(1);
				finished_number = prefix + year +countn;
			}else{
				finished_number = prefix + year +"0001";
			}
			record.set("semimanufactures_number", finished_number);
			result = Db.save("semimanufactures", record);
		}
		renderJson(result);
	}
	
}
