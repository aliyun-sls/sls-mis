import base64

from locust import HttpUser, TaskSet, task
from random import randint, choice



class Web(HttpUser):
    min_wait = 1000
    max_wait = 5000
    @task
    def load(self):
        base64string = base64.b64encode(('%s:%s' % ('user', 'password')).encode('utf-8')).decode('utf-8').replace('\n', '')

        resp = self.client.get("/catalogue")
        #print(resp.content)
        catalogue = resp.json()
        category_item = choice(catalogue)
        item_id = category_item["id"]

        self.client.get("/")
        self.client.get("/login", headers={"Authorization":"Basic %s" % base64string})
        self.client.get("/category.html")
        self.client.get("/detail.html?id={}".format(item_id))
        self.client.delete("/cart")
        self.client.post("/cart", json={"id": item_id, "quantity": 1})
        self.client.get("/basket.html")
        self.client.post("/orders")
