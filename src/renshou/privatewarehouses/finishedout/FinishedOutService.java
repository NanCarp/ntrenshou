package renshou.privatewarehouses.finishedout;

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

/**
 * @ClassName: FinishedOutService.java
 * @Description:
 * @author: LiYu
 * @date: 2017年8月10日下午5:55:40
 * @version: 1.0 版本初成
 */
public class FinishedOutService {

    /** 
    * @Title: getFinishedIn 
    * @Description: 成品出库列表
    * @param pageindex
    * @param pagelimit
    * @param stock_number
    * @param user_name
    * @return Page<Record>
    * @author liyu
    */
    public static Page<Record> getFinishedIn(Integer pageindex, Integer pagelimit, String outgoing_number,
            String user_name) {
        String select = "SELECT a.*,b.company_name,c.user_name ";
                
        String sql = " FROM `finished_product_outgoing` AS a "
                + " LEFT JOIN company AS b "
                + " ON a.company = b.id "
                + " LEFT JOIN t_user AS c "
                + " ON a.t_user_id = c.id "
                + " WHERE 1=1 ";

        if (outgoing_number != null && outgoing_number != "") {
            sql += " and outgoing_number like '%" + outgoing_number + "%'";
        }
        if (user_name != null && user_name != "") {
            sql += " and user_name like '%" + user_name + "%'";
        }
        sql +=" order by a.id desc";
        return Db.paginate(pageindex, pagelimit, select, sql);
    }

    /** 
    * @Title: getStockList 
    * @Description: 获得库存产品列表
    * @return List<Record>
    * @author liyu
    */
    public static List<Record> getStockList() {
        String sql = " SELECT a.*,b.finished_number,b.trade_name,b.specifications,b.measurement_unit,b.remark "
                + " FROM `finished_product_stock` AS a "
                + " LEFT JOIN finished_product AS b "
                + " ON a.finished_product_id = b.id ";
        
        return Db.find(sql);
    }

    /** 
    * @Title: getStockDetailByProductNo 
    * @Description: 根据产品编号查询某一产品在各个仓库的位置及数量
    * @param finished_number
    * @return List<Record>
    * @author liyu
    */
    public static List<Record> getStockDetailByProductNo(String finished_number, String trade_name) {
        String sql = "SELECT a.id,a.warehouse_id,a.finished_product_id,a.num, "
                + " c.finished_number,c.trade_name,c.specifications,c.measurement_unit,c.remark, "
                + " IFNULL(num - b.total_out_quantity,num) AS left_quantity "
                + " FROM finished_product_stock_detail AS a "
                + " LEFT JOIN "
                + " (SELECT b.warehouse_id,b.finished_product_id,SUM(num) AS total_out_quantity  "
                + " FROM finished_product_outgoing AS a "
                + " LEFT JOIN finished_product_outgoing_detail AS b "
                + " ON a.id = b.finished_product_outgoing_id "
                + " WHERE a.state = 0 "
                + " GROUP BY warehouse_id, finished_product_id) AS b "
                + " ON a.finished_product_id = b.finished_product_id AND a.warehouse_id = b.warehouse_id "
                + " LEFT JOIN finished_product AS c ON "
                + " a.finished_product_id = c.id "
                + " WHERE IFNULL(num - b.total_out_quantity,num) > 0 ";
        // 产品编码
        if(finished_number!=null && !"".equals(finished_number)) {
            sql += " AND c.finished_number like '%"+ finished_number +"%'";
        }
        // 品名
        if(trade_name!=null && !"".equals(trade_name)) {
            sql += " AND c.trade_name like '%"+ trade_name +"%'";
        }
        
        sql += " ORDER BY c.finished_number, a.warehouse_id ";
        
        List<Record> list = Db.find(sql);
        // 仓库名
        list = modifyWarehouseName(list);
        return list;
    }

    /** 
    * @Title: getStockDetailList 
    * @Description: 获得库存产品列表 对应仓库
    * @return List<Record>
    * @author liyu
    */
    public static List<Record> getStockDetailList() {

        String sql = "SELECT a.id,a.warehouse_id,a.finished_product_id,a.num, "
                + " c.finished_number,c.trade_name,c.specifications,c.measurement_unit,c.remark, "
                + " IFNULL(num - b.total_out_quantity,num) AS left_quantity "
                + " FROM finished_product_stock_detail AS a "
                + " LEFT JOIN "
                + " (SELECT b.warehouse_id,b.finished_product_id,SUM(num) AS total_out_quantity  "
                + " FROM finished_product_outgoing AS a "
                + " LEFT JOIN finished_product_outgoing_detail AS b "
                + " ON a.id = b.finished_product_outgoing_id "
                + " WHERE a.state = 0 "
                + " GROUP BY warehouse_id, finished_product_id) AS b "
                + " ON a.finished_product_id = b.finished_product_id AND a.warehouse_id = b.warehouse_id "
                + " LEFT JOIN finished_product AS c ON "
                + " a.finished_product_id = c.id "
                + " WHERE IFNULL(num - b.total_out_quantity,num) > 0 ";
        
        sql += " ORDER BY c.finished_number, a.warehouse_id ";
        
        List<Record> list = Db.find(sql);
        // 仓库名
        list = modifyWarehouseName(list);
        return list;
    }

    /** 
    * @Title: save 
    * @Description: 保存出库详细
    * @param finished_product_outgoing_id
    * @param outgoingDetailList
    * @param user_id
    * @return Map<String,Object>
    * @author liyu
    */
    public static Map<String, Object> save(Long finished_product_outgoing_id, String outgoingDetailList,
            Integer user_id, Integer company_id, String remark) {
        
        // 返回信息
        Map<String, Object> message = new HashMap<>(); 
        
        boolean result = Db.tx(new IAtom() {
            
            @Override
            public boolean run() throws SQLException {
                // 出库产品列表
                List<Record> recordList = jsonToRecordList(outgoingDetailList);
                // 出库单 id
                Long outgoing_id = finished_product_outgoing_id;
                // 出库单
                Record outgoing = new Record();
                Date now = new Date(); // 当前日期
                outgoing.set("outgoing_time", now); // 出库日期
                outgoing.set("t_user_id", user_id); // 出库人
                outgoing.set("company", company_id); // 客户 
                outgoing.set("remark", remark); // 备注
                
                if(null != outgoing_id) { // 编辑
                    outgoing.set("id", outgoing_id);
                    Db.update("finished_product_outgoing", outgoing);
                } else { // 新增
                    outgoing.set("outgoing_number", getWarehouseOutNo()); // 出库单号
                    Db.save("finished_product_outgoing", outgoing);
                    outgoing_id = outgoing.getLong("id");
                }
                
                // 删除出库产品详情
                List<Record> dbOutgoingDetail = Db.find("SELECT * FROM finished_product_outgoing_detail WHERE finished_product_outgoing_id = ?", outgoing_id);
                if (dbOutgoingDetail.size() > 0) {
                    for (Record r : dbOutgoingDetail) {
                        Db.delete("finished_product_outgoing_detail", r);
                    }
                }
                
                // 保存 
                for (Record r : recordList) {
                    r.set("finished_product_outgoing_id", outgoing_id);
                    r.remove("id");
                    Db.save("finished_product_outgoing_detail", r);
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
    * @author liyu
    */
    protected static List<Record> getInventory() {
        String sql = "SELECT a.id,a.warehouse_id,a.finished_product_id,a.num, "
                + " c.finished_number,c.trade_name,c.specifications,c.measurement_unit,c.remark, "
                + " IFNULL(num - b.total_out_quantity,num) AS left_quantity "
                + " FROM finished_product_stock_detail AS a "
                + " LEFT JOIN "
                + " (SELECT b.warehouse_id,b.finished_product_id,SUM(num) AS total_out_quantity  "
                + " FROM finished_product_outgoing AS a "
                + " LEFT JOIN finished_product_outgoing_detail AS b "
                + " ON a.id = b.finished_product_outgoing_id "
                + " WHERE a.state = 0 "
                + " GROUP BY warehouse_id, finished_product_id) AS b "
                + " ON a.finished_product_id = b.finished_product_id AND a.warehouse_id = b.warehouse_id "
                + " LEFT JOIN finished_product AS c ON "
                + " a.finished_product_id = c.id ";
        return Db.find(sql);
    }

    /** 
    * @Title: jsonToRecordList 
    * @Description: json 转换成 recordList
    * @param json
    * @return List<Record>
    * @author liyu
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
    * @author liyu
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
                + " FROM finished_product_outgoing "
                + " WHERE outgoing_number LIKE '%"+ dateString +"%'"
                + " ORDER BY outgoing_number DESC ";
        List<Record> list = Db.find(sql);
        if (list.size() > 0) {
            String db_warehouse_out_no = list.get(0).getStr("outgoing_number");
            Integer i = Integer.parseInt(db_warehouse_out_no.substring(10, 13));
            i += 1001;
            warehouseOutNo = "YC" + dateString + i.toString().substring(1);
        } else {
            warehouseOutNo = "YC" + dateString + "001";
        }
        
        return warehouseOutNo;
    }

    /** 
    * @Title: getFinishedOut 
    * @Description: 根据出库单 id 查看出库单主表信息
    * @param id
    * @return Record
    * @author liyu
    */
    public static Record getFinishedOut(Long id) {
        String sql = "SELECT a.*,b.company_name,c.user_name "
                + " FROM `finished_product_outgoing` AS a "
                + " LEFT JOIN company AS b "
                + " ON a.company = b.id "
                + " LEFT JOIN t_user AS c "
                + " ON a.t_user_id = c.id "
                + " WHERE a.id = ? ";
        return Db.find(sql, id).get(0);
    }

    /** 
    * @Title: getFinishedOutDetailList 
    * @Description: 根据出库单 id 查看出库单子表信息
    * @param id
    * @return List<Record>
    * @author liyu
    */
    public static List<Record> getFinishedOutDetailList(Long id) {
        String sql = "SELECT a.*,b.trade_name,b.finished_number,b.specifications,b.measurement_unit,b.remark,"
                + " c.id AS stock_detail_id "
                + " FROM `finished_product_outgoing_detail` AS a "
                + " LEFT JOIN finished_product AS b "
                + " ON a.finished_product_id = b.id "
                + " LEFT JOIN finished_product_stock_detail AS c "
                + " ON a.finished_product_id = c.finished_product_id AND a.warehouse_id = c.warehouse_id "
                + " WHERE a.finished_product_outgoing_id = ? ";
        List<Record> list = Db.find(sql, id);
        // 仓库名
        list = modifyWarehouseName(list);
        return list;
    }

    /** 
    * @Title: delete 
    * @Description: 删除出库信息
    * @param ids
    * @return boolean
    * @author liyu
    */
    public static boolean delete(String[] ids) {
        boolean flag = Db.tx(new IAtom() {
            boolean result = true;
            @Override
            public boolean run() throws SQLException {
                for(String id:ids){
                    // 删除出库单
                    Db.deleteById("finished_product_outgoing", id);
                    // 删除出库单相关产品 
                    Db.update("DELETE FROM finished_product_outgoing_detail WHERE finished_product_outgoing_id = ?", id);
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
    * @author liyu
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
                Db.update("finished_product_outgoing", record);
                
                // 更新库存
                // 出库产品列表
                List<Record> outgoingProductList = Db.find("SELECT * "
                        + " FROM `finished_product_outgoing_detail` WHERE finished_product_outgoing_id = ?", id);                
                               
                // 更新 产品-仓库 对应数量
                for (Record outgoingProduct : outgoingProductList) {
                    Integer product_id = outgoingProduct.getInt("finished_product_id"); // 产品 id
                    Integer warehouse_id = outgoingProduct.getInt("warehouse_id"); // 仓库 id
                    List<Record> stockDetailList = Db.find("SELECT * FROM `finished_product_stock_detail` WHERE finished_product_id = ? AND warehouse_id = ? ", product_id, warehouse_id);
                    if (stockDetailList.size() > 0) { // 更新数量
                        Record stockDetail = stockDetailList.get(0);
                        stockDetail.set("num", stockDetail.getBigDecimal("num").subtract(outgoingProduct.getBigDecimal("num")));
                        Db.update("finished_product_stock_detail", stockDetail);
                    } else { // 新增
                        message.put("tips", "库存不足！");
                        return false;
                    }
                }
                
                // 更新库存主表产品数量
                Set<Integer> productIds = new HashSet<>();
                for (Record outgoingProduct : outgoingProductList) {
                    Integer product_id = outgoingProduct.getInt("finished_product_id"); // 产品 id
                    productIds.add(product_id);
                }
                for (Integer product_id : productIds) {
                    BigDecimal finished_product_stock_num = 
                            Db.queryBigDecimal("SELECT SUM(num) FROM `finished_product_stock_detail` WHERE finished_product_id = ? ", product_id);
                    System.out.println(finished_product_stock_num);
                    Db.update("UPDATE finished_product_stock SET finished_product_stock_num = ? WHERE finished_product_id = ?", finished_product_stock_num, product_id);
                }
                
                message.put("tips", "保存成功");
                
                return true;
            }
        });
        // 更新库存
        updateStock();
        message.put("isSuccess", result);
        
        return message;
    }

    /** 
    * @Title: getOutgoingProductList 
    * @Description: 根据出库单 id 查看出库单子表信息，同一商品求和
    * @param id
    * @return List<Record>
    * @author liyu
    */
    public static List<Record> getOutgoingProductList(Long id) {
        String sql = "SELECT a.*,b.trade_name,b.finished_number,b.specifications,b.measurement_unit,b.remark,"
                + " c.id AS stock_detail_id,SUM(a.num) AS total_out_quantity "
                + " FROM `finished_product_outgoing_detail` AS a "
                + " LEFT JOIN finished_product AS b "
                + " ON a.finished_product_id = b.id "
                + " LEFT JOIN finished_product_stock_detail AS c "
                + " ON a.finished_product_id = c.finished_product_id AND a.warehouse_id = c.warehouse_id "
                + " WHERE a.finished_product_outgoing_id = ? "
                + " GROUP BY a.finished_product_id ";
        List<Record> list = Db.find(sql, id);
        // 仓库名
        list = modifyWarehouseName(list);
        return list;
    }

    /** 
    * @Title: getCompanyList 
    * @Description: 客户列表
    * @return List<Record>
    * @author liyu
    */
    public static List<Record> getCompanyList() {
        return Db.find("SELECT * FROM `company`");
    }  
    
    /** 
    * @Title: modifyWarehouseName 
    * @Description: 修改仓库名称
    * @param list
    * @return List<Record>
    * @author liyu
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
    * @author liyu
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
    * @Title: getFinishedByNum 
    * @Description: 根据产品编号查询产品信息
    * @param num
    * @return Record
    * @author liyu
    */
    public static List<Record> getFinishedByNum(String num) {
        String sql = "SELECT a.id,a.warehouse_id,a.finished_product_id,a.num, "
                + " c.finished_number,c.trade_name,c.specifications,c.measurement_unit,c.remark, "
                + " IFNULL(num - b.total_out_quantity,num) AS left_quantity "
                + " FROM finished_product_stock_detail AS a "
                + " LEFT JOIN "
                + " (SELECT b.warehouse_id,b.finished_product_id,SUM(num) AS total_out_quantity  "
                + " FROM finished_product_outgoing AS a "
                + " LEFT JOIN finished_product_outgoing_detail AS b "
                + " ON a.id = b.finished_product_outgoing_id "
                + " WHERE a.state = 0 "
                + " GROUP BY warehouse_id, finished_product_id) AS b "
                + " ON a.finished_product_id = b.finished_product_id AND a.warehouse_id = b.warehouse_id "
                + " LEFT JOIN finished_product AS c ON "
                + " a.finished_product_id = c.id "
                + " WHERE c.finished_number = '"+ num +"'"
                + " AND IFNULL(num - b.total_out_quantity,num) > 0";
        List<Record> list = Db.find(sql);
        // 仓库名
        list = modifyWarehouseName(list);
        return list;
    }
    
    /**
     * @author liyu
     * @desc 更新库存，删除库存为0数据
     */
    public static void updateStock(){
        Db.tx(new IAtom() {       
          @Override
          public boolean run() throws SQLException {
              String sql1 ="DELETE from finished_product_stock_detail where num = 0";
              Db.update(sql1);
              String sql2 = "DELETE from finished_product_stock where finished_product_stock_num = 0";
              Db.update(sql2);
              return true;
          }
      });
    }

}
