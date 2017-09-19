package com.glj.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.glj.entity.Message;

/**
 * 发送消息
 * @author Jiomer
 */
@WebServlet("/SendMessage.do")
public class SendMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendMessageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String str = request.getParameter("msg");
		String reader =request.getParameter("reader");

		Object obj = request.getSession().getAttribute("user");		//从Session中获取发送者名字
		if(obj == null || str == null || str == ""){	//非空验证
			out.print("false");
		}else{
			Map<String,ConcurrentLinkedQueue<Message>> msglist =(Map<String,ConcurrentLinkedQueue<Message>>)(request.getServletContext().getAttribute("totalmsglist"));
			
			//构造出消息对象 并添加到Application中
			Message msg = new Message();
			msg.setMessage(str);
			msg.setSender(obj.toString());
			msg.setSendtime(new Date());
			if(reader==null || reader.equals("")){
				Iterator<String> mlist = msglist.keySet().iterator();
				while(mlist.hasNext()){
					String key = mlist.next();
					if(!key.equals(obj.toString())){
						msglist.get(key).add(msg);
					}
				}
			}else {
				msg.setReader(reader);
				ConcurrentLinkedQueue<Message> mlist = msglist.get("reader");
				if(mlist !=null){
					mlist.add(msg);
				}
			}
			out.print("true");
		}
		out.flush();
		out.close();
	}

}
