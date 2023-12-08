<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "user.UserDAO" %>
<%@ page import = "java.io.PrintWriter" %>
<%request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="user" class="user.User" scope="page"/>
<jsp:setProperty name="user" property="userID"/>
<jsp:setProperty name="user" property="userPassword"/>
<jsp:setProperty name="user" property="userName"/>
<jsp:setProperty name="user" property="userGender"/>
<jsp:setProperty name="user" property="userEmail"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP 掲示板 WEB site</title>
</head>
<body>
	<% 
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String) session.getAttribute("userID");
		}
		if(userID != null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('이미 login이 되어 있습니다.')");
			script.println("location.href ='main.jsp'");
			script.println("</script>");
		} 
		if(user.getUserID() == null || user.getUserPassword() == null || user.getUserName() == null 
			|| user.getUserGender() == null || user.getUserEmail() == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('入力しない項目があります。')");
			script.println("history.back()");  //이전 page로 돌려 보낸다.
			script.println("</script>");
		} else{
			UserDAO userDAO = new UserDAO();
			int result = userDAO.join(user);
			
			if(result == -1){ //아이디가 존재하지 않았을때 실행되는 부분
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('userIDは既に存在しています')");
				script.println("history.back()");  //이전 page로 돌려 보낸다.
				script.println("</script>");
			} else { //아이디가 존재할때 실행되는 부분, 회원가입 처리
				PrintWriter script = response.getWriter();
				session.setAttribute("userID", user.getUserID());
				script.println("<script>");
				script.println("location.href ='main.jsp'");  //회원가입이 됬을때는 main페이지로 이동시킨다.
				script.println("</script>");
			}
		}
		
	%>
</body>
</html>