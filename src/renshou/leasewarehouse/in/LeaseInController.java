package renshou.leasewarehouse.in;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import renshou.interceptor.ManageInterceptor;

/**
 * @ClassName: LeaseInController.java
 * @Description:
 * @author: LiYu
 * @date: 2017年8月3日上午9:16:46
 * @version: 1.0 版本初成
 */
@Before(ManageInterceptor.class)
public class LeaseInController extends Controller {
    /** 
    * @Title: index 
    * @Description: 入库页面
    * @author liyu
    */
    public void index() {
        render("lease_in.html");
    }
    
    /** 
    * @Title: getJson 
    * @Description: 入库数据列表
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
        
        Page<Record> page = LeaseInService.getDataPages(pageindex, pagelimit, warehouse_in_no, company_name);
        
        Map<String, Object> map = new HashMap<String,Object>();
        
        map.put("total", page.getTotalRow());
        
        // 修改入库单仓库名称
        List<Record> list = LeaseInService.modifyWarehouseName(page.getList());
        
        map.put("rows", list);
        
        renderJson(map);
    }
    
    /** 
    * @Title: getRecord 
    * @Description: 获取单条入库数据
    * @author liyu
    */
    public void getRecord() {
        // id
        Integer id = getParaToInt();

        if (id != null) {//编辑
            // 入库单
            Record record = Db.findById("t_lease_warehouse_in", id);
            setAttr("record", record);
            // 入库单产品详情
            List<Record> productList = LeaseInService.getProductList(id);
            setAttr("productList", productList);
        }
        // 客户列表
        List<Record> companyList = LeaseInService.getCompanyList();
        setAttr("companyList", companyList);
        // 仓库列表 
        List<Record> warehouseList = LeaseInService.getWarehouseList();
        setAttr("warehouseList", warehouseList);
        
        render("lease_in_detail.html");
    }
    
    /** 
    * @Title: check 
    * @Description: 查看单条入库信息
    * @author liyu
    */
    public void check() {
        // id
        Integer id = getParaToInt();
        // 入库单
        Record record = Db.findById("t_lease_warehouse_in", id);
        setAttr("record", record);
        // 入库单产品详情
        List<Record> productList = LeaseInService.getProductList(id);
        setAttr("productList", productList);
          
        render("lease_in_check.html");
    }
    
    /** 
    * @Title: save 
    * @Description: 保存入库单
    * @author liyu
    */
    public void save() {
        // id
        Long id = getParaToLong("id");
        // 客户
        Integer customer = getParaToInt("customer");
        // 对应仓库 
        Integer warehouse_id = getParaToInt("warehouse_id");
        // 摆放位置
        String location = getPara("location");
        // 产品列表
        String productList = getPara("productList");
        // 入库人
        Record user = getSessionAttr("admin");
        String warehouse_in_person = user.getStr("user_name");
        
        // 返回消息
        Map<String, Object> message = LeaseInService.save(id, customer, warehouse_id, location, productList,warehouse_in_person);
        
        renderJson(message);
    }
    
    /**
     * @desc:批量删除
     * @author liyu
     */
    public void delete(){
        // id
        String idStr = getPara();
        String[] ids = idStr.split(","); 
        // 返回信息
        Map<String, Object> response = new HashMap<>();
        
        boolean result = LeaseInService.delete(ids);
        response.put("isSuccess", result);
        response.put("tips", result ? "删除成功": "删除失败");
        renderJson(response);
    }
    
    /** 
    * @Title: confirm 
    * @Description: 确认入库
    * @author liyu
    */
    public void confirm() {
        // id
        Long id = getParaToLong();
        
        // 返回消息
        Map<String, Object> message = LeaseInService.confirm(id);
        
        renderJson(message);
    }
}
