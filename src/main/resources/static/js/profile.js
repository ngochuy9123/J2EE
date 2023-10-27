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


var avatarChanged = false;
var backgroundChanged = false;


var avatarInput = document.getElementById('edit_avatar');


function chooseAvatarImg(){
  avatarInput.addEventListener('change', function(event) {
    var file = event.target.files[0];
    var reader = new FileReader();

    reader.onload = function() {
      var imgElement = document.querySelector('.avt-img');
      imgElement.src = reader.result;
      avatarChanged = true
    }

    reader.readAsDataURL(file);
  });
}

var backgroundInput = document.getElementById('edit_background');


function chooseBackgroundImg(){
  backgroundInput.addEventListener('change', function(event) {
    var file = event.target.files[0];
    var reader = new FileReader();

    reader.onload = function() {
      var imgElement = document.querySelector('.avt-img');
      imgElement.src = reader.result;
      avatarChanged = true
    }

    reader.readAsDataURL(file);
  });
}



let imgBackground = null
document.getElementById('edit_background').addEventListener('click', function() {
  backgroundChanged = true;

  var input = document.createElement('input');
  input.type = 'file';
  input.accept = 'image/*';
  input.onchange = function(event) {
    var file = event.target.files[0];
    imgBackground=file
    var reader = new FileReader();
    reader.onload = function() {
      var imgElement = document.querySelector('.cover-img');
      imgElement.src = reader.result;
    };

    reader.readAsDataURL(file);
  };

  input.click();
});


document.getElementById('save_changes').addEventListener('click', async function () {
  if (backgroundChanged) {
    console.log('Người dùng đã thay đổi background');
    let avatar = avatarInput.files[0]

    let data = new FormData
    data.append('image', avatar)
    // fetch
    const resp = await fetch("/editAvatar",
        {
          method: "POST",
          body: data
        })
    const data1 = await resp.text();
    console.log(data1)

    avatarChanged = false


    backgroundChanged = false
  }
  if (avatarChanged) {
    console.log('Người dùng đã thay đổi avatar');
    let avatar = avatarInput.files[0]

    let data = new FormData
    data.append('image', avatar)
    // fetch
    const resp = await fetch("/editAvatar",
        {
          method: "POST",
          body: data
        })
    const data1 = await resp.text();
    console.log(data1)

    avatarChanged = false

  }
  location.reload()
});