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
      var imgElement = document.querySelector('.cover-img');
      imgElement.src = reader.result;
      avatarChanged = true
    }

    reader.readAsDataURL(file);
  });
}



// let imgBackground = null
// document.getElementById('edit_background').addEventListener('click', function() {
//   backgroundChanged = true;
//
//   var input = document.createElement('input');
//   input.type = 'file';
//   input.accept = 'image/*';
//   input.onchange = function(event) {
//     var file = event.target.files[0];
//     imgBackground=file
//     var reader = new FileReader();
//     reader.onload = function() {
//       var imgElement = document.querySelector('.cover-img');
//       imgElement.src = reader.result;
//     };
//
//     reader.readAsDataURL(file);
//   };
//
//   input.click();
// });

//

document.getElementById('save_changes').addEventListener('click', async function () {
  if (backgroundChanged) {
    console.log('Người dùng đã thay đổi background');
    let background = backgroundInput.files[0]

    let data = new FormData
    data.append('image', background)
    // fetch
    const resp = await fetch("/editBackground",
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


// Friends
async function addFriend() {
  let userId = document.querySelector('input[name="idUserToHidden"]').value;
  let data = new FormData();
  data.append('userToId', userId);
  const resp = await fetch("/addFriend", {
    method: "POST",
    body: data
  });
  let http = resp.status
  let announce = await resp.text()
  console.log(announce)
}
async function acceptFriendRequest(button) {
  let inputValue = button.previousElementSibling.value;
  let data = new FormData
  data.append("userToId",inputValue)

  const resp = await fetch("/acceptFriendRequest",
      {
        method: "POST",
        body: data
      })
  let status = resp.status

  const data1 = await resp.text();
  console.log(data1)
  if (status === 200){
    location.reload()
  }
}
async function declineFriendRequest(button) {
  let hiddenInput = button.parentElement.querySelector('.accept-input');
  let inputValue = hiddenInput.value;
  let data = new FormData
  data.append("userToId",inputValue)

  const resp = await fetch("/declineFriendRequest",
      {
        method: "POST",
        body: data
      })
  const data1 = await resp.text();
  console.log(data1)

}