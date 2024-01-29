const express = require("express");
const http = require("http");
const app = express();
const { Server } = require("socket.io");
const path = require("path");
const mongoose = require("mongoose");

const Msg = require("./models/models");

const mongoUrl =
  "mongodb+srv://geekysayan:sayan2003@cluster0.itllhoq.mongodb.net/?retryWrites=true&w=majority";

mongoose
  .connect(mongoUrl)
  .then(() => {
    console.log("Database Connected...");
  })
  .catch(() => {
    console.log("Failed to connect");
  });

const server = http.createServer(app);

const io = new Server(server);

io.on("connection", (socket) => {
  console.log("A new user has connected", socket.id);

  Msg.find()
    .then((res) => {
      console.log(res);
      socket.emit("output-message", res);
    })
    .catch((err) => {
      console.log(err);
    });

  socket.on("join", function (userNickname) {
    console.log(userNickname + " : has joined the chat ");
    socket.broadcast.emit(
      "userjoinedthechat",
      userNickname + " : has joined the chat "
    );
  });

  socket.emit("message", "Hello world");

  socket.on("messagedetection", (name, message) => {
    console.log(name + " : " + message);
    let messages = { message: message, senderNickname: name };

    const message_db = new Msg({ msg: message });

    message_db.save().then(() => {
      // send the message to the client side
      io.emit("message", messages);
      console.log("message", messages);
    });
  });

  socket.on("disconnect", function () {
    console.log("user has left ");
    socket.broadcast.emit("userdisconnect", " user has left");
  });
});

app.use(express.static(path.resolve("./")));

app.get("/", (req, res) => {
  res.send("Chat server is running");
});

server.listen(3000, () => {
  console.log("Node app is running on port 3000");
});

///socket.io/socket.io.js
