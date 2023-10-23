const Database = require("./../DAO/userDAO");
const db = new Database();

async function register(req, res) {
    // create verification code
    // send code to user's email
}
async function add(req, res) {
    // add user to database after successful registration/verification
}
async function get(req, res) {}
async function updateUserInfo(req, res) {}
async function addInterest(req, res) {}
async function removeInterest(req, res) {}
async function getRecommended(req, res) {}
async function verifyRegistration(req, res) {}
async function addFriend(req, res) {}
async function removeFriend(req, res) {}
async function sendRecommendation(req, res) {}

module.exports = {
    register,
    getRecommended,
    get,
    updateUserInfo,
    addInterest,
    removeFriend,
    removeInterest,
    addFriend,
    sendRecommendation,
    verifyRegistration,
    add,
};
