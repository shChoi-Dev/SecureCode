<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <title>Insert title here</title>
    </head>

    <body>
	<!-- 
		아래 코드는 
		클라이언트가 form 태그를 통해 요청을 진행했을 때 "keyword"파라미터에 저장된 값을 추출해서
		해당 값 그대로 main.jsp에 값을 담아서 응답을 진행
	 -->
	<% 
		//java 코드
		String keyword;
		request.setCharacterEncoding("UTF-8");
		keyword = request.getParameter("keyword");
	%>
	<%
		response.sendRedirect("main4.jsp?keyword=" + keyword);
	%>
    </body>
    
    </html>