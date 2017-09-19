package com.glj.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ��¼ʱ��Servlet
 * @author Jiomer
 */
@WebServlet("/Login.do")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * doGet
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

	/**
	 * ��¼������
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// �����ַ�����
		request.setCharacterEncoding("utf-8");
		// ��ȡǰ̨user
		String user = request.getParameter("user");
		// ��֤user
		if (user != null && user != "") {
			request.getSession().setAttribute("user", user);
			response.sendRedirect("talk.jsp?type="
					+ request.getParameter("type"));
		} else {
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script type=\"text/javascript\">alert('��¼ʧ��');window.location.href='login.html';</script>");
			out.flush();
			out.close();
		}

	}

}
