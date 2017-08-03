package renshou.system.company;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import renshou.interceptor.ManageInterceptor;

/**
 * @ClassName: CompanyController
 * @Description: 系统管理_公司管理
 * @author: xuhui
 * @date: 2017年5月15日下午2:00:00
 * @version: 1.0 版本初成
 */
@Before(ManageInterceptor.class)
public class CompanyController extends Controller{
	
	public void index() {
		// 公司名称
		String companyName = getPara("companyName","").trim();
		setAttr("companyName", companyName);
		// 公司状态
		String state = getPara("state","");
		setAttr("state", state);
		
		Map<String,Object> params = new HashMap<>();
		params.put("companyName", companyName);
		params.put("state", state);
		
		// 公司列表
		List<Record> companyList = CompanyService.getCompanyList(params);
		setAttr("companyList", companyList);
		
		render("companycontrol.html");
	}
	
	/** 
	* @Title: saveCompany 
	* @Description: 保存公司
	*/
	public void saveCompany() {
		// 公司 id
		Integer id = getParaToInt("id");
		// 公司名称
		String companyName = getPara("companyName").trim();
		// 备注
		String remark = getPara("remark","");
		// 当前时间
		Date now = new Date();
		// 保存结果
		boolean result = false;
		// 返回信息
        Map<String, Object> response = new HashMap<>();
		// 重复检测
		if (id == null && CompanyService.isDuplicate(companyName)) {
            response.put("tips", "公司名重复！");
            response.put("isSuccess", false);
            renderJson(response);
            return;
        }
		
		Record record = new Record();
		record.set("company_name", companyName);
		record.set("remark", remark);
		record.set("review_time", now);// 修改时间

		if (id != null){// 编辑
			record.set("id", id);
			result = Db.update("t_company", record);
			response.put("isSuccess", result);
	        response.put("tips", result ? "保存成功": "保存失败");
		} else {
			record.set("create_time", now);
			result = Db.save("t_company", record);
			response.put("isSuccess", result);
	        response.put("tips", result ? "保存成功": "保存失败");
		}

		renderJson(response);
	}
	
	/**
	 * @desc:公司列表
	 */
	public void getCompany(){
		// 公司 id
		Integer id = getParaToInt();
		
		if (id != null) {//编辑
			Record company = Db.findById("t_company", id);
			setAttr("company", company);
		}
		
		render("companycontrol-detail.html");
	}
	
	/**
	 * @desc:冻结或启用公司
	 */
	public void freezeOrEnableCompany() {
	    // 公司 id
        Integer id = getParaToInt("id");
        // 公司状态，1：启用，0：冻结
        boolean state = getParaToBoolean("state");

        // 启用或冻结公司操作结果
        boolean result = CompanyService.freezeOrEnableCompany(id, state);
        renderJson(result);
    }
	
	/**
	 * @desc:批量删除
	 * @author xuhui
	 */
	public void delete(){
		String ids = getPara(0);
		boolean result = CompanyService.delete(ids);
		renderJson(result);
	}
}
