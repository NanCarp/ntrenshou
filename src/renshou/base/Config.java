package renshou.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

import renshou.model._MappingKit;
import renshou.utils.TaskService;
import renshou.utils.TaskServiceStop;



/**
 * @ClassName: Config
 * @Description:基础配置文件类
 * @author: LiYu
 * @date: 2017年5月12日 下午1:50:08
 * @version: 1.0 版本初成
 */
public class Config extends JFinalConfig {
	private Timer timer = new Timer();

	@Override
	public void configConstant(Constants me) {
		PropKit.use("config.txt");
		//设置当前环境为开发环境
		me.setDevMode(PropKit.getBoolean("devMode"));
		me.setViewType(ViewType.FREE_MARKER);
		me.setError404View("/pages/error/404.html");
        me.setError500View("/pages/error/500.html");
	}

	@Override
	public void configRoute(Routes me) {
         me.add(new FrontRoutes()); //前端路由
         me.add(new AdminRoutes()); //后端路由
	}

	@Override
	public void configEngine(Engine me) {
	}

	public static DruidPlugin createDruidPlugin() {
        return new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
    }
	
	@Override
	public void configPlugin(Plugins me) {
	 // 配置C3p0数据库连接池插件
        DruidPlugin druidPlugin = createDruidPlugin();
        me.add(druidPlugin);
        
        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        // 所有映射在 MappingKit 中自动化搞定
        _MappingKit.mapping(arp);
        me.add(arp);
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void afterJFinalStart() {
		// TODO Auto-generated method stub
		super.afterJFinalStart();
		String s1 ="2018-8-1 00:00:00";
		String s2 ="2018-9-1 00:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			timer.schedule(new TaskService(), sdf.parse(s1));
			timer.schedule(new TaskServiceStop(), sdf.parse(s2));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void beforeJFinalStop() {
		// TODO Auto-generated method stub
		super.beforeJFinalStop();
	}

}
