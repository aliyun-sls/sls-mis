package works.weave.socks.queuemaster.configuration;

import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;

abstract class DeliverConsumer implements Consumer {

    @Override
    public void handleConsumeOk(String consumerTag) {

    }

    @Override
    public void handleCancelOk(String consumerTag) {

    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {

    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {

    }

    @Override
    public void handleRecoverOk(String consumerTag) {

    }

}
