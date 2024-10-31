<!--
	* The page displays the bookings
-->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
</head>
<body>
	<div layout:fragment="content">
		<div class="container">
			<h2 class="fw-bold mb-4 text-center">Bookings</h2>
			<p class="text-muted">
				The page displays all the bookings in the system.
				<br/>
				Additionally, you have the option to search for specific bookings to streamline
				your navigation.
			</p>
		</div>

		<!-- filter form -->
		<section>
			<div class="container">
				<form th:action="@{/adminBookings}" class="mb-4">
					<div class="row">
						<div class="col-md-2">
							<input type="number" class="form-control" name="bookingID"
								placeholder="Booking ID" th:value="${param.bookingID}">
						</div>
						<div class="col-md-2">
							<input type="number" class="form-control" name="userID"
								placeholder="User ID" th:value="${param.userID}">
						</div>
		                <div class="col-md-2">
		                    <input type="checkbox" class="btn-check" id="btn-check-4" autocomplete="off"
		                           name="isCanceled" value="true"
		                           th:checked="${#strings.equals(param.isCanceled, 'true')}">
		                    <label class="btn" for="btn-check-4">Canceled</label>
		                </div>
					</div>
					<div class="row mt-3 d-flex justify-content-center text-center">
						<div class="col-md-3 mb-5">
							<button type="submit" class="btn btn-secondary rounded-0">Search</button>
						</div>
					</div>
				</form>
			</div>
		</section>
		
		<section th:if="${!bookingsList.isEmpty()}">
			<div class="container" style="align-items: center;">
				<table id="table" class="table table-hover">
					<thead>
						<tr>
							<th>Booking</th>
							<th>User ID</th>
							<th>Tickets</th>
							<th>Total Price</th>
							<th>Booking Date</th>
							<th>Last Modification</th>
							<th>Status</th>
						</tr>
					</thead>
					<tbody>
						<tr th:each="booking : ${bookingsList}">
							<td><span th:text="${booking.id}"></span></td>
							<td><span th:text="${booking.userID}"></span></td>
							<td><span
								th:text="${booking.numOfTickets}"></span>
							</td>
							<td><span
								th:text="${'$' + #numbers.formatDecimal(booking.totalPrice, 0, 'COMMA', 0, 'POINT')}"></span>
							</td>
							<td><span
								th:text="${booking.bookingDate}"></span>
							</td>
							<td><span
								th:text="${booking.lastModifyDate}"></span>
							</td>
							<td>
								<button disabled type="button" th:if="${booking.isCanceled == true}"
									class="btn btn-danger rounded-0">Canceled
								</button>
								<button disabled type="button" th:if="${booking.isCanceled == false}"
									class="btn btn-success rounded-0">Active
								</button>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</section>

		<section th:if="${bookingsList.isEmpty()}">
			<br />
			<div class="container text-center">
				<div class="alert alert-primary w-40" role="alert">
				No bookings exist for the given filters. </div>
			</div>
		</section>

	</div>
</body>
</html>