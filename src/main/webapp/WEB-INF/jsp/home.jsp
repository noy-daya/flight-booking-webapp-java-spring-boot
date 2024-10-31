<!-- The page displays cities according to searching bar -->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<style>
	.card:hover {
		box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.2);
	}
	
	.limited { /*limit description length */
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 4;
		line-clamp: 4;
		overflow: hidden;
	}
	</style>
</head>
<body>
	<div layout:fragment="content">
		<section id="gallery">
			<div class="container">
				<div class="row">
					<div th:each="city : ${citiesList}" class="col-lg-3 col-md-6 mb-4">
						<div class="card rounded-0">
							<img class="card-img-top rounded-0" th:src="${city.imgUrl}" />
							<!-- images/countries/... -->
							<div class="card-body">
								<h5 class="card-title text-center" th:text="${city.name}"></h5>
								<p class="card-text limited" th:text="${city.description}"></p>
								<div th:if="${sessionUser != null && sessionUser.isAdmin == true}">
									<form th:action="@{/adminCity}" method="post">
										<input type="hidden" name="cityID" th:value="${city.id}" />
										<div class="d-grid gap-2">
											<button type="submit"
												class="btn btn-outline-primary rounded-0">Read More</button>
										</div>
									</form>
								</div>
								<div th:if="${sessionUser == null || sessionUser.isAdmin == false}">
									<form th:action="@{/city}" method="post">
										<input type="hidden" name="cityID" th:value="${city.id}" />
										<div class="d-grid gap-2">
											<button type="submit"
												class="btn btn-outline-primary rounded-0">Read More</button>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>

	</div>
</body>
</html>