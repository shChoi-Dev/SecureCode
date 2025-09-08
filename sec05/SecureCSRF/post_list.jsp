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
<title>게시글 목록</title>
</head>
<body>
	<form action="post_read.jsp" method="get">
		<h1>게시글 목록</h1>
		<%
			// 로그인 증명 확인위해 session 값을 조회(추출)
			String S_ID = (String)session.getAttribute("S_ID"); // 로그인된 사용자의 요청인지 확인용
			String S_Name = (String)session.getAttribute("S_name"); // 게시글 목록은 모든 사용자의 게시글을 볼 수 있기 때문에 S_name session 속성을 사용안함
			if(S_ID != null){ // 로그인된 사용자만 목록보기가 가능하도록, 로그인되지 않은경우 url통해 직접 접근도 금지
			try
			{
				Class.forName("oracle.jdbc.driver.OracleDriver");
				String db_address = "jdbc:oracle:thin:@localhost:1521:xe";
				String db_username = "SQL_USER";
				String db_pwd = "1234";
				
				Connection connection = DriverManager.getConnection(db_address, db_username, db_pwd);
				
				String insertQuery = "SELECT * FROM pratice_board order by num desc";
				PreparedStatement psmt = connection.prepareStatement(insertQuery);
				ResultSet result = psmt.executeQuery();%>
				
				<table border="1">
					<tr>
						<td colspan="4">
							<h3>게시글 제목 클릭시 상세 열람 가능</h3>
						</td>
					</tr>
					<tr>
					    <td colspan="45">
					        <button type="button" value="신규 글 작성" onClick="location.href='post_new.jsp'">신규 글 작성</button>
					        <button type="button" value="로그아웃" onClick="location.href='logout.jsp'">로그아웃</button>
					    </td>
					</tr>
					<tr>
						<td>번호</td>
						<td>작성자</td>
						<td>제목</td>
						<td>작성일</td>
					</tr>
					<%
					while (result.next())
					{%>
						<tr>
							<td><%=result.getInt("num") %></td>
							<td><%=result.getString("writer") %></td>
							<td><a href="post_read.jsp?num=<%=result.getInt("num") %>"><%=result.getString("title") %></a></td>
							<td><%=result.getTimestamp("regdate") %></td>
						</tr>
					<%
					}%>
				</table>
			<%
			}
			catch (Exception ex)
			{
				out.println("오류가 발생했습니다. 오류 메시지 : " + ex.getMessage());
			}
			
			} else {
				response.sendRedirect("main.html"); //session 정보를 페이지 요청시 갖고 오지 않았으면 메인페이지로 이동
			}%>
	</form>
</body>
</html>