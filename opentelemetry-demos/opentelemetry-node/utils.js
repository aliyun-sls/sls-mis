process = require('process');

const project = process.env.PROJECT;
const logstore = process.env.LOGSTORE;
const service_name = process.env.SERVICE_NAME || "opentelemetry-node";
const access_key_id = process.env.ACCESS_KEY_ID;
const access_secret = process.env.ACCESS_SECRET;
const endpoint = process.env.ENDPOINT;

function check_param(parameter, desc) {
    if (parameter == undefined || parameter === "") {
        console.log("Miss Parameter[" + desc + "]");
        process.exit(-1);
    }
}

module.exports = () => {
    check_param(project, "PROJECT");
    check_param(logstore, "LOGSTORE")
    check_param(access_key_id, "ACCESS_KEY_ID")
    check_param(access_secret, "ACCESS_SECRET")
    check_param(endpoint, "ENDPOINT")

    return {
        project: project,
        logstore: logstore,
        service_name: service_name,
        access_key_id: access_key_id,
        access_secret: access_secret,
        endpoint: endpoint,
    }
}

