package renshou.system.button;

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
 * @ClassName: ButtonController
 * @Description: 系统管理_按钮管理
 * @author: xuhui
 * @date: 2017年8月3日下午2:00:00
 * @version: 1.0 版本初成
 */
@Before(ManageInterceptor.class)
public class ButtonController extends Controller {
	 /**
     * @Title: button
     * @Description: 按钮列表
     */
    public void index(){
        // 按钮列表
        List<Record> buttonList = ButtonService.getButtonList();
        setAttr("buttonList", buttonList);
        render("button.html");
    }

    /**
     * @Title: getButton
     * @Description: 获取单条按钮数据
     */
    public void getButton() {
        // 按钮 id
        Integer id = getParaToInt();

        if (id != null) {//编辑
            Record button = Db.findById("t_button", id);
            setAttr("button", button);
        }

        // 父级菜单列表
        String params = " AND pid != 0";
        List<Record> parentMenuList = ButtonService.getMenuListByParams(params);
        setAttr("parentMenuList", parentMenuList);

        render("button_detail.html");
    }

    /**
     * @Title: saveButton
     * @Description: 保存按钮
     */
    public void saveButton() {
        // 按钮 id
        Integer id = getParaToInt("id");
        // 按钮 button_id
        Integer buttonId = getParaToInt("buttonId");
        // 按钮名称
        String buttonName = getPara("buttonName").trim();
        // 父级菜单 id
        Integer pid = getParaToInt("pid", 0);
        // 当前时间
        Date now = new Date();
        // 保存结果
        boolean result = false;
        // 返回信息
        Map<String, Object> response = new HashMap<>();
        // 重复检测
        if (id == null && ButtonService.isDuplicate(buttonId)) {
            response.put("tips", "按钮ID重复！");
            response.put("isSuccess", false);
            renderJson(response);
            return;
        }
        
        Record record = new Record();
        record.set("button_name", buttonName);
        record.set("button_id", buttonId);
        record.set("menu_id", pid);
        record.set("review_time", now);// 修改时间
        if (id != null) {// 编辑
            record.set("id", id);
            result = Db.update("t_button", record);
            response.put("isSuccess", result);
            response.put("tips", result ? "保存成功": "保存失败");
        } else {// 新增
            record.set("create_time", now);
            result = Db.save("t_button", record);
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
		boolean result = ButtonService.delete(ids);
		renderJson(result);
	}
}
