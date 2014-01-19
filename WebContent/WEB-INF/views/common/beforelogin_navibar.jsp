<%@page import="com.imeeting.constants.WebConstants"%>
<%@page import="com.imeeting.bean.UserBean"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	UserBean userBean = (UserBean) session
			.getAttribute(UserBean.SESSION_BEAN);
	String pageName = String.valueOf(request
			.getAttribute(WebConstants.page_name.name()));
%>
<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container">
			<a class="btn btn-navbar" data-toggle="collapse"
				data-target=".nav-collapse"> <span class="icon-bar"></span> <span
				class="icon-bar"></span> <span class="icon-bar"></span> </a> <a
				class="brand" href="/imeetings/home">智会</a>
			<div class="nav-collapse">
				<ul class="nav pull-right">
					<li class="<%="home".equals(pageName) ? "active" : ""%>">
					   <a href="/imeetings/home">首页</a>
					</li>
                    <li class="<%="signup".equals(pageName) ? "active" : ""%>">
                        <a href="/imeetings/signup">注册</a>
                    </li>
					<li class="<%="signin".equals(pageName) ? "active" : ""%>">
					<% if (userBean == null) { %>
						<a href="/imeetings/signin">登录</a>
					<% } else {	%>
						<a href="/imeetings/myconference">进入账户</a>
					<% } %>
					</li>
					<!-- 
				    <li class="<%="deposite".equals(pageName) ? "active" : ""%>">
				        <a href="/imeetings/deposite">在线充值</a>
                    </li>
                     -->
				</ul>
			</div>
		</div>
	</div>
</div>