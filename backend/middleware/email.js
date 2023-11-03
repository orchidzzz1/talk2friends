// var SibApiV3Sdk = require("sib-api-v3-sdk");
require("dotenv").config();
var nodemailer = require("nodemailer");

async function send(to, subject, text) {
    // var defaultClient = SibApiV3Sdk.ApiClient.instance;
    // // Configure API key authorization: api-key
    // var apiKey = defaultClient.authentications["api-key"];
    // apiKey.apiKey = process.env.BREVO_API_KEY;
    // var apiInstance = new SibApiV3Sdk.TransactionalEmailsApi();
    // var sendSmtpEmail = new SibApiV3Sdk.SendSmtpEmail(); // SendSmtpEmail | Values to send a transactional email

    // sendSmtpEmail = {
    //     sender: { email: "talk2friends-noreply@gmail.com" },
    //     to: [
    //         {
    //             email: to,
    //         },
    //     ],
    //     replyTo: { email: "hangtngu@usc.edu" },
    //     subject: subject,
    //     htmlContent: text,
    //     headers: {
    //         "X-Mailin-custom":
    //             "custom_header_1:custom_value_1|custom_header_2:custom_value_2",
    //     },
    // };
    // //"<html><body><h1>Common: This is my first transactional email {{params.parameter}}</h1></body></html>"
    // apiInstance.sendTransacEmail(sendSmtpEmail).then(
    //     function (data) {
    //         console.log("API called successfully. Returned data: " + data);
    //     },
    //     function (error) {
    //         console.error(error);
    //     }
    // );
    var transporter = nodemailer.createTransport({
        service: "gmail",
        host: "smtp.gmail.com",
        port: 465,
        secure: true,
        auth: {
            user: "noreplytalk2friends@gmail.com",
            pass: process.env.EMAIL_PW,
        },
    });

    var mailOptions = {
        from: "Talk2Friends",
        to: to,
        subject: subject,
        text: text,
    };

    transporter.sendMail(mailOptions, function (error, info) {
        if (error) {
            console.log(error);
        } else {
            console.log("Email sent: " + info.response);
        }
    });
}
module.exports = { send };
