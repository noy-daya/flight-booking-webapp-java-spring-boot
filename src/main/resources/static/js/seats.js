/*
=====================================================================================
The following file consists functions relating to seats reservation
=====================================================================================
*/

/*
* The Function adds "selected-seat" to buttons with initial text content.
*/  
function addSelectedSeatClass() {
    $('.seat').each(function() {
        // Check if the button has text content
        if ($(this).text().trim() !== "") {
            $(this).addClass('selected-seat-old');
        }
    });
}

/*
* The function fetch seat availability status and updates UI of seats.
*/   
function updateSeatAvailability() {
	// Make AJAX request to fetch seat availability for each flight
	$('.plane').each(function() {
		var flightId = $(this).data('flight-id');
		var planeElement = $(this);
		console.log("seats refresh!");
		var seatsAreNowTaken = false;
		$.get('/fetchSeatAvailability', { flightId: flightId }, function(response) {
			// Iterate over the response and update UI based on seat availability status
			response.forEach(function(seatAvailability) {
				var row = seatAvailability.row;
				var seat = seatAvailability.seat;
				var isAvailable = seatAvailability.available;
				// Update UI to reflect seat availability status
				var seatElement = planeElement.find('.seat[data-row="' + row + '"][data-seat="' + seat + '"]');
				if (isAvailable) {
					seatElement.removeClass('unavailable').addClass('available');
					seatElement.prop('disabled', false);
				}
				else if(seatElement.hasClass('selected-seat-old'))
				{
					seatElement.removeClass('unavailable').addClass('available');
					seatElement.prop('disabled', false);
				}
				else {
					seatElement.removeClass('available').addClass('unavailable');
					seatElement.prop('disabled', true);
					if (seatElement.hasClass('selected-seat')) {
						seatsAreNowTaken = true;
						seatElement.text("");
						seatElement.removeClass('selected-seat');
					}
				}
			});
			if (seatsAreNowTaken)
				$('#seatsNowTaken').show();
		});
	});
}

/*
* The function handles seat selection
*/ 
function handleSeatSelection()
{
	    var numOfPassengers = $('#numOfPassengers').val();
	    var numOfFlights = $('#numOfFlights').val();

        var selectedPassengers = {}; // Object to store selected passengers and their selected seats

        var initSelectedSeats = document.getElementById('selectedSeats').value;
        var seatsArray = initSelectedSeats.split(',');
            
		// Initialize selectedPassengers for each passenger and flight
		for (var pIndex = 0; pIndex < numOfPassengers; pIndex++) {
		    selectedPassengers[pIndex] = {}; // Initialize each passenger index with an empty object
		    for (var fIndex = 0; fIndex < numOfFlights; fIndex++) {
		        // Iterate through the seatsArray
		        var index = pIndex * numOfFlights + fIndex; // Calculate the index in the seatsArray
		        if (index < seatsArray.length) { // Check if the index is within the bounds of seatsArray
		            // Extract row and seat information from the seat string
		            var seatParts = seatsArray[index].split('-');
		            var row = parseInt(seatParts[0]); // Convert row to integer if needed
		            var seatNum = seatParts[1];
		            // Assign the seat information to the appropriate passenger and flight
		            selectedPassengers[pIndex][fIndex] = { row: row, seat: seatNum };
		        }
		    }
		}
		
        // Function to enable seat selection for a specific flight matrix
        function enableSeatSelection(flightIndex) {
            $('.seat[data-flight="' + flightIndex + '"]').addClass('active');
            $('.seat[data-flight!="' + flightIndex + '"]').removeClass('active').off('click');
        }

        // Function to disable seat selection for all flight matrices
        function disableAllSeatSelection() {
            $('.seat').removeClass('active').off('click');
        }
        
        // Loop through each seat and add margin to every second seat on page load
        $('.seat').each(function(index) {
            if ((index + 1) % 2 == 0) {
                $(this).css('margin-right', '30px');
            }
        });

        // Event handler for clicking on a passenger button
        $('.passenger-button').click(function() {
            // Remove the border class from all passenger buttons
            $('.passenger-button').removeClass('selected-passenger');

            // Add the border class to the clicked passenger button
            $(this).addClass('selected-passenger');

            // Get the selected passenger's index and flight index
            var indexes = $(this).data('index').split('-');
            var passengerIndex = indexes[0];
            var flightIndex = indexes[1];

            // Disable seat selection for all flight matrices
            disableAllSeatSelection();

            // Enable seat selection for the corresponding flight matrix
            enableSeatSelection(flightIndex);

            // Update seat click event handler with the current passenger and flight index
            $('.seat[data-flight="' + flightIndex + '"]').click(function() {
            	
            	$('#seatsNowTaken').hide();
            	
                var row = $(this).data('row');
                var seat = $(this).data('seat');
                var selectedSeat = row + "-" + seat;

                var $seatElement = $(this);
                
                // Check if the seat has already been selected by another passenger within the same flight matrix
                var isSeatTaken = Object.values(selectedPassengers).some(function(selections) {
                    return (selections[flightIndex] && (selections[flightIndex].row + "-" + selections[flightIndex].seat) == selectedSeat) ||
                    	!$seatElement.hasClass('available'); // Check if the seat is not available (taken by another passenger)
                });
                
                console.log(isSeatTaken);

                // If the seat is taken, prevent the selection and display a message
                if (isSeatTaken) {
                    return;
                }

                // Remove any previous selection for this passenger within the same flight matrix
                var previousSelection = selectedPassengers[passengerIndex];
                console.log(previousSelection);
                if (previousSelection && previousSelection[flightIndex]) {
                    // If there was a previous selection, update the text on the previous seat
                    var previousSeat = $('.seat[data-row="' + previousSelection[flightIndex].row + '"][data-seat="' + previousSelection[flightIndex].seat + '"][data-flight="' + flightIndex + '"]');
                    previousSeat.text("");
                    previousSeat.removeClass('selected-seat');
                    previousSeat.siblings('.seat-number').text(previousSelection[flightIndex].row + '-' + previousSelection[flightIndex].seat);
                }

                // Update the selection for the current passenger within the same flight matrix
                if (!selectedPassengers[passengerIndex]) {
                    selectedPassengers[passengerIndex] = {};
                }
                selectedPassengers[passengerIndex][flightIndex] = { row: row, seat: seat };

                // Append the passenger's name to the existing text on the selected seat
                var passengerName = $('.passenger-button[data-index="' + passengerIndex + '-' + flightIndex + '"] span').text();
             	// Set the text of .seat-number to the seat number
                $(this).text(passengerName);
                $(this).addClass('selected-seat');
                $(this).siblings('.seat-number').text(row + '-' + seat);
                
                // Update the hidden input with the selected seats for all passengers within the same flight matrix
                
                // selectedSeats string looks like this:
				// "{seat for passenger1 in flight1}, {seat for passenger1 in flight2}, {seat for passenger2 in flight1}, , {seat for passenger2 in flight2}
	
		        // Update the hidden input with the selected seats for all passengers within the same flight matrix
		        var selectedSeats = [];
		        // Iterate over each flight and passenger to ensure the desired order
		        for (var fIndex in selectedPassengers) {
		            for (var pIndex in selectedPassengers[fIndex]) {
		                var selection = selectedPassengers[fIndex][pIndex];
		                selectedSeats.push(selection.row + '-' + selection.seat);
		            }
		        }
		        $('#selectedSeats').val(selectedSeats.join(','));
            });
        });
}
