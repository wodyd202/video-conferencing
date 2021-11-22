const HOST = "192.168.45.163";
const PORT = 8443;
const QUERY_STRING = getQueryStringObject();
const CODE = QUERY_STRING.code;
const MAPPING = "/class";
const peerConnectionConfig = {
    'iceServers': [
        {'urls': 'stun:stun.l.google.com:19302'}
    ]
};

let ws;
let localStream;
let connections = {};

const currentUserId = document.getElementById('currentUserId').value;
const shareScreen = document.getElementById('shareScreen');
const originScreen = document.getElementById('originScreen');

const selfView = document.getElementById("selfView");
const container = document.getElementById("remoteVideosContainer");
const sendBtn = document.getElementById('sendBtn');
const shakeBtn = document.getElementById('shakeBtn');
const chatAbleBtn = document.getElementById('chatAbleBtn');

const chatFelid = document.getElementById('chatFelid');
const chatMessageArea = document.getElementById('chatMessageArea');
const alramArea = document.getElementById('alramArea');
const joinerList = document.getElementById('joinerList');
const joinerCount = document.getElementById('joinerCount');

const bigSizeVideoContainer = document.getElementById('bigSizeVideoContainer');
const bigSizeVideo = document.getElementById('bigSizeVideo');

const chatArea = document.getElementById('chatArea');
const videoArea = document.getElementById('videoArea');
let chatFlag = false;

let cameraFlag = QUERY_STRING.cam === 'true' ? true : false;
let micFlag = QUERY_STRING.mic === 'true' ? true : false;

let currentLayout = 'col-3';

init(CODE);

// 채팅창 활성화
chatAbleBtn.addEventListener('click', ()=>{
    chatFlag = !chatFlag;
    if(chatFlag){
        videoArea.classList.remove('col-12');
        videoArea.classList.add('col-8');
        chatArea.style.display = 'block';
    }else{
        videoArea.classList.remove('col-8');
        videoArea.classList.add('col-12');
        chatArea.style.display = 'none';
    }
});

// 전체화면 해제
bigSizeVideo.addEventListener('click', ()=>{
    bigSizeVideo.srcObject = null;
    bigSizeVideoContainer.style.display = 'none';
    container.style.display = 'block';
});

// 흔들기 버튼 클릭
shakeBtn.addEventListener('click', ()=>{
    sendMessage({
        type : 'shake'
    });
});

// 채팅중 엔터키 입력
chatFelid.addEventListener('keyup', (event)=>{
    const message = chatFelid.value.trim();
    if(event.keyCode === 13 && message !== ''){
        sendMessage({
            type : 'chat',
            data : message
        });
        printChatMessage("",message,true,false);
    }
});

// 채팅 메시지 보내기
sendBtn.addEventListener('click', ()=>{
    const message = chatFelid.value.trim();
    if(message === ''){
        alert('메시지를 입력해주세요.');
        chatFelid.focus();
        return;
    }
    sendMessage({
        type : 'chat',
        data : message
    });
    printChatMessage("", message, true,false);
});

// 회의에서 나가기
document.getElementById('exitBtn').addEventListener('click', ()=>{
    location.href = './class-list';
});

// 설정
document.getElementById('settingComplateBtn').addEventListener('click', ()=>{
   const changeLayout = document.getElementById('changeLayoutSelect').value;

   const videos = document.getElementsByTagName('video');
   for(let i =0;i<videos.length;i++){
       videos[i].classList.remove(currentLayout);
       videos[i].classList.add(changeLayout);
   }
   currentLayout = changeLayout;
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
        audio: micFlag,
        video: cameraFlag
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
        audio: micFlag,
        video: cameraFlag
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
        localStream.getVideoTracks()[0].enabled = cameraFlag;
        localStream.getAudioTracks()[0].enabled = micFlag;
        document.getElementById('camera').checked = cameraFlag;
        document.getElementById('mic').checked = micFlag;
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
        case "shake":
            handleShake(signal);
            break;
        case "join":
            handleJoin(signal);
            break;
        case "enter":
            handleEnter(signal);
            break;
        case 'chat':
            handleChat(signal);
            break;
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

function handleShake(signal){
    showAlram('shake', signal.sender, signal.sender + "님이 손을 흔드셨습니다.");
    const video = document.getElementById('video_' + signal.sender);
    video.classList.add('shake');

    setTimeout(()=>{
        video.classList.remove('shake');
    },2000);
}

function handleJoin(signal){
    showAlram('join', signal.sender, signal.sender + "님이 입장하셨습니다.");
}

function showAlram(type, userId, message){
    const parentDiv = document.createElement('div');
    parentDiv.classList.add('toast');
    parentDiv.classList.add('show');
    parentDiv.id = type + '_' + userId;

    const childDiv = document.createElement('div');
    childDiv.classList.add('toast-header');

    const childStrong = document.createElement('strong');
    childStrong.classList.add('me-auto');
    childStrong.innerText = message;

    childDiv.appendChild(childStrong);
    parentDiv.appendChild(childDiv);

    alramArea.appendChild(parentDiv);

    setTimeout(()=>{
        const joinAlram = document.getElementById(type + '_' + userId);
        joinAlram.parentNode.removeChild(joinAlram);
    },3000);
}

function handleEnter(signal){
    const messageList = signal.data;
    messageList.forEach(messageInfo=>{
        printChatMessage(messageInfo.sender, messageInfo.message, currentUserId === messageInfo.sender,true);
    })
}

function handleChat(signal){
    printChatMessage(signal.sender, signal.data, false,true);
    if(!chatFlag){
        showAlram("show_message", signal.sender, signal.sender + "님의 메시지를 확인해주세요.");
    }
}

// 대화창에 메시지 띄움
function printChatMessage(sender, message, isMy, isLoad){
    let childDiv = document.createElement('div');
    childDiv.classList.add('media-body');

    // 나의 메시지
    if(isMy){
        childDiv.style.float = 'right';
    }else{
        let childTitle = document.createElement('h6');
        childTitle.innerText = sender;

        childDiv.appendChild(childTitle);
    }

    let childP = document.createElement('p');
    childP.innerText = message;

    childDiv.appendChild(childP);

    let parentDiv = document.createElement("div");
    parentDiv.classList.add('media');
    parentDiv.appendChild(childDiv);

    chatMessageArea.appendChild(parentDiv);
    if(!isLoad){
        chatFelid.value = '';
        chatFelid.focus();
    }
    chatMessageArea.scrollTop = chatMessageArea.scrollHeight;
}

function handleLogout(signal) {
    var peerId = "video_" + signal.sender;
    delete connections[signal.sender];
    var videoElement = document.getElementById(peerId);
    videoElement.outerHTML = "";

    document.getElementById('joiner_' + signal.sender).outerHTML = '';
    showAlram('logout', signal.sender, signal.sender + "님이 퇴장하셨습니다.");
    joinerCount.innerText = parseInt(joinerCount.innerText) - 1;

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

function getRTCPeerConnectionObject(userId) {
    if(connections[userId]) {
        return connections[userId];
    }

    let connection = new RTCPeerConnection(peerConnectionConfig);

    connection.addStream(localStream);

    connection.onicecandidate = function (event) {
        console.log("candidate is: " + event.candidate);
        if (event.candidate) {
            sendMessage({
                type: "ice",
                receiver: userId,
                data: event.candidate
            });
        }
    };

    connection.onaddstream = function(event) {
        console.log('Received new stream from ' + userId);
        const video = document.createElement("video");
        video.classList.add('col-3');
        video.addEventListener('click', ()=>{
            container.style.display = 'none';
            bigSizeVideoContainer.style.display = 'block';
            bigSizeVideo.srcObject = event.stream;
        });
        container.appendChild(video);
        video.id = "video_" + userId;
        video.autoplay = true;
        video.srcObject = event.stream;

        const parentLi = document.createElement('li');
        parentLi.classList.add('list-group-item');
        parentLi.id = 'joiner_' + userId;
        parentLi.innerText = userId;

        joinerList.appendChild(parentLi);
        joinerCount.innerText = parseInt(joinerCount.innerText) + 1;
    };

    connections[userId] = connection;
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