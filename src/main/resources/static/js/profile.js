const id = document.getElementById("userIdDiv").innerText;
const uuid = document.getElementById("UUIDDiv").innerText;

const host = new URL(location)

const stompClient = new StompJs.Client({
  brokerURL: `ws://${host.host}/ws/general`
});

stompClient.onConnect = (frame) => {
  console.log('Connected: ' + frame);
  stompClient.subscribe(`/topic/general/${uuid}`, async (message) => {
    await handleMessage(message);
  });

};

stompClient.onWebSocketError = (error) => {
  console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
  console.error('Broker reported error: ' + frame.headers['message']);
  console.error('Additional details: ' + frame.body);
};


const handleMessage = async (message) => {
  const resp = JSON.parse(message.body)
  const data = resp["data"]

  console.log(resp)

  switch (resp["type"]) {
    case "COMMENT":
      await handleComment(data['post_id'])
      break;
  }
}
stompClient.activate()

// Search
async function searchFriend() {
  var inputElement = document.querySelector('.search input');
  var userInput = inputElement.value;

  var ignoreClickOnMeElement = document.querySelector(".search");

  document.addEventListener('click', function (event) {
    var isClickInsideElement = ignoreClickOnMeElement.contains(event.target);
    if (!isClickInsideElement) {
      document.getElementById("fetch-data-search").style.display = "none";
    } else {
      processChange();
      document.getElementById("fetch-data-search").style.display = "block";
    }
  });

  let data = new FormData
  data.append("contentSearch", userInput)

  const resp1 = await fetch("/searchUser",
      {
        method: "POST",
        body: data
      })
  if (resp1.ok) {
    let userList = await resp1.json();
    let strHTML = "";


    if (!userInput) {
      document.getElementById("fetch-data-search").innerHTML = "";
      document.getElementById("fetch-data-search").style.display = "none";
    } else {
      userList.forEach(item => {
        strHTML += `
      <li>
                <a class="dropdown-item" href="profile?id=${item.id}">
              
                <div class="user-search">
                  <img
                    src="${item.avatar}"
                    alt=""
                  >
                  <div class="name-search">
                    ${item.lastName} ${item.firstName}

                    <span class="location-search">${item.email}</span>
                  </div>
                  
                  <i class="fa-solid fa-xmark time-search"></i>
                </div>
                </a>
              </li>`;
      });
      console.log(userList);
      if (!strHTML) {
        strHTML = `<li>
<div class="alart alert-success text-ceter">Không có người dùng nào!</div>
</li>`;
      }
      document.getElementById("fetch-data-search").innerHTML = strHTML;
      document.getElementById("fetch-data-search").style.display = "block";
    }
  } else {
    console.error("Lỗi khi truy vấn danh sách người dùng");
  }
}


function debounce(func, timeout = 500) {
  let timer;
  return (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => {
      func.apply(this, args);
    }, timeout);
  };
}

const processChange = debounce(() => searchFriend());


// cha biet
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
  let heartIconMore = document.getElementById("likeMore"+postId);


  if (heartIconMore){
    heartIconMore.classList.toggle("red-heart");

    if (heartIconMore.classList.contains("fa-regular")) {
      heartIcon.classList.remove("fa-regular", "fa-heart");
      heartIcon.classList.add("fa-solid", "fa-heart","red-heart");

      heartIconMore.classList.remove("fa-regular", "fa-heart");
      heartIconMore.classList.add("fa-solid", "fa-heart","red-heart");
      likePost(postId).then(r => updateLikeUI(postId))
    } else {
      heartIcon.classList.remove("fa-solid", "fa-heart");
      heartIcon.classList.add("fa-regular", "fa-heart","red-heart");

      heartIconMore.classList.remove("fa-solid", "fa-heart");
      heartIconMore.classList.add("fa-regular", "fa-heart","red-heart");
      dislikePost(postId).then(r => updateLikeUI(postId))
    }
  }
  else{
    if (heartIcon) {
      heartIcon.classList.toggle("red-heart");

      if (heartIcon.classList.contains("fa-regular")) {
        heartIcon.classList.remove("fa-regular", "fa-heart");
        heartIcon.classList.add("fa-solid", "fa-heart","red-heart");
        likePost(postId).then(r => updateLikeUI(postId))
      } else {
        heartIcon.classList.remove("fa-solid", "fa-heart");
        heartIcon.classList.add("fa-regular", "fa-heart","red-heart");
        dislikePost(postId).then(r => updateLikeUI(postId))
      }
    }
  }

}

// post

function taoGiaoDienLike(idPost,slgLike){
  let htmlLike = document.getElementById("txtLike"+idPost)
  let htmlMoreLike = document.getElementById("txtLikeMore"+idPost)

  htmlLike.innerHTML = `${slgLike} LIKES`

  if (htmlMoreLike != null){
    htmlMoreLike = document.getElementById("txtLikeMore"+idPost)
    htmlMoreLike.innerHTML = `${slgLike} LIKES`
  }
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



const postComment = async (postId) => {
  let commentInput = document.getElementById('cmtm' + postId)


  let more = true
  if (commentInput == null){
    more = false
    commentInput= document.getElementById('cmt'+postId)
  }



  const commentText = commentInput.value.trim();
  // Lấy giá trị từ các trường input
  console.log(commentText)

  //  them comment
  if (commentText !== "") {
    const resp = await fetch(`/createComment?postId=${postId}&content=${commentText}`);
    let status = resp.status;
    const data1 = await resp.text();
  }
}


const handleComment = async (postId) => {
  let commentInput = document.getElementById('cmtm' + postId)


  let more = true
  if (commentInput == null){
    more = false
    commentInput= document.getElementById('cmt'+postId)
  }







  let avatar = document.getElementById("avatar").value
  let userId = document.getElementById("idUser").value
  let username = document.getElementById("username").value
  const d = new Date();
  let time = d.getDate();


  let comments = await getListComment(postId);
  console.log(comments)
  let htmlCmt = ``
  let lstComment = document.getElementById("lstComment"+postId)



  let tempIdSlgComment = "idSlgCommemt"+postId
  let slgComment = document.getElementById(tempIdSlgComment)

  let slgMaxCmt = 2
  if (more === true){
    tempIdSlgComment = "idSlgMoreComment"+postId
    let slgMoreComment = document.getElementById(tempIdSlgComment)
    slgMoreComment.innerHTML = comments.length+" Comments"

    let htmlMoreComment = ``
    for (let i = 0; i < comments.length; i++) {
      let cmt = comments[i];
      if (i < comments.length ) { // Check if the index is less than 2
        htmlMoreComment += `
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

    let lstMoreComment = document.getElementById("lstCommentMore"+postId)
    lstMoreComment.innerHTML = htmlMoreComment

  }


  slgComment.innerHTML = comments.length+" Comments"


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




async function testCmt(button) {
  let postId = button.getAttribute("data-post-id");
  await postComment(postId)
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
  let status = resp.status
  if (status === 201){
    location.reload()
  }
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
  // let inputValue = button.previousElementSibling.value;
  let inputValue = document.getElementById("inputWithIdUser").value
  let data = new FormData
  data.append("userToId",inputValue)
  console.log(inputValue)

  const resp = await fetch("/declineFriendRequest",
      {
        method: "POST",
        body: data
      })
  const data1 = await resp.text();
  console.log(data1)
  let status = resp.status
  if (status === 200){
    location.reload()
  }
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
           <div class="item" id="itemMore-${postInfo.id}" onclick="toggleLike(${postInfo.id})">
             <span class="like-icon">
                <i class="${postInfo.liked ? ' fa-solid' : 'fa-regular'} red-heart  fa-heart" id="likeMore${postInfo.id}"></i>
             </span>
             <span id="txtLikeMore${postInfo.id}">${postInfo.numLikes} LIKES</span>
           </div>
           <div class="item" onclick="toggleComments(1)">
             <span class="comment-icon"><i class="fa-regular fa-comment-dots"></i></span>
             <span id="idSlgMoreComment${postInfo.id}" class="comment-icon">${postInfo.lstComment.length} Comments</span> 
           </div>
         </div>
        <div class=" comments">
              <!-- Comments section, replace with your comments -->

                <div class="write">
                  <img src="${postInfo.image}" alt="" />
                  <input type="text" placeholder="write a comment" id="cmtm${postInfo.id}" />
                  <button onclick="testCmt(this,1)" data-post-id="${postInfo.id}">Send</button>
                </div>

                <div class="container-post" id="lstCommentMore${postInfo.id}">

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

