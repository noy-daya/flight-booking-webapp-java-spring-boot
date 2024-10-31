<!-- This page displays Seats Step of booking process -->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<!-- css files -->
	<link th:href="@{/css/stepper.css}" rel="stylesheet" />
	<link th:href="@{/css/seats.css}" rel="stylesheet" />
	
	<!-- js file -->
	<script type="text/javascript" th:src="@{/js/seats.js}"></script>

	<script th:inline="javascript">
	    $(document).ready(function() {
	    	$('#seatsNowTaken').hide();
	        // update seat availability
	        updateSeatAvailability(); // file "/js/seats.js"
	        // Periodically update seat availability every 3 seconds
	        setInterval(updateSeatAvailability, 3000);
	        // handle matrix on selection
	        handleSeatSelection(); // file "/js/seats.js"
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

		<div class="container-fluid">
			<div class="row">
				<div class="col col-8">
					<div class="container">
						<!-- Iterate over each flight in flightList -->
						<div class="row">
							<div th:each="flight, flightIndex : ${flightList}"
								class="col-lg-5 col-md-4 mb-5">
								
								<!-- Display flight and passengers details -->
								<div class="col">
									<h4 class="text-primary">
										Flight ID: <span th:text="${flight.id}"></span>
									</h4>
									<p class="text-muted">Pick a seat for each passenger:</p>
									<!-- Iterate over passengerList in PassengerListDTO -->
									<div
										th:each="passenger, passIndex : ${passengerListDTO.passengerList}">
										<!-- Use data attributes to store passenger's first and last name -->
										<div class="row align-items-center">
											<div class="col">
												<span
													th:text="${passenger.firstName + ' ' + passenger.lastName}"></span>
											</div>
											<div class="col">
												<button type="button"
													class="btn btn-light mt-2 passenger-button"
													th:data-index="${passIndex.index + '-' + flightIndex.index}">
													<i class="fa-solid fa-user-large"></i> <span
														th:text="${#strings.toUpperCase(passenger.firstName.substring(0,1))} + ${#strings.toUpperCase(passenger.lastName.substring(0,1))}"></span>
												</button>
											</div>
										</div>
									</div>
								</div>
								<br/><br/>
								
								<!-- Display flight seat matrix -->
								<div class="col">
									<div th:each="flightDTO : ${flightDTOList}"
										th:if="${flightDTO.flight.id == flight.id}">
										<div class="plane" th:data-flight-id="${flight.id}">
											<br /> <br /> <br />
											<div th:each="row, rowIdx : ${flightDTO.seatMatrix}">
												<div th:each="seat, seatStat : ${row}" class="seat-container">
													<span th:if="${seatStat.index == 0}" class="row-counter" th:text="${rowIdx.index + 1}"></span>
													<button class="seat"
														th:attr="data-row=${seat.row}, data-seat=${seat.seat}, data-flight=${flightIndex.index}">
													</button>
													<div th:classappend="${seatStat.index == 0} ? 'seat-number-most-left' : 'seat-number'" th:text="${seat.row + '-' + seat.seat}"></div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col">
					<div id="seatsNowTaken" class="alert alert-warning alert-dismissible" role="alert">
					  <span class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></span>
					  Oops, some of the seats you selected are now taken... please select again.
					</div>
					
					<div class="d-flex align-items-center">
						<a class="btn btn-secondary rounded-0 me-4" href="javascript:history.back()"><i class="fa-solid fa-circle-arrow-left p-2"></i>Back</a>
						<form method="post" th:action="@{/bookingSeats/ProcessBookingSeats}">
							<!-- Hidden input field to store selected seats -->
							<input type="hidden" id="selectedSeats" name="selectedSeats" />
							<button id="nextBtn" type="submit" class="btn btn-primary rounded-0">Next
								<i class="fa-solid fa-circle-arrow-right p-2"></i></button>
						</form>
					</div>
			
					<span th:if="${error_message != null}" class="text-danger" th:text="${error_message}"> </span>
				</div>
			</div>
		</div>

	</div>
</body>
</html>