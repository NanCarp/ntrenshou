package renshou.database.customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.json.Json;
import com.jfinal.plugin.activerecord.Record;

import renshou.database.finishedproduct.FinishedProductService;
import renshou.database.storage.StorageService;
import renshou.interceptor.FrontInterceptor;
import renshou.interceptor.ManageInterceptor;

/**
 * @desc 公司管理
 * @author xuhui
 */
@Before(ManageInterceptor.class)
public class CustomController extends Controller {

	/**
	 * @desc 展示清单页面
	 */
	public void index(){
		render("customer.html");
	}
	
	/**
	 * @desc 展示清单页数据
	 * @author xuhui
	 */
	public void getJson(){
		String company_name = getPara("company_name");
		System.out.println(company_name);
    	Integer	pageindex = 0;
    	Integer pagelimit = getParaToInt("limit")==null? 12 :getParaToInt("limit");
    	Integer offset = getParaToInt("offset")==null?0:getParaToInt("offset");
    	if(offset!=0){
    		pageindex = offset/pagelimit;
    	}
    	pageindex += 1;
    	Map<String, Object> map = new HashMap<String,Object>(); 	
    	List<Record> dictionaryList = CustomerService.getCustomer(pageindex, pagelimit,company_name).getList();
    	map.put("rows", dictionaryList);
    	map.put("total",CustomerService.getCustomer(pageindex, pagelimit,company_name).getTotalRow());
    	renderJson(map);	
	}
	
	/**
	 * @author xuhui
	 * @desc 打开公司管理新增以及编辑页面
	 */
	public void getEdit(){
		Integer id = getParaToInt(0);
		if(id!=null){
			Record company = CustomerService.getCustom(id);
			List<Record> company_contact = CustomerService.getContant(id);
			setAttr("company", company);
			setAttr("company_contact", company_contact);
		}
		render("customer_detail.html");
	}
	
	/**
	 * @author xuhui
	 * @desc 打开新增联系人页面
	 */
	public void addContant(){
		String name = getPara("name");
		String call_num = getPara("call_num");
		String QQ = getPara("QQ");
		String wechat = getPara("wechat");
		String e_mail = getPara("e_mail");
		String remark = getPara("remark");
		Integer position = getParaToInt("position");
		Record record = new Record();
		record.set("position", position);
		record.set("name", name);
		record.set("call_num", call_num);
		record.set("QQ", QQ);
		record.set("wechat", wechat);
		record.set("e_mail", e_mail);
		record.set("remark", remark);
		setAttr("im",record);
		render("company_contact_detail.html");
	}
	
	/**
	 * @desc 保存客户管理
	 * @author xuhui
	 */
	public void saveCustom(){
		Integer id= getParaToInt("id");
		String company_address = getPara("company_address");
		String company_name = getPara("company_name");
		String remark = getPara("remark");
		String list = getPara("list");
		boolean result = CustomerService.saveCustoms(id, company_name, company_address, remark, list);
		renderJson(result);
	}
	
	/**
	 * @desc:批量删除
	 * @author xuhui
	 */
	public void delete(){
		String ids = getPara(0);
		boolean result = CustomerService.delete(ids);
		renderJson(result);
	}
}
