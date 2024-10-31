<!--
	* The page displays information about a selected city.
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

		<div class="d-flex justify-content-center">
			<div class="card mb-3" style="width: 70%;">
				<div class="row g-0" th:object="${city}">
					<div class="col-md-4">
						<img th:src="${city.imgUrl}" class="img-fluid rounded-start"
							alt="City Image">
					</div>
					<div class="col-md-8">
						<div class="card-body">
							<h5 class="card-title" th:text="*{name + ', ' + country.name}"></h5>
							<div>
								<p th:field="*{description}" th:text="*{description}"></p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>