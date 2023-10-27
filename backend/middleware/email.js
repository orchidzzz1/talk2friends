const admin = require("firebase-admin");

async function send(to, subject, text) {
    try {
        await admin.messaging().send({
            data: {
                to: to,
                subject: subject,
                text: text,
            },
        });
    } catch (error) {
        console.error("Error sending email:", error);
    }
}
module.exports = { send };
