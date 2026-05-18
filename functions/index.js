const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.sendChatNotification = functions.firestore
    .document("chats/{chatId}/messages/{messageId}")
    .onCreate(async (snapshot, context) => {

        const messageData = snapshot.data();

        const senderId = messageData.senderId;
        const receiverId = messageData.receiverId;
        const text = messageData.text;

        try {

            const userDoc = await admin
                .firestore()
                .collection("users")
                .doc(receiverId)
                .get();

            if (!userDoc.exists) {
                console.log("Receiver not found");
                return null;
            }

            const userData = userDoc.data();

            const token = userData.fcmToken;

            if (!token) {
                console.log("No FCM token");
                return null;
            }

            const payload = {
                notification: {
                    title: "New Message",
                    body: text,
                },
                data: {
                    senderId: senderId,
                    receiverId: receiverId,
                }
            };

            await admin.messaging().sendToDevice(token, payload);

            console.log("Notification sent");

            return null;

        } catch (error) {

            console.error(error);

            return null;
        }
    });