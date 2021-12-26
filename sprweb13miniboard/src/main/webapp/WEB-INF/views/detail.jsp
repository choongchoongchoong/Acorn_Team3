<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
window.onload = function(){
	document.getElementById("btnList").onclick = function(){
		location.href = "list";
	}
}
</script>
</head>
<body>
** 상세보기 **<p/>
글번호 : ${board.num }<br>
작성자 : ${board.author }<br>
글제목 : ${board.title }<br>
글내용 : ${board.content }<br><br>
작성일 : ${board.bwrite }<br>
조회수 : ${board.readcnt }<br><br>
<input type="button" value="글목록 보기" id="btnList">
</body>
</html>