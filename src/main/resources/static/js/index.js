
const productsContainer = document.getElementById("products");
const statusMessage = document.getElementById("statusMessage");

const userInfo = document.getElementById("userInfo");
const loginLink = document.getElementById("loginLink");
const registerLink = document.getElementById("registerLink");
const logoutButton = document.getElementById("logoutButton");

// Read the user information saved by login.js.
const userJson = sessionStorage.getItem("user");
const user = userJson ? JSON.parse(userJson) : null;

if (user) {
    userInfo.textContent = `Welcome, ${user.nome}`;
    loginLink.hidden = true;
    registerLink.hidden = true;
    logoutButton.hidden = false;
}
document.addEventListener("DOMContentLoaded", () => {
    const userData = sessionStorage.getItem("user");

    const loginLink = document.getElementById("loginLink");
    const registerLink = document.getElementById("registerLink");
    const logoutButton = document.getElementById("logoutButton");
    const welcomeMessage = document.getElementById("welcomeMessage");

    const clienteOptions = document.getElementById("clienteOptions");
    const adminOptions = document.getElementById("adminOptions");

    if (!userData) {
        return;
    }

    const user = JSON.parse(userData);

    loginLink.hidden = true;
    registerLink.hidden = true;
    logoutButton.hidden = false;

    welcomeMessage.textContent = `Welcome, ${user.nome}!`;

    if (user.papel === "ADMIN") {
        adminOptions.hidden = false;
    } else if (user.papel === "CLIENTE") {
        clienteOptions.hidden = false;
    }

    logoutButton.addEventListener("click", async () => {
        // Clear the local UI state.
        sessionStorage.removeItem("user");

        // Reload the homepage.
        window.location.href = "/";
    });
});

logoutButton.addEventListener("click", async () => {
    // This clears the frontend user information.
    // It does not invalidate the server-side session.
    sessionStorage.removeItem("user");
    window.location.href = "/login";
});

async function loadProducts() {
    try {
        const response = await fetch("/api/produtos");

        if (response.status === 401 || response.status === 403) {
            throw new Error("You must be logged in to browse products.");
        }

        if (!response.ok) {
            throw new Error("Failed to load products.");
        }

        const products = await response.json();

        productsContainer.replaceChildren();

        if (products.length === 0) {
            statusMessage.textContent = "No products available yet.";
            return;
        }

        statusMessage.textContent = "";

        products.forEach(product => {
            const card = document.createElement("article");
            card.className = "product-card";

            const name = document.createElement("h3");
            name.textContent = product.nome;

            const description = document.createElement("p");
            description.textContent = product.descricao || "";

            const price = document.createElement("p");
            price.textContent = Number(product.preco).toLocaleString(
                "pt-BR",
                { style: "currency", currency: "BRL" }
            );

            card.append(name, description, price);
            productsContainer.appendChild(card);
        });

    } catch (error) {
        statusMessage.textContent = error.message;
    }
}

loadProducts();