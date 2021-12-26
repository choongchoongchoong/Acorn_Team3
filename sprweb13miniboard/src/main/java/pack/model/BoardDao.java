package pack.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pack.controller.BoardBean;

@Repository
public class BoardDao {
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private DataSource ds;
	
	@Autowired
	public BoardDao(DataSource dataSource) {
		try {
			ds = dataSource;
		} catch (Exception e) {
			System.out.println("연결 실패 : " + e);
		}
	}
	
	public List<BoardDto> list(){  //전체 자료 읽기
		ArrayList<BoardDto> list = new ArrayList<BoardDto>();
		
		try {
			conn = ds.getConnection();
			
			String sql = "SELECT * FROM springboard ORDER BY num DESC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardDto boardDto = new BoardDto();
				boardDto.setNum(rs.getInt("num"));
				boardDto.setAuthor(rs.getString("author"));
				boardDto.setTitle(rs.getString("title"));
				boardDto.setContent(rs.getString("content"));
				boardDto.setBwrite(rs.getString("bwrite"));
				boardDto.setReadcnt(rs.getInt("readcnt"));
				list.add(boardDto);
			}
			
		} catch (Exception e) {
			System.out.println("list err : " + e);
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		return list;
	}
	
	public boolean writeData(BoardBean bean) {  //글쓰기
		boolean b = false;
		
		try {
			int newnum = 1;
			String sql = "SELECT MAX(num) FROM springboard";
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				newnum = rs.getInt(1) + 1;  //새 글번호 얻기
			}
			
			sql = "INSERT INTO springboard(num,author,title,content) VALUES(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, newnum);
			pstmt.setString(2, bean.getAuthor());
			pstmt.setString(3, bean.getTitle());
			pstmt.setString(4, bean.getContent());
			
			if(pstmt.executeUpdate() > 0) {  //추가 성공시
				b = true; 
			}
			
		} catch (Exception e) {
			System.out.println("writeData err : " + e);
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		return b;
	}
	
	public List<BoardDto> search(String name, String value){  //검색 결과 처리
		ArrayList<BoardDto> list = new ArrayList<BoardDto>();
		
		try {
			conn = ds.getConnection();
			
			String sql = "SELECT * FROM springboard WHERE " 
						+ name + " LIKE '%" + value + "%'";
			
			sql += " ORDER BY num DESC";
			System.out.println("sql : " + sql);
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BoardDto boardDto = new BoardDto();
				boardDto.setNum(rs.getInt("num"));
				boardDto.setAuthor(rs.getString("author"));
				boardDto.setTitle(rs.getString("title"));
				boardDto.setContent(rs.getString("content"));
				boardDto.setBwrite(rs.getString("bwrite"));
				boardDto.setReadcnt(rs.getInt("readcnt"));
				list.add(boardDto);
			}
			
		} catch (Exception e) {
			System.out.println("search err : " + e);
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		return list;
	}
	
	
	public BoardDto detail(String num) {
		BoardDto boardDto = null;
		
		try {
			//상세보기 할 때마다 조회수를 증가
			String sql = "UPDATE springboard SET readcnt = readcnt + 1 WHERE num = " + num;
			
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.executeQuery();
			
			//상세보기 대상 레코드 읽기
			sql = "SELECT * FROM springboard WHERE num=" + num;
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				boardDto = new BoardDto();
				boardDto.setNum(rs.getInt("num"));
				boardDto.setAuthor(rs.getString("author"));
				boardDto.setTitle(rs.getString("title"));
				boardDto.setContent(rs.getString("content"));
				boardDto.setBwrite(rs.getString("bwrite"));
				boardDto.setReadcnt(rs.getInt("readcnt"));
				//한개만 읽으므로 list.add 안함
			}
			
		} catch (Exception e) {
			System.out.println("detail err : " + e);
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		return boardDto;
	}
	
	
}
