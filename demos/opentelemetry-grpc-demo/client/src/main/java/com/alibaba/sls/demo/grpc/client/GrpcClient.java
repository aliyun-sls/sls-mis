package com.alibaba.sls.demo.grpc.client;

import com.alibaba.sls.demo.grpc.helloworld.HelloReply;
import com.alibaba.sls.demo.grpc.helloworld.HelloRequest;
import com.alibaba.sls.demo.grpc.helloworld.StreamingGreeterGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class GrpcClient {
    private final StreamingGreeterGrpc.StreamingGreeterBlockingStub blockingStub;

    public GrpcClient(Channel channel) {
        blockingStub = StreamingGreeterGrpc.newBlockingStub(channel);
    }

    public void greet(String name) {
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        Iterator<HelloReply> response;
        try {
            response = blockingStub.sayHelloStreaming(request);

            response.forEachRemaining(new Consumer<HelloReply>() {
                @Override public void accept(HelloReply reply) {
                    System.out.print(reply.getMessage());
                }
            });
        } catch (StatusRuntimeException e) {
            return;
        }
    }

    public static void main(String[] args) throws Exception {
        String user = "world";
        String target = "localhost:50051";
        if (args.length > 0) {
            if ("--help".equals(args[0])) {
                System.err.println("Usage: [name [target]]");
                System.err.println("");
                System.err.println("  name    The name you wish to be greeted by. Defaults to " + user);
                System.err.println("  target  The server to connect to. Defaults to " + target);
                System.exit(1);
            }
            user = args[0];
        }
        if (args.length > 1) {
            target = args[1];
        }

        ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
        try {
            GrpcClient client = new GrpcClient(channel);
            client.greet(user);
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
