package com.ljy.videoclass.services.panelist.query.infrastructure;

import com.ljy.videoclass.services.panelist.domain.Panelist;
import com.ljy.videoclass.services.panelist.domain.model.PanelistModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.ljy.videoclass.services.panelist.domain.PanelistFixture.aPanelist;
import static org.junit.Assert.assertTrue;

@SpringBootTest
public class RedisQueryPanelistRepository_Test {
    @Autowired
    RedisQueryPanelistRepository panelistRepository;

    @Test
    void 모델_저장후_다시_가져옴(){
        // given
        Panelist panelist = aPanelist().build();
        PanelistModel panelistModel = panelist.toModel();

        // when
        panelistRepository.save(panelistModel);
        Optional<PanelistModel> panelistModelOptional = panelistRepository.findById(panelistModel.getEmail());

        // then
        assertTrue(panelistModelOptional.isPresent());
    }
}
