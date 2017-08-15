package renshou.privatewarehouses.finishedin;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * @author liyu
 * @desc 自用仓库管理 - 成品入库管理
 */
public class FinishedInService {

	/**
	 * @author liyu
	 * @param pageNumber
	 * @param pageSize
	 * @param storage_number
	 * @param user_name
	 * @return Page<Record>
	 */
	public static Page<Record> getFinishedIn(Integer pageNumber,Integer pageSize,String storage_number ,String user_name){
		String sql = " from finished_product_storage s LEFT JOIN t_user u ON s.t_user_id = u.id where 1=1";
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
	 * @author liyu
	 */
/*	public static List<Record> getSublevel(){
		String sql ="SELECT w.id,w.warehouse_name,w.pid,a.id as aid,a.warehouse_name as awarehouse_name,r.id as rid"
				+ ",r.warehouse_name as rwarehouse_name FROM warehouse w LEFT JOIN warehouse a ON w.pid = a.id LEFT JOIN"
				+ " warehouse r ON r.id = a.pid where r.id IS NOT NULL";
		return Db.find(sql); 
	}*/
    /** 
    * @Title: getWarehouseList 
    * @Description: 仓库列表
    * @return List<Record>
    * @author liyu
    */
    public static List<Record> getWarehouseList() {
        String sql = "SELECT a.id, "
                + " CONCAT(c.warehouse_name,b.warehouse_name,a.warehouse_name) AS warehouse_name "
                + " FROM `warehouse` AS a "
                + " INNER JOIN warehouse AS b "
                + " ON a.pid = b.id "
                + " INNER JOIN warehouse AS c "
                + " ON b.pid = c.id ";
        return Db.find(sql);
    }
	
	/**
	 * @desc 查询所有成品
	 * @author liyu
	 */
	public static List<Record> getFinishedProducts(){
		String sql ="SELECT * FROM finished_product";
		return Db.find(sql);	
	}
	
	/**
	 * @desc 根据商品编号查询商品信息
	 * @author liyu
	 */
	public static Record getFinishedByNum(String num){
		String sql = "SELECT finished_number,trade_name,specifications,measurement_unit"
				+ " from finished_product where finished_number = '"+num+"'";
		return Db.findFirst(sql);
	}
	
	/**
	 * @author liyu
	 * @desc 根据日期录入入库单号
	 */
	public static Record getStroageDate(String Mdate){
		String sql = "SELECT MAX(storage_number) as maxnum from finished_product_storage WHERE"
				+ " storage_number like '"+Mdate+"%'";
		return Db.findFirst(sql);
	}
	
	/**
	 * @author liyu
	 * @desc 获取入库单信息并保存
	 */
	public static boolean saveFinishedIn(String storage_number,String list,Integer id, Integer t_user_id){
		boolean flag = false;
		flag = Db.tx(new IAtom() {
			List<JSONObject> jlist = (List<JSONObject>)JSONObject.parse(list);
			boolean r = false;
			@Override
			public boolean run() throws SQLException {
				Integer rukuid = id;
				if(rukuid!=null){// id不为空更新已经入库数据
					//id不为空，删除该入库单下全部数据，重新录入
					String sql= "delete from finished_product_storage_detail where finished_product_storage_id ="+rukuid;
					Db.update(sql);
					for(JSONObject obj:jlist){
						//Integer finished_product_storage_id = id; 						
						String finished_number = obj.getString("finished_number");
						String sqlMe = "SELECT id from finished_product WHERE finished_number = '"+finished_number+"'";
						Integer finished_product_id = Db.findFirst(sqlMe).getInt("id");
						Double num = obj.getDouble("num");
						Integer warehouse_id = obj.getInteger("warehouse_id");
						Record re = new Record();					
						re.set("finished_product_storage_id", rukuid);
						re.set("finished_product_id", finished_product_id);
						re.set("num", num);
						re.set("warehouse_id", warehouse_id);
						System.out.println(re);
						r = Db.save("finished_product_storage_detail", re);
					}
				}else{ // id 为空，新增入库单
					Record record = new Record();
					record.set("storage_number", storage_number); // 入库单号
					record.set("storage_time", new Date()); // 入库日期
					record.set("t_user_id", t_user_id); // 入库人
					r = Db.save("finished_product_storage", record);
					//判断是否保存成功
					if(r){
						// String sql="SELECT id from finished_product_storage WHERE storage_number= '"+storage_number+"'";
						//已存入入库单id
						//Integer sid = Db.findFirst(sql).getInt("id");
					    Long sid = record.getLong("id");
						for(JSONObject obj:jlist){
							//Integer finished_product_storage_id = sid; 
							String finished_number = obj.getString("finished_number");
							String sqlMe = "SELECT id from finished_product WHERE finished_number = '"+finished_number+"'";
							Integer finished_product_id = Db.findFirst(sqlMe).getInt("id");
							Double num = obj.getDouble("num");
							Integer warehouse_id = obj.getInteger("warehouse_id");
							Record re = new Record();
							re.set("finished_product_storage_id", sid);
							re.set("finished_product_id", finished_product_id);
							re.set("num", num);
							re.set("warehouse_id", warehouse_id);
							Db.save("finished_product_storage_detail", re);
						}
					}	
				}
				return r;
			}
		});
		return flag;
	}
	
	/**
	 * @author liyu
	 * @desc 根据id查询
	 */
	public static Record getFinished(Integer id){
		String sql = "SELECT s.*,u.user_name from finished_product_storage s"
				+ " LEFT JOIN t_user u ON s.t_user_id = u.id where s.id="+id;
		return Db.findFirst(sql);
	}
	
	/**
	 * @desc 根据id查询入库单号下所有信息
	 * @author liyu
	 */
	public static List<Record> getProduct(Integer id){
		String sql = "select finished_product_id from finished_product_storage_detail where finished_product_storage_id ="+id;
		return Db.find(sql);
	}
	
	/**
	 * @desc 根据入库单id查询明确入库单信息
	 * @author liyu
	 */
	public static List<Record> getFinishedIn(Integer id){
		String sql ="SELECT s.id,s.finished_product_storage_id,s.finished_product_id,s.num,s.warehouse_id"
				+ ",m.finished_number,m.trade_name,m.specifications,m.measurement_unit from"
				+ " finished_product_storage_detail s LEFT JOIN finished_product m ON s.finished_product_id = m.id"
				+ " WHERE s.finished_product_storage_id ="+id;
		return Db.find(sql);
	}
	
	/**
	 * @desc 根据id批量删除操作
	 * @author liyu
	 */
	public static boolean delete(String ids){
		String[] allid = ids.split(",");	
		boolean flag = Db.tx(new IAtom() {
			boolean result = true;
			@Override
			public boolean run() throws SQLException {
				for(String id:allid){
					result =Db.deleteById("finished_product_storage", "id", id);
					Db.update("delete from finished_product_storage_detail where finished_product_storage_id = ?", id);	
					}
				return result;
			}
		});
		return flag;
	}
	
	/**
	 * @desc 确认入库单
	 * @author liyu
	 */
	public static boolean conform(Integer id){
	    boolean flag = Db.tx(new IAtom() {
            boolean result = true;
            @Override
            public boolean run() throws SQLException {
                
                // 入库单状态修改为已入库
                Record record = new Record();
                record.set("id", id);
                record.set("state", 1);
                Db.update("finished_product_storage", record);
                
                // 更新库存
                // 入库产品列表
                List<Record> storageProductList = Db.find("SELECT * "
                        + " FROM `finished_product_storage_detail` WHERE finished_product_storage_id = ?", id);
                
                // 查询库存对应产品，更新产品数量，没有则新建
                for (Record storageProduct : storageProductList) {
                    Integer product_id = storageProduct.getInt("finished_product_id"); // 产品 id
                    List<Record> stockProductList = Db.find("SELECT * FROM `finished_product_stock` WHERE finished_product_id = ? ", product_id);
                    if (stockProductList.size() > 0) { // 更新库存数量
                        Record stockProduct = stockProductList.get(0);
                        stockProduct.set("finished_product_stock_num", stockProduct.getBigDecimal("finished_product_stock_num").add(storageProduct.getBigDecimal("num")));
                        Db.update("finished_product_stock", stockProduct);
                    } else { // 新增库存
                        Record stockProduct = new Record();
                        stockProduct.set("finished_product_id", product_id);
                        stockProduct.set("finished_product_stock_num", storageProduct.getBigDecimal("num"));
                        Db.save("finished_product_stock", stockProduct);
                    }
                }
                
                // 更新 产品-仓库 对应数量，没有则新建
                for (Record storageProduct : storageProductList) {
                    Integer product_id = storageProduct.getInt("finished_product_id"); // 产品 id
                    Integer warehouse_id = storageProduct.getInt("warehouse_id"); // 仓库 id
                    List<Record> stockDetailList = Db.find("SELECT * FROM `finished_product_stock_detail` WHERE finished_product_id = ? AND warehouse_id = ? ", product_id, warehouse_id);
                    if (stockDetailList.size() > 0) { // 更新数量
                        Record stockDetail = stockDetailList.get(0);
                        stockDetail.set("num", stockDetail.getBigDecimal("num").add(storageProduct.getBigDecimal("num")));
                        Db.update("finished_product_stock_detail", stockDetail);
                    } else { // 新增
                        Record stockDetail = new Record();
                        stockDetail.set("finished_product_id", product_id);
                        stockDetail.set("warehouse_id", warehouse_id);
                        stockDetail.set("num", storageProduct.getBigDecimal("num"));
                        Db.save("finished_product_stock_detail", stockDetail);
                    }
                }
                
                return result;
            }
        });
		
		return flag;
		
	}

    /** 
    * @Title: getStorageNo 
    * @Description: 获取入库单号
    * @return String
    * @author liyu
    */
    public static String getStorageNo() {
        String no = "";
        Date date = new Date();
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(date);
        String Mdate = "YR"+today;
        Record record = FinishedInService.getStroageDate(Mdate);
        if(record.get("maxnum")!=null){
            String MMdate = record.get("maxnum");
            Integer mm = Integer.parseInt(MMdate.substring(10));
            Integer mmInteger = mm + 1001;
            String mmString = String.valueOf(mmInteger).substring(1);
            no = "YR"+today+mmString;
        }else{
            no = "YR"+today+"001";
        }
        
        return no;
    }
    
}
