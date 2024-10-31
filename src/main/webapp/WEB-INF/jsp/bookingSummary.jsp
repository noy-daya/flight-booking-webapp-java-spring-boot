<!-- This page displays Summary Step of booking process -->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>

	<!-- css file -->
	<link th:href="@{/css/stepper.css}" rel="stylesheet" />
	<link th:href="@{/css/timeline.css}" rel="stylesheet" />

	<!-- js files -->
	<script type="text/javascript" th:src="@{/js/seatCountdown.js}"></script>

	<style>
	.alert
	{
		position: fixed;
		top: 35%;
		left: 0;
		z-index: 100000;
	}
	</style>
	
	<script>
	    $(document).ready(function() {
	        startCountdown(); // file "/js/seatCountdown.js"
	    });
    </script>
    
</head>
<body>
	<div layout:fragment="content">

		<!-- stepper -->
		<div class="container padding-bottom-3x mb-1">
			<div class="card mb-1 border-0">
				<h2 class="fw-bold mb-1 text-center">Booking Summary</h2>
				<div class="card-body">
					<div
						class="steps d-flex flex-wrap flex-sm-nowrap justify-content-between padding-top-2x padding-bottom-1x">
						<div class="step completed">
							<div class="step-icon-wrap">
								<div class="step-icon">
									<i class="fa-solid fa-earth-americas"></i>
								</div>
							</div>
							<h4 class="step-title">Trip</h4>
						</div>
						<div class="step completed">
							<div class="step-icon-wrap">
								<div class="step-icon">
									<i class="fa-solid fa-plane"></i>
								</div>
							</div>
							<h4 class="step-title">Flight</h4>
						</div>
						<div class="step completed">
							<div class="step-icon-wrap">
								<div class="step-icon">
									<i class="fa-solid fa-users"></i>
								</div>
							</div>
							<h4 class="step-title">Passengers</h4>
						</div>
						<div class="step completed">
							<div class="step-icon-wrap">
								<div class="step-icon">
									<i class="fa-solid fa-chair"></i>
								</div>
							</div>
							<h4 class="step-title">Seats</h4>
						</div>
						<div class="step completed">
							<div class="step-icon-wrap">
								<div class="step-icon">
									<i class="fa-solid fa-suitcase-rolling"></i>
								</div>
							</div>
							<h4 class="step-title">Luggage</h4>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div id="countdown-alert" class="alert alert-primary w-40"
			role="alert">
			<input type="hidden" id="seatsReserveTime"
				th:value="${SeatsReservationTime}" /> Your selected seats are saved
			for <span id="countdown"></span> minutes!
		</div>

		<!-- Route -->
		<section th:object="${route}">
			<br/><br/><br/>
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
												th:id="${'passengerName' + passenger.passportID}"
												th:value="${passenger.firstName + ' ' + passenger.lastName}">
											<label th:for="${'passengerName' + passenger.passportID}">Passenger
												Name</label>
										</div>
									</div>
									<div class="col">
										<div class="form-floating mb-3">
											<!-- Display passport ID -->
											<input disabled type="text" class="form-control"
												th:id="${'passenger' + passenger.passportID}"
												th:value="${passenger.passportID}"> <label
												th:for="${'passenger' + passenger.passportID}">Passport
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

		<!-- Total -->
		<div class="text-center">
			<section>
				<strong>Total Booking Price:</strong> <span class="text-info"
					th:if="${totalPrice != null}" th:text="${'$' + totalPrice}">
				</span>
			</section>


			<form th:action="@{/bookingSummary/ProcessBooking}" method="post">
				<div class="mb-2 mt-4">
					<a class="btn btn-secondary rounded-0 me-4" href="javascript:history.back()">
								<i class="fa-solid fa-circle-arrow-left p-2"></i>Back</a>
					<button id="submitBtn" th:if="${success_message != null}" disabled
						type="submit" onclick="load(event)" data-mdb-button-init
						data-mdb-ripple-init
						class="btn btn-primary rounded-0 p-2 col-2">
						<span id='spinner' style="display: none"
							class="spinner-border spinner-border-sm" role="status"
							aria-hidden="true"></span> Book the flight!
					</button>
					<button id="submitBtn" th:if="${success_message == null}"
						type="submit" onclick="load(event)" data-mdb-button-init
						data-mdb-ripple-init
						class="btn btn-primary rounded-0 p-2 col-2">
						<span id='spinner' style="display: none"
							class="spinner-border spinner-border-sm" role="status"
							aria-hidden="true"></span> Book the flight!
					</button>
				</div>
			</form>
		</div>

	</div>
</body>
</html>