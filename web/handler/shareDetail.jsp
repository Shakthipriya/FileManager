<%--
    Document   : shareDetail
    Created on : 2012/10/30, 9:26:51
    Author     : Atsushi OHNO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Share file</title>
</head>
<body>
    <h1>shareDetail.jsp</h1>
    <h2><%=request.getAttribute("isPassForm")%></h2>
    <form method="POST">
        <p>パスワード: <input type="password" name="password"></p>
        <p><input type="submit" name="download" value="Download"></p>
    </form>
</body>
</html>
