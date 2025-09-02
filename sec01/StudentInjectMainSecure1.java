package secure.sec01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentInjectMainSecure1 {

	public static void main(String[] args) {
		// PreparedStatement 사용(place holder 추가)코드로 수정
		DBConnect dbCon = new DBConnect();
		Connection con = dbCon.getConnection();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Scanner sc = new Scanner(System.in);

		try {
			System.out.print("학생 번호 입력 : ");
			String studentNo = sc.nextLine();
			System.out.println(studentNo);

			// sql 쿼리문 작성 - 플레이스홀더 사용하지 않고 변수 이용해서 코딩
			// 플레이스 홀더 사용 방식으로 수정
			//String sql = "select * from student where stdNo='" + studentNo + "'";
			String sql = "select * from student where stdNo='?";
			System.out.println(sql);
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, studentNo);
			rs = pstmt.executeQuery();

			// 제목 출력
			System.out.println("----------- 학생 정보 조회 -----------");
			System.out.println("학생번호 \t 학생이름 \t\t\t\t 학년");

			// 필요 내용만 추출
			while (rs.next()) {
				String stdNo = rs.getString(1);
				String stdName = rs.getString(2);
				int stdYear = rs.getInt(3);

				// 한 행씩 출력
				System.out.format("%-10s\t %-20s\t %6d \n", stdNo, stdName, stdYear);
			}

			// 리소스 반환
			rs.close();
			pstmt.close();
			con.close();
			sc.close();

		} catch (SQLException e) {
			e.printStackTrace(); // 개발단계에서 개발자가 확인하기 위한 에러 출력문
		}
	}

}
