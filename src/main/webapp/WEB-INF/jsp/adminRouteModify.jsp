<!-- 
	* The page displays route modification form, for a given route.
	* allows admin to modify departure date/time/plane.
	* The page is displayed in a modal in page adminRoutes.jsp
-->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<!-- Security: CSRF token and header name, for sensitive AJAX requests -->
	<meta name="_csrf" th:content="${_csrf.token}" />
	<meta name="_csrf_header" th:content="${_csrf.headerName}" />

	<!-- bootstrap-select -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>

	<!--  FlatPickr library for datePicker  -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
	<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>

	<!-- js files -->
	<script type="text/javascript" th:src="@{/js/routeModify.js}"></script>

<script>
$(document).ready(function() {
    $('.selectpicker').selectpicker(); // Initialize bootstrap-select

    // Preselect the value on page load based on flight.plane.id
    $('.selectpicker').each(function() {
        var selectedValue = $(this).data('selected');
        $(this).selectpicker('val', selectedValue);
    });

    // Update only the available quantities when the dropdown is shown
    $('.selectpicker').on('show.bs.select', function() {
        var $dropdown = $(this); // Store the correct 'this' context
        updatePlaneAvailability($dropdown); // file: 'routeModify.js'
    });

    // Attach event listeners for departure-date, departure-time, and plane-id change events
    $('.departure-date, .departure-time').change(function() {
        $("#success_message").text("");
        $("#error_message").text("");
        // 1. calculate and display the up-to-date arrival date-time
        setArrivalDateTime(this); // file: "/js/routeModify.js"
        // 2. update flights in database
        updateAllFlights(); // file: "/js/routeModify.js"
    });

    // Call updateAllFlights only when an option is changed
    $('.selectpicker').on('changed.bs.select', function(e, clickedIndex, isSelected, previousValue) {
        updateAllFlights(); // file: "/js/routeModify.js"
    });
});
</script>
</head>
<body>
	<table id="table" class="table table-hover table-responsive">
		<thead class="table-light">
			<tr>
				<th>#</th>
				<th>From</th>
				<th>To</th>
				<th>Departure Date</th>
				<th>Departure Time</th>
				<th>Arrival Date</th>
				<th>Arrival Time</th>
				<th>Duration</th>
				<th>Plane</th>
			</tr>
		</thead>
		<tbody>
			<tr th:each="flight : ${flightsList}"
				th:data-flight-id="${flight.id}">
				<td th:text="${flight.id}"></td>
				<td>
					<p th:text="${flight.departureAirport.code}"></p> <small
					class="text-muted"
					th:text="${flight.departureAirport.city.name + ', ' + flight.departureAirport.city.country.name}"></small>
				</td>
				<td>
					<p th:text="${flight.arrivalAirport.code}"></p> <small
					class="text-muted"
					th:text="${flight.arrivalAirport.city.name + ', ' + flight.arrivalAirport.city.country.name}"></small>
				</td>
				<td><input type="text" name="departureDate" id="departureDate"
					th:value="${flight.departureDate}"
					class="form-control departure-date" placeholder="Departure Date">
					<script>
						flatpickr("#departureDate", {
							dateFormat : "Y-m-d",
							altInput : true,
							altFormat : "d.m.Y",
						});
					</script>
				</td>
				<td><input type="text" name="departureTime" id="departureTime"
					th:value="${flight.departureTime}"
					class="form-control departure-time" placeholder="Departure Time">
					<script>
						flatpickr("#departureTime",
						{
						    enableTime: true,
						    noCalendar: true,
						    dateFormat: "H:i:ss",
						});
					</script>
				</td>
				<td class="arrival-date" th:text="${flight.arrivalDate}"></td>
				<td class="arrival-time" th:text="${flight.arrivalTime}"></td>
				<td><input disabled
					th:value="${T(com.example.SwipeFlight.server.utils.Formatter).formatDuration(flight.duration) + ' hours'}"
					class="form-control"
					style="background-color: transparent; border: 0" /> <input
					type="hidden" class="duration" th:value="${flight.duration}" /></td>
				<td>
					<div th:if="${is_booking_allowed == false}">
						<select name="planeID"
						class="selectpicker plane-id form-control" data-size="5">
							<option value="0">Select</option>
							<option th:each="plane : ${planesList}" th:value="${plane.id}"
								th:text="${plane.description}"
								th:data-subtext="${'available: ' + plane.availableQuantity +
	                        									', capacity: ' + (plane.numOfRows * plane.numOfSeatsPerRow)}"
	                        	th:selected="${#strings.equals(#strings.toString(plane.id), #strings.toString(flight.plane.id))}">
	                        </option>
						</select>
					</div>
					<div th:if="${is_booking_allowed == true}">
						<select disabled name="planeID"
						class="selectpicker plane-id form-control" data-size="5">
							<option th:each="plane : ${planesList}" th:value="${plane.id}"
								th:text="${plane.description}"
								th:data-subtext="${'available: ' + plane.availableQuantity +
	                        									', capacity: ' + (plane.numOfRows * plane.numOfSeatsPerRow)}"
	                        	th:selected="${#strings.equals(#strings.toString(plane.id), #strings.toString(flight.plane.id))}">
	                        </option>
						</select>
					</div>
				</td>
			</tr>
		</tbody>
	</table>

	<span id="success_message" class="text-success"></span>
	<span id="error_message" class="text-danger"></span>

</body>
</html>