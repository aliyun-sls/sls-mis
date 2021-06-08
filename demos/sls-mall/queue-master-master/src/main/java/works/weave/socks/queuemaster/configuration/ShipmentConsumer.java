package works.weave.socks.queuemaster.configuration;

import works.weave.socks.shipping.entities.Shipment;

public interface ShipmentConsumer {

    void OnMessage(Shipment shipment);
}
