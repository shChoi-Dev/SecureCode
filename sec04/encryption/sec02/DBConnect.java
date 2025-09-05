package encryption.sec02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnect {
	//데이터베이스 연결 후 
	//connection 객체를 반환하는 메소드
	public Connection getConnection() {
		Connection con = null;
		
		try {
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String user = "SQL_USER";
			String pwd = "1234";
			
			con = DriverManager.getConnection(url,user,pwd);
			
			if(con != null) {
				System.out.println("DB연결 성공!");
			}else {
				System.out.println("DB연결 실패!");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	//자원반환 메소드 : close()
	//static으로 구성, 반환되는 자원에 따라 다르게 사용하도록 메소드 오버로딩
	
	//1. Connection, PreparedStatement, ResultSet 자원 3개 반환
	public static void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
		try {
			if(rs != null) {
				rs.close();
				rs=null;
			}
			
			if(pstmt != null) {
				pstmt.close();
				pstmt=null;
			}
			
			if(con != null) {
				con.close();
				con=null;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//2. Connection, PreparedStatement 자원 2개 반환
	public static void close(Connection con, PreparedStatement pstmt) {
		try {		
			if(pstmt != null) {
				pstmt.close();
				pstmt=null;
			}
			
			if(con != null) {
				con.close();
				con=null;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	//3. Connection 자원 1개 반환
		public static void close(Connection con) {
			try {	
				if(con != null) {
					con.close();
					con=null;
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
	
}
