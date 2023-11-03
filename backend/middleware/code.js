const NodeCache = require("node-cache");

const codeCache = new NodeCache();

function generate6DigitCode() {
    const code = Math.floor(Math.random(100000, 1000000) * 1000000) + 100000;
    return code;
}

function verifyCode(userEmail, code) {
    // check cache if given code is correct
    const correctCode = codeCache.get(userEmail);
    if (code == correctCode) {
        return true;
    } else {
        return false;
    }
}

function addCode(userEmail, code) {
    // add or update code in cache for given userEmail
    codeCache.set(userEmail, code, 120);
}

module.exports = { generate6DigitCode, addCode, verifyCode };
