const AWS = require("aws-sdk");
require("dotenv").config();
import User from "./../models/user";

class UserDAO {
    constructor() {
        // Configure the AWS credentials and region
        AWS.config.update({
            accessKeyId: process.env.AWS_ACCESS_KEY_ID,
            secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY,
            sessionToken: process.env.AWS_SESSION_TOKEN,
            region: process.env.AWS_REGION,
        });
    }

    async addUser(user) {
        // expect a User object
        try {
            const item = {
                pk: "user",
                sk: user.email,
                username: user.name,
                affiliation: user.affiliation,
                international: user.international,
            };
            const client = new AWS.DynamoDB.DocumentClient();

            const params = {
                TableName: process.env.CYCLIC_DB,
                Item: item,
            };
            await client.put(params).promise();
        } catch (error) {
            console.log("Error adding user: ", error);
        }
    }

    async getUserInfo(userEmail) {
        // get userinfo, list of interests, list of friends, and list of created meetings
        const pk = "user";
        const sk = userEmail;
        const client = new AWS.DynamoDB.DocumentClient();
        const params = {
            TableName: process.env.CYCLIC_DB,
            KeyConditionExpression: "pk = :pk and sk = :sk",
            ExpressionAttributeValues: {
                ":pk": pk,
                ":sk": sk,
            },
        };
        const result = await client.query(params).promise();
        var user = result.Items[0];
        if (user) {
            // get list of friends and list of interests
        }
        return user;
    }

    async updateUserInfo(userEmail, name, affiliation, role) {}
    async removeInterest(userEmail, interest) {}
    async addInterest(userEmail, interest) {}
    async getRecommended(userEmail) {}
    async addVerification(userEmail, code) {}
    async verifyRegistration(userEmail, code) {}
    async addFriend(userEmail, friendEmail) {
        // if no friend request, add new request
    }
    async removeFriend(userEmail, friendEmail) {}
    async addFriendRequest(userEmail, friendEmail) {}
}
module.exports = UserDAO;
