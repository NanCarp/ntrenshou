package renshou.database.finishedproduct;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

import renshou.database.semimanufactures.SemimanufactureService;
import renshou.interceptor.FrontInterceptor;
import renshou.interceptor.ManageInterceptor;


/**
 * @author xihui
 * @desc 成品信息管理
 */
@Before(ManageInterceptor.class)
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
		boolean result = false;
		Map<String,Object> map = new HashMap<String, Object>();
		boolean flag = FinishedProductService.deleteOr(ids);
		if(!flag){
			result = FinishedProductService.delete(ids);
		}	
		map.put("flag", flag);
		map.put("result", result);
		renderJson(map);		
	}
	
	/**
	 * @desc:保存数据
	 * @author xuhui
	 */
	public void saveFinishedProduct(){
		boolean result = false;//判断是否保存成功
		boolean flag = false;
		Map<String,Object> map = new HashMap<String,Object>();
		String finished_number = getPara("finished_number");
		Integer id = getParaToInt("id");
		Record record = new Record();
		record.set("specifications", getPara("specifications"));
		record.set("measurement_unit", getPara("measurement_unit"));
		record.set("remark", getPara("remark"));
		if(id!=null){
			record.set("id", getParaToInt("id"));
			result = Db.update("finished_product", record);
			map.put("tips", "保存成功");
		}else{
			//id为空判断是否存在该编号
			Record JudgeRecord = FinishedProductService.Judge(finished_number);
			if(JudgeRecord!=null){
				map.put("tips", "该成品编号已经存在");
				flag = true;
				result = true;
			}else{
				record.set("foreign_code", getPara("foreign_code"));
				record.set("finished_number", finished_number);
				record.set("trade_name",getPara("trade_name"));
				result = Db.save("finished_product", record);
				map.put("tips", "保存成功");
			}
		}
		map.put("flag", flag);
		map.put("result",result);
		renderJson(map);
	}
}
