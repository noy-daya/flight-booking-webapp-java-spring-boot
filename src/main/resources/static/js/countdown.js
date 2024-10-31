/*
=====================================================================================
The following file consist function related to Countdown until the next flight.
=====================================================================================
*/

/*
* The function is called from page userFlightCountdown.jsp
* - Displays a countDown until the start of departureDate date.
* - Considering local time
*/
function startCountdown() {
    const second = 1000,
        minute = second * 60,
        hour = minute * 60,
        day = hour * 24;

	// ========= retrieve inputs from page =========
    // departure date input element
    const departureDateInput = document.getElementById("departureDate");
    if (!departureDateInput) {
        console.error("Departure date input element not found.");
        return;
    }
    // departure date value
    const departureDateString = departureDateInput.value;
    if (!departureDateString) {
        console.error("Departure date value is null.");
        return;
    }
    // ========= calculation =========
    const departureDate = new Date(departureDateString); // convert to local time
	departureDate.setHours(0, 0, 0, 0); // set time to beginning of the day
	
    const countDown = departureDate.getTime(),
        x = setInterval(function() {    
            const now = new Date().getTime(), // get current local time
                distance = countDown - now;
			
            document.getElementById("days").innerText = Math.floor(distance / (day));
            document.getElementById("hours").innerText = Math.floor((distance % (day)) / (hour));
            document.getElementById("minutes").innerText = Math.floor((distance % (hour)) / (minute));
            document.getElementById("seconds").innerText = Math.floor((distance % (minute)) / second);

            // when the date is reached
            if (distance < 0) {
                document.getElementById("headline").innerText = "Your Upcoming flight is today!!!";
                document.getElementById("countdown").style.display = "none";
                document.getElementById("content").style.display = "block";
                clearInterval(x);
            }
        }, 0);
}