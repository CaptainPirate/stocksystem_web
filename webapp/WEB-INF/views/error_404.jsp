<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
<meta http-equiv="pragram" content="no-cache">
<meta http-equiv="cache-control" content="no-cache, must-revalidate"> 
<meta http-equiv="expires" content="0"> 
<title>Page Not Found</title>
<style type="text/css">
	.error_img {
		width:589px;
		height: 410px;
		position: absolute;
		top: 50%;
		left: 50%;
		margin-left: -294px;
		margin-top: -205px;
	}
</style>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js" ></script>
</head>
<body>
	<c:set var="resources" value="${pageContext.servletContext.contextPath }/resources/"></c:set>

	<img class="error_img" alt="" src="${resources}/img/not_found.png" />

	<script type="text/javascript">
		var zoom = $(window).width() / 1080;
		$(window).bind('resize load', function() {
		  $("body").css("zoom", zoom);
		  $("body").css("display", "block");
		});
		window.onresize = function(){
		  var height = $(window).height() / zoom;
		  height -= 86;
		  $("#scroll").css('height', height + 'px');
		}
	</script>
</body>
</html>