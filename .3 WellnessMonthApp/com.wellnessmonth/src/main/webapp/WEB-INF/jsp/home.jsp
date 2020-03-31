<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:import url="/WEB-INF/jsp/common/header.jsp"/>

<div id="flex-container">

	<c:url value="/login" var="loginURL"/>
	<c:url value="/register" var="registerURL"/>
	<c:url value="/teams" var="teamsURL"/>
	<c:url value="/teams" var="teamsURL"/>
	<c:url value="/challenge" var="challengeURL"/>
	<c:url value="/contest" var="contestURL"/>
	
	<nav>
		
		<a href="${loginURL}">LOGIN</a>
		<a href="${registerURL}">CREATE ACCOUNT</a>	
		<c:if test="${not empty user}">
			<a href="${teamsURL}?newTeam=true">START NEW TEAM</a>
			<a href="${teamsURL}?newTeam=false">JOIN A TEAM</a>
		</c:if>
		<a href="${contestURL}">START NEW CONTEST</a>	
		<a href="${challengeURL}">ADD NEW CHALLENGE</a>
		
	</nav>
	
	<div id="main">
		<c:forEach items="${challenges}" var="challenge">
			<div class="card mb-3">
				<div class="row no-gutters">
					<div class="col-md-4">
						<img class="card-img" src="img/challenge/${challenge.category}.png"/>
					</div>
					<div class="col-md-8">
						<div class="card-body">
						<p>${challenge.name}</p>
						<c:if test="${not empty user}">
							<c:url value="/detail?id=${challenge.id}" var="detailURL"/>
							<a class="btn btn-dark" href="${detailURL}">See Details</a>
						</c:if>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
	<div id="standings">
		<div id="contestStandings">
			<h5>Contest Standings</h5>
			<ol>
			<c:if test="${not empty user}">
				<c:forEach items="${userContest.getStandings()}" var="team">
					<li>${team.name}: ${team.getTeamPoints()} pts</li>
				</c:forEach>
			</c:if>
			</ol>
		</div>
		<div id="teamStandings">
			<h5>Team Standings</h5>
			<ol>
			<c:if test="${not empty user}">
				<c:forEach items="${userTeam.getStandings()}" var="player">
					<li>${player.name}: ${player.points} pts</li>
				</c:forEach>
			</c:if>
			</ol>
		</div>
	</div>
</div>

<c:import url="/WEB-INF/jsp/common/footer.jsp"/>