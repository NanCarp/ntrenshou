package renshou.system.authority;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
/**
 * @ClassName: AuthorityService
 * @Description: 系统管理_权限管理
 * @author: xuhui
 * @date: 2017年5月15日下午2:00:00
 * @version: 1.0 版本初成
 */
public class AuthorityService {
	public static List<Record> getAuthorityList(String company,String role) {
        String sql = "SELECT a.*,b.id AS menus_id,c.id AS buttons_id " +
                "FROM t_role AS a " +
                "LEFT JOIN t_role_menu AS b " +
                "ON a.id = b.role_id " +
                "LEFT JOIN t_role_button AS c " +
                "ON a.id = c.role_id ";
        if(role!=""&&role!=null){
        	sql +=" and a.role_type like '%"+role+"%'";
        }
        return Db.find(sql);
    }

    // 保存权限
	public static boolean saveAuthority(Integer roleId, String authorityIds, Integer menusId, Integer buttonsId) {
         boolean succeed = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                String[] authorityList = new String[]{};
                String menus = "";
                String buttons = "";
                if(roleId == 1){// 超级管理员，获取所有权限
                    menus = Db.queryStr("SELECT CONCAT('0',',',GROUP_CONCAT(id)) FROM t_menu");
                    buttons = Db.queryStr("SELECT GROUP_CONCAT(button_id) FROM t_button");
                }
                authorityList = authorityIds.split(",");
                for(int i=0;i<authorityList.length;i++){
                    if(Integer.parseInt(authorityList[i]) < 100){
                        menus += authorityList[i] + ",";
                    }else{
                        buttons += authorityList[i] + ",";
                    }
                }
                menus = menus.substring(0, menus.length() - 1);
                buttons = buttons.substring(0, buttons.length() - 1);
                Date now = new Date();

                Record record1 = new Record();
                record1.set("role_id", roleId);
                record1.set("menu_ids", menus);
                boolean result1 = false;
                if (menusId != null) {// 更新 t_role_menu 数据
                    record1.set("id", menusId);
                    result1 = Db.update("t_role_menu", record1);
                } else {// 新增
                    result1 = Db.save("t_role_menu", record1);
                }

                Record record2 = new Record();
                record2.set("role_id", roleId);
                record2.set("button_ids", buttons);
                boolean result2 = false;
                if (buttonsId != null) {// 更新 t_role_button 数据
                    record2.set("id", buttonsId);
                    result2 = Db.update("t_role_button", record2);
                } else {// 新增
                    result2 = Db.save("t_role_button", record2);
                }


                return result1 && result2;
            }
        });
        return succeed;
	}

	// 根据角色 id  获取权限
    public static Record getMenusByRoleId(Integer roleId) {
        String sql = "SELECT a.*,CONCAT(b.menu_ids,',',c.button_ids) AS authority, " +
                "b.id AS menus_id,c.id AS buttons_id " +
                "FROM t_role AS a " +
                "LEFT JOIN t_role_menu AS b " +
                "ON a.id = b.role_id " +
                "LEFT JOIN t_role_button AS c " +
                "ON a.id = c.role_id " +
                "WHERE a.id = ? ";
        return Db.find(sql, roleId).get(0);
    }
    /**
     * @desc:获取公司列表
     * @return
     */
    public static List<Record> getCompanyList() {
		return Db.find(" SELECT * FROM `t_company` WHERE state = 1 ");
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
    // 根据公司 id 获取角色列表，剔除已分配权限的角色，
    public static List<Record> getRoleByCompanyIdNotAuthorized(Integer companyId) {
        String sql = "SELECT a.* " +
                "FROM t_role AS a " +
                "LEFT JOIN t_role_menu AS b " +
                "ON a.id = b.role_id " +
                "WHERE b.menu_ids IS NULL " +
                "AND a.company_id = ? ";
        return Db.find(sql, companyId);
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
					if(Integer.parseInt(id) == 1){
						result = false;
						break;
					}
					result = Db.deleteById("t_role_menu", "role_id", id);
					if(result){
						result = Db.deleteById("t_role_button", "role_id", id);
					}
				}
				return result;
			}
		});
		return flag;
	}
}
