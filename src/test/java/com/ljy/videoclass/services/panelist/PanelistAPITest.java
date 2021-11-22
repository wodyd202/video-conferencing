package com.ljy.videoclass.services.panelist;

import com.ljy.videoclass.services.panelist.domain.Panelist;
import com.ljy.videoclass.services.panelist.domain.PanelistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PanelistAPITest {
    @Autowired
    protected PanelistRepository panelistRepository;

    public void newPanelist(Panelist.PanelistBuilder panelistBuilder){
        Panelist panelist = panelistBuilder.build();
        panelistRepository.save(panelist);
    }
}
