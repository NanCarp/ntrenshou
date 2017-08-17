package renshou.leaseprice.inventory;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * @ClassName: LeaseInventoryPriceService.java
 * @Description:
 * @author: LiYu
 * @date: 2017年8月9日下午1:46:31
 * @version: 1.0 版本初成
 */
public class LeaseInventoryPriceService {

    /** 
    * @Title: getDataPages 
    * @Description: 库存费用列表
    * @param pageindex
    * @param pagelimit
    * @param warehouse_in_no
    * @param company_name
    * @return Page<Record>
    * @author liyu
    */
    public static Page<Record> getDataPages(Integer pageindex, Integer pagelimit, String warehouse_in_no,
            String company_name) {
        String select = "SELECT a.*,b.warehouse_in_no,b.warehouse_in_date,b.location, "
                + " b.warehouse_id,c.company_name,d.warehouse_name,DATE_FORMAT(NOW(),'%Y-%m-%d') AS today, "
                + " DATEDIFF(NOW(), warehouse_in_date) + 1  AS days, "
                + " (DATEDIFF(NOW(), warehouse_in_date) + 1) * inventory_price_per_day AS total_cost ";
        String sql = " FROM `t_lease_warehouse_inventory` AS a "
                + " LEFT JOIN `t_lease_warehouse_in` AS b "
                + " ON a.warehouse_in_id = b.id "
                + " LEFT JOIN `company` AS c "
                + " ON b.customer = c.id "
                + " LEFT JOIN warehouse AS d "
                + " ON b.warehouse_id = d.id "
                + " WHERE a.is_all_out = FALSE ";
        // 入库单号
        if (warehouse_in_no != null && !"".equals(warehouse_in_no)) {
            sql += "AND warehouse_in_no like '%" + warehouse_in_no + "%'";
        }
        // 客户名称
        if (company_name != null && !"".equals(company_name)) {
            sql += "AND company_name like '%" + company_name + "%'";
        }
        return Db.paginate(pageindex, pagelimit, select, sql);
    }

    /** 
    * @Title: getProductList 
    * @Description: 库存产品列表
    * @param warehouse_in_id
    * @return List<Record>
    * @author liyu
    */
    public static List<Record> getProductList(Long warehouse_in_id) {
        String sql = " SELECT b.*,a.left_quantity  "
                + " FROM t_lease_warehouse_inventory_product AS a "
                + " LEFT JOIN t_lease_warehouse_in_product AS b "
                + " ON a.product_id = b.id "
                + " WHERE warehouse_in_id = ? ";
        return Db.find(sql, warehouse_in_id);
    }

}
