package bbs;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {

	private Connection conn; //DB접근해주게 하는 하나의 객체
	private ResultSet rs;  //어떤 정보를 담을수 있는 객체
	
	public BbsDAO() {
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
	
	public String getDate(){
		String SQL ="SELECT NOW()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //sql문장을 싱행 준비단계로 만들어 준다.
			rs = pstmt.executeQuery(); //실제로 실행했을때 나오는 결과를 가져온다.
			if(rs.next()) { //결과가 있는 경우
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; //데이터베이스오류
	}
	
	public int getNext(){
		String SQL ="SELECT bbsID FROM BBS ORDER BY bbsID DESC";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //sql문장을 싱행 준비단계로 만들어 준다.
			rs = pstmt.executeQuery(); //실제로 실행했을때 나오는 결과를 가져온다.
			if(rs.next()) { //결과가 있는 경우
				return rs.getInt(1)+1; //나온 결과에다가 1을 더해 그 다음 게시글에 번호가 들어갈수 있도록 해준다.
			}
			return 1; // 첫번째 게시물인 경우 1을 반환
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}

	public int write(String bbsTitle, String userID, String bbsContent){
		String SQL ="INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //sql문장을 싱행 준비단계로 만들어 준다.
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);  //제목
			pstmt.setString(3, userID);    //userID
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
	
	public ArrayList<Bbs> getList(int pageNumber){
		String SQL ="SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable =1 ORDER BY bbsID DESC LIMIT 10";
		ArrayList<Bbs> list = new ArrayList<Bbs>();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //sql문장을 싱행 준비단계로 만들어 준다.
			pstmt.setInt(1, getNext() - (pageNumber - 1)*10);
			rs = pstmt.executeQuery(); //실제로 실행했을때 나오는 결과를 가져온다.
			while(rs.next()) { 	        //결과가 있는 경우
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				list.add(bbs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list; 
	}
	
	//페이징 처리
	public boolean nextPage(int pageNumber) { //10단위로 끊기면 다음페이지가 없다는걸 알려준다.
		String SQL ="SELECT * FROM BBS WHERE bbsID < ? AND bbsAvailable =1 ORDER BY bbsID DESC LIMIT 10";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //sql문장을 싱행 준비단계로 만들어 준다.
			pstmt.setInt(1, getNext() - (pageNumber - 1)*10);
			rs = pstmt.executeQuery(); //실제로 실행했을때 나오는 결과를 가져온다.
			if(rs.next()) { 	        //결과가 있는 경우
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//특정한 ID에 해당하는 게시글을 그대로 가져올 수 있도록 한다.
	public Bbs getBbs(int bbsID) {
		String SQL ="SELECT * FROM BBS WHERE bbsID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //'bbsID=숫자'에 해당하는 게시글을 가져온다.
			pstmt.setInt(1, bbsID);
			rs = pstmt.executeQuery(); //실제로 실행했을때 나오는 결과를 가져온다.
			if(rs.next()) { 	        //결과가 있는 경우
				Bbs bbs = new Bbs();
				bbs.setBbsID(rs.getInt(1));
				bbs.setBbsTitle(rs.getString(2));
				bbs.setUserID(rs.getString(3));
				bbs.setBbsDate(rs.getString(4));
				bbs.setBbsContent(rs.getString(5));
				bbs.setBbsAvailable(rs.getInt(6));
				return bbs;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; //해당 글이 존재하지 않는경우 null을 반환
	}
	
	public int update(int bbsID, String bbsTitle, String bbsContent) {
		String SQL ="UPDATE BBS SET bbsTitle=?,bbsContent=? WHERE bbsID=?"; //특정한 ID에 해당하는 제목과, 내용을 바꿔준다.
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //sql문장을 싱행 준비단계로 만들어 준다.
			pstmt.setString(1, bbsTitle);  //제목
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
	
	public int delete(int bbsID) {
		String SQL ="UPDATE BBS SET bbsAvailable=0 WHERE bbsID=?"; // 글을 삭제 해더라도 그에 대한 정보가 남아 있을 수 있도록 bbsAvailable = 0 으로 한다.
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); //sql문장을 싱행 준비단계로 만들어 준다.
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
}