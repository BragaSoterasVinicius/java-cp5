const form = document.getElementById("registerAdminForm");
const button = document.getElementById("registerAdminButton");
const errorMessage = document.getElementById("errorMessage");
const successMessage = document.getElementById("successMessage");

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    errorMessage.textContent = "";
    successMessage.textContent = "";
    button.disabled = true;
    button.textContent = "Creating account...";

    const request = {
        nome: document.getElementById("nome").value.trim(),
        email: document.getElementById("email").value.trim(),
        senha: document.getElementById("senha").value
    };

    try {
        const response = await fetch("/api/auth/register/admin", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(request)
        });

        if (!response.ok) {
            const message = await response.text();
            throw new Error(
                message || `Registration failed (${response.status}).`
            );
        }

        const admin = await response.json();

        successMessage.textContent =
            `Admin account created for ${admin.email}.`;

        form.reset();

    } catch (error) {
        errorMessage.textContent =
            error.message || "Could not create the admin account.";
    } finally {
        button.disabled = false;
        button.textContent = "Create admin account";
    }
});