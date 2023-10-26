document.addEventListener("DOMContentLoaded", function () {
  // Get a reference to the input element
  const openModalInput = document.getElementById("openModalInput");

  // Get a reference to the modal element
  const modal = new bootstrap.Modal(document.getElementById("exampleModal"));

  // Add a click event listener to the input element
  openModalInput.addEventListener("click", function () {
    // Trigger the modal when the input is clicked
    modal.show();
  });
});

// Upload file
// Function to trigger the file input when the "Image" icon is clicked
document.getElementById("upload-image").addEventListener("click", function () {
  document.getElementById("file-input").click();
});

// Function to handle the file upload
function handleFileUpload(input) {
  const selectedFile = input.files[0];
  if (selectedFile) {
    // You can perform further actions with the selected file here
    console.log("Selected file:", selectedFile.name);
  }
}

document.getElementById('edit_avatar').addEventListener('click', function() {
  var input = document.createElement('input');
  input.type = 'file';
  input.accept = 'image/*';
  input.onchange = function(event) {
    var file = event.target.files[0];
    var reader = new FileReader();

    reader.onload = function() {
      var imgElement = document.querySelector('.avt-img');
      imgElement.src = reader.result;
    };
    var fileName = file.name;
    console.log('Tên tệp tin đã chọn: ' + fileName);
    reader.readAsDataURL(file);
  };

  input.click();
});