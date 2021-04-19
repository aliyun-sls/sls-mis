[![Build Status](https://travis-ci.org/microservices-demo/front-end.svg?branch=master)](https://travis-ci.org/microservices-demo/front-end)
[![](https://images.microbadger.com/badges/image/weaveworksdemos/front-end.svg)](http://microbadger.com/images/weaveworksdemos/front-end "Get your own image badge on microbadger.com")
[![Actions Status](https://github.com/microservices-demo/front-end/workflows/ci/badge.svg)](https://github.com/microservices-demo/front-end/workflows/ci/badge.svg)


Front-end app
---
Front-end application written in [Node.js](https://nodejs.org/en/) that puts together all of the microservices under [microservices-demo](https://github.com/microservices-demo/microservices-demo).

# Build

## Dependencies

<table>
  <thead>
    <tr>
      <th>Name</th>
      <th>Version</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td><a href="https://docker.com">Docker</a></td>
      <td>>= 1.12</td>
    </tr>
    <tr>
      <td><a href="https://docs.docker.com/compose/">Docker Compose</a></td>
      <td>>= 1.8.0</td>
    </tr>
    <tr>
      <td><a href="gnu.org/s/make">Make</a>&nbsp;(optional)</td>
      <td>>= 4.1</td>
    </tr>
  </tbody>
</table>

## Node

`npm install`

## Docker

`make test-image`

## Docker Compose

`make up`

# Test

**Make sure that the microservices are up & running**

## Unit & Functional tests:

```
make test
```

## End-to-End tests:
  
To make sure that the test suite is running against the latest (local) version with your changes, you need to manually build
the image, run the container and attach it to the proper Docker networks.
There is a make task that will do all this for you:

```
make dev
```

That will also tail the logs of the container to make debugging easy.
Then you can run the tests with:

```
make e2e
```

# Run

## Node

`npm start`

## Docker

`make server`

# Use

## Node

`curl http://localhost:8081`

## Docker Compose

`curl http://localhost:8080`

# Push

`GROUP=weaveworksdemos COMMIT=test ./scripts/push.sh`
