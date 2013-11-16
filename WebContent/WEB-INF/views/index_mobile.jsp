<%@page import="com.imeeting.framework.ContextLoader"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh">
  <head>
    <title>赛果</title>
	<jsp:include page="common/_head.jsp"></jsp:include>
  </head>

  <body>
    <div class="container">
    	<div class="row">
    		<div class="span4 offset2">
    			<div class="row">
    				<div class="span3 offset1">
	    				<div class="app_demo_view">
		    				<img alt="iphone" src="./img/app_screen.png"/>
	    				</div>
    				</div>
    			</div>
    		</div>
    		<div class="span4">
    			<hr>
    			<h3>立刻下载开始你的赛果之旅</h3>
    			<div>
    				<!-- <a class="btn" href="https://itunes.apple.com/us/app/zhi-hui/id554959651?ls=1&mt=8">
    					<div>
    						<img class="pull-left" alt="app store" src="./img/iphone.png">
    						<div class="pull-right">
    							<p><strong>&nbsp;智会 iPhone 版</strong><br>App Store 下载</p>
    						</div>
    					</div>
    				</a> -->
    				<a class="btn" href="/imeetings/downloadapp/2/android">
    					<div>
    						<img class="pull-left" alt="app store" src="./img/android.png">
    						<div class="pull-right">
    							<p><strong>&nbsp;赛果</strong><br>下载 apk 文件</p>
    						</div>
    					</div>
    				</a>    				
    			</div>
    		</div>
    	</div>
    	
    	<jsp:include page="common/_footer.jsp"></jsp:include>
    </div> <!-- /container -->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="/imeetings/js/lib/jquery-1.8.0.min.js"></script>
    <script src="/imeetings/js/lib/bootstrap.min.js"></script>

  </body>
</html>
