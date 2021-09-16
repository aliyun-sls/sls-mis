(function () {
    'use strict';

    var express = require("express")
        , request = require("request")
        , app = express()
    const {context, trace} = require('@opentelemetry/api');

    app.get("/createOrders", function (req, res, next) {
        let childLogging = req.log.child({'operation': 'CreateOrder'});
        childLogging.info({parameters: req.query, msg: '创建订单请求', url: req.url});
        let options = {
            uri: process.env.BACKEEND_DOMAIN + '/createOrder?id=' + req.query.custId + "&name=" + req.query.productName,
            method: 'GET',
        };
        let span = trace.getSpan(context.active());
        let startTime = Math.floor(Date.now() / 1000);

        request(options, function (error, response, body) {
            childLogging.info({
                msg: "调用[backEnd]：创建订单请求",
                isError: error,
                body: body,
                url: options.uri,
                cost: Math.floor(Date.now() / 1000) - startTime
            })

            if (error) {
                return next(error);
            }

            let data = JSON.parse(body);
            if (data.success) {
                return respondSuccessBody(res, 200, JSON.stringify({"message": "创建订单成功"}), function (span) {
                    if (span) {
                        return {'trace-id': span.spanContext().traceId}
                    }
                }(span));
            }

            return respondSuccessBody(res, 500, JSON.stringify(data), function (span) {
                if (span) {
                    return {'trace-id': span.spanContext().traceId}
                }
            }(span));
        }.bind({
            res: res
        }));
    });

    function respondSuccessBody(res, statusCode, body, headers) {
        res.charset = "utf-8"
        res.contentType("application/json")

        res.writeHeader(statusCode, headers);
        res.write(body);
        res.end();
    }

    module.exports = app;
}());
