<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:import url="/WEB-INF/jsp/common/header.jsp"/>
	
<c:url value="/contest" var="contestURL"/>
<form action="${contestURL}" method="POST">
	<input type="text" name="orgName">
	<input type="date" name="startDate">
	<input type="date" name="endDate">
	<button type="submit">Submit</button>
</form>
<c:import url="/WEB-INF/jsp/common/footer.jsp"/>