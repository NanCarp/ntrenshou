package renshou.database.semimanufactures;

import java.sql.SQLException;
import java.util.List;

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
	
	/**
	 * @desc 判断是否有该成品编号
	 * @author xuhui
	 */
	public static Record Judge(String semimanufactures_number){
		String sql = "select * from semimanufactures where semimanufactures_number ='"+semimanufactures_number+"'";
		return Db.findFirst(sql);
	}
	
	/**
	 * @desc 判断该半成品是否有后续业务产生
	 * @author xuhui
	 */
	public static boolean deleteOr(String ids){
		String[] addid = ids.split(",");
		boolean flag = false;
		for(String s:addid){
			Integer sid = Integer.parseInt(s);
			String sql = "select * from semimanufactures_storage_detail where semimanufactures_id ="+sid;
			Record record = Db.findFirst(sql);
 			if(record!=null){
 				flag = true;
 			} 
		}	
		return flag;
	}
	
}
