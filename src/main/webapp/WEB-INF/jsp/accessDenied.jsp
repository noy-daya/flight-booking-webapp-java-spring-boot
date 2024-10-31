<!--
	* The page displays a message "access denied"
		in case a user's request is not allowed due to their authorities.
	* WebSecurityConfig class determines accessDeniedHandler which redirects to this page.
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
                    <p><i class="fa fa-exclamation-triangle fa-5x me-2"></i><br/>Status Code: 403</p>
               </div>
               <div class="col-md-10">
                    <h3>Sorry...</h3>
                    <p>Sorry, you do not have permission to access this page.<br/>Please go back to the previous page to continue browsing.</p>
                    <a class="btn btn-outline-danger" href="javascript:history.back()">Go Back</a>
               </div>
          </div>
     </div>
    </div>
</body>
</html>