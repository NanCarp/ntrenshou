package renshou.system.menu;

import java.sql.SQLException;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
/**
 * @ClassName: MenuService
 * @Description: 系统管理_菜单管理
 * @author: xuhui
 * @date: 2017年5月15日下午2:00:00
 * @version: 1.0 版本初成
 */
public class MenuService {
	/** 
	* @Title: getMenuList 
	* @Description: 获取菜单列表
	* @return List<Record>
	* @throws 
	*/
	public static List<Record> getMenuList() {
		String sql = " SELECT a.*,b.module_name AS pname "
				+ " FROM t_menu AS a "
				+ " LEFT JOIN t_menu AS b "
				+ " ON a.pid = b.id order by id desc";
		return Db.find(sql);
	}

	/**
	 * @desc 根据id批量删除操作
	 * @author xuhui
	 */
	public static boolean delete(String ids){
		String[] allid = ids.split(",");	
		boolean flag = Db.tx(new IAtom() {
			boolean result = true;
			@Override
			public boolean run() throws SQLException {
				// TODO Auto-generated method stub
				for(String id:allid){
					result = Db.deleteById("t_menu", "id", id);		
					}
				return result;
			}
		});
		return flag;
	}
	
	/** 
	* @Title: getMenuListByParams 
	* @Description: 根据条件获取菜单列表
	* @param params
	* @return List<Record>
	* @throws 
	*/
	public static List<Record> getMenuListByParams(String params) {
		String sql = " SELECT * FROM `t_menu` WHERE 1=1 ";
		sql += params;
		return Db.find(sql);
	}

	// 菜单列表，ztree 使用
    public static List<Record> getMenuListForZTree() {
        String sql = " SELECT id ,pid AS pId, module_name AS `name`  FROM t_menu " +
                "UNION " +
                "SELECT  button_id AS id, menu_id AS pId, button_name AS `name` FROM t_button " +
                "UNION " +
                "SELECT 0,99999, '权限列表' ";
        return Db.find(sql);
    }

    /** 
    * @Title: isDuplicate 
    * @Description: 重复检测
    * @param menuName
    * @param pid
    * @return boolean
    * @author liyu
    */
    public static boolean isDuplicate(String menuName, Integer pid) {
        return Db.find("SELECT * FROM t_menu WHERE module_name = ? AND pid = ?", 
                menuName, pid).size() > 0;
    }
}
