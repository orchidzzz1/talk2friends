const Database = require("../DAO/meetingDAO");
const AWS = require("aws-sdk");
const db = new Database();
require("dotenv").config();

describe("add", () => {
    var meetingId;
    const currentDatetime = new Date().getTime();
    test("add meeting", async () => {
        await db.addMeeting("trojan1@usc.edu", {
            datetime: currentDatetime + 10000,
            title: "test",
            location: "GFS 102",
            description: "test meeting",
        });
        const meetings = await db.getAllMeetings();
        expect(meetings.length).not.toEqual(0);
        const meeting = meetings[0];
        expect(meeting.title).toBe("test");
        expect(meeting.location).toBe("GFS 102");
        expect(meeting.description).toBe("test meeting");
        expect(meeting.datetime).toEqual(currentDatetime + 10000);
        expect(meeting.sk).toMatch(/trojan1@usc.edu/);
        meetingId = meeting.sk;
    }, 50000);

    afterEach(async () => {
        AWS.config.update({
            accessKeyId: process.env.AWS_ACCESS_KEY_ID,
            secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY,
            sessionToken: process.env.AWS_SESSION_TOKEN,
            region: process.env.AWS_REGION,
        });

        const client = new AWS.DynamoDB.DocumentClient();
        // delete meeting
        const params = {
            TableName: process.env.CYCLIC_DB,
            Key: {
                pk: "meetings",
                sk: meetingId,
            },
        };
        await client.delete(params).promise();

        // delete participantation by person who created meeting
        const params2 = {
            TableName: process.env.CYCLIC_DB,
            Key: {
                pk: "participations",
                sk: meetingId + "-trojan1@usc.edu",
            },
        };
        await client.delete(params2).promise();
    }, 50000);
});

describe("modifying", () => {
    var meetingId;
    var currentDatetime;
    beforeAll(async () => {
        currentDatetime = new Date().getTime();
        await db.addMeeting("trojan2@usc.edu", {
            datetime: currentDatetime + 10000000000,
            title: "test",
            location: "GFS 102",
            description: "test meeting",
        });
        const meetings = await db.getAllMeetings();
        meetingId = meetings[0].sk;
        console.log(meetingId);
    }, 50000);

    test("edit meeting", async () => {
        const meeting = {
            id: meetingId,
            title: "new test",
            description: "test meeting",
            location: "Zoom link",
            datetime: currentDatetime + 10000000000,
        };
        await db.editMeeting(meeting);
        const meetings = await db.getAllMeetings();
        const res = meetings[0];
        expect(res.sk).toBe(meetingId);
        expect(res.title).toBe("new test");
        expect(res.description).toBe("test meeting");
        expect(res.location).toBe("Zoom link");
    }, 50000);

    test("get meetings", async () => {
        const meetings = await db.getAllMeetings();
        expect(meetings.length).not.toEqual(0);
    }, 50000);

    test("add participants", async () => {
        await db.addParticipant(meetingId, "friend@usc.edu");
        const meetings = await db.getAllMeetings();
        const meeting = meetings[0];
        console.log(meeting);
        expect(meeting["participants"].length).not.toEqual(0);
    }, 50000);

    describe("removal", () => {
        test("remove participants", async () => {
            await db.removeParticipant(meetingId, "friend@usc.edu");
            await db.removeParticipant(meetingId, "trojan2@usc.edu");
            const meetings = await db.getAllMeetings();
            const meeting = meetings[0];
            expect(meeting["participants"].length).toEqual(0);
        }, 50000);
        describe("last removal", () => {
            test("remove meeting", async () => {
                await db.removeMeeting(meetingId);
                const meetings = await db.getAllMeetings();
                expect(meetings.length).toEqual(0);
            }, 50000);
        });
    });
});

// // reset db
// afterAll(async () => {
//     AWS.config.update({
//         accessKeyId: process.env.AWS_ACCESS_KEY_ID,
//         secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY,
//         sessionToken: process.env.AWS_SESSION_TOKEN,
//         region: process.env.AWS_REGION,
//     });
//     const client = new AWS.DynamoDB.DocumentClient();
//     const scanParams = {
//         TableName: process.env.CYCLIC_DB,
//     };
//     const scanData = await client.scan(scanParams).promise();
//     var promises = [];
//     scanData.Items.forEach(async (item) => {
//         var promise = new Promise(async (resolve, reject) => {
//             const deleteParams = {
//                 TableName: process.env.CYCLIC_DB,
//                 Key: {
//                     pk: item.pk,
//                     sk: item.sk,
//                 },
//             };
//             await client.delete(deleteParams).promise();
//             resolve();
//         });
//         promises.push(promise);
//     });
//     await Promise.all(promises);
// }, 500000);
