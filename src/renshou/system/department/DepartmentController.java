package renshou.system.department;

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
 * @ClassName: DepartmentController
 * @Description: 系统管理_部门管理
 * @author: xuhui
 * @date: 2017年5月15日下午2:00:00
 * @version: 1.0 版本初成
 */
@Before(ManageInterceptor.class)
public class DepartmentController extends Controller{
	public void index() {
	    // 部门
	    String department = getPara("department", "").trim();
        setAttr("department", department);
        // 公司
        String company = getPara("company", "");
        setAttr("company", company);
        // 部门状态
	    String state = getPara("state", "");
        setAttr("state", state);

        Map<String, Object> params = new HashMap<>();
        params.put("department", department);
        params.put("company", company);
        params.put("state", state);

        // 部门列表
        List<Record> departmentList =  DepartmentService.getDepartmentList(params);
        setAttr("departmentList", departmentList);

		render("department.html");
	}
	/**
	 * @desc:部门
	 */
	public void getDepartment(){
        // 部门 id
        Integer id = getParaToInt();

        if (id != null) {//编辑
            Record department  = Db.findById("t_department", id);
            setAttr("department", department);
        }

		// 公司列表
		List<Record> companyList = DepartmentService.getCompanyList();
		setAttr("companyList", companyList);

		render("department-detail.html");
	}

	// 根据公司 id 获取部门列表
	public void getDepartmentByCompanyId() {
	    // 公司 id
        Integer companyId = getParaToInt();
        // 部门列表
        List<Record> departmentList = DepartmentService.getDepartmentByCompanyId(companyId);

        renderJson(departmentList);
    }

    public void saveDepartment() {
        // 部门 id
        Integer id = getParaToInt("id");
        // 部门名称
        String department = getPara("department").trim();
        // 所属公司
        Integer companyId = getParaToInt("companyId");
        // 备注
        String remark = getPara("remark", "");
        // 当前时间
        Date now = new Date();
        // 保存结果
        boolean result = false;
        // 返回信息
        Map<String, Object> response = new HashMap<>();
        // 重复检测
        if (id == null && DepartmentService.isDuplicate(department, companyId)) {
            response.put("tips", "部门重复！");
            response.put("isSuccess", false);
            renderJson(response);
            return;
        }

        Record record = new Record();
        record.set("department_name", department);
        record.set("company_id", companyId);
        record.set("remark", remark);
        record.set("review_time", now);// 修改时间
        if (id != null) {// 编辑
            record.set("id", id);
            result = Db.update("t_department", record);
            response.put("isSuccess", result);
            response.put("tips", result ? "保存成功": "保存失败");
        } else {// 新增
            record.set("create_time", now);
            result = Db.save("t_department", record);
            response.put("isSuccess", result);
            response.put("tips", result ? "保存成功": "保存失败");
        }

        renderJson(response);
    }

    // 冻结或启用部门
    public void freezeOrEnableDepartment() {
        // 部门 id
        Integer id = getParaToInt("id");
        // 部门状态，1：启用，0：冻结
        boolean state = getParaToBoolean("state");

        // 启用或冻结部门操作结果
        boolean result = DepartmentService.freezeOrEnableDepartment(id, state);

        renderJson(result);
    }
    
    /**
	 * @desc:批量删除
	 * @author xuhui
	 */
	public void delete(){
		String ids = getPara(0);
		boolean result = DepartmentService.delete(ids);
		renderJson(result);
	}

}
