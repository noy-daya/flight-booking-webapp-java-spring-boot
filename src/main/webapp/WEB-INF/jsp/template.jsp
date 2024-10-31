<!-- The page is the main template of the web-app (displays logo, search and menu) -->

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">
<head>
	<meta charset="UTF-8">
	
	<!-- bootstrap, font-awesome, bootstrap-icons -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"/>
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" />
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
	
	<!-- auto-complete -->
	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/jquery-ui.js"></script>
	<link rel="stylesheet" type="text/css" media="screen" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.1/themes/base/jquery-ui.css" />
	
	<!-- bootstrap scripts for modal -->
	<script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/js/bootstrap.min.js"></script>
	
	<!--  using Flatpickr library for datepicker  -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
	<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
	<!-- <link rel="stylesheet" type="text/css" href="https://npmcdn.com/flatpickr/dist/themes/dark.css"> <!-- color -->
	
	<!-- bootstrap-select -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>
	
	
	<!-- css files -->
	<link th:href="@{/css/general.css}" rel="stylesheet" />

	<script type="text/javascript">
        $(document).ready(function () {
	    	/*
	    	------------------------------------
	    	auto-complate
	    	------------------------------------
	    	*/
            $('.searchInput').autocomplete({
            	// when user enteres something: get list of suggestions
                source: '/citySearch/fetchSuggestionsForCitySearch',
                // when user selects something from the suggestions: refresh page with the results
                select: function(event, ui) {
                    var selectedCityFromSuggestions = ui.item.value;
                    window.location.href = '/home?selectedCityFromSuggestions=' + selectedCityFromSuggestions;
                }
            });
        });
    </script>
</head>
<body>

	<!-- Header -->
	<header class="fixed-top">
	
		<svg class="waves" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 300">
  			<path fill="#fff" fill-opacity="1" d="M0,192L120,208C240,224,480,256,720,256C960,256,1200,224,1320,208L1440,192L1440,320L1320,320C1200,320,960,320,720,320C480,320,240,320,120,320L0,320Z"></path>
		</svg>
		
		<section>
			<div class="text-center bg-image p-4"
				style="background-image: url('/images/general/logo.png'); background-position: center 27%; background-repeat: no-repeat; background-size: cover;">
				
				<!-- first NavBar: brand, login/register, dropDown menus for authenticated users -->
				<nav class="navbar navbar-expand-lg bg-body-tertiary"
					style="background-color: white">
					<!-- Container wrapper -->
					<div class="container">
						<a class="navbar-brand text-primary fw-bolder"> <img
							src="/images/general/icon.png" height="30"
							style="margin-top: -1px;" />SWIPE FLIGHT
						</a>
						<button data-mdb-collapse-init class="navbar-toggler" type="button"
							data-mdb-target="#navbarButtonsExample"
							aria-controls="navbarButtonsExample" aria-expanded="false"
							aria-label="Toggle navigation">
							<i class="fas fa-bars"></i>
						</button>
						<div class="collapse navbar-collapse" id="navbarButtonsExample">
							<!-- Left links -->
							<ul class="navbar-nav me-auto mb-lg-0">
							</ul>
							<div th:if="${sessionUser == null}"
								class="d-flex align-items-center">
								<div class="d-flex align-items-center">
									<a data-mdb-ripple-init href="/login" class="btn btn-link me-2">Login</a>
									<a data-mdb-ripple-init href="/registration"
										class="btn btn-link me-2">Register</a>
								</div>
							</div>
							<div th:if="${sessionUser != null}"
								class="d-flex align-items-center">
								<div th:if="${sessionUser.isAdmin == true}">
									<div class="dropdown">
										<button
											class="dropdown-toggle text-white bg-primary border-0 rounded-1"
											type="button" data-toggle="dropdown" aria-haspopup="true"
											aria-expanded="false">
											<i class="fa-solid fa-star me-2"></i> <span
												th:text="${sessionUser.userName}"></span>
										</button>
										<div class="dropdown-menu main-menu">
											<a class="dropdown-item main-item" href="/adminAddFlight">Add
												Flight</a> <a class="dropdown-item main-item" href="/adminRoutes">Routes
												</a> <a class="dropdown-item main-item"
												href="/adminFeedbacks">View Feedbacks</a>
												<a class="dropdown-item main-item"
												href="/adminBookings">View Bookings</a>
										</div>
									</div>
								</div>
								<div th:if="${sessionUser.isAdmin == false}">
									<div class="dropdown">
										<button
											class="dropdown-toggle text-white bg-primary border-0 rounded-1"
											type="button" data-toggle="dropdown" aria-haspopup="true"
											aria-expanded="false">
											<i class="fa-solid fa-circle-user me-2"></i> <span
												th:text="${sessionUser.userName}"></span>
										</button>
										<div class="dropdown-menu main-menu">
											<a class="dropdown-item main-item" href="/userBookings">My
												Bookings</a> <a class="dropdown-item main-item"
												href="/userFlightCountdown">My Upcoming Flight</a> <a
												class="dropdown-item main-item"
												href="/userFeedbackAvailability">Feedback Forms</a>
										</div>
									</div>
								</div>
								<form th:action="@{/logout/ProcessUserLogout}" method="post">
									<button type="submit" data-mdb-ripple-init class="btn btn-link">Logout</button>
								</form>
							</div>
						</div>
					</div>
				</nav>
	
				<!-- second NavBar: main categories -->
				<nav class="navbar navbar-expand-lg navbar-dark"
					style="background-color: transparent; color: white">
					<div class="container-fluid">
						<button class="navbar-toggler" type="button"
							data-mdb-toggle="collapse" aria-controls="navbarExample01"
							aria-expanded="false" aria-label="Toggle navigation">
							<i class="fas fa-bars"></i>
						</button>
						<div class="collapse navbar-collapse">
							<ul class="navbar-nav me-auto mb-2 mb-lg-0">
								<li class="nav-item"><a class="nav-link" href="/home">Home</a></li>
								<li class="nav-item">
									<hr class="d-lg-none" /> <span
									class="nav-link d-none d-lg-block mx-1">|</span>
								</li>
								<li class="nav-item"><a class="nav-link" href="/aboutUs">About
										Us</a></li>
								<li class="nav-item">
									<hr class="d-lg-none" /> <span
									class="nav-link d-none d-lg-block mx-1">|</span>
								</li>
								<li class="nav-item"><a class="nav-link" href="/bookingTrip">Booking</a></li>
							</ul>
						</div>
					</div>
				</nav>
				<br /> <br /> <br />
				<br /> <br /> <br />
			</div>
		</section>
		
		<!-- search country (autoComplete) -->
		<section>
			<div class="d-flex">
				<div class="searchBox">
					<input class="searchInput" type="text" name="" placeholder="Where do you want to travel?" th:value="${citySearch}"
            				aria-label="Search" aria-describedby="search-addon">
            		<button class="searchButton">
		                <i class="fa-solid fa-magnifying-glass"></i>
            		</button>
        		</div>
			</div>
		</section>
	</header>

	<!-- Content: Dynamic -->
	<div layout:fragment="content" class="page-content"></div>

</body>
</html>