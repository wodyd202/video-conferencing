package com.ljy.videoclass.services.rtcConference.infratructure;

import com.ljy.videoclass.services.rtcConference.application.ChatMessageRepository;
import com.ljy.videoclass.services.rtcConference.model.ChatMessage;
import com.ljy.videoclass.services.rtcConference.model.ConferenceCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ChatMessageRepository_Test {
    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Test
    void 해당_회의에_누적된_메시지_개수_가져옴(){
        // given
        ConferenceCode conferenceCode = ConferenceCode.of("conferenceCode");
        for(int i =0;i<5;i++){
            chatMessageRepository.save(conferenceCode, ChatMessage.builder()
                    .message("MESSAGE")
                    .sender("SENDER")
                    .build());
        }

        // when
        long count = chatMessageRepository.countAll(conferenceCode);

        // then
        assertEquals(count, 5);
    }
}
