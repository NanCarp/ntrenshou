package renshou.database.storage;

import java.sql.SQLException;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author xuhui
 * @desc 仓库管理
 */
public class StorageService {

	/**
	 * @author xuhui
	 * @param pageNumber
	 * @param pageSize
	 * @return Page<Record>
	 */
	public static Page<Record> getStorage(Integer pageNumber,Integer pageSize,String warehouse_name){
		String sql = " from warehouse w LEFT JOIN warehouse e ON w.pid = e.id where 1=1";
		sql +=" and w.warehouse_name like '%"+warehouse_name+"%'";
		return Db.paginate(pageNumber, pageSize,"SELECT w.id,w.warehouse_name,w.position,e.warehouse_name as p_warehouse_name",sql);
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
					result = Db.deleteById("warehouse", "id", id);		
					}
				return result;
			}
		});
		return flag;
	}
	
	/**
	 * @desc 获取所有父级仓库
	 * @author xuhui
	 */
	public static List<Record> getCorrwarehouse(){
		String sql = "SELECT id,warehouse_name,pid,ppid FROM (SELECT w.id,w.warehouse_name"
				+ ",w.pid,e.pid as ppid from warehouse w LEFT JOIN warehouse e ON w.pid = e.id)"
				+ " k where ppid IS NULL or ppid = 0 UNION SELECT 0 as id,'一级仓库' as warehouse_name"
				+ ",9999 as pid,99999 as ppid from DUAL ORDER BY id";
		return Db.find(sql);
	}
	
	/**
	 * @desc 根据id获取单条仓库信息
	 * @author xuhui
	 */
	public static Record getSingleWarehouse(Integer id){
		return Db.findById("warehouse", id);
	}
}
