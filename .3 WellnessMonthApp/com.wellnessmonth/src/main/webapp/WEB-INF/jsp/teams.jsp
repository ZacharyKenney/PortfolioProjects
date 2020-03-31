<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:import url="/WEB-INF/jsp/common/header.jsp"/>
<div id="teamsForm">
	<form action="#" method="POST">
		<input type="hidden" value="${newTeam}">
		<c:if test="${newTeam}">
			<div class="form-group">
				<label for="newTeamName">New Team Name</label>
				<input type="text" id="newTeamName" class="form-control" name="newTeamName" placeholder="Enter team name here"/>
			</div>
			<button type="submit">Start New Team</button>
			
		</c:if>
		<c:if test="${!newTeam}">
			<div class="form-group">
				<label for="teamIdToJoin">Select A Team To Join</label>
				<select id="teamIdToJoin" class="form-control" name="teamIdToJoin">
					<c:forEach items="${userContest.getStandings()}" var="team">
						<option value="${team.id}">${team.name}</option>
					</c:forEach>
				</select>
			</div>
			<button class="btn btn-outline-secondary" type="submit">Join Team</button>
		</c:if>
	</form>
</div>

<c:import url="/WEB-INF/jsp/common/footer.jsp"/>