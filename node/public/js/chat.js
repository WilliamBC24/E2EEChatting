"use strict";

const chatPage = document.querySelector("#chat-page");
const messageForm = document.querySelector("#messageForm");
const messageInput = document.querySelector("#message");
const chatArea = document.querySelector("#chat-messages");
const logout = document.querySelector("#logout");
const connectedUsersList = document.querySelector("#connectedUsers");
const connectedUser = document.querySelector("#connected-user");

//Bonus
// window.addEventListener("beforeunload", () => {
//   stompClient.disconnect()
// });

//If you send data to the server with socket.send(), 
//it wont go to any destination, just to the server
//you would have to registerWebSocketHandler to handle it
//which introduces complexity
const socket = new WebSocket("ws://localhost:8090/message/ws");

socket.addEventListener('open', event => {
  console.log('WebSocket connection established!');
});
socket.addEventListener('close', event => {
  console.log('WebSocket connection closed:', event.code, event.reason);
});
socket.addEventListener('error', error => {
  console.error('WebSocket error:', error);
});

let stompClient = null;
let user = null;
let selectedUser = null;

const connect = () => {
  stompClient = Stomp.over(socket);
  stompClient.connect({}, onConnected, onError);
};

// await is similar to .then
// use async await when getUserList requires output from fetch
// await is basically a checkpoint
// no need to worry about memory usage as the consts are short-lived 
const onConnected = async () => {
  const res = await fetch("http://localhost:8090/message/user/connect", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(user),
    credentials: "include",
  });
  const data = await res.json();
  if (data) {
    localStorage.setItem("chatList", JSON.stringify(data));
    getOnlineUserList();
  }
}

const onError = () => {};

const getOnlineUserList = async () => {
  const res = await fetch('http://localhost:8090/message/user/users', {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
  });
  let data = await res.json();
  data = data.filter(u => u.username !== user.username)
  connectedUsersList.innerHTML = '';
  data.forEach((e, index) => {
    appendUserToList(e, connectedUsersList);
    if (index < data.length-1) {
      const separator = document.createElement('li');
      separator.classList.add('separator');
      connectedUsersList.appendChild(separator);
    }
  });
}

const appendUserToList = (u, target) => {
  const listItem = document.createElement('li');
  listItem.classList.add('user-item');
  listItem.id = u.username;

  const userImage = document.createElement('img');
  userImage.src = '../img/user_icon.png';

  const usernameSpan = document.createElement('span');
  usernameSpan.textContent = u.username;

  // const receivedMsgs = document.createElement('span');
  // receivedMsgs.textContent = '0';
  // receivedMsgs.classList.add('nbr-msg', 'hidden');

  listItem.appendChild(userImage);
  listItem.appendChild(usernameSpan);
  // listItem.appendChild(receivedMsgs);

  //Will have to write this
  // listItem.addEventListener('click', userItemClick);

  connectedUsersList.appendChild(listItem);
}

// const getOnlineUserList = () => {
//   fetch('http://localhost:8081/users', {
//     method: "GET",
//     headers: {
//       "Content-Type": "application/json",
//     },
//     credentials: "include",
//   }).then(res => res.json())
//   .then(data => {
//     if (data) {
//       localStorage.setItem("onlineList", JSON.stringify(data));
//     }
//   })
// };

document.addEventListener("DOMContentLoaded", () => {
  user = {
    username: "sonbui"
  }
  // username = JSON.parse(localStorage.getItem("user"));
  // if (!username) {
  //   alert("Can't find user in local storage");
  //   return;
  // }
  connectedUser.textContent = user.username;
  connect();
});
