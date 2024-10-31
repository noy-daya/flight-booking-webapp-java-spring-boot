<!--
	The page allows user to fill a feedback form for its previous routes.
	In case the form is available, button will be enabled.
-->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<!-- scripts -->
	<script>
	    $(document).ready(function() {
	    	/*
	    	------------------------------------
	    	event: modal was closed
	    	------------------------------------
	    	*/
	        $('#modal').on('hidden.bs.modal', function (e) {
	        	window.location.reload();
	        });
	    
	    	/*
	    	------------------------------------
	    	event: open modal button was clicked
	    	------------------------------------
	    	*/
		    $(document).on('click', '.openModalButton', function() {
		    	// load page into model content
		        var routeNumber = $(this).data('route-number');
		        $('#modal-body').load('userFeedbackAdd?routeNumber=' + routeNumber, function() {
		            $('#modal').modal('show');
		        });
		    });
	    });
	</script>
</head>
<body>
	<div layout:fragment="content">
		<div class="container">
			<h2 class="fw-bold mb-4 text-center">Feedback Forms</h2>
			<p class="text-muted">
				Click on the buttons in order to fill a feedback regarding to your previous routes.
			</p>
		</div>
		
		<section th:if="${!routeFeedbackMap.isEmpty()}">
			<div class="container" style="align-items: center;">
				<!-- button will be disabled/enabled based on the value of routeFeedbackMap
				(indication to whether there a feedback can be filled or not -->
				<button th:each="entry : ${routeFeedbackMap}"
					class="btn btn-light p-4 m-4 openModalButton"
					th:data-route-number="${entry.key.routeNumber}"
					th:disabled="${!entry.value}">
					<div th:if="${#lists.size(entry.key.flights) == 1}">
						<!-- The list has one flight -->
						<div th:each="flight : ${entry.key.flights}"
							class="d-flex justify-content-center">
							<div th:if="${flightStat.index == 0}">
								<i class="fa-solid fa-calendar me-2"></i><span class="me-4"
									th:text="${'Arrival On ' + flight.arrivalDate + ' ' + flight.arrivalTime}"></span>
								<span
									th:text="${flight.departureAirport.city.name + ', ' + flight.departureAirport.city.country.name}"></span>
								<i class="fa-solid fa-arrow-right-long me-2"></i> <span
									th:text="${flight.arrivalAirport.city.name + ', ' + flight.arrivalAirport.city.country.name}"></span>
							</div>
						</div>
					</div>
					<div th:if="${#lists.size(entry.key.flights) == 2}"
						class="d-flex justify-content-center">
						<!-- The list has two flights -->
						<div th:each="flight : ${entry.key.flights}">
							<div th:if="${flightStat.index == 0}">
								<i class="fa-solid fa-calendar me-2"></i><span class="me-4"
									th:text="${'Arrival On ' + flight.arrivalDate + ' ' + flight.arrivalTime}"></span>
								<span
									th:text="${flight.departureAirport.city.name + ', ' + flight.departureAirport.city.country.name}"></span>
								<i class="fa-solid fa-arrow-right-long me-2"></i>
							</div>
							<div th:if="${flightStat.index == 1}">
								<span
									th:text="${flight.arrivalAirport.city.name + ', ' + flight.arrivalAirport.city.country.name}"></span>
							</div>
						</div>
					</div>
				</button>
			</div>
		</section>
		
		<section th:if="${routeFeedbackMap.isEmpty()}">
			<br/>
			<div class="container text-center">
				<div class="alert alert-primary w-40" role="alert">
					No bookings exist, join us to explore a new adventure!
				</div>
			</div>
		</section>

		<!-- Modal -->
		<div class="modal fade" id="modal" aria-labelledby="exampleModalLabel"
			aria-hidden="true">
			<div class="modal-dialog modal-dialog-centered">
				<div class="modal-content">
					<div class="modal-header d-flex justify-content-center bg-info text-white">
						<h5 class="modal-title" id="exampleModalLabel">Feedback</h5>
					</div>
					<div class="modal-body" id="modal-body"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>