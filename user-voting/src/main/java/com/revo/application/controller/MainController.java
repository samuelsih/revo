package com.revo.application.controller;

import com.revo.application.entity.Voting;
import com.revo.application.response.Response;
import com.revo.application.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MainController {
    private final VotingService service;

    @Autowired
    public MainController(VotingService service) {
        this.service = service;
    }

    @PostMapping("/insert")
    public ResponseEntity<Object> store(@RequestBody Voting voting) {
        this.service.saveVote(voting);
        return Response.make(HttpStatus.CREATED, "Vote created!");
    }
}
