package renshou.database.semimanufactures;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import renshou.database.finishedproduct.FinishedProductService;
import renshou.interceptor.FrontInterceptor;
import renshou.interceptor.ManageInterceptor;

/**
 * @desc 半成品信息管理
 * @author xuhui
 */
@Before(ManageInterceptor.class)
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
		boolean result = false;
		Map<String,Object> map = new HashMap<String, Object>();
		boolean flag = SemimanufactureService.deleteOr(ids);
		if(!flag){
			result = SemimanufactureService.delete(ids);
		}	
		map.put("flag", flag);
		map.put("result", result);
		renderJson(map);
	}
	
	/**
	 * @desc:保存数据
	 * @author xuhui
	 */
	public void saveSemimanufacture(){
		boolean result = false;
		boolean flag = false;
		Map<String,Object> map = new HashMap<String,Object>();
		String semimanufactures_number = getPara("semimanufactures_number");
		Integer id = getParaToInt("id");
		Record record = new Record();
		record.set("semimanufactures_number", semimanufactures_number);
		record.set("trade_name",getPara("trade_name"));
		record.set("specifications", getPara("specifications"));
		record.set("measurement_unit", getPara("measurement_unit"));
		record.set("remark", getPara("remark"));
		if(id!=null){
			record.set("id", getParaToInt("id"));
			result = Db.update("semimanufactures", record);
			map.put("tips", "保存成功");
		}else{
			//id为空判断是否存在该编号
			Record JudgeRecord = SemimanufactureService.Judge(semimanufactures_number);
			if(JudgeRecord!=null){
				map.put("tips", "该成品编号已经存在");
				flag = true;
				result = true;
			}else{
				result = Db.save("semimanufactures", record);
				map.put("tips", "保存成功");
			}
		}
		map.put("flag", flag);
		map.put("result",result);
		renderJson(map);

	}
	
}
