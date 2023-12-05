function formatTime(time){
    return  moment(time).fromNow();
}

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
        case "LIKE":
            await handleLike(data['idPost'])
            break;
        case "NOTIFICATION":
            await handleNotification(data["postId"])
            break;
        case "FRIENDS_REQUEST":
            await handleFriendSocket(data["postId"])
            break;
    }
}
stompClient.activate()


const handleComment = async (postId) => {
    await fetchAllCommentData(postId)
}

const handleLike = async (postId) => {
//     // let container = item.parentElement.parentElement;
    let post_container = document.getElementById("fetch-data-comment-" + postId);
    let post_container_modal = document.getElementById("fetch-comment-modal-" + postId);
    if (post_container != null) {
        await updateLike(post_container.parentElement.parentElement, postId);
    }
    if (post_container_modal != null) {
        await updateLike(post_container_modal.parentElement.parentElement, postId);

    }
}


async function showMoreComments(postId) {
    console.log("Clicked on 'More comments' for post with ID:", postId);

    let postInfo = await getInfoPost(postId)
    let userInfo = await getInfoUser(postInfo.user.id);
    let userInfoLogin = await getInfoUserLogin();

    const modalTitle = document.getElementById("modalTitle");
    const modalContent = document.getElementById("modalContent");

    // DUyet tat ca cac comment
    let data = new FormData();
    let str_comment = "";
    data.append("post_id", postId);

    const resp1 = await fetch("/searchComment",
        {
            method: "POST",
            body: data
        })
    if (resp1.ok) {
        let comments = await resp1.json();
        console.log(comments);
        if (comments.length > 0) {
            comments.forEach(item => {
                str_comment += `
          <div class="comment">
          <img src="${item.user_avatar}" alt="" />
          <div class="info">
            <span>${item.user_name}</span>
            <p>${item.content}</p>
          </div>
          <span class="date">${formatTime(item.createdAt)}</span>
        </div>
          `;
            });
        }
        modalTitle.textContent = `This is ${userInfo.lastName}'s post`;
        modalContent.innerHTML = `
     <div class="post">
       <div class="container container-cmt">
         <div class="user">
           <div class="userInfo">
             <img src="${postInfo.user.avatar}" alt="User Profile Pic">
             <div class="details">
               <a href="/profile/" style="text-decoration: none; color: inherit;">
                 <span class="name">${userInfo.username}</span>
               </a>
               <span class="date">${postInfo.createAtFormat}</span>
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
            <div class="item">
                <span class="like-icon"><i class="fa-regular fa-heart"></i></span>
                <span>1 like</span>
            </div>
            <!--                    So luot binh luan-->
            <div class="item" >
                <span class="comment-icon"><i class="fa-regular fa-comment-dots"></i></span>
                12 Comments
            </div>
        </div>
        <div class=" comments comments-modal" >
              <!-- Comments section, replace with your comments -->

                <div class="write">
                    <a href="/profile"><img src="${userInfo.avatar}" alt=""></a>
                    <input type="text" spellcheck="false" placeholder="write a comment" id="cmt-modal-${postId}">
                    <button onclick="sendCommentInModal(this)" data-post-id="${postId}">Send</button>
                </div>

                <div class="container-post-modal" id="fetch-comment-modal-${postId}">
                 <div class="comments">
                 ${str_comment}
                 </div>
                </div>
            </div>
          </div>
        </div>
     </div>
   `;


        const modal = document.getElementById("modalComment");
        const modalInstance = new bootstrap.Modal(modal);
        modalInstance.show();

        let containerPost = document.querySelector(".container-post-modal");
        await fetDataCommentModal(containerPost);

    }
}

async function fetDataCommentModal(item){
    let id = item.id;
    let post_id = id.split("-")[3];





    let data = new FormData();
    data.append("post_id", post_id);

    const resp1 = await fetch("/searchComment",
        {
            method: "POST",
            body: data
        })
    if (!resp1.ok) {
        return;
    }

    let str_comment = "";
    let comments = await resp1.json();
    if (comments.length > 0) {
        comments.forEach(item => {
            str_comment += `
              <div class="comment">
              <a href="profile?id=${item.user_id}"><img src="${item.user_avatar}" alt="" /></a>
              <div class="info">
                <a href="profile?id=${item.user_id}"><span>${item.user_name}</span></a>
                <p>${item.content}</p>
              </div>
              <span class="date">${formatTime(item.createdAt)}</span>
            </div>
              `;
        });
        item.innerHTML = str_comment;
    }

    await updateLike(item.parentElement.parentElement, post_id)

}

async function sendCommentInModal(button) {
    let postId = button.getAttribute("data-post-id");
    const commentInput = document.getElementById('cmt-modal-' + postId);
    const commentText = commentInput.value.trim();
    // Lấy giá trị từ các trường input

    if (commentText.length === 0) {
        return
    }

    // Dữ liệu cần gửi
    var data = {
        id: postId,
        content: commentText
    };

    const resp = await fetch(`/createComment?postId=${postId}&content=${commentText}`);

    let status = resp.status;


    const data1 = await resp.text();


    // await fetchAllCommentData(postId)

    commentInput.value = "";

}

const fetchAllCommentData = async (postId) => {
    let post_container = document.getElementById("fetch-data-comment-" + postId);
    let post_container_modal = document.getElementById("fetch-comment-modal-" + postId);
    if (post_container != null) {
        await fetDataComment(post_container);
    }
    if (post_container_modal != null) {
        await fetDataCommentModal(post_container_modal);

    }
}

async function getInfoUserLogin() {
    let data = new FormData

    const resp = await fetch("/getInfoUserLogin",
        {
            method: "POST",
            body: data
        })
    return await resp.json()
}

async function getInfoUser(user_id) {
    let data = new FormData
    data.append("user_id", user_id)

    const resp = await fetch("/findUserById",
        {
            method: "POST",
            body: data
        })
    return await resp.json()
}

async function getInfoPost(postId) {
    let data = new FormData
    data.append("idPost", postId)
    const resp = await fetch("/getInfoPost",
        {
            method: "POST",
            body: data
        })
    let status = resp.status

    return await resp.json()
}


const posts = [];
const comments = [];

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


async function toggleLike(postId) {
    // Find the heart icon element based on postId
    const heartIcon = document.querySelector(`#item-${postId} .like-icon i`);
    let slgLike = document.getElementById(`slgLike+${postId}`)
    if (heartIcon) {
        heartIcon.classList.toggle("red-heart");

        // Like
        if (heartIcon.classList.contains("fa-regular")) {
            heartIcon.classList.remove("fa-regular");
            heartIcon.classList.add("fa-solid",);
            slgLike.textContent = (Number.parseInt(slgLike.textContent.replace("Likes", "")) + 1) + " Likes";
            await likePost(postId);
        }
        // Huy like
        else {
            heartIcon.classList.remove("fa-solid");
            heartIcon.classList.add("fa-regular");
            slgLike.textContent = (Number.parseInt(slgLike.textContent.replace("Likes", "")) - 1) + " Likes";
            await dislikePost(postId)
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

            reader.onload = function (e) {
                const image = new Image();
                image.onload = function () {
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
    const commentInput = document.getElementById('cmt' + postId);
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

    let post_container = document.getElementById("fetch-data-comment-" + postId);
    await fetDataComment(post_container);

    commentInput.value = "";

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



function debounce(func, timeout = 500) {
    let timer;
    return (...args) => {
        clearTimeout(timer);
        timer = setTimeout(() => {
            func.apply(this, args);
        }, timeout);
    };
}


// const processChange = debounce(() => console.log(123));


async function fetchDataCommentPost() {
    const containerPosts = document.querySelectorAll(".container-post");
    await containerPosts.forEach(async item => await fetDataComment(item));
}

const updateLike = async (container, post_id) => {
    let data = new FormData();
    data.append("post_id", post_id);

    // fetch like
    let info = container.querySelectorAll(".item")[0];

    const respLike = await fetch("/countLikeIdPost",
        {
            method: "POST",
            body: data
        })

    let slgLike = await respLike.text();

    const respLiked = await fetch("/postLiked",
        {
            method: "POST",
            body: data
        })

    let liked = await respLiked.json();
    console.log(liked)

    let htmlHeart = ""
    if (liked) {
        htmlHeart = `<span class="like-icon"><i class="fa-solid fa-heart red-heart"></i></span>`
    } else {
        htmlHeart = `<span class="like-icon"><i class="fa-regular fa-heart"></i></span>`
    }

    info.innerHTML = `
          ` + htmlHeart + `
          <span id="slgLike+${post_id}">${slgLike} Likes</span>

        `;

    info.setAttribute("onclick", `toggleLike(${post_id})`)
    info.setAttribute("id", `item-${post_id}`)
}

const updateTotalComments = async (container, post_id, totalComments) => {
    let data = new FormData();
    data.append("post_id", post_id);

    // fetch like
    let info = container.querySelectorAll(".item")[1];


    info.innerHTML = `
            <span class="comment-icon"><i class="fa-regular fa-comment-dots"></i></span>
            ${totalComments} Comments
        `;

    info.setAttribute("onclick", `toggleComments(${post_id})`)
    // info.setAttribute("id", `item-${post_id}`)
    // <div class="item" id="item-${post_id}" onclick="toggleLike(${post_id})">
//
// </div>
//     <div class="item" onclick="toggleComments(${post_id})">

//     </div>
}

// const updateLikeModal = async (container, post_id) => {
//
//     let data = new FormData();
//     data.append("post_id", post_id);
//
//     // Get container
//     // let container = item.parentElement.parentElement;
//     let info = container.querySelector(".info");
//
//     const respLike = await fetch("/countLikeIdPost",
//         {
//             method: "POST",
//             body: data
//         })
//     let slgLike = await respLike.text();
//     const respLiked = await fetch("/postLiked",
//         {
//             method: "POST",
//             body: data
//         })
//     let liked = await respLiked.json();
//     console.log(liked)
//     let htmlHeart = ""
//     if (liked) {
//         htmlHeart = `<span class="like-icon"><i class="fa-solid fa-heart red-heart"></i></span>`
//     } else {
//         htmlHeart = `<span class="like-icon"><i class="fa-regular fa-heart"></i></span>`
//     }
//     info.innerHTML = `
//       <div class="item" id="item-${post_id}" onclick="toggleLike(${post_id})">
//       ` + htmlHeart + `
//       <span id="slgLike+${post_id}">${slgLike} Likes</span>
//       </div>
//       <div class="item" onclick="toggleComments(${post_id})">
//         <span class="comment-icon"><i class="fa-regular fa-comment-dots"></i></span>
//         ${comments.length} Comments
//       </div>
//     `;
// }

async function fetDataComment(item) {
    let id = item.id;
    let post_id = id.split("-")[3];

    let container = item.parentElement.parentElement;
    console.log(container)

    let data = new FormData();
    data.append("post_id", post_id);

    const resp1 = await fetch("/searchComment",
        {
            method: "POST",
            body: data
        })
    if (!resp1.ok) {
        return;
    }

    let str_comment = "";
    let comments = await resp1.json();
    console.log(comments);
    let i = 0;
    if (comments.length > 0) {
        comments.forEach(item => {
            if (i < 2) {
                str_comment += `
              <div class="comment">
              <a href="profile?id=${item.user_id}"><img src="${item.user_avatar}" alt="" /></a>
              <div class="info">
                <a href="profile?id=${item.user_id}"><span>${item.user_name}</span></a>
                <p>${item.content}</p>
              </div>
              <span class="date">${formatTime(item.createdAt)}</span>
            </div>
              `;
            }
            i++;
        });

        if (comments.length > 2) {
            str_comment += `<div class="more-cmt" onclick="showMoreComments(${post_id})">More Comments</div>`
        }
        item.innerHTML = str_comment;

    }

    await updateLike(item.parentElement.parentElement, post_id)
    await updateTotalComments(item.parentElement.parentElement, post_id, comments.length)
}

fetchDataCommentPost();




