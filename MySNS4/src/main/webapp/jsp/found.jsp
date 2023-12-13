<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="dao.*" %>

<%
	request.setCharacterEncoding("utf-8");
	out.print((new FeedDAO()).found(request.getParameter("id")));
	
%>

