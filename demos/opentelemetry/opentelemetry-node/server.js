'use strict';
require('./init-trace')(require('./utils')());
const app = require('express')();

app.get('/hello-world', (req, res, next) => {
    res.send("hello world");
});

app.listen(8086, () => {
    console.log(`Listening on http://localhost:8086. Place visit http://localhost:8086/hello-world`);
});
