// Code generated by protoc-gen-go-grpc. DO NOT EDIT.
// versions:
// - protoc-gen-go-grpc v1.2.0
// - protoc             v3.6.1
// source: checker_voting.proto

package autogenerated

import (
	context "context"

	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
// Requires gRPC-Go v1.32.0 or later.
const _ = grpc.SupportPackageIsVersion7

// VoteStatusServiceClient is the client API for VoteStatusService service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type VoteStatusServiceClient interface {
	CheckStatus(ctx context.Context, in *Request, opts ...grpc.CallOption) (*Response, error)
}

type voteStatusServiceClient struct {
	cc grpc.ClientConnInterface
}

func NewVoteStatusServiceClient(cc grpc.ClientConnInterface) VoteStatusServiceClient {
	return &voteStatusServiceClient{cc}
}

func (c *voteStatusServiceClient) CheckStatus(ctx context.Context, in *Request, opts ...grpc.CallOption) (*Response, error) {
	out := new(Response)
	err := c.cc.Invoke(ctx, "/autogenerated.VoteStatusService/CheckStatus", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// VoteStatusServiceServer is the server API for VoteStatusService service.
// All implementations must embed UnimplementedVoteStatusServiceServer
// for forward compatibility
type VoteStatusServiceServer interface {
	CheckStatus(context.Context, *Request) (*Response, error)
	mustEmbedUnimplementedVoteStatusServiceServer()
}

// UnimplementedVoteStatusServiceServer must be embedded to have forward compatible implementations.
type UnimplementedVoteStatusServiceServer struct {
}

func (UnimplementedVoteStatusServiceServer) CheckStatus(context.Context, *Request) (*Response, error) {
	return nil, status.Errorf(codes.Unimplemented, "method CheckStatus not implemented")
}
func (UnimplementedVoteStatusServiceServer) mustEmbedUnimplementedVoteStatusServiceServer() {}

// UnsafeVoteStatusServiceServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to VoteStatusServiceServer will
// result in compilation errors.
type UnsafeVoteStatusServiceServer interface {
	mustEmbedUnimplementedVoteStatusServiceServer()
}

func RegisterVoteStatusServiceServer(s grpc.ServiceRegistrar, srv VoteStatusServiceServer) {
	s.RegisterService(&VoteStatusService_ServiceDesc, srv)
}

func _VoteStatusService_CheckStatus_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(Request)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(VoteStatusServiceServer).CheckStatus(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/autogenerated.VoteStatusService/CheckStatus",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(VoteStatusServiceServer).CheckStatus(ctx, req.(*Request))
	}
	return interceptor(ctx, in, info, handler)
}

// VoteStatusService_ServiceDesc is the grpc.ServiceDesc for VoteStatusService service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var VoteStatusService_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "autogenerated.VoteStatusService",
	HandlerType: (*VoteStatusServiceServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "CheckStatus",
			Handler:    _VoteStatusService_CheckStatus_Handler,
		},
	},
	Streams:  []grpc.StreamDesc{},
	Metadata: "checker_voting.proto",
}
