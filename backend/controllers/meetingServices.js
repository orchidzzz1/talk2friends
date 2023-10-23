const Database = require("./../DAO/meetingDAO");
const db = new Database();

async function createMeeting(req, res) {
    const data = req.body;
}

async function editMeeting(req, res) {}
async function getAllMeetings(req, res) {}

async function addParticipant(req, res) {}
async function removeParticipant(req, res) {}

module.exports = {
    createMeeting,
    editMeeting,
    getAllMeetings,
    addParticipant,
    removeParticipant,
};
