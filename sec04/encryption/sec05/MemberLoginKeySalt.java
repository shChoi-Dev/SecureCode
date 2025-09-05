package encryption.sec05;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import encryption.sec01.DBConnect;

public class MemberLoginKeySalt {

	public static String Byte_to_String(byte[] tmp) {
		StringBuilder sb = new StringBuilder();
		for (byte b : tmp) {
			sb.append(String.format("%02x", b));
		}

		return sb.toString();
	}

	// 해시값 생성
	public static String Hashing(byte[] password, String Salt) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		// key-stetching
		// password + salt 결합해서 다이제스트 생성
		for (int i = 0; i < 10000; i++) {
			String temp = Byte_to_String(password) + Salt;
			md.update(temp.getBytes());
			password = md.digest();
		}
		return Byte_to_String(password);
	}

	public static void main(String[] args) throws Exception {
		// sha256으로 비밀번호 해시, 스트레칭 + salt 추가
		DBConnect dbCon = new DBConnect();
		Connection con = dbCon.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Scanner sc = new Scanner(System.in);
		
		String dbPwd = null;
		String dbSalt = null;

		try {
			System.out.print("ID 입력 : ");
			String memID = sc.nextLine();

			System.out.print("비밀번호 입력 : ");
			String memPass = sc.nextLine();

			// 웹사이트에서 회원가입 진행 시 id는 유일하게 구성됨(id는 유일하다고 가정함)
			// 해당 id가 있는지 확인 -> 정보 추출
			String sql = "select * from member where memID = '" + memID + "'";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); // 입력된 id의 회원에 대한 정보 저장
			
			if (rs.next()) {
				dbPwd = rs.getString(2); //회원가입시 해시되어져 저장된 비밀번호
				dbSalt = rs.getString(6); // 개인별 저장한 salt 값
				String hashPass = Hashing(memPass.getBytes(), dbSalt);
				System.out.println(dbPwd);
				System.out.println(hashPass);
				
				if(dbPwd.equals(hashPass)) {
					System.out.println("로그인 성공");
				} else {
					System.out.println("로그인 실패"); // id는 존재하는데 비밀번호가 다른경우
				}
				
			} else {
				System.out.println("로그인 실패"); // id가 없는 경우
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnect.close(con, pstmt, rs);
			sc.close();
		}
	}

}
