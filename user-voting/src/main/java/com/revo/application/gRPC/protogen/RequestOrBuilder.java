// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: vote-status-service

package com.revo.application.gRPC.protogen;

public interface RequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:com.revo.application.gRPC.protogen.Request)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string vote_id = 1;</code>
   * @return The voteId.
   */
  java.lang.String getVoteId();
  /**
   * <code>string vote_id = 1;</code>
   * @return The bytes for voteId.
   */
  com.google.protobuf.ByteString
      getVoteIdBytes();

  /**
   * <code>int32 candidate_position = 2;</code>
   * @return The candidatePosition.
   */
  int getCandidatePosition();
}