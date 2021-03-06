package com.glj.servlet;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import com.glj.entity.Message;

/**
 * 服务器启动时执行的Servlet
 * @author Admin
 *
 */
public class Global extends HttpServlet implements ServletContextListener {

	private static final long serialVersionUID = -8680344467984908901L;
	
	private Timer clearTimer =null;		//清空聊天记录用Timer
	
	/**
	 * 服务器停止时执行该事件
	 */
    public void contextDestroyed(ServletContextEvent sce) {
    	clearTimer.cancel();
    }

    /**
     * 服务器启动时进行初始化
     */
    public void contextInitialized(ServletContextEvent sce) {
    	//获取XML配置文件中是否使用线程安全的容器保存消息记录
    	boolean isSynch = sce.getServletContext().getInitParameter("isSynch").equals("1")?true:false;		//获取是否线程同步的设置
    	
    	//在Application中创建出消息集合
    	Map<String, ConcurrentLinkedQueue<Message>> msglist = null;

		if(isSynch){
			msglist = new Hashtable<String, ConcurrentLinkedQueue<Message>>();
		}else {
			msglist = new HashMap<String, ConcurrentLinkedQueue<Message>>();
		}
		sce.getServletContext().setAttribute("totalmsglist", msglist);
		
		//在Application中创建出在线用户集合
		List<String> userlist = null;
		if(isSynch){
			userlist = new Vector<String>();
		}else {
			userlist = new ArrayList<String>();
		}
		sce.getServletContext().setAttribute("userlist", userlist);
		
		//获取XML配置文件中每隔多久清空一次聊天记录(毫秒为单位),并启动自动清除器
		int task = Integer.parseInt(sce.getServletContext().getInitParameter("clearUserInterval"));
		if(task > 0){
			clearTimer = new Timer();
			clearTimer.schedule(ClearMessageTask.getInstance(msglist), new Date(), task);
		}
    }
    
    /**
     * 消息记录自动清除器
     * @author Admin
     *
     */
    static class ClearMessageTask extends TimerTask{
    	private ClearMessageTask(Map<String, ConcurrentLinkedQueue<Message>> msgList){
    		this.msgList = msgList;
    	}
    	
    	private Map<String, ConcurrentLinkedQueue<Message>> msgList;
    	private static ClearMessageTask clear = null;
    	
    	public static TimerTask getInstance(Map<String, ConcurrentLinkedQueue<Message>> msgList){
    		if(clear == null){
    			clear = new ClearMessageTask(msgList);
    		}
    		return clear;
    	}
    	
		@Override
		public void run() {
			System.out.print("正在清空服务器的消息记录:当前记录["+msgList.size()+"条]");
			//msgList.clear();
			System.out.println("清空完毕,还剩下["+msgList.size()+"]条");
		}
    }
}