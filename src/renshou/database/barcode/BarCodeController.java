package renshou.database.barcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

import renshou.database.finishedproduct.FinishedProductService;
import renshou.interceptor.FrontInterceptor;
import renshou.interceptor.ManageInterceptor;
/**
 * @author xuhui
 * @desc 条形码管理
 */
@Before(ManageInterceptor.class)
public class BarCodeController extends Controller {
	
	public void index(){
		render("barcode.html");
	}
	
	/**
	 * @desc 展示清单页数据,同时加载成品和半成品
	 * @author xuhui
	 */
	public void getJson(){
    	Integer	pageindex = 0;
    	Integer pagelimit = getParaToInt("limit")==null? 12 :getParaToInt("limit");
    	Integer offset = getParaToInt("offset")==null?0:getParaToInt("offset");
    	if(offset!=0){
    		pageindex = offset/pagelimit;
    	}
    	pageindex += 1;
    	Map<String, Object> map = new HashMap<String,Object>(); 	
    	List<Record> dictionaryList = BarCodeService.getProduct(pageindex, pagelimit).getList();
    	map.put("rows", dictionaryList);
    	map.put("total",BarCodeService.getProduct(pageindex, pagelimit).getTotalRow());
    	renderJson(map);	
	}
	
	/**
	 * @desc 打印条形码，添加数量
	 */
	public void addNum(){
		String products = getPara(0);
		String[] productNums = products.split(",");
		String addproduct = "";
		int count = 1;
		for(String productNum:productNums){
			addproduct += "'"+productNum+"'";
				if(count<productNums.length){
					addproduct += ",";
				}
				count++;
		}
		List<Record> list = BarCodeService.getConfromPrint(addproduct);
		setAttr("userList", list);
		render("barcode_num.html");
	}
	
	/**
	 * 
	 * @desc 获取打印数量
	 * @author xuhui
	 */
	public void getPrintNum(){
		List<Record> list = BarCodeService.getAllProduct();
		List<Record> storage = new ArrayList<>();
		Map<String,String> map = new HashMap<>();
		for(Record record:list){
			String printNum = getPara(record.getStr("product_num"));
			if(printNum!=null){
				map.put(record.getStr("product_num"), printNum);
			}
		}
		for(String key:map.keySet()){
			Record record = BarCodeService.getSingleProduct(key);
			Integer count =Integer.parseInt(map.get(key).trim());
			for(int k=0;k<count;k++){
				storage.add(record);
			}
		}
		getSession().setAttribute("storage", storage);
		System.out.println(storage.size());
		renderJson(true);
	}
	
	/**
	 * @desc 展示条形码
	 * @author xuhui
	 */
	public void showPrint(){
		List<Record> storage =(List<Record>)getSession().getAttribute("storage");
		setAttr("storages", storage);
		render("barcode_look.html");
	}
}
