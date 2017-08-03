package renshou.system.login;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

import renshou.interceptor.ManageInterceptor;
/**
 * @ClassName: SysLoginController
 * @Description: 系统管理_基础数据管理
 * @author: xuhui
 * @date: 2017年5月15日下午2:00:00
 * @version: 1.0 版本初成
 */
@Before(ManageInterceptor.class)
public class SysLoginController extends Controller {
	 public void index() {
		 	String accountnum = getPara("accountnum");
		 	String beginyear = getPara("beginyear");
		 	String lastyear = getPara("lastyear");	 		 	
	    	List<Record> userloginList = LoginService.getUserLog(accountnum,beginyear,lastyear);
	    	setAttr("userloginList", userloginList);
	    	setAttr("accountnum", accountnum);
	    	setAttr("beginyear", beginyear);
	    	setAttr("lastyear", lastyear);	
	    	render("login_log.html");
	    }
	 
}
