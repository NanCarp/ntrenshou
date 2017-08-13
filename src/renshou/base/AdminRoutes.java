package renshou.base;
import com.jfinal.config.Routes;


import renshou.database.barcode.BarCodeController;
import renshou.database.customer.CustomController;
import renshou.database.finishedproduct.FinishedProductController;
import renshou.database.semimanufactures.SemimanufacturesController;
import renshou.database.storage.StorageController;

import renshou.leaseprice.in.LeaseInPriceController;
import renshou.leaseprice.inventory.LeaseInventoryPriceController;
import renshou.leaseprice.out.LeaseOutPriceController;
import renshou.leasewarehouse.in.LeaseInController;
import renshou.leasewarehouse.inventory.LeaseInventoryController;
import renshou.leasewarehouse.out.LeaseOutController;

import renshou.login.LoginController;
import renshou.privatewarehouses.finishedin.FinishedInController;
import renshou.privatewarehouses.finishedout.FinishedOutController;
import renshou.privatewarehouses.finishedstock.FinishedStockController;
import renshou.privatewarehouses.semiin.SemiInController;
import renshou.privatewarehouses.semiout.SemiOutController;
import renshou.privatewarehouses.semistock.SemiStockController;
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
		//基础信息管理-成品信息管理
		add("/database/finishedproduct",FinishedProductController.class,"/database");
		//基础信息管理-半成品信息管理
		add("/database/semimanufactures",SemimanufacturesController.class,"/database");
		//基础信息管理-条形码管理
		add("/database/barcode",BarCodeController.class,"/database");
		//基础信息管理-仓库管理
		//add("/database/storage",StorageController.class,"/database");
		//基础信息管理-公司管理
		add("/database/customes",CustomController.class,"/database");
		
		//自用仓库管理 - 半成品入库管理
		add("/private/semiin",SemiInController.class,"/privatewarehouses");
		//自用仓库管理 - 半成品出库管理
		add("/private/semiout",SemiOutController.class,"/privatewarehouses");
		//自用仓库管理 - 库存管理
		add("/private/semistock",SemiStockController.class,"/privatewarehouses");
		
		//自用仓库管理 - 成品入库管理
        add("/private/finishedin",FinishedInController.class,"/privatewarehouses");
        //自用仓库管理 - 成品出库管理
        add("/private/finishedout",FinishedOutController.class,"/privatewarehouses");
        //自用仓库管理 - 成品库存管理
        add("/private/finishedstock",FinishedStockController.class,"/privatewarehouses");
		
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
        
        //租赁费用-入库
        add("/leaseprice/leaseInPrice",LeaseInPriceController.class,"/leaseprice");
        //租赁费用-出库
        add("/leaseprice/leaseOutPrice",LeaseOutPriceController.class,"/leaseprice");
        //租赁费用-库存
        add("/leaseprice/leaseInventoryPrice",LeaseInventoryPriceController.class,"/leaseprice");

	}
}
