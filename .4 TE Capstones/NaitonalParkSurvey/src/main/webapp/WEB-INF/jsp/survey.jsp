<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:import url="/WEB-INF/jsp/common/header.jsp" />

<c:url value="/survey" var="submitSurveyUrl"/>
<form:form action="${submitSurveyUrl}" method="POST" modelAttribute="surveyData">
	<div class="form-group">
		<label for="favPark">Favorite National Park</label>
		<form:select class="form-control form-control-lg" id="favPark" name="favPark" required="required" path="favPark">
			<form:option value=""></form:option>
			<c:forEach items="${parks}" var="park">
				<form:option value="${park.parkCode}">${park.parkName}</form:option>
			</c:forEach>
		</form:select>
		<form:errors path="favPark" class="errors"/>
	</div>
	<div class="form-group">
		<label for="email">Your Email</label>
		<form:input class="form-control form-control-lg" type="email" id="email" name="email" required="required" placeholder="email address" path="email"/>
		<form:errors path="email" class="errors"/>
	</div>
	<div class="form-group">	
		<label for="stateRes">State of Residence</label>
		<form:select class="form-control form-control-lg" id="stateRes" name="stateRes" required="required" path="stateRes">
			<form:option value=""></form:option>
		<c:forEach items="${states}" var="state">
				<form:option value="${state}">${state}</form:option>
			</c:forEach>
		</form:select>
		<form:errors path="validState" class="errors"/>
		
	</div>
	<c:forEach items="${levels}" var="level">
	<div class="form-check">
		<form:radiobutton class="form-check-input" id="${level}" name="activityLvl" value="${level}" path="activityLvl"/>
		<label class="form-check-label" for="${level}">${level}</label>
	</div>
	</c:forEach>
	<br>
	<button class="btn btn-outline-primary" type="submit">Submit</button>
</form:form>
<c:import url="/WEB-INF/jsp/common/footer.jsp" />