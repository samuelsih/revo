package com.revo.application.controller;

import com.revo.application.dto.VotingDTO;
import com.revo.application.entity.User;
import com.revo.application.response.Response;
import com.revo.application.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class MainController {
    private final VotingService service;

    @Autowired
    public MainController(VotingService service) {
        this.service = service;
    }

    @PostMapping("/vote")
    public ResponseEntity<Object> storeVote(
            @RequestAttribute("user") User user,
            @Validated VotingDTO dto
    ) {
        this.service.saveVote(dto, user);
        return Response.make(HttpStatus.CREATED, "Vote has been created");
    }

    @GetMapping("/_ah/warmup")
    public ResponseEntity<Object> appEngineWarmup() {
        var result = this.service.getListTables();
        return Response.make(HttpStatus.OK, "OK", result);
    }

    @GetMapping("/warmup")
    public ResponseEntity<Object> keepAlive() {
        var result = this.service.getListTables();
        return Response.make(HttpStatus.OK, "OK", result);
    }
}
