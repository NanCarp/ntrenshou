package renshou.system.role;

import java.sql.SQLException;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
/**
 * @ClassName: RoleService
 * @Description: 系统管理_角色管理
 * @author: xuhui
 * @date: 2017年5月15日下午2:00:00
 * @version: 1.0 版本初成
 */
public class RoleService {
	  public static List<Record> getRoleList(String rolename,String department) {
	        String sql = " SELECT * " +
	                " FROM t_role WHERE 1=1 ";
	        if(rolename!=""&&rolename!=null){
	        	sql += " and role_type like '%"+rolename+"%'";
	        }
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
						if(Integer.parseInt(id) == 1){
							result = false;
							break;
						}
						//删除与角色对应用户
						List<Record> list = Db.find("select id from t_user where role_id=?", id);
						for(Record record:list){
							Db.update("delete from t_user where id = ?",record.getInt("id"));
						}
							result = Db.deleteById("t_role", "id", id);		
						}
					return result;
				}
			});
			return flag;
		}
	    
	    // 根据公司 id 获取角色列表
	    public static List<Record> getRoleByCompanyId(Integer companyId) {
	        return Db.find(" SELECT * FROM `t_role` WHERE company_id = ?", companyId);
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


        /** 
        * @Title: isDuplicate 
        * @Description: 重复检测
        * @param role
        * @param companyId
        * @return boolean
        * @author liyu
        */
        public static boolean isDuplicate(String role) {
            return Db.find("SELECT * FROM t_role WHERE role_type = ? ", role).size() > 0;
        }
}
