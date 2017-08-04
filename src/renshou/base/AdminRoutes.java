package renshou.base;
import com.jfinal.config.Routes;

import renshou.leasewarehouse.in.LeaseInController;
import renshou.leasewarehouse.inventory.LeaseInventoryController;
import renshou.leasewarehouse.out.LeaseOutController;
import renshou.login.LoginController;
import renshou.system.authority.AuthorityController;
import renshou.system.button.ButtonController;
import renshou.system.menu.MenuController;
import renshou.system.role.RoleController;
import renshou.system.user.UserController;



/**
* @ClassName: AdminRoutes
* @Description: 配置后端路由（供管理系统）
* @author: Liyu
* @date: 2017年5月12日 下午1:21:20
* @version: 1.0 版本初成
 */
public class AdminRoutes extends Routes{
	/**
	 *@desc 配置后端路由
	 *@date 2017/05/12 
	 */
	@Override
	public void config() {
		// 设置页面base路径
		setBaseViewPath("/pages");
		// 用户登录控制器
		add("/pages",LoginController.class,"");
		
		
		//系统管理-角色管理控制器
		add("/system/role",RoleController.class,"/systemcontrol");
		//系统管理-用户管理控制器
		add("/system/user",UserController.class,"/systemcontrol");
		//系统管理-菜单管理控制器
		add("/system/menu",MenuController.class,"/systemcontrol");
		//系统管理-按钮管理控制器
		add("/system/button",ButtonController.class,"/systemcontrol");
		//系统管理-权限管理控制器
		add("/system/authority",AuthorityController.class,"/systemcontrol");

		//租赁仓库-入库
        add("/leasewarehouse/leaseIn",LeaseInController.class,"/leasewarehouse");
        //租赁仓库-出库
        add("/leasewarehouse/leaseOut",LeaseOutController.class,"/leasewarehouse");
        //租赁仓库-库存
        add("/leasewarehouse/leaseInventory",LeaseInventoryController.class,"/leasewarehouse");
	}
}
