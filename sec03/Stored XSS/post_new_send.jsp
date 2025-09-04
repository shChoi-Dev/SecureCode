<%@page import="java.sql.Timestamp"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
try {
	Class.forName("oracle.jdbc.driver.OracleDriver");
	String db_address = "jdbc:oracle:thin:@localhost:1521:xe";
	String db_username = "SQL_USER";
	String db_pwd = "1234";
	Connection connection = DriverManager.getConnection(db_address, db_username, db_pwd);

	String insertQuery = "SELECT MAX(num) FROM pratice_board";
	PreparedStatement psmt = connection.prepareStatement(insertQuery);
	ResultSet result = psmt.executeQuery(); // 조회된 데이터가 반환되면

	int num = 0;

	while (result.next()) {
		num = result.getInt("MAX(num)") + 1; // 지금 입력되는 게시글의 게시글 번호로 사용
	}

	Timestamp today_date = new Timestamp(System.currentTimeMillis()); // 지금 저장되는 게시글의 작성일
	request.setCharacterEncoding("UTF-8"); // 파라미터 값이 한글일 경우 조합방법 설정
	String writer = request.getParameter("writer");
	String title = request.getParameter("title");
	String content = request.getParameter("content");
	
	// StoredXSS 방어를 위한 입력 값 변경
	writer = writer.replaceAll("<", "&lt;")
				   .replaceAll(">", "&gt;")
				   .replaceAll("\\(", "&#40;")
				   .replaceAll("\\)", "&#41;")
				   .replaceAll("'", "&#x27;");
	
	title = title.replaceAll("<", "&lt;")
			     .replaceAll(">", "&gt;")
			     .replaceAll("\\(", "&#40;")
			     .replaceAll("\\)", "&#41;")
			     .replaceAll("'", "&#x27;");
	
	content  = content.replaceAll("<", "&lt;")
			   		  .replaceAll(">", "&gt;")
			   		  .replaceAll("\\(", "&#40;")
			   		  .replaceAll("\\)", "&#41;")
			   		  .replaceAll("'", "&#x27;");

	insertQuery = "INSERT INTO pratice_board(num, title, writer, content, regdate) VALUES (?,?,?,?,?)";

	psmt = connection.prepareStatement(insertQuery);
	psmt.setInt(1, num);
	psmt.setString(2, title);
	psmt.setString(3, writer);
	psmt.setString(4, content);
	psmt.setTimestamp(5, today_date);

	psmt.executeUpdate();

	response.sendRedirect("post_list.jsp"); // post_new.jsp -> 저장버튼 클릭하면 요청 -> post_new_send.jsp -> db에 데이터 저장 완료되면 요청 -> post_list.jsp

} catch (Exception ex) {
	out.println("오류가 발생했습니다. 오류 메시지 : " + ex.getMessage());
}
%>
