/*
=====================================================================================
The following file consists functions relating to seats countDown, in booking process
=====================================================================================
*/

/*
* The function displays countdown starting from $('#seatsReserveTime') until 00:00
*/    
function startCountdown() {
	// Get the reservation time from the hidden input value
	var reservationTimeInput = $('#seatsReserveTime').val();
	if (reservationTimeInput !== "") {
		var reservationTime = new Date(reservationTimeInput);
		// Calculate the target time, which is 5 minutes from the reservation time
		var targetTime = new Date(reservationTime.getTime() + 5 * 60 * 1000); // Adding 5 minutes in milliseconds
		// Update the countdown every second
		var countdownElement = $('#countdown');
		var countdownInterval = setInterval(function() {
			// Calculate the remaining time in seconds
			var currentTime = new Date().getTime();
			var difference = Math.max(targetTime - currentTime, 0); // Ensure the difference is non-negative
			var minutes = Math.floor(difference / (60 * 1000));
			var seconds = Math.floor((difference % (60 * 1000)) / 1000);
			// Update the countdown display
			countdownElement.text(minutes + ':' + (seconds < 10 ? '0' : '') + seconds);
			// If time is up, stop the countdown
			if (difference <= 0) {
				clearInterval(countdownInterval);
				$('#submitBtn').prop('disabled', true); // do not allow to continue
			}
		}, 1000); // Update every second
	} else { // the input value can be null, in case the reserved seats were removed from db
		$('#countdown').text('0:00');
		$('#submitBtn').prop('disabled', true); // do not allow to continue
	}
}