<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:import url="/WEB-INF/jsp/common/header.jsp"/>
	
<c:url value="/login" var="loginURL"/>
<form action="${loginURL}" method="POST">
	<label for="playerName">Player Name</label>
	<input type="text" name="playerName" id="playerName">
	
	<label for="playerPW">Password</label>
	<input type="text" name="playerPW" id="playerPW">

	<button type="submit">Login</button>
</form>
<c:import url="/WEB-INF/jsp/common/footer.jsp"/>