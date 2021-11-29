function Participant(name) {
	this.name = name;
	var video = document.createElement('video');

	var rtcPeer;
	video.id = 'video-' + name;
	video.autoplay = true;
	video.controls = false;
	video.classList.add(currentLayout);
	video.addEventListener('click', ()=>{
		video.webkitRequestFullscreen();
	});

	document.getElementById('remoteVideosContainer').appendChild(video);
	document.getElementById('joinerCount').innerText = parseInt(document.getElementById('joinerCount').innerText) + 1;
	const creator = document.getElementById('conferenceCreator').innerText;
	const joinerList = document.getElementById('joinerList');
	const liParent = document.createElement('li');
	liParent.id = "joiner-" + name;
	liParent.classList.add('list-group-item');
	let innerHTML = "";
	if(name === creator){
		innerHTML += `<h5><span class="badge bg-primary">${name} (방장)</span></h5>`;
	}else{
		innerHTML += name + " ";
		if(creator === currentPanelistId){
			innerHTML += `<button style='float: right' onclick="expel('${name}')">추방</button>`;
		}
	}

	liParent.innerHTML = innerHTML;

	joinerList.appendChild(liParent);

	this.getVideoElement = function() {
		return video;
	}

	this.offerToReceiveVideo = function(error, offerSdp, wp){
		var msg =  {
				id : "receiveVideoFrom",
				sender : name,
				sdpOffer : offerSdp
			};
		sendMessage(msg);
	}


	this.onIceCandidate = function (candidate, wp) {
		  var message = {
		    id: 'onIceCandidate',
		    candidate: candidate,
		    name: name
		  };
		  sendMessage(message);
	}

	Object.defineProperty(this, 'rtcPeer', { writable: true});

	this.dispose = function() {
		this.rtcPeer.dispose();
		const videoElem = document.getElementById('video-' + name);
		videoElem.parentNode.removeChild(videoElem);
	};
}
