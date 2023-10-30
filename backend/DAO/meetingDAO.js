const AWS = require("aws-sdk");
require("dotenv").config();

class MeetingDAO {
    constructor() {
        // Configure the AWS credentials and region
        AWS.config.update({
            accessKeyId: process.env.AWS_ACCESS_KEY_ID,
            secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY,
            sessionToken: process.env.AWS_SESSION_TOKEN,
            region: process.env.AWS_REGION,
        });
    }
    async addMeeting(userEmail, meeting) {
        const client = new AWS.DynamoDB.DocumentClient();
        const currentDatetime = new Date().getTime().toString();
        const meetindId = `${userEmail}-${currentDatetime}`;
        const params = {
            TableName: process.env.CYCLIC_DB,
            Item: {
                pk: "meetings",
                sk: meetindId,
                title: meeting.title,
                datetime: meeting.datetime,
                description: meeting.description,
                location: meeting.location,
            },
        };
        try {
            await client.put(params).promise();
            // add the creator to the participations list of the meeting
            await this.addParticipant(meetindId, userEmail);
            console.log("Meeting added successfully");
        } catch (err) {
            console.error("Error adding meeting:", err);
        }
    }
    async editMeeting(meeting) {
        const client = new AWS.DynamoDB.DocumentClient();
        const primaryKey = {
            pk: "meetings",
            sk: meeting.id,
        };
        const updateExpression =
            "SET title = :title, #desc = :description, #dt = :datetime, #lo = :location";
        const expressionAttributeValues = {
            ":title": meeting.title,
            ":description": meeting.description,
            ":datetime": meeting.datetime,
            ":location": meeting.location,
        };
        const expressionAttributeNames = {
            "#desc": "description", // 'description' is a reserved keyword so have to use this
            "#dt": "datetime",
            "#lo": "location",
        };
        const params = {
            TableName: process.env.CYCLIC_DB,
            Key: primaryKey,
            UpdateExpression: updateExpression,
            ExpressionAttributeValues: expressionAttributeValues,
            ExpressionAttributeNames: expressionAttributeNames,
        };
        try {
            await client.update(params).promise();
            console.log("Meeting updated successfully");
        } catch (err) {
            console.error("Error updating meeting: ", err);
        }
    }

    async getAllMeetings() {
        const client = new AWS.DynamoDB.DocumentClient();
        // get all active meetings start later than current time
        const pk = "meetings";
        const currentDatetime = new Date().getTime();
        const params = {
            TableName: process.env.CYCLIC_DB,
            KeyConditionExpression: "pk = :pk",
            FilterExpression: "#dt > :val",
            ExpressionAttributeValues: {
                ":pk": pk,
                ":val": currentDatetime,
            },
            ExpressionAttributeNames: {
                "#dt": "datetime",
            },
        };
        const result = await client.query(params).promise();
        const meetings = result.Items;
        // get all participants for each meeting and attach to meeting object
        var promises = [];
        meetings.forEach(async (meeting) => {
            var promise = new Promise(async (resolve, reject) => {
                var meetingId = meeting.sk;
                const pk2 = "participations";
                var params = {
                    TableName: process.env.CYCLIC_DB,
                    KeyConditionExpression:
                        "#pk = :pk and begins_with(sk, :meetingId)",
                    ExpressionAttributeNames: {
                        "#pk": "pk",
                    },
                    ExpressionAttributeValues: {
                        ":pk": pk2,
                        ":meetingId": meetingId,
                    },
                };
                const result2 = await client.query(params).promise();
                const participants = result2.Items;
                const participantsList = [];
                participants.forEach((participant) => {
                    const parts = participant.sk.split("-");
                    // sk = 'meetingId-email'
                    const email = parts[2];
                    participantsList.push(email);
                });
                meeting.participants = participantsList;
                resolve();
            });
            promises.push(promise);
        });
        await Promise.all(promises);
        return meetings;
    }
    async addParticipant(meetingId, userEmail) {
        const client = new AWS.DynamoDB.DocumentClient();
        const params = {
            TableName: process.env.CYCLIC_DB,
            Item: { pk: "participations", sk: `${meetingId}-${userEmail}` },
        };
        try {
            await client.put(params).promise();
            console.log("Participant added successfully");
        } catch (err) {
            console.error("Error adding participant: ", err);
        }
    }
    async removeParticipant(meetingId, userEmail) {
        const client = new AWS.DynamoDB.DocumentClient();
        const params = {
            TableName: process.env.CYCLIC_DB,
            Key: { pk: "participations", sk: `${meetingId}-${userEmail}` },
        };
        try {
            await client.delete(params).promise();
            console.log("Participant removed successfully");
        } catch (err) {
            console.error("Error removing participant: ", err);
        }
    }
    async removeMeeting(meetingId) {
        const client = new AWS.DynamoDB.DocumentClient();
        const params = {
            TableName: process.env.CYCLIC_DB,
            Key: { pk: "meetings", sk: `${meetingId}` },
        };
        try {
            await client.delete(params).promise();
            console.log("Meeting removed successfully");
        } catch (err) {
            console.error("Error removing meeting: ", err);
        }
    }
}
module.exports = MeetingDAO;
