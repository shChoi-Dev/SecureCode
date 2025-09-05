package encryption.sec01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Scanner;

public class MemberLogin {
	
	public static void main(String[] args) {
		// DB에 저장된 비밀번호가 plain text기 때문에 사용자 입력값과 db에서 조회한 값을 바로 비교
		// sql 쿼리를 문자열을 결합해서 동적으로 생성하고 있음 : sql 삽입 공격에 노출됨
		DBConnect dbCon = new DBConnect();
		Connection con = dbCon.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Scanner sc = new Scanner(System.in);
		
		try {
			System.out.print("ID 입력 : ");
			String memID = sc.nextLine();
			
			System.out.print("비밀번호 입력 : ");
			String memPass = sc.nextLine();
			
			//select 쿼리문 작성
			String sql = "select * from member where memID = '" + memID + "'" + "and memPWD = '" + memPass + "'";
			
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			//회원가입 진행 시 중복 아이디 허용하지 않았다고 가정
			//위 쿼리가 진행되었다면 한명에 대한 정보가 나오거나 정보가 추출되지 않았을 것임
			if(rs.next()) {
				System.out.println("로그인 되었습니다");
				//웹에서는 로그인 되었다면 자격증명을 포함해서 클라이언트에게 전달
			} else {
				System.out.println("로그인 실패");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnect.close(con, pstmt, rs);
			sc.close();
		}
		
		
	}
	
}
