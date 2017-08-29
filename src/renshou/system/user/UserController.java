package renshou.system.user;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import renshou.interceptor.ManageInterceptor;
import renshou.utils.MD5Util;
/**
 * @ClassName: AuthorityController
 * @Description: 系统管理_用户管理
 * @author: xuhui
 * @date: 2017年8月3日下午2:00:00
 * @version: 1.0 版本初成
 */
@Before(ManageInterceptor.class)
public class UserController extends Controller {
	public void index() {
	    // 用户名称
        String username = getPara("username","").trim();
        setAttr("username", username);
        // 查询条件
        Map<String,Object> params = new HashMap<>();
        params.put("username", username);
        // 用户列表
        List<Record> userList =  UserService.getUserList(params);
        setAttr("userList", userList);

        render("user.html");
    }
	/**
	 * @desc:用户列表
	 */
	public void getUser(){
	    // 用户 id
        Integer id = getParaToInt();

        if (id != null) {
            // 用户
            Record user = UserService.getUserById(id);
            setAttr("user", user);
        }
        // 角色列表
        List<Record> roleList = UserService.getRoleList();
        setAttr("roleList", roleList);
        
		render("user-detail.html");
	}

	// 保存用户
	public void saveUser() throws NoSuchAlgorithmException, UnsupportedEncodingException {
	    // 账号 id
        Integer id = getParaToInt("id");
	    // 账号
        String account = getPara("account");
        // 账户密码
        String pwd = getPara("password");
        // 角色 id
        String roleId = getPara("roleId");
        // 姓名
        String user_name = getPara("user_name");
        // 保存结果
        boolean result = false;
        // 返回信息
        Map<String, Object> response = new HashMap<>();
        
        // 检测账号名是否重复
        if (id == null && UserService.isUserDuplicate(account)) {
            response.put("tips", "用户已存在");
            response.put("isSuccess", false);
            renderJson(response);
            return;
        }
        
        Record record = new Record();
        record.set("role_id", roleId);
        record.set("account", account);
        record.set("user_name", user_name);
        if (id != null) {// 编辑
            // 密码
            Record user = Db.findById("t_user", id);           
            record.set("password", MD5Util.getEncryptedPwd(pwd));
            record.set("id", id);
            result = Db.update("t_user", record);
            response.put("isSuccess", result);
            response.put("tips", result ? "保存成功": "保存失败");
            
        } else {// 新增            
            record.set("password", MD5Util.getEncryptedPwd(pwd));
            result = Db.save("t_user", record);
            response.put("isSuccess", result);
            response.put("tips", result ? "保存成功": "保存失败");
        }

        renderJson(response);
    }

	/**
	 * @desc 删除以及批量删除
	 */
	public void delete(){
	    // 返回信息
        Map<String, Object> response = new HashMap<>();
        // 账号
		String ids = getPara(0);
		// 查询是否产生业务，产生业务无法删除
		for (String id : ids.split(",")) {
		    if (UserService.hasBusiness(id)) {
		        response.put("isSuccess", false);
		        response.put("tips", "已有出入库数据产生，不能删除");
		        renderJson(response);
		        return;
		    }
		}
		
		boolean result = UserService.delete(ids);
		response.put("isSuccess", result);
        response.put("tips", result ? "删除成功" : "删除失败");
        renderJson(response);
	}
	
	/**
	 * @desc 修改密码
	 */
    public void change_pwd(){
    	render("user_changepwd.html");
    }
    
    /**
     * @throws UnsupportedEncodingException 
     * @throws NoSuchAlgorithmException 
     * @desc 保存修改密码
     */
    public void save_change_pwd() throws NoSuchAlgorithmException, UnsupportedEncodingException{
    	boolean v = true;
    	Record admin = (Record) getSession().getAttribute("admin");
    	String oldpwd = getPara("oldpwd"); 
    	Integer id = admin.getInt("id");
    	v = MD5Util.validPassword(oldpwd,admin.getStr("password"));
    	if(v){
    		String comformpwd = getPara("comformpwd");   
    		Record record = new Record();
    		record.set("password", MD5Util.getEncryptedPwd(comformpwd));
    		record.set("id", id);
    		Db.update("t_user", record);
    	}
    	renderJson(v);
    }
    
    // 根据公司 id 获取角色列表
    public void getRoleByCompanyId() {
        // 公司 id
        Integer companyId = getParaToInt();
        // 角色列表
        List<Record> roleList = UserService.getRoleByCompanyId(companyId);

        renderJson(roleList);
    }
    
    /**
     * @desc:冻结或启用账号
     * 1：启用，0：冻结
     */
    public void freezeOrEnable() {
        // 账号 id
        Integer id = getParaToInt("id");

        // 启用或冻结公司操作结果
        boolean result = UserService.freezeOrEnable(id);
        
        renderJson(result);
    }
    
}
