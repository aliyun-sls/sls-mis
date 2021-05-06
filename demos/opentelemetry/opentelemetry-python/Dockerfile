FROM python:3.9.4-slim

ENV ACCESS_KEY_ID="" \
    ACCESS_SECRET="" \
    PROJECT="" \
    LOGSTORE="" \
    HTTPS_ENDPOINT="" \
    SERVICE_NAME="opentelementry-python" \
    SERVICE_VERSION="1.1.0" \
    SERVICE_HOST="127.0.0.1"


WORKDIR /opentelemetry-python

RUN pip install poetry
COPY pyproject.toml poetry.lock startup.sh ./
COPY . .
RUN poetry install --no-dev

EXPOSE 8088

CMD ["bash", "startup.sh"]
