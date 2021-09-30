(function () {
    'use strict';

    var async = require("async")
        , express = require("express")
        , request = require("request")
        , helpers = require("../../helpers")
        , endpoints = require("../endpoints")
        , app = express()
    const {context, getSpan, trace} = require('@opentelemetry/api');

    // List items in cart for current logged in user.
    app.get("/cart", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'ListCart'});
        childLogging.info({parameters: req.params, msg: '查看购物车请求', url: req.url});
        var span = trace.getSpan(context.active());
        const startTime = Math.floor(Date.now() / 1000);
        request(endpoints.cartsUrl + "/" + custId + "/items", function (error, response, body) {
            childLogging.info({
                msg: '请求[Carts]后端查看购物车服务',
                url: endpoints.cartsUrl + "/" + custId + "/items",
                cost: Math.floor(Date.now() / 1000) - startTime,
                isError: error,
                body: body
            })
            if (error) {
                return next(error);
            }
            if (span) {
                helpers.respondStatusBody(res, response.statusCode, body, {'trace-id': span.spanContext().traceId})
            } else {
                helpers.respondStatusBody(res, response.statusCode, body)
            }

        });
    });

    // Delete cart
    app.delete("/cart", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'ClearCart'});
        childLogging.info({parameters: req.params, msg: '清空购物车请求', 'url': req.url});
        var options = {
            uri: endpoints.cartsUrl + "/" + custId,
            method: 'DELETE'
        };
        var span = trace.getSpan(context.active());

        const startTime = Math.floor(Date.now() / 1000);
        request(options, function (error, response, body) {
            childLogging.info({
                msg: '请求[Carts]后端清空购物车服务',
                url: options.uri,
                method: options.method,
                cost: Math.floor(Date.now() / 1000) - startTime,
                isError: error,
                body: body
            })

            if (error) {
                return next(error);
            }
            if (span) {
                helpers.respondStatus(res, response.statusCode, {'trace-id': span.spanContext().traceId});
            } else {
                helpers.respondStatus(res, response.statusCode)
            }
        });
    });

    // Delete item from cart
    app.delete("/cart/:id", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'DeleteProductFromCart'});
        if (req.params.id == null) {
            childLogging.warn({'exception': '无效参数，缺少产品ID参数'})
            return next(new Error("Must pass id of item to delete"), 400);
        }

        childLogging.info({
            parameters: req.params,
            msg: '删除购物车产品请求',
            url: req.url,
            parameter: {id: req.params.id.toString()}
        });
        var options = {
            uri: endpoints.cartsUrl + "/" + custId + "/items/" + req.params.id.toString(),
            method: 'DELETE'
        };

        const startTime = Math.floor(Date.now() / 1000);
        var span = trace.getSpan(context.active());
        request(options, function (error, response, body) {
            childLogging.info({
                msg: '请求[Carts]后端删除购物车产品服务',
                url: options.uri,
                method: options.method,
                cost: Math.floor(Date.now() / 1000) - startTime,
                isError: error,
                body: body
            })

            if (error) {
                if (span) {
                    helpers.respondStatus(res, response.statusCode, {'trace-id': span.spanContext().traceId});
                } else {
                    helpers.respondStatus(res, response.statusCode)
                }
            }
            if (span) {
                helpers.respondStatus(res, response.statusCode, {'trace-id': span.spanContext().traceId});
            } else {
                helpers.respondStatus(res, response.statusCode)
            }
        });
    });

    // Add new item to cart
    app.post("/cart", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'AddProductToCart'});
        if (req.body.id == null) {
            childLogging.warn({'exception': '无效参数，缺少产品ID参数'})
            next(new Error("Must pass id of item to add"), 400);
            return;
        }


        var span = trace.getSpan(context.active());

        let startTime = Math.floor(Date.now() / 1000);
        async.waterfall([
            function (callback) {
                request(endpoints.catalogueUrl + "/catalogue/" + req.body.id.toString(), function (error, response, body) {
                    childLogging.info({
                        msg: '请求[catalogue]后端产品详情服务',
                        cost: Math.floor(Date.now() / 1000) - startTime,
                        isError: error,
                        body: body
                    })
                    startTime = Math.floor(Date.now() / 1000)
                    callback(error, JSON.parse(body));
                });
            },
            function (item, callback) {
                var options = {
                    uri: endpoints.cartsUrl + "/" + custId + "/items",
                    method: 'POST',
                    json: true,
                    body: {itemId: item.id, unitPrice: item.price}
                };
                request(options, function (error, response, body) {
                    childLogging.info({
                        msg: '请求[cart]后端添加产品到购物车服务',
                        cost: Math.floor(Date.now() / 1000) - startTime,
                        isError: error,
                        body: body
                    })

                    if (error) {
                        callback(error)
                        return;
                    }
                    callback(null, response.statusCode);
                });
            }
        ], function (err, statusCode) {
            if (err) {
                return next(err);
            }
            if (statusCode != 201) {
                return next(new Error("Unable to add to cart. Status code: " + statusCode))
            }
            if (span) {
                helpers.respondStatus(res, statusCode, {'trace-id': span.spanContext().traceId});
            } else {
                helpers.respondStatus(res, statusCode)
            }
        });
    });

// Update cart item
    app.post("/cart/update", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'UpdateCartItem'});

        if (req.body.id == null) {
            childLogging.warn({'exception': '缺少参数，缺少产品ID'})
            next(new Error("Must pass id of item to update"), 400);
            return;
        }
        if (req.body.quantity == null) {
            childLogging.warn({'exception': '缺少参数，缺少产品数量'})
            next(new Error("Must pass quantity to update"), 400);
            return;
        }

        childLogging.info({
            parameters: req.params,
            msg: '更新购物车产品请求',
            url: req.url,
            parameter: {id: req.body.id, quantity: req.body.quantity}
        });

        var span = trace.getSpan(context.active());

        async.waterfall([
            function (callback) {
                let startTime = Math.floor(Date.now() / 1000);
                request(endpoints.catalogueUrl + "/catalogue/" + req.body.id.toString(), function (error, response, body) {
                    childLogging.info({
                        msg: '请求[catalogue]后端产品详情服务',
                        url: req.url,
                        parameter: {id: req.body.id, quantity: req.body.quantity},
                        cost: Math.floor(Date.now() / 1000) - startTime,
                        isError: error,
                        responseBody: JSON.parse(body)
                    });
                    callback(error, JSON.parse(body));
                });
            },
            function (item, callback) {
                var options = {
                    uri: endpoints.cartsUrl + "/" + custId + "/items",
                    method: 'PATCH',
                    json: true,
                    body: {itemId: item.id, quantity: parseInt(req.body.quantity), unitPrice: item.price}
                };
                let startTime = Math.floor(Date.now() / 1000);
                request(options, function (error, response, body) {

                    childLogging.info({
                        msg: '请求[carts]后端更新产品服务',
                        url: options.uri,
                        parameter: options.body,
                        method: 'PATCH',
                        cost: Math.floor(Date.now() / 1000) - startTime,
                        isError: error,
                        responseBody: body,
                        responseCode: response.statusCode,
                    });

                    if (error) {
                        callback(error)
                        return;
                    }
                    callback(null, response.statusCode);
                });
            }
        ], function (err, statusCode) {
            if (err) {
                return next(err);
            }
            if (statusCode != 202) {
                return next(new Error("Unable to add to cart. Status code: " + statusCode))
            }

            if (span) {
                helpers.respondStatus(res, statusCode, {'trace-id': span.spanContext().traceId});
            } else {
                helpers.respondStatus(res, statusCode)
            }
        });
    });

    module.exports = app;
}());
