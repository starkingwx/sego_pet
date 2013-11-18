<%@page import="com.imeeting.framework.ContextLoader"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh">
<head>
<title>赛果</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<style>
	* {
		margin: 0;
		padding: 0;
	}
	
	.header {
	 	width: 1024px;
	 	height: 525px;
	   	background: url(/imeetings/img/simple/header.jpg);
	   	margin: 0 auto;
	   	position: relative;
	 }
	 
	 .android {
	 	display: block;
	 	width: 234px;
	 	height: 68px;
	 	background: url(/imeetings/img/simple/android_download.png);
	 	position: absolute;
	 	top: 300px;
	 	left: 300px;
	 }
	 
	 .iphone {
	 	display: block;
	 	width: 234px;
	 	height: 68px;
	 	background: url(/imeetings/img/simple/iphone_download.png);
	 	position: absolute;
	 	top: 300px;
	 	left: 50px;	 	
	 }	 
	 
	 .headinfo {
	 	position: absolute;
	 	top: 420px; 	
	 }
	 
	 .info {
	 	margin: 0 60px;
	 }
	 
	 .info h1 {
	 	font-size: 16px;
	 	margin-bottom: 10px;
	 }	 
	 
	 .content {
	 	width: 1024px;
	 	height: 600px;
	 	background: url(/imeetings/img/simple/body_line.jpg);
	 	margin: 0 auto;
	 }
	 
	 .arrow {
	 	width: 16px;
	 	height: 16px;
	 	background: url(/imeetings/img/simple/right_arrow.png);
	 	display: inline-block;
	 }
	 
	 .content ul {
	 	list-style-type: none;
	 	margin: 0 60px;
	 }
	 
	 .content ul li {
	 	display: block;
	 	width: 204px;
	 	height: 535px;
	 	background: url(/imeetings/img/simple/gray_bg.png);
	 	float: left;
	 	margin: 0 10px;
	 }
	 
	 .snapshot {
	 	width: 204px;
	 	height: 369px;
	 }
	 
	 #divSnapShot1 {
	 	background: url(/imeetings/img/simple/snapshot_01.png);
	 }
	 
	 #divSnapShot2 {
	 	background: url(/imeetings/img/simple/snapshot_02.png);
	 }
	 
	 #divSnapShot3 {
	 	background: url(/imeetings/img/simple/snapshot_03.png);
	 }	
	 
	 #divSnapShot4 {
	 	background: url(/imeetings/img/simple/snapshot_04.png);
	 }
	 
	 .button {
	 	width: 160px;
	 	height: 37px;
	 	background: url(/imeetings/img/simple/yellow_bt.png);
	 	margin: 0 22px;
	 }
	 
	 .button span {
		display: block;
		padding-top: 9px;
		text-align: center;
		font-weight: bold;
		color: white;
	 }
	 
	 .description p {
	 	padding: 10px;
	 }
	 
	 .footer {
	 	width: 1024px;
	 	height: 100px;
	 	background: #914511;
	 	margin: 0 auto;
	 }
	 
	 .footer p {
	 	color: #999;
	 	padding: 10px;
	 }
	 
	 body {
	 	background-color: #e0ded9;
	 }
</style>
</head>

<body>
<div class="header">
	<a class="iphone" href="#">
	</a>
	<a class="android" href="http://www.wetalking.net/imeetings/downloadapp/2/android">
	</a>
	<div class="headinfo info">
		<h1>智会说明<i class="arrow"></i></h1>
		<p>智会是基于Android和iPhone的会议办公应用。通过独创技术，可以使用手机方便发起多方通话。智会应用旨在为您带来
		随时随地安排开启电话会议的体验。智会支持绑定手机号码或者邮箱，可以在多个设备上同步，查看和安排会议。无论您身在
		何方，智会为您打造手机上的移动会议室。
		</p>
	</div>	
</div>
<div class="content">
		<form id="file_upload" action="/segopet/petinfo/uploadavatar" method="POST"
			enctype="multipart/form-data">
			<input id="select_doc" type="file" name="avatar_file" /> 
			<input
				type="text" value="" name="petid" />
			<input
				type="text" value="" name="username" />
			<input type="submit" value="Submit"/>
		</form>
		
		<p>
		<form action="/segopet/gallery/uploadphoto" method="POST"
			enctype="multipart/form-data">
			<input type="file" name="photo_file" /> 
			<input
				type="text" value="" name="galleryid" placeholder="gallery id"/>
			<input
				type="text" value="" name="username" placeholder="user name" />
			<input
				type="text" value="" name="name" placeholder="photo name" />
			<input
				type="text" value="" name="type" placeholder="photo type" />
			<input
				type="text" value="" name="descritpion" placeholder="photo descritpion" />
			
			<input type="submit" value="Submit"/>
		</form>
		</p>
	</div>
<div class="footer">
	<p>
		<span>© 合肥优云信息技术有限公司</span>
		<span>皖ICP备12016494号</span>
	</p>
</div>
</body>
</html>
