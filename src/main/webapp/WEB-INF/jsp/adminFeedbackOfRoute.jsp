<!--
	* The page displays chart for the given route, consisting average grades from users.
	* The page is displayed in a modal in page adminFeedbacks.jsp
-->

<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <!-- chart -->
    <script src="https://cdn.plot.ly/plotly-2.32.0.min.js" charset="utf-8"></script>
</head>

<body>
   	<div id="myDiv"></div>
   	<input type="hidden" id="routeNumber" th:value="${routeNumber}">
	<div id="no-feedback-msg" class="alert alert-primary w-40" role="alert">
		No feedbacks were submitted for the route.
	</div>
		
    <script>
        $(document).ready(function() {
        	
			/*
			------------------------------------
			display chart or no-feedback-msg
			------------------------------------
			*/
        	$('#no-feedback-msg').hide();
        	var routeNumber = $('#routeNumber').val(); // hidden field
        	// retrieve values for the given route and display chart
            $.ajax({
                url: "/adminFeedbackOfRoute/chartData",
                type: "GET",
                data: {
                	routeNumber: routeNumber
				},
                dataType: "json",
                success: function(response) { // response is the List<Double> returned from controller
                	console.log(response);
                	// if values exist
                    if(response[0] != 0 || response[1] != 0 || response[2] != 0)
                    {
                    	$('#no-feedback-msg').hide();
                    	var xValue = ['Cleaning', 'Convenience', 'Service'];
	                    var yValue = response;
	
	                    var trace1 = {
	                        x: xValue,
	                        y: yValue,
	                        type: 'bar',
	                        text: yValue.map(String),
	                        textposition: 'auto',
	                        hoverinfo: 'none',
	                        marker: {
	                            color: 'rgb(13, 110, 253)',
	                            opacity: 0.6,
	                            line: {
	                                width: 0
	                            }
	                        }
	                    };
	                    var data = [trace1];
	                    var layout = {
	                        title: 'Average Ratings For Route #' + routeNumber,
	                        showlegend: false
	                    };
	
	                    Plotly.newPlot('myDiv', data, layout, {staticPlot: true});
                    }
                	// values are 0 (-> no feedbacks were submitted for this route)
                    else
                    {
                    	$('#no-feedback-msg').show();
                    }
                },
                error: function(xhr, status, error) {
                    console.error("Error fetching data:", error);
                }
            });
        });
    </script>

</body>
</html>