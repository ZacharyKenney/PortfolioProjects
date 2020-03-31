<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
	<head>
		<title>WellnessMonth</title>
		
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
		<link href="https://fonts.googleapis.com/css?family=Merienda&display=swap" rel="stylesheet">
		<c:url value="/css/wellnessMonth.css" var="cssHref"/>
		<link rel="stylesheet" href="${cssHref}">
		
	</head>
	<body>
		<header>
			<div id="wMHeader">
				<c:url value="/" var="homeURL"/>
				<a href="${homeURL}"><img class="headerLogo" src="img/WMLogo.png"></a>
				<span>Wellness Month</span>
				<img class="headerLogo" src="img/ZKLogo.png">
			</div>
			<c:if test="${not empty confirmation}">
				<span>${confirmation}</span>
			</c:if>
			<c:if test="${not empty user}">
				<div id="userInfo">
					<div id="userInfo-body">
						${user.name}
						<img id="userImg" src="img/account.png">
						Points: ${user.points} <br>
						Team: ${userTeam.name}
					</div>
				</div>
			</c:if>
		</header>
		