<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글 수정</title>
</head>
<body>
    <h1>글 수정</h1>
    <%
    
	  String S_ID = (String)session.getAttribute("S_ID");
	  String S_name = (String)session.getAttribute("S_name");
	  String CSRF_TOKEN = (String)session.getAttribute("CSRF_TOKEN"); //로그인시 추가된 CSRF_TOKEN 추출, 서버에게 폼데이터로 전달되도록 구성
	  System.out.println(CSRF_TOKEN);
	  
    if(S_ID != null){ 
    try
    {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		String db_address = "jdbc:oracle:thin:@localhost:1521:xe";
		String db_username = "SQL_USER";
		String db_pwd = "1234";
        Connection connection = DriverManager.getConnection(db_address, db_username, db_pwd);
        
        request.setCharacterEncoding("UTF-8");
        
        String num = request.getParameter("num");
        
        String insertQuery = "SELECT * FROM pratice_board WHERE num=" + num;
        
        PreparedStatement psmt = connection.prepareStatement(insertQuery);
        
        ResultSet result = psmt.executeQuery();
        
        while(result.next())
        {%>
            <form action="post_modify_send.jsp" method="post">
            <input type="hidden" name="num" value="<%=result.getInt("num") %>"> <!-- hidden 속성 이용 글번호를 서버에 전송 -->
            <table border="1">
                <tr>
                    <td>작성자</td>
                    <td><input type="text" name="writer" value="<%=result.getString("writer") %>"></td>
                </tr>
                <tr>
                    <td>제목</td>
                    <td><input type="text" name="title" value="<%=result.getString("title") %>"></td>
                </tr>
                <tr>
                    <td>내용</td>
                    <td><textarea rows="10" cols="20" name="content"><%=result.getString("content") %></textarea>
                </tr>
                <tr>
                    <td colspan="2">
                        <button type="submit">수정</button>
                        <button type="button" onclick="location.href='post_list.jsp'">목록으로</button>
                        <button type="reset">원상복구</button>
                    </td>
                </tr>
            </table>
            <% %>
            <input type="hidden" name="csrftoken" value="<%= CSRF_TOKEN%>">
            </form>
    <%
        }
    }
    catch (Exception ex)
    {
    	out.println("오류가 발생했습니다. 오류 메시지 : " + ex.getMessage());
    }
    }else{
    	response.sendRedirect("main.html");
    }
    %>
</body>
</html>