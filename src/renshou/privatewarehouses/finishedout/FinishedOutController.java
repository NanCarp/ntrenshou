package renshou.privatewarehouses.finishedout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.json.FastJson;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import renshou.interceptor.ManageInterceptor;
import renshou.leasewarehouse.in.LeaseInService;
import renshou.leasewarehouse.out.LeaseOutService;
import renshou.privatewarehouses.finishedin.FinishedInService;

/**
 * @ClassName: FinishedOutController.java
 * @Description:
 * @author: LiYu
 * @date: 2017年8月10日下午5:45:14
 * @version: 1.0 版本初成
 */
@Before(ManageInterceptor.class)
public class FinishedOutController extends Controller {
    /** 
    * @Title: index 
    * @Description: 成品出库管理页面
    * @author liyu
    */
    public void index() {
        render("finishedout.html");
    }
    
    /** 
    * @Title: getJson 
    * @Description: 成品出库列表数据
    * @author liyu
    */
    public void getJson() {
        String outgoing_number = getPara("outgoing_number"); // 出库单号
        String user_name = getPara("user_name"); // 用户
        
        Integer pageindex = 0;
        Integer pagelimit = getParaToInt("limit")==null? 12 :getParaToInt("limit");
        Integer offset = getParaToInt("offset")==null?0:getParaToInt("offset");
        if(offset!=0){
            pageindex = offset/pagelimit;
        }
        pageindex += 1;
        
        Page<Record> page = FinishedOutService.getFinishedIn(pageindex, pagelimit, outgoing_number, user_name);
        
        Map<String, Object> map = new HashMap<String,Object>();
        
        map.put("total", page.getTotalRow());
        map.put("rows", page.getList());
        
        renderJson(map);
    }
    
    /** 
    * @Title: getRecord 
    * @Description: 新增、编辑出库页面
    * @author liyu
    */
    public void getRecord() {
        // 出库单 id
        Long id = getParaToLong();
        
        if (id != null) {
            // 出库单主表信息
            Record outgoing = FinishedOutService.getFinishedOut(id);
            setAttr("outgoing", outgoing);
            // 出库单产品详情
            List<Record> outgoingDetailList = FinishedOutService.getFinishedOutDetailList(id);
            setAttr("outgoingDetailList", outgoingDetailList);
        }
        // 客户列表
        List<Record> companyList = FinishedOutService.getCompanyList();
        setAttr("companyList", companyList);
        // 库存所有产品余量
        List<Record> stockDetailList = FinishedOutService.getStockDetailList();
        // 去除余量为 0 的产品
        setAttr("stockList", JsonKit.toJson(stockDetailList));
        
        render("finishedout_detail.html");
    }
    
    /** 
    * @Title: getStockList
    * @Description: 查询库存所有产品
    * @author liyu
    */
    public void getStockList() {
        // 库存所有产品
        List<Record> stockDetailList = FinishedOutService.getStockDetailList();
        setAttr("stockList", JsonKit.toJson(stockDetailList));
        render("finishedout_detail_add.html");
    }
    
    /** 
    * @Title: getProductStockDetail 
    * @Description: 根据产品编号查询某一产品在各个仓库的位置及数量
    * @author liyu
    */
    public void getStockDetailByProductNo() {
        // 产品编码
        String finished_number = getPara();
        // 产品-仓库位置-库存 列表
        List<Record> stockDetailList = FinishedOutService.getStockDetailByProductNo(finished_number);
        setAttr("stockDetailList", stockDetailList);
        
        render("finishedout_stock_detail.html");
    }
    
    /** 
    * @Title: save 
    * @Description: 保存出库信息
    * @author liyu
    */
    public void save() {
        // 出库单 id
        Long finished_product_outgoing_id = getParaToLong("id");
        // 出库详情列表
        String outgoingDetailList = getPara("outgoingDetailList");
        // 客户
        Integer company_id = getParaToInt("company_id");
        // 入库人
        Record user = getSessionAttr("admin");
        Integer user_id = user.getInt("id");
        // 备注
        String remark = getPara("remark");
        
        // 返回消息
        Map<String, Object> message = FinishedOutService.save(finished_product_outgoing_id, outgoingDetailList, user_id, company_id, remark);
        
        renderJson(message);
        
    }
    
    /** 
    * @Title: confirm 
    * @Description: 确认出库
    * @author liyu
    */
    public void confirm() {
        // id
        Long id = getParaToLong();
        
        // 返回消息
        Map<String, Object> message = FinishedOutService.confirm(id);
        
        renderJson(message);
    }
    
    /** 
    * @Title: delete 
    * @Description: 删除出库信息
    * @author liyu
    */
    public void delete(){
        // id
        String idStr = getPara();
        String[] ids = idStr.split(","); 
        // 返回信息
        Map<String, Object> response = new HashMap<>();
        
        boolean result = FinishedOutService.delete(ids);
        response.put("isSuccess", result);
        response.put("tips", result ? "删除成功": "删除失败");
        renderJson(response);
    }
    
    /**
     * @desc 查看
     * @author liyu
     */
    public void look(){
        // 出库单 id
        Long id = getParaToLong();
        Record outgoing = FinishedOutService.getFinishedOut(id);//查询出出库单信息
        setAttr("outgoing", outgoing);
        // 出库单产品详情 同一商品求和
        List<Record> outgoingProductList = FinishedOutService.getOutgoingProductList(id);
        setAttr("outgoingProductList",outgoingProductList);
        
        // List<Record> rlist = FinishedOutService.getFinishedIn(id); //查询入库单明细信息
        // setAttr("rlist", rlist);
        render("finishedout_detail_look.html");
    }
    
    /**
     * @author liyu
     * @desc 根据产品编号查询产品信息
     */
    public void getFinishedByNum(){
        String num = getPara("num");
        List<Record> list = FinishedOutService.getFinishedByNum(num);
        renderJson(list);
    }
}
