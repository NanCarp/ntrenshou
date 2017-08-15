package renshou.privatewarehouses.finishedin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

import renshou.interceptor.ManageInterceptor;
/**
 * @author liyu
 * @desc 自用仓库管理-成品入库管理
 */
@Before(ManageInterceptor.class)
public class FinishedInController extends Controller {

	/**
	 * @desc 展示页面
	 * @author liyu
	 */
	public void index(){
		render("finishedin.html");
	}
	
	/**
	 * @desc 展示清单页数据
	 * @author liyu
	 */
	public void getJson(){
		String storage_number = getPara("storage_number");
		String user_name = getPara("user_name");
    	Integer	pageindex = 0;
    	Integer pagelimit = getParaToInt("limit")==null? 12 :getParaToInt("limit");
    	Integer offset = getParaToInt("offset")==null?0:getParaToInt("offset");
    	if(offset!=0){
    		pageindex = offset/pagelimit;
    	}
    	pageindex += 1;
    	Map<String, Object> map = new HashMap<String,Object>(); 	
    	List<Record> dictionaryList = FinishedInService.getFinishedIn(pageindex, pagelimit,storage_number,user_name).getList();
    	map.put("rows", dictionaryList);
    	map.put("total",FinishedInService.getFinishedIn(pageindex, pagelimit,storage_number,user_name).getTotalRow());
    	renderJson(map);	
	}
	
	/**
	 * @author liyu
	 * @desc 打开编辑清单页面
	 */
	public void getEdit(){
		Integer id = getParaToInt(0);
		Record record = FinishedInService.getFinished(id);
		System.out.println(record);
		setAttr("imm", record);
		// 仓库列表
		List<Record> warehouseList = FinishedInService.getWarehouseList();
		setAttr("warehouseList",warehouseList);
		List<Record> rlist = FinishedInService.getFinishedIn(id);
		setAttr("rlist", rlist);
		render("finishedin_detail.html");
	}
	
	/**
	 * @author liyu
	 * @desc 打开新增页面
	 */
	public void addEdit(){
		List<Record> list = FinishedInService.getFinishedProducts();
		setAttr("im", list);
		render("finishedin_detail_add.html");
	}
	
	/**
	 * @author liyu
	 * @desc 根据产品编号查询产品信息
	 */
	public void getFinishedByNum(){
		String num = getPara("num");
		Record record = FinishedInService.getFinishedByNum(num);
		renderJson(record);
	}
	
	/**
	 * @author liyu
	 * @desc 保存成品入库
	 */
	public void saveFinishedIn(){
		Record admin = (Record) getSession().getAttribute("admin");
		System.out.println(admin);
		Integer t_user_id = admin.getInt("id");
		//Integer t_user_id = 1; // TODO
		String storage_number = FinishedInService.getStorageNo();
		//System.out.println(storage_number);
		String list = getPara("list");
		Integer id = getParaToInt("id");
		
		boolean flag = FinishedInService.saveFinishedIn(storage_number, list, id, t_user_id);
		System.out.println(flag);
		renderJson(flag);
	}
	
	/**
	 * @desc:批量删除
	 * @author liyu
	 */
	public void delete(){
		String ids = getPara(0);
		String[] allid = ids.split(",");
		Boolean flag = true;
		for(String id:allid){
		Record record = FinishedInService.getFinished(Integer.parseInt(id));
			if(record.getBoolean("state")==true){
				flag = false;
			}
		}
		if(flag){
			boolean result = FinishedInService.delete(ids);
		}
		renderJson(flag);
	}
	
	/**
	 * @desc:确认入库
	 * @author liyu
	 * @return 
	 */
	public void check(){
		String ids = getPara(0);
		System.out.println(ids);
		Integer id = Integer.parseInt(ids);
		boolean result = FinishedInService.conform(id);
		renderJson(result);
	}
	
	/**
	 * @desc 编辑前判断是否确认入库操作
	 * @author liyu
	 */
	public void checkOr(){
		Integer id = getParaToInt("id");
		Record record = FinishedInService.getFinished(id);
		boolean result = false;
		if(record.getBoolean("state")==false){
			result = true;
		}
		renderJson(result);
	}
	
	/**
	 * @desc 查看
	 * @author liyu
	 */
	public void look(){
		Integer id = getParaToInt(0);
		Record record = FinishedInService.getFinished(id);//查询出入库单信息
		setAttr("imm", record);
		List<Record> list = FinishedInService.getWarehouseList();//查询最子级仓库
		setAttr("im",list);
		List<Record> rlist = FinishedInService.getFinishedIn(id);//查询入库单明细信息
		setAttr("rlist", rlist);
		render("finishedin_detail_look.html");
	}
}
