package com.siteview.svdb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpenApiServlet extends HttpServlet {
	private static final StatisticsStatus itf = new StatisticsStatusImpl();
	private static final long serialVersionUID = 87026055815090544L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String id = request.getParameter("svid");
		response.setContentType("text/html; charset=UTF-8");

		PrintWriter out = response.getWriter();
		try {
			Map<String, Integer> map = itf.getStatisticsStatus(username, password, id);
			out.print(map.toString());
		} catch (Exception e) {
			out.print(e.getMessage());
		}finally{
			out.flush();
			out.close();
		}
		
	}

}
