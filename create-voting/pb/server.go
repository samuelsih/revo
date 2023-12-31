package pb

import (
	ag "github.com/samuelsih/revo-voting/pb/autogenerated"
	"google.golang.org/grpc"
	"google.golang.org/grpc/reflection"
)

func Server(votingThemeFinder VotingThemeFinder) *grpc.Server {
	server := grpc.NewServer()
	service := CheckerVotingService{VotingThemeFinder: votingThemeFinder}

	ag.RegisterVoteStatusServiceServer(server, &service)
	reflection.Register(server)

	return server
}
