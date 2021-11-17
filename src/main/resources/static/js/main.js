const HOST = "192.168.45.242";
const PORT = 8443;
const CODE = getQueryStringObject().code;
const MAPPING = "/class";
const peerConnectionConfig = {
    'iceServers': [
        {'urls': 'stun:stun.l.google.com:19302'}
    ]
};

let ws;
let localStream;
let connections = {};
const shareScreen = document.getElementById('shareScreen');
const originScreen = document.getElementById('originScreen');

const selfView = document.getElementById("selfView");
const container = document.getElementById("remoteVideosContainer");
const sendBtn = document.getElementById('sendBtn');
const chatFelid = document.getElementById('chatFelid');
const chatMessageArea = document.getElementById('chatMessageArea');

const bigSizeVideoContainer = document.getElementById('bigSizeVideoContainer');
const bigSizeVideo = document.getElementById('bigSizeVideo');

let cameraFlag = true;
let micFlag = true;

init(CODE);

// 전체화면 해제
bigSizeVideo.addEventListener('click', ()=>{
    bigSizeVideo.srcObject = null;
    bigSizeVideoContainer.style.display = 'none';
    container.style.display = 'block';
});

// 채팅 메시지 보내기
sendBtn.addEventListener('click', ()=>{
    if(chatFelid.value.trim() === ''){
        alert('메시지를 입력해주세요.');
        chatFelid.focus();
        return;
    }
   sendMessage({
       type : 'chat',
       data : chatFelid.value.trim()
   });
    let childDiv = document.createElement('div');
    childDiv.classList.add('media-body');
    childDiv.style.float = 'right';

    let childP = document.createElement('p');
    childP.innerText = chatFelid.value.trim();

    childDiv.appendChild(childP);

    let parentDiv = document.createElement("div");
    parentDiv.classList.add('media');
    parentDiv.classList.add('p-3');

    parentDiv.appendChild(childDiv);

    chatMessageArea.appendChild(parentDiv);
    chatFelid.value = '';
    chatFelid.focus();
});

// 카메라
document.getElementById("camera").addEventListener("click", ()=>{
    cameraFlag = !cameraFlag;
    localStream.getVideoTracks()[0].enabled = cameraFlag;
});

// 오디오
document.getElementById("mic").addEventListener("click", ()=>{
    micFlag = !micFlag;
    localStream.getAudioTracks()[0].enabled = micFlag;
});

// 화면 공유
shareScreen.addEventListener('click', ()=>{
    navigator.mediaDevices.getDisplayMedia({
        audio: true,
        video: true
    }).then(function(stream){
        for(let uuid in connections){
            connections[uuid].getSenders().forEach(sender => {
                sender.replaceTrack(stream.getVideoTracks()[0]);
            })
        }
        shareScreen.style.display = 'none';
        originScreen.style.display = 'block';
    }).catch(function(e){
        console.log("Stream NOT OK: " + e.name + ': ' + e.message);
    });
});

originScreen.addEventListener('click', ()=>{
    navigator.mediaDevices.getUserMedia({
        audio: true,
        video: true
    }).then(function(stream){
        for(let uuid in connections){
            connections[uuid].getSenders().forEach(sender => {
                sender.replaceTrack(stream.getVideoTracks()[0]);
            })
        }
        shareScreen.style.display = 'block';
        originScreen.style.display = 'none';
    }).catch(function(e){
        console.log("Stream NOT OK: " + e.name + ': ' + e.message);
    });
});

function init(code) {
    navigator.mediaDevices.getUserMedia({video: true, audio: true})
        .then(function (stream) {
        console.log("Stream OK");
        localStream = stream;
        selfView.srcObject = stream;
        selfView.addEventListener('click', ()=>{
            container.style.display = 'none';
            bigSizeVideoContainer.style.display = 'block';
            bigSizeVideo.srcObject = stream;
        });
        ws = new WebSocket('wss://' + HOST + ':' + PORT + MAPPING + "/" + code);
        ws.onmessage = processWsMessage;
        ws.onopen = logMessage;
        ws.onclose = logMessage;
        ws.onerror = logMessage;
    }).catch(function (error) {
        console.log("Stream NOT OK: " + error.name + ': ' + error.message);
    });
}

function processWsMessage(message) {
    var signal = JSON.parse(message.data);
    switch (signal.type) {
        case 'chat':
            handleChat(signal);
        case 'init':
            handleInit(signal);
            break;
        case 'logout':
            handleLogout(signal);
            break;
        case 'offer':
            handleOffer(signal);
            break;
        case 'answer':
            handleAnswer(signal);
            break;
        case 'ice':
            handleIce(signal);
            break;
    }
}

function handleInit(signal) {
    var peerId = signal.sender;
    var connection = getRTCPeerConnectionObject(peerId);

    connection.createOffer().then(function (sdp) {
        connection.setLocalDescription(sdp);
        console.log("-----------------------------------------");
        console.log('Creating an offer for', peerId);
        sendMessage({
            type: "offer",
            receiver: peerId,
            data: sdp
        });
    }).catch(function(e) {
        console.log('Error in offer creation.', e);
    });
}

function handleChat(signal){
    console.log(signal.data);
    let childDiv = document.createElement('div');
    childDiv.classList.add('media-body');

    let childTitle = document.createElement('div');
    childTitle.innerText = signal.data;
    let childP = document.createElement('p');
    childP.innerText = chatFelid.value.trim();

    childDiv.appendChild(childTitle);
    childDiv.appendChild(childP);

    let parentDiv = document.createElement("div");
    parentDiv.classList.add('media');
    parentDiv.classList.add('p-3');
    parentDiv.appendChild(childDiv);

    chatMessageArea.appendChild(parentDiv);
}

function handleLogout(signal) {
    var peerId = signal.sender;
    delete connections[peerId];
    var videoElement = document.getElementById(peerId);
    videoElement.outerHTML = "";
    delete videoElement;
}

function handleOffer(signal) {
    var peerId = signal.sender;
    var connection = getRTCPeerConnectionObject(peerId);
    connection.setRemoteDescription(new RTCSessionDescription(signal.data)).then(function () {
        console.log('Setting remote description by offer from ' + peerId);
        connection.createAnswer().then( function(sdp) {
            connection.setLocalDescription(sdp);
            sendMessage({
                type: "answer",
                receiver: peerId,
                data: sdp
            });
        }).catch(function(e) {
            console.log('Error in offer handling.', e);
        });

    }).catch(function(e) {
        console.log('Error in offer handling.', e);
    });
}

function handleAnswer(signal) {
    var connection = getRTCPeerConnectionObject(signal.sender);
    connection.setRemoteDescription(new RTCSessionDescription(signal.data)).then(function() {
        console.log('Setting remote description by answer from' + signal.sender);
    }).catch(function(e) {
        console.log('Error in answer acceptance.', e);
    });
}

function handleIce(signal) {
    if (signal.data) {
        console.log('Adding ice candidate');
        var connection = getRTCPeerConnectionObject(signal.sender);
        connection.addIceCandidate(new RTCIceCandidate(signal.data));
    }
}

function getRTCPeerConnectionObject(uuid) {

    if(connections[uuid]) {
        return connections[uuid];
    }

    var connection = new RTCPeerConnection(peerConnectionConfig);

    connection.addStream(localStream);

    connection.onicecandidate = function (event) {
        console.log("candidate is: " + event.candidate);
        if (event.candidate) {
            sendMessage({
                type: "ice",
                receiver: uuid,
                data: event.candidate
            });
        }
    };

    connection.onaddstream = function(event) {
        console.log('Received new stream from ' + uuid);
        const video = document.createElement("video");
        video.classList.add('col-3');
        video.addEventListener('click', ()=>{
            container.style.display = 'none';
            bigSizeVideoContainer.style.display = 'block';
            bigSizeVideo.srcObject = event.stream;
        });
        container.appendChild(video);
        video.id = uuid;
        video.autoplay = true;
        video.srcObject = event.stream;
    };

    connections[uuid] = connection;
    return connection;
}

function sendMessage(payload) {
    payload.roomId = CODE;
    ws.send(JSON.stringify(payload));
}

function logMessage(message) {
    console.log(message);
}

function disconnect() {
    console.log('Disconnecting ');
    if(ws != null) {
        ws.close();
    }
}

function getQueryStringObject() {
    var a = window.location.search.substr(1).split('&');
    if (a == "") return {};
    var b = {};
    for (var i = 0; i < a.length; ++i) {
        var p = a[i].split('=', 2);
        if (p.length == 1)
            b[p[0]] = "";
        else
            b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
    }
    return b;
}