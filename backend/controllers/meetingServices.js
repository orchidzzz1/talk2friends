const Database = require("./../DAO/meetingDAO");
const db = new Database();
const Meeting = require("./..//models/meeting");

async function createMeeting(req, res) {
    try {
        const data = req.body;
        let meeting = new Meeting();
        meeting.description = data.description;
        meeting.title = data.title;
        meeting.datetime = data.datetime;
        meeting.location = data.location;
        await db.addMeeting(req.query.userEmail, meeting);
        res.sendStatus(200);
    } catch (error) {
        res.sendStatus(500);
    }
}

async function editMeeting(req, res) {
    try {
        const data = req.body;
        let meeting = new Meeting();
        meeting.description = data.description;
        meeting.title = data.title;
        meeting.datetime = data.datetime;
        meeting.location = data.location;
        await db.editMeeting(meeting);
        res.sendStatus(200);
    } catch (error) {
        res.sendStatus(500);
    }
}
async function getAllMeetings(req, res) {
    const meetings = await db.getAllMeetings();
    res.json(meetings);
}

async function addParticipant(req, res) {
    try {
        const data = req.body;
        let meetingId = data.meetingId;
        await db.addParticipant(meetingId, req.query.userEmail);
        res.sendStatus(200);
    } catch (error) {
        res.sendStatus(500);
    }
}
async function removeParticipant(req, res) {
    try {
        const data = req.body;
        let meetingId = data.meetingId;
        await db.removeParticipant(meetingId, req.query.userEmail);
        res.sendStatus(200);
    } catch (error) {
        res.sendStatus(500);
    }
}

async function removeMeeting(req, res) {
    try {
        const data = req.body;
        const email = req.query.userEmail;
        let meetingId = data.meetingId;
        if (!meetingId.includes(email)) {
            throw new Error("Unauthorized");
        }
        await db.removeMeeting(meetingId);
        res.sendStatus(200);
    } catch (error) {
        res.sendStatus(500);
    }
}

module.exports = {
    createMeeting,
    editMeeting,
    getAllMeetings,
    addParticipant,
    removeParticipant,
    removeMeeting,
};
