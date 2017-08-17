package renshou.privatewarehouses.semiout;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class SemiOutService {
	
    /** 
    * @Description: 半成品出库列表
    * @param pageindex
    * @param pagelimit
    * @param stock_number
    * @param user_name
    * @return Page<Record>
    * @author xuhui
    */
    public static Page<Record> getSemiOut(Integer pageindex, Integer pagelimit, String outgoing_number,
            String user_name) {
        String select = "SELECT a.*,b.company_name,c.user_name ";
                
        String sql = " FROM `semimanufactures_outgoing` AS a "
                + " LEFT JOIN company AS b "
                + " ON a.company_id = b.id "
                + " LEFT JOIN t_user AS c "
                + " ON a.t_user_id = c.id where 1=1";

        if (outgoing_number != null && outgoing_number != "") {
            sql += " and outgoing_number like '%" + outgoing_number + "%'";
        }
        if (user_name != null && user_name != "") {
            sql += " and user_name like '%" + user_name + "%'";
        }
        return Db.paginate(pageindex, pagelimit, select, sql);
    }

    /** 
     * @Title: getStockList 
     * @Description: 获得库存产品列表
     * @return List<Record>
     * @author xuhui
     */
     public static List<Record> getStockList() {
         String sql = " SELECT a.*,b.finished_number,b.trade_name,b.specifications,b.measurement_unit,b.remark "
                 + " FROM `semimanufactures_stock` AS a "
                 + " LEFT JOIN finished_product AS b "
                 + " ON a.semimanufactures_id = b.id ";
         
         return Db.find(sql);
     }

     /** 
     * @Title: getStockDetailByProductNo 
     * @Description: 根据产品编号查询某一产品在各个仓库的位置及数量
     * @param finished_number
     * @return List<Record>
     * @author xuhui
     */
     public static List<Record> getStockDetailByProductNo(String finished_number) {
         String sql = "SELECT * "
                 + " FROM `semimanufactures_stock_detail` AS a "
                 + " LEFT JOIN finished_product AS b "
                 + " ON a.semimanufactures_id = b.id "
                 + " WHERE finished_number = '"+ finished_number +"'";
         return Db.find(sql);
     }
    
    /** 
     * @Title: getSemiOut 
     * @Description: 根据出库单 id 查看出库单主表信息
     * @param id
     * @return Record
     * @author xuhui
     */
     public static Record getSemiOut(Long id) {
         String sql = "SELECT a.*,b.company_name,c.user_name "
                 + " FROM `semimanufactures_outgoing` AS a "
                 + " LEFT JOIN company AS b "
                 + " ON a.company_id = b.id "
                 + " LEFT JOIN t_user AS c "
                 + " ON a.t_user_id = c.id "
                 + " WHERE a.id = ? ";
         return Db.find(sql, id).get(0);
     }
     
     /** 
      * @Description: 根据出库单 id 查看出库单子表信息
      * @param id
      * @return List<Record>
      * @author xuhui
      */
      public static List<Record> getSemiOutDetailList(Long id) {
          String sql = "SELECT a.*,b.trade_name,b.semimanufactures_number,b.specifications,b.measurement_unit,b.remark,"
                  + " c.id AS stock_detail_id "
                  + " FROM `semimanufactures_outgoing_detail` AS a "
                  + " LEFT JOIN semimanufactures AS b "
                  + " ON a.semimanufactures_id = b.id "
                  + " LEFT JOIN semimanufactures_stock_detail AS c "
                  + " ON a.semimanufactures_id = c.semimanufactures_id AND a.warehouse_id = c.warehouse_id "
                  + " WHERE a.semimanufactures_outgoing_id = ? ";
          List<Record> list = Db.find(sql, id);
          // TODO 仓库名
          list = modifyWarehouseName(list);
          return list;
      }
      
      /** 
       * @Description: 获得库存产品列表 对应仓库
       * @return List<Record>
       * @author xuhui
     * @param trade_name 
     * @param finished_number 
       */
       public static List<Record> getStockDetailList(String semimanufactures_number, String trade_name) {
       	String sql = "SELECT a.id,a.warehouse_id,a.semimanufactures_id,a.num, "
                   + " c.semimanufactures_number,c.trade_name,c.specifications,c.measurement_unit,c.remark, "
                   + " IFNULL(num - b.total_out_quantity,num) AS left_quantity "
                   + " FROM semimanufactures_stock_detail AS a "
                   + " LEFT JOIN "
                   + " (SELECT b.warehouse_id,b.semimanufactures_id,SUM(num) AS total_out_quantity  "
                   + " FROM semimanufactures_outgoing AS a "
                   + " LEFT JOIN semimanufactures_outgoing_detail AS b "
                   + " ON a.id = b.semimanufactures_outgoing_id "
                   + " WHERE a.state = 0 "
                   + " GROUP BY warehouse_id, semimanufactures_id) AS b "
                   + " ON a.semimanufactures_id = b.semimanufactures_id AND a.warehouse_id = b.warehouse_id "
                   + " LEFT JOIN semimanufactures AS c ON "
                   + " a.semimanufactures_id = c.id "
                   + " WHERE IFNULL(num - b.total_out_quantity,num) > 0 ";   
       	
       	if(semimanufactures_number!=null && !"".equals(semimanufactures_number)) {
            sql += " AND c.semimanufactures_number like '%"+ semimanufactures_number +"%'";
        }
        
        if(trade_name!=null && !"".equals(trade_name)) {
            sql += " AND c.trade_name like '%"+ trade_name +"%'";
        }
        
        sql += " ORDER BY c.semimanufactures_number, a.warehouse_id ";
        
        List<Record> list = Db.find(sql);
        // 仓库名
        list = modifyWarehouseName(list);
        return list;

       }
       
       public static List<Record> getStockDetailList() {
           return getStockDetailList(null, null);
       }
       
       /** 
        * @Title: save 
        * @Description: 保存出库详细
        * @param semimanufactures_outgoing_id
        * @param outgoingDetailList
        * @param user_id
        * @return Map<String,Object>
        * @author xuhui
        */
        public static Map<String, Object> save(Long semimanufactures_outgoing_id, String outgoingDetailList,
                Integer user_id, Integer company_id,String remark) {
            
            // 返回信息
            Map<String, Object> message = new HashMap<>(); 
            
            boolean result = Db.tx(new IAtom() { 
                @Override
                public boolean run() throws SQLException {
                    // 出库产品列表
                    List<Record> recordList = jsonToRecordList(outgoingDetailList);
                    // 出库单 id
                    Long outgoing_id = semimanufactures_outgoing_id;
                    // 出库单
                    Record outgoing = new Record();
                    Date now = new Date(); // 当前日期
                    outgoing.set("outgoing_time", now); // 出库日期
                    outgoing.set("t_user_id", user_id); // 出库人
                    outgoing.set("company_id", company_id); // 客户 
                    outgoing.set("remark", remark); // 备注
                    
                    if(null != outgoing_id) { // 编辑
                        outgoing.set("id", outgoing_id);
                        Db.update("semimanufactures_outgoing", outgoing);
                    } else { // 新增
                        outgoing.set("outgoing_number", getWarehouseOutNo()); // 出库单号
                        Db.save("semimanufactures_outgoing", outgoing);
                        outgoing_id = outgoing.getLong("id");
                    }
                    
                    // 删除出库产品详情
                    List<Record> dbOutgoingDetail = Db.find("SELECT * FROM semimanufactures_outgoing_detail WHERE semimanufactures_outgoing_id = ?", outgoing_id);
                    if (dbOutgoingDetail.size() > 0) {
                        for (Record r : dbOutgoingDetail) {
                            Db.delete("semimanufactures_outgoing_detail", r);
                        }
                    }
                    
                    // 保存 
                    for (Record r : recordList) {
                        r.set("semimanufactures_outgoing_id", outgoing_id);
                        r.remove("id");
                        Db.save("semimanufactures_outgoing_detail", r);
                    }
                    
                    // 统计是否超出库存
                    // 库存产品列表 
                    List<Record> inventoryProductList = getInventory();
                    for(Record r : inventoryProductList) {
                        BigDecimal a = r.getBigDecimal("left_quantity");
                        // 如果余量为负，拒绝出库
                        if (a.compareTo(BigDecimal.ZERO) == -1) {
                            message.put("tips", "库存不足！");
                            return false;
                        }
                    }
                    
                    message.put("tips", "保存成功");
                    
                    return true;
                }
            });
            message.put("isSuccess", result);
            
            return message;
        }

        /** 
        * @Title: getInventory 
        * @Description: 实际库存减去未确认出库的余量
        * @return List<Record>
        * @author xuhui
        */
        protected static List<Record> getInventory() {
        	String sql = "SELECT a.id,a.warehouse_id,a.semimanufactures_id,a.num, "
                    + " c.semimanufactures_number,c.trade_name,c.specifications,c.measurement_unit,c.remark, "
                    + " IFNULL(num - b.total_out_quantity,num) AS left_quantity "
                    + " FROM semimanufactures_stock_detail AS a "
                    + " LEFT JOIN "
                    + " (SELECT b.warehouse_id,b.semimanufactures_id,SUM(num) AS total_out_quantity  "
                    + " FROM semimanufactures_outgoing AS a "
                    + " LEFT JOIN semimanufactures_outgoing_detail AS b "
                    + " ON a.id = b.semimanufactures_outgoing_id "
                    + " WHERE a.state = 0 "
                    + " GROUP BY warehouse_id, semimanufactures_id) AS b "
                    + " ON a.semimanufactures_id = b.semimanufactures_id AND a.warehouse_id = b.warehouse_id "
                    + " LEFT JOIN semimanufactures AS c ON "
                    + " a.semimanufactures_id = c.id ";       
            return Db.find(sql);
        }

        /** 
        * @Title: jsonToRecordList 
        * @Description: json 转换成 recordList
        * @param json
        * @return List<Record>
        * @author xuhui
        */
        private static List<Record> jsonToRecordList(String json) {
            List<JSONObject> list = new ArrayList<>();
            list = JSONObject.parseObject(json, list.getClass());
            List<Record> recordList = new ArrayList<Record>();
            for (JSONObject o : list) {
                Record record = new Record().setColumns(o);
                recordList.add(record);
            }
            return recordList;
        }
        
        /** 
        * @Title: getwarehouseOutNo 
        * @Description: 生成出库单号
        * @return String
        * @author xuhui
        */
        private static String getWarehouseOutNo() {
            // 
            String warehouseOutNo = "";
            // 当前日期
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String dateString = formatter.format(currentTime);
            System.out.println(dateString);
            // 最大出库单号
            String sql = " SELECT * "
                    + " FROM semimanufactures_outgoing "
                    + " WHERE outgoing_number LIKE '%"+ dateString +"%'"
                    + " ORDER BY outgoing_number DESC ";
            System.out.println(sql);
            List<Record> list = Db.find(sql);
            if (list.size() > 0) {
                String db_warehouse_out_no = list.get(0).getStr("outgoing_number");
                Integer i = Integer.parseInt(db_warehouse_out_no.substring(8, 12));
                System.out.println(i);
                i += 1;
                warehouseOutNo = "YC" + dateString + i;
            } else {
                warehouseOutNo = "YC" + dateString + "1001";
            }
            
            return warehouseOutNo;
        }

        /** 
        * @Title: getFinishedOut 
        * @Description: 根据出库单 id 查看出库单主表信息
        * @param id
        * @return Record
        * @author xuhui
        */
        public static Record getFinishedOut(Long id) {
            String sql = "SELECT a.*,b.company_name,c.user_name "
                    + " FROM `semimanufactures_outgoing` AS a "
                    + " LEFT JOIN company AS b "
                    + " ON a.company_id = b.id "
                    + " LEFT JOIN t_user AS c "
                    + " ON a.t_user_id = c.id "
                    + " WHERE a.id = ? ";
            return Db.find(sql, id).get(0);
        }

        /** 
         * @Title: getOutgoingProductList 
         * @Description: 根据出库单 id 查看出库单子表信息，同一商品求和
         * @param id
         * @return List<Record>
         * @author liyu
         */
         public static List<Record> getOutgoingProductList(Long id) {
             String sql = "SELECT a.*,b.trade_name,b.semimanufactures_number,b.specifications,b.measurement_unit,b.remark,"
                     + " c.id AS stock_detail_id,SUM(a.num) AS total_out_quantity "
                     + " FROM `semimanufactures_outgoing_detail` AS a "
                     + " LEFT JOIN semimanufactures AS b "
                     + " ON a.semimanufactures_id = b.id "
                     + " LEFT JOIN semimanufactures_stock_detail AS c "
                     + " ON a.semimanufactures_id = c.semimanufactures_id AND a.warehouse_id = c.warehouse_id "
                     + " WHERE a.semimanufactures_outgoing_id = ? "
                     + " GROUP BY a.semimanufactures_id ";
             List<Record> list = Db.find(sql, id);
             // 仓库名
             list = modifyWarehouseName(list);
             return list;
         }        
        
        /** 
        * @Title: getFinishedOutDetailList 
        * @Description: 根据出库单 id 查看出库单子表信息
        * @param id
        * @return List<Record>
        * @author xuhui
        */
        public static List<Record> getFinishedOutDetailList(Long id) {
            String sql = "SELECT a.*,b.trade_name,b.finished_number,b.specifications,b.measurement_unit,b.remark,"
                    + " c.id AS stock_detail_id "
                    + " FROM `semimanufactures_outgoing_detail` AS a "
                    + " LEFT JOIN finished_product AS b "
                    + " ON a.semimanufactures_id = b.id "
                    + " LEFT JOIN semimanufactures_stock_detail AS c "
                    + " ON a.semimanufactures_id = c.semimanufactures_id AND a.warehouse_id = c.warehouse_id "
                    + " WHERE a.semimanufactures_outgoing_id = ? ";
            List<Record> list = Db.find(sql);
            // TODO 仓库名
            list = modifyWarehouseName(list);
            return list;
        }

        /** 
        * @Title: delete 
        * @Description: 删除出库信息
        * @param ids
        * @return boolean
        * @author xuhui
        */
        public static boolean delete(String[] ids) {
            boolean flag = Db.tx(new IAtom() {
                boolean result = true;
                @Override
                public boolean run() throws SQLException {
                    for(String id:ids){
                        // 删除出库单
                        Db.deleteById("semimanufactures_outgoing", id);
                        // 删除出库单相关产品 
                        Db.update("DELETE FROM semimanufactures_outgoing_detail WHERE semimanufactures_outgoing_id = ?", id);
                    }
                    return result;
                }
            });
            return flag;
        }

        /** 
        * @Title: confirm 
        * @Description: 确认出库
        * @param id
        * @return Map<String,Object>
        * @author xuhui
        */
        public static Map<String, Object> confirm(Long id) {
            // 返回信息
            Map<String, Object> message = new HashMap<>();
            
            boolean result = Db.tx(new IAtom() {
                
                @Override
                public boolean run() throws SQLException {
                    // 出库单状态修改为已出库
                    Record record = new Record();
                    record.set("id", id);
                    record.set("state", 1);
                    Db.update("semimanufactures_outgoing", record);
                    
                    // 更新库存
                    // 出库产品列表
                    List<Record> outgoingProductList = Db.find("SELECT * "
                            + " FROM `semimanufactures_outgoing_detail` WHERE semimanufactures_outgoing_id = ?", id);                
                                   
                    // 更新 产品-仓库 对应数量
                    for (Record outgoingProduct : outgoingProductList) {
                        Integer product_id = outgoingProduct.getInt("semimanufactures_id"); // 产品 id
                        Integer warehouse_id = outgoingProduct.getInt("warehouse_id"); // 仓库 id
                        List<Record> stockDetailList = Db.find("SELECT * FROM `semimanufactures_stock_detail` WHERE semimanufactures_id = ? AND warehouse_id = ? ", product_id, warehouse_id);
                        if (stockDetailList.size() > 0) { // 更新数量
                            Record stockDetail = stockDetailList.get(0);
                            stockDetail.set("num", stockDetail.getBigDecimal("num").subtract(outgoingProduct.getBigDecimal("num")));
                            Db.update("semimanufactures_stock_detail", stockDetail);
                        } else { // 新增
                            message.put("tips", "库存不足！");
                            return false;
                        }
                    }
                    
                    // 更新库存主表产品数量
                    Set<Integer> productIds = new HashSet<>();
                    for (Record outgoingProduct : outgoingProductList) {
                        Integer product_id = outgoingProduct.getInt("semimanufactures_id"); // 产品 id
                        productIds.add(product_id);
                    }
                    for (Integer product_id : productIds) {
                        // List<Record> stockProductList = Db.find("SELECT * FROM `semimanufactures_stock` WHERE semimanufactures_id = ? ", product_id);
                        BigDecimal semimanufactures_stock_num = 
                                Db.queryBigDecimal("SELECT SUM(num) FROM `semimanufactures_stock_detail` WHERE semimanufactures_id = ? ", product_id);
                        System.out.println(semimanufactures_stock_num);
                        Db.update("UPDATE semimanufactures_stock SET semimanufactures_stock_num = ? WHERE semimanufactures_id = ?", semimanufactures_stock_num, product_id);
                    }
                    
                    message.put("tips", "保存成功");
                    
                    return true;
                }
            });
            updateStock();
            message.put("isSuccess", result);    
            return message;
        } 
        
        /** 
         * @Title: getCompanyList 
         * @Description: 客户列表
         * @return List<Record>
         * @author xuhui
         */
         public static List<Record> getCompanyList() {
             return Db.find("SELECT * FROM `company`");
         }    

         /** 
          * @Title: modifyWarehouseName 
          * @Description: 修改仓库名称
          * @param list
          * @return List<Record>
          * @author xuhui
          */
          public static List<Record> modifyWarehouseName(List<Record> list) {
              // 仓库 map
              Map<Integer, String> warehouses = getWarehouses();
              
              for(Record r : list) {
                  r.set("warehouse_name", warehouses.get(r.getInt("warehouse_id")));
              }
              
              return list;
          }
          
          /** 
          * @Title: getWarehouses 
          * @Description: 所有仓库 map
          * @return Map<Integer,String>
          * @author xuhui
          */
          private static Map<Integer, String> getWarehouses() {
              String sql = "SELECT a.id,CONCAT(c.warehouse_name,b.warehouse_name,a.warehouse_name) AS warehouse_name "
                      + " FROM `warehouse` AS a "
                      + " INNER JOIN warehouse AS b "
                      + " ON a.pid = b.id "
                      + " INNER JOIN warehouse AS c "
                      + " ON b.pid = c.id";
              List<Record> warehouseList = Db.find(sql);
              
              Map<Integer, String> map = new HashMap<>();
              for (Record r : warehouseList) {
                  map.put(r.getInt("id"), r.getStr("warehouse_name"));
              }
              return map;
          }
          
          /**
           * @author xuhui
           * @desc 更新库存，删除库存为0数据
           */
          public static void updateStock(){
        	  Db.tx(new IAtom() {		
				@Override
				public boolean run() throws SQLException {
					// TODO Auto-generated method stub
					String sql1 ="DELETE from semimanufactures_stock_detail where num = 0";
					Db.update(sql1);
					String sql2 = "DELETE from semimanufactures_stock where semimanufactures_stock_num = 0";
					Db.update(sql2);
					return true;
				}
			});
          }


}
