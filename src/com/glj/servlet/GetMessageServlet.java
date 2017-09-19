package com.glj.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
 * 短连接方式获取消息用Servlet
 * @author Jiomer
 *
 */
@WebServlet("/GetMessage.do")
public class GetMessageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMessageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Object obj = request.getSession().getAttribute("user");			//从Session中获取当前用户
		if(obj != null){												//如果用户存在 则读取消息

			response.setContentType("text/xml;charset=utf-8");
			PrintWriter out = response.getWriter();
			StringBuilder sb = new StringBuilder();			//xml字符串拼接对象
			sb.append("<date>");
			
			String username = obj.toString();							//获取用户名
			
			//从Session中获取消息集合
			ConcurrentLinkedQueue<Message> msglist = (ConcurrentLinkedQueue<Message>)(request.getSession().getAttribute("msglist"));
			//如果不存在 则创建一个新的消息集合
			if(msglist == null){
				msglist = new ConcurrentLinkedQueue<Message>();
				
				//写入Session
				request.getSession().setAttribute("msglist",msglist);
				
				//写入Application
				((Map<String, ConcurrentLinkedQueue<Message>>)request.getServletContext().getAttribute("totalmsglist")).put(username, msglist);
			}
			
			Iterator<Message> it = msglist.iterator();
			while(it.hasNext()){
				Message m = it.next();
				sb.append("<msg s=\""+m.getSender()+"\" m=\""+m.getMessage()+"\" t=\""+m.getSendtime().toString()+"\" r=\""+(m.getReader()==null?"":m.getReader())+"\" />");
				it.remove();
			}
			
			sb.append("</date>");
			out.print(sb.toString());		//输入获取到的消息字符串
			out.flush();
			out.close();
		}else {
			response.sendRedirect("login.html");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);
	}

}
