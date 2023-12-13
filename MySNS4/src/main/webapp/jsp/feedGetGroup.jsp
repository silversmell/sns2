<%@ page contentType="text/html" pageEncoding="utf-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="util.*" %>
<%@ page import="dao.*" %>

<%
out.print((new FeedDAO()).getGroup(request.getParameter("frids"),request.getParameter("maxNo")));
	 
%>