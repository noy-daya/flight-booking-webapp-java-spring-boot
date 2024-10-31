<!-- The page displays registration form -->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<script>
		/* spinner */
		function load(event) {
			event.preventDefault(); // Prevent default form submission behavior
			document.getElementById("spinner").style.display = "block";
			setTimeout(function() {
				document.getElementById("form").submit(); // Submit the form 0.5 second after spinner is displayed
			}, 500);
		}
	</script>
	<script>
		/* show/hide password */
		$(document).ready(function() {
		    $("#show_hide_password button").on('click', function(event) {
		        event.preventDefault();
		        var $passwordInput = $('#show_hide_password input');
		        var $icon = $('#show_hide_password i');
		        
		        if ($passwordInput.attr("type") == "text") {
		            $passwordInput.attr('type', 'password');
		            $icon.addClass("fa-eye-slash");
		            $icon.removeClass("fa-eye");
		        } else if ($passwordInput.attr("type") == "password") {
		            $passwordInput.attr('type', 'text');
		            $icon.removeClass("fa-eye-slash");
		            $icon.addClass("fa-eye");
		        }
		    });
		});
	</script>

</head>
<body>
	<div layout:fragment="content">

		<section>
			<div class="card mx-auto col-md-4 shadow-5-strong"
				style="background: hsla(0, 0%, 100%, 0.8); backdrop-filter: blur(30px);">
				<div class="card-body p-4">

					<div class="row d-flex justify-content-center">
						<div class="col-lg-10">
							<h2 class="fw-bold mb-5 text-center">Register</h2>
							<form id="form"
								th:action="@{/registration/ProcessUserRegistration}"
								th:object="${user}" method="post">

								<div class="form-floating mb-3">
									<input name="userName" type="text" th:field="*{userName}"
										class="form-control" placeholder="Username" maxlength="20" />
									<label for="userName">User Name*</label>
									<p th:if="${#fields.hasErrors('userName')}"
										th:errors="*{userName}" class="text-danger error-message">User
										name Error</p>
								</div>

								<div class="input-group mb-3" id="show_hide_password">
									<div class="form-floating flex-grow-1">
										<input name="password" type="password" th:field="*{password}"
											class="form-control" placeholder="Password" maxlength="20" />
										<label for="password">Password*</label>
									</div>
									<button class="btn btn-outline-secondary" type="button">
										<i class="fa fa-eye-slash"></i>
									</button>
								</div>
								<p th:if="${#fields.hasErrors('password')}"
									th:errors="*{password}" class="text-danger error-message">Password
									Error</p>

								<div class="form-floating mb-3">
									<input name="email" type="text" th:field="*{email}"
										class="form-control" placeholder="Email" maxlength="40" /> <label
										for="email">Email*</label>
									<p th:if="${#fields.hasErrors('email')}" th:errors="*{email}"
										class="text-danger error-message">Email Error</p>
								</div>

								<div class="text-center">
									<button type="submit" onclick="load(event)"
										data-mdb-button-init data-mdb-ripple-init
										class="btn btn-outline-primary col-12 rounded-0">
										<span id='spinner' style="display: none"
											class="spinner-border spinner-border-sm" role="status"
											aria-hidden="true"></span> Register
									</button>
								</div>

								<div th:if="${#fields.hasErrors('global')}"
									class="text-center mt-2">
									<p th:each="err : ${#fields.errors('global')}" th:text="${err}"
										class="text-danger"></p>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</section>

	</div>

</body>
</html>