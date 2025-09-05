package encryption.sec05;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom; //암호화 시 사용권장되는 메소드

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.random.*; //암호학적으로 충분한 난수를 추출하지 않음, 암호화 진행시 사용 제한

import encryption.sec04.DBConnect;

public class MemberJoinKeySalt {

	// salt크기 설정
	private static final int SALT_SIZE = 16;

	public static String getSalt() throws Exception {
		SecureRandom rnd = new SecureRandom();
		byte[] temp = new byte[SALT_SIZE];

		rnd.nextBytes(temp); // 배열 크기만큼의 난수 발생시켜서 배열에 저장
		// slat 사용하려면 문자열로 사용하는게 일반적(회원마다 다른 slat 사용 - db에 저장할 예정)

		return Byte_to_String(temp); // 문자열 salt가 반환 됨
	}

	// 바이트 값을 16진수로 변경해준다
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

		Scanner sc = new Scanner(System.in);

		try {
			System.out.print("ID 입력 : ");
			String memID = sc.nextLine();

			System.out.print("비밀번호 입력 : ");
			String memPass = sc.nextLine();

			String memSalt = getSalt(); //회원 개인별 salt값 저장해야 함
			memPass = Hashing(memPass.getBytes(), memSalt);
			System.out.println(memPass);
			
			System.out.print("이름 입력 : ");
			String memName = sc.nextLine();

			System.out.print("e-mail 입력 : ");
			String memEmail = sc.nextLine();

			// 회원가입 시간
			LocalDate now = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
			String memJoinDate = now.format(formatter);
			
			String sql = "insert into member values(?,?,?,?,?,?)";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memID);
			pstmt.setString(2, memPass);
			pstmt.setString(3, memName);
			pstmt.setString(4, memEmail);
			pstmt.setString(5, memJoinDate);
			pstmt.setString(6, memSalt);

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("회원가입 성공");
			} else {
				System.out.println("회원가입 실패");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBConnect.close(con, pstmt);
			sc.close();
		}

	}

}
