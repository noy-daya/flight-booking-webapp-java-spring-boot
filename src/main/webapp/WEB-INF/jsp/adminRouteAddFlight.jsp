<!-- 
	* The page displays route modification form, for a given route.
	* allows admin to add existing flights to route.
	* The page is displayed in a modal in page adminRoutes.jsp
-->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<!-- bootstrap-select -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>
	
	<!-- js files -->
	<script type="text/javascript" th:src="@{/js/add.js}"></script>
	<script type="text/javascript" th:src="@{/js/routeAddFlight.js}"></script>
	
	<script type="text/javascript">
		
		$('.selectpicker').selectpicker(); // Initialize selectpicker
	
	    $(document).ready(function() {
	        // Call the function to fetch flights in route
	        fetchFlightsInRouteNum('adminRouteAddFlight/FetchFlightsInRouteNum'); // file: "/js/routeAddFlight.js"
	    });
	</script>
	
</head>
<body>

	<form id="addForm"
		th:action="@{/adminRouteAddFlight/ProcessAddFlightToRoute}"
		th:object="${route}" method="post">
		<p th:text="${'Route #' + route.routeNumber}">
		
		<!-- display flights in route -->
		<div class="row">
			<div class="col">
				<input type="hidden" name="routeNumber" id="routeNumber"
					th:field="*{routeNumber}" th:value="*{routeNumber}"
					class="form-control">
			</div>
			<div>
				<table class="table table-hover table-responsive">
					<thead class="table-light">
						<tr>
							<th>#</th>
							<th>From</th>
							<th>To</th>
							<th>Departure Date</th>
							<th>Departure Time</th>
							<th>Arrival Date</th>
							<th>Arrival Time</th>
							<th>Duration</th>
						</tr>
					</thead>
					<tbody id="flightsInRoute">
					</tbody>
				</table>
			</div>
		</div>
		
		<!-- display flight selection -->
		<div class="row">
			<div class="col">
				<select name="flights[0].id" id="flightIDToAdd"
					th:field="*{flights[0].id}" class="selectpicker form-select"
					data-width="75%" data-size="5">
					<option value="0">Select an additional flight</option>
					<option th:each="option : ${suggestedFlights}"
						th:value="${option.id}"
						th:text="${'#' + option.id + ':' +
	                    		    	'   ' + option.departureAirport.city.name + ', ' + option.departureAirport.city.country.name +
	                    		    	'  ->  ' + option.arrivalAirport.city.name + ', ' + option.arrivalAirport.city.country.name}"
						th:data-subtext="${'Departure: ' + option.departureDate + ' ' + option.departureTime +
	                    		    		'  ,  Arrival: ' + option.arrivalDate + ' ' + option.arrivalTime}"></option>
				</select>
				<div class="text-danger">
					<p th:if="${#fields.hasErrors('flights[0].id')}"
						th:errors="*{flights[0].id}" class="error-message"></p>
				</div>
			</div>
		</div>
		<button type="submit" id="submitBtn" class="btn btn-primary rounded-0"
			style="display: block; margin: auto;">
			<i class="fa-solid fa-plus me-2"></i>Add
		</button>
		<span th:if="${success_message != null}" class="text-success"
			th:text="${success_message}"> </span>
		<p id="limitErrorMessage" class="text-muted" style="display: none;"></p>
	</form>
</body>
</html>