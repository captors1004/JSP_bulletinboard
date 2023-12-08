<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "bbs.BbsDAO" %>
<%@ page import = "java.io.PrintWriter" %>
<%request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="bbs" class="bbs.Bbs" scope="page"/>
<jsp:setProperty name="bbs" property="bbsTitle"/>
<jsp:setProperty name="bbs" property="bbsContent"/>

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
		if(userID == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('loginをしてください。')");
			script.println("location.href ='login.jsp'");
			script.println("</script>");
		} else {
			if(bbs.getBbsTitle() == null || bbs.getBbsContent() == null){
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('入力しない項目があります。')");
					script.println("history.back()");  //이전 page로 돌려 보낸다.
					script.println("</script>");
				} else{
					BbsDAO bbsDAO = new BbsDAO();
					int result = bbsDAO.write(bbs.getBbsTitle(), userID, bbs.getBbsContent());
					
					if(result == -1){ //존재하지 않았을때 실행되는 부분
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("alert('書き込みに失敗しました。')");
						script.println("history.back()");  //이전 page로 돌려 보낸다.
						script.println("</script>");
					} else { //존재할때 실행되는 부분, 회원가입 처리
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("location.href ='bbs.jsp'");  //글쓰기 성공했을때는 bbs페이지로 이동시킨다.
						script.println("</script>");
					}
				}
		}
		
	%>
</body>
</html>