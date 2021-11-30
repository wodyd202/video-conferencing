let chatFlag = false;

// 채팅 보내기
// 채팅중 엔터키 입력
chatFelid.addEventListener('keyup', (event)=>{
    const message = chatFelid.value.trim();
    if(event.keyCode === 13 && message !== ''){
        sendMessage({
            id : 'chat',
            message : message
        });
        chatFelid.value = '';
    }
});

// 대화창에 메시지 띄움
function printChatMessage(sender, message, isMy){
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
    chatMessageArea.scrollTop = chatMessageArea.scrollHeight;
}

// 채팅창 활성화
chatAbleBtn.addEventListener('click', ()=>{
    chatFlag = !chatFlag;
    if(chatFlag){
        videoArea.classList.remove('col-12');
        videoArea.classList.add('col-8');
        chatArea.style.display = 'block';
        chatMessageArea.scrollTop = chatMessageArea.scrollHeight;
    }else{
        videoArea.classList.remove('col-8');
        videoArea.classList.add('col-12');
        chatArea.style.display = 'none';
    }
});