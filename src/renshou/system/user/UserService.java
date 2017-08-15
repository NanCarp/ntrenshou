package renshou.system.user;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
/**
 * @ClassName: UserService
 * @Description: 系统管理_用户管理
 * @author: xuhui
 * @date: 2017年5月15日下午2:00:00
 * @version: 1.0 版本初成
 */
public class UserService {
	 // 用户管理列表
    public static List<Record> getUserList() {
        String sql = " SELECT a.*, b.company_name, c.role_type " +
                " FROM t_user AS a " +
                " LEFT JOIN t_company AS b " +
                " ON a.company_id = b.id " +
                " LEFT JOIN t_role AS c " +
                " ON a.role_id = c.id ";
        return Db.find(sql);
    }
    // 根据查询条件查询用户列表
    public static List<Record> getUserList(Map<String, Object> params) {
        // 公司名称
        String username = (String) params.get("username");
        String sql = " SELECT a.*, c.role_type " +
                " FROM t_user AS a " +
                " LEFT JOIN t_role AS c " +
                " ON a.role_id = c.id " +
                " WHERE 1=1 ";
        if (!"".equals(username)) {
            sql += " AND a.user_name like '%" + username +"%' ";
        }
        return Db.find(sql);
    }
    // 根据 id 获取用户
    public static Record getUserById(Integer id) {
        return Db.findById("t_user", id);
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
					for(String id:allid){
						if(Integer.parseInt(id) == 1){
							result = false;
							break;
						}
							result = Db.deleteById("t_user", "id", id);		
						}
					return result;
				}
			});
			return flag;
		}
    /**
     * @Title: getCompanyList
     * @Description: 获取公司列表
     * @return List<Record>
     * @throws
     */
    public static List<Record> getCompanyList() {
		String sql = "SELECT *  " +
				"FROM t_company " +
				"WHERE state = 1 " ;
		return Db.find(sql);
    }
    
    // 根据公司 id 获取角色列表
    public static List<Record> getRoleByCompanyId(Integer companyId) {
        return Db.find(" SELECT * FROM `t_role` WHERE company_id = ?", companyId);
    }
    /** 
    * @Title: isUserDuplicate 
    * @Description: 账号是否重复
    * @param account
    * @return boolean
    */
    public static boolean isUserDuplicate(String account) {
        
        return Db.find("SELECT * FROM t_user WHERE account = ?", account).size() > 0;
    }
    
    /** 
     * @Title: getRoleList 
     * @Description: 获取角色列表
     * @return List<Record>
     * @author liyu
     */
    public static List<Record> getRoleList() {
        String sql = " SELECT *  FROM t_role ";
        return Db.find(sql);
    }
     
    /** 
    * @Title: freezeOrEnable
    * @Description: 冻结或启用账号
    * @param id
    * @return boolean
    * @author liyu
    */
    public static boolean freezeOrEnable(Integer id) {
        Record record = Db.findById("t_user", id);
        record.set("state", !record.getBoolean("state"));
        return Db.update("t_user", record);
    }

    
}
