package renshou.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;


/**
 * @ClassName: ManageInterceptor
 * @Description: 后台系统拦截器
 * @author: xuhui
 * @date: 2017年8月10日下午4:48:37
 * @version: 1.0 版本初成
 */
public class ManageInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller c = inv.getController();
		Record admin = c.getSessionAttr("admin");
		if(admin!=null){
			inv.invoke();
		}else{		
			String ck = inv.getControllerKey();
			if("/pages".equals(ck)){
				c.redirect("/pages/login");
			}else{
				c.renderHtml("<script>window.top.loginOut();</script>");
			}
		}
	}
}
