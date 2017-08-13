package renshou.privatewarehouses.semiin;

import java.sql.SQLException;
import java.util.List;

import org.omg.PortableInterceptor.INACTIVE;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author xuhui
 * @desc 自用仓库管理 - 半成品入库管理
 */
public class SemiInService {

	/**
	 * @author xuhui
	 * @param pageNumber
	 * @param pageSize
	 * @param storage_number
	 * @param user_name
	 * @return Page<Record>
	 */
	public static Page<Record> getSemiIn(Integer pageNumber,Integer pageSize,String storage_number ,String user_name){
		String sql = " from semimanufactures_storage s LEFT JOIN t_user u ON s.t_user_id = u.id where 1=1";
		if(storage_number!=null&&storage_number!=""){
			sql +=" and storage_number like '%"+storage_number+"%'";
		}
		if(user_name!=null&&user_name!="")
		{
			sql +=" and user_name like '%"+user_name+"%'";
		}
		return Db.paginate(pageNumber, pageSize, "SELECT s.*,u.user_name",sql);
	}
	
	/**
	 * @desc 找到最子级仓库
	 * @author xuhui
	 */
	public static List<Record> getSublevel(){
		String sql ="SELECT w.id,w.warehouse_name,w.pid,a.id as aid,a.warehouse_name as awarehouse_name,r.id as rid"
				+ ",r.warehouse_name as rwarehouse_name FROM warehouse w LEFT JOIN warehouse a ON w.pid = a.id LEFT JOIN"
				+ " warehouse r ON r.id = a.pid where r.id IS NOT NULL";
		return Db.find(sql); 
	}
	
	/**
	 * @desc 查询所有半成品
	 * @author xuhui
	 */
	public static List<Record> getSemimanufactures(){
		String sql ="SELECT * FROM semimanufactures";
		return Db.find(sql);	
	}
	
	/**
	 * @desc 根据商品编号查询商品信息
	 * @author xuhui
	 */
	public static Record getSemimanufacturesByNum(String num){
		String sql = "SELECT semimanufactures_number,trade_name,specifications,measurement_unit"
				+ " from semimanufactures where semimanufactures_number = '"+num+"'";
		return Db.findFirst(sql);
	}
	
	/**
	 * @author xuhui
	 * @desc 根据日期录入入库单号
	 */
	public static Record getStroageDate(String Mdate){
		String sql = "SELECT MAX(storage_number) as maxnum from semimanufactures_storage WHERE"
				+ " storage_number like '"+Mdate+"%'";
		return Db.findFirst(sql);
	}
	
	/**
	 * @author xuhui
	 * @desc 获取入库单信息并保存
	 */
	public static boolean saveSemiIn(String storage_number,String list,Integer id){
		boolean flag = false;
		flag = Db.tx(new IAtom() {
			List<JSONObject> jlist = (List<JSONObject>)JSONObject.parse(list);
			boolean r = false;
			@Override
			public boolean run() throws SQLException {
				Integer rukuid = id;
				// TODO Auto-generated method stub
				if(rukuid!=null){// id不为空更新已经入库数据
					//id不为空，删除该入库单下全部数据，重新录入
					String sql= "delete from semimanufactures_storage_detail where semimanufactures_storage_id ="+rukuid;
					Db.update(sql);
					for(JSONObject obj:jlist){
						//Integer semimanufactures_storage_id = id; 						
						String semimanufactures_number = obj.getString("semimanufactures_number");
						String sqlMe = "SELECT id from semimanufactures WHERE semimanufactures_number = '"+semimanufactures_number+"'";
						Integer semimanufactures_id = Db.findFirst(sqlMe).getInt("id");//半成品id
						Double num = obj.getDouble("num");//入库数量
						Integer warehouse_id = obj.getInteger("warehouse_id");//对应仓库
						Record re = new Record();					
						re.set("semimanufactures_storage_id", rukuid);
						re.set("semimanufactures_id", semimanufactures_id);
						re.set("num", num);
						re.set("warehouse_id", warehouse_id);
						r = Db.save("semimanufactures_storage_detail", re);
						
					}
				}else{
					Record record = new Record();
					record.set("storage_number", storage_number);
					r = Db.save("semimanufactures_storage", record);
					//判断是否保存成功
					if(r){
						String sql="SELECT id from semimanufactures_storage WHERE storage_number= '"+storage_number+"'";
						//已存入入库单id
						Integer sid = Db.findFirst(sql).getInt("id");
						for(JSONObject obj:jlist){
							//Integer semimanufactures_storage_id = sid; 
							String semimanufactures_number = obj.getString("semimanufactures_number");
							String sqlMe = "SELECT id from semimanufactures WHERE semimanufactures_number = '"+semimanufactures_number+"'";
							Integer semimanufactures_id = Db.findFirst(sqlMe).getInt("id");
							Double num = obj.getDouble("num");
							Integer warehouse_id = obj.getInteger("warehouse_id");
							Record re = new Record();
							re.set("semimanufactures_storage_id", sid);
							re.set("semimanufactures_id", semimanufactures_id);
							re.set("num", num);
							re.set("warehouse_id", warehouse_id);
							Db.save("semimanufactures_storage_detail", re);
							
						}
					}	
				}
				return r;
			}
		});
		return flag;
	}
	
	/**
	 * @author xuhui
	 * @desc 根据id查询
	 */
	public static Record getSemi(Integer id){
		String sql = "SELECT s.*,u.user_name from semimanufactures_storage s"
				+ " LEFT JOIN t_user u ON s.t_user_id = u.id where s.id="+id;
		return Db.findFirst(sql);
	}
	
	/**
	 * @desc 根据id查询入库单号下所有信息
	 * @author xuhui
	 */
	public static List<Record> getProduct(Integer id){
		String sql = "select semimanufactures_id from semimanufactures_storage_detail where semimanufactures_storage_id ="+id;
		return Db.find(sql);
	}
	
	/**
	 * @desc 根据入库单id查询明确入库单信息
	 * @author xuhui
	 */
	public static List<Record> getSeimIn(Integer id){
		String sql ="SELECT s.id,s.semimanufactures_storage_id,s.semimanufactures_id,s.num,s.warehouse_id"
				+ ",m.semimanufactures_number,m.trade_name,m.specifications,m.measurement_unit from"
				+ " semimanufactures_storage_detail s LEFT JOIN semimanufactures m ON s.semimanufactures_id = m.id"
				+ " WHERE s.semimanufactures_storage_id ="+id;
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
					result =Db.deleteById("semimanufactures_storage", "id", id);
					Db.update("delete from semimanufactures_storage_detail where semimanufactures_storage_id = ?", id);	
					}
				return result;
			}
		});
		return flag;
	}
	
	/**
	 * @desc 根据入库单id找到入库单明细信息
	 * @author xuhui
	 */
	public  static List<Record> getDetail(Integer semimanufactures_storage_id){
		String sql = "select * from semimanufactures_storage_detail where semimanufactures_storage_id="+semimanufactures_storage_id;	
		return Db.find(sql);
	}
	
	/**
	 * @desc 确认入库单
	 * @author xuhui
	 */
	public static boolean conform(Integer id){
		boolean flag = false;
		flag = Db.tx(new IAtom() {		
			@Override
			public boolean run() throws SQLException {
				// TODO Auto-generated method stub
				//根据入库单id找到入库单明细信息
				List<Record> list = SemiInService.getDetail(id);
				//遍历循环该入库单中的对应仓库，比较该对应仓库是否有对应商品，没有则新增，有则叠加
				for(Record r:list){
					Integer	warehouse_id = r.get("warehouse_id");
					Integer semimanufactures_id = r.getInt("semimanufactures_id");
					Double num = r.getBigDecimal("num").doubleValue();
					//寻找对应半成品仓库中是否有该货物存储数量，如果有将追加，如果没有则新增
					String sqlstockdetail ="SELECT * from semimanufactures_stock_detail WHERE warehouse_id= "+warehouse_id
							+" and semimanufactures_id = "+semimanufactures_id;
					Record r1 = Db.findFirst(sqlstockdetail);
					System.out.println(r1);
					//判断库存明细表是否保存成功
					boolean semistockflag = false;
					//r1为空，则表示该对应仓库下没有该半成品，否则说明该对用仓库下有该半成品
					if(r1!=null){
						Integer id = r1.getInt("id");
						Double senum = r1.getBigDecimal("num").doubleValue();
						senum += num;
						Record r2 = new Record();
						r2.set("id", id);
						r2.set("num", senum);
						semistockflag = Db.update("semimanufactures_stock_detail", r2);
					}else{
						Record r2 = new Record();
						r2.set("warehouse_id", warehouse_id);
						r2.set("semimanufactures_id", semimanufactures_id);
						r2.set("num",num);
						semistockflag = Db.save("semimanufactures_stock_detail", r2);
					}
					//查询出该半成品的在库存明细表的总数，保存到库存表中
					if(semistockflag){
						String s1 = "SELECT semimanufactures_id,SUM(num) as totalnum from semimanufactures_stock_detail"
								+ " WHERE semimanufactures_id = "+semimanufactures_id;
						Double totalnum = Db.findFirst(s1).getBigDecimal("totalnum").doubleValue();
						Integer semi_id = Db.findFirst(s1).getInt("semimanufactures_id");
						String s2 ="UPDATE semimanufactures_stock SET semimanufactures_stock_num = "+totalnum
								+" WHERE semimanufactures_id = "+semi_id;
						Db.update(s2);
					}
				}
				Record record = new Record();
				record.set("id", id);
				record.set("state", 1); 	
				return Db.update("semimanufactures_storage", record);
			}
		});
		
		return flag;
	}
}
