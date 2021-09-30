(function (){
  'use strict';

  var async     = require("async")
    , express   = require("express")
    , request   = require("request")
    , endpoints = require("../endpoints")
    , helpers   = require("../../helpers")
    , app       = express()
  const {context, trace} = require('@opentelemetry/api');

  app.get("/orders", function (req, res, next) {
    var logged_in = req.cookies.logged_in;
    if (!logged_in) {
      req.log.warn("用户没有登陆")
      throw new Error("User not logged in.");
      return
    }

    var custId = req.session.customerId;
    let childLogging = req.log.child({"cust_id": custId, 'operation': 'GetOrders'});
    childLogging.info({parameters: req.params, msg: '查看用户订单信息', url: req.url});
    var span = trace.getSpan(context.active());
    async.waterfall([
        function (callback) {
          let startTime = Math.floor(Date.now() / 1000);
          request(endpoints.ordersUrl + "/orders/search/customerId?sort=date&custId=" + custId, function (error, response, body) {
            childLogging.info({
              msg: "调用[order]：获取用户订单信息",
              isError: error,
              body: body,
              url: endpoints.ordersUrl + "/orders/search/customerId?sort=date&custId=" + custId,
              cost: Math.floor(Date.now() / 1000) - startTime
            })

            if (error) {
              return callback(error);
            }
            if (response.statusCode == 404) {
              return callback(null, []);
            }
            callback(null, JSON.parse(body)._embedded.customerOrders);
          });
        }
    ],
    function (err, result) {
      if (err) {
        return next(err);
      }
      helpers.respondStatusBody(res, 201, JSON.stringify(result), function (span){
        if (span) {
          return {'trace-id': span.spanContext().traceId}
        }
      }(span));
    });
  });

  app.get("/orders/*", function (req, res, next) {
    var url = endpoints.ordersUrl + req.url.toString();
    request.get(url).pipe(res);
  });

  app.post("/orders", function(req, res, next) {
    var logged_in = req.cookies.logged_in;
    if (!logged_in) {
      req.log.warn("用户没有登陆")
      throw new Error("User not logged in.");
      return
    }

    var custId = req.session.customerId;
    let childLogging = req.log.child({"cust_id": custId, 'operation': 'CreateOrder'});
    childLogging.info({parameters: req.params, msg: '查看用户订单信息', url: req.url});
    var span = trace.getSpan(context.active());
    async.waterfall([
        function (callback) {
          let startTime = Math.floor(Date.now() / 1000);
          request(endpoints.customersUrl + "/" + custId, function (error, response, body) {
            childLogging.info({
              msg: "调用[user]：校验用户是否存在",
              isError: error || body.status_code === 500,
              body: body,
              url: endpoints.customersUrl + "/" + custId,
              cost: Math.floor(Date.now() / 1000) - startTime
            })

            if (error || body.status_code === 500) {
              callback(error);
              return;
            }
            var jsonBody = JSON.parse(body);
            var customerlink = jsonBody._links.customer.href;
            var addressLink = jsonBody._links.addresses.href;
            var cardLink = jsonBody._links.cards.href;
            var order = {
              "customer": customerlink,
              "address": null,
              "card": null,
              "items": endpoints.cartsUrl + "/" + custId + "/items"
            };
            callback(null, order, addressLink, cardLink);
          });
        },
        function (order, addressLink, cardLink, callback) {
          async.parallel([
              function (callback) {
                let startTime = Math.floor(Date.now() / 1000);
                request.get(addressLink, function (error, response, body) {
                  childLogging.info({
                    msg: "调用[user]：校验用户地址是否存在",
                    isError: error,
                    body: body,
                    url: addressLink,
                    cost: Math.floor(Date.now() / 1000) - startTime
                  })

                  if (error) {
                    callback(error);
                    return;
                  }
                  var jsonBody = JSON.parse(body);
                  if (jsonBody.status_code !== 500 && jsonBody._embedded.address[0] != null) {
                    order.address = jsonBody._embedded.address[0]._links.self.href;
                  }
                  callback();
                });
              },
              function (callback) {
                let startTime = Math.floor(Date.now() / 1000);
                request.get(cardLink, function (error, response, body) {
                  childLogging.info({
                    msg: "调用[user]：校验用户银行信息",
                    isError: error,
                    body: body,
                    url: addressLink,
                    cost: Math.floor(Date.now() / 1000) - startTime
                  })

                  if (error) {
                    callback(error);
                    return;
                  }
                  var jsonBody = JSON.parse(body);
                  if (jsonBody.status_code !== 500 && jsonBody._embedded.card[0] != null) {
                    order.card = jsonBody._embedded.card[0]._links.self.href;
                  }
                  callback();
                });
              }
          ], function (err, result) {
            if (err) {
              callback(err);
              return;
            }
            callback(null, order);
          });
        },
        function (order, callback) {
          var options = {
            uri: endpoints.ordersUrl + '/orders',
            method: 'POST',
            json: true,
            body: order
          };
          let startTime = Math.floor(Date.now() / 1000);
          request(options, function (error, response, body) {
            childLogging.info({
              msg: "调用[user]：创建订单",
              isError: error,
              body: body,
              url: options.uri,
              parameters: options.body,
              cost: Math.floor(Date.now() / 1000) - startTime
            })

            if (error) {
              return callback(error);
            }
            callback(null, response.statusCode, body);
          });
        }
    ],
    function (err, status, result) {
      if (err) {
        return next(err);
      }
      helpers.respondStatusBody(res, status, JSON.stringify(result), function (span){
        if (span) {
          return {'trace-id': span.spanContext().traceId}
        }
      }(span));
    });
  });

  module.exports = app;
}());
