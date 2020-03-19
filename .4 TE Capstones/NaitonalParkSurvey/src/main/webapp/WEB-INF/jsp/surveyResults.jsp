<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:import url="/WEB-INF/jsp/common/header.jsp" />
<div class="cardDiv">
	<c:forEach items="${parkResults}" var="result">
		<c:set var="leaderCard" value=""/>
		<c:if test="${result.getKey().leader}">
			<c:set var="leaderCard" value="leaderCard"/>
		</c:if>
		<div class="card ${leaderCard}">
		<a href="<c:url value="/detail?code=${result.getKey().parkCode}"/>">
			<img class="card-img-top" src="img/parks/${result.getKey().imageName}"/>
			</a>
			<div class="card-body">
				<strong class="card-title"> ${result.getKey().parkName}</strong>
				<p class="card-text">${result.getValue()} Vote(s)</p>
			</div>
		</div>
	</c:forEach>
</div>


<c:import url="/WEB-INF/jsp/common/footer.jsp" />
