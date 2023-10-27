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
        this.client = new AWS.DynamoDB.DocumentClient();
    }
    async addMeeting(userEmail, meeting) {
        const currentDatetime = new Date().getTime();
        const meetindId = `${userEmail}-${currentDatetime}`;
        const params = {
            pk: "meetings",
            sk: meetindId,
            title: meeting.title,
            datetime: meeting.datetime,
            description: meeting.description,
            location: meeting.location,
        };
        this.client.put(params, (err, data) => {
            if (err) {
                console.error("Error adding meeting:", err);
            } else {
                console.log("Meeting added successfully");
            }
        });
        // add the creator to the participations list of the meeting
        await this.addParticipant(meetindId, userEmail);
    }
    async editMeeting(meeting) {
        const primaryKey = {
            pk: "meetings",
            sk: meeting.id,
        };
        const updateExpression =
            "SET title = :title, #description = :description, datetime = :datetime, location = :location,";
        const expressionAttributeValues = {
            ":title": meeting.title,
            ":description": meeting.description,
            ":datetime": meeting.datetime,
            ":location": meeting.location,
        };
        const expressionAttributeNames = {
            "#desc": "description", // 'description' is a reserved keyword so have to use this
        };
        const params = {
            TableName: process.env.CYCLIC_DB,
            Key: primaryKey,
            UpdateExpression: updateExpression,
            ExpressionAttributeValues: expressionAttributeValues,
            ExpressionAttributeNames: expressionAttributeNames,
        };
        this.client.update(params, (err, data) => {
            if (err) {
                console.error("Error updating meeting: ", err);
            } else {
                console.log("Meeting updated successfully");
            }
        });
    }

    async getAllMeetings() {
        // get all active meetings start later than current time
        const pk = "meetings";
        const currentDatetime = new Date().getTime();
        const params = {
            TableName: process.env.CYCLIC_DB,
            KeyConditionExpression: "#pk = :pk and #dt > :val",
            ExpressionAttributeNames: {
                "#pk": "pk",
                ":val": currentDatetime,
            },
            ExpressionAttributeValues: {
                ":pk": pk,
                ":dt": datetime,
            },
        };
        const result = await this.client.query(params).promise();
        const meetings = result.Items;
        // get all participants for each meeting and attach to meeting object
        meetings.forEach(async (meeting) => {
            var meetingId = meeting.sk;
            pk2 = "participations";
            var params = {
                TableName: process.env.CYCLIC_DB,
                KeyConditionExpression:
                    "#pk = :pk and begins_with(sk, :meetindId)",
                ExpressionAttributeNames: {
                    "#pk": "pk",
                },
                ExpressionAttributeValues: {
                    ":pk": pk2,
                    ":meetingId": meetingId,
                },
            };
            const result2 = await this.client.query(params).promise();
            const participants = result2.Items;
            const participantsList = [];
            const startingIdx = meetingId.length;
            participants.forEach((participant) => {
                const id = participant.sk;
                // id = 'meetingId-email'
                const email = id.slice(startingIdx + 1);
                participantsList.push(email);
            });
            meeting.participants = participantList;
        });
        return meetings;
    }
    async addParticipant(meetingId, userEmail) {
        const params = {
            pk: "participations",
            sk: `${meetingId}-${userEmail}`,
        };
        this.client.put(params, (err, data) => {
            if (err) {
                console.error("Error adding participant: ", err);
            } else {
                console.log("Participant added successfully");
            }
        });
    }
    async removeParticipant(meetingId) {
        const params = {
            pk: "participations",
            sk: `${meetingId}-${userEmail}`,
        };
        this.client.delete(params, (err, data) => {
            if (err) {
                console.error("Error removing participant: ", err);
            } else {
                console.log("Participant removed successfully");
            }
        });
    }
    async removeMeeting(meetingId) {
        const params = {
            pk: "meetings",
            sk: `${meetingId}`,
        };
        this.client.delete(params, (err) => {
            if (err) {
                console.error("Error removing meeting: ", err);
            } else {
                console.log("Meeting removed successfully");
            }
        });
    }
}
module.exports = MeetingDAO;
