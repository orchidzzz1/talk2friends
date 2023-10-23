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
    async addMeeting(userEmail) {}
    async editMeeting(userEmail) {}

    async getAllMeetings() {
        const pk = "meetings";
        const params = {
            TableName: process.env.CYCLIC_DB,
            KeyConditionExpression: "#pk = :pk",
            FilterExpression: "attribute_exists(active) AND active = :active",
            ExpressionAttributeNames: {
                "#pk": "pk",
            },
            ExpressionAttributeValues: {
                ":pk": pk,
            },
        };
        const result = await this.client.query(params).promise();
        const meetings = result.meetings;
        // get all participants for each meeting and attach to meeting object
        meetings.forEach((meeting) => {});
        return meetings;
    }
    async addParticipant(meetingId, userEmail) {}
    async removeParticipant(meetingId, userEmail) {}
}
module.exports = MeetingDAO;
