package com.glj.servlet;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import com.glj.entity.Message;

/**
 * ����������ʱִ�е�Servlet
 * @author Admin
 *
 */
public class Global extends HttpServlet implements ServletContextListener {

	private static final long serialVersionUID = -8680344467984908901L;
	
	private Timer clearTimer =null;		//��������¼��Timer
	
	/**
	 * ������ֹͣʱִ�и��¼�
	 */
    public void contextDestroyed(ServletContextEvent sce) {
    	clearTimer.cancel();
    }

    /**
     * ����������ʱ���г�ʼ��
     */
    public void contextInitialized(ServletContextEvent sce) {
    	//��ȡXML�����ļ����Ƿ�ʹ���̰߳�ȫ������������Ϣ��¼
    	boolean isSynch = sce.getServletContext().getInitParameter("isSynch").equals("1")?true:false;		//��ȡ�Ƿ��߳�ͬ��������
    	
    	//��Application�д�������Ϣ����
    	Map<String, ConcurrentLinkedQueue<Message>> msglist = null;

		if(isSynch){
			msglist = new Hashtable<String, ConcurrentLinkedQueue<Message>>();
		}else {
			msglist = new HashMap<String, ConcurrentLinkedQueue<Message>>();
		}
		sce.getServletContext().setAttribute("totalmsglist", msglist);
		
		//��Application�д����������û�����
		List<String> userlist = null;
		if(isSynch){
			userlist = new Vector<String>();
		}else {
			userlist = new ArrayList<String>();
		}
		sce.getServletContext().setAttribute("userlist", userlist);
		
		//��ȡXML�����ļ���ÿ��������һ�������¼(����Ϊ��λ),�������Զ������
		int task = Integer.parseInt(sce.getServletContext().getInitParameter("clearUserInterval"));
		if(task > 0){
			clearTimer = new Timer();
			clearTimer.schedule(ClearMessageTask.getInstance(msglist), new Date(), task);
		}
    }
    
    /**
     * ��Ϣ��¼�Զ������
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
			System.out.print("������շ���������Ϣ��¼:��ǰ��¼["+msgList.size()+"��]");
			//msgList.clear();
			System.out.println("������,��ʣ��["+msgList.size()+"]��");
		}
    }
}