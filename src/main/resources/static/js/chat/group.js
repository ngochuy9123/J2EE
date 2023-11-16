const friendsInRoom = []
const uid = parseInt(document.getElementById("userIdDiv").innerText)

const addFriends = (event) => {
    const elems = event.currentTarget;
    const id = elems.id.slice(1);
    let html;
    if (friendsInRoom.includes(id)) {
        html = `<i class="bi bi-plus-circle-fill" style="padding: 0.25rem"></i>`

        const index = friendsInRoom.indexOf(id)
        friendsInRoom.splice(index, 1)

    }
    else {
        html = `<i class="bi bi-dash-circle-fill" style="padding: 0.25rem"></i>`
        friendsInRoom.push(id)
    }

    friendsInRoom.push()
    elems.innerHTML = html
}

const generateAddFriendBoxHtml = (data) => {
    let html = ``
    for (const datum of data) {

        const className = friendsInRoom.includes(`${datum['id']}`) ? "bi-dash-circle-fill" : "bi-plus-circle-fill"
        html += `
              <div id="friendList" class="d-flex justify-content-between">
                    <a class="user">
                        <img src="${datum["avatar"]}" alt="">
                        <span>${datum["email"]}</span>
                    </a>
                    <button id="f${datum['id']}" class="btn btn-outline-secondary" onclick="addFriends(event)"><i class="bi ${className}"  style="padding: 0.25rem" ></i></button>
                </div>
        `
    }

    return html
}

const addFriendClick = async () => {
    const resp = await fetch("/api/friends")
    const data = await resp.json();

    const html = generateAddFriendBoxHtml(data)

    document.getElementById("friendContent").innerHTML = html;


}


const addRoomSearch = async (event) => {
    const value = event.currentTarget.value;
    if (value.length === 0) {
        await addFriendClick()
        return;
    }

    if (value.length < 3) {
        return
    }

    const resp = await fetch(`/api/user?email=${value}&limit=5`)
    const data = await resp.json()
    const filteredData = data.filter(d => d["id"] !== uid)

    document.getElementById("friendContent").innerHTML = generateAddFriendBoxHtml(filteredData);
}

const saveRoom = async () => {
    const data = new FormData()
    data.append("users", JSON.stringify(friendsInRoom.map(Number)))
    console.log(data)

    const resp = await fetch("/api/room", {
        method: "POST",
        body: data,
    })

    if (resp.ok) {
        await setupSidePanel()
        friendsInRoom.splice(0)
    }
}

const removeFromRoom = async () => {
    if (selectedId === 0) {
        return;
    }
    const data = new FormData()
    data.append("roomId", selectedId)
    console.log(data)

    const resp = await fetch("/api/room", {
        method: "DELETE",
        body: data,
    })

    if (resp.ok) {
        await setupSidePanel()
        setMainChatPanel("TRUE")
    }
}

const rename = async () => {
    if (selectedId === 0) {
        return;
    }

    const input = document.getElementById("renameInput")
    const value = input.value

    const data = new FormData()
    data.append("roomId", selectedId)
    data.append("value", value)
    console.log(data)

    const resp = await fetch("/api/room", {
        method: "PATCH",
        body: data,
    })

    if (resp.ok) {
        await setupSidePanel()
        setRoomName(value)
    }
}

const changePhoto = async () => {
    if (selectedId === 0) {
        return;
    }

    const input = document.getElementById("photoInput")
    const value = input.files[0]

    const data = new FormData()
    data.append("roomId", selectedId)
    data.append("value", value)
    console.log(data)

    const resp = await fetch("/api/roomAvatar", {
        method: "POST",
        body: data,
    })

    if (resp.ok) {
        await setupSidePanel()
        setRoomImage(value)
    }
}