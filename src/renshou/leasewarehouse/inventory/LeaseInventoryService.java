package renshou.leasewarehouse.inventory;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * @ClassName: LeaseInventoryService.java
 * @Description:
 * @author: LiYu
 * @date: 2017年8月10日上午10:16:43
 * @version: 1.0 版本初成
 */
public class LeaseInventoryService {

    /** 
    * @Title: getDataPages 
    * @Description: 库存列表
    * @param pageindex
    * @param pagelimit
    * @param warehouse_in_no
    * @param company_name
    * @return Page<Record>
    * @author liyu
    */
    public static Page<Record> getDataPages(Integer pageindex, Integer pagelimit, String warehouse_in_no,
            String company_name) {
        String select = " SELECT c.company_name,d.warehouse_name,d.position, "
                + " b.warehouse_id,b.warehouse_in_no,b.location,a.* ";
                
        String sql = " FROM t_lease_warehouse_inventory AS a "
                + " LEFT JOIN `t_lease_warehouse_in` AS b "
                + " ON b.id = a.warehouse_in_id "
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
    * @Description: 库存产品列表
    * @param inventory_id
    * @return List<Record>
    * @author liyu
    */
    public static List<Record> getProductList(Integer inventory_id) {
        String sql = " SELECT * FROM "
                + " (SELECT b.* FROM  "
                + " `t_lease_warehouse_in` AS a "
                + " LEFT JOIN t_lease_warehouse_in_product AS b "
                + " ON a.id = b.warehouse_in_id "
                + " WHERE is_in = TRUE) AS a "
                + " LEFT JOIN "
                + " (SELECT b.product_id, b.left_quantity,b.inventory_id "
                + " FROM t_lease_warehouse_inventory AS a "
                + " LEFT JOIN t_lease_warehouse_inventory_product AS b "
                + " ON a.id = b.inventory_id) AS b "
                + " ON a.id = b.product_id "
                + " WHERE inventory_id = ?";
        
        List<Record> list = Db.find(sql, inventory_id);
        
        return list;
    }

}
