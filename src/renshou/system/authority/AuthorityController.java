package renshou.system.authority;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;

import renshou.interceptor.ManageInterceptor;

/**
 * @ClassName: AuthorityController
 * @Description: 系统管理_权限管理
 * @author: xuhui
 * @date: 2017年8月3日下午2:00:00
 * @version: 1.0 版本初成
 */
@Before(ManageInterceptor.class)
public class AuthorityController extends Controller {
	public void index(){
		
		String role = getPara("role");
        // 授权列表
        List<Record> authorityList = AuthorityService.getAuthorityList(null,role);
        setAttr("authorityList", authorityList);
        setAttr("role", role);
        render("authority.html");
    }
    /**
     * @desc:权限
     */
    public void getAuthority(){
        // 角色 role_id
        Integer roleId = getParaToInt("roleId");

        // 角色权限
        if (roleId != null) {
            Record role = AuthorityService.getMenusByRoleId(roleId);
            setAttr("role", role);
            setAttr("authority", role.getStr("authority"));
        }

        // 菜单列表，ztree 数据源
        List<Record> menuList = AuthorityService.getMenuListForZTree();
        String menuListJson = JsonKit.toJson(menuList);
        setAttr("menuListJson", menuListJson);

        render("authority_detail.html");
    }

    // 保存权限
    public void saveAuthority() {

        // 角色 id
        Integer roleId = getParaToInt("roleId");
        // 角色权限 ids
        String authorityIds = getPara("authorityIds");
        // t_role_menu 表 id
        Integer menusId = getParaToInt("menusId");
        // t_role_button 表 id
        Integer buttonsId = getParaToInt("buttonsId");

        // 保存结果
        boolean result = AuthorityService.saveAuthority(roleId, authorityIds, menusId, buttonsId);

        renderJson(result);

    }

    // 查看权限
    public void checkAuthority() {
        // 角色 id
        Integer roleId = getParaToInt();

        // 角色权限
        Record role = AuthorityService.getMenusByRoleId(roleId);
        setAttr("role", role);
        setAttr("authority", role.getStr("authority"));

        // 菜单列表，ztree 数据源
        List<Record> menuList = AuthorityService.getMenuListForZTree();
        String menuListJson = JsonKit.toJson(menuList);
        setAttr("menuListJson", menuListJson);

        render("authority_check.html");
    }
    
    // 根据公司 id 获取角色列表，剔除已分配权限的角色，
    public void getRoleByCompanyIdNotAuthorized() {
        // 公司 id
        Integer companyId = getParaToInt();
        // 角色列表
        List<Record> roleList = AuthorityService.getRoleByCompanyIdNotAuthorized(companyId);

        renderJson(roleList);
    }
    
    /**
	 * @desc:批量删除
	 * @author xuhui
	 */
	public void delete(){
		String ids = getPara(0);
		System.out.println(ids);
		boolean result = AuthorityService.delete(ids);
		renderJson(result);
	}
    
    
}
