package renshou.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * @ClassName: FrontInterceptor.java
 * @Description: 前端（移动端）拦截器
 * @author: LiYu
 * @date: 2017年5月12日下午4:50:06
 * @version: 1.0 版本初成
 */
public class FrontInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller c = inv.getController();
		inv.invoke();
	}

}