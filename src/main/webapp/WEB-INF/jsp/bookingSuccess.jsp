<!-- This page displays a message for successful booking -->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<!-- css files -->
	<link th:href="@{/css/bookingSuccess.css}" rel="stylesheet" />
</head>
<body>
	<div layout:fragment="content">
		<div class="container py-5">
			<div class="row">
				<div class="col-md-2 text-center">
					<div class="success-animation">
						<svg class="checkmark" xmlns="http://www.w3.org/2000/svg"
							viewBox="0 0 52 52">
							<circle class="checkmark__circle" cx="26" cy="26" r="25"
								fill="none" />
							<path class="checkmark__check" fill="none"
								d="M14.1 27.2l7.1 7.2 16.7-16.8" /></svg>
					</div>
				</div>
				<div class="col-md-10">
					<h3>Your flight was booked!</h3>
					<h4 th:if="${bookingID != null}"
						th:text="${'Booking ID #' + bookingID}"></h4>
					<a class="btn btn-primary rounded-0 p-2" href="/home">Keep traveling</a>
				</div>
			</div>
		</div>
	</div>
</body>
</html>