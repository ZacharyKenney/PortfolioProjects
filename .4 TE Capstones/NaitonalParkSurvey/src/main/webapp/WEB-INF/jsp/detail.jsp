<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<c:import url="/WEB-INF/jsp/common/header.jsp" />
<img class="detailImg" src="img/parks/${park.imageName}"/>
<br>
<div class="detailDesc">
	<h3>${park.parkName}</h3> 
	<br>
	<span>${park.description}</span>
	<br>
</div>
<table class="parkStats">
	<tr>
		<th>State</th> 
		<th>Climate</th>
		<th>Acreage</th> 	
		<th>Elevation</th> 
		<th>Species</th>
	</tr>
	<tr>
		<td>${park.state}</td>
		<td>${park.climate}</td>
		<td>${park.acreage}</td>
		<td>${park.elevationInFeet}</td>
		<td>${park.numberOfAnimalSpecies}</td>
	</tr>
</table>
<div class="detailDesc">

	<span>${park.parkName} features 
	<fmt:formatNumber type="number" maxFractionDigits="2" value="${park.milesOfTrail}" />
 		miles of trail<c:if test="${park.numberOfCampsites > 0}"> and boasts ${park.numberOfCampsites} unique camp sites</c:if>. It was
		founded in ${park.yearFounded} and received ${park.annualVisitors}
		visitors in the previous year.<c:if test="${park.entryFee > 0}"> Come visit this amazing park for only
		$${park.entryFee}.</c:if></span>
		<br>
</div>
<div class="detailDesc">
	<em>"${park.inspirationalQuote}"</em>
	<br>
	<em>- ${park.inspQuoteSource}</em>
</div>
<c:set value="${weathers.get(0)}" var="today"/>
	<div class="weatherHeader">
		<h4>Five Day Forecast:</h4>
		<span>${today.date.getMonth()} ${today.date.getDayOfMonth()}</span>
	</div>
<div id="todayWeather">
<div class="col-4">
	<img src="img/weather/${today.imageName}" alt="${today.icon}"/>
	
</div >
	<div class="col-8">
		<strong>High: <fmt:formatNumber type="number" maxFractionDigits="0" value="${today.highTemp}" /> 
			<c:if test="${celsius == 't'}"> °C</c:if><c:if test="${celsius != 't'}"> °F</c:if>
		</strong> 
		<strong>Low: <fmt:formatNumber type="number" maxFractionDigits="0" value="${today.lowTemp}" /> 
			<c:if test="${celsius == 't'}"> °C</c:if><c:if test="${celsius != 't'}"> °F</c:if>
		</strong>
		<br>
	<c:if test="${not empty today.getRecommendation()}">
		<strong>Recommendation(s):</strong> 
		<p>${today.getRecommendation()}</p>
	</c:if>
	</div>
</div>
<div class="weatherDiv">
<c:forEach var="weather" items="${weathers}">
	<c:if test="${weathers.indexOf(weather) > 0 && weathers.indexOf(weather) < 5}">
		<div>
			<span>${weather.date.getMonth()} ${weather.date.getDayOfMonth()}</span>
			<img src="img/weather/${weather.imageName}" alt="${weather.icon}"/>
			<span>High: <fmt:formatNumber type="number" maxFractionDigits="0" value="${weather.highTemp}" /> <c:if test="${celsius == 't'}"> °C</c:if><c:if test="${celsius != 't'}"> °F</c:if></span>
			<span>Low: <fmt:formatNumber type="number" maxFractionDigits="0" value="${weather.lowTemp}" /> <c:if test="${celsius == 't'}"> °C</c:if><c:if test="${celsius != 't'}"> °F</c:if></span>
			<br>
		</div>
	</c:if>
</c:forEach>
</div>
<c:url value="/detail" var="detailURL"/> 
<form action="${detailURL}" method="GET">
	<input type="hidden" name="code" value="${park.parkCode}">
	<c:choose>
		<c:when test="${empty celsius or celsius == 'f'}">
			<input type="hidden" name="celsius" value="t">
			<button class="btn btn-outline-primary" type="submit">Convert to °C</button>
		</c:when>
		<c:otherwise>
			<input type="hidden" name="celsius" value="f">
			<button class="btn btn-outline-primary" type="submit">Convert to °F</button>
		</c:otherwise>
	</c:choose>
</form>
<c:import url="/WEB-INF/jsp/common/footer.jsp" />

