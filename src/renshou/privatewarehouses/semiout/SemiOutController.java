package renshou.privatewarehouses.semiout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import renshou.interceptor.FrontInterceptor;
import renshou.interceptor.ManageInterceptor;
import renshou.privatewarehouses.finishedout.FinishedOutService;


/**
 * @author xuhui
 * @desc 半半成品出库
 */
@Before(ManageInterceptor.class)
public class SemiOutController extends Controller{

	/**
	 * @desc 展示清单页码
	 * @author xuhui
	 */
	public void index(){
		render("semiout.html");
	}
	
 	/** 
    * @Title: getJson 
    * @Description: 半成品出库列表数据
    * @author xuhui
    */
    public void getJson() {
        String outgoing_number = getPara("storage_number"); // 出库单号
        String user_name = getPara("user_name"); // 用户
        System.out.println(outgoing_number);
        System.out.println(user_name);
        Integer pageindex = 0;
        Integer pagelimit = getParaToInt("limit")==null? 12 :getParaToInt("limit");
        Integer offset = getParaToInt("offset")==null?0:getParaToInt("offset");
        if(offset!=0){
            pageindex = offset/pagelimit;
        }
        pageindex += 1;
        
        Page<Record> page = SemiOutService.getSemiOut(pageindex, pagelimit, outgoing_number, user_name);
        
        Map<String, Object> map = new HashMap<String,Object>();
        
        map.put("total", page.getTotalRow());
        map.put("rows", page.getList());
        
        renderJson(map);
    }
	    
    /** 
     * @Title: getRecord 
     * @Description: 新增、编辑出库页面
     * @author xuhui
     */
     public void getRecord() {
         // 出库单 id
         Long id = getParaToLong();
         
         if (id != null) {
             // 出库单主表信息
             Record outgoing = SemiOutService.getSemiOut(id);
             setAttr("outgoing", outgoing);
             // 出库单产品详情
             List<Record> outgoingDetailList = SemiOutService.getSemiOutDetailList(id);
             setAttr("outgoingDetailList", outgoingDetailList);
         }     	
         // 客户列表
         List<Record> companyList = FinishedOutService.getCompanyList();
         setAttr("companyList", companyList);
         // 库存所有产品余量
         List<Record> stockDetailList = SemiOutService.getStockDetailList();
         // 去除余量为 0 的产品
         setAttr("stockList", JsonKit.toJson(stockDetailList));
         
         render("semiout_detail.html");
     }
	
     /** 
      * 
      * @Title: getStockList
      * @Description: 查询库存所有产品
      * @author xuhui
      */
      public void getStockList() {
          // 产品编码
          String semimanufactures_number = getPara("semimanufactures_number");
          setAttr("semimanufactures_number", semimanufactures_number);
          // 品名
          String trade_name = getPara("trade_name");
          setAttr("trade_name", trade_name);
          
          // 库存所有产品
          List<Record> stockDetailList = SemiOutService.getStockDetailList(semimanufactures_number, trade_name);
          setAttr("stockDetailList", JsonKit.toJson(stockDetailList));
          render("semiout_detail_add.html");
      }
      
    
      
      /** 
      * @Title: save 
      * @Description: 保存出库信息
      * @author xuhui
      */
      public void save() {
          // 出库单 id
          Long finished_product_outgoing_id = getParaToLong("id");
          // 出库详情列表
          String outgoingDetailList = getPara("outgoingDetailList");
          // 客户
          Integer company = getParaToInt("company_id");
          // 入库人
          Record user = getSessionAttr("admin");
         /* user = Db.findById("t_user", 1); // TODO 测试，完成后删除
          Integer user_id = user.getInt("id");*/
          Record admin = (Record) getSession().getAttribute("admin");
  		  Integer user_id = admin.getInt("id");
  		 // 备注
          String remark = getPara("remark");
  		  
          // 返回消息
          Map<String, Object> message = SemiOutService.save(finished_product_outgoing_id, outgoingDetailList, user_id, company,remark);
          
          renderJson(message);
          
      }
      
      /** 
      * @Title: confirm 
      * @Description: 确认出库
      * @author xuhui
      */
      public void confirm() {
          // id
          Long id = getParaToLong();
          
          // 返回消息
          Map<String, Object> message = SemiOutService.confirm(id);
          
          renderJson(message);
      }
      
      /** 
      * @Title: delete 
      * @Description: 删除出库信息
      * @author xuhui
      */
      public void delete(){
          // id
          String idStr = getPara();
          String[] ids = idStr.split(","); 
          // 返回信息
          Map<String, Object> response = new HashMap<>();
          
          boolean result = SemiOutService.delete(ids);
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
          Record outgoing = SemiOutService.getFinishedOut(id);//查询出出库单信息
          setAttr("outgoing", outgoing);
          // 出库单产品详情 同一商品求和
          List<Record> outgoingProductList = SemiOutService.getOutgoingProductList(id);
          setAttr("outgoingProductList",outgoingProductList);
          
          // List<Record> rlist = FinishedOutService.getFinishedIn(id); //查询入库单明细信息
          // setAttr("rlist", rlist);
          render("semiout_detail_look.html");
      }
}
