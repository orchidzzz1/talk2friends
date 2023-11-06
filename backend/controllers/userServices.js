const Database = require("./../DAO/userDAO");
const db = new Database();
const codeServices = require("../middleware/code");
const emailServices = require("../middleware/email");

async function authorize(req, res) {
    // check if user exists already
    const userEmail = req.query.userEmail;
    const user = await db.getUserInfo(userEmail);

    if (user) {
        // return true and redirect to get user
        res.status(200).json({ isVerified: true, user: user });
    } else {
        // else redirect to send verification code
        res.status(200).json({ isVerified: false });
    }
}

async function get(req, res) {
    try {
        const user = await db.getUserInfo(req.query.userEmail);
        res.status(200).json({ user: user });
    } catch (error) {
        res.sendStatus(500);
    }
}
async function add(req, res) {
    try {
        const data = req.body;
        await db.addUser(data.user);
        res.sendStatus(200);
    } catch (error) {
        res.sendStatus(500);
    }
}

async function updateUserInfo(req, res) {
    try {
        const data = req.body;
        await db.updateUserInfo(
            req.query.userEmail,
            data.name,
            data.affiliation,
            data.international
        );
        res.sendStatus(200);
    } catch (error) {
        res.sendStatus(500);
    }
}
async function addInterest(req, res) {
    try {
        const data = req.body;
        await db.addInterest(req.query.userEmail, data.interest);
        res.sendStatus(200);
    } catch (error) {
        res.sendStatus(500);
    }
}
async function removeInterest(req, res) {
    try {
        const data = req.body;
        await db.removeInterest(req.query.userEmail, data.interest);
        res.sendStatus(200);
    } catch (error) {
        res.sendStatus(500);
    }
}
async function getRecommended(req, res) {
    try {
        const data = req.body;
        const users = await db.getRecommended(data.interests);
        res.status(200).json({ users: users });
    } catch (error) {
        res.sendStatus(500);
    }
}

async function sendVerification(req, res) {
    try {
        const code = codeServices.generate6DigitCode();
        const userEmail = req.query.userEmail;
        // send email for new code
        const subject = "Here's your verification code from Talk2Friends";
        const body = `Verification code: ${code.toString()}. \n\nThis code expires in 2 minutes. Enter this code to continue with your registration. \n\nTalk2Friends Team`;
        emailServices.send(userEmail, subject, body);
        console.log(code);
        // add new code to cache
        codeServices.addCode(userEmail, code);
        res.status(200).json({ isVerified: false });
    } catch (error) {
        res.sendStatus(500);
    }
}
async function addFriend(req, res) {
    try {
        const data = req.body;
        await db.addFriend(req.query.userEmail, data.friendEmail);
        res.sendStatus(200);
    } catch (error) {
        res.sendStatus(500);
    }
}
async function removeFriend(req, res) {
    try {
        const data = req.body;
        await db.removeFriend(req.query.userEmail, data.friendEmail);
        res.sendStatus(200);
    } catch (error) {
        res.sendStatus(500);
    }
}
async function sendRecommendation(req, res) {
    try {
        // send email
        const data = req.body;
        const email = data.email;
        const userName = data.userName;
        const linkToApp = "";
        const subject = `Invite from ${userName} to join Talk2Friends`;
        const body = `Hello! \n\nMeet Talk2Friends!\nIt's the app I use to connect with students at USC and practice my language skills with. It has been a great app that helped me to improve as well as to have fun! \n\nTry it out and see you in the app! \n${linkToApp}\n\n${userName}`;
        await emailServices.send(email, subject, body);
        res.sendStatus(200);
    } catch (error) {
        res.sendStatus(500);
    }
}
async function verify(req, res) {
    try {
        const data = req.body;
        if (codeServices.verifyCode(req.query.userEmail, data.code)) {
            res.status(200).json({ isVerified: true });
        } else {
            res.status(200).json({ isVerified: false });
        }
    } catch (error) {
        res.sendStatus(500);
    }
}

module.exports = {
    authorize,
    getRecommended,
    get,
    add,
    updateUserInfo,
    addInterest,
    removeFriend,
    removeInterest,
    addFriend,
    sendRecommendation,
    sendVerification,
    verify,
};
