"use strict";
require("./init-trace")(require("./utils")());
const app = require("express")();
const {context, getSpan, trace} = require("@opentelemetry/api");

async function sleep(num) {
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve(true);
        }, num);
    });
}

app.get("/hello-world", (req, res, next) => {
    res.send("hello world");
});

app.get("/save-order", async (req, res, next) => {
    const currentSpan = getSpan(context.active());

    const orderId = req.query.id;
    res.set("trace-id", currentSpan.context().traceId);

    try {
        if (orderId == null) {
            throw Error("Order ID is empty")
        }

        const childSpan = trace.getTracer("opentelemetry-node").startSpan('SaveOrder/To/DB', {
            attributes: {
                orderID: orderId
            }
        }, context.active());
        childSpan.setAttribute("orderInfo", "other information");
        childSpan.end()

        res.send("Saved")
    } catch (e) {
        currentSpan.addEvent("Error with process order", {
            error: e
        });
        res.send("Error with process order");
    }
});


app.listen(8086, () => {
    console.log(
        `Listening on http://localhost:8086. Place visit http://localhost:8086/hello-world`
    );
});
