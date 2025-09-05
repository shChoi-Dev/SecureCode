package encryption.sec02;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MemberJoinMD5 {
	public static String md5(String pass) {
		String encData = "";
		try {
			// md5 해시알고리즘으로 password 변경 후 반환
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = pass.getBytes(); // 문자열을 byte타입의 배열로 변환
			md.update(bytes); // byte input 값(해시로 변경할 message)을 파라미터로 전달해야 함
			
			// password 해시값으로 변경 - 반환 타입 byte[]
			byte[] digest = md.digest();
			System.out.println(digest);
			for (int i=0; i < digest.length; i++) {
				encData += Integer.toHexString(digest[i] & 0xff);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return encData;
	}

	public static void main(String[] args) {
		// MD5 알고리즘을 활용해서 비밀번호를 해시값으로 변경해서 db에 저장
		DBConnect dbCon = new DBConnect();
		Connection con = dbCon.getConnection();
		PreparedStatement pstmt = null;
		
		Scanner sc = new Scanner(System.in);
		
		try {
			System.out.print("ID 입력 : ");
			String memID = sc.nextLine();
			
			System.out.print("비밀번호 입력 : ");
			String memPass = md5(sc.nextLine()); // static 메서드 md5 호출 비밀번호를 md5형식의 해시값으로 변경
			
			System.out.print("이름 입력 : ");
			String memName = sc.nextLine();
			
			System.out.print("e-mail 입력 : ");
			String memEmail = sc.nextLine();
			
			//회원가입 시간
			LocalDate now = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			String memJoinDate = now.format(formatter);
			String sql = "insert into member values(?,?,?,?,?)";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memID);
			pstmt.setString(2, memPass); // 바인딩되는 패스워드 plain text
			pstmt.setString(3, memName);
			pstmt.setString(4, memEmail);
			pstmt.setString(5, memJoinDate);
			
			int result = pstmt.executeUpdate();
			
			if(result > 0) {
				System.out.println("회원가입 성공");
			}else {
				System.out.println("회원가입 실패");
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			DBConnect.close(con, pstmt);
			sc.close();
		}
		
	}
	
}
