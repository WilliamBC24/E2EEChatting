'use strict'

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
        if (data) {
          localStorage.setItem("user", JSON.stringify(data));
          window.location.href = "/chat";
        } else {
          alert("Invalid credentials");
        }
      })
      .catch((error) => console.error("Error:", error));
  };