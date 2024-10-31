<!-- 
	* The page displays Feedback form, for a given route
	* * The page is displayed in a modal in page userFeedbackAvailability.jsp
-->

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org"
	xmlns:sec="https://www.thymeleaf.org/thymeleaf-extras-springsecurity5">
<head>
	<!-- js files -->
	<script type="text/javascript" th:src="@{/js/add.js}"></script>
	
    <style>
    	/* stars */
        .checked {
            color: #FFC109;
            font-size: 30px;
        }
        .unchecked {
            font-size: 30px;
        }
        .fa-star {
        	cursor: pointer;
        }
    </style>
	<script>
		$(document).ready(function() {
		
			/*
			------------------------------------
			event: a star was clicked
			------------------------------------
			*/
		    $(".fa-star").click(function() {
		        var rating = $(this).data("rating");
		        var category = $(this).data("category");
		        // remove 'checked' class from all stars in the same category
		        $(".fa-star[data-category='" + category + "']").removeClass("checked");
		        // add 'checked' class to clicked star and all previous stars in the same category
		        $(this).addClass("checked").prevAll(".fa-star[data-category='" + category + "']").addClass("checked");
		        // set the value of hidden input field to the selected rating
		        $("#" + category + "RatingInput").val(rating);
		    });
		});
	</script>
</head>
<body>

	<div th:if="${success_message == null}">
		<form id="addForm" th:action="@{/userFeedbackAdd/ProcessFeedbackForm}" method="post">
			<!-- Cleaning rating -->
			<div class="row">
				<div class="col">
					<label class="m-4">Cleaning</label>
				</div>
				<div class="col mt-4 text-secondary">
					<span class="fa fa-star unchecked" data-rating="1"
						data-category="cleaning"></span> <span
						class="fa fa-star unchecked" data-rating="2"
						data-category="cleaning"></span> <span
						class="fa fa-star unchecked" data-rating="3"
						data-category="cleaning"></span> <span
						class="fa fa-star unchecked" data-rating="4"
						data-category="cleaning"></span> <span
						class="fa fa-star unchecked" data-rating="5"
						data-category="cleaning"></span>
				</div>
			</div>
			<!-- Convenience rating -->
			<div class="row">
				<div class="col">
					<label class="m-4">Convenience</label>
				</div>
				<div class="col mt-4 text-secondary">
					<span class="fa fa-star unchecked" data-rating="1"
						data-category="convenience"></span> <span
						class="fa fa-star unchecked" data-rating="2"
						data-category="convenience"></span> <span
						class="fa fa-star unchecked" data-rating="3"
						data-category="convenience"></span> <span
						class="fa fa-star unchecked" data-rating="4"
						data-category="convenience"></span> <span
						class="fa fa-star unchecked" data-rating="5"
						data-category="convenience"></span>
				</div>
			</div>
			<!-- Service rating -->
			<div class="row">
				<div class="col">
					<label class="m-4">Service</label>
				</div>
				<div class="col mt-4 text-secondary">
					<span class="fa fa-star unchecked" data-rating="1"
						data-category="service"></span> <span class="fa fa-star unchecked"
						data-rating="2" data-category="service"></span> <span
						class="fa fa-star unchecked" data-rating="3"
						data-category="service"></span> <span class="fa fa-star unchecked"
						data-rating="4" data-category="service"></span> <span
						class="fa fa-star unchecked" data-rating="5"
						data-category="service"></span>
				</div>
			</div>
			<!-- Hidden input fields to store selected ratings -->
			<!-- values are initialized to 0, and are modified in javascript based on the stars -->
			<input type="hidden" name="cleaningRating" id="cleaningRatingInput"
				value="0"> <input type="hidden" name="convenienceRating"
				id="convenienceRatingInput" value="0"> <input type="hidden"
				name="serviceRating" id="serviceRatingInput" value="0"> <input
				type="hidden" name="routeNumber" th:value="${routeNumber}">

			<div class="text-center mt-4">
				<button type="submit" class="btn btn-primary rounded-0">Submit</button>
			</div>
			<span th:if="${error_message != null}" class="text-danger"
				th:text="${error_message}"> </span>

		</form>
	</div>
	<div th:if="${success_message != null}" class="text-success">
		<i class="fa-solid fa-circle-check me-2"></i><span th:text="${success_message}"> </span>
	</div>


</body>
</html>