<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="dao.*" %>

<%
    FeedDAO dao = new FeedDAO();
	request.setCharacterEncoding("utf-8");
    int num = Integer.parseInt(request.getParameter("no"));
    String did = request.getParameter("id");
  

	if (dao.cancel(num,did)==true) {
		out.print("OK");
	}
	else{
		out.print("ER");
	}
%>

