package renshou.leasewarehouse.in;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class LeaseInService {

    public static Page<Record> getDataPages(Integer pageindex, Integer pagelimit, String warehouse_in_no,
            String company_name) {
        String sql = " FROM t_lease_warehouse_in ";
        return Db.paginate(pageindex, pagelimit, "SELECT *", sql);
    }

    public static List<Record> getProductList(String warehouse_in_no) {
        String sql = " SELECT * FROM `t_lease_warehouse_in_product` WHERE warehouse_in_no like '%"+ warehouse_in_no +"%' ";
        return Db.find(sql);
    }

}
