/*
=====================================================================================
The following file consists functions which handle file uploading,
and the elements related to it.
=====================================================================================
*/

/*
* The function displays file name into element $('#fileName')
*/
function updateFileName() {
	var fileName = $('#fileName');
	var input = $('#fileInput')[0];
	if (input.files.length > 0) {
		fileName.text(input.files[0].name);
	} else {
		fileName.text('No file chosen');
	}
}

/*
* The function displays preview image into element $('#imagePreview')
*/
function previewImage() {
	var fileInput = $('#fileInput')[0];
	var imagePreview = $('#imagePreview');

	// Clear previous preview
	imagePreview.empty();

	if (fileInput.files && fileInput.files[0]) {
		var reader = new FileReader();

		reader.onload = function(e) {
			var img = $('<img>').attr('src', e.target.result).addClass('preview-img');
			imagePreview.html(img); // replace existing content with the new image
		};

		reader.readAsDataURL(fileInput.files[0]);
	}
}

/*
* The function clears elements related to the current file uploaded
*/
function clearFile() {
	var input = $('#fileInput');
	var fileName = $('#fileName');
	var imagePreview = $('#imagePreview');

	input.val(''); // clear the file input without opening file selection dialog
	fileName.text('No file chosen'); // update the file name display
	imagePreview.empty(); // cear image preview

	toggleClearButton();
}

/*
* The function toggles clear button
*/
function toggleClearButton() {
	var input = $('#fileInput')[0];
	var clearButton = $('.clear-btn');
	if (input.files.length > 0) {
		clearButton.show();
	} else {
		clearButton.hide();
	}
}

/*
* The function validates file size
* - if size exceeds 700KB -> display an error message
*/
function validateFileSize() {
    var fileSize = $('#fileInput')[0].files[0].size; // Accessing through DOM element
    var maxSize = 700 * 1024; // Maximum file size in bytes (700 KB)
	var sizeErrorMessage = $('#sizeErrorMessage');
	
    if (fileSize > maxSize)
    {
		clearFile();
      	sizeErrorMessage.text('File size exceeds 700KB. Please select a different file.');
        sizeErrorMessage.show();
    }
    else
        sizeErrorMessage.hide();
  }