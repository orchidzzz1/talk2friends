const Database = require("../DAO/userDAO");
const AWS = require("aws-sdk");
const db = new Database();
require("dotenv").config();

describe("user", () => {
    test("add user", async () => {
        const user = {
            email: "trojan1@usc.edu",
            international: false,
            affiliation: "CS",
            userName: "Trojan",
            interests: [],
        };
        await db.addUser(user);
        const res = await db.getUserInfo("trojan1@usc.edu");
        expect(res).not.toBeNull();
        expect(res.sk).toBe("trojan1@usc.edu");
        expect(res.international).toBeFalsy();
        expect(res.affiliation).toBe("CS");
        expect(res.userName).toBe("Trojan");
    }, 50000);

    test("update user info", async () => {
        await db.updateUserInfo("trojan1@usc.edu", "Trojan", "CSBA", false);
        const res = await db.getUserInfo("trojan1@usc.edu");
        expect(res).not.toBeNull();
        expect(res.sk).toBe("trojan1@usc.edu");
        expect(res.international).toBeFalsy();
        expect(res.affiliation).toBe("CSBA");
        expect(res.userName).toBe("Trojan");
    }, 50000);

    test("add interest", async () => {
        await db.addInterest("trojan1@usc.edu", "sports");
        const res = await db.getUserInfo("trojan1@usc.edu");
        expect(res).not.toBeNull();
        const interests = res.interests;
        expect(interests.length).toEqual(1);
        expect(interests[0]).toBe("sports");
    }, 50000);

    test("add friend request", async () => {
        await db.addFriend("trojan2@usc.edu", "trojan1@usc.edu");
        const res = await db.getUserInfo("trojan1@usc.edu");
        expect(res).not.toBeNull();
        const requests = res.requests;
        expect(requests.length).toEqual(1);
        expect(requests[0]).toBe("trojan2@usc.edu");
    }, 50000);
    describe("turn request to friend", () => {
        test("accept friend request", async () => {
            await db.addFriend("trojan1@usc.edu", "trojan2@usc.edu");
            const res = await db.getUserInfo("trojan1@usc.edu");
            expect(res).not.toBeNull();
            const friends = res.friends;
            expect(friends.length).toEqual(1);
            expect(friends[0]).toBe("trojan2@usc.edu");
            const requests = res.requests;
            expect(requests.length).toEqual(0);
        }, 50000);
    });

    describe("removal", () => {
        test("remove friend", async () => {
            await db.removeFriend("trojan1@usc.edu", "trojan2@usc.edu");
            const res = await db.getUserInfo("trojan1@usc.edu");
            expect(res).not.toBeNull();
            const friends = res.friends;
            expect(friends.length).toEqual(0);
        }, 50000);
        test("remove interest", async () => {
            await db.removeInterest("trojan1@usc.edu", "sports");
            const res = await db.getUserInfo("trojan1@usc.edu");
            expect(res).not.toBeNull();
            const interests = res.interests;
            expect(interests.length).toEqual(0);
        }, 50000);
    });

    afterAll(async () => {
        AWS.config.update({
            accessKeyId: process.env.AWS_ACCESS_KEY_ID,
            secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY,
            sessionToken: process.env.AWS_SESSION_TOKEN,
            region: process.env.AWS_REGION,
        });

        const client = new AWS.DynamoDB.DocumentClient();

        // delete user
        const params = {
            TableName: process.env.CYCLIC_DB,
            Key: {
                pk: "users",
                sk: "trojan1@usc.edu",
            },
        };
        await client.delete(params).promise();
    }, 50000);
});
describe("recommendation", () => {
    beforeEach(async () => {
        await db.addInterest("trojan2@usc.edu", "sports");
        await db.addInterest("trojan3@usc.edu", "anime");
    }, 50000);
    test("get recommended friends", async () => {
        const friends = await db.getRecommended(["sports"]);
        expect(Object.keys(friends).length).toEqual(1);
        expect(Object.keys(friends)[0]).toBe("trojan2@usc.edu");
        expect(friends["trojan2@usc.edu"].length).toEqual(1);
    }, 50000);

    afterEach(async () => {
        await db.removeInterest("trojan2@usc.edu", "sports");
        await db.removeInterest("trojan3@usc.edu", "anime");
    }, 50000);
});
