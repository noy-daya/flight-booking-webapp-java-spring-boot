<!--
	The page displays an error message,
	in case that exception from GlobalExceptionHandler.js occurs.
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
     <div class="container py-5">
          <div class="row">
               <div class="col-md-2 text-center">
                    <p><i class="fa fa-xmark fa-5x"></i><br/></p>
               </div>
               <div class="col-md-10">
                    <h3>Oops, Something went wrong!</h3>
                    <p th:text="${error_message}"></p>
                    <a class="btn btn-outline-danger" href="javascript:history.back()">Go Back</a>
               </div>
          </div>
     </div>
    </div>
</body>
</html>