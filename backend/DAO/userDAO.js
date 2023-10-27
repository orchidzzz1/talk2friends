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
        this.client = new AWS.DynamoDB.DocumentClient();
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
            const params = {
                TableName: process.env.CYCLIC_DB,
                Item: item,
            };
            await this.client.put(params).promise();
            // add interests
        } catch (error) {
            console.log("Error adding user: ", error);
        }
    }

    async getUserInfo(userEmail) {
        // get userinfo, list of interests, list of friends, and list of created meetings
        const pk = "user";
        const sk = userEmail;
        const params = {
            TableName: process.env.CYCLIC_DB,
            KeyConditionExpression: "pk = :pk and sk = :sk",
            ExpressionAttributeValues: {
                ":pk": pk,
                ":sk": sk,
            },
        };
        const result = await this.client.getItem(params).promise();
        var user = result.Item;
        if (user) {
            // get list of interests
            const interestParams = {
                TableName: process.env.CYCLIC_DB,
                KeyConditionExpression: "pk = :pk and begins_with(sk, :email)",
                ExpressionAttributeValues: {
                    ":pk": "interests",
                    ":email": userEmail,
                },
            };
            const interestRes = await this.client
                .query(interestParams)
                .promise();
            user.interests = interestRes.Items;

            // get list of friends
            const friendsParams = {
                TableName: process.env.CYCLIC_DB,
                KeyConditionExpression: "pk = :pk and contains(sk, :email)",
                ExpressionAttributeValues: {
                    ":pk": "interests",
                    ":email": userEmail,
                },
            };
            const friendsRes = await this.client.query(friendsParams).promise();
            var requests = [];
            var friends = [];
            friendsRes.Items.forEach((entry) => {
                if (person.accepted) {
                    friends.push(person);
                } else {
                    requests.push(person);
                }
            });
            user.requests = requests;
            user.friends = friends;

            // list of created meetings
            const meetingsParams = {
                TableName: process.env.CYCLIC_DB,
                KeyConditionExpression: "pk = :pk and contains(sk, :email)",
                ExpressionAttributeValues: {
                    ":pk": "meetings",
                    ":email": userEmail,
                },
            };
            const meetingsRes = await this.client
                .query(friendsParams)
                .promise();
            if (meetingsRes.Items) {
                user.meetings = meetingsRes.Items;
            } else {
                user.meetings = [];
            }
        }
        return user;
    }

    async updateUserInfo(userEmail, name, affiliation, international) {
        const primaryKey = {
            pk: "users",
            sk: userEmail,
        };
        const updateExpression =
            "SET name = :name, affiliation = :affiliation, international = :international";
        const expressionAttributeValues = {
            ":name": name,
            ":affiliation": affiliation,
            ":international": international,
        };

        const params = {
            TableName: process.env.CYCLIC_DB,
            Key: primaryKey,
            UpdateExpression: updateExpression,
            ExpressionAttributeValues: expressionAttributeValues,
        };
        this.client.update(params, (err, data) => {
            if (err) {
                console.error("Error updating user's info: ", err);
            } else {
                console.log("User's info updated successfully");
            }
        });
    }
    async removeInterest(userEmail, interest) {
        const params = {
            pk: "interests",
            sk: `${userEmail}-${interest}`,
        };
        this.client.delete(params, (err, data) => {
            if (err) {
                console.error("Error removing interest: ", err);
            } else {
                console.log("Interest removed successfully");
            }
        });
    }
    async addInterest(userEmail, interest) {
        const params = {
            pk: "interests",
            sk: `${userEmail}-${interest}`,
        };
        this.client.put(params, (err, data) => {
            if (err) {
                console.error("Error adding interest: ", err);
            } else {
                console.log("Interest added successfully");
            }
        });
    }
    async getRecommended(interests) {
        var res = {};
        var recommendedList = [];
        interests.forEach(async (interest) => {
            const pk = "interests";

            const params = {
                TableName: process.env.CYCLIC_DB,
                KeyConditionExpression: "pk = :pk and contains(sk, :interest))",
                ExpressionAttributeValues: {
                    ":pk": pk,
                    ":interest": interest,
                },
            };
            const result = await this.client.query(params).promise();
            const people = result.Items;
            people.forEach((person) => {
                if (!res.hasOwnProperty(person)) {
                    res.person = [interest];
                } else {
                    res.person.push(interest);
                }
            });
        });
        return recommendedList;
    }
    async addFriend(userEmail, friendEmail) {
        // if no friend request, add new request
        const pk = "user";
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
        const result1 = await this.client.getItem(params1).promise();
        const result2 = await this.client.getItem(params2).promise();
        if (!result1.Item && !result2.Item) {
            // add new request
            const item = {
                pk: "user",
                sk: sk1,
                accepted: false,
            };
            const params = {
                TableName: process.env.CYCLIC_DB,
                Item: item,
            };
            await this.client.put(params).promise();
        } else if (result1.Item) {
            // update
            const updateExpression = "SET accepted = :accepted";
            const expressionAttributeValues = {
                ":accepted": accepted,
            };
            const params = {
                TableName: process.env.CYCLIC_DB,
                Key: { pk: "friends", sk: sk1 },
                UpdateExpression: updateExpression,
                ExpressionAttributeValues: expressionAttributeValues,
            };
            await this.client.update(params).promise();
        } else if (result2.Item) {
            // update
            const updateExpression = "SET accepted = :accepted";
            const expressionAttributeValues = {
                ":accepted": accepted,
            };
            const params = {
                TableName: process.env.CYCLIC_DB,
                Key: { pk: "friends", sk: sk2 },
                UpdateExpression: updateExpression,
                ExpressionAttributeValues: expressionAttributeValues,
            };
            await this.client.update(params).promise();
        }
    }
    async removeFriend(userEmail, friendEmail) {
        const sk1 = `${userEmail}-${friendEmail}`;
        const sk2 = `${friendEmail}-${userEmail}`;

        const params1 = {
            pk: "interests",
            sk: sk1,
        };
        const params2 = {
            pk: "interests",
            sk: sk2,
        };
        try {
            await this.client.delete(params1).promise();
            await this.client.delete(params2).promise();
        } catch (error) {
            console.log("Error removing friend: ", error);
        }
    }
}
module.exports = UserDAO;
