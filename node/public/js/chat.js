"use strict";

import { encrypt, decrypt, convertFrom, deriveSharedKey } from "./sodiumEncryption.js";
import {getKey, getRoom, getUsername, saveRoom} from "./indexDB.js"

const chatPage = document.querySelector("#chat-page");
const messageInput = document.querySelector("#message");
const chatArea = document.querySelector("#chat-messages");
const logout = document.querySelector("#logout");
const connectedUsersList = document.querySelector("#connectedUsers");
const friendList = document.querySelector("#friend");
const connectedUser = document.querySelector("#connected-user");
const sendButton = document.querySelector("#sendButton");
const chatBanner = document.querySelector("#chat-banner");
const friendUsernames = new Set();

let currentRoom;
let newChat = false;
let currentTarget;
let currentSub;

sendButton.onclick = () => {
  sendMessage();
};
messageInput.onkeydown = (e) => {
  if (e.key === "Enter") {
    sendMessage();
  }
};
window.onbeforeunload = () => {
  stompClient.disconnect();
};

//If you send data to the server with socket.send(),
//it wont go to any destination, just to the server
//you would have to registerWebSocketHandler to handle it
//which introduces complexity
// const socket = new WebSocket("ws://sonbui.com/gateway/message/ws");
const socket = new WebSocket("ws://localhost:8090/message/ws");

socket.addEventListener("open", (event) => {
  console.log("WebSocket connection established!");
});
socket.addEventListener("close", (event) => {
  console.log("WebSocket connection closed:", event.code, event.reason);
});
socket.addEventListener("error", (error) => {
  console.error("WebSocket error:", error);
});

let stompClient = null;
let user = null;
let selectedUser = null;

const connect = () => {
  stompClient = Stomp.over(socket);
  stompClient.connect({}, onConnected, onError);
};

const sendMessage = async () => {
  const text = messageInput.value;
  //? checks if var is null/undefined
  //if trim returns an empty string then it wont run
  //because "" is falsy
  messageInput.value = "";

  if (newChat) {
    await createChatOnInitiation();
    await fetchOldMessages(currentRoom);
  }

  if (text?.trim()) {
    const allKeys = await getAllKeys();
    const encryptedMessage = await encrypt(text, await deriveSharedKey(allKeys.ourKey, allKeys.otherKey));
    let msg = JSON.stringify({
      ...encryptedMessage,
      //get current chat
      chatId: currentRoom,
      sender: user.username,
      timestamp: Date.now(),
    });
    stompClient.send("/app/chat", {}, msg);
  }
};

const getAllKeys = async () => {
  const room = await getRoom(currentRoom);
  const otherKey = room.key;
  const ourKey = await getKey()
  return {
    ourKey: ourKey.key,
    otherKey: otherKey,
  }
}

const onConnected = async () => {
  // subscribeToChat();
  //close connection to old chat when user clicks on a new chat
  setOnlineStatusAndGetUserList();
  // getOnlineUserList();
};

const onError = () => {};

const subscribeToChat = (id) => {
  if (currentSub) {
    currentSub.unsubscribe();
  }
  currentSub = stompClient.subscribe("/chatbox/" + id, async (m) => {
    const message = JSON.parse(m.body);
    const messageForDecryption = {
      content: message.content,
      nonce: message.nonce,
    }
    const allKeys = await getAllKeys();
    //Add sent/read status
    //Add a fallback if there is no internet, the message should still show up
    if (message.sender !== user.username) {
      addReceivedMessageToChatArea(await decrypt(messageForDecryption, await deriveSharedKey(allKeys.ourKey, allKeys.otherKey)));
    } else {
      addSentMessageToChatArea(await decrypt(messageForDecryption, await deriveSharedKey(allKeys.ourKey, allKeys.otherKey)));
      chatArea.scrollTop = chatArea.scrollHeight;
    }
  });
  currentRoom = id;
};

const setOnlineStatusAndGetUserList = async () => {
  // const res = await fetch("/gateway/message/user/connect", {
  await fetch("http://localhost:8090/message/user/connect", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(user),
    credentials: "include",
  });
  await renderPeople();
};

const renderPeople = async () => {
  friendList.innerHTML = "";
  connectedUsersList.innerHTML = "";
  const data = await fetch("http://localhost:8090/message/user/rooms", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      username: user.username,
    }),
    credentials: "include",
  });
  const rooms = await data.json();
  //gotta add pagination
  for (const room of rooms.data) {
    const id = room.room.chatId;
    const otherKey = await convertFrom(room.key);
    const ourKey = await getKey()
    const sharedKey = await deriveSharedKey(ourKey.key, otherKey);
    const roomToSave = {
      id: id,
      key: sharedKey,
    }
    await saveRoom(roomToSave)
    const participant = room.room.participants.find((p) => p !== user.username);
    appendFriendToList(participant, id);
    friendUsernames.add(participant);
  }
  const onlineUsers = await fetch("http://localhost:8090/message/user/users");
  const onlineList = await onlineUsers.json();
  for (const online of onlineList.data) {
    online.username !== user.username &&
      !friendUsernames.has(online.username) &&
      appendOnlineToList(online.username);
  }
};

const fetchOldMessages = async (room) => {
  try {
    chatArea.innerHTML = "";
    const past = await fetch(`http://localhost:8090/message/message/` + room);
    const messages = await past.json();
    const messageElements = [];
    let decryptedList = [];
    const allKeys = await getAllKeys();
    const sharedKey = await deriveSharedKey(allKeys.ourKey, allKeys.otherKey);
    for(const mes of messages.data) {
      const messageForDecryption = {
        content: mes.content,
        nonce: mes.nonce,
      }
      let decrypted = await decrypt(messageForDecryption, sharedKey);
      decrypted = {
        content: decrypted,
        sender: mes.sender,
      }
      decryptedList.push(decrypted)
    }
    for (const mes of decryptedList) {
      const messageDiv = document.createElement("div");
      messageDiv.classList.add("message-bubble");
      if (mes.sender !== user.username) {
        messageDiv.classList.add("reveicer");
      } else {
        messageDiv.classList.add("sender");
      }
      messageDiv.textContent = mes.content;
      messageElements.push(messageDiv);
    }

    for (const div of messageElements) {
      chatArea.appendChild(div);
    }

    chatArea.scrollTop = chatArea.scrollHeight;
  } catch (e) {
    console.log(e);
  }
};

const renderChatBanner = (roomUser) => {
  const banner = document.getElementById("chat-banner");
  banner.innerHTML = ""; // Clear previous content

  const container = document.createElement("div");
  container.style.display = "flex";
  container.style.alignItems = "center";
  container.style.justifyContent = "space-between";
  container.style.padding = "10px";
  container.style.borderBottom = "1px solid #ccc";
  container.style.backgroundColor = "#f9f9f9";

  const leftSide = document.createElement("div");
  leftSide.style.display = "flex";
  leftSide.style.alignItems = "center";

  const img = document.createElement("img");
  img.src = "../cat.png";
  img.style.width = "40px";
  img.style.height = "40px";
  img.style.borderRadius = "50%";
  img.style.marginRight = "10px";

  const name = document.createElement("span");
  name.textContent = roomUser;
  name.style.fontWeight = "bold";

  leftSide.appendChild(img);
  leftSide.appendChild(name);

  const rightSide = document.createElement("div");
  rightSide.textContent = "⋮"; // menu placeholder
  rightSide.style.cursor = "pointer";
  rightSide.style.fontSize = "20px";

  container.appendChild(leftSide);
  container.appendChild(rightSide);
  banner.appendChild(container);
};

const addReceivedMessageToChatArea = (message) => {
  const messageDiv = document.createElement("div");
  messageDiv.classList.add("message-bubble");
  messageDiv.classList.add("reveicer");

  messageDiv.textContent = message;

  chatArea.appendChild(messageDiv);
};

const addSentMessageToChatArea = (message) => {
  const messageDiv = document.createElement("div");
  messageDiv.classList.add("message-bubble");
  messageDiv.classList.add("sender");

  messageDiv.textContent = message;

  chatArea.appendChild(messageDiv);
};

const appendFriendToList = (u, room) => {
  const listItem = document.createElement("li");
  listItem.classList.add("user-item");
  listItem.id = u;

  const userImage = document.createElement("img");
  userImage.src = "../cat.png";

  const usernameSpan = document.createElement("span");
  usernameSpan.textContent = u;

  // const receivedMsgs = document.createElement('span');
  // receivedMsgs.textContent = '0';
  // receivedMsgs.classList.add('nbr-msg', 'hidden');

  listItem.appendChild(userImage);
  listItem.appendChild(usernameSpan);
  // listItem.appendChild(receivedMsgs);

  listItem.addEventListener("click", async () => {
    newChat = false;

    currentTarget = "";
    subscribeToChat(room);
    await fetchOldMessages(room);
    renderChatBanner(u);
    messageInput.value = '';
  });

  friend.appendChild(listItem);
};

const appendOnlineToList = (u) => {
  const listItem = document.createElement("li");
  listItem.classList.add("user-item");
  listItem.id = u;

  const userImage = document.createElement("img");
  userImage.src = "../cat.png";

  const usernameSpan = document.createElement("span");
  usernameSpan.textContent = u;

  // const receivedMsgs = document.createElement('span');
  // receivedMsgs.textContent = '0';
  // receivedMsgs.classList.add('nbr-msg', 'hidden');

  listItem.appendChild(userImage);
  listItem.appendChild(usernameSpan);
  // listItem.appendChild(receivedMsgs);

  listItem.addEventListener("click", () => {
    newChat = true;
    currentTarget = u;
    chatArea.innerHTML = "";
    renderChatBanner(u);
    messageInput.value = '';
  });

  connectedUsersList.appendChild(listItem);
};

const createChatOnInitiation = async () => {
  const participants = {
    participants: [user.username, currentTarget],
  };
  const newRoom = await fetch(
    "http://localhost:8090/message/message/createChat",
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(participants),
      credentials: "include",
    }
  );
  const chatId = await newRoom.json();
  currentRoom = chatId.data;
  newChat = false;
  await renderPeople();

  document.getElementById(currentTarget)?.click();
};

document.addEventListener("DOMContentLoaded", async () => {
  user = await getUsername();
  if (!user) {
    window.location.href = "/login";
  }
  user = {
    username: user.username,
  };
  connectedUser.textContent = user.username;
  connect();
});