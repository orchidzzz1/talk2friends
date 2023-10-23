const admin = require("firebase-admin");
const express = require("express");
const cors = require("cors");
require("dotenv").config();
const user = require("../controllers/userServices");
const meeting = require("../controllers/meetingServices");
const verify = require("../middleware/verify");
/**
 * Express server
 */
class Server {
    constructor() {
        this.app = express();
        this.port = process.env.PORT; // Loaded from .env file

        this.middlewares();
        this.routes();
    }

    middlewares() {
        this.app.use(cors()); // Enable CORS
        this.app.use(express.json({ limit: "50mb" }));
        // const serviceAccount = require("./../trojanmarket-firebase.json");
        // use this concatenation for cyclic deployment --> env var max char = 255
        const FIREBASE_PRIVATE_KEY =
            process.env.FIREBASE_PRIVATE_KEY1 +
            process.env.FIREBASE_PRIVATE_KEY2 +
            process.env.FIREBASE_PRIVATE_KEY3 +
            process.env.FIREBASE_PRIVATE_KEY4 +
            process.env.FIREBASE_PRIVATE_KEY5 +
            process.env.FIREBASE_PRIVATE_KEY6 +
            process.env.FIREBASE_PRIVATE_KEY7;
        // const FIREBASE_PRIVATE_KEY = process.env.FIREBASE_PRIVATE_KEY;
        admin.initializeApp({
            credential: admin.credential.cert({
                type: "service_account",
                project_id: process.env.FIREBASE_PROJECT_ID,
                private_key_id: process.env.FIREBASE_PRIVATE_KEY_ID,
                private_key: FIREBASE_PRIVATE_KEY.replace(/\\n/g, "\n"),
                client_email: process.env.FIREBASE_CLIENT_EMAIL,
                client_id: process.env.FIREBASE_CLIENT_ID,
                auth_uri: "https://accounts.google.com/o/oauth2/auth",
                token_uri: "https://oauth2.googleapis.com/token",
                auth_provider_x509_cert_url:
                    "https://www.googleapis.com/oauth2/v1/certs",
                client_x509_cert_url: process.env.FIREBASE_CLIENT_X509_CERT_URL,
                universe_domain: "googleapis.com",
            }),
        });
    }

    // Bind controllers to routes
    routes() {
        // routes that do not need authorization
        this.app.post("/api/user/verify", user.verifyRegistration);
        this.app.post("/api/user/register", user.register);

        // middleware for verifying authorization
        this.app.use(verify);

        // user routes
        this.app.get("/api/user/get", user.get);
        this.app.post("/api/user/add", user.add);
        this.app.post("/api/user/update", user.updateUserInfo);
        this.app.post("/api/user/friends/add", user.addFriend);
        this.app.post("/api/user/friends/remove", user.removeFriend);
        this.app.post("/api/user/addInterest", user.addInterest);
        this.app.post("/api/user/removeInterest", user.removeInterest);
        this.app.get("/api/user/friends/recommended", user.getRecommended);
        this.app.post("/api/user/recommend", user.sendRecommendation);

        // meeting routes
        this.app.get("/api/meeting/get", meeting.getAllMeetings);
        this.app.post("/api/meeting/edit", meeting.editMeeting);
        this.app.post("api/meeting/create", meeting.createMeeting);
        this.app.post("api/meeting/addParticipant", meeting.addParticipant);
        this.app.post(
            "api/meeting/removeParticipant",
            meeting.removeParticipant
        );
    }

    listen() {
        this.app.listen(this.port, () => {
            console.log("Server running on port: ", this.port);
        });
    }
}

module.exports = Server;
