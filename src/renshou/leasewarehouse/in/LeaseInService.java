package renshou.leasewarehouse.in;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * @ClassName: LeaseInService.java
 * @Description:
 * @author: LiYu
 * @date: 2017年8月10日上午10:14:56
 * @version: 1.0 版本初成
 */
public class LeaseInService {

    /** 
    * @Title: getDataPages 
    * @Description: 入库单列表
    * @param pageindex
    * @param pagelimit
    * @param warehouse_in_no
    * @param company_name
    * @return Page<Record>
    * @author liyu
    */
    public static Page<Record> getDataPages(Integer pageindex, Integer pagelimit, String warehouse_in_no,
            String company_name) {
        String select = " SELECT b.*,c.company_name,d.warehouse_name,d.position ";
                
        String sql = " FROM `t_lease_warehouse_in` AS b "
                + " LEFT JOIN company AS c  "
                + " ON b.customer = c.id "
                + " LEFT JOIN warehouse AS d "
                + " ON b.warehouse_id = d.id "
                + " WHERE 1=1 ";
        
        if (warehouse_in_no != null && !"".equals(warehouse_in_no)) {
            sql += "AND warehouse_in_no like '%" + warehouse_in_no + "%'";
        }
        
        if (company_name != null && !"".equals(company_name)) {
            sql += "AND company_name like '%" + company_name + "%'";
        }
        
        return Db.paginate(pageindex, pagelimit, select, sql);
    }

    /** 
    * @Title: getProductList 
    * @Description: 入库单产品列表
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
    * @Description: 保存入库单
    * @param id
    * @param customer
    * @param warehouse_id
    * @param location
    * @param productList
    * @return Map<String,Object>
    * @author liyu
    */
    public static Map<String, Object> save(Long id, Integer customer, Integer warehouse_id, String location, String productList, String warehouse_in_person) {
        boolean result = Db.tx(new IAtom() {
            
            @Override
            public boolean run() throws SQLException {
                Long warehouse_in_id = id; // 入库单 id
                // 产品
                List<Record> recordList = jsonToRecordList(productList);
                
                // 入库单
                Date now = new Date(); // 当前日期
                Record warehouseIn = new Record();
                warehouseIn.set("warehouse_in_date", now); // 入库日期
                warehouseIn.set("customer", customer); // 客户
                warehouseIn.set("warehouse_id", warehouse_id); // 仓库
                warehouseIn.set("location", location); // 摆放位置
                warehouseIn.set("warehouse_in_person", warehouse_in_person); // 入库人 
                
                if(null != warehouse_in_id) { // 编辑
                    warehouseIn.set("id", id);
                    Db.update("t_lease_warehouse_in", warehouseIn);
                } else { // 新增
                    warehouseIn.set("warehouse_in_no", getWarehouseInNo()); // 入库单号
                    Db.save("t_lease_warehouse_in", warehouseIn);
                    warehouse_in_id = warehouseIn.getLong("id");
                }
                
                // 删除原产品
                List<Record> originalProduct = Db.find("SELECT * FROM t_lease_warehouse_in_product WHERE warehouse_in_id = ?", warehouse_in_id);
                if (originalProduct.size() > 0) {
                    for (Record r : originalProduct) {
                        Db.delete("t_lease_warehouse_in_product", r);
                    }
                }
                // 保存 
                for (Record r : recordList) {
                    r.set("warehouse_in_id", warehouse_in_id);
                    Db.save("t_lease_warehouse_in_product", r);
                }
                
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
    * @Description: json 转 record 列表
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
            String name = product.getString("name");
            String specification = product.getString("specification");
            String unit = product.getString("unit");
            String quantity = product.getString("quantity");
            System.out.println("name: "+name+" specification: "+specification+" unit: "+unit+" quantity: "+quantity);
            Record record = new Record();
            record.set("name", name);
            record.set("specification", specification);
            record.set("unit", unit);
            record.set("quantity", quantity);
            recordList.add(record);
        }
        
        return recordList;
    }

    /** 
    * @Title: getWarehouseInNo 
    * @Description: 生成入库单号
    * @return String
    * @author liyu
    */
    private static String getWarehouseInNo() {
        // 
        String warehouseInNo = "";
        // 当前日期
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        String dateString = formatter.format(currentTime);
        System.out.println(dateString);
        // 最大入库单号
        String sql = " SELECT * "
                + " FROM t_lease_warehouse_in "
                + " WHERE warehouse_in_no LIKE '%"+ dateString +"%'"
                + " ORDER BY warehouse_in_no DESC ";
        System.out.println(sql);
        List<Record> list = Db.find(sql);
        if (list.size() > 0) {
            String db_warehouse_in_no = list.get(0).getStr("warehouse_in_no");
            Integer i = Integer.parseInt(db_warehouse_in_no.substring(8, 12));
            System.out.println(i);
            i += 1;
            warehouseInNo = "RK" + dateString + i;
        } else {
            warehouseInNo = "RK" + dateString + "1001";
        }
        
        return warehouseInNo;
    }
    
    /** 
    * @Title: delete 
    * @Description: 批量删除
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
                    // 删除入库单
                    result = Db.deleteById("t_lease_warehouse_in", id);
                    // 删除入库单相关产品 
                    Db.update("DELETE FROM t_lease_warehouse_in_product WHERE warehouse_in_id = ?", id);
                    
                    if (result == false) {
                        break;
                    }
                }
                return result;
            }
        });
        return flag;
    }

    /** 
    * @Title: confirm 
    * @Description: 确认入库
    * @param id
    * @return Map<String,Object>
    * @author liyu
    */
    public static Map<String, Object> confirm(Long id) {
        boolean result = Db.tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                //Long warehouse_in_id = id; // 入库单 id
                // 修改入库状态
                Record warehouseIn = Db.findById("t_lease_warehouse_in", id);
                warehouseIn.set("is_in", true);
                Db.update("t_lease_warehouse_in", warehouseIn);
                // 更新库存
                // 主表
                Record warehouseInventory = new Record();
                warehouseInventory.set("warehouse_in_id", id);
                Db.save("t_lease_warehouse_inventory", warehouseInventory);
                // 子表
                List<Record> inProductList = Db.find("SELECT id AS product_id, quantity AS left_quantity FROM `t_lease_warehouse_in_product` WHERE warehouse_in_id = ?", id);
                Long inventory_id = warehouseInventory.getLong("id");
                for (Record r : inProductList) {
                    r.set("inventory_id", inventory_id);
                    Db.save("t_lease_warehouse_inventory_product", r);
                }
                
                return true;
            }
        });
        
        Map<String, Object> message = new HashMap<>();
        message.put("isSuccess", result);
        message.put("tips", result ? "入库成功" : "入库失败");

        return message;
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
    
}
