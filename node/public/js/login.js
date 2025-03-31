const submitLogin = () => {
    event.preventDefault();

    const user = {
      username: document.getElementById("username").value,
      password: document.getElementById("password").value,
    };

    fetch("/gateway/authservice/auth/login", {
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
          alert(JSON.parse(data));
          // localStorage.setItem("user", JSON.stringify(data))
          //setup login session then redirect to home page
        } else {
          alert("Invalid credentials");
        }
      })
      .catch((error) => console.error("Error:", error));
  };