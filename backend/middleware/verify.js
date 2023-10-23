// verify token from frontend for all routes
const admin = require("firebase-admin");

const verify = (req, res, next) => {
    const token = req.headers.authorization;

    if (!token) {
        return res.status(401).json({ error: "Token is missing" });
    }

    admin
        .auth()
        .verifyIdToken(token)
        .then(async (decodedToken) => {
            const uid = decodedToken.uid;
            try {
                const userRecord = await admin.auth().getUser(uid);
                const email = userRecord.providerData.find(
                    (provider) => provider.providerId === "google.com"
                ).email;
                req.query.userEmail = email;
                next();
            } catch (error) {
                console.error("Email not found: ", error);
                throw error;
            }
        })
        .catch((error) => {
            return res.status(403).json({ error: "Invalid token: ", error });
        });
};

module.exports = verify;
