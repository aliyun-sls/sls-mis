ANTI_CHEATING = anti-cheating
CARTS_MASTER = carts-master
INTEGRAL_MASTER = integral-master
ORDERS_MASTER = orders-master
QUEUE_MASTER_MASTER = queue-master-master
SHIPPING_MASTER = shipping-master

MALL_PROJECTS = $(ANTI_CHEATING) $(CARTS_MASTER) $(INTEGRAL_MASTER) $(ORDERS_MASTER) $(QUEUE_MASTER_MASTER) $(SHIPPING_MASTER)

build_images:
	for i in $(MALL_PROJECTS) ; do ( cd $$i ; make build_images) ; done

push_images: build_images
	for i in $(MALL_PROJECTS) ; do ( cd $$i; make push_images) ; done
