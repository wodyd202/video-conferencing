package com.ljy.videoclass.services.rtcConference.application;

import com.ljy.videoclass.services.rtcConference.model.ChatMessage;
import com.ljy.videoclass.services.rtcConference.model.ConferenceCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ChatService_Test {
    @Autowired RTCChatService chatService;
    @Autowired ChatMessageRepository chatMessageRepository;

    @Test
    void 누적_메시지_개수가_80개가_되면_60개_제거_후_DB에_저장(){
        // given
        ConferenceCode conferenceCode = ConferenceCode.of("conferenceCode");
        for(int i =0;i<80;i++){
            chatService.persist(conferenceCode, ChatMessage.builder()
                    .sender("SENDER")
                    .message("MESSAGE")
                    .build());
        }

        // when
        long count = chatMessageRepository.countAll(conferenceCode);

        // then
        assertEquals(count, 20);
    }
}
