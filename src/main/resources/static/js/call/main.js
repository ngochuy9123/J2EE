// /*
//  *  Copyright (c) 2021 The WebRTC project authors. All Rights Reserved.
//  *
//  *  Use of this source code is governed by a BSD-style license
//  *  that can be found in the LICENSE file in the root of the source
//  *  tree.
//  */
//
// 'use strict';
//
// const startButton = document.getElementById('startButton');
// const hangupButton = document.getElementById('hangupButton');
// hangupButton.disabled = true;
//
// const localVideo = document.getElementById('localVideo');
// const remoteVideo = document.getElementById('remoteVideo');
//
// let pc;
// let localStream;
//
// // const signaling = new BroadcastChannel('webrtc');
// const stompClient = new StompJs.Client({
//     brokerURL: 'ws://localhost:8080/ws/call/1'
// });
//
// const conn = new WebSocket('ws://localhost:8080/chat');
// function send(message) {
//     conn.send(JSON.stringify(message));
// }
//
// conn.onmessage
//
// stompClient.onConnect = (frame) => {
//     setConnected(true);
//     console.log('Connected: ' + frame);
//     stompClient.subscribe(`/topic/call/1`, async (message) => {
//         await showMessage(message);
//     });
// };
//
// stompClient.activate();
//
//
// const showMessage = (message) => {
//     const e = JSON.parse(message.body)
//     console.log(e)
//     if (!localStream) {
//         console.log('not ready yet');
//         return;
//     }
//     switch (e.type) {
//         case 'offer':
//             handleOffer(e);
//             break;
//         case 'answer':
//             handleAnswer(e);
//             break;
//         case 'candidate':
//             handleCandidate(e);
//             break;
//         case 'ready':
//             // A second tab joined. This tab will initiate a call unless in a call already.
//             if (pc) {
//                 console.log('already in call, ignoring');
//                 return;
//             }
//             makeCall();
//             break;
//         case 'bye':
//             if (pc) {
//                 hangup();
//             }
//             break;
//         default:
//             console.log('unhandled', e);
//             break;
//     }
// };
//
// function setConnected(connected) {
//     console.log(connected)
// }
//

// const send = (value) => {
//     stompClient.publish({
//         destination: "/topic/call/1",
//         body: JSON.stringify(value)
//     })
// }
//
//
// startButton.onclick = async () => {
//     localStream = await navigator.mediaDevices.getUserMedia({audio: true, video: true});
//     localVideo.srcObject = localStream;
//
//
//     startButton.disabled = true;
//     hangupButton.disabled = false;
//
//     send({type: 'ready'});
// };
//
//
// hangupButton.onclick = async () => {
//     hangup();
//     send({type: 'bye'});
// };
//
// async function hangup() {
//     if (pc) {
//         pc.close();
//         pc = null;
//     }
//     localStream.getTracks().forEach(track => track.stop());
//     localStream = null;
//     startButton.disabled = false;
//     hangupButton.disabled = true;
// };
//
// function createPeerConnection() {
//     pc = new RTCPeerConnection();
//     pc.onicecandidate = e => {
//         const message = {
//             type: 'candidate',
//             candidate: null,
//         };
//         if (e.candidate) {
//             message.candidate = e.candidate.candidate;
//             message.sdpMid = e.candidate.sdpMid;
//             message.sdpMLineIndex = e.candidate.sdpMLineIndex;
//         }
//         console.log(message)
//         send(message);
//     };
//     pc.ontrack = e => remoteVideo.srcObject = e.streams[0];
//     localStream.getTracks().forEach(track => pc.addTrack(track, localStream));
// }
//
// async function makeCall() {
//     await createPeerConnection();
//
//     const offer = await pc.createOffer();
//     send({type: 'offer', sdp: offer.sdp});
//     await pc.setLocalDescription(offer);
// }
//
// async function handleOffer(offer) {
//     if (pc) {
//         console.error('existing peerconnection');
//         return;
//     }
//     await createPeerConnection();
//     await pc.setRemoteDescription(offer);
//
//     const answer = await pc.createAnswer();
//     send({type: 'answer', sdp: answer.sdp});
//     await pc.setLocalDescription(answer);
// }
//
// async function handleAnswer(answer) {
//     if (!pc) {
//         console.error('no peerconnection');
//         return;
//     }
//     await pc.setRemoteDescription(answer);
// }
//
// async function handleCandidate(candidate) {
//     if (!pc) {
//         console.error('no peerconnection');
//         return;
//     }
//     if (!candidate.candidate) {
//         await pc.addIceCandidate(null);
//     } else {
//         await pc.addIceCandidate(candidate);
//     }
// }
//



/*
 *  Copyright (c) 2021 The WebRTC project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree.
 */

'use strict';

const startButton = document.getElementById('startButton');
const hangupButton = document.getElementById('hangupButton');
hangupButton.disabled = true;

const localVideo = document.getElementById('localVideo');
const remoteVideo = document.getElementById('remoteVideo');

const localUUID = crypto.randomUUID();

const createVideoDiv = (uuid) => {
    const a = document.getElementById("container")
    const node = document.createElement("video");
    node.id = uuid
    node.playsInline = true
    node.autoplay = true
    a.appendChild(node);

    return document.getElementById(uuid)
}


let isAnswerSet = false;

const pcs = {

}

let pc;
let localStream;

const alreadyAnswer = []

const host = new URL(location)
console.log(host.host)

const signaling = new WebSocket(`ws://${host.host}/call`);
signaling.onmessage = async message => {
    if (!localStream) {
        console.log('not ready yet');
        return;
    }
    const data = JSON.parse(message.data);
    console.log(data)

    const pc = pcs[data.id]

    switch (data.type) {
        case 'offer':
            await handleOffer(data);
            break;
        case 'answer':
            await handleAnswer(data)
            break;
        case 'candidate':
            await handleCandidate(data);
            break;
        case 'ready':
            await makeCall(data);
            break;
        case 'bye':
            if (pc) {
                await hangup();
            }
            break;
        default:
            console.log('unhandled', data);
            break;
    }
};

startButton.onclick = async () => {
    localStream = await navigator.mediaDevices.getUserMedia({audio: true, video: true});
    localVideo.srcObject = localStream;


    startButton.disabled = true;
    hangupButton.disabled = false;

    send({type: 'ready', id: localUUID});
};

hangupButton.onclick = async () => {
    hangup();
    send({type: 'bye', id: localUUID});
};

async function hangup() {
    if (pc) {
        pc.close();
        pc = null;
    }
    localStream.getTracks().forEach(track => track.stop());
    localStream = null;
    startButton.disabled = false;
    hangupButton.disabled = true;
};

function createPeerConnection(data) {
    let pc = new RTCPeerConnection(null);
    const div = createVideoDiv(data.id)

    pc.onicecandidate = e => {
        const message = {
            type: 'candidate',
            candidate: null,
            id: localUUID
        };
        if (e.candidate) {
            message.candidate = e.candidate.candidate;
            message.sdpMid = e.candidate.sdpMid;
            message.sdpMLineIndex = e.candidate.sdpMLineIndex;
        }
        send(message);
    };
    pc.ontrack = e => div.srcObject = e.streams[0];
    localStream.getTracks().forEach(track => pc.addTrack(track, localStream));

    pcs[data.id] = pc;
}

async function makeCall(data) {
    await createPeerConnection(data);

    const pc = pcs[data.id]

    const offer = await pc.createOffer();
    send({type: 'offer', sdp: offer.sdp, id: localUUID});
    await pc.setLocalDescription(offer);
}

async function handleOffer(offer) {

    await createPeerConnection(offer);

    const pc = pcs[offer.id]

    await pc.setRemoteDescription(offer);

    const answer = await pc.createAnswer();
    send({type: 'answer', sdp: answer.sdp, id: localUUID});
    await pc.setLocalDescription(answer);
}

async function handleAnswer(answer) {
    console.log("[ANSWER!!!------]")
    console.log(answer)
    console.log("[ANSWER!!!------]")
    if (alreadyAnswer.includes(answer.id)) {
        return;
    }
    try {
        const pc = pcs[answer.id];
        alreadyAnswer.push(answer.id)
        
        if (!pc) {
            console.error('no peerconnection');
            return;
        }


        await pc.setRemoteDescription(answer);

    }
    catch (e) {
            const pc = pcs[answer.id];
            console.error(e)
            console.log("ERR:")
            console.log("____")
            console.log(answer)
            console.log(pc)
            console.log("____")
        }
}

async function handleCandidate(candidate) {
    const pc = pcs[candidate.id];
    if (!pc) {
        console.error('no peerconnection');
        return;
    }
    if (!candidate.candidate) {
        await pc.addIceCandidate(null);
    } else {
        await pc.addIceCandidate(candidate);
    }
}

function send(message) {
    signaling.send(JSON.stringify(message));
}