package com.ljy.videoclass.services.elrolment.command.presentation;

import com.ljy.videoclass.services.elrolment.command.application.ElrolmentService;
import com.ljy.videoclass.services.elrolment.domain.read.ElrolmentModel;
import com.ljy.videoclass.services.elrolment.domain.value.ClassroomCode;
import com.ljy.videoclass.services.elrolment.domain.value.Requester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * 수강신청 API
 */
@RestController
@RequestMapping("api/elrolment/{classroomCode}")
public class ElrolmentAPI {
    @Autowired
    private ElrolmentService elrolmentService;

    /**
     * # 수강신청
     */
    @PostMapping
    public ResponseEntity<ElrolmentModel> request(@PathVariable ClassroomCode classroomCode, Principal principal){
        ElrolmentModel request = elrolmentService.request(classroomCode, Requester.of(principal.getName()));
        return ResponseEntity.ok(request);
    }

    @DeleteMapping
    public ResponseEntity<String> cencel(@PathVariable ClassroomCode classroomCode, Principal principal){
        elrolmentService.cencel(classroomCode, Requester.of(principal.getName()));
        return ResponseEntity.ok("수강신청 철회 완료");
    }

}
