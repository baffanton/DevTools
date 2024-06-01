<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"  language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Title</title>
</head>
<body>
<div align="center">
    <h1>Check word in TEXTS</h1>
    <form action="/files" method="get" style="margin: 10px">
        <label>
            <input type="text" name="word" style="height: 40px; font-size: 14px; align-content: center; padding: 0 10px" />
        </label>
        <label>
            <button style="height: 40px; width: 100px;">Check</button>
        </label>
    </form>
    <table
        id="files"
        border="1"
        cellpadding="10"
        style="width: 100%; max-width: 600px; margin-bottom: 20px; border: 5px solid #fff; border-top: 5px solid #fff; border-collapse: collapse; outline: 3px solid #ffd300; font-size: 15px; background: #fff!important;">
        <tr style="border-bottom: 3px solid #ffd300">
            <td align="center">File Path</td>
            <td align="center">Count</td>
        </tr>
        <c:forEach var="item" items="${requestScope.files}">
            <tr>
                <td align="center"><a href="/download/${item.key}"><c:out value="${item.key}"/></a></td>
                <td align="center"><c:out value="${item.value}"/></td>
            </tr>
        </c:forEach>
    </table>
    <br />
</div>
</body>
</html>