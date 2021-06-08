package works.weave.socks.queuemaster.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import works.weave.socks.shipping.entities.Shipment;

public class ShipmentConsumerImpl implements ShipmentConsumer {

    private Logger logger = LoggerFactory.getLogger(ShipmentConsumerImpl.class);

    @Override
    public void OnMessage(Shipment shipment) {
        logger.info("Receive an Shipment: {}", shipment);
    }
}
