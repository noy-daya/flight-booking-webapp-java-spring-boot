<!--
	* The page displays information about a selected city, for both users and admin.
	* For admin, allows modifying information and uploading a picture.
 -->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
	layout:decorate="~{template}"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<!-- js files -->
	<script type="text/javascript" th:src="@{/js/fileUpload.js}"></script>

	<!-- css file -->
	<link th:href="@{/css/fileUpload.css}" rel="stylesheet"/>
	
	<script>
		$(document).ready(function() {
	
			$('.clear-btn').hide();
	
	    	/*
	    	------------------------------------
	    	event: a file was selected
	    	------------------------------------
	    	*/
			$('#fileInput').change(function() {
				// Update file name and preview on file selection
				updateFileName();
				previewImage();
				toggleClearButton();
				validateFileSize();
			});
	
	    	/*
	    	------------------------------------
	    	event: clear button was clicked
	    	------------------------------------
	    	*/
			$('.clear-btn').click(function() {
				// Clear file input and preview
				clearFile();
			});
		});
	</script>
	
</head>
<body>
	<div layout:fragment="content">

		<div class="d-flex justify-content-center">
			<div class="card mb-3" style="width: 70%;">
				<form method="post" enctype="multipart/form-data"
					th:action="@{/adminCity/ProcessCityModification}" th:object="${city}">
					<div class="row g-0">
						<div class="col-md-4">
							<img th:src="${city.imgUrl}" class="img-fluid rounded-start"
								alt="City Image">
						</div>
						<div class="col-md-8">
							<div class="card-body">
								<h5 class="card-title" th:text="*{name + ', ' + country.name}"></h5>
								<div>
									<textarea name="description" th:field="*{description}"
										th:value="${description}" class="form-control"
										placeholder="Description" style="height: 300px"
										maxlength="950"></textarea>
									<div class="text-danger">
										<p th:if="${#fields.hasErrors('description')}"
											th:errors="*{description}" class="error-message"></p>
									</div>
								</div>
							</div>
						</div>
						<div>
							<div class="card-footer">
								<div class="row">
									<div class="col col-10">
										<div class="upload-btn-wrapper">
											<label class="btn btn btn-info rounded-0 text-white"> <i
												class="fa-solid fa-file-image me-2"></i> <input type="file"
												name="fileInput" id="fileInput"
												onchange="updateFileName(); previewImage();"
												style="display: none;" />
											</label> <span id="fileName">No file chosen</span> <label
												class="clear-btn" onclick="clearFile()" title="clear">
												<i class="fa-solid fa-circle-xmark text-danger me-2"></i>
											</label>
										</div>
										<div id="imagePreview"></div>
										<div class="text-danger">
											<p th:if="${#fields.hasErrors('imgUrl')}"
												th:errors="*{imgUrl}" class="error-message"></p>
											<p id="sizeErrorMessage" style="display: none;"></p>
										</div>

										<input type="hidden" name="id" th:field="*{id}"
											class="form-control"> <input type="hidden"
											name="name" th:field="*{name}" class="form-control">
									</div>
									<div class="col d-flex justify-content-end align-self-start">
										<button type="submit" class="btn btn-primary rounded-0">
											<i class="fa-solid fa-plus me-2"></i>Modify
										</button>
									</div>
									<span th:if="${success_message != null}" class="text-success"
											th:text="${success_message}"></span>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>

	</div>


</body>
</html>