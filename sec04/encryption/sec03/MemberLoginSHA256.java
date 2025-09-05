package encryption.sec03;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Scanner;

public class MemberLoginSHA256 {
	// sha256 static method는 회원가입할 때 사용했던 메서드와 동일해야 함
	// static이기 때문에 기존 메서드를 호출해서 사용하는게 일반적임
	public static String sha256(String pass) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(pass.getBytes());
		return byteToHex(md.digest()); // 16진수 문자열로 변환시키는 함수 호출
	}

	public static String byteToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();

	}

	public static void main(String[] args) throws Exception {
		// 회원가입 시 md5 알고리즘을 사용해서 비밀번호 저장
		// 로그인 진행 할 때는 평문 비밀번호 받아서 저장된 해시값과 같은지 확인
		// 해시값은 역연산이 불가능하므로 로그인 로직에서는입력된 비밀번호를 해시값으로 변경해서
		// 저장된 비밀번호 해시값과 일치하는지 확인
		DBConnect dbCon = new DBConnect();
		Connection con = dbCon.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null; // select 구문

		Scanner sc = new Scanner(System.in);

		try {
			System.out.print("ID 입력 : ");
			String memID = sc.nextLine();

			System.out.print("비밀번호 입력 : ");
			String memPass = sha256(sc.nextLine()); // 회원가입할때와 동일한 알고리즘 사용 해시값으로 변경 후 그 값으로 쿼리 구성
			System.out.println(memPass);
			// select 쿼리문 작성
			String sql = "select * from member where memID = '" + memID + "'" + "and memPWD = '" + memPass + "'";

			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			// 회원가입 진행 시 중복 아이디 허용하지 않았다고 가정
			// 위 쿼리가 진행되었다면 한명에 대한 정보가 나오거나 정보가 추출되지 않았을 것임
			if (rs.next()) {
				System.out.println("로그인 되었습니다");
				// 웹에서는 로그인 되었다면 자격증명을 포함해서 클라이언트에게 전달
			} else {
				System.out.println("로그인 실패");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnect.close(con, pstmt, rs);
			sc.close();
		}

	}

}
