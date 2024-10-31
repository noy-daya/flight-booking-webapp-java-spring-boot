/*
=====================================================================================
The following file consists function which dynamically
populates a dropdown list (#arrivalAirportID) with arrival airports based on
the selected departure airport (#departureAirportID).
(using an AJAX request to fetch the relevant data from the server)
=====================================================================================
*/
function fetchArrivalAirports() {
    $('#arrivalAirportID').attr('disabled', 'disabled');
    $('#departureAirportID').change(function() {
        var departureAirportID = $(this).val();
        
        // Make an AJAX request to fetch arrival airports based
        // on the selected departure airport
        $.ajax({
            url: '/airport/FetchArrivalAirports',
            type: 'GET',
            data: {
                departureAirportID: departureAirportID
            },
            dataType: 'json',
            success: function(data) {

                $('#arrivalAirportID').empty();
                $('#arrivalAirportID').append('<option selected value="0">Select</option>');
                $('#arrivalAirportID').selectpicker('refresh');

                if (departureAirportID == 0) {
                    $('#arrivalAirportID').attr('disabled', 'disabled');
                } else {
                    $('#arrivalAirportID').removeAttr('disabled');
                }

                // Add new options
                $.each(data, function(index, option) {
                    $('#arrivalAirportID').append(
                        '<option value="' + option.id + '" data-subtext="' + option.city.name + ', ' + option.city.country.name + '">' +
                        option.code + '</option>'
                    );
                });
                $('#arrivalAirportID').selectpicker('refresh');
            },
            error: function(xhr, status, error) {
                console.error(error);
            }
        });
    }).change();
}