package com.revo.application.gRPC.protogen;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.48.1)",
    comments = "Source: vote-status-service")
@io.grpc.stub.annotations.GrpcGenerated
public final class VoteStatusServiceGrpc {

  private VoteStatusServiceGrpc() {}

  public static final String SERVICE_NAME = "com.revo.application.gRPC.protogen.VoteStatusService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.revo.application.gRPC.protogen.Request,
      com.revo.application.gRPC.protogen.Response> getCheckStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CheckStatus",
      requestType = com.revo.application.gRPC.protogen.Request.class,
      responseType = com.revo.application.gRPC.protogen.Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.revo.application.gRPC.protogen.Request,
      com.revo.application.gRPC.protogen.Response> getCheckStatusMethod() {
    io.grpc.MethodDescriptor<com.revo.application.gRPC.protogen.Request, com.revo.application.gRPC.protogen.Response> getCheckStatusMethod;
    if ((getCheckStatusMethod = VoteStatusServiceGrpc.getCheckStatusMethod) == null) {
      synchronized (VoteStatusServiceGrpc.class) {
        if ((getCheckStatusMethod = VoteStatusServiceGrpc.getCheckStatusMethod) == null) {
          VoteStatusServiceGrpc.getCheckStatusMethod = getCheckStatusMethod =
              io.grpc.MethodDescriptor.<com.revo.application.gRPC.protogen.Request, com.revo.application.gRPC.protogen.Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CheckStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.revo.application.gRPC.protogen.Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.revo.application.gRPC.protogen.Response.getDefaultInstance()))
              .setSchemaDescriptor(new VoteStatusServiceMethodDescriptorSupplier("CheckStatus"))
              .build();
        }
      }
    }
    return getCheckStatusMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static VoteStatusServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<VoteStatusServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<VoteStatusServiceStub>() {
        @java.lang.Override
        public VoteStatusServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new VoteStatusServiceStub(channel, callOptions);
        }
      };
    return VoteStatusServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static VoteStatusServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<VoteStatusServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<VoteStatusServiceBlockingStub>() {
        @java.lang.Override
        public VoteStatusServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new VoteStatusServiceBlockingStub(channel, callOptions);
        }
      };
    return VoteStatusServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static VoteStatusServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<VoteStatusServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<VoteStatusServiceFutureStub>() {
        @java.lang.Override
        public VoteStatusServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new VoteStatusServiceFutureStub(channel, callOptions);
        }
      };
    return VoteStatusServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class VoteStatusServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void checkStatus(com.revo.application.gRPC.protogen.Request request,
        io.grpc.stub.StreamObserver<com.revo.application.gRPC.protogen.Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCheckStatusMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getCheckStatusMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                com.revo.application.gRPC.protogen.Request,
                com.revo.application.gRPC.protogen.Response>(
                  this, METHODID_CHECK_STATUS)))
          .build();
    }
  }

  /**
   */
  public static final class VoteStatusServiceStub extends io.grpc.stub.AbstractAsyncStub<VoteStatusServiceStub> {
    private VoteStatusServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected VoteStatusServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new VoteStatusServiceStub(channel, callOptions);
    }

    /**
     */
    public void checkStatus(com.revo.application.gRPC.protogen.Request request,
        io.grpc.stub.StreamObserver<com.revo.application.gRPC.protogen.Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCheckStatusMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class VoteStatusServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<VoteStatusServiceBlockingStub> {
    private VoteStatusServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected VoteStatusServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new VoteStatusServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.revo.application.gRPC.protogen.Response checkStatus(com.revo.application.gRPC.protogen.Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCheckStatusMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class VoteStatusServiceFutureStub extends io.grpc.stub.AbstractFutureStub<VoteStatusServiceFutureStub> {
    private VoteStatusServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected VoteStatusServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new VoteStatusServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.revo.application.gRPC.protogen.Response> checkStatus(
        com.revo.application.gRPC.protogen.Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCheckStatusMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CHECK_STATUS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final VoteStatusServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(VoteStatusServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CHECK_STATUS:
          serviceImpl.checkStatus((com.revo.application.gRPC.protogen.Request) request,
              (io.grpc.stub.StreamObserver<com.revo.application.gRPC.protogen.Response>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class VoteStatusServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    VoteStatusServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.revo.application.gRPC.protogen.VoteStatusServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("VoteStatusService");
    }
  }

  private static final class VoteStatusServiceFileDescriptorSupplier
      extends VoteStatusServiceBaseDescriptorSupplier {
    VoteStatusServiceFileDescriptorSupplier() {}
  }

  private static final class VoteStatusServiceMethodDescriptorSupplier
      extends VoteStatusServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    VoteStatusServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (VoteStatusServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new VoteStatusServiceFileDescriptorSupplier())
              .addMethod(getCheckStatusMethod())
              .build();
        }
      }
    }
    return result;
  }
}
