
const productsGrid = document.getElementById("productsGrid");
const loadingMessage = document.getElementById("loadingMessage");
const errorMessage = document.getElementById("errorMessage");
const emptyMessage = document.getElementById("emptyMessage");
const reloadButton = document.getElementById("reloadProducts");

async function carregarProdutos() {
    productsGrid.replaceChildren();
    loadingMessage.hidden = false;
    errorMessage.hidden = true;
    emptyMessage.hidden = true;

    try {
        const response = await fetch("/api/produtos", {
            headers: {
                Accept: "application/json"
            },
            credentials: "same-origin"
        });

        if (!response.ok) {
            throw new Error(
                response.status === 401 || response.status === 403
                    ? "You do not have permission to view these products."
                    : "Failed to load products."
            );
        }

        const produtos = await response.json();

        if (!Array.isArray(produtos)) {
            throw new Error("Invalid response received from the products API.");
        }

        const produtosAtivos = produtos.filter(
            produto => produto.ativo === "S"
        );

        if (produtosAtivos.length === 0) {
            emptyMessage.hidden = false;
            return;
        }

        produtosAtivos.forEach(produto => {
            const card = document.createElement("article");
            card.className = "product-card";

            const nome = document.createElement("h3");
            nome.textContent = produto.nome ?? "Unnamed product";

            const descricao = document.createElement("p");
            descricao.textContent =
                produto.descricao || "No description available.";

            const preco = document.createElement("p");
            const valor = Number(produto.preco);

            preco.textContent = Number.isFinite(valor)
                ? valor.toLocaleString("pt-BR", {
                    style: "currency",
                    currency: "BRL"
                })
                : "Price unavailable";

            const estoque = document.createElement("p");
            estoque.textContent = `Stock: ${produto.estoque ?? 0}`;

            const categoria = document.createElement("p");
            categoria.textContent =
                `Category: ${produto.categoria?.nome ?? "Uncategorized"}`;

            card.append(nome, descricao, preco, estoque, categoria);
            productsGrid.appendChild(card);
        });

        if (productsGrid.childElementCount === 0) {
            emptyMessage.hidden = false;
        }
    } catch (error) {
        errorMessage.textContent =
            error.message || "An unexpected error occurred.";
        errorMessage.hidden = false;
    } finally {
        loadingMessage.hidden = true;
    }
}

reloadButton.addEventListener("click", carregarProdutos);

carregarProdutos();
