FROM python:3-slim

ENV ACCESS_KEY_ID="" \
    ACCESS_KEY_SECRET="" \
    PROJECT="" \
    LOGSTORE="" \
    ENDPOINT="" \
    SERVICE_NAME="opentelementry-python-demo" \
    SERVICE_VERSION="1.0.0" \
    SERVICE_HOST="127.0.0.1"


WORKDIR /opentelemetry-python

RUN pip install poetry
COPY pyproject.toml poetry.lock startup.sh ./
COPY . .
RUN poetry install --no-dev

EXPOSE 8087

CMD ["bash", "startup.sh"]
