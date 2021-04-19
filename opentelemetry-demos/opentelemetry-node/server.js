'use strict';
require('./init-trace')(require('./utils')());
const express = require('express');
const app = express();

app.get('/hello-world', (req, res, next) => {
    res.send("hello world");
});

app.listen(8080, () => {
    console.log(`Listening on http://localhost:8080. Place visit http://localhost:8080/hello-world`);
});