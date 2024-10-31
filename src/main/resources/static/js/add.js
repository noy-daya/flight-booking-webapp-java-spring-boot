/*
=====================================================================================
The following file consists event handler which prevents '#addForm' to be submitted,
and displays a modal content instead.
=====================================================================================
*/
$('#addForm').submit(function(event) {
    event.preventDefault(); // Prevent default form submission
    var form = $(this);
    $.ajax({
        type: form.attr('method'),
        url: form.attr('action'),
        data: form.serialize(), // Serialize form data
        success: function(response) {
            // Reload modal content with updated data
            $('#modal-body').html(response);
        },
        error: function(xhr, status, error) {
            // Handle errors
            console.error('Error occurred: ' + error);
        }
    });
});