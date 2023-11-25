
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
      backgroundChanged = true
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
      likePost(postId).then(r => updateLikeUI(postId))
    } else {
      heartIcon.classList.remove("fa-solid", "fa-heart");
      heartIcon.classList.add("fa-regular", "fa-heart");
      dislikePost(postId).then(r => updateLikeUI(postId))
    }
  }

}

// post

function taoGiaoDienLike(idPost,slgLike){
  let htmlLike = document.getElementById("txtLikem"+idPost)
  if (htmlLike == null){
    htmlLike = document.getElementById("txtLike"+idPost)
  }
  htmlLike.innerHTML = `${slgLike} LIKES`

}




async function updateLikeUI(postId) {
  let post = await getPost(postId)
  taoGiaoDienLike(postId,post.numLikes)
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
  let commentInput = document.getElementById('cmtm'+postId)
  let more = true
  if (commentInput == null){
    more = false
    commentInput= document.getElementById('cmt'+postId)
  }



  const commentText = commentInput.value.trim();
  // Lấy giá trị từ các trường input
  console.log(commentText)
  // Dữ liệu cần gửi
  let data = {
    id: postId,
    content: commentText
  };

  const resp = await fetch(`/createComment?postId=${postId}&content=${commentText}`);
  let status = resp.status;
  const data1 = await resp.text();



  let avatar = document.getElementById("avatar").value
  let userId = document.getElementById("idUser").value
  let username = document.getElementById("username").value
  const d = new Date();
  let time = d.getDate();

  let comments = await getListComment(postId);
  console.log(comments)
  let htmlCmt = ``
  let lstComment = document.getElementById("lstComment"+postId)

  let slgMaxCmt = 2
  if (more === true){
    console.log("trong see more")
    slgMaxCmt = comments.length
  }
  for (let i = 0; i < comments.length; i++) {
    let cmt = comments[i];
    if (i < slgMaxCmt ) { // Check if the index is less than 2
        htmlCmt += `
        <div class="comment">
          <img src="${cmt.user_avatar}" alt="">
          <div class="info">
            <span>${cmt.user_name}</span>
            <p>${cmt.content}</p>
          </div>
          <span class="date"> ${formatTime(cmt.createAt)}</span>
        </div>
      `;
    }


  }

  if (comments.length>2){
    let htmlSeeMore = document.getElementById("seeMore"+postId)
    htmlSeeMore.innerHTML = `<a onclick="seeMorePost(${postId})" id="seeMore${postId}">See More</a>`
  }

  lstComment.innerHTML = htmlCmt

  commentInput.value = "";
}




async function getListComment(postId) {
  let data = new FormData();
  data.append("idPost", postId);

  const resp = await fetch("/getComment", {
    method: "POST",
    body: data,
  });

  return await resp.json();
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

// Notification
async function changeStatusNotification(userId) {

  let data = new FormData
  data.append("userId", userId)

  const resp = await fetch("/changeStatusAnnounce",
      {
        method: "POST",
        body: data
      })
  return await resp.text();


}

function formatTime(time){
  return  moment(time).fromNow();
}
// See More Post

async function getPost(id_post) {
  let data = new FormData
  data.append("idPost", id_post)

  const resp = await fetch("/getInfoPost",
      {
        method: "POST",
        body: data
      })
  return await resp.json();

}

async function seeMorePost(id_post) {
  let post = await getPost(id_post)
  showMoreComments(post);
  console.log(post.content)
}

async function showMoreComments(postInfo) {
console.log("post",postInfo)

  const modalTitle = document.getElementById("modalTitle");
  const modalContent = document.getElementById("modalContent");
  modalTitle.textContent = `This is ${postInfo.user.username} post`;
  modalContent.innerHTML = `
     <div class="post">
       <div class="container container-cmt">
         <div class="user">
           <div class="userInfo">
             <img src="${postInfo.user.avatar}" alt="User Profile Pic">
             <div class="details">
               <a href="/profile/" style="text-decoration: none; color: inherit;">
                 <span class="name">${postInfo.user.email}</span>
               </a>
               <span class="date">${postInfo.created_at}</span>
             </div>
           </div>
           <div class="more-icon" id="dropdownTrigger" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
             <span>...</span>
           </div>
           <ul class="dropdown-menu" aria-labelledby="dropdownTrigger">
             <li><a class="dropdown-item" href="#"><i class="fa-solid fa-trash"></i>Delete</a></li>
             <li><a class="dropdown-item" href="#"><i class="fa-solid fa-eye-slash"></i>Hide Post</a></li>
           </ul>
         </div>
         <div class="content content-cmt">
           <p>${postInfo.content}</p>
           
          ${postInfo.image !== "" ? `<img src="${postInfo.image}" alt="Post Image">` : ''}
  
         </div>
         <div class="info">
            <input type="hidden" value="${postInfo.id}" id="post${postInfo.id}">
           <div class="item" id="item-" onclick="toggleLike(${postInfo.id})">
             <span class="like-icon">
                <i class="${postInfo.liked ? ' fa-solid' : 'fa-regular'} red-heart  fa-heart" ></i>
             </span>
             <span id="txtLikem${postInfo.id}">${postInfo.numLikes} LIKES</span>
           </div>
           <div class="item" onclick="toggleComments(1)">
             <span class="comment-icon"><i class="fa-regular fa-comment-dots"></i></span>
             ${postInfo.lstComment.length} Comments
           </div>
         </div>
        <div class=" comments">
              <!-- Comments section, replace with your comments -->

                <div class="write">
                  <img src="${postInfo.image}" alt="" />
                  <input type="text" placeholder="write a comment" id="cmtm${postInfo.id}" />
                  <button onclick="testCmt(this,1)" data-post-id="${postInfo.id}">Send</button>
                </div>

                <div class="container-post" id="lstComment${postInfo.id}">

                 <div class="comments">
                 ${postInfo.lstComment.map(cmt => `
                <div class="comment">
                  <img src="${cmt.avatar}" alt="" />
                  <div class="info">
                    <span>${cmt.email}</span>
                    <p>${cmt.contentComment}</p>
                  </div>
                  <span class="date"> ${ formatTime(cmt.create_at) }</span>
                  
                </div>
                </br>
              `).join('')}
                 </div>
                </div>
            </div>
          </div>
        </div>
     </div>
   `;

  // Loop through the posts and append them to the container

  // Trigger the modal


  // console.log("comment",comment,currentUser,postId);
  // generateCommentsHTML(comment,currentUser,postId);

  const modal = document.getElementById("modalComment");
  const modalInstance = new bootstrap.Modal(modal);
  modalInstance.show();

}

function dislike(){
  console.log("dislike")
}
function like(){
  console.log("like")
}

