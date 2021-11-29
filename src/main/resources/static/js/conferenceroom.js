let ws;
let participants = {};
const QUERY_STRING = getQueryStringObject();
const CONFERENCE_CODE = QUERY_STRING.code;

const currentPanelistId = document.getElementById('currentPanelistId').value;

let currentLayout = 'col-3';
let cameraFlag = true;
let micFlag = true;

init(CONFERENCE_CODE);
function init(code){
	ws = new WebSocket('wss://' + location.host + '/conference/' + code);

	ws.onmessage = function(message) {
		var parsedMessage = JSON.parse(message.data);
		switch (parsedMessage.id) {
			case 'existingPanelists':
				onExistingParticipants(parsedMessage);
				break;
			case 'newPanelistArrived':
				onNewParticipant(parsedMessage);
				break;
			case 'panelistLeft':
				onParticipantLeft(parsedMessage);
				break;
			case 'receiveVideoAnswer':
				receiveVideoResponse(parsedMessage);
				break;
			case "conferenceInfo":
				document.getElementById('conferenceCode').innerText = code;
				document.getElementById('conferenceDate').innerText = parsedMessage.createDateTime;
				document.getElementById('conferenceCreator').innerText = parsedMessage.creator;
				break;
			case 'beforeMessage':
				parsedMessage.messages.forEach(message=>printChatMessage(message.sender, message.message, message.sender === currentPanelistId));
				break;
			case "chat":
				if(parsedMessage.sender !== currentPanelistId && !chatFlag){
					showAlram('chat', parsedMessage.sender, parsedMessage.sender + "님의 메시지를 확인해주세요.");
				}
				printChatMessage(parsedMessage.sender, parsedMessage.message, parsedMessage.sender === currentPanelistId);
				break;
			case "shake":
				handleShake(parsedMessage.sender);
				break;
			case "expel":
				alert('추방 당했습니다.');
				location.href = './main';
				break;
			case 'iceCandidate':
				participants[parsedMessage.name].rtcPeer.addIceCandidate(parsedMessage.candidate, function (error) {
					if (error) {
						console.error("Error adding candidate: " + error);
						return;
					}
				});
				break;
			default:
				console.error('Unrecognized message', parsedMessage);
		}
	}
}

// 화면 공유
shareScreen.addEventListener('click', ()=>{
	navigator.mediaDevices.getDisplayMedia({
		audio: micFlag,
		video: cameraFlag
	}).then(function(stream){
		const screenTrack = stream.getVideoTracks()[0];
		for(let panelistId in participants){
			let senders = participants[panelistId].rtcPeer.peerConnection.getSenders().find(s=>{
				return s.track.kind == screenTrack.kind;
			});
			senders.replaceTrack(screenTrack);
		}
		shareScreen.style.display = 'none';
		originScreen.style.display = 'block';
	}).catch(function(e){
		console.log("Stream NOT OK: " + e.name + ': ' + e.message);
	});
});

// 화면 공유 해제
originScreen.addEventListener('click', ()=>{
	navigator.mediaDevices.getUserMedia({
		audio: micFlag,
		video: cameraFlag
	}).then(function(stream){
		const screenTrack = stream.getVideoTracks()[0];
		for(let panelistId in participants){
			let senders = participants[panelistId].rtcPeer.peerConnection.getSenders().find(s=>{
				return s.track.kind == screenTrack.kind;
			});
			senders.replaceTrack(screenTrack);
		}
		shareScreen.style.display = 'block';
		originScreen.style.display = 'none';
	}).catch(function(e){
		console.log("Stream NOT OK: " + e.name + ': ' + e.message);
	});
});

// 카메라
document.getElementById("camera").addEventListener("click", ()=>{
	cameraFlag = !cameraFlag;
	participants[currentPanelistId].rtcPeer.videoEnabled = cameraFlag;
});

// 마이크
document.getElementById("mic").addEventListener("click", ()=>{
	micFlag = !micFlag;
	participants[currentPanelistId].rtcPeer.audioEnabled = micFlag;
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

// 흔들기 버튼 클릭
shakeBtn.addEventListener('click', ()=>{
	sendMessage({
		id : 'shake'
	});
});

// 회의에서 나가기
document.getElementById('exitBtn').addEventListener('click', ()=>{
	location.href = './main';
});


// 코드 복사
document.getElementById('copyCodeBtn').addEventListener('click', ()=>{
	const copy = document.getElementById('copyInput');
	copy.style.display = 'block';
	copy.value = document.getElementById('conferenceCode').innerText;
	copy.select();
	document.execCommand("Copy");
	copy.style.display = 'none';
});

function expel(name){
	sendMessage({
		id : "expel",
		targetPanelistId : name
	})
}

function handleShake(name){
	showAlram('shake', name, name + "님이 손을 흔드셨습니다.");
	const video = document.getElementById('video-' + name);
	video.classList.add('shake');

	setTimeout(()=>{
		video.classList.remove('shake');
	},2000);
}

function showAlram(type, panelistId, message){
	const parentDiv = document.createElement('div');
	parentDiv.classList.add('toast');
	parentDiv.classList.add('show');
	parentDiv.id = type + '_' + panelistId;

	const childDiv = document.createElement('div');
	childDiv.classList.add('toast-header');

	const childStrong = document.createElement('strong');
	childStrong.classList.add('me-auto');
	childStrong.innerText = message;

	childDiv.appendChild(childStrong);
	parentDiv.appendChild(childDiv);

	alramArea.appendChild(parentDiv);

	setTimeout(()=>{
		const joinAlram = document.getElementById(type + '_' + panelistId);
		joinAlram.parentNode.removeChild(joinAlram);
	},3000);
}

function onNewParticipant(request) {
	receiveVideo(request.name);
	showAlram('chat', request.name, request.name + "님이 입장했습니다.");
}

function receiveVideoResponse(result) {
	participants[result.name].rtcPeer.processAnswer (result.sdpAnswer, function (error) {
		if (error) return console.error (error);
	});
}

function onExistingParticipants(msg) {
	var constraints = {
		audio : true,
		video : true
	};
	var participant = new Participant(currentPanelistId);
	participants[currentPanelistId] = participant;
	var video = participant.getVideoElement();

	var options = {
	      localVideo: video,
	      mediaConstraints: constraints,
	      onicecandidate: participant.onIceCandidate.bind(participant)
	}
	participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendrecv(options,
		function (error) {
		  if(error) {
			  return console.error(error);
		  }
		  this.generateOffer (participant.offerToReceiveVideo.bind(participant));
	});

	msg.data.forEach(receiveVideo);
}

function leaveRoom() {
	sendMessage({
		id : 'leaveRoom'
	});

	for ( var key in participants) {
		participants[key].dispose();
	}

	document.getElementById('join').style.display = 'block';
	document.getElementById('room').style.display = 'none';

	ws.close();
}

function receiveVideo(sender) {
	var participant = new Participant(sender);
	participants[sender] = participant;
	var video = participant.getVideoElement();

	var options = {
      remoteVideo: video,
      onicecandidate: participant.onIceCandidate.bind(participant)
    }

	participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendrecv(options,
			function (error) {
			  if(error) {
				  return console.error(error);
			  }
			this.generateOffer (participant.offerToReceiveVideo.bind(participant));
	});;
}

function onParticipantLeft(request) {
	console.log('Participant ' + request.name + ' left');
	var participant = participants[request.name];
	const panelist = document.getElementById('joiner-' + request.name);
	panelist.parentNode.removeChild(panelist);
	document.getElementById('joinerCount').innerText = parseInt(document.getElementById('joinerCount').innerText) - 1;
	participant.dispose();
	delete participants[request.name];
	showAlram('chat', request.name, request.name + "님이 퇴장했습니다.");
}

function sendMessage(message) {
	ws.send(JSON.stringify(message));
}

window.onbeforeunload = function() {
	ws.close();
};

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
