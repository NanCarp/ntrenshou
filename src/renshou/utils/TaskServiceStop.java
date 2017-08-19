package renshou.utils;

import java.util.TimerTask;

import com.jfinal.plugin.activerecord.Db;

/**
 * 
 * @desc 停用所以账号
 */
public class TaskServiceStop extends TimerTask{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String sql ="update t_user set type = 2";
		Db.update(sql);
	}
}
