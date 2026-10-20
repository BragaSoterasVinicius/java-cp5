const loginForm = document.getElementById("loginForm");
const loginButton = document.getElementById("loginButton");
const errorMessage = document.getElementById("errorMessage");

loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    errorMessage.textContent = "";
    loginButton.disabled = true;
    loginButton.textContent = "Signing in...";

    const credentials = {
        email: document.getElementById("email").value.trim(),
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
            throw new Error(
                response.status === 401
                    ? "Invalid email or password."
                    : "Login failed. Please try again."
            );
        }

        const user = await response.json();

        // Store the user data returned by your Java backend.
        sessionStorage.setItem("user", JSON.stringify(user));

        // Redirect according to the user's role.
        if (user.papel === "ADMIN") {
            window.location.href = "/admin";
        } else {
            window.location.href = "/";
        }

    } catch (error) {
        errorMessage.textContent =
            error.message || "Unable to log in. Please try again.";
    } finally {
        loginButton.disabled = false;
        loginButton.textContent = "Sign in";
    }
});