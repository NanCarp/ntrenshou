package renshou.leasewarehouse;

import com.jfinal.core.Controller;

/**
 * @ClassName: LeaseInController.java
 * @Description:
 * @author: LiYu
 * @date: 2017年8月3日上午10:36:35
 * @version: 1.0 版本初成
 */
public class LeaseInController extends Controller {
    // 页面
    public void index() {
        render("lease_in.html");
    }
}
