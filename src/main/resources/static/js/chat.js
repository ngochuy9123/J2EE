$(document).ready(function () {
  $("#action_menu_btn").click(function () {
    $(".action_menu").toggle();
  });
});
// Get the file input element
const fileInput = document.getElementById("fileInput");

// Get the div element to trigger the file input
const fileUploadButton = document.getElementById("fileUploadButton");

// Add a click event listener to the div
fileUploadButton.addEventListener("click", () => {
  // Trigger a click on the hidden file input when the div is clicked
  fileInput.click();
});

// Add an event listener to the file input to handle file selection
fileInput.addEventListener("change", () => {
  // Get the selected file(s)
  const selectedFiles = fileInput.files;

  if (selectedFiles.length > 0) {
    // You can now work with the selected file(s)
    console.log("Selected file:", selectedFiles[0].name);
    // Here, you can upload the file or perform any other desired actions.
  }
});

document.addEventListener("DOMContentLoaded", () => {
  // Your code here
  const listItems = document.querySelectorAll(".contacts li");

  listItems.forEach((item) => {
    item.addEventListener("click", () => {
      listItems.forEach((li) => li.classList.remove("active"));
      item.classList.add("active");
    });
  });
});
