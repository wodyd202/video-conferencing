package com.ljy.videoclass.services.panelist.query.presentation;

import com.ljy.videoclass.services.panelist.domain.model.PanelistModel;
import com.ljy.videoclass.services.panelist.query.infrastructure.RedisQueryPanelistRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/panelist")
@AllArgsConstructor
public class PanelistSearchAPI {
    private RedisQueryPanelistRepository panelistRepository;

    @GetMapping
    public ResponseEntity<PanelistModel> getPanelist(Principal principal){
        PanelistModel panelistModel = panelistRepository.findById(principal.getName()).get();
        panelistModel.emptyPassword();
        return ResponseEntity.ok(panelistModel);
    }

}
