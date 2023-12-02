// const conn = new WebSocket('ws://localhost:8080/chat');
// function send(message) {
//     conn.send(JSON.stringify(message));
// }
// configuration = null;
// const peerConnection = new RTCPeerConnection(configuration);
//
// var dataChannel = peerConnection.createDataChannel("dataChannel", { reliable: true });
// dataChannel.onerror = function(error) {
//     console.log("Error:", error);
// };
// dataChannel.onclose = function() {
//     console.log("Data channel is closed");
// };
//
// peerConnection.createOffer(function(offer) {
//     send({
//         event : "offer",
//         data : offer
//     });
//     peerConnection.setLocalDescription(offer);
// }, function(error) {
//     // Handle error here
// });
//
// peerConnection.onicecandidate = function(event) {
//     if (event.candidate) {
//         peerConnection.addIceCandidate(new RTCIceCandidate(event.candidate));
//         send({
//             event : "candidate",
//             data : event.candidate
//         });
//     }
// };


const id = document.getElementById("userIdDiv").innerText;
const uuid = document.getElementById("UUIDDiv").innerText;
let selectedId = 0;

const host = new URL(location)


const stompClient = new StompJs.Client({
    brokerURL: `ws://${host.host}/ws/chat`
});


stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe(`/topic/message/${id}/${uuid}`, async (message) => {
        await showMessage(message);
    });

};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};


function setConnected(connected) {
    console.log(connected)
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

const handleMessage = async (data) => {
    if (data['roomId'] != selectedId) {
        await updateTotalMissed(data['roomId'])
        return;
    }

    const chatHistoryElem = document.getElementById("chatHistory");
    chatHistoryElem.innerHTML += generateChatHtml(data);

    chatHistoryElem.scrollTo( 0, chatHistoryElem.scrollHeight )

}

const handleRename = async (data) => {
    const { id, name } = data

    const roomNameElem = document.getElementById("roomName")

    if (selectedId == id) {
        roomNameElem.innerText = name
    }

    const sideDiv = document.getElementById(`r${id}`)
    if (sideDiv == null) {
        await setupSidePanel()
        return;
    }

    const sideName = sideDiv.querySelector("div.name")
    sideName.innerText = name


};

const handlePhoto = async (data) => {
    const { id, roomAvatar } = data

    const roomImgElem = document.getElementById("groupAvatar")

    if (selectedId == id) {
        roomImgElem.src = roomAvatar
    }

    const sideDiv = document.getElementById(`r${id}`)
    if (sideDiv == null) {
        await setupSidePanel()
        return;
    }

    const sidePhoto = sideDiv.querySelector("img")
    sidePhoto.src = roomAvatar


};

async function showMessage(message) {
    const resp = JSON.parse(message.body)
    console.log(resp)
    //
    await updateLifeLine()

    const data = resp["data"]


    switch (resp["type"]) {
        case "MESSAGE":
            await handleMessage(data)
            break;
        case "RENAME":
            await handleRename(data)
            break;
        case "PHOTO":
            await handlePhoto(data)
            break
    }



}

const updateTotalMissed = async (roomId) => {
    const total = await getTotalMissedMessages(roomId);
    const div = document.getElementById(`missed${roomId}`);
    if (div == null) {
        setupSidePanel();
        return;
    }

    let missedDiv= "";
    if (total !== 0) {
        missedDiv = `<div class="btn btn-warning">${total}</div>`
    }

    div.innerHTML = missedDiv
}

const getDiffTime = (date) => {
    const now = Date.now();
    const diffInDays = (now - date) / (1000 * 60 * 60 * 24) | 0;
    const diffInHours = (now - date) / (1000 * 60 * 60) | 0;

    let showDate = "";
    if (diffInDays !== 0) {
        return diffInDays + " ngày"
    }
    else {
        return diffInHours + " giờ"
    }
}

const setupSidePanel = async () => {
    const resp = await fetch("/rooms");
    const data = await resp.json();

    // console.log(data)

    let html = ``;

    for (const datum of data) {
        const showDate = getDiffTime(new Date(datum['lastUpdated']))
        const missedMessage = await getTotalMissedMessages(datum['id'])
        let missedDiv= "";
        if (missedMessage !== 0) {
            missedDiv = `<div class="btn btn-warning">${missedMessage}</div>`
        }
        
        console.log(datum)
        html += `
            <li class="clearfix d-flex" id="r${datum['id']}">
                <img src="${datum['roomAvatar']}" alt="avatar">
                <div class="about d-flex justify-content-between" style="width: 80%">
                <div>
                    <div class="name">${datum['name']}</div>
                        <div class="status"> <i class="fa fa-circle"></i> ${showDate} </div>
                    </div>
                <div id="missed${datum['id']}">
                    ${missedDiv}
                </div>    
                </div>
            </li>
        `
        document.getElementById("roomList").innerHTML = html;

    }

    for (const datum of data) {
        const elem = document.getElementById(`r${datum['id']}`);
        elem.addEventListener("click",()=> setChatPanel(datum['id']))
    }
}

const getTotalMissedMessages = async (roomId) => {
    const resp = await fetch(`api/room/missed/${roomId}`)
    return Number(await resp.text());
}



const setChatPanel = async (id) => {

    const roomResp = await fetch(`/api/roomInfo/${id}`)
    const room = await roomResp.json()

    const roomId = room['id']
    const roomImg = room['roomAvatar']

    const messageResp = await fetch(`/api/roomMessage/${roomId}`)
    const messages = await messageResp.json();

    const groupImg = document.getElementById("groupAvatar");
    groupImg.src = roomImg

    const roomNameDiv = document.getElementById("roomName");
    roomNameDiv.innerText = room["name"]

    selectedId = roomId;

    await updateLifeLine()

    await updateTotalMissed(roomId)


    console.log(messages)

    let html = "";
    for (const datum of messages) {
        html += generateChatHtml(datum)
    }

    const chatHistoryElem = document.getElementById("chatHistory");
    chatHistoryElem.innerHTML = html;
    const container = document.getElementById("chatHistoryContainer");
    setMainChatPanel("FALSE")

    container.scrollTo( 0, container.scrollHeight)
}

const handleCall = () => {
    const url = `/call/${selectedId}`
    window.open(url, '_blank').focus();
}


const generateChatHtml = (data) => {
    const uId = data['userId']
    const messageType = data['messageType']
    let html = "";
    const date = getDiffTime(new Date(data['timeSend']));

    let content;

    if (messageType === "MESSAGE") {
        content = data["message"]
    }
    else if (messageType === "PICTURE") {
        content = `<img src="${data["message"]}" alt="user message" class="messageImage"  />`
    }
    else {
        content = data["message"]
    }


    if (uId == id) {
        return `
            <li class="clearfix">
                <div class="message-data ">
                    <span class="message-data-time d-flex justify-content-end">${date}</span>
                </div>
                <div class="message other-message float-right"> ${content} </div>
            </li>
            `
    }
    else {
        return `
           <li class="clearfix">
                <div class="message-data">
                    <span class="message-data-time">${date}</span>
                </div>
                <div class="message my-message">${content}</div>
            </li>
        `
    }
}

const updateLifeLine = async () => {
    if (selectedId === undefined) {
        selectedId = 0;
    }
    const resp = await fetch(`/api/message/lifeline/${selectedId}`)
    const data = await resp.text();
    console.log(data)
}

const sendMessage = async (event) => {
    event.preventDefault()
    console.log(event)
    if (selectedId === 0) {
        return;
    }

    const data = {
        message : event.target[0].value,
        userId : id,
        roomId : selectedId,
        timeSend : Date.now(),
        messageType : "NORMAL"
    };

    const response = await fetch("/api/message", {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        mode: "cors", // no-cors, *cors, same-origin
        cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
        credentials: "same-origin", // include, *same-origin, omit
        headers: {
            "Content-Type": "application/json",
            // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        redirect: "follow", // manual, *follow, error
        referrerPolicy: "no-referrer", // no-referrer, *no-referrer-when-downgrade, origin, origin-when-cross-origin, same-origin, strict-origin, strict-origin-when-cross-origin, unsafe-url
        body: JSON.stringify(data), // body data type must match "Content-Type" header
    });

    event.target[0].value = ""
}


const sendPhotoMessage = async () => {
    if (selectedId === 0) {
        return;
    }
    const data = {
        message : "",
        userId : id,
        roomId : selectedId,
        timeSend : Date.now(),
        messageType : "PICTURE"
    };

    const fileElem = document.getElementById("sendPhotoInput")
    const form = new FormData();
    form.append("message", JSON.stringify(data))
    form.append("value", fileElem.files[0])



    const response = await fetch("/api/photoMessage", {
        method: "POST", // *GET, POST, PUT, DELETE, etc.
        body: form, // body data type must match "Content-Type" header
    });

    fileElem.value = ""
}

const setMainChatPanel = (value) => {
    const main = document.getElementById("mainChat")
    main.setAttribute("data-hide", value)
}

const hideChatPanel = () => {
    setMainChatPanel("TRUE");
}

const setRoomName = (value) => {
    const elem = document.getElementById("roomName")
    elem.innerText = value
}


const setRoomImage = (value) => {
    const elem = document.getElementById("groupAvatar")
    const fr = new FileReader()
    fr.readAsDataURL(value)

    fr.onload = () => {
        elem.src = fr.result;
    }

}

connect();
setupSidePanel();
setMainChatPanel("TRUE");