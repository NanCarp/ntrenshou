package renshou.database.semimanufactures;

import java.sql.SQLException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class SemimanufactureService {

	/**
	 * @author xuhui
	 * @param pageNumber
	 * @param pageSize
	 * @param trade_name
	 * @return
	 */
	public static Page<Record> getSemimanufacture(Integer pageNumber,Integer pageSize,String trade_name){
		String sql = " from semimanufactures where 1=1";
		sql += " and trade_name like '%"+trade_name+"%'";
		return Db.paginate(pageNumber, pageSize, "SELECT *",sql);
	}
	
	/**
	 * @author xuhui
	 * @desc 根据id查询单条数据
	 */
	public static Record getSingleSemimanufactures(Integer id){
		return Db.findById("semimanufactures", id);
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
					result = Db.deleteById("semimanufactures", "id", id);		
					}
				return result;
			}
		});
		return flag;
	}
	
	/**
	 *@desc 自动生成半成品编号,获取最大的编号
	 *@author xuhui
	 */
	public static Record getMaxNum(){
		String sql = "select MAX(semimanufactures_number) as num from semimanufactures";
		return Db.findFirst(sql);
	}
}
