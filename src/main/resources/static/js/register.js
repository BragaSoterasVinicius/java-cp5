const form = document.getElementById("registerForm");
const errorMessage = document.getElementById("errorMessage");
const successMessage = document.getElementById("successMessage");
const registerButton = document.getElementById("registerButton");

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    errorMessage.textContent = "";
    successMessage.textContent = "";
    registerButton.disabled = true;

    const payload = {
        nome: document.getElementById("nome").value.trim(),
        email: document.getElementById("email").value.trim(),
        senha: document.getElementById("senha").value
    };

    try {
        const response = await fetch("/api/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const message = await response.text();
            throw new Error(message || `Registration failed (${response.status})`);
        }

        successMessage.textContent =
            "Account created successfully! Redirecting to login...";

        setTimeout(() => {
            window.location.href = "/login";
        }, 1200);

    } catch (error) {
        errorMessage.textContent = error.message;
    } finally {
        registerButton.disabled = false;
    }
});
