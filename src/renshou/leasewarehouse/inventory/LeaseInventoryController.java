package renshou.leasewarehouse.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import renshou.interceptor.ManageInterceptor;
import renshou.leasewarehouse.in.LeaseInService;

/**
 * @ClassName: LeaseInController.java
 * @Description:
 * @author: LiYu
 * @version: 1.0 版本初成
 */
@Before(ManageInterceptor.class)
public class LeaseInventoryController extends Controller {
    /** 
    * @Title: index 
    * @Description: 库存页面
    * @author liyu
    */
    public void index() {
        render("lease_inventory.html");
    }
    
    /** 
    * @Title: getJson 
    * @Description: 库存列表数据
    * @author liyu
    */
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
        
        Page<Record> page = LeaseInventoryService.getDataPages(pageindex, pagelimit, warehouse_in_no, company_name);
        
        Map<String, Object> map = new HashMap<String,Object>();
        // 修改入库单仓库名称 
        List<Record> list = LeaseInService.modifyWarehouseName(page.getList());
        map.put("rows", list);
        map.put("total", page.getTotalRow());
        
        renderJson(map);
    }
    
    /** 
    * @Title: getRecord 
    * @Description: 获得单条库存数据
    * @author liyu
    */
    public void getRecord() {
        // id
        Integer id = getParaToInt();

        if (id != null) {//编辑
            Record record = Db.findById("t_lease_warehouse_inventory", id);
            setAttr("record", record);
            // 产品详情
            List<Record> productList = LeaseInventoryService.getProductList(id);
            setAttr("productList", productList);
        }
        
        render("lease_inventory_detail.html");
    }
    
    /** 
    * @Title: check 
    * @Description: 查看单条库存信息
    * @author liyu
    */
    public void check() {
        // id
        Integer id = getParaToInt();

        // 产品详情
        List<Record> productList = LeaseInventoryService.getProductList(id);
        setAttr("productList", productList);
        
        render("lease_inventory_check.html");
    }
}
