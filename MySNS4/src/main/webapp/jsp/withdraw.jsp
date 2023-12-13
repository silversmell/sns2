<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="dao.*" %>

<%
	request.setCharacterEncoding("utf-8");

	String uid = request.getParameter("id");

	if (new UserDAO().delete(uid)) {
		out.print("OK");
	}
	
	else {
		out.print("WE");
	}
	
%>

