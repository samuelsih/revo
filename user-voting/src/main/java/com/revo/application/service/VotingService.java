package com.revo.application.service;

import com.revo.application.dto.VotingDTO;
import com.revo.application.entity.User;

public interface VotingService {
    public void saveVote(VotingDTO dto, User user);
    public Object getListTables();
}
