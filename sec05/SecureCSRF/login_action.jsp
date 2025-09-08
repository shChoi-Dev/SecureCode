<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
// 요청하면서 전달된 파라미터 값을 추출해서 변수에 저장
request.setCharacterEncoding("utf-8"); // 전달된 파라미터의 한글처리
String id = request.getParameter("id");
String pw = request.getParameter("pass");
System.out.println(id);
System.out.println(pw);
try
{
	Class.forName("oracle.jdbc.driver.OracleDriver");
	String db_address = "jdbc:oracle:thin:@localhost:1521:xe";
	String db_username = "SQL_USER";
	String db_pwd = "1234";
	Connection connection = DriverManager.getConnection(db_address, db_username, db_pwd);
    
    // request.setCharacterEncoding("UTF-8");
    
    String insertQuery = "SELECT * FROM member WHERE memid='" + id +"' and mempwd='"+pw+"'";
    
    PreparedStatement psmt = connection.prepareStatement(insertQuery);
    
    ResultSet result = psmt.executeQuery();
    
    if(result.next()){ // 반환된 레코드가 1개 있으면
    	String dbid = result.getString("memid");
    	//String dbpwd = result.getString("mempwd");
    	String dbname = result.getString("memname");
		//로그인증명을 위한 session 생성 - 클라이언트별로 생성되므로 클라이언트를 구별할 수 있는 값을 이용해서 세션 속성을 만듬
		session.setAttribute("S_ID", dbid); // 서버측에 특정id의 세션공간을 만들고 S_ID=dbid변수의 값이 저장
		session.setAttribute("S_name", dbname);
    }
	response.sendRedirect("post_list.jsp"); //로그인 완료 후 목록 페이지 이동(브라우저에 의해서 post_list.jsp가 요청되게 됨)
}catch (Exception ex)
{	ex.printStackTrace();
    out.println("오류가 발생했습니다. 오류 메시지 : " + ex.getMessage());
}
%>