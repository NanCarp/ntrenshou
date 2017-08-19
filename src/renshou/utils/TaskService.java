package renshou.utils;

import java.util.TimerTask;

import com.jfinal.plugin.activerecord.Db;
/**
 * 
 * @desc 设置提示信息
 */
public class TaskService extends TimerTask {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String sql ="update t_user set type = 1";
		Db.update(sql);
	}

}
