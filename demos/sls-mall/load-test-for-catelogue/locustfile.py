import base64

from locust import HttpUser, TaskSet, task
from random import randint, choice



class Web(HttpUser):
    min_wait = 15000
    max_wait = 30000
    @task
    def load(self):
        self.client.get("/tags")
