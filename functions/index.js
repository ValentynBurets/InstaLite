"use strict";

const functions = require("firebase-functions");
const nodemailer = require("nodemailer");

const gmailEmail = functions.config().gmail.email;
const gmailPassword = functions.config().gmail.password;

const mailTransport = nodemailer.createTransport({
  service: "gmail",
  auth: {
    user: gmailEmail,
    pass: gmailPassword,
  },
});

const APP_NAME = "InstaLite";

exports.sendWelcomeEmail = functions.auth.user().onCreate((user) => {
  const email = user.email;
  const displayName = user.displayName;

  return sendWelcomeEmail(email, displayName);
});

/**
 * Add two numbers.
 * @param {string} email The first number.
 * @param {string} displayName The second number.
 * @return {Void} The sum of the two numbers.
 */
function sendWelcomeEmail(email, displayName) {
  const mialOptions = {
    from: `${APP_NAME} <noreplay@firebase.com`,
    to: email,
  };

  mialOptions.subject = `Welcome to ${APP_NAME}!`;
  mialOptions.text = `Hey ${displayName || ""}! Welcome to ${APP_NAME}, 
  we hope  you enjoy our service.`;

  mailTransport.sendMail(mialOptions);
  console.log("New email sent to", email);

  return null;
}
