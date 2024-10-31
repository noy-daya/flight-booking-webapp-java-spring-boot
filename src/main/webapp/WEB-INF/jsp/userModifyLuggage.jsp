<!-- The page allows user to modify luggage selections of his bookings -->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<!-- css files -->
	<link th:href="@{/css/adminMenu.css}" rel="stylesheet"/>
	
	<script>
	$(document).ready(function() {
		$('ul li').on('click', function() {
			$('li').removeClass('active');
			$(this).addClass('active');
		});
	 });
	</script>
</head>

<body>
	<div layout:fragment="content">
		<div class="container">
			<h2 class="fw-bold mb-4 text-center">Luggage Modification</h2>
		</div>
		
		<div class="container-fluid">
			<div class="row">
			
				<!-- side menu -->
				<nav class="sidebar-navigation">
					<ul style="list-style: none;">
						<li class="active"><a href="/userModifyLuggage"> <i
								class="fa-solid fa-suitcase-rolling"></i> <span class="tooltip">Luggage</span>
						</a></li>
						<li><a href="/userModifySeats"> <i class="fa-solid fa-chair"></i> <span
								class="tooltip">Seats</span>
						</a></li>
					</ul>
				</nav>
				
				<!-- luggage selection -->
				<div class="col">
					<form th:action="@{/userModifyLuggage/ProcessModifyLuggage}" th:object="${passengerListDTO}" method="post">
						<div class="row">
							<div th:each="passenger, passengerIndex : *{passengerList}"
								class="col-lg-4 col-md-4 mb-3">
								<div class="card card-pass" style="max-width: 450px;">
									<div class="row">
										<div class="col">
											<div class="card-body">
												<div class="form-floating mb-3">
													<!-- Display passenger name -->
													<input disabled type="text" class="form-control" th:id="${'passenger' + passenger.passportID}"
														th:value="${passenger.firstName + ' ' + passenger.lastName}">
													<label th:for="${'passenger' + passenger.passportID}">Passenger Name</label>
												</div>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col mb-3">
											<!-- Use unique name for luggage selection for each passenger -->
											<select
												th:name="'passenger_' + ${passenger.passportID} + '_luggageIds'"
												class="selectpicker form-control" multiple data-size="5">
												<option th:each="luggage : ${LuggageList}"
													th:value="${luggage.id}"
													th:text="${luggage.description + ' - ' + '$' + #numbers.formatDecimal(luggage.price, 0, 'COMMA', 0, 'POINT')}"
													th:selected="${passenger.flightAndLuggage.get(passenger.flightAndSeats.keySet()[0]).contains(#strings.toString(luggage.id))}"></option>
											</select>
											<script>
												// the script makes sure that handbag option (1st option)
												// is always selected for every passenger
												$(document).ready(function() {
												    $('.selectpicker option[value="1"]').prop('selected', true);
												    $('.selectpicker').selectpicker('refresh');
												    
												    $(".selectpicker").on("change", function() {
												    	$('.selectpicker option[value="1"]').prop('selected', true);
													    $('.selectpicker').selectpicker('refresh');
												      });
												});
											</script>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6 mb-3">
								<button type="submit"
									class="btn btn-outline-primary rounded-0">
									<i class="fa-regular fa-pen-to-square me-2"></i>Save
								</button>
								<span th:if="${success_message != null}" class="text-success" th:text="${success_message}"> </span>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>

</html>