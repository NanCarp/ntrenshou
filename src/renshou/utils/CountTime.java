package renshou.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;

public class CountTime implements Runnable {
	HttpSession session;
	String username;
	public CountTime(String username,HttpSession session){		
		this.session = session;
		this.username = username;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Map<String, Integer> map = (Map<String, Integer>) session.getAttribute("countMap");
		Integer count = map.get(username);		
		while(count>2){
			try {
				Thread.currentThread().sleep(1000*20);
				count--;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		map.put(username, 0);
		session.setAttribute("countMap", map);
		System.out.println("time out");
	}
}
