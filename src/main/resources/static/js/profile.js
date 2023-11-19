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



// Function to handle the file upload
function handleFileUpload(input) {
  const selectedFile = input.files[0];
  if (selectedFile) {
    // You can perform further actions with the selected file here
    console.log("Selected file:", selectedFile.name);
  }
}


let avatarChanged = false;
let backgroundChanged = false;


let avatarInput = document.getElementById('edit_avatar');

function chooseAvatarImg(){
  avatarInput.addEventListener('change', function(event) {
    let file = event.target.files[0];
    let reader = new FileReader();

    reader.onload = function() {
      let imgElement = document.querySelector('.avt-img');
      imgElement.src = reader.result;
      avatarChanged = true
    }

    reader.readAsDataURL(file);
  });
}

let backgroundInput = document.getElementById('edit_background');

function chooseBackgroundImg(){
  backgroundInput.addEventListener('change', function(event) {
    let file = event.target.files[0];
    let reader = new FileReader();

    reader.onload = function() {
      let imgElement = document.querySelector('.cover-img');
      imgElement.src = reader.result;
      avatarChanged = true
    }

    reader.readAsDataURL(file);
  });
}



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

  let data = new FormData
  data.append("location",document.getElementsByName("location")[0].value)
  data.append("github",document.getElementsByName("github")[0].value)
  data.append("twitter",document.getElementsByName("twitter")[0].value)
  data.append("instagram",document.getElementsByName("instagram")[0].value)

  let resp = await fetch("/editInfoUser",
      {
        method: "POST",
        body: data
      })

  if (resp.status === 200){
    alert("thay doi thanh cong")
  }

  location.reload()
});

function toggleLike(postId) {

  // Find the heart icon element based on postId
  const heartIcon = document.getElementById("like"+postId);

  if (heartIcon) {
    heartIcon.classList.toggle("red-heart");

    if (heartIcon.classList.contains("fa-regular")) {
      heartIcon.classList.remove("fa-regular", "fa-heart");
      heartIcon.classList.add("fa-solid", "fa-heart");
      likePost(postId).then(r => console.log("Hello"))
    } else {
      heartIcon.classList.remove("fa-solid", "fa-heart");
      heartIcon.classList.add("fa-regular", "fa-heart");
      dislikePost(postId).then(r => console.log("Dislike"))
    }
  }
}


async function likePost(postId) {
  let data = new FormData
  data.append("idPost", postId)
  const resp = await fetch("/likePost",
      {
        method: "POST",
        body: data
      })
  let status = resp.status

  const data1 = await resp.text();
  console.log(data1)
}

async function dislikePost(postId) {
  let data = new FormData
  data.append("idPost", postId)
  const resp = await fetch("/dislikePost",
      {
        method: "POST",
        body: data
      })
  let status = resp.status

  const data1 = await resp.text();
  console.log(data1)
}


async function testCmt(button) {
  let postId = button.getAttribute("data-post-id");
  const commentInput = document.getElementById('cmt'+postId);
  const commentText = commentInput.value.trim();
  // Lấy giá trị từ các trường input

  // Dữ liệu cần gửi
  let data = {
    id: postId,
    content: commentText
  };

  const resp = await fetch(`/createComment?postId=${postId}&content=${commentText}`);
  let status = resp.status;
  const data1 = await resp.text();

  let post_container = document.getElementById("fetch-data-comment-"+postId);

  let avatar = document.getElementById("avatar").value
  let userId = document.getElementById("idUser").value
  let username = document.getElementById("username").value
  const d = new Date();
  let time = d.getDate();

  let lstComment = document.getElementById("lstComment"+postId)
  let htmlCmt = `
    <div class="comment" id="'lstComment' + ${postId}">
      <img src="${avatar}" alt="">
      <div class="info">
        <span >${username}</span>
        <p >${commentText}</p>
      </div>
      <span class="date" >${time}</span>
    </div>
  `

  lstComment.innerHTML = htmlCmt

  commentInput.value = "";
}



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
  let inputValue = button.previousElementSibling.value;
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


