package renshou.leaseprice.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import renshou.leasewarehouse.in.LeaseInService;



/**
 * @ClassName: LeaseInController.java
 * @Description:
 * @author: LiYu
 * @version: 1.0 版本初成
 */
public class LeaseInventoryPriceController extends Controller {
    // 页面
    public void index() {
        render("lease_inventory_price.html");
    }
    
    // 数据列表
    public void getJson(){
        // 入库单号
        String warehouse_in_no = getPara("warehouse_in_no");
        setAttr("warehouse_in_no", warehouse_in_no);
        // 客户名称
        String company_name = getPara("company_name");
        setAttr("company_name", company_name);
        
        Integer pageindex = 0; // 页码
        Integer pagelimit = getParaToInt("limit")==null? 12 :getParaToInt("limit"); // 每页数据条数
        Integer offset = getParaToInt("offset")==null?0:getParaToInt("offset");
        if(offset!=0){
            pageindex = offset/pagelimit;
        }
        pageindex += 1;
        
        Page<Record> page = LeaseInventoryPriceService.getDataPages(pageindex, pagelimit, warehouse_in_no, company_name);
        
        Map<String, Object> map = new HashMap<String,Object>();
        
        // 修改入库单仓库名称 TODO
        List<Record> list = LeaseInService.modifyWarehouseName(page.getList());
        map.put("rows", list);
        map.put("total", page.getTotalRow());
        //System.out.println(page.getList());
        
        renderJson(map);
    }
    
    // 获得
    public void getRecord() {
        // id
        Integer id = getParaToInt();

        /*if (id != null) {//编辑
            Record record = Db.findById("t_lease_warehouse_inventory", id);
            setAttr("record", record);
            // 入库单产品详情
            List<Record> productList = LeaseInventoryPriceService.getProductList(id);
            setAttr("productList", productList);
        }*/
        
        render("lease_inventory_price_detail.html");
    }
    
    // 查看
    public void check() {
        // 入库单 id
        Long warehouse_in_id = getParaToLong();
        
        List<Record> productList = LeaseInventoryPriceService.getProductList(warehouse_in_id);
        setAttr("productList", productList);
        
        render("lease_inventory_price_check.html");
    }
}
