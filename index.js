const express = require("express");
const http = require("http");
const app = express();
const { Server } = require("socket.io");
const path = require("path");

const server = http.createServer(app);

const io = new Server(server);

io.on("connection", (socket) => {
  console.log("A new user has connected", socket.id);
  socket.on("join", function (userNickname) {
    console.log(userNickname + " : has joined the chat ");
    socket.broadcast.emit(
      "userjoinedthechat",
      userNickname + " : has joined the chat "
    );
  });

  socket.on("messagedetection", (name, message) => {
    console.log(name + " : " + message);
    let messages = { message: message, senderNickname: name };

    // send the message to the client side

    socket.emit("message", messages);
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
