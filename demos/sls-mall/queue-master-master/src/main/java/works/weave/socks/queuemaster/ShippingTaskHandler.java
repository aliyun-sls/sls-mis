package works.weave.socks.queuemaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import works.weave.socks.shipping.entities.Shipment;

@Component
public class ShippingTaskHandler {

	@Autowired
	DockerSpawner docker;

	public void handleMessage(Shipment shipment) {
		System.out.println("Received shipment task: " + shipment.getName());
		//docker.init();
		//docker.spawn();
	}

	public void onshipping(Shipment shipment){
		System.out.println("---------onshipping-------------");
		System.out.println(shipment.toString());
	}
}
