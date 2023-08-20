package com.revo.application.app;

import com.revo.application.dto.VotingDTO;
import com.revo.application.entity.Status;
import com.revo.application.entity.User;
import com.revo.application.entity.VoteData;
import com.revo.application.entity.Voting;
import com.revo.application.exception.ExpiredVotingTimeException;
import com.revo.application.exception.VotingNotFoundException;
import com.revo.application.exception.VotingServerErrorException;
import com.revo.application.gRPC.client.VoteStatusServiceClient;
import com.revo.application.repository.VotingRepository;
import com.revo.application.service.VotingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
public class VotingServiceTest {
    @Mock
    private VotingRepository repository;

    @Mock
    private VoteStatusServiceClient grpcClient;

    private VotingServiceImpl service;

    @BeforeEach
    public void setUp() {
        service = new VotingServiceImpl(repository, grpcClient);
    }

    @Test
    public void testGetListTables() {
        var expected = new HashMap<String, Object>();
        expected.put("some", "thing");

        Mockito.when(repository.getListTables()).thenReturn(expected);
        var result = service.getListTables();

        Mockito.verify(repository, Mockito.times(1)).getListTables();
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testStoreThrowExpiredVotingTimeException()  {
        var dto = new VotingDTO();
        dto.setVoteId("12312323");
        dto.setChoose(2);

        var user = new User();
        user.setUserId("456456456");
        user.setName("hama");
        user.setEmail("hahahihi@mail.com");

        Mockito.when(grpcClient.checkVoteStatus(dto.getVoteId(), dto.getChoose()))
                .thenReturn(new VoteData(Status.EXPIRED, "joko", "widodo", "google.com"));

        Assertions.assertThrows(ExpiredVotingTimeException.class, () -> {
            service.saveVote(dto, user);
        });
    }

    @Test
    public void testStoreThrowVotingNotFoundException() {
        var dto = new VotingDTO();
        dto.setVoteId("12312323");
        dto.setChoose(2);

        var user = new User();
        user.setUserId("456456456");
        user.setName("hama");
        user.setEmail("hahahihi@mail.com");

        Mockito.when(grpcClient.checkVoteStatus(dto.getVoteId(), dto.getChoose()))
                .thenReturn(new VoteData(Status.NOT_FOUND, "joko", "widodo", "google.com"));

        Assertions.assertThrows(VotingNotFoundException.class, () -> {
            service.saveVote(dto, user);
        });
    }

    @Test
    public void testStoreThrowVotingServerErrorException() {
        var dto = new VotingDTO();
        dto.setVoteId("12312323");
        dto.setChoose(2);

        var user = new User();
        user.setUserId("456456456");
        user.setName("hama");
        user.setEmail("hahahihi@mail.com");

        Mockito.when(grpcClient.checkVoteStatus(dto.getVoteId(), dto.getChoose()))
                .thenReturn(new VoteData(Status.SERVER_ERROR, "joko", "widodo", "google.com"));

        Assertions.assertThrows(VotingServerErrorException.class, () -> {
            service.saveVote(dto, user);
        });
    }

    @Test
    public void testStoreSuccess() throws Exception {
        var dto = new VotingDTO();
        dto.setVoteId("12312323");
        dto.setChoose(2);

        var user = new User();
        user.setUserId("456456456");
        user.setName("hama");
        user.setEmail("hahahihi@mail.com");

        var voteData = new VoteData(Status.OK, "joko", "widodo", "google.com");
        var voting = Voting.builder()
                .userId(user.getUserId())
                .voteId(dto.getVoteId())
                .voteTo(dto.getChoose())
                .voteAtUnix(Instant.now().getEpochSecond())
                .name(voteData.name())
                .description(voteData.description())
                .imgLink(voteData.imgLink())
                .build();

        Mockito.when(grpcClient.checkVoteStatus(dto.getVoteId(), dto.getChoose()))
                .thenReturn(voteData);

        var result = service.saveVote(dto, user);
        Mockito.verify(grpcClient, Mockito.times(1))
                .checkVoteStatus(dto.getVoteId(), dto.getChoose());
        Mockito.verify(repository, Mockito.times(1)).store(Mockito.any(voting.getClass()));

        Assertions.assertEquals(result, "ok");
    }
}
