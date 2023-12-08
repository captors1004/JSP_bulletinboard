<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP 掲示板 WEB site</title>
</head>
<body>
	<% 
		session.invalidate(); /* 현재 이 페이지에 접속한 회원이 바로 이렇게 세션을 빼앗기도록 만들어서 logout시켜준다.*/
	%>
	<script>
		location.href = 'main.jsp';
	</script>
</body>
</html>