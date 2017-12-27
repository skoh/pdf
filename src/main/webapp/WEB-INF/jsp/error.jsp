<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>error</title>
</head>
<body>
  <h2><오류 발생></h2>
  <%
  	Object status_code = request.getAttribute("javax.servlet.error.status_code");
  	status_code = (status_code == null) ? 0 : status_code;
  	out.println("<br/><strong>- 상태코드 : </strong>" + status_code);

  	Object exception_type = request.getAttribute("javax.servlet.error.exception_type");
  	exception_type = (exception_type == null) ? "" : exception_type;
  	out.println("<br/><strong>- 예외클래스 : </strong>" + exception_type);

  	Object message = request.getAttribute("javax.servlet.error.message");
  	message = (message == null) ? "" : message;
  	out.println("<br/><strong>- 메세지 : </strong>" + message);

  	Object request_uri = request.getAttribute("javax.servlet.error.request_uri");
  	request_uri = (request_uri == null) ? "" : request_uri;
  	out.println("<br/><strong>- 요청URI : </strong>" + request_uri);

  	out.println("<br/><strong>- 상세내역</strong><br/>");
  	Object ex = request.getAttribute("javax.servlet.error.exception");
  	if (ex != null) {
  		java.io.StringWriter sw = new java.io.StringWriter();
  		java.io.PrintWriter pw = new java.io.PrintWriter(sw);
  		((Throwable) ex).printStackTrace(pw);
  		out.println(sw.toString().replace("\n", "<br/>"));
  	}
  %>
</body>
</html>