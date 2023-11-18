const AWS = require("aws-sdk");
require("dotenv").config();

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
        const client = new AWS.DynamoDB.DocumentClient();
        // expect a User object
        try {
            const item = {
                pk: "users",
                sk: user.email,
                userName: user.userName,
                affiliation: user.affiliation,
                international: user.international,
            };
            const params = {
                TableName: process.env.CYCLIC_DB,
                Item: item,
            };
            await client.put(params).promise();
            // add interests
            user.interests.forEach(async (interest) => {
                await this.addInterest(user.email, interest);
            });
        } catch (error) {
            console.log("Error adding user: ", error);
        }
    }

    async getUserInfo(userEmail) {
        const client = new AWS.DynamoDB.DocumentClient();
        // get userinfo, list of interests, list of friends, and list of created meetings
        const pk = "users";
        const sk = userEmail;
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
            // get list of interests
            var interests = [];
            const interestParams = {
                TableName: process.env.CYCLIC_DB,
                KeyConditionExpression: "pk = :pk and begins_with(sk, :email)",
                ExpressionAttributeValues: {
                    ":pk": "interests",
                    ":email": userEmail,
                },
            };
            const interestRes = await client.query(interestParams).promise();
            interestRes.Items.forEach((interest) => {
                interests.push(interest.interest);
            });
            user.interests = interests;

            // get list of friends
            const friendsParams = {
                TableName: process.env.CYCLIC_DB,
                KeyConditionExpression: "pk = :pk",
                FilterExpression: "sender = :email OR receiver = :email",
                ExpressionAttributeValues: {
                    ":pk": "friends",
                    ":email": userEmail,
                },
            };
            const friendsRes = await client.query(friendsParams).promise();
            var requests = [];
            var friends = [];
            friendsRes.Items.forEach((person) => {
                const sender = person.sender;
                const receiver = person.receiver;
                if (person.accepted) {
                    if (sender == userEmail) {
                        friends.push(receiver);
                    } else {
                        friends.push(sender);
                    }
                } else {
                    if (receiver == userEmail) {
                        requests.push(sender);
                    }
                }
            });

            user.requests = requests;
            user.friends = friends;

            // list of created meetings
            const meetingsParams = {
                TableName: process.env.CYCLIC_DB,
                KeyConditionExpression: "pk = :pk and begins_with(sk, :email)",
                ExpressionAttributeValues: {
                    ":pk": "meetings",
                    ":email": userEmail,
                },
            };
            const meetingsRes = await client.query(meetingsParams).promise();
            if (meetingsRes.Items) {
                user.meetings = meetingsRes.Items;
            } else {
                user.meetings = [];
            }
        }
        return user;
    }

    async updateUserInfo(userEmail, userName, affiliation, international) {
        const client = new AWS.DynamoDB.DocumentClient();
        const primaryKey = {
            pk: "users",
            sk: userEmail,
        };
        const updateExpression =
            "SET userName = :userName, affiliation = :affiliation, international = :international";
        const expressionAttributeValues = {
            ":userName": userName,
            ":affiliation": affiliation,
            ":international": international,
        };

        const params = {
            TableName: process.env.CYCLIC_DB,
            Key: primaryKey,
            UpdateExpression: updateExpression,
            ExpressionAttributeValues: expressionAttributeValues,
        };
        try {
            await client.update(params).promise();
            console.log("User's info updated successfully");
        } catch (err) {
            console.error("Error updating user's info: ", err);
        }
    }
    async removeInterest(userEmail, interest) {
        const client = new AWS.DynamoDB.DocumentClient();
        const params = {
            TableName: process.env.CYCLIC_DB,
            Key: { pk: "interests", sk: `${userEmail}-${interest}` },
        };
        try {
            await client.delete(params).promise();
            console.log("Interest removed successfully");
        } catch (err) {
            console.error("Error removing interest: ", err);
        }
    }
    async addInterest(userEmail, interest) {
        const client = new AWS.DynamoDB.DocumentClient();
        const params = {
            TableName: process.env.CYCLIC_DB,
            Item: {
                pk: "interests",
                sk: `${userEmail}-${interest}`,
                interest: interest,
            },
        };
        try {
            await client.put(params).promise();
            console.log("Interest added successfully");
        } catch (err) {
            console.error("Error adding interest: ", err);
        }
    }
    async getRecommended(interests) {
        try {
            console.log(typeof interests);
            const client = new AWS.DynamoDB.DocumentClient();
            var res = {};
            var promises = [];
            const pk = "interests";
            if (interests.length == 0) {
                return res;
            }
            interests = JSON.parse(interests);
            interests.forEach(async (interest) => {
                var promise = new Promise(async (resolve, reject) => {
                    const params = {
                        TableName: process.env.CYCLIC_DB,
                        KeyConditionExpression: "pk = :pk ",
                        FilterExpression: "interest = :interest",
                        ExpressionAttributeValues: {
                            ":pk": pk,
                            ":interest": interest,
                        },
                    };
                    const result = await client.query(params).promise();
                    const people = result.Items;
                    people.forEach((person) => {
                        // parse for email from sk
                        let email = person.sk.split("-")[0];

                        if (!res.hasOwnProperty(email)) {
                            res[email] = [interest];
                        } else {
                            res[email].push(interest);
                        }
                    });

                    resolve();
                });
                promises.push(promise);
            });
            await Promise.all(promises);
            return res;
        } catch (error) {
            console.log(error);
            return {};
        }
    }
    async addFriend(userEmail, friendEmail) {
        const client = new AWS.DynamoDB.DocumentClient();
        // if no friend request, add new request
        const pk = "friends";
        const sk1 = `${userEmail}-${friendEmail}`;
        const sk2 = `${friendEmail}-${userEmail}`;

        const params1 = {
            TableName: process.env.CYCLIC_DB,
            KeyConditionExpression: "pk = :pk and sk = :sk",
            ExpressionAttributeValues: {
                ":pk": pk,
                ":sk": sk1,
            },
        };
        const params2 = {
            TableName: process.env.CYCLIC_DB,
            KeyConditionExpression: "pk = :pk and sk = :sk",
            ExpressionAttributeValues: {
                ":pk": pk,
                ":sk": sk2,
            },
        };
        const result1 = await client.query(params1).promise();
        const result2 = await client.query(params2).promise();
        if (result1.Items.length == 0 && result2.Items.length == 0) {
            // add new request
            const item = {
                pk: "friends",
                sk: sk1,
                accepted: false,
                sender: userEmail,
                receiver: friendEmail,
            };
            const params = {
                TableName: process.env.CYCLIC_DB,
                Item: item,
            };
            await client.put(params).promise();
        } else if (result1.Items.length != 0) {
            // update
            const updateExpression = "SET accepted = :accepted";
            const expressionAttributeValues = {
                ":accepted": true,
            };
            const params = {
                TableName: process.env.CYCLIC_DB,
                Key: { pk: "friends", sk: sk1 },
                UpdateExpression: updateExpression,
                ExpressionAttributeValues: expressionAttributeValues,
            };
            await client.update(params).promise();
        } else if (result2.Items.length != 0) {
            // update
            const updateExpression = "SET accepted = :accepted";
            const expressionAttributeValues = {
                ":accepted": true,
            };
            const params = {
                TableName: process.env.CYCLIC_DB,
                Key: { pk: "friends", sk: sk2 },
                UpdateExpression: updateExpression,
                ExpressionAttributeValues: expressionAttributeValues,
            };
            try {
                await client.update(params).promise();
            } catch (err) {
                console.log("Error removing friend: ", err);
            }
        }
    }
    async removeFriend(userEmail, friendEmail) {
        const client = new AWS.DynamoDB.DocumentClient();
        const sk1 = `${userEmail}-${friendEmail}`;
        const sk2 = `${friendEmail}-${userEmail}`;

        const params1 = {
            TableName: process.env.CYCLIC_DB,
            Key: { pk: "friends", sk: sk1 },
        };
        const params2 = {
            TableName: process.env.CYCLIC_DB,
            Key: { pk: "friends", sk: sk2 },
        };
        try {
            await client.delete(params1).promise();
            await client.delete(params2).promise();
        } catch (err) {
            console.log("Error removing friend: ", err);
        }
    }
}
module.exports = UserDAO;
