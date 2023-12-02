const userInNewRoom = {
    data: [],
    users: []
}
let userInRoom = {
    originalUserInRoomList : [],
    changedUserInRoomList : [],
    userNotInRoom: []
}

const uid = parseInt(document.getElementById("userIdDiv").innerText)

const addFriends = async (event) => {
    const elems = event.currentTarget;
    const id = elems.id.slice(1);
    let html;
    if (userInNewRoom.users.includes(id)) {
        html = `<i class="bi bi-plus-circle-fill" style="padding: 0.25rem"></i>`

        const index = userInNewRoom.users.indexOf(id)
        userInNewRoom.users.splice(index, 1)

        const dataIndex = userInNewRoom.data.map(d => d["id"]).indexOf(id)
        userInNewRoom.data.splice(dataIndex, 1)

    }
    else {
        html = `<i class="bi bi-dash-circle-fill" style="padding: 0.25rem"></i>`
        userInNewRoom.users.push(id)

        const userResp = await fetch(`/api/userById?id=${id}`)
        userInNewRoom.data.push(await userResp.json())

    }

    elems.innerHTML = html
}

const generateAddFriendBoxHtml = (data) => {
    let html = ``
    for (const datum of data) {

        const className = userInNewRoom.users.includes(`${datum['id']}`) ? "bi-dash-circle-fill" : "bi-plus-circle-fill"
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


const addDataToUserNotInRoom = async (id) => {
    const userResp = await fetch(`/api/userById?id=${id}`)
    const userData = await userResp.json()
    userInRoom.userNotInRoom.push(userData)

}

const removeDataToUserNotInRoom = (id) => {
    const userNotInRoom = userInRoom.userNotInRoom
    const userIds = userNotInRoom.map(u => `${u['id']}`)
    const index = userIds.indexOf(id)
    userNotInRoom.splice(index, 1)
}

const changeUser = async (event) => {
    const elems = event.currentTarget;
    const id = elems.id.slice(1);
    let html;
    if (userInRoom.changedUserInRoomList.includes(id)) {
        html = `<i class="bi bi-plus-circle-fill" style="padding: 0.25rem"></i>`

        const index = userInRoom.changedUserInRoomList.indexOf(id)
        userInRoom.changedUserInRoomList.splice(index, 1)

        removeDataToUserNotInRoom()

    }
    else {
        html = `<i class="bi bi-dash-circle-fill" style="padding: 0.25rem"></i>`
        userInRoom.changedUserInRoomList.push(id)

        if (!userInRoom.originalUserInRoomList.includes(id)) {
            await addDataToUserNotInRoom(id)
        }
    }

    elems.innerHTML = html
}

const generateChangeUserHtml = (data, isOwner) => {
    const {originalUserInRoomList, changedUserInRoomList} = userInRoom

    let html = ``
    for (const datum of data) {
        const uId = datum['id'];
        let className ;
        let button;

        if (isOwner === true) {
            className = changedUserInRoomList.includes(`${uId}`)
                ? "bi-dash-circle-fill"
                : "bi-plus-circle-fill"

            button = `<button id="u${datum['id']}" class="btn btn-outline-secondary" 
                                onClick="changeUser(event)"><i class="bi ${className}"  style="padding: 0.25rem" ></i></button>`
        }
        else {
            if (changedUserInRoomList.includes(`${uId}`) && !originalUserInRoomList.includes(`${uId}`)) {
                className = "bi-dash-circle-fill"
                button = `<button id="u${datum['id']}" class="btn btn-outline-secondary" 
                                onClick="changeUser(event)"><i class="bi ${className}"  style="padding: 0.25rem" ></i></button>`
            }
            else if (!changedUserInRoomList.includes(`${uId}`) && !originalUserInRoomList.includes(`${uId}`)) {
                className = "bi-plus-circle-fill"
                button = `<button id="u${datum['id']}" class="btn btn-outline-secondary" 
                                onClick="changeUser(event)"><i class="bi ${className}"  style="padding: 0.25rem" ></i></button>`

            }

            else {
                button = ""
            }

        }





        html += `
              <div id="friendList" class="d-flex justify-content-between">
                    <a class="user">
                        <img src="${datum["avatar"]}" alt="">
                        <span>${datum["email"]}</span>
                    </a>
                    ${button}
                </div>
        `
    }

    return html
}


const resetAndAddNewRoom = async () => {
    userInNewRoom.users = []
    userInNewRoom.data = []

    await handleAddNewRoom()
}


const handleAddNewRoom = async () => {
    const resp = await fetch("/api/friends")
    const data = await resp.json();


    let html = generateAddFriendBoxHtml(userInNewRoom.data)

    html += generateAddFriendBoxHtml(data.filter(d => !userInNewRoom.users.includes(`${d["id"]}`)))
    document.getElementById("friendContent").innerHTML = html;
}

const handleChangeUser = async () => {

    const usersInRoomResp = await fetch(`/api/roomInfo/${selectedId}`)
    const usersInRoomData = await usersInRoomResp.json();

    const isOwnerResp = await fetch(`/api/isRoomOwner/${selectedId}`)
    const isOwnerData = await isOwnerResp.json();

    console.log(isOwnerData)
    console.log(usersInRoomData)

    const participants = usersInRoomData["participants"]
    userInRoom.originalUserInRoomList = participants.map(p => `${p["id"]}`)
    userInRoom.changedUserInRoomList = [...userInRoom.originalUserInRoomList]
    userInRoom.userNotInRoom = []

    document.getElementById("userRoomContent").innerHTML = generateChangeUserHtml(participants, isOwnerData);
    document.getElementById("otherRoomContent").innerHTML = "";
}


const submitUserChange = async () => {
    const changedUser = userInRoom.changedUserInRoomList.map(id => Number(id))

    const data = new FormData()
    data.append("users", JSON.stringify(changedUser))

    const resp = await fetch(`/api/room/${selectedId}`, {
        method: "PUT",
        body: data,
    })

    if (resp.ok) {
        await setupSidePanel()
        userInNewRoom.splice(0)
    }
    
}

const generateSelectedSearchUser = async () => {
    const isOwnerResp = await fetch(`/api/isRoomOwner/${selectedId}`)
    const isOwnerData = await isOwnerResp.json();

    return generateChangeUserHtml(userInRoom.userNotInRoom, isOwnerData)

}

const changeUserSearch = async (event) => {
    const {changedUserInRoomList} = userInRoom

    const value = event.currentTarget.value;
    if (value.length === 0) {
        return;
    }

    if (value.length < 3) {
        return
    }

    const isOwnerResp = await fetch(`/api/isRoomOwner/${selectedId}`)
    const isOwnerData = await isOwnerResp.json();

    const resp = await fetch(`/api/user?email=${value}&limit=5`)
    const data = await resp.json()
    const filteredData = data.filter(d => !changedUserInRoomList.includes(`${d["id"]}`))

    let content = generateChangeUserHtml(filteredData, isOwnerData);
    content += await generateSelectedSearchUser()
    document.getElementById("otherRoomContent").innerHTML = content

}


const addRoomSearch = async (event) => {
    const value = event.currentTarget.value;
    if (value.length === 0) {
        await handleAddNewRoom()
        return;
    }

    if (value.length < 3) {
        return
    }

    const resp = await fetch(`/api/user?email=${value}&limit=5`)
    const data = await resp.json()
    const filteredData = data.filter(d => d["id"] !== uid)
        .filter(d => !userInNewRoom.users.includes(`${d["id"]}`))

    let html = generateAddFriendBoxHtml(filteredData);
    html += generateAddFriendBoxHtml(userInNewRoom.data)
    document.getElementById("friendContent").innerHTML = html;
}

const saveRoom = async () => {
    const data = new FormData()
    data.append("users", JSON.stringify(userInNewRoom.users.map(Number)))
    console.log(data)

    const resp = await fetch("/api/room", {
        method: "POST",
        body: data,
    })

    if (resp.ok) {
        await setupSidePanel()
        userInNewRoom.splice(0)
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

}