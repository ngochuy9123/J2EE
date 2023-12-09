const generateFriendDropdownHtml = (friend) => {
  return `
    <li>
      <a class="dropdown-item"
        href="/profile?id=${friend['userId']}"></a>
      <i class="user">
      <img
              src="${friend['avatar']}"
              alt=""
      />
      <span class="detail-noti">  <strong th:text="${friend['email']}">
      
      
      </strong>đã gửi cho bạn lời mời kết bạn
      <div>${friend['email']}</div>
      <div class="wrap-btn">
      <input type="hidden" value="${friend['userId']}" class="accept-input"/>
      <button onclick="acceptFriendRequest(this)" class="accept-btn">Accept</button>
      <button onclick="declineFriendRequest(this)" class="dismiss-btn">Dismiss</button>
      </div>
      </span>
      </i>
    </li>
  `
}

const generateFriendIconDiv = (total) => {
  return total > 0
      ? `<div class="notice-addfr"
                         >${total}</div>`
      : ""
}

const handleFriendSocket = async () => {
  const resp = await fetch("/api/friendRequests")
  const data = await resp.json()
  const html = data.map(generateFriendDropdownHtml).join()
  console.log(data)
  console.log(html)

  const friendsIconDiv = document.getElementById("friendIcon")
  friendsIconDiv.innerHTML = generateFriendIconDiv(data.length)

  const friendDropdown = document.getElementById("friendRequestList")
  friendDropdown.innerHTML = html

}

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

// document.addEventListener("click", function (event) {
//   // Hide the tooltip when clicking outside the trigger element
//   if (event.target !== triggerElement) {
//     tooltipContainer.style.display = "none";
//   }
// });
function makeEditable(fieldName) {
  const element = document.querySelector(`.${fieldName}`);
  const currentValue = element.innerText;

  // Create an input element
  const inputElement = document.createElement("input");
  inputElement.value = currentValue;

  // Set the input element's class and focus
  inputElement.className = "form-control";
  inputElement.addEventListener("blur", () =>
    updateValue(fieldName, inputElement.value)
  );
  inputElement.addEventListener("keyup", (event) => {
    if (event.key === "Enter") {
      inputElement.blur();
    }
  });

  // Replace the span with the input
  element.replaceWith(inputElement);

  // Focus the input
  inputElement.focus();
}

function updateValue(fieldName, newValue) {
  // Create a new span element
  const spanElement = document.createElement("span");
  spanElement.className = `editable ${fieldName}`;
  spanElement.innerText = newValue;

  // Replace the input with the span
  document.querySelector(`.${fieldName}`).replaceWith(spanElement);
}

// // Friends
// async function declineFriendRequest(id_user) {
//
//   console.log(id_user)
//   let data = new FormData
//   data.append("userToId",id_user)
//
//   const resp = await fetch("/declineFriendRequest",
//       {
//         method: "POST",
//         body: data
//       })
//   const data1 = await resp.text();
//   console.log(data1)
//
// }


async function acceptFriendRequest(inputValue) {
  // let inputValue = button.previousElementSibling.value;
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
  data.append("userToId", inputValue)

  const resp = await fetch("/declineFriendRequest",
      {
        method: "POST",
        body: data
      })
  const data1 = await resp.text();
  console.log(data1);

}