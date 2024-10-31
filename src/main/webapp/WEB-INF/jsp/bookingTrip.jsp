<!-- This page displays Trip Step of booking process -->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<!-- css files -->
	<link th:href="@{/css/stepper.css}" rel="stylesheet" />

	<!-- js files -->
	<script type="text/javascript" th:src="@{/js/airports.js}"></script>

	<script type="text/javascript">
		$('.selectpicker').selectpicker(); // bootstrap-select fields
	
		$(document).ready(function() {
			fetchArrivalAirports(); // file: "/js/airports.js"
		});
	</script>
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
						<div class="step">
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
		<section>
			<div class="card mx-auto col-md-8 shadow-5-strong"
				style="background: hsla(0, 0%, 100%, 0.8); backdrop-filter: blur(30px);">
				<div class="card-body">
					<p class="text-muted">Fill the form below:</p>
					<div class="row d-flex justify-content-center">
						<div>
							<form th:action="@{/bookingTrip/ProcessBookingTrip}"
								th:object="${tripForm}" method="post">
								<div class="row">
									<div class="col-3">
										<div class="form-floating mb-3">
											<select name="departureAirportID" id="departureAirportID"
												class="selectpicker form-control" data-size="4"
												th:field="*{departureAirportID}">
												<option value="0">Select</option>
												<option th:each="option : ${departureAirportsList}"
													th:value="${option.id}" th:text="${option.code}"
													th:data-subtext="${option.city.name + ', ' + option.city.country.name}"></option>
											</select> <label for="departureAirportID">Departure
												Airport</label>
										</div>
										<div class="text-danger">
											<p th:if="${#fields.hasErrors('departureAirportID')}"
												th:errors="*{departureAirportID}" class="error-message"></p>
										</div>
									</div>
									<div class="col-3">
										<div class="form-floating mb-3">
											<select name="arrivalAirportID" id="arrivalAirportID"
												class="selectpicker form-control" data-size="4"
												th:field="*{arrivalAirportID}">
												<option value="0">Select</option>
											</select><label for="arrivalAirportID">Arrival Airport</label>
										</div>
										<div class="text-danger">
											<p th:if="${#fields.hasErrors('arrivalAirportID')}"
												th:errors="*{arrivalAirportID}" class="error-message"></p>
										</div>
									</div>
									<div class="col-3">
										<div class="form-floating mb-3">
											<input type="text" name="departureDate" id="departureDate"
												class="form-control" placeholder="Departure Date"
												th:field="*{departureDate}"> <label
												for="departureDate">Departure Date</label>
											<script>
												flatpickr("#departureDate", {
													dateFormat : "Y-m-d",
													minDate : "today",
													altInput : true,
													altFormat : "d.m.Y",
												});
											</script>
										</div>
										<div class="text-danger">
											<p th:if="${#fields.hasErrors('departureDate')}"
												th:errors="*{departureDate}" class="error-message"></p>
										</div>
									</div>
									<div class="col-2">
										<div class="form-floating mb-3">
											<select name="numOfPassengers" id="numOfPassengers"
												class="selectpicker form-control" data-size="4"
												th:field="*{numOfPassengers}">
												<option value="0">Select</option>
												<option value="1">1</option>
												<option value="2">2</option>
												<option value="3">3</option>
												<option value="4">4</option>
												<option value="5">5</option>
												<option value="6">6</option>
												<option value="7">7</option>
												<option value="8">8</option>
												<option value="9">9</option>
												<option value="10">10</option>
											</select> <label for="numOfPassengers">Number of passengers</label>
										</div>
										<div class="text-danger">
											<p th:if="${#fields.hasErrors('numOfPassengers')}"
												th:errors="*{numOfPassengers}" class="error-message"></p>
										</div>
									</div>
								</div>
								<div class="text-center">
									<button id="nextBtn" type="submit" class="btn btn-primary rounded-0">Next
									<i class="fa-solid fa-circle-arrow-right p-2"></i></button>
								</div>

							</form>
							<span th:if="${error_message != null}" class="text-danger"
								th:text="${error_message}"> </span>
						</div>
					</div>
				</div>
			</div>
		</section>

	</div>
</body>
</html>