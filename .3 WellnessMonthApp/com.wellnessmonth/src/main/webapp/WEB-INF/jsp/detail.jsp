<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:import url="/WEB-INF/jsp/common/header.jsp"/>

<div id="details">
	<h2>${challenge.name}</h2>
	<span>${challenge.points} Points</span>
	<span>${challenge.timeframe}</span>
	<p>${challenge.description}</p>
	<c:url value="/detail" var="deatilURL"/>
	<c:if test="${completed}">
		<h4>Challenge Completed!</h4>
		<c:url value="/" var="homeURL"/>
		<a class="btn btn-outline-secondary" href="${homeURL}">Return Home</a>
	</c:if>
</div>
<c:if test="${!completed}">
	<form action="${detailURL}" method="POST">
		<input type="hidden" name="playerName" value="${user.name}">
		<input type="hidden" name="challengeId" value="${challenge.id}">
		<button class="btn btn-outline-secondary" type="submit">Complete!</button>
	</form>
</c:if>
<c:import url="/WEB-INF/jsp/common/footer.jsp"/>