<!-- 
	* The page displays full details of a given booking.
	* The page is displayed in a modal in page userBookings.jsp
-->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<!-- css file -->
	<link th:href="@{/css/timeline.css}" rel="stylesheet" />
</head>
<body>
	<div class="d-flex justify-content-center text-white"
		style="background-color: #ced4da">
		<h5 th:text="${'Booking #' + bookingID}"></h5>
	</div>
	<br />
	<br />
	
	<!-- Route -->
	<section th:object="${route}">
		<div class="horizontal-timeline">
			<ul class="list-inline items">
				<li th:each="flight : ${route.flights}"
					th:if="${flightStat.index == 0}"
					class="list-inline-item items-list">
					<div class="px-3">
						<div class="event-date badge bg-info">
							<i class="fa-regular fa-calendar"></i> <span
								th:text="${flight.departureDate + ' ' + flight.departureTime}"></span>
						</div>
						<i class="fa-solid fa-plane-departure text-info"></i>
						<h5 class="pt-2"
							th:text="${flight.departureAirport.city.name + ', ' + flight.departureAirport.city.country.name}"></h5>
						<p class="text-muted" th:text="${flight.departureAirport.code}"></p>
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
						<!-- if flight is canceled in the route -->
						<small class="text-danger fw-bold"
							th:if="${#strings.equals(#strings.toString(flight.statusID),
												 	#strings.toString(T(com.example.SwipeFlight.server.utils.Constants).FLIGHT_STATUS_CANCELED))}">
							CANCELED</small>
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
							<i class="fa-regular fa-calendar"></i> <span
								th:text="${flight.arrivalDate + ' ' + flight.arrivalTime}"></span>
						</div>
						<i class="fa-solid fa-plane-arrival text-info"></i>
						<h5 class="pt-2"
							th:text="${flight.arrivalAirport.city.name + ', ' + flight.arrivalAirport.city.country.name}"></h5>
						<p class="text-muted" th:text="${flight.arrivalAirport.code}"></p>
					</div>
				</li>
				<li th:each="flight : ${route.flights}"
					th:if="${flightStat.index == 1}"
					class="list-inline-item items-list">
					<div class="px-3">
						<div class="event-date badge bg-info">
							<i class="fa-regular fa-calendar"></i> <span
								th:text="${flight.departureDate + ' ' + flight.departureTime}"></span>
						</div>
						<i class="fa-solid fa-plane-departure text-info"></i>
						<h5 class="pt-2"
							th:text="${flight.departureAirport.city.name + ', ' + flight.departureAirport.city.country.name}"></h5>
						<p class="text-muted" th:text="${flight.departureAirport.code}"></p>
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
						<!-- if flight is canceled in the route -->
						<small class="text-danger fw-bold"
							th:if="${#strings.equals(#strings.toString(flight.statusID),
												 	#strings.toString(T(com.example.SwipeFlight.server.utils.Constants).FLIGHT_STATUS_CANCELED))}">
							CANCELED</small>
						<div class="pt-2">
							<i class="fa-solid fa-circle-info"></i>
						</div>
					</div>
				</li>
				<li th:each="flight : ${route.flights}"
					th:if="${flightStat.index == 1}"
					class="list-inline-item items-list">
					<div class="px-3">
						<div class="event-date badge bg-info">
							<i class="fa-regular fa-calendar"></i> <span
								th:text="${flight.arrivalDate + ' ' + flight.arrivalTime}"></span>
						</div>
						<i class="fa-solid fa-plane-arrival text-info"></i>
						<h5 class="pt-2"
							th:text="${flight.arrivalAirport.city.name + ', ' + flight.arrivalAirport.city.country.name}"></h5>
						<p class="text-muted" th:text="${flight.arrivalAirport.code}"></p>
					</div>
				</li>
			</ul>
		</div>
	</section>

	<!-- Passengers -->
	<section th:object="${passengerListDTO}">
		<div class="container-fluid">
			<div class="row">
				<div th:each="passenger, passengerIndex : *{passengerList}"
					class="col-lg-4 col-md-4 mb-3">
					<div class="card card-pass" style="max-width: 450px;">
						<div class="card-body">
							<div class="row">
								<div class="col">

									<div class="form-floating mb-3">
										<!-- Display passenger name -->
										<input disabled type="text" class="form-control"
											th:value="${passenger.firstName + ' ' + passenger.lastName}">
										<label>Passenger Name</label>
									</div>
								</div>
								<div class="col">
									<div class="form-floating mb-3">
										<!-- Display passport ID -->
										<input disabled type="text" class="form-control"
											th:value="${passenger.passportID}"> <label>Passport
											ID</label>
									</div>
								</div>
							</div>
							<div th:each="flightId : ${passenger.flightAndSeats.keySet()}">
								<!-- Group by Flight ID -->
								<div class="mb-3">
									<h5 class="text-primary strong">
										Flight ID: <span th:text="${flightId}"></span>
									</h5>
									<p>
										<strong>Seat:</strong> <span
											th:text="${passenger.flightAndSeats.get(flightId)}"></span>
									</p>
									<p>
										<strong>Luggage:</strong>
									</p>
									<table class="table">
										<tbody class="text-muted">
											<tr
												th:each="luggageId : ${passenger.flightAndLuggage.get(flightId)}">
												<td th:each="luggage : ${LuggageList}"
													th:if="${#strings.equals(#strings.toString(luggage.id), #strings.toString(luggageId))}">
													<span th:text="${luggage.description}"></span>
												</td>
												<td th:each="luggage : ${LuggageList}"
													th:if="${#strings.equals(#strings.toString(luggage.id), #strings.toString(luggageId))}">
													<span th:text="${'$' + #numbers.formatDecimal(luggage.price, 0, 'COMMA', 0, 'POINT')}"></span>
												</td>
											</tr>
										</tbody>
									</table>

								</div>
							</div>
							<br />
							<div class="row">
								<div class="col">
									<strong>Total price of luggage:</strong> <span
										class="text-info float-end"
										th:text="${'$' + __${'totalLuggagePriceForPassenger_' + passenger.passportID}__}"></span>

								</div>
							</div>
							<div class="row">
								<div class="col">
									<!-- Calculate and display total ticket price for this passenger -->
									<strong>Ticket price:</strong> <span class="text-info float-end"
										th:if="${totalTicketPriceForPassenger != null}"
										th:text="${'$' + #numbers.formatDecimal(totalTicketPriceForPassenger, 0, 'COMMA', 0, 'POINT')}"> </span>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>

	<section>
		<strong>Total Booking Price:</strong> <span class="text-info"
			th:if="${totalPrice != null}" th:text="${'$' + #numbers.formatDecimal(totalPrice, 0, 'COMMA', 0, 'POINT')}"> </span>
	</section>

</body>
</html>