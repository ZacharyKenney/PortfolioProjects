<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:import url="/WEB-INF/jsp/common/header.jsp"/>
	
<c:url value="/register" var="registerURL"/>
<form action="${registerURL}" method="POST">
	<label for="playerName">Player Name</label>
	<input type="text" name="playerName" id="playerName">
	<label for="contestId">Contest ID</label>
	<input type="text" name="contestId" id="contestId">
	<button type="submit">Submit</button>
</form>
<c:import url="/WEB-INF/jsp/common/footer.jsp"/>