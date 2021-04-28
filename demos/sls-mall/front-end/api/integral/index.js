(function (){
    'use strict';

    var async     = require("async")
        , express   = require("express")
        , request   = require("request")
        , endpoints = require("../endpoints")
        , helpers   = require("../../helpers")
        , app       = express();

    app.get("/integral", function (req, res, next) {
        console.log("integral Request received with body: " + JSON.stringify(req.body));
        var logged_in = req.cookies.logged_in;
        if (!logged_in) {
            throw new Error("User not logged in.");
            return
        }

        var custId = req.session.customerId;
        async.waterfall([
                function (callback) {
                    request("http://integral/usableIntergral?custId=" + custId, function (error, response, body) {
                    //request("http://127.0.0.1:10000/usableIntergral?custId=888888", function (error, response, body) {
                        if (error) {
                            return callback(error);
                        }
                        console.log("Received response: " + JSON.stringify(body));
                        if (response.statusCode == 404) {
                            console.log("No integral found for user: " + custId);
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
        console.log("all_integral Request received with body: " + JSON.stringify(req.body));
        var logged_in = req.cookies.logged_in;
        if (!logged_in) {
            throw new Error("User not logged in.");
            return
        }

        var custId = req.session.customerId;
        async.waterfall([
                function (callback) {
                    request("http://integral//intergralSum?custId=" + custId, function (error, response, body) {
                    //request("30.43.120.7:10000/intergralSum?custId=888888", function (error, response, body) {
                        if (error) {
                            return callback(error);
                        }
                        console.log("Received response: " + JSON.stringify(body));
                        if (response.statusCode == 404) {
                            console.log("No integral found for user: " + custId);
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
