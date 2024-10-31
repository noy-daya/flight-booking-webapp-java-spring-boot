<!-- This page displays Flight Step of booking process -->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>

	<!-- css files -->
	<link th:href="@{/css/stepper.css}" rel="stylesheet" />
	<link th:href="@{/css/timeline.css}" rel="stylesheet" />
			
</head>
<body>
	<div layout:fragment="content">

		<!-- stepper -->
		<div class="container padding-bottom-3x mb-1">
			<div class="card mb-1 border-0">
				<h2 class="fw-bold mb-1 text-center">Booking</h2>
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
						<div class="step">
							<div class="step-icon-wrap">
								<div class="step-icon">
									<i class="fa-solid fa-plane"></i>
								</div>
							</div>
							<h4 class="step-title">Flight</h4>
						</div>
						<div class="step">
							<div class="step-icon-wrap">
								<div class="step-icon">
									<i class="fa-solid fa-users"></i>
								</div>
							</div>
							<h4 class="step-title">Passengers</h4>
						</div>
						<div class="step">
							<div class="step-icon-wrap">
								<div class="step-icon">
									<i class="fa-solid fa-chair"></i>
								</div>
							</div>
							<h4 class="step-title">Seats</h4>
						</div>
						<div class="step">
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

		<!-- Form -->
		<section th:if="${!#lists.isEmpty(routesList)}">
			<div th:each="route : ${routesList}">
				<div class="container">
					<div class="card mb-3">
						<div class="card-header">
							<div class="row">
								<div class="col">
									<p th:text="${'Route #' + route.routeNumber}"></p>
								</div>
								<div class="col">
									<p
										th:text="${'Ticket price for single passenger: ' + '$' + #numbers.formatDecimal(route.routePriceForSinglePassenger, 0, 'COMMA', 0, 'POINT')}"></p>
								</div>
								<div class="col d-flex justify-content-end">
									<form th:action="@{/bookingFlight/ProcessBookingFlight}"
										method="post">
										<input type="hidden" name="routeNumber"
											th:value="${route.routeNumber}" />
										<button type="submit" class="btn btn-primary rounded-0">
											<i class="fa-regular fa-circle-check me-2 p-2"></i>Select
										</button>
									</form>
								</div>
							</div>
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
														<i class="fa-regular fa-calendar"></i> <span
															th:text="${flight.departureDate + ' ' + flight.departureTime}"></span>
													</div>
													<i class="fa-solid fa-plane-departure text-info"></i>
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
														<i class="fa-regular fa-calendar"></i> <span
															th:text="${flight.arrivalDate + ' ' + flight.arrivalTime}"></span>
													</div>
													<i class="fa-solid fa-plane-arrival text-info"></i>
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
														<i class="fa-regular fa-calendar"></i> <span
															th:text="${flight.departureDate + ' ' + flight.departureTime}"></span>
													</div>
													<i class="fa-solid fa-plane-departure text-info"></i>
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
													<p class="text-muted"
														th:text="${flight.arrivalAirport.code}"></p>
												</div>
											</li>
										</ul>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>
		
		<section th:if="${#lists.isEmpty(routesList)}">
			<div class="container">
				<p class="text-danger">No flights exist, please select different dates and/or locations.</p>
			</div>
		</section>
		
		<div class="d-flex justify-items-center mt-4 mb-2">
			<div class="container">
				<a class="btn btn-secondary rounded-0 me-4" href="javascript:history.back()"><i class="fa-solid fa-circle-arrow-left p-2"></i>Back</a>
			</div>
		</div>

	</div>
</body>
</html>