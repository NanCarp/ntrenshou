package renshou.login;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * @ClassName: LoginService.java
 * @Description:
 * @author: LiYu
 * @date: 2017年6月12日上午11:13:58
 * @version: 1.0 版本初成
 */
public class LoginService {
    /*********************登陆管理*************************/
    // 根据角色 id ，获取菜单列表
    public static List<Record> getMenusByRoleId(Integer roleId) {
        // roleId 对应的菜单权限记录
        Record record = Db.find("SELECT * FROM t_role_menu WHERE role_id = ? ", roleId).get(0);
        // 菜单 ids
        String menuIds = record.getStr("menu_ids");
        // 菜单列表
        List<Record> menus = Db.find("SELECT * FROM `t_menu` WHERE FIND_IN_SET(id,?)", menuIds);

        return menus;
    }
    public static Record getLoginInfo(String username){
		return Db.findFirst("select * from t_user where account = ?",username);
	}
	
	public static boolean saveuserLogin(int user_id,String ip,String agent){

		return false;
	}

	// 根据用户名从 SAP 获取用户数据
    public static Record getUserFromSAP(String username) {
	    Record user = new Record();
	    // TODO 从 SAP 获取数据

        return null;
    }
    
	/**
	 * @desc 保存用户登录信息
	 * @param loginRecordMap
	 * @return boolean 
	 */
	public static boolean saveLoginMessage(Map<String, Object> loginRecordMap){
		Record record = new Record();
		Record record1=Db.findById("t_user",loginRecordMap.get("userid"));
		record.set("user_account", record1.getStr("account"));
		record.set("ip", loginRecordMap.get("userip"));
		record.set("agent", loginRecordMap.get("MoblieOrPc"));
		record.set("login_time",loginRecordMap.get("loginTime"));
		record.set("logout_time",new Date());
		System.out.println(loginRecordMap.get("userid"));
		return Db.save("t_user_log", record);
	}
	
	/**
	 * @desc 自用仓库库存查询
	 * @author xuhui
	 */
	public static Page<Record> getIframe(Integer pageNumber,Integer pageSize,String product_num,String trade_name){
		String sql ="from (SELECT e.semimanufactures_number as pronum,e.trade_name,e.specifications,e.measurement_unit"
				+ ",semimanufactures_stock_num,'半成品' as proflag from semimanufactures_stock s LEFT JOIN semimanufactures"
				+ " e ON e.id = s.semimanufactures_id UNION SELECT p.finished_number as pronum,p.trade_name,p.specifications"
				+ ",p.measurement_unit,f.finished_product_stock_num,'成品' as proflag from finished_product_stock f LEFT JOIN"
				+ " finished_product p ON p.id = f.finished_product_id) t where 1=1";
		if(product_num!=null&&product_num!=""){
			sql +=" and pronum like '%"+product_num+"%'";
		}
		if(trade_name!=null&&trade_name!=""){
			sql +=" and trade_name like'%"+trade_name+"%'";
		}
		return Db.paginate(pageNumber, pageSize, "select *",sql);
		
	}
}
