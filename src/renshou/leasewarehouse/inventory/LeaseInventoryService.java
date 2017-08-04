package renshou.leasewarehouse.inventory;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class LeaseInventoryService {

    public static Page<Record> getDataPages(Integer pageindex, Integer pagelimit, String warehouse_in_no,
            String company_name) {
        String sql = " FROM t_lease_warehouse_inventory ";
        return Db.paginate(pageindex, pagelimit, "SELECT *", sql);
    }

    public static List<Record> getProductList(Integer inventory_id) {
        String sql = " SELECT * FROM `t_lease_warehouse_inventory_product` WHERE inventory_id = '"+ inventory_id +"' ";
        return Db.find(sql);
    }

}
