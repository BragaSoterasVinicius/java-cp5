
const loginForm = document.getElementById("loginForm");
const loginButton = document.getElementById("loginButton");
const errorMessage = document.getElementById("errorMessage");

loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    errorMessage.textContent = "";
    loginButton.disabled = true;
    loginButton.textContent = "Signing in...";

    const credentials = {
        email: document.getElementById("email").value,
        senha: document.getElementById("senha").value
    };

    try {
        const response = await fetch("/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(credentials)
        });

        if (!response.ok) {
            throw new Error("Invalid email or password.");
        }

        const data = await response.json();

        if (!data.token) {
            throw new Error(
                "Login succeeded, but the response did not contain a token."
            );
        }

        sessionStorage.setItem("token", data.token);

        window.location.href = "/admin";
    } catch (error) {
        errorMessage.textContent =
            error.message || "Unable to log in. Please try again.";
    } finally {
        loginButton.disabled = false;
        loginButton.textContent = "Sign in";
    }
});
