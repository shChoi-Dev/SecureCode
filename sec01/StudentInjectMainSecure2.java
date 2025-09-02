package secure.sec01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.*; // 정규식 클래스 추가

public class StudentInjectMainSecure2 {

	public static void main(String[] args) {
		// 입력값 필터링 - 정규식 활용
		// 문자와 숫자를 제외한 나머지 특수 기호 필터링
		// union 삽입 공격에 사용하는 키워드들은 필터링
		DBConnect dbCon = new DBConnect();
		Connection con = dbCon.getConnection();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Scanner sc = new Scanner(System.in);

		Pattern pattern = Pattern.compile("[^a-zA-Z0-9]|[select|delete|update|insert|create|alter|drop|all|union]");
		Matcher matcher = null;

		try {
			System.out.print("학생 번호 입력 : ");
			String studentNo = sc.nextLine();
			System.out.println(studentNo);

			matcher = pattern.matcher(studentNo.toLowerCase());
			boolean match = true;
			// matcher.find(); // 설정한 정규식 패턴과 전달된 문자열을 매칭시켜서 정규식 패턴에 대한 문자열 확인되면 true 반환
			while (matcher.find()) {
				match = false;
				break;
			}

			// sql 쿼리문 작성 - 플레이스홀더 사용하지 않고 변수 이용해서 코딩(전체컬럼 추출)
			// 필터링 통해 취약점 제거
			if (match) {

				String sql = "select * from student where stdNo='" + studentNo + "'";
				System.out.println(sql);
				pstmt = con.prepareStatement(sql); // 하드코딩된 쿼리 구문 그대로 적용 가능
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
			} else {
				System.out.println("잘못된 입력입니다");
			}

		} catch (SQLException e) {
			e.printStackTrace(); // 개발단계에서 개발자가 확인하기 위한 에러 출력문
		}

	}

}
