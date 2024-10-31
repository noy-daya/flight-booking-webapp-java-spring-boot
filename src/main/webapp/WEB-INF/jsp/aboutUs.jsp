<!--  The page displays information about the company -->

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

		<section class="py-3 py-md-5">
			<div class="container">
				<div class="row gy-3 gy-md-4 gy-lg-0 align-items-lg-center">
					<div class="col-12 col-lg-6 col-xl-5">
						<img class="img-fluid rounded" src="/images/general/aboutUs.jpeg">
					</div>
					<div class="col-12 col-lg-6 col-xl-7">
						<div class="row justify-content-xl-center">
							<div class="col-12 col-xl-11">
								<h2 class="mb-3">About Swipe Flight</h2>
								<p class="lead fs-4 text-secondary mb-3">At Swipe Flight,
									we're not just another airline; we're your gateway to 
									stress-free travel experiences. With a commitment to excellence
									and a passion for innovation, we're redefining the way you fly.</p>
								<p class="mb-3">Established with a vision to revolutionize
									the aviation industry, Swipe Flight took flight in 2024.
									Founded by a team of seasoned aviation professionals,
									technologists, and travel enthusiasts, our journey began with a
									simple yet powerful idea: to make air travel more accessible,
									comfortable, and enjoyable for everyone.</p>
								<div class="row gy-4 gy-md-0 gx-xxl-5X">
									<div class="col-12 col-md-6">
										<div class="d-flex">
											<div class="me-4 text-primary"></div>
											<div>
												<h2 class="h4 mb-3">Our Mission</h2>
												<p class="text-secondary mb-0">At Swipe Flight, our
													mission is to connect people, places, and cultures by
													providing safe, reliable, and affordable air travel
													solutions.</p>
											</div>
										</div>
									</div>
									<div class="col-12 col-md-6">
										<div class="d-flex">
											<div class="me-4 text-primary"></div>
											<div>
												<h2 class="h4 mb-3">Our Values</h2>
												<p class="text-secondary mb-0">We prioritize
													customer-centricity, innovation, and integrity 
													in all aspects of our operations.</p>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>

		<section class="py-3 py-md-5">
			<div class="container">
				<div class="row justify-content-center">
					<div class="col-md-10">
						<p class="lead mb-4 text-center">Join Us on Your Next
							Adventure</p>
						<p class="text-center mb-0">Whether you're embarking on a
							business trip, planning a family vacation, or exploring new
							destinations, Swipe Flight is your trusted partner in air travel.
							Join us on your next adventure and experience the difference of
							flying with Swipe Flight. Welcome aboard!</p>
					</div>
				</div>
			</div>
		</section>

	</div>
</body>
</html>