<!-- The page displays user's bookings, with options of cancellation and modification -->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<!-- Security: CSRF token and header name, for sensitive AJAX requests -->
	<meta name="_csrf" th:content="${_csrf.token}" />
	<meta name="_csrf_header" th:content="${_csrf.headerName}" />

	<script>
	    $(document).ready(function() {
	    	/*
	    	------------------------------------
	    	event: cancel button was clicked
	    	------------------------------------
	    	*/
			$('.cancel-btn').on('click', function() {
				$('#confirmationModal').modal('show');
				var form = $(this).closest('.cancel-form');
				// sumbit form after confirmCancel button was clicked
				$('#confirmCancel').off('click').on('click', function() {
					form.submit();
				});
			});
	    
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
	    	event: info button was clicked
	    	------------------------------------
	    	*/
		    $('.openInfoModalButton').on('click', function() {
		        var bookingID = $(this).data('booking-id');
		        // load page into modal content
		        $('#modal-body').load('/userBookingInfo?bookingID=' + bookingID, function() {
		            $('#modal').modal('show');
		        });
		    });

	    	/*
	    	------------------------------------
	    	event: modify button was clicked (there are few)
	    	------------------------------------
	    	*/
	    	$('.modifyBtn').on('click', function() {
	    		
	    		/* Security: CSRF token and header name, for sensitive AJAX requests */
	    		var token = $("meta[name='_csrf']").attr("content");
	    		var header = $("meta[name='_csrf_header']").attr("content");
	    			
	    		
	        	console.log("x");
	            var bookingID = $(this).data('booking-id');
	            // save bookingID in session
	            $.ajax({
	                type: 'POST',
	                url: '/userBookings/saveInSessionUserBookingID',
	                data: {
	                    bookingID: bookingID
	                },
	    			// include CSRF token (of spring security) to the header of the request
	    			beforeSend: function(xhr) {
	    				xhr.setRequestHeader(header, token);
	    			},
	                success: function(data) {
	                	// assuming data consists the url to load
	                	window.location.href = data;
	                },
	                error: function(xhr, status, error) {
	                    console.error(error);
	                }
	            });
	        });
	    });
	</script>
</head>
<body>
	<div layout:fragment="content">
		<div class="container">
			<h2 class="fw-bold mb-4 text-center">My Bookings</h2>
			<p class="text-muted">
				Click on the buttons to view, modify or cancel your bookings.
			</p>
		</div>

		<section th:if="${!#lists.isEmpty(bookingsList)}">
			<div class="container">
				<div class="row">
					<!-- Loop through bookingsList -->
					<div th:each="booking : ${bookingsList}"
						class="col-lg-5 col-md-6 mb-4">
						<div class="card rounded-0">
							<div class="card-header text-white" style="background-color:#ced4da">
								<div class="row">
									<div class="col">
										<h5 class="card-title mb-0">
											Booking #<span th:text="${booking.id}"></span>
										</h5>
									</div>
									<div class="col d-flex justify-content-end">
										<form class="cancel-form"
											th:action="@{/userBookings/ProcessBookingCancellation}" method="post">
											<input type="hidden" name="bookingID" th:value="${booking.id}" />
											<!-- allow cancellation only if:
												1. booking is not canceled yet
												2. 1st flight in route is not canceled (-> so it the 2nd)
												3. more than 2 days before departure
											-->
											<button type="button"
											    th:if="${booking.isCanceled == false
											            and booking.route != null
											            and booking.route.flights != null
											            and not booking.route.flights.isEmpty()
											            and !#strings.equals(booking.route.flights[0].statusID, T(com.example.SwipeFlight.server.utils.Constants).FLIGHT_STATUS_CANCELED)
											            and T(com.example.SwipeFlight.server.utils.DateTimeUtils).isNowBeforeGivenDateTimeWithOffset(booking.route.flights[booking.route.flights.size()-1].departureDate, booking.route.flights[booking.route.flights.size()-1].departureTime, 2)}"
											    class="btn btn-outline-danger rounded-0 cancel-btn">
											    <i class="fa-solid fa-circle-xmark"></i>
											</button>
										</form>
									</div>
								</div>
							</div>
							<div class="card-body">
								<ul class="list-group list-group-flush">
									<li class="list-group-item border-0"><strong>Tickets:</strong><span
										class="float-end" th:text="${booking.numOfTickets}"></span></li>
									<li class="list-group-item border-0"><strong>Price:</strong><span
										class="float-end" th:text="${'$' + #numbers.formatDecimal(booking.totalPrice, 0, 'COMMA', 0, 'POINT')}"></span></li>
									<li class="list-group-item border-0"><strong>Booking
											Date:</strong><span class="float-end" th:text="${booking.bookingDate}"></span></li>
									<li class="list-group-item border-0"><strong>Last
											Modification Date:</strong><span class="float-end"
										th:text="${booking.lastModifyDate}"></span></li>
									<li th:each="status : ${FlightStatusList}"
										class="list-group-item border-0"
										th:if="${#strings.equals(#strings.toString(booking.route.flights[0].statusID), #strings.toString(status.id))}">
										<strong>Route Status:</strong> <span class="float-end"
										th:if="${status.id == 1}" th:text="${status.description}"></span>
										<!-- if status == scheduled --> <span
										class="float-end text-danger fw-bold"
										th:if="${status.id != 1}" th:text="${status.description}"></span>
										<!-- if status != scheduled -->
									</li>
									<li class="list-group-item border-0">
										<strong>Booking Status:</strong>
										<span th:if="${booking.isCanceled == true}" class="float-end text-danger fw-bold">Canceled</span>
										<span th:if="${booking.isCanceled == false}"class="float-end" >Active</span>
									</li>
								</ul>
							</div>
							<div class="d-flex justify-content-center">
								<!-- always allow displaying information -->
								<button type="button"
									class="btn btn-primary rounded-0 me-2 openInfoModalButton"
									th:data-booking-id="${booking.id}">
									<i class="fa-solid fa-circle-info me-2"></i> Details
								</button>
								<!-- allow modifying only if:
									1. booking is not canceled yet
									2. 1st flight in route is not canceled (-> so it the 2nd)
									3. more than 2 days before departure
								-->
								<button type="button"
										    th:if="${booking.isCanceled == false
										            and booking.route != null
										            and booking.route.flights != null
										            and not booking.route.flights.isEmpty()
										            and !#strings.equals(booking.route.flights[0].statusID, T(com.example.SwipeFlight.server.utils.Constants).FLIGHT_STATUS_CANCELED)
										            and T(com.example.SwipeFlight.server.utils.DateTimeUtils).isNowBeforeGivenDateTimeWithOffset(booking.route.flights[booking.route.flights.size()-1].departureDate, booking.route.flights[booking.route.flights.size()-1].departureTime, 2)}"
										class="btn btn-primary rounded-0 me-2 modifyBtn"
										th:data-booking-id="${booking.id}">
										<i class="fa-regular fa-pen-to-square me-2"></i> Modify
								</button>
							</div>
						</div>
					</div>
					<!-- End Loop on bookingsList -->
				</div>
			</div>
		</section>

		<section th:if="${#lists.isEmpty(bookingsList)}">
			<div class="container text-center">
				<br /> <div class="alert alert-primary w-40" role="alert">
					No routes exist.
				</div>
			</div>
		</section>
		
		<!-- Modal -->
		<div class="modal fade" id="modal" aria-labelledby="exampleModalLabel"
			aria-hidden="true">
			<div class="modal-dialog modal-xl modal-dialog-centered">
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
						booking?</div>
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