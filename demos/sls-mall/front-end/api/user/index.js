(function () {
    'use strict';
    const {context, trace} = require('@opentelemetry/api');

    var async = require("async"), express = require("express"), request = require("request"),
        endpoints = require("../endpoints"), helpers = require("../../helpers"), app = express(),
        cookie_name = "logged_in"


    app.get("/customers/:id", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'GetUserInfo'});
        childLogging.info({parameters: req.params, msg: '查看用户信息', url: req.url});
        let startTime = Math.floor(Date.now() / 1000);
        helpers.simpleHttpRequest(endpoints.customersUrl + "/" + req.session.customerId, res, next, function (error, response, body) {
            childLogging.info({
                msg: "调用[user]：查看用户详情",
                isError: error,
                parameter: {id: req.session.customerId},
                body: body,
                url: endpoints.customersUrl + "/" + req.session.customerId,
                cost: Math.floor(Date.now() / 1000) - startTime
            })
        });
    });
    app.get("/cards/:id", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'GetUserCardInfo'});
        childLogging.info({parameters: req.params, msg: '查看用户银行卡信息', url: req.url});
        let startTime = Math.floor(Date.now() / 1000);

        helpers.simpleHttpRequest(endpoints.cardsUrl + "/" + req.params.id, res, next, function (error, response, body) {
            childLogging.info({
                msg: "调用[user]：查看用户银行卡信息",
                isError: error,
                parameter: {id: req.params.id},
                body: body,
                url: endpoints.cardsUrl + "/" + req.params.id,
                cost: Math.floor(Date.now() / 1000) - startTime
            })
        });
    });

    app.get("/customers", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'GetUserCardInfo'});
        childLogging.info({parameters: req.params, msg: '获取所有用户信息', url: req.url});
        let startTime = Math.floor(Date.now() / 1000);
        helpers.simpleHttpRequest(endpoints.customersUrl, res, next, function (error, response, body) {
            childLogging.info({
                msg: "调用[user]：获取所有用户信息",
                isError: error,
                body: body,
                url: endpoints.customersUrl,
                cost: Math.floor(Date.now() / 1000) - startTime
            })
        });
    });
    app.get("/addresses", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let startTime = Math.floor(Date.now() / 1000);
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'GetALLAddresses'});
        childLogging.info({parameters: req.params, msg: '获取所有地址信息', url: req.url});
        helpers.simpleHttpRequest(endpoints.addressUrl, res, next, function (error, response, body) {
            childLogging.info({
                msg: "调用[user]：获取所有地址信息",
                isError: error,
                body: body,
                url: endpoints.addressUrl,
                cost: Math.floor(Date.now() / 1000) - startTime
            })
        });
    });

    app.get("/cards", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'GetAllCards'});
        childLogging.info({parameters: req.params, msg: '获取所有银行卡信息', url: req.url});

        let startTime = Math.floor(Date.now() / 1000);
        helpers.simpleHttpRequest(endpoints.cardsUrl, res, next,function (error, response, body) {
            childLogging.info({
                msg: "调用[user]：获取所有银行卡信息",
                isError: error,
                body: body,
                url: endpoints.cardsUrl,
                cost: Math.floor(Date.now() / 1000) - startTime
            })
        });
    });

    // Create Customer - TO BE USED FOR TESTING ONLY (for now)
    app.post("/customers", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'CreateCustomer'});
        childLogging.info({parameters: req.params, msg: '新建用户', url: req.url});

        var options = {
            uri: endpoints.customersUrl,
            method: 'POST',
            json: true,
            body: req.body
        };

        var span = trace.getSpan(context.active());
        let startTime = Math.floor(Date.now() / 1000);
        request(options, function (error, response, body) {
            childLogging.info({
                msg: "调用[user]：创建用户",
                isError: error,
                body: body,
                url: options.uri,
                parameters: options.body,
                cost: Math.floor(Date.now() / 1000) - startTime
            })

            if (error) {
                return next(error);
            }
            helpers.respondSuccessBody(res, JSON.stringify(body), function (span) {
                if (span) {
                    return {'trace-id': span.spanContext().traceId}
                }
            }(span));
        }.bind({
            res: res
        }));
    });

    app.post("/addresses", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'AddAddressInfo'});
        childLogging.info({parameters: req.params, msg: '添加地址信息', url: req.url});

        req.body.userID = helpers.getCustomerId(req, app.get("env"));

        var options = {
            uri: endpoints.addressUrl,
            method: 'POST',
            json: true,
            body: req.body
        };
        let span = trace.getSpan(context.active());
        let startTime = Math.floor(Date.now() / 1000);
        request(options, function (error, response, body) {
            childLogging.info({
                msg: "调用[user]：添加地址信息",
                isError: error,
                body: body,
                url: options.uri,
                parameters: options.body,
                cost: Math.floor(Date.now() / 1000) - startTime
            })

            if (error) {
                return next(error);
            }
            helpers.respondSuccessBody(res, JSON.stringify(body), function (span) {
                if (span) {
                    return {'trace-id': span.spanContext().traceId}
                }
            }(span));
        }.bind({
            res: res
        }));
    });

    app.get("/card", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'GetCustomerCard'});
        childLogging.info({parameters: req.params, msg: '获取用户信用卡信息', url: req.url});

        var options = {
            uri: endpoints.customersUrl + '/' + custId + '/cards',
            method: 'GET',
        };
        var span = trace.getSpan(context.active());
        let startTime = Math.floor(Date.now() / 1000);
        request(options, function (error, response, body) {
            childLogging.info({
                msg: "调用[user]：获取用户信用卡信息",
                isError: error,
                body: body,
                url: options.uri,
                cost: Math.floor(Date.now() / 1000) - startTime
            })

            if (error) {
                return next(error);
            }
            var data = JSON.parse(body);
            if (data.status_code !== 500 && data._embedded.card.length !== 0) {
                var resp = {
                    "number": data._embedded.card[0].longNum.slice(-4)
                };
                return helpers.respondSuccessBody(res, JSON.stringify(body), function (span) {
                    if (span) {
                        return {'trace-id': span.spanContext().traceId}
                    }
                }(span));
            }

            return helpers.respondSuccessBody(res, JSON.stringify({"status_code": 500}), function (span) {
                if (span) {
                    return {'trace-id': span.spanContext().traceId}
                }
            }(span));
        }.bind({
            res: res
        }));
    });

    app.get("/address", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'GetCustomerAddress'});
        childLogging.info({parameters: req.params, msg: '获取用户地址信息', url: req.url});

        var options = {
            uri: endpoints.customersUrl + '/' + custId + '/addresses',
            method: 'GET',
        };
        var span = trace.getSpan(context.active());
        let startTime = Math.floor(Date.now() / 1000);
        request(options, function (error, response, body) {
            childLogging.info({
                msg: "调用[user]：获取用户地址信息",
                isError: error,
                body: body,
                url: options.uri,
                cost: Math.floor(Date.now() / 1000) - startTime
            })

            if (error) {
                return next(error);
            }
            var data = JSON.parse(body);
            if (data.status_code !== 500 && data._embedded.address.length !== 0) {
                var resp = data._embedded.address[0];
                return helpers.respondSuccessBody(res, JSON.stringify(resp), function (span) {
                    if (span) {
                        return {'trace-id': span.spanContext().traceId}
                    }
                }(span));
            }

            return helpers.respondSuccessBody(res, JSON.stringify({"status_code": 500}), function (span) {
                if (span) {
                    return {'trace-id': span.spanContext().traceId}
                }
            }(span));
        }.bind({
            res: res
        }));
    });

    app.post("/cards", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'AddCardInfo'});
        childLogging.info({parameters: req.params, msg: '添加用户银行卡信息', url: req.url});

        req.body.userID = helpers.getCustomerId(req, app.get("env"));

        var options = {
            uri: endpoints.cardsUrl,
            method: 'POST',
            json: true,
            body: req.body
        };
        let startTime = Math.floor(Date.now() / 1000);
        var span = trace.getSpan(context.active());
        request(options, function (error, response, body) {
            childLogging.info({
                msg: "调用[user]：添加用户银行卡信息",
                isError: error,
                body: body,
                url: options.uri,
                parameters: options.body,
                cost: Math.floor(Date.now() / 1000) - startTime
            })

            if (error) {
                return next(error);
            }
            helpers.respondSuccessBody(res, JSON.stringify(body), function (span) {
                if (span) {
                    return {'trace-id': span.spanContext().traceId}
                }
            }(span));
        }.bind({
            res: res
        }));
    });

    // Delete Customer - TO BE USED FOR TESTING ONLY (for now)
    app.delete("/customers/:id", function (req, res, next) {
        var custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'DeleteCustomer'});
        childLogging.info({parameters: req.params, msg: '删除用户', url: req.url});

        var options = {
            uri: endpoints.customersUrl + "/" + req.params.id,
            method: 'DELETE'
        };
        var span = trace.getSpan(context.active());
        let startTime = Math.floor(Date.now() / 1000);
        request(options, function (error, response, body) {
            childLogging.info({
                msg: "调用[user]：删除用户",
                isError: error,
                body: body,
                url: options.uri,
                cost: Math.floor(Date.now() / 1000) - startTime
            })

            if (error) {
                return next(error);
            }
            helpers.respondSuccessBody(res, JSON.stringify(body), function (span) {
                if (span) {
                    return {'trace-id': span.spanContext().traceId}
                }
            }(span));
        }.bind({
            res: res
        }));
    });

    // Delete Address - TO BE USED FOR TESTING ONLY (for now)
    app.delete("/addresses/:id", function (req, res, next) {
        let custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'DeleteCustomerAddress'});
        childLogging.info({parameters: req.params, msg: '移除用户地址信息', url: req.url});

        var options = {
            uri: endpoints.addressUrl + "/" + req.params.id,
            method: 'DELETE'
        };
        var span = trace.getSpan(context.active());
        let startTime = Math.floor(Date.now() / 1000);
        request(options, function (error, response, body) {
            childLogging.info({
                msg: "调用[user]：移除用户地址信息",
                isError: error,
                body: body,
                url: options.uri,
                cost: Math.floor(Date.now() / 1000) - startTime
            })

            if (error) {
                return next(error);
            }
            helpers.respondSuccessBody(res, JSON.stringify(body), function (span) {
                if (span) {
                    return {'trace-id': span.spanContext().traceId}
                }
            }(span));
        }.bind({
            res: res
        }));
    });

    // Delete Card - TO BE USED FOR TESTING ONLY (for now)
    app.delete("/cards/:id", function (req, res, next) {
        let custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'RemoveCard'});
        childLogging.info({parameters: req.params, msg: '移除用户银行卡信息', url: req.url});

        var options = {
            uri: endpoints.cardsUrl + "/" + req.params.id,
            method: 'DELETE'
        };
        var span = trace.getSpan(context.active());
        let startTime = Math.floor(Date.now() / 1000);
        request(options, function (error, response, body) {
            childLogging.info({
                msg: "调用[user]：移除用户银行卡信息",
                isError: error,
                body: body,
                url: options.uri,
                cost: Math.floor(Date.now() / 1000) - startTime
            })

            if (error) {
                return next(error);
            }
            helpers.respondSuccessBody(res, JSON.stringify(body), function (span) {
                if (span) {
                    return {'trace-id': span.spanContext().traceId}
                }
            }(span));
        }.bind({
            res: res
        }));
    });

    app.post("/register", function (req, res, next) {
        let custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'UserRegister'});
        childLogging.info({parameters: req.params, msg: '用户注册', url: req.url});

        var options = {
            uri: endpoints.registerUrl,
            method: 'POST',
            json: true,
            body: req.body
        };

        var span = trace.getSpan(context.active());
        async.waterfall([
                function (callback) {
                    let startTime = Math.floor(Date.now() / 1000);
                    request(options, function (error, response, body) {
                        childLogging.info({
                            msg: "调用[user]：用户注册",
                            isError: error,
                            body: body,
                            url: options.uri,
                            parameter: options.body,
                            cost: Math.floor(Date.now() / 1000) - startTime
                        })

                        if (error !== null) {
                            callback(error);
                            return;
                        }
                        if (response.statusCode == 200 && body != null && body != "") {
                            if (body.error) {
                                callback(body.error);
                                return;
                            }
                            var customerId = body.id;
                            req.session.customerId = customerId;
                            callback(null, customerId);
                            return;
                        }
                        callback(true);
                    });
                },
                function (custId, callback) {
                    var sessionId = req.session.id;
                    var options = {
                        uri: endpoints.cartsUrl + "/" + custId + "/merge" + "?sessionId=" + sessionId,
                        method: 'GET'
                    };
                    let startTime = Math.floor(Date.now() / 1000);
                    request(options, function (error, response, body) {
                        childLogging.info({
                            msg: "调用[user]：合并购物车产品",
                            isError: error,
                            body: body,
                            url: options.uri,
                            cost: Math.floor(Date.now() / 1000) - startTime
                        })

                        if (error) {
                            if (callback) callback(error);
                            return;
                        }
                        if (callback) callback(null, custId);
                    });
                }
            ],
            function (err, custId) {
                if (err) {
                    res.status(500);
                    res.end();
                    return;
                }
                res.status(200);
                if (span) {
                    res.header('trace-id', span.spanContext().traceId)
                }
                res.cookie(cookie_name, req.session.id, {
                    maxAge: 3600000
                }).send({id: custId});
                res.end();
                return;
            }
        );
    });

    app.get("/login", function (req, res, next) {
        let custId = helpers.getCustomerId(req, app.get("env"));
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'UserLogin'});
        childLogging.info({parameters: req.params, msg: '用户登陆', url: req.url});

        var span = trace.getSpan(context.active());
        async.waterfall([
                function (callback) {
                    var options = {
                        headers: {
                            'Authorization': req.get('Authorization')
                        },
                        uri: endpoints.loginUrl
                    };
                    let startTime = Math.floor(Date.now() / 1000);
                    request(options, function (error, response, body) {
                        childLogging.info({
                            msg: "调用[user]：用户认证",
                            isError: error,
                            body: body,
                            url: options.uri,
                            cost: Math.floor(Date.now() / 1000) - startTime
                        })

                        if (error) {
                            callback(error);
                            return;
                        }
                        if (response.statusCode == 200 && body != null && body != "") {
                            var customerId = JSON.parse(body).user.id;
                            req.session.customerId = customerId;
                            callback(null, customerId);
                            return;
                        }
                        callback(true);
                    });
                },
                function (custId, callback) {
                    var sessionId = req.session.id;
                    var options = {
                        uri: endpoints.cartsUrl + "/" + custId + "/merge" + "?sessionId=" + sessionId,
                        method: 'GET'
                    };
                    let startTime = Math.floor(Date.now() / 1000);
                    request(options, function (error, response, body) {
                        childLogging.info({
                            msg: "调用[user]：合并购物车产品",
                            isError: error,
                            body: body,
                            url: options.uri,
                            cost: Math.floor(Date.now() / 1000) - startTime
                        })
                        if (error) {
                            // if cart fails just log it, it prevenst login
                            //return;
                        }
                        callback(null, custId);
                    });
                }
            ],
            function (err, custId) {
                if (err) {
                    res.status(401);
                    res.end();
                    return;
                }
                if (span) {
                    res.header('trace-id', span.spanContext().traceId)
                }
                res.status(200);
                res.cookie(cookie_name, req.session.id, {
                    maxAge: 3600000
                }).send('Cookie is set');
                res.end();
                return;
            });
    });

    module.exports = app;
}());
