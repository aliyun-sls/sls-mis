(function () {
    'use strict';

    var async = require("async")
        , express = require("express")
        , request = require("request")
        , endpoints = require("../endpoints")
        , helpers = require("../../helpers")
        , app = express();

    app.get("/integral", function (req, res, next) {
        var logged_in = req.cookies.logged_in;
        if (!logged_in) {
            req.log.warn("用户没有登陆")
            throw new Error("User not logged in.");
            return
        }

        var custId = req.session.customerId;
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'GetUserPoint'});
        childLogging.info({parameters: req.params, msg: '查看用户积分', url: req.url});
        async.waterfall([
                function (callback) {
                    let startTime = Math.floor(Date.now() / 1000);
                    request(endpoints.integralUrl + "/usableIntergral?custId=" + custId, function (error, response, body) {
                        childLogging.info({
                            msg: "调用[user-points]：获取用户积分情况",
                            isError: error || response.statusCode === 404,
                            body: body,
                            url: endpoints.integralUrl + "/usableIntergral?custId=" + custId,
                            cost: Math.floor(Date.now() / 1000) - startTime
                        })

                        if (error) {
                            return callback(error);
                        }
                        if (response.statusCode == 404) {
                            return callback(null, []);
                        }
                        callback(null, JSON.parse(body));
                    });
                }
            ],
            function (err, result) {
                if (err) {
                    return next(err);
                }
                helpers.respondStatusBody(res, 201, JSON.stringify(result));
            });
    });
    app.get("/all_integral", function (req, res, next) {
        var logged_in = req.cookies.logged_in;
        if (!logged_in) {
            req.log.warn("用户没有登陆")
            throw new Error("User not logged in.");
            return
        }

        var custId = req.session.customerId;
        let childLogging = req.log.child({"cust_id": custId, 'operation': 'GetAllUserPoints'});
        childLogging.info({parameters: req.params, msg: '查看用户积分明细', url: req.url});

        async.waterfall([
                function (callback) {
                    let startTime = Math.floor(Date.now() / 1000);
                    request(endpoints.integralUrl + "/intergralSum?custId=" + custId, function (error, response, body) {
                        childLogging.info({
                            msg: "调用[user-points]：查看用户积分明细",
                            isError: error || response.statusCode === 404,
                            body: body,
                            url: endpoints.integralUrl + "/intergralSum?custId=" + custId,
                            cost: Math.floor(Date.now() / 1000) - startTime
                        })

                        if (error) {
                            return callback(error);
                        }
                        if (response.statusCode == 404) {
                            return callback(null, []);
                        }
                        callback(null, JSON.parse(body));
                    });
                }
            ],
            function (err, result) {
                if (err) {
                    return next(err);
                }
                helpers.respondStatusBody(res, 201, JSON.stringify(result));
            });
    });
    module.exports = app;
}());
