"use strict";

import { saveUsername, getKey, saveKey } from "./indexDB.js";
import { generateKeypair, convertTo } from "./sodiumEncryption.js";

const login = document.querySelector(".btn-login");

const submitLogin = () => {
  const user = {
    username: document.getElementById("username").value,
    password: document.getElementById("password").value,
  };

  // fetch("/gateway/authservice/auth/login", {
  fetch("http://localhost:8090/auth/auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(user),
    credentials: "include",
  })
    .then((response) => response.json())
    .then((data) => {
      //Validate this
      if (data) {
        if (data.success === true) {
          const name = {
            username: data.data.username,
          };
          fetch("http://localhost:8090/message/user/add", {
            method: "PUT",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(name),
            credentials: "include",
          })
            .then((res) => res.json())
            .then(async (e) => {
              localStorage.setItem("user", name.username);
              await saveUsername(name.username);
              const key = await getKey();
              if (!key) {
                const newKey = await generateKeypair();
                const to = await convertTo(newKey.publicKey);
                await saveKey(newKey.privateKey);
                const savePublic = await fetch(
                  "http://localhost:8090/message/user/updateKey",
                  {
                    method: "PUT",
                    headers: {
                      "Content-Type": "application/json",
                    },
                    body: JSON.stringify({
                      username: name.username,
                      key: to,
                    }),
                    credentials: "include",
                  }
                );
                await savePublic.json()
              }
              window.location.href = "/chat";
            });
        } else {
          alert("Invalid credentials");
        }
      }
    })
    .catch((error) => console.error("Error:", error));
};

login.onclick = submitLogin;
