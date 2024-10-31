<!--
	* The page displays the routes, allows admin search by using filters.
		- refreshes each time the user presses "Search".
		- getting values of last searching from url (using "param.{nameOfURLParam})"
	* Admin can add existing flights to the route, modify and cancel a flight.
-->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<!-- css files -->
	<link th:href="@{/css/timeline.css}" rel="stylesheet" />

	<script>
	    $(document).ready(function() {
	    	
	    	/*
	    	------------------------------------
	    	event: modal was closed
	    	------------------------------------
	    	*/
	    	// Store scroll position before reloading
	        $('#modal').on('hidden.bs.modal', function (e) { 
	            window.location.reload();
	        });
	        
	    	/*
	    	------------------------------------
	    	event: add button was clicked
	    	------------------------------------
	    	*/
		    $(document).on('click', '.openAddModalButton', function() {
		        var routeNumber = $(this).data('route-number');
		    	 // load page into modal content
		        $('#modal-body').load('/adminRouteAddFlight?routeNumber=' + routeNumber, function() {
		            $('#modal').modal('show');
		        });
		    });
	        
	    	/*
	    	------------------------------------
	    	event: modify button was clicked
	    	------------------------------------
	    	*/
		    $(document).on('click', '.openModifyModalButton', function() {
		        var routeNumber = $(this).data('route-number');
		     	// load page into modal content
		        $('#modal-body').load('/adminRouteModify?routeNumber=' + routeNumber, function() {
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
	    
			/*
			------------------------------------
			event: cancel flight button was clicked
			------------------------------------
			*/
			$(".cancel-btn").click(function() {
		        $('#confirmationModal').modal('show');
		        // store the form associated with the clicked button
		        var form = $(this).closest('.cancel-form');
		        // event: confirmation button was clicked
		        // ------------------------------------
		        $('#confirmCancel').off('click').on('click', function() {
		            form.submit(); // submit the form when confirmed
		        });
		    });
	    });
	</script>

</head>
<body>
	<div layout:fragment="content">
		<div class="container">
		
			<!-- display success message from adminAddFlight.jsp page,
				after a successful flight insertion
			-->
			<span th:if="${add_flight_success_message != null}" class="text-success"
				th:text="${add_flight_success_message}"> </span>
				
			<h2 class="fw-bold mb-4 text-center">Routes and Flights</h2>
			<p class="text-muted">
				The page displays all the routes in the system (which are not canceled). <br /> In case flight has not landed yet, you can click
				on the buttons to add an existing flight or modify flights in route.
				<br /> Additionally, you have the option to search for specific
				routes to streamline your navigation.
			</p>
		</div>

		<!-- filter form -->
		<section>
			<div class="container">
				<form th:action="@{/adminRoutes}" class="mb-4">
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
									<a class="btn btn-outline-secondary clear-button rounded-0"
										title="Clear" data-clear> <i class="fa fa-close"></i>
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
									<a class="btn btn-outline-secondary clear-button rounded-0"
										title="Clear" data-clear> <i class="fa fa-close"></i>
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
			<div th:each="route : ${routesList}">
				<div class="container py-4">
					<div class="card mb-3">
						<span class="card-header fw-bold text-white bg-info mb-2" th:text="${'Route #' + route.routeNumber}"></span>
						<div class="d-flex justify-content-end">
							<!-- Allow booking Form -->
							<form th:action="@{/adminRoutes/ProcessRouteAllowBooking(routeNumber=${route.routeNumber})}"
							      method="post">
							    <input type="hidden" name="routeNumber" th:value="${route.routeNumber}" />
							    <!-- allow booking only if:
							        1. 1st flight in route is not canceled (-> so it the 2nd)
							        2. current date and time is before departure
							    -->
							    <button type="submit"
							            th:if="${route.flights != null
							                and route.isBookingAllowed == false
							                and not route.flights.isEmpty()
							                and !#strings.equals(route.flights[0].statusID, T(com.example.SwipeFlight.server.utils.Constants).FLIGHT_STATUS_CANCELED)
							                and T(com.example.SwipeFlight.server.utils.DateTimeUtils).isNowBeforeGivenDateTimeWithOffset(route.flights[0].departureDate, route.flights[0].departureTime, 0)}"
							            class="btn btn-outline-primary rounded-0 me-2"> Allow Booking
							    </button>
							    <button disabled type="button"
							            th:if="${route.flights != null
							                and route.isBookingAllowed == true
							                and not route.flights.isEmpty()
							                and !#strings.equals(route.flights[0].statusID, T(com.example.SwipeFlight.server.utils.Constants).FLIGHT_STATUS_CANCELED)
							                and T(com.example.SwipeFlight.server.utils.DateTimeUtils).isNowBeforeGivenDateTimeWithOffset(route.flights[0].departureDate, route.flights[0].departureTime, 0)}"
							            class="btn btn-outline-success rounded-0 me-2">
							        <i class="fa-solid fa-circle-check me-2"></i>Booking is Allowed
							    </button>
							</form>
							<!-- Route Cancellation Form -->
							<form class="cancel-form"
								th:action="@{/adminRoutes/ProcessRouteCancellation}"
								method="post">
								<input type="hidden" name="routeNumber" th:value="${route.routeNumber}" />
								<!-- allow cancellation only if:
									1. 1st flight in route is not canceled (-> so it the 2nd)
									2. current date and time is before departure
								-->
								<button type="button"
								th:if="${route.flights != null
											and not route.flights.isEmpty()
											and !#strings.equals(route.flights[0].statusID, T(com.example.SwipeFlight.server.utils.Constants).FLIGHT_STATUS_CANCELED)
											and T(com.example.SwipeFlight.server.utils.DateTimeUtils).isNowBeforeGivenDateTimeWithOffset(route.flights[0].departureDate, route.flights[0].departureTime, 0)}"
									class="btn btn-outline-danger rounded-0 cancel-btn">
									<i class="fa-solid fa-circle-xmark" title="Cancel"></i>
								</button>
							</form>
						</div>
						<br /> <br />
						<div class="card-body">
							<div class="row">
								<div class="col-lg-11">
									<div class="horizontal-timeline">
										<ul class="list-inline items">
											<li th:each="flight : ${route.flights}"
												th:if="${flightStat.index == 0}"
												class="list-inline-item items-list">
												<div class="px-3">
													<div class="event-date badge bg-info">
														<i class="fa-regular fa-calendar me-2"></i> <span
															th:text="${flight.departureDate + ' ' + flight.departureTime}"></span>
													</div>
													<i class="fa-solid fa-plane-departure text-info me-2"></i>
													<h5 class="pt-2"
														th:text="${flight.departureAirport.city.name + ', ' + flight.departureAirport.city.country.name}"></h5>
													<p class="text-muted"
														th:text="${flight.departureAirport.code}"></p>
												</div>
											</li>
											<!-- flight duration 1 -->
											<li th:each="flight : ${route.flights}"
												th:if="${flightStat.index == 0}"
												class="list-inline-item items-list">
												<div class="px-3 text-muted">
													<small
														th:text="${T(com.example.SwipeFlight.server.utils.Formatter).formatDuration(flight.duration) + ' hours'}"></small>
													<br /> <small th:text="${'Flight ID: ' + flight.id}"></small>
													<div class="pt-2">
														<i class="fa-solid fa-circle-info"></i>
													</div>
												</div>
											</li>
											<li th:each="flight : ${route.flights}"
												th:if="${flightStat.index == 0}"
												class="list-inline-item items-list">
												<div class="px-3">
													<div class="event-date badge bg-info">
														<i class="fa-regular fa-calendar me-2"></i> <span
															th:text="${flight.arrivalDate + ' ' + flight.arrivalTime}"></span>
													</div>
													<i class="fa-solid fa-plane-arrival text-info me-2"></i>
													<h5 class="pt-2"
														th:text="${flight.arrivalAirport.city.name + ', ' + flight.arrivalAirport.city.country.name}"></h5>
													<p class="text-muted"
														th:text="${flight.arrivalAirport.code}"></p>
												</div>
											</li>
											<li th:each="flight : ${route.flights}"
												th:if="${flightStat.index == 1}"
												class="list-inline-item items-list">
												<div class="px-3">
													<div class="event-date badge bg-info">
														<i class="fa-regular fa-calendar me-2"></i> <span
															th:text="${flight.departureDate + ' ' + flight.departureTime}"></span>
													</div>
													<i class="fa-solid fa-plane-departure text-info me-2"></i>
													<h5 class="pt-2"
														th:text="${flight.departureAirport.city.name + ', ' + flight.departureAirport.city.country.name}"></h5>
													<p class="text-muted"
														th:text="${flight.departureAirport.code}"></p>
												</div>
											</li>
											<!-- flight duration 2 -->
											<li th:each="flight : ${route.flights}"
												th:if="${flightStat.index == 1}"
												class="list-inline-item items-list">
												<div class="px-3 text-muted">
													<small
														th:text="${T(com.example.SwipeFlight.server.utils.Formatter).formatDuration(flight.duration) + ' hours'}"></small>
													<br /> <small th:text="${'Flight ID: ' + flight.id}"></small>
													<div class="pt-2">
														<i class="fa-solid fa-circle-info me-2"></i>
													</div>
												</div>
											</li>
											<li th:each="flight : ${route.flights}"
												th:if="${flightStat.index == 1}"
												class="list-inline-item items-list">
												<div class="px-3">
													<div class="event-date badge bg-info">
														<i class="fa-regular fa-calendar me-2"></i> <span
															th:text="${flight.arrivalDate + ' ' + flight.arrivalTime}"></span>
													</div>
													<i class="fa-solid fa-plane-arrival text-info me-2"></i>
													<h5 class="pt-2"
														th:text="${flight.arrivalAirport.city.name + ', ' + flight.arrivalAirport.city.country.name}"></h5>
													<p class="text-muted"
														th:text="${flight.arrivalAirport.code}"></p>
												</div>
											</li>
										</ul>

									</div>

								</div>
							</div>
						</div>
						<div class="card-footer">
							<div
								th:with="lastFlight=${route.flights[route.flights.size()-1]}"> <!-- ignore error -->
								<!-- allow adding flight only if:
									1. current time is before last flight's arrival date and time
									2. booking is not allowed yet
								-->
								<button type="button"
									class="btn btn-outline-primary rounded-0 openAddModalButton"
									th:data-route-number="${route.routeNumber}"
									th:disabled="${!T(com.example.SwipeFlight.server.utils.DateTimeUtils).isNowBeforeGivenDateTimeWithOffset(lastFlight.arrivalDate, lastFlight.arrivalTime, 0)
													or route.isBookingAllowed == true}">
									<i class="fa-solid fa-plus me-2"></i>Add to route
								</button>
								<!-- allow adding flight only if:
									current time is before last flight's arrival date and time
								-->
								<button type="button"
									class="btn btn-outline-primary rounded-0 openModifyModalButton"
									th:data-route-number="${route.routeNumber}"
									th:disabled="${!T(com.example.SwipeFlight.server.utils.DateTimeUtils).isNowBeforeGivenDateTimeWithOffset(lastFlight.arrivalDate, lastFlight.arrivalTime, 0)}">
									<i class="fa-regular fa-pen-to-square me-2"></i>Modify flights
								</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>

		<section th:if="${routesList.isEmpty()}">
			<br />
			<div class="container text-center">
				<div class="alert alert-primary w-40" role="alert">No routes
					exist for the the given filters.</div>
			</div>
		</section>

		<!-- Modal -->
		<div class="modal fade" id="modal" aria-labelledby="exampleModalLabel"
			aria-hidden="true">
			<div class="modal-dialog modal-dialog-centered"
				style="width: fit-content; max-width: none;">
				<div class="modal-content">
					<div class="modal-body" id="modal-body"></div>
				</div>
			</div>
		</div>
		
		<!-- Confirmation Modal -->
		<div class="modal fade" id="confirmationModal" tabindex="-1"
			aria-labelledby="confirmationModalLabel" aria-hidden="true">
			<div class="modal-dialog modal-dialog-centered">
				<div class="modal-content">
					<div class="modal-body">Are you sure you want to cancel this
						Route?</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-dismiss="modal">No</button>
						<button type="button" class="btn btn-danger" id="confirmCancel">Yes</button>
					</div>
				</div>
			</div>
		</div>

	</div>
</body>
</html>