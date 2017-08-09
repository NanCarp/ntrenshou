package renshou.database.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import renshou.database.barcode.BarCodeService;
import renshou.database.finishedproduct.FinishedProductService;
/**
 * @author xuhui
 * @desc 仓库管理
 */
public class StorageController extends Controller {

	/**
	 * @desc 展示仓库管理清单页面
	 * @author xuhui
	 */
	public void index(){
		render("storage.html");
	}
	
	/**
	 * @desc 展示清单页数据,同时加载成品和半成品
	 * @author xuhui
	 */
	public void getJson(){
		String warehouse_name = getPara("warehouse_name");
    	Integer	pageindex = 0;
    	Integer pagelimit = getParaToInt("limit")==null? 12 :getParaToInt("limit");
    	Integer offset = getParaToInt("offset")==null?0:getParaToInt("offset");
    	if(offset!=0){
    		pageindex = offset/pagelimit;
    	}
    	pageindex += 1;
    	Map<String, Object> map = new HashMap<String,Object>(); 	
    	List<Record> dictionaryList = StorageService.getStorage(pageindex, pagelimit,warehouse_name).getList();
    	map.put("rows", dictionaryList);
    	map.put("total",StorageService.getStorage(pageindex, pagelimit,warehouse_name).getTotalRow());
    	renderJson(map);	
	}
	
	/**
	 * @desc:批量删除
	 * @author xuhui
	 */
	public void delete(){
		String ids = getPara(0);
		boolean result = StorageService.delete(ids);
		renderJson(result);
	}
	
	/**
	 * @desc 打开新增以及修改页面
	 * @author xuhui
	 */
	public void getStorage(){
		Integer id = getParaToInt(0);
		List<Record> corrwarsehouse = StorageService.getCorrwarehouse();
		setAttr("corrwarsehouses", corrwarsehouse);
		if(id !=null){
			Record record = StorageService.getSingleWarehouse(id);
			setAttr("im", record);
		}	
		render("storage_detail.html");
	}
	
	/**
	 * @desc 新增以及修改数据
	 * @author xuhui
	 */
	public void saveStorage(){
		boolean flag = false;
		Integer id = getParaToInt("id");
		String warehouse_name = getPara("warehouse_name");
		String position = getPara("position");
		Integer pid = getParaToInt("p_warehouse_name");
		Record record = new Record();
		record.set("warehouse_name", warehouse_name);
		record.set("position", position);
		record.set("pid", pid);
		if(id!=null){
			record.set("id", id);
			flag = Db.update("warehouse", record);
		}else{
			flag = Db.save("warehouse", record);
		}
		renderJson(flag);
	}
}
