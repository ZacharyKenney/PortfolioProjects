<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:import url="/WEB-INF/jsp/common/header.jsp"/>
	
<c:url value="/contest" var="contestURL"/>
<div id="contestForm">
	<form action="${contestURL}" method="POST">
		<div class="form-group">
			<label for="orgName">Organization Name</label>
			<input class="form-control" type="text" name="orgName">
		</div>
		<div class="form-group">
			<label for="orgName">Start Date</label>
			<input class="form-control" type="date" name="startDate">
		</div>
		<div class="form-group">
			<label for="orgName">End Date</label>
			<input class="form-control" type="date" name="endDate">
		</div>
		<button class="btn btn-outline-secondary" type="submit">Submit</button>
	</form>
</div>
<c:import url="/WEB-INF/jsp/common/footer.jsp"/>