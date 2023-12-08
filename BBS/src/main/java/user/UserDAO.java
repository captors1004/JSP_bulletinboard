package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

	private Connection conn; //DB접근해주게 하는 하나의 객체
	private PreparedStatement  pstmt;
	private ResultSet rs;  //어떤 정보를 담을수 있는 객체
	
	
	public UserDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS"; //localhost:3306 port는 우리컴퓨터에 설치된 MySQL port번호이다.
			String dbID ="root";
			String dbPassword ="root";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
				e.printStackTrace(); //오류가 먼지 출력해준다.
		}
	}
	
	public int login(String userID, String userPassword) {
		String SQL = "SELECT userPassword FROM USER WHERE userID = ?"; //해당사용자의 비밀번호를 가져온다.
	
		try {
			pstmt =conn.prepareStatement(SQL);  //pstmt에 어떤 정해지 SQL문장을 삽입한다.
			pstmt.setString(1, userID);  //SQL injection해킹 기법을 방어하기 위해 PreparedStatement 이용한다.
			rs = pstmt.executeQuery();   //rs에 실행한결과를 넣어준다.
			if(rs.next()) {  //아이디가 있는 경우
				 if(rs.getString(1).equals(userPassword)) {
					 return 1; //로그인 성공
				 } else {
					 return 0; //비밀번호 불일치
				}
			}
			return -1; //아이디가 없음
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2; //데이터베이스 오류
	}
	
	public int join(User user) {
		String SQL = "INSERT INTO USER VALUES (?, ?, ?, ?, ?)";
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	

}