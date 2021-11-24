package com.ljy.videoclass.services.panelist.command.infrastructure;

import com.ljy.videoclass.services.panelist.domain.Panelist;
import com.ljy.videoclass.services.panelist.domain.value.PanelistId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.ljy.videoclass.services.panelist.domain.PanelistFixture.aPanelist;
import static org.junit.Assert.assertTrue;

@SpringBootTest
public class QuerydslPanelistRepository_Test {
    @Autowired
    private QuerydslPanelistRepository querydslPanelistRepository;

    // 데이터베이스에 회의자 저장 후 바로 가져옴
    @Test
    void findById(){
        // given
        querydslPanelistRepository.save(aPanelist().id(PanelistId.of("persistPanelist")).build());

        // when
        Optional<Panelist> panelistOptional = querydslPanelistRepository.findById(PanelistId.of("persistPanelist"));

        // then
        assertTrue(panelistOptional.isPresent());
    }
}
