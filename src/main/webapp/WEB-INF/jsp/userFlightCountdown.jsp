<!-- The page displays countDown until user's next flight -->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
    <!-- css files -->
    <link th:href="@{/css/countdown.css}" rel="stylesheet" /> <!-- for countDown -->
    
    <!-- js files -->
    <script type="text/javascript" th:src="@{/js/countdown.js}"></script> <!-- for countDown -->

</head>
<body>
	<div layout:fragment="content">
		<div class="container" style="align-items: center;">
			<i class="fa-solid fa-plane text-info display-4 me-2"></i>
			<h1 id="headline" class="text-info">My Upcoming Flight</h1>

			<section th:if="${route != null}">
				<div th:object="${route}">
					<div th:if="${#lists.size(route.flights) == 1}">
						<!-- The list has one flight -->
						<div th:each="flight : ${route.flights}"
							class="d-flex justify-content-center">
							<div th:if="${flightStat.index == 0}">
								<span class="me-4"
									th:text="${'Departure On ' + flight.departureDate}"></span> <span
									class="me-4"
									th:text="${'From ' + flight.departureAirport.city.name + ', ' + flight.departureAirport.city.country.name}"></span>
								<span
									th:text="${'To ' + flight.arrivalAirport.city.name + ', ' + flight.arrivalAirport.city.country.name}"></span>
							</div>
						</div>
					</div>
					<div th:if="${#lists.size(route.flights) == 2}"
						class="d-flex justify-content-center">
						<!-- The list has two flights -->
						<div th:each="flight : ${route.flights}">
							<div th:if="${flightStat.index == 0}">
								<span class="me-4"
									th:text="${'Departure On ' + flight.departureDate}"></span> <span
									class="me-4"
									th:text="${'From ' + flight.departureAirport.city.name + ', ' + flight.departureAirport.city.country.name}"></span>
							</div>
							<div th:if="${flightStat.index == 1}">
								<span
									th:text="${'To ' + flight.arrivalAirport.city.name + ', ' + flight.arrivalAirport.city.country.name}"></span>
							</div>
						</div>
					</div>
					<input type="hidden" id="departureDate"
						th:value="${route.flights[0].departureDate}" />
				</div>
				<div id="countdown">
					<ul>
						<li class="countdownTitle"><span id="days"></span>days</li>
						<li class="countdownTitle"><span id="hours"></span>Hours</li>
						<li class="countdownTitle"><span id="minutes"></span>Minutes</li>
						<li class="countdownTitle"><span id="seconds"></span>Seconds</li>
					</ul>
				</div>
				<script>
					// run script only within this section
	    			$(document).ready(function() {
	        			startCountdown(); // script in: js/countdown.js
	    			});
    			</script>
			</section>

			<section th:if="${route == null}">
				<div class="container text-center">
					<br /> <span class="alert alert-primary w-40" role="alert">
						No routes exist, join us to explore a new adventure! </span>
				</div>
			</section>
		</div>
	</div>
</body>
</html>