package renshou.privatewarehouses.semiin;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import renshou.database.customer.CustomerService;
import renshou.database.finishedproduct.FinishedProductService;
import renshou.interceptor.FrontInterceptor;
import renshou.interceptor.ManageInterceptor;
import renshou.utils.ExcelKit;
/**
 * @author xuhui
 * @desc 自用仓库管理-半成品入库管理
 */
@Before(ManageInterceptor.class)
public class SemiInController extends Controller {

	/**
	 * @desc 展示页面
	 * @author xuhui
	 */
	public void index(){
		render("semiin.html");
	}
	
	/**
	 * @desc 展示清单页数据
	 * @author xuhui
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
    	List<Record> dictionaryList = SemiInService.getSemiIn(pageindex, pagelimit,storage_number,user_name).getList();
    	map.put("rows", dictionaryList);
    	map.put("total",SemiInService.getSemiIn(pageindex, pagelimit,storage_number,user_name).getTotalRow());
    	renderJson(map);	
	}
	
	/**
	 * @author xuhui
	 * @desc 打开编辑清单页面
	 */
	public void getEdit(){
		Integer id = getParaToInt(0);
		Record record = SemiInService.getSemi(id);
		System.out.println(record);
		setAttr("imm", record);
		List<Record> list = SemiInService.getSublevel();
		setAttr("im",list);
		List<Record> rlist = SemiInService.getSeimIn(id);
		setAttr("rlist", rlist);
		render("semiin_detail.html");
	}
	
	/**
	 * @author xuhui
	 * @desc 打开新增页面
	 */
	public void addEdit(){
		String semimanufactures_number = getPara("semimanufactures_number");
		String trade_name = getPara("trade_name");
		List<Record> list = SemiInService.getSemimanufactures(semimanufactures_number,trade_name);
		setAttr("trade_name", trade_name);
		setAttr("semimanufactures_number", semimanufactures_number);
		setAttr("im", list);
		render("semiin_detail_add.html");
	}
	
	/**
	 * @author xuhui
	 * @desc 根据产品编号查询产品信息
	 */
	public void getSemiByNum(){
		String num = getPara("num");
		Record record = SemiInService.getSemimanufacturesByNum(num);
		renderJson(record);
	}
	
	/**
	 * @author xuhui
	 * @desc 保存半成品入库
	 */
	public void saveSemiIn(){
		Record admin = (Record) getSession().getAttribute("admin");
		Integer t_user_id = admin.getInt("id");
		String storage_number = "";
		String list = getPara("list");
		Integer id = getParaToInt("id");
		Date date = new Date();
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(date);
		String Mdate = "YR"+today;
		Record record = SemiInService.getStroageDate(Mdate);
		if(record.get("maxnum")!=null){
			String MMdate = record.get("maxnum");
			Integer mm = Integer.parseInt(MMdate.substring(10));
			Integer mmInteger = mm + 1001;
			String mmString = String.valueOf(mmInteger).substring(1);
			storage_number = "YR"+today+mmString;
		}else{
			storage_number = "YR"+today+"001";
		}
		System.out.println(storage_number);
		boolean flag = SemiInService.saveSemiIn(storage_number, list, id,t_user_id);
		System.out.println(flag);
		renderJson(flag);
	}
	
	/**
	 * @desc:批量删除
	 * @author xuhui
	 */
	public void delete(){
		String ids = getPara(0);
		String[] allid = ids.split(",");
		Boolean flag = true;
		for(String id:allid){
		Record record = SemiInService.getSemi(Integer.parseInt(id));
			if(record.getBoolean("state")==true){
				flag = false;
			}
		}
		if(flag){
			boolean result = SemiInService.delete(ids);
		}
		renderJson(flag);
	}
	
	/**
	 * @desc:确认入库
	 * @author xuhui
	 * @return 
	 */
	public void check(){
		String ids = getPara(0);
		Record record = SemiInService.getSemi(Integer.parseInt(ids));
		Boolean flag = true;
		if(record.getBoolean("state")==true){
			flag = false;
		}
		if(flag){
			boolean result = SemiInService.conform(Integer.parseInt(ids));
		}
		renderJson(flag);
	}
	
	/**
	 * @desc 编辑前判断是否确认入库操作
	 * @author xuhui
	 */
	public void checkOr(){
		Integer id = getParaToInt("id");
		Record record = SemiInService.getSemi(id);
		boolean result = false;
		if(record.getBoolean("state")==false){
			result = true;
		}
		renderJson(result);
	}
	
	/**
	 * @desc 查看
	 * @author xuhui
	 */
	public void look(){
		Integer id = getParaToInt(0);
		Record record = SemiInService.getSemi(id);//查询出入库单信息
		setAttr("imm", record);
		List<Record> list = SemiInService.getSublevel();//查询最子级仓库
		setAttr("im",list);
		List<Record> rlist = SemiInService.getSeimIn(id);//查询入库单明细信息
		setAttr("rlist", rlist);
		render("semiin_detail_look.html");
	}
	
	
}
