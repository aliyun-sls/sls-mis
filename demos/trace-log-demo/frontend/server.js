const tracer = require('./tracer')(require('./utils')());
var request = require("request")
    , express = require("express")
    , logging = require('pino-http')({
        serializers: {
            req(req) {
                return {};
            },
            res(res) {
                return {};
            }
        }
    }),
    order = require("./api/order")
    , app = express()

app.use(logging)
app.use(order);

var server = app.listen(process.env.PORT || 8090, function () {
    var port = server.address().port;
    console.log("App now running in %s mode on port %d", app.get("env"), port);
});