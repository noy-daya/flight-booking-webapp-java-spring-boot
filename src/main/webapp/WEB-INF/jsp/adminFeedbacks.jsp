<!--
	* The page displays the routes, allows user search by using filters.
		- refreshes each time the user presses "Search".
		- getting values of last searching from url (using "param.{nameOfURLParam})"
	* The user can display the average rating of each route.
-->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<!-- javascript file -->
	<script type="text/javascript" th:src="@{/js/airports.js}"></script>

    <!-- chart, for modal content -->
    <script src="https://cdn.plot.ly/plotly-2.32.0.min.js" charset="utf-8"></script>

	<script>
	    $(document).ready(function() {
	    	
	    	$('.selectpicker').selectpicker();
	    	
			/*
			------------------------------------
			event: view feedback button was clicked
			------------------------------------
			*/
		    $(document).on('click', '.openModalButton', function() {
		        var routeNumber = $(this).data('route-number');
		     	// load page into model content
		        $('#modal-body').load('adminFeedbacks/adminFeedbackOfRoute?routeNumber=' + routeNumber, function() {
		            $('#modal').modal('show');
		        });
		    });
	    
			/*
			------------------------------------
			event: clear button (of filters) was clicked
			------------------------------------
			*/
		    $(".clear-button").click(function() {
		        $(this).closest('.input-group').find('input').val('');
		    });
	    });
	</script>
</head>
<body>
	<div layout:fragment="content">
		<div class="container">
			<h2 class="fw-bold mb-4 text-center">Feedbacks</h2>
			<p class="text-muted">
				Please click on the buttons to access average ratings,
				derived from user feedback submitted for each route.
				<br/>
				Additionally, you have the option to search for specific routes to streamline
				your navigation.
			</p>
		</div>

		<!-- filter form -->
		<section>
			<div class="container">
				<form th:action="@{/adminFeedbacks}" class="mb-4">
					<div class="row">
						<div class="col-md-2">
							<input type="number" class="form-control" name="routeNumber"
								placeholder="Route Number" th:value="${param.routeNumber}">
						</div>
						<div class="col-md-3">
							<select name="departureAirportID" id="departureAirportID"
								class="selectpicker form-control" data-size="5">
								<option value="0">Departure Airport</option>
								<option th:each="option : ${airportsList}"
									th:value="${option.id}" th:text="${option.code}"
									th:data-subtext="${option.city.name + ', ' + option.city.country.name}"
									th:selected="${#strings.equals(option.id, #strings.toString(param.departureAirportID))}">
								</option>
							</select>
						</div>
						<div class="col-md-3">
							<select name="arrivalAirportID" id="arrivalAirportID"
								class="selectpicker form-control" data-size="5">
								<option value="0">Arrival Airport</option>
								<option th:each="option : ${airportsList}"
									th:value="${option.id}" th:text="${option.code}"
									th:data-subtext="${option.city.name + ', ' + option.city.name + ', ' + option.city.country.name}"
									th:selected="${#strings.equals(option.id, #strings.toString(param.arrivalAirportID))}">
								</option>
							</select>
						</div>
						<div class="col-md-2">
							<div class="input-group">
								<input type="text" name="departureDate" id="departureDate"
									th:value="${param.departureDate}" class="form-control"
									placeholder="Departure Date">
								<div class="input-group-append">
									<a class="btn btn-outline-secondary clear-button rounded-0" title="Clear"
										data-clear> <i class="fa fa-close"></i>
									</a>
								</div>
							</div>
							<script>
								flatpickr("#departureDate", {
									dateFormat : "Y-m-d",
									altInput : true,
									altFormat : "d.m.Y",
								});
							</script>
						</div>
						<div class="col-md-2">
							<div class="input-group">
								<input type="text" name="arrivalDate" id="arrivalDate"
									th:value="${param.arrivalDate}" class="form-control"
									placeholder="Arrival Date">
								<div class="input-group-append">
									<a class="btn btn-outline-secondary clear-button rounded-0" title="Clear"
										data-clear> <i class="fa fa-close"></i>
									</a>
								</div>
							</div>
							<script>
								flatpickr("#arrivalDate", {
									dateFormat : "Y-m-d",
									altInput : true,
									altFormat : "d.m.Y",
								});
							</script>
						</div>
					</div>
					<div class="row mt-3 d-flex justify-content-center text-center">
						<div class="col-md-3 mb-5">
							<button type="submit" class="btn btn-secondary rounded-0">Search</button>
						</div>
					</div>
				</form>
			</div>
		</section>
		
		<section th:if="${!routesList.isEmpty()}">
			<div class="container" style="align-items: center;">
				<table id="table" class="table table-hover">
					<thead>
						<tr>
							<th>Route</th>
							<th>Flights</th>
							<th>Departure From</th>
							<th>Final Destination</th>
							<th>Departure</th>
							<th>Arrival</th>
						</tr>
					</thead>
					<tbody>
						<tr th:each="route : ${routesList}"
							th:if="${#lists.size(route.flights) == 1}">
							<td><span th:text="${route.routeNumber}"></span></td>
							<td><span th:text="${route.flights[0].id}"></span></td>
							<td><span
								th:text="${route.flights[0].departureAirport.code + ', ' + route.flights[0].departureAirport.city.name + ', ' + route.flights[0].departureAirport.city.country.name}"></span>
							</td>
							<td><span
								th:text="${route.flights[0].arrivalAirport.code + ', ' + route.flights[0].arrivalAirport.city.name + ', ' + route.flights[0].arrivalAirport.city.country.name}"></span>
							</td>
							<td><span
								th:text="${route.flights[0].departureDate + ' ' + route.flights[0].departureTime}"></span>
							</td>
							<td><span
								th:text="${route.flights[0].arrivalDate + ' ' + route.flights[0].arrivalTime}"></span>
							</td>
							<td>
								<button type="button"
									class="btn btn-outline-primary openModalButton rounded-0"
									th:data-route-number="${route.routeNumber}">
									<i class="fa-solid fa-chart-simple"></i>
								</button>
							</td>
						</tr>
						<tr th:each="route : ${routesList}"
							th:if="${#lists.size(route.flights) == 2}">
							<td><span th:text="${route.routeNumber}"></span></td>
							<td><span
								th:text="${route.flights[0].id + ',' + route.flights[1].id}"></span>
							</td>
							<td><span
								th:text="${route.flights[0].departureAirport.code + ', ' + route.flights[0].departureAirport.city.name + ', ' + route.flights[0].departureAirport.city.country.name}"></span>
							</td>
							<td><span
								th:text="${route.flights[1].arrivalAirport.code + ', ' + route.flights[1].arrivalAirport.city.name + ', ' + route.flights[1].arrivalAirport.city.country.name}"></span>
							</td>
							<td><span
								th:text="${route.flights[0].departureDate + ' ' + route.flights[0].departureTime}"></span>
							</td>
							<td><span
								th:text="${route.flights[1].arrivalDate + ' ' + route.flights[1].arrivalTime}"></span>
							</td>
							<td>
								<button type="button" title="Ratings"
									class="btn btn-outline-primary openModalButton rounded-0"
									th:data-route-number="${route.routeNumber}">
									<i class="fa-solid fa-chart-simple"></i>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</section>

		<section th:if="${routesList.isEmpty()}">
			<br />
			<div class="container text-center">
				<div class="alert alert-primary w-40" role="alert">
				No routes exist for the the given filters. </div>
			</div>
		</section>

		<!-- Modal -->
		<div class="modal fade" id="modal" aria-labelledby="exampleModalLabel"
			aria-hidden="true">
			<div class="modal-dialog modal-dialog-centered modal-lg">
				<div class="modal-content">
					<div class="modal-body" id="modal-body"></div>
				</div>
			</div>
		</div>

	</div>
</body>
</html>