package renshou.leaseprice.in;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * @ClassName: LeaseInPriceService.java
 * @Description:
 * @author: LiYu
 * @date: 2017年8月9日下午7:40:12
 * @version: 1.0 版本初成
 */
public class LeaseInPriceService {

    /** 
    * @Title: getDataPages 
    * @Description: 租赁仓库入库费用列表
    * @param pageindex
    * @param pagelimit
    * @param warehouse_in_no
    * @param company_name
    * @return Page<Record>
    * @author liyu
    */
    public static Page<Record> getDataPages(Integer pageindex, Integer pagelimit, String warehouse_in_no,
            String company_name) {
        String select = " SELECT a.*,b.company_name,c.warehouse_name ";
        String sql = " FROM `t_lease_warehouse_in` AS a "
                + " LEFT JOIN company AS b "
                + " ON a.customer = b.id "
                + " LEFT JOIN warehouse AS c "
                + " ON a.warehouse_id = c.id "
                + " WHERE a.is_in = TRUE "
                + " ORDER BY is_checked, id DESC ";
        // 根据入库单号模糊查询
        if (warehouse_in_no != null && !"".equals(warehouse_in_no)) {
            sql += "AND warehouse_in_no like '%" + warehouse_in_no + "%'";
        }
        // 根据客户名称模糊查询
        if (company_name != null && !"".equals(company_name)) {
            sql += "AND company_name like '%" + company_name + "%'";
        }
        
        return Db.paginate(pageindex, pagelimit, select, sql);
    }

    /** 
    * @Title: getProductList 
    * @Description: 入库单产品详情
    * @param id
    * @return List<Record>
    * @author liyu
    */
    public static List<Record> getProductList(Integer id) {
        String sql = " SELECT * FROM `t_lease_warehouse_in_product` WHERE warehouse_in_id = "+ id;
        return Db.find(sql);
    }
    
    /** 
    * @Title: save 
    * @Description: 保存入库单产品价格，更新出库、库存价格
    * @param id
    * @param customer
    * @param warehouse_id
    * @param location
    * @param productList
    * @return Map<String,Object>
    * @author liyu
    */
    public static Map<String, Object> save(Long warehouse_in_id, String productList) {
        boolean result = Db.tx(new IAtom() {
            
            @Override
            public boolean run() throws SQLException {
                // 产品
                List<Record> recordList = jsonToRecordList(productList);
                
                // 入库单
                Record warehouseIn = Db.findById("t_lease_warehouse_in", warehouse_in_id);
                // 入库每日总仓储费用
                BigDecimal in_price_per_day = new BigDecimal("0");
                for (Record r : recordList) {
                    BigDecimal price_per_day = r.getBigDecimal("price_per_day");
                    in_price_per_day = in_price_per_day.add(price_per_day);
                }
                warehouseIn.set("in_price_per_day", in_price_per_day);
                warehouseIn.set("is_checked", true); // 标记为已核算
                Db.update("t_lease_warehouse_in", warehouseIn);
                // 保存入库产品价格
                for (Record r : recordList) {
                    r.set("warehouse_in_id", warehouse_in_id);
                    r.set("is_checked", true);
                    Db.update("t_lease_warehouse_in_product", r);
                }
                
                // 如果存在对应出库单，更新其每日仓储价格
                // 出库单 id 及其出库每日价格
                List<Record> warehouseOutList = Db.find(" SELECT a.warehouse_out_id AS id,b.id AS warehouse_in_id, "
                        + " SUM(a.out_quantity * b.unit_price) AS out_price_per_day "
                        + " FROM `t_lease_warehouse_out_product` AS a "
                        + " LEFT JOIN t_lease_warehouse_in_product AS b "
                        + " ON a.product_id = b.id "
                        + " WHERE warehouse_in_id = 1 "
                        + " GROUP BY warehouse_out_id");
                Db.batchUpdate("t_lease_warehouse_out", warehouseOutList, warehouseOutList.size());
                
                // 更新库存费用 
                List<Record> inventoryProductList = Db.find("SELECT * "
                        + " FROM t_lease_warehouse_inventory_product AS a "
                        + " LEFT JOIN t_lease_warehouse_in_product AS b "
                        + " ON a.product_id = b.id "
                        + " WHERE warehouse_in_id = ? ", warehouse_in_id);
                BigDecimal inventory_price_per_day = new BigDecimal(0);
                for (Record r : inventoryProductList) {
                    BigDecimal price_per_day = new BigDecimal(0);
                    price_per_day = r.getBigDecimal("left_quantity").multiply(r.getBigDecimal("unit_price"));
                    inventory_price_per_day = inventory_price_per_day.add(price_per_day);
                }
                Record inventory = Db.findFirst("SELECT * FROM t_lease_warehouse_inventory WHERE warehouse_in_id = ?", warehouse_in_id);
                inventory.set("inventory_price_per_day", inventory_price_per_day);
                Db.update("t_lease_warehouse_inventory", inventory);
                
                return true;
            }
        });
        
        Map<String, Object> message = new HashMap<>();
        message.put("isSuccess", result);
        message.put("tips", result ? "保存成功" : "保存失败");

        return message;
        
    }
    
    /** 
    * @Title: jsonToRecordList 
    * @Description: json 转化成 record 列表
    * @param jsonString
    * @return List<Record>
    * @author liyu
    */
    private static List<Record> jsonToRecordList(String jsonString) {
        List<JSONObject> list = new ArrayList<>();
        list = JSONObject.parseObject(jsonString, list.getClass());
        System.out.println(list);
        
        List<Record> recordList = new ArrayList<Record>();
        for (JSONObject product : list) {
            String id = product.getString("id");
            BigDecimal unit_price = product.getBigDecimal("unit_price");
            BigDecimal price_per_day = product.getBigDecimal("price_per_day");
            Record record = new Record();
            record.set("id", id);
            record.set("unit_price", unit_price);
            record.set("price_per_day", price_per_day);
            recordList.add(record);
        }
        
        return recordList;
    }

    
}
