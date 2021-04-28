[![Build Status](https://travis-ci.org/microservices-demo/queue-master.svg?branch=master)](https://travis-ci.org/microservices-demo/queue-master)
[![Coverage Status](https://coveralls.io/repos/github/microservices-demo/queue-master/badge.svg?branch=master)](https://coveralls.io/github/microservices-demo/queue-master?branch=master)
[![](https://images.microbadger.com/badges/image/weaveworksdemos/queue-master.svg)](http://microbadger.com/images/weaveworksdemos/queue-master "Get your own image badge on microbadger.com")

# queue-master

A microservices-demo service that provides reading from the shipping
queue. It will spawn new docker containers that simulate the shipping
process.

This build is built, tested and released by travis.

# Test

`./test/test.sh < python testing file >`. For example: `./test/test.sh
unit.py`

# Build

`GROUP=weaveworksdemos COMMIT=test ./scripts/build.sh`

# Push

`GROUP=weaveworksdemos COMMIT=test ./scripts/push.sh`


## Redesign

This service will shortly be redesigned.

- Remove docker container shipping like functionality. Was only to make
  a better demonstration on Scope.
- Read object from queue and provide feedback on status
- See microservices-demo/orders and microservices-demo/shipping
