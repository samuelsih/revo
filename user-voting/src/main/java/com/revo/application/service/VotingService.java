package com.revo.application.service;

import com.revo.application.dto.VotingDTO;
import com.revo.application.entity.User;
import com.revo.application.exception.ExpiredVotingTimeException;
import com.revo.application.exception.VotingNotFoundException;
import com.revo.application.exception.VotingServerErrorException;

public interface VotingService {
    String saveVote(VotingDTO dto, User user)
            throws ExpiredVotingTimeException,
            VotingNotFoundException,
            VotingServerErrorException;
    Object getListTables();
}
