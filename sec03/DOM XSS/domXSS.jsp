<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <style>
        
    </style>
    <script type="text/javascript">
		// 서버에서 해석해야 할 내용이 없는 공격, dombased 공격에 취약한 코드가 될 수 있음
		const hash = window.location.hash.slice(1); // #기호뒤에 문자열이 있으면 해당 문자열 반환, slice(idx) idx부터 끝까지 추출 hash.slice(1) -> hash 문자열이 #first.jsp면 first.jsp
		if(hash) { //first.jsp -> 일반 문자열 (url 형식으로 구성해야 href 에 대입될 수 있음)
			window.location.href= decodeURIComponent(hash); //first.jsp -> /first.jsp로 구성
		}
		
		window.addEventListener('hashchange', function(){
			window.location.href=decodeURIComponent(window.location.hash.slice(1));
		});
    </script>
    <meta charset="UTF-8">
    <title>DOM Based XSS 공격</title>
</head>

<body>
<h1>DOM Based XSS 공격</h1>

<div class="container">

	<!-- 
		http://localhost:8080/SecureDOMXSS/domXSS.jsp -> First 바로가기 클릭 -> http://localhost:8080/SecureDOMXSS/domXSS.jsp#first.jsp -> #제외 /first.jsp
	--> 

    <a id="first" href="#first.jsp" class="item">First 바로가기</a>
    <a id="second" href="#second.jsp" class="item">Second 바로가기</a>
</div>

</body>
</html>