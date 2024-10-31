/*
=====================================================================================
The following file consists functions relating to adding a flight into route.
=====================================================================================
*/

/*
* The function displays all the flights related to the route number in $('#routeNumber')
*/
function fetchFlightsInRouteNum(url) {
	var routeNumber = $('#routeNumber').val();
	$.ajax({
		url: url,
		type: 'GET',
		data: { routeNumber: routeNumber },
		dataType: 'json',
		success: function(data) {
			$('#flightsInRoute').empty();
			if (data.length == 2) // limit number of flights in route
			{
				$('#flightIDToAdd').attr('disabled', 'disabled');
				$('#limitErrorMessage').text('A route must contain no more than two distinct flights.');
				$('#limitErrorMessage').show();
				$('#submitBtn').attr('disabled', 'disabled');
			}
			else {
				$('#flightIDToAdd').removeAttr('disabled');
				$('#limitErrorMessage').hide();
			}

			$.each(data, function(index, flight) {
				$('#flightsInRoute').append('<tr>' +
					'<td>' + flight.id + '</td>' +
					'<td>' + flight.departureAirport.city.name + ', ' + flight.departureAirport.city.country.name + '</td>' +
					'<td>' + flight.arrivalAirport.city.name + ', ' + flight.arrivalAirport.city.country.name + '</td>' +
					'<td>' + flight.departureDate + '</td>' +
					'<td>' + flight.departureTime + '</td>' +
					'<td>' + flight.arrivalDate + '</td>' +
					'<td>' + flight.arrivalTime + '</td>' +
					'<td>' + formatDuration(flight.duration) + ' hours' + '</td>' +
					'</tr>');
			});
		},
		error: function(xhr, status, error) {
			console.error(error);
		}
	});
}

/*
* The function converts duration from format PT..H to hh:mm
*/
function formatDuration(duration) {
	const match = duration.match(/PT(\d+)H/);
	if (match && match[1]) {
		const hours = parseInt(match[1]);
		const formattedHours = ('0' + hours).slice(-2);
		return formattedHours + ':00';
	}
	return '';
}