<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:import url="/WEB-INF/jsp/common/header.jsp"/>
<div id="challengeForm">
	<c:url value="/challenge" var="challengeURL"/>
	<form action="${challengeURL}" method="POST">
		<div class="form-group">
			<label for="challengeName">Challenge Name</label>
			<input class="form-control" type="text" name="challengeName" id="challengeName">
		</div>
		<div class="form-group">
			<label for="challengeCategory">Points</label>
			<select class="form-control" name="challengeCategory" id="challengeCategory">
				<option value="heart">Lifestyle</option>
				<option value="food">Diet</option>
				<option value="workout">Cardio Training</option>
				<option value="muscle">Muscle Training</option>
				<option value="smart-cart">Grocery Store</option>
			</select>
		</div>
		<div class="form-group">
			<label for="challengePoints">Points</label>
			<input class="form-control" type="text" name="challengePoints" id="challengePoints">
		</div>
		<div class="form-group">
			<label for="challengeTimeFrame">Time Frame</label>
			<select class="form-control" name="challengeTimeFrame" id="challengeTimeFrame">
				<option>1 Day</option>
				<option>2 Days</option>
				<option>3 Days</option>
				<option>1 Week</option>
				<option>2 Weeks</option>
				<option>1 Month</option>
			</select>
		</div>
		<div class="form-group">
			<label for="challengeDesc">Description/Instructions</label>
			<input class="form-control" type="text" name="challengeDesc" id="challengeDesc">
		</div>
		<button class="btn btn-outline-secondary" type="submit">Submit</button>
	</form>
</div>
<c:import url="/WEB-INF/jsp/common/footer.jsp"/>