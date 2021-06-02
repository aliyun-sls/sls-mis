package works.weave.socks.queuemaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import works.weave.socks.shipping.entities.Shipment;

@Component
public class ShippingTaskHandler {

    private static Logger logger = LoggerFactory.getLogger(ShippingTaskHandler.class);

    @Autowired
    DockerSpawner docker;

    public void handleMessage(Shipment shipment) {
        logger.info("Received shipment: {}" + shipment);
    }

    public void onshipping(Shipment shipment) {
        logger.info("Received shipment: {}", shipment);
    }
}
