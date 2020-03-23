<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
	<head>
		<title>WellnessMonth</title>
	</head>
	<body>
	<c:if test="${not empty confirmation}">
		<span>${confirmation}</span>
	</c:if>