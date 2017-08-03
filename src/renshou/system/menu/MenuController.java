package renshou.system.menu;

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
 * @ClassName: MenuController
 * @Description: 系统管理_菜单管理
 * @author: xuhui
 * @date: 2017年5月15日下午2:00:00
 * @version: 1.0 版本初成
 */
@Before(ManageInterceptor.class)
public class MenuController extends Controller {
	/** 
	* @Title: menu 
	* @Description: 菜单列表
	*/
	public void index(){
		// 菜单列表
		List<Record> menuList = MenuService.getMenuList();
		setAttr("menuList", menuList);
		render("menu.html");
	}
	
	/** 
	* @Title: getMenu 
	* @Description: 获取单条菜单数据 
	*/
	public void getMenu() {
		// 菜单 id
		Integer id = getParaToInt();
		
		if (id != null) {//编辑
			Record menu = Db.findById("t_menu", id);
			setAttr("menu", menu);
		}
		
		// 父级菜单列表
		String params = " AND pid = 0";
		List<Record> parentMemuList = MenuService.getMenuListByParams(params);
		setAttr("parentMenuList", parentMemuList);
		
		render("menu_detail.html");
	}
	
	/** 
	* @Title: saveMenu 
	* @Description: 保存菜单
	*/
	public void saveMenu() {
		// 菜单 id
		Integer id = getParaToInt("id");
		// 菜单名称
		String menuName = getPara("menuName").trim();
		// 父级菜单 id
		Integer pid = getParaToInt("pid", 0);
		// 菜单路径
		String url = getPara("url").trim();
        // 图标
        String icon = getPara("icon").trim();
		// 备注
		String remark = getPara("remark", "");
		// 保存结果
		boolean result = false;
		// 返回信息
        Map<String, Object> response = new HashMap<>();
        // 重复检测
        if (id == null && MenuService.isDuplicate(menuName, pid)) {
            response.put("tips", "菜单重复！");
            response.put("isSuccess", false);
            renderJson(response);
            return;
        }
		
		Record record = new Record();
		record.set("module_name", menuName);
		record.set("pid", pid);
		record.set("url", url);
		record.set("icon", icon);
		record.set("remark", remark);
		if (id != null) {// 编辑
			record.set("id", id);
			result = Db.update("t_menu", record);
			response.put("isSuccess", result);
            response.put("tips", result ? "保存成功": "保存失败");
		} else {// 新增
			result = Db.save("t_menu", record);
			response.put("isSuccess", result);
            response.put("tips", result ? "保存成功": "保存失败");
		}
		
		renderJson(response);
	}
	
	/**
	 * @desc:批量删除
	 * @author xuhui
	 */
	public void delete(){
		String ids = getPara(0);
		boolean result = MenuService.delete(ids);
		renderJson(result);
	}

}
