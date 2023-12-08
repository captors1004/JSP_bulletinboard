<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "bbs.BbsDAO" %>
<%@ page import = "bbs.Bbs" %>
<%@ page import = "java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
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
		}
		int bbsID = 0;
		if(request.getParameter("bbsID") != null){
			bbsID = Integer.parseInt(request.getParameter("bbsID"));
		}
		if(bbsID == 0){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 글입니다.')");
			script.println("location.href ='bbs.jsp'");
			script.println("</script>");
		}
		Bbs bbs = new BbsDAO().getBbs(bbsID);  //현재 작성한 글이 작성한 사람 본인인지 확인할 필요가 있다. 이때 세션관리가 필요하다.
		if(!userID.equals(bbs.getUserID())){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('権限がないです。')");
			script.println("location.href ='bbs.jsp'");
			script.println("</script>");
		} else {
			if(request.getParameter("bbsTitle") == null || request.getParameter("bbsContent") == null
					|| request.getParameter("bbsTitle").equals("") || request.getParameter("bbsContent").equals("")){ //update페이지에서 넘어오는 글제목과 글내용이 매개변수로 넘어온다.
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('入力しない項目があります。')");
					script.println("history.back()");  //이전 page로 돌려 보낸다.
					script.println("</script>");
				} else {
					BbsDAO bbsDAO = new BbsDAO();
					int result = bbsDAO.update(bbsID, request.getParameter("bbsTitle"), request.getParameter("bbsContent"));
					if(result == -1){ //존재하지 않았을때 실행되는 부분
						PrintWriter script = response.getWriter();
						script.println("<script>");
						script.println("alert('修正に失敗しました。')");
						script.println("history.back()");  //이전 page로 돌려 보낸다.
						script.println("</script>");
					} 
					else { //존재할때 실행되는 부분, 회원가입 처리
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