/*
=====================================================================================
The following file consist functios related route modification.
=====================================================================================
*/

/*
* The function is called from page adminRouteModify.jsp
* - performs AJAX reqeust to update all the flights in route (arrival date + time + plane)
* - displays a message.
* - CSRF token will be added into AJAX request's header.
*/
function updateAllFlights() {
	
	/* Security: CSRF token and header name, for sensitive AJAX requests */
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
		
	var flightIds = []; 		// Array to store flight IDs
	var departureDates = []; 	// Array to store departureDates
	var departureTimes = []; 	// Array to store departureTimes
	var planeIDs = []; 			// Array to store planeIDs
	var noneIsEmpty = true; 	// Flag to indicate if all values are not empty
	
	// iterate over each row in the table
	$('#table tbody tr').each(function() {
		var flightId = $(this).data('flight-id');
		var departureDate = $(this).find('.departure-date').val();
		var departureTime = $(this).find('.departure-time').val();
		var planeID = $(this).find('.plane-id').find('select').val();
		
		console.log(departureDate + ',' + departureTime + ',' + planeID);
		
		if (departureDate == "" || departureTime == "" || planeID == "") {
			noneIsEmpty = false;
			return false; // Exit the function
		}

		flightIds.push(flightId);
		departureDates.push(departureDate);
		departureTimes.push(departureTime);
		planeIDs.push(planeID);
	});

	// update only if none of the fields is empty
	if (noneIsEmpty) {
		// Send updated flights information to the server
		$.ajax({
			url: '/adminRouteModify/updateAllFlights',
			type: 'POST',
			dataType: 'json',
			traditional: true, // for serialization
			data: {
				flightIds: flightIds,
				departureDates: departureDates,
				departureTimes: departureTimes,
				planeIDs: planeIDs
			},
			// include CSRF token (of spring security) to the header of the request
			beforeSend: function(xhr) {
				xhr.setRequestHeader(header, token);
			},
			success: function(response) {
				$("#success_message").text(response.success_message);
				$("#error_message").text("");

			},
            error: function(xhr, status, error) {
                try {
                    var response = JSON.parse(xhr.responseText);
                    $("#error_message").text(response.error_message);
                    $("#success_message").text("");
                } catch (e) {
					console.log(e);
                    $("#error_message").text("An unexpected error occurred.");
                    $("#success_message").text("");
                }
            }
		});
	}
}

/*
* The function is called from page adminRouteModify.jsp
* - parameter: modifiedInput (to access the row it belongs to)
* - performs AJAX reqeust to calculate and fetch the arrival date and time.
* - displays the calculation.
* - CSRF token will be added into AJAX request's header.
*/
function setArrivalDateTime(modifiedInput) {
	
	/* Security: CSRF token and header name, for sensitive AJAX requests */
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
		
	// Find the closest table row to the changed departure date or time
	var row = $(modifiedInput).closest('tr');

	// Create a Flight object
	var flight = {
		id: row.data('flight-id'),
		departureDate: row.find('.departure-date').val(),
		departureTime: row.find('.departure-time').val(),
		duration: row.find('.duration').val()
	};

    // Send AJAX request to server
    $.ajax({
        url: '/adminRouteModify/fetchArrivalDateTime',
        method: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(flight), // Send the Flight object as JSON
		// adding in the csrf token and header name into the head meta tags in the application
		beforeSend: function( xhr ) {
			  xhr.setRequestHeader(header, token);
		},
        success: function(response) {
            // Update arrival date and time in the corresponding cells
            row.find('.arrival-date').text(response.arrivalDate);
            row.find('.arrival-time').text(response.arrivalTime);
        },
        error: function(xhr, status, error) {
            // Handle error
            console.error('Error:', error);
        }
    });
}


/*
* The function is called from page adminRouteModify.jsp
* - performs AJAX reqeust to visualize the availability of the planes.
*/
function updatePlaneAvailability($dropdown)
{
    $.ajax({
        url: '/plane/FetchPlanes',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            // iterate over each option and update the available quantity
            $dropdown.find('option').each(function() {
                var option = $(this);
                var planeId = option.val();

                // iind the corresponding plane data
                var plane = data.find(function(p) {
                    return p.id == planeId;
                });

                if (plane) {
                    // Update the data-available attribute and subtext
                    option.attr('data-available', plane.availableQuantity);
                    option.attr('data-subtext', 'available: ' + plane.availableQuantity + ', capacity: ' + (plane.numOfRows * plane.numOfSeatsPerRow));
                }
            });

            // Refresh the Bootstrap Select dropdown to reflect changes
            $dropdown.selectpicker('refresh');
        },
        error: function(xhr, status, error) {
            console.error('error:', error);
        }
    });
}