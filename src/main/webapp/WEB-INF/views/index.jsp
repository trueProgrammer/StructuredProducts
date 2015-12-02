<%@ page import="java.util.Date" %>
<%@ page import="com.structuredproducts.SpitterService" %>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link href="../../css/bootstrap.min.css" rel="stylesheet">
    <link href="../../css/business-casual.css" rel="stylesheet">

    <script src="../../js/jquery.js"></script>
    <script src="../../js/bootstrap.min.js"></script>

</head>

<body>

    <h1>JSP Examples</h1>
    <p>
        Examples of my JSP!.
    </p>
    <ul>
        <li><a href="/views/index.jsp">main page</a></li>
    </ul>

    <h1>Hello, the time & date is now: <%= SpitterService.RANDOM %></h1>

</body>

</html>
