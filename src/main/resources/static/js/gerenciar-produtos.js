
const API_URL = "/api/produtos";

const form = document.getElementById("productForm");
const formSection = document.getElementById("productFormSection");
const formTitle = document.getElementById("formTitle");
const formMessage = document.getElementById("formMessage");
const listErrorMessage = document.getElementById("listErrorMessage");
const statusMessage = document.getElementById("statusMessage");
const tableBody = document.getElementById("productsTableBody");

const productId = document.getElementById("productId");
const nome = document.getElementById("nome");
const descricao = document.getElementById("descricao");
const preco = document.getElementById("preco");
const estoque = document.getElementById("estoque");
const categoriaId = document.getElementById("categoriaId");
const ativo = document.getElementById("ativo");

const saveButton = document.getElementById("saveProductButton");
const cancelButton = document.getElementById("cancelEditButton");

function formatarPreco(valor) {
    const numero = Number(valor);

    return Number.isFinite(numero)
        ? numero.toLocaleString("pt-BR", {
            style: "currency",
            currency: "BRL"
        })
        : "—";
}

function formatarData(data) {
    if (!data) return "—";

    const dataObj = new Date(data);

    return Number.isNaN(dataObj.getTime())
        ? "—"
        : dataObj.toLocaleString("pt-BR");
}

function criarCelula(valor) {
    const cell = document.createElement("td");
    cell.textContent = valor ?? "—";
    return cell;
}

async function requisicao(url, options = {}) {
    const response = await fetch(url, {
        ...options,
        headers: {
            Accept: "application/json",
            ...(options.body
                ? { "Content-Type": "application/json" }
                : {}),
            ...options.headers
        },
        credentials: "same-origin"
    });

    if (!response.ok) {
        let mensagem = `Erro HTTP ${response.status}`;

        try {
            const body = await response.json();

            mensagem =
                body.message ||
                body.mensagem ||
                body.detail ||
                body.title ||
                mensagem;
        } catch {
            // A resposta pode não ser JSON.
        }

        throw new Error(mensagem);
    }

    if (response.status === 204) return null;

    return response.json();
}

function adicionarBotao(texto, classe, callback) {
    const button = document.createElement("button");

    button.type = "button";
    button.className = classe;
    button.textContent = texto;
    button.addEventListener("click", callback);

    return button;
}

function renderizarProduto(produto) {
    const row = document.createElement("tr");

    row.appendChild(criarCelula(produto.id));
    row.appendChild(criarCelula(produto.nome));
    row.appendChild(criarCelula(formatarPreco(produto.preco)));
    row.appendChild(criarCelula(produto.estoque));
    row.appendChild(criarCelula(produto.categoriaId ?? "Sem categoria"));
    row.appendChild(
        criarCelula(produto.ativo === 'S' ? "Ativo" : "Inativo")
    );

    const actionsCell = document.createElement("td");
    actionsCell.className = "table-actions";

    actionsCell.appendChild(
        adicionarBotao("Consultar", "secondary-button", () =>
            consultarProduto(produto.id)
        )
    );

    actionsCell.appendChild(
        adicionarBotao("Editar", "secondary-button", () =>
            prepararEdicao(produto)
        )
    );

    actionsCell.appendChild(
        adicionarBotao("Excluir", "danger-button", () =>
            excluirProduto(produto.id, produto.nome)
        )
    );

    row.appendChild(actionsCell);
    tableBody.appendChild(row);
}

async function listarProdutos() {
    statusMessage.textContent = "Carregando produtos...";
    listErrorMessage.textContent = "";
    tableBody.replaceChildren();

    try {
        const produtos = await requisicao(API_URL);

        if (!Array.isArray(produtos)) {
            throw new Error("A API não retornou uma lista de produtos.");
        }

        produtos.forEach(renderizarProduto);

        statusMessage.textContent = produtos.length
            ? `${produtos.length} produto(s) encontrado(s).`
            : "Nenhum produto cadastrado.";
    } catch (error) {
        statusMessage.textContent = "";
        listErrorMessage.textContent = error.message;
    }
}

async function consultarProduto(id) {
    try {
        const produto = await requisicao(`${API_URL}/${id}`);

        alert(
            `ID: ${produto.id}\n` +
            `Nome: ${produto.nome}\n` +
            `Descrição: ${produto.descricao ?? "—"}\n` +
            `Preço: ${formatarPreco(produto.preco)}\n` +
            `Estoque: ${produto.estoque ?? 0}\n` +
            `Categoria ID: ${produto.categoriaId ?? "Sem categoria"}\n` +
            `Status: ${produto.ativo === "S" ? "Ativo" : "Inativo"}\n` +
            `Criado em: ${formatarData(produto.criadoEm)}`
        );
    } catch (error) {
        alert(`Erro ao consultar produto: ${error.message}`);
    }
}

function prepararEdicao(produto) {
    productId.value = produto.id;
    nome.value = produto.nome ?? "";
    descricao.value = produto.descricao ?? "";
    preco.value = produto.preco ?? "";
    estoque.value = produto.estoque ?? 0;
    categoriaId.value = produto.categoriaId ?? "";
    ativo.value = produto.ativo ?? 'S';

    formTitle.textContent = `Editar produto #${produto.id}`;
    saveButton.textContent = "Salvar alterações";
    cancelButton.hidden = false;
    formMessage.textContent = "";

    formSection.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
}

function limparFormulario() {
    form.reset();

    productId.value = "";
    estoque.value = "0";
    categoriaId.value = "";
    ativo.value = 'S';

    formTitle.textContent = "Cadastrar produto";
    saveButton.textContent = "Cadastrar produto";
    cancelButton.hidden = true;
    formMessage.textContent = "";
}

function montarPayload() {
    return {
        nome: nome.value.trim(),
        descricao: descricao.value.trim() || null,
        preco: Number(preco.value),
        estoque: Number(estoque.value),
        categoriaId: categoriaId.value.trim() === ""
            ? null
            : Number(categoriaId.value),
        ativo: ativo.value
    };
}

form.addEventListener("submit", async event => {
    event.preventDefault();
    formMessage.textContent = "";

    if (!form.reportValidity()) return;

    const editando = productId.value !== "";

    const url = editando
        ? `${API_URL}/${productId.value}`
        : API_URL;

    saveButton.disabled = true;

    try {
        await requisicao(url, {
            method: editando ? "PUT" : "POST",
            body: JSON.stringify(montarPayload())
        });

        limparFormulario();
        await listarProdutos();

        statusMessage.textContent = editando
            ? "Produto atualizado com sucesso."
            : "Produto cadastrado com sucesso.";
    } catch (error) {
        formMessage.textContent = error.message;
    } finally {
        saveButton.disabled = false;
    }
});

async function excluirProduto(id, nomeProduto) {
    if (!confirm(`Deseja excluir "${nomeProduto}" (ID ${id})?`)) {
        return;
    }

    try {
        await requisicao(`${API_URL}/${id}`, {
            method: "DELETE"
        });

        if (productId.value === String(id)) {
            limparFormulario();
        }

        await listarProdutos();
        statusMessage.textContent = "Produto excluído com sucesso.";
    } catch (error) {
        listErrorMessage.textContent = error.message;
    }
}

document.getElementById("newProductButton")
    .addEventListener("click", () => {
        limparFormulario();

        formSection.scrollIntoView({
            behavior: "smooth",
            block: "start"
        });

        nome.focus();
    });

cancelButton.addEventListener("click", limparFormulario);

document.getElementById("reloadProductsButton")
    .addEventListener("click", listarProdutos);

listarProdutos();
