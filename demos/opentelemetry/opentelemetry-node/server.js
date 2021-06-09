"use strict";
require("./init-trace")(require("./utils")());
const app = require("express")();
const { context, getSpan, trace } = require("@opentelemetry/api");

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

app.get("/save", async (req, res, next) => {
  const span = getSpan(context.active());
  const query = req.query;
  if (query.event) {
    span.addEvent("custom event : " + query.event);
    span.setAttribute("event", query.event);
  }

  const saveSpan = trace.getTracer().startSpan("Save to db");
  await sleep(Math.random(10) * 1000);
  saveSpan.end();
  res.send("save success");
});

app.listen(8086, () => {
  console.log(
    `Listening on http://localhost:8086. Place visit http://localhost:8086/hello-world`
  );
});
