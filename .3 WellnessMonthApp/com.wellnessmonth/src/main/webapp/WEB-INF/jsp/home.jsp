<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:import url="/WEB-INF/jsp/common/header.jsp"/>
	
<ul id="challenges">
	<c:forEach items="${challenges}" var="challenge">
		<li>${challenge.name}</li>
	</c:forEach>
</ul>
<c:url value="/register" var="registerURL"/>
<a href="${registerURL}">Create Account</a>	

<c:url value="/contest" var="contestURL"/>
<a href="${contestURL}">Start New Contest</a>	

<c:import url="/WEB-INF/jsp/common/footer.jsp"/>