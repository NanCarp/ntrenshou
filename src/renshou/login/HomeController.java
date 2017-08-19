package renshou.login;

import com.jfinal.core.Controller;

public class HomeController extends Controller {
    public void index(){
        redirect("/pages/login");
    }
}
