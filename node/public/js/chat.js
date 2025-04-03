"use strict";

const chatPage = document.querySelector("#chat-page");
const messageForm = document.querySelector("#messageForm");
const messageInput = document.querySelector("#message");
const chatArea = document.querySelector("#chat-messages");
const logout = document.querySelector("#logout");
const connectedUsersList = document.querySelector(".connectedUsers");
const connectedUser = document.querySelector("#connected-user");

// const socket = new SockJS("http://localhost:8090/message/ws");
const socket = new WebSocket("http://localhost:8090/message/ws");

let stompClient = null;
let username = null;
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
  const res = await fetch("http://localhost:8090/message/user.connect", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(username),
    credentials: "include",
  });
  const data = await res.json();
  if (data) {
    localStorage.setItem("chatList", JSON.stringify(data));
    getOnlineUserList();
  }
}

// const onConnected = () => {
//   fetch("http://localhost:8081/user.connect", {
//     method: "POST",
//     headers: {
//       "Content-Type": "application/json",
//     },
//     body: JSON.stringify(username),
//     credentials: "include",
//   })
//     .then(res => res.json())
//     .then(data => {
//       if (data) {
//         localStorage.setItem("chatList", JSON.stringify(data));
//         getOnlineUserList();
//       }
//     });
// };

const onError = () => {};

const getOnlineUserList = async () => {
  const res = await fetch('http://localhost:8090/message/users', {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
  });
  let data = await res.json();
  data = data.filter(u => u.username !== username)
  connectedUsersList.innerHTML = '';
  data.forEach((e, index) => {
    appendUserToList(e, connectedUsersList);
    if (index < data.length-1) {
      const separator = document.createElement(li);
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

  listItem.addEventListener('click', userItemClick);

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
  username = "sonbui";
  // username = JSON.parse(localStorage.getItem("user"));
  // if (!username) {
  //   alert("Can't find user in local storage");
  //   return;
  // }
  connectedUser.textContent = username;
  connect();
});
