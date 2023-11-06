const conn = new WebSocket('ws://localhost:8080/chat');
function send(message) {
    conn.send(JSON.stringify(message));
}
configuration = null;
const peerConnection = new RTCPeerConnection(configuration);

var dataChannel = peerConnection.createDataChannel("dataChannel", { reliable: true });
dataChannel.onerror = function(error) {
    console.log("Error:", error);
};
dataChannel.onclose = function() {
    console.log("Data channel is closed");
};

peerConnection.createOffer(function(offer) {
    send({
        event : "offer",
        data : offer
    });
    peerConnection.setLocalDescription(offer);
}, function(error) {
    // Handle error here
});

peerConnection.onicecandidate = function(event) {
    if (event.candidate) {
        peerConnection.addIceCandidate(new RTCIceCandidate(event.candidate));
        send({
            event : "candidate",
            data : event.candidate
        });
    }
};


const id = document.getElementById("userIdDiv").innerText;
const uuid = document.getElementById("UUIDDiv").innerText;
let selectedId = 0;

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/ws/chat'
});

const stompClient2 = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/ws/chat'
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


async function showMessage(message) {
    const data = JSON.parse(message.body)
    console.log(data)
    //
    await updateLifeLine()


    if (data['roomId'] != selectedId) {
        await updateTotalMissed(data['roomId'])
        return;
    }

    const chatHistoryElem = document.getElementById("chatHistory");
    chatHistoryElem.innerHTML += generateChatHtml(data);

    chatHistoryElem.scrollTo( 0, chatHistoryElem.scrollHeight )

}

const updateTotalMissed = async (roomId) => {
    const total = await getTotalMissedMessages(roomId);
    const div = document.getElementById(`missed${roomId}`);

    let missedDiv= "";
    if (total !== 0) {
        missedDiv = `<div class="btn btn-warning">${total}</div>`
    }

    div.innerHTML = missedDiv
}


const setupSidePanel = async () => {
    const resp = await fetch("/rooms");
    const data = await resp.json();

    // console.log(data)

    let html = ``;

    for (const datum of data) {
        const now = Date.now();
        const diffInDays = (now - new Date(datum[
            'lastUpdated'
            ])) / (1000 * 60 * 60 * 24) | 0;

        const diffInHours = (now - new Date(datum[
            'lastUpdated'
            ])) / (1000 * 60 * 60) | 0;


        let showDate = "";
        if (diffInDays != 0) {
            showDate = diffInDays + " ngày"
        }
        else {
            showDate = diffInHours + " giờ"
        }
        const missedMessage = await getTotalMissedMessages(datum['id'])
        let missedDiv= "";
        if (missedMessage !== 0) {
            missedDiv = `<div class="btn btn-warning">${missedMessage}</div>`
        }
        
        console.log(datum)
        html += `
            <li class="clearfix d-flex" id="r${datum['id']}" onclick="setChatPanel(${datum['id']})">
                <img src="https://bootdey.com/img/Content/avatar/avatar1.png" alt="avatar">
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
}

const getTotalMissedMessages = async (roomId) => {
    const resp = await fetch(`api/room/missed/${roomId}`)
    return Number(await resp.text());
}



const setChatPanel = async (roomId) => {
    const resp = await fetch(`/room/${roomId}`)
    const data = await resp.json();

    selectedId = roomId;

    await updateLifeLine()

    await updateTotalMissed(roomId)


    console.log(data)

    let html = "";
    for (const datum of data) {
        html += generateChatHtml(datum)
    }

    const chatHistoryElem = document.getElementById("chatHistory");
    chatHistoryElem.innerHTML = html;

    chatHistoryElem.scrollTo( 0, chatHistoryElem.scrollHeight )

}

const generateChatHtml = (data) => {
    const uId = data['userId']
    let html = "";
    const date = new Date(data['timeSend']);

    if (uId == id) {
        return `
            <li class="clearfix">
                <div class="message-data ">
                    <span class="message-data-time d-flex justify-content-end">${date}</span>
                </div>
                <div class="message other-message float-right"> ${data['message']} </div>
            </li>
            `
    }
    else {
        return `
           <li class="clearfix">
                <div class="message-data">
                    <span class="message-data-time">${date.getUTCDate()}</span>
                </div>
                <div class="message my-message">${data['message']}</div>
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
        timeSend : Date.now()
    };

    const response = await fetch("/message", {
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
}

connect();
setupSidePanel();