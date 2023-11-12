function generatePostHTML(post) {
  return `
    <div class="post">
      <div class="container">
        <div class="user">
          <div class="userInfo">
            <img src="${post.profilePic}" alt="User Profile Pic">
            <div class="details">
              <a href="/profile/${post.userId}" style="text-decoration: none; color: inherit;">
                <span class="name">${post.name}</span>
              </a>
              <span class="date">1 min ago</span>
            </div>
          </div>
          <div class="more-icon" id="dropdownTrigger" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          <span>...</span>
      </div>
      <ul class="dropdown-menu" aria-labelledby="dropdownTrigger">
          <li><a class="dropdown-item" href="#"><i class="fa-solid fa-trash"></i></i>Delete</a></li>
          <li><a class="dropdown-item" href="#"><i class="fa-solid fa-eye-slash"></i>Hide Post</a></li>
        
      </ul>
        </div>
        <div class="content">
          <p>${post.desc}</p>
          <img src="${post.img}" alt="Post Image">
        </div>
        <div class="info">
          <div class="item" id="item-${post.id}" onclick="toggleLike(${post.id})">
<span class="like-icon"><i class="fa-regular fa-heart"></i></span>
30 Likes
</div>
          <div class="item" onclick="toggleComments(${post.id})">
            <span class="comment-icon"><i class="fa-regular fa-comment-dots"></i></span>
            12 Comments
          </div>
          <div class="item">
            <span class="share-icon"><i class="fa-solid fa-share"></i></span>
            Share
          </div>
        </div>
        <div class=" comments" id="comments-container-${post.id}" >
          <!-- Comments section, replace with your comments -->
        </div>
      </div>
    </div>
  `;
}
const posts = [];
const comments = [
];
function generateCommentsHTML(comments, currentUser, postId) {
  let commentsHTML = `
      <div class="comments" id="comments-container-${postId}">
        <div class="write">
          <img src="${currentUser.profilePic}" alt="" />
          <input type="text" placeholder="write a comment" id="comment-input-${postId}" />
          <button onclick="addComment(${postId})">Send</button>
        </div>
    `;

  commentsHTML += comments
    .map(
      (comment) => `
          <div class="comment">
            <img src="${comment.profilePicture}" alt="" />
            <div class="info">
              <span>${comment.name}</span>
              <p>${comment.desc}</p>
            </div>
            <span class="date">1 hour ago</span>
          </div>
        `
    )
    .join("");

  commentsHTML += `</div>`;
  return commentsHTML;
}

const currentUser = {
  profilePic:
    "https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
};

// const commentsContainer = document.querySelectorAll("#comments-container");
// Get the container where you want to include the posts
const postContainer = document.getElementById("post-container");
// Loop through the posts and append them to the container
posts.forEach((post) => {
  const postHTML = generatePostHTML(post);
  postContainer.innerHTML += postHTML;
  const commentsContainer = document.getElementById(
    `comments-container-${post.id}`
  );
  console.log(commentsContainer);
  if (commentsContainer) {
    const commentsHTML = generateCommentsHTML(comments, currentUser, post.id);
    commentsContainer.innerHTML = commentsHTML;
  }
});
// for (const element of commentsContainer) {
//   console.log(element);
//   element.innerHTML = generateCommentsHTML(comments, currentUser);
// }
// commentsContainer.innerHTML = generateCommentsHTML(comments, currentUser);
function toggleComments(postId) {
  console.log(postId);

  // Select the comments container based on postId
  const commentsContainer = document.getElementById(
    `comments-container-${postId}`
  );

  if (commentsContainer) {
    // Toggle the "hidden" class on the selected comments container
    commentsContainer.classList.toggle("hidden");
  }
}
function addComment(postId) {
  const commentInput = document.getElementById(`comment-input-${postId}`);
  const commentText = commentInput.value.trim();

  if (commentText !== "") {
    const newComment = {
      id: comments.length + 1,
      desc: commentText,
      name: "Your Name",
      userId: comments.length + 1,
      profilePicture:
        "https://images.pexels.com/photos/1036623/pexels-photo-1036623.jpeg?auto=compress&cs=tinysrgb&w=1600",
    };

    comments.push(newComment);
    const commentsContainer = document.getElementById(
      `comments-container-${postId}`
    );
    console.log(commentsContainer);
    commentsContainer.innerHTML = generateCommentsHTML(
      comments,
      currentUser,
      postId
    );

    // Clear the comment input field
    commentInput.value = "";
  }
}




function toggleLike(postId) {
  // Find the heart icon element based on postId
  const heartIcon = document.querySelector(`#item-${postId} .like-icon i`);

  if (heartIcon) {
    heartIcon.classList.toggle("red-heart");

    if (heartIcon.classList.contains("fa-regular")) {
      heartIcon.classList.remove("fa-regular", "fa-heart");
      heartIcon.classList.add("fa-solid", "fa-heart");
      likePost(postId).then(r => console.log("Hello"))
    } else {
      heartIcon.classList.remove("fa-solid", "fa-heart");
      heartIcon.classList.add("fa-regular", "fa-heart");
      dislikePost(postId)
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

// ==================HOME ================

// Array of story objects
const stories = [
  {
    id: 1,
    name: "John Doe",
    img: "https://images.pexels.com/photos/13916254/pexels-photo-13916254.jpeg?auto=compress&cs=tinysrgb&w=1600&lazy=load",
  },
  {
    id: 2,
    name: "John Doe",
    img: "https://images.pexels.com/photos/13916254/pexels-photo-13916254.jpeg?auto=compress&cs=tinysrgb&w=1600&lazy=load",
  },
  {
    id: 3,
    name: "John Doe",
    img: "https://images.pexels.com/photos/13916254/pexels-photo-13916254.jpeg?auto=compress&cs=tinysrgb&w=1600&lazy=load",
  },
  {
    id: 4,
    name: "John Doe",
    img: "https://images.pexels.com/photos/13916254/pexels-photo-13916254.jpeg?auto=compress&cs=tinysrgb&w=1600&lazy=load",
  },
  // Add more story objects here
];
const storiesContainer = document.getElementById("stories-container");
stories.forEach((story) => {
  const storyElement = document.createElement("div");
  storyElement.classList.add("story");

  const imgElement = document.createElement("img");
  imgElement.src = story.img;
  imgElement.alt = story.name;

  const spanElement = document.createElement("span");
  spanElement.textContent = story.name;

  storyElement.appendChild(imgElement);
  storyElement.appendChild(spanElement);

  storiesContainer.appendChild(storyElement);
});
// ==================UP POST================
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
  const files = input.files;
  const imageContainer = document.getElementById('image-container');

  for (let i = 0; i < files.length; i++) {
    const selectedFile = files[i];
    if (selectedFile) {
      const reader = new FileReader();

      reader.onload = function(e) {
        const image = new Image();
        image.onload = function() {
          // Chỉnh kích thước của hình ảnh thành 50x50 pixels
          image.width = 120;
          image.height = 150;
          imageContainer.appendChild(image);
        }
        image.src = e.target.result;
      }

      reader.readAsDataURL(selectedFile);
    }
  }
}

async function testCmt(button) {


  var postId = button.getAttribute("data-post-id");
  const commentInput = document.getElementById('cmt'+postId);
  const commentText = commentInput.value.trim();
  // Lấy giá trị từ các trường input

  // Dữ liệu cần gửi
  var data = {
    id: postId,
    content: commentText
  };

  const resp = await fetch(`/createComment?postId=${postId}&content=${commentText}`);

  let status = resp.status;


  const data1 = await resp.text();

  let post_container = document.getElementById("fetch-data-comment-"+postId);
  await fetDataComment(post_container);

  commentInput.value = "";

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
  console.log(data1);

}



function createUserList(userList) {
  var ulElement = document.createElement('ul');
  ulElement.classList.add('dropdown-menu', 'dropdown-menu-end', 'drop-search');

  var recentlyElement = document.createElement('div');
  recentlyElement.classList.add('recently');
  recentlyElement.textContent = 'Recently';

  ulElement.appendChild(recentlyElement);

  userList.forEach(user => {
    var liElement = document.createElement('li');
    var aElement = document.createElement('a');
    aElement.classList.add('dropdown-item');
    aElement.href = '#';

    var userSearchElement = document.createElement('div');
    userSearchElement.classList.add('user-search');

    var imgElement = document.createElement('img');
    imgElement.src = user.avatar;
    imgElement.alt = '';

    var nameSearchElement = document.createElement('div');
    nameSearchElement.classList.add('name-search');
    nameSearchElement.textContent = user.background;

    var emailSearchElement = document.createElement('div');
    emailSearchElement.classList.add('email-search');
    emailSearchElement.textContent = user.email;

    var phoneSearchElement = document.createElement('div');
    phoneSearchElement.classList.add('phone-search');
    phoneSearchElement.textContent = user.phone;

    var timeSearchElement = document.createElement('i');
    timeSearchElement.classList.add('fa-solid', 'fa-xmark', 'time-search');

    nameSearchElement.appendChild(emailSearchElement);
    userSearchElement.appendChild(imgElement);
    userSearchElement.appendChild(nameSearchElement);
    userSearchElement.appendChild(phoneSearchElement);
    userSearchElement.appendChild(timeSearchElement);

    aElement.appendChild(userSearchElement);
    liElement.appendChild(aElement);
    ulElement.appendChild(liElement);
  });

  var dropdownTriggerElement = document.getElementById('dropdownTrigger');
  dropdownTriggerElement.parentNode.replaceChild(ulElement, dropdownTriggerElement);
}

async function searchFriend(){
  var inputElement = document.querySelector('.search input');
  var userInput = inputElement.value;

  var ignoreClickOnMeElement = document.querySelector(".search");

  document.addEventListener('click', function(event) {
    var isClickInsideElement = ignoreClickOnMeElement.contains(event.target);
    if (!isClickInsideElement) {
      document.getElementById("fetch-data-search").style.display = "none";
    }else {
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


    if(!userInput){
      document.getElementById("fetch-data-search").innerHTML = "";
      document.getElementById("fetch-data-search").style.display = "none";
    }else{
      userList.forEach(item=>{
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
      if(!strHTML){
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



function debounce(func, timeout = 500){
  let timer;
  return (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => { func.apply(this, args); }, timeout);
  };
}
const processChange = debounce(() => searchFriend());
// const processChange = debounce(() => console.log(123));


async function fetchDataCommentPost(){
  const containerPosts = document.querySelectorAll(".container-post");
  await containerPosts.forEach(async item => await fetDataComment(item));
}

async function fetDataComment(item){
  let id = item.id;
  let post_id = id.split("-")[3];

  let container = item.parentElement.parentElement;

  let info = container.querySelector(".info");




  let data = new FormData();
  data.append("post_id", post_id);

  const resp1 = await fetch("/searchComment",
      {
        method: "POST",
        body: data
      })
  if (resp1.ok) {
    let str_comment = "";
    let comments = await resp1.json();
    console.log(comments);
    if(comments.length > 0){
      comments.forEach(item=>{
        str_comment += `
          <div class="comment">
          <img src="${item.user_avatar}" alt="" />
          <div class="info">
            <span>${item.user_name}</span>
            <p>${item.content}</p>
          </div>
          <span class="date">1 hour ago</span>
        </div>
          `;
      });
      item.innerHTML = str_comment;
    }

    info.innerHTML = `
  <div class="item" id="item-${post_id}" onclick="toggleLike(${post_id})">
    <span class="like-icon"><i class="fa-regular fa-heart"></i></span>
    40 Likes
  </div>
  <div class="item" onclick="toggleComments(${post_id})">
    <span class="comment-icon"><i class="fa-regular fa-comment-dots"></i></span>
    ${comments.length} Comments
  </div>
  <div class="item">
    <span class="share-icon"><i class="fa-solid fa-share"></i></span>
    Share
  </div>
  `;
  }
}

fetchDataCommentPost();



