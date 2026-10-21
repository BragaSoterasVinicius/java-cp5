# CP5 E-commerce

Aplicação de e-commerce desenvolvida com Spring Boot para o projeto CP5 da FIAP pelo INTELLIJ. O sistema combina uma interface web renderizada no servidor com endpoints REST para autenticação e gerenciamento de produtos.
## Documentação
<img width="1920" height="1080" alt="Screenshot 2026-10-18 233543" src="https://github.com/user-attachments/assets/d318c5c4-cc5d-49fb-8f6a-d1b447907bb4" />
collection: https://www.postman.com/amandaba-group/workspace/rm566230

## Funcionalidades

- Cadastro e login de usuários.
- Navegação de acordo com o perfil autenticado (`CLIENTE` ou `ADMIN`).
- Catálogo de produtos acessível a clientes e administradores.
- Gerenciamento de produtos por administradores: criação, listagem, consulta, atualização e exclusão.
- Páginas administrativas para categorias e vendas.
- Páginas HTML com Thymeleaf, além de arquivos estáticos CSS e JavaScript.
- Validação dos dados de produtos, incluindo nome, preço, estoque e status de ativação.

## Tecnologias

- Java
- Spring Boot
- Spring MVC e REST
- Spring Security
- Thymeleaf
- Maven
- Banco de dados relacional configurado na aplicação

> As versões exatas do Java e do Spring Boot, o banco de dados utilizado e as configurações de conexão devem ser conferidos no `pom.xml` e nos arquivos de configuração da aplicação.

## Pré-requisitos

- JDK compatível com a versão definida no `pom.xml`.
- Maven (ou o Maven Wrapper, caso esteja incluído no repositório).
- Banco de dados em execução e configurado para a aplicação.

## Como executar

### 1. Clone o repositório

```bash
git clone https://github.com/BragaSoterasVinicius/java-cp5
cd https://github.com/BragaSoterasVinicius/java-cp5
```

### 2. Configure o banco de dados

Confira o arquivo `src/main/resources/application.properties` ou `src/main/resources/application.yml` e configure as propriedades de conexão com o banco de dados.

### 3. Compile o projeto

Com o Maven instalado:

```bash
mvn clean package
```

Se o projeto possuir Maven Wrapper, você também pode executar:

```bash
./mvnw clean package
```

No Windows, usando PowerShell:

```powershell
.\mvnw.cmd clean package
```

### 4. Execute a aplicação

Com Maven:

```bash
mvn spring-boot:run
```

No Windows, usando o Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

Ou execute o JAR gerado:

```bash
java -jar target/*.jar
```

Se a porta não tiver sido alterada na configuração, acesse:

- Página inicial: `http://localhost:8080/`
- Login: `http://localhost:8080/login`
- Cadastro de cliente: `http://localhost:8080/register`
- Catálogo de produtos: `http://localhost:8080/produtos`

## Perfis de usuário e permissões

O sistema diferencia os seguintes perfis:

| Perfil | Acesso previsto |
|---|---|
| `CLIENTE` | Consultar o catálogo e acessar funcionalidades do cliente, como o explorer o catalogo de compras. |
| `ADMIN` | Acessar páginas administrativas e executar operações de escrita em produtos. |

A interface pode exibir opções diferentes conforme o perfil do usuário autenticado. Porém, ocultar um botão no navegador é apenas um recurso visual: a autorização também deve ser aplicada no servidor por meio do Spring Security, nas rotas e nos endpoints correspondentes.

## Principais rotas

### Páginas web

| Rota | Finalidade |
|---|---|
| `/` | Página inicial |
| `/login` | Página de login |
| `/register` | Cadastro de cliente |
| `/register-admin` | Cadastro de administrador, conforme as regras de segurança configuradas |
| `/produtos` | Catálogo de produtos |
| `/produtos/gerenciar` | Página de gerenciamento de produtos para administradores |

Outras páginas administrativas podem incluir `/admin`, `/admin/categorias` e `/admin/vendas`, conforme os controllers presentes no projeto.

### API de autenticação

| Método | Endpoint | Finalidade |
|---|---|---|
| `POST` | `/api/auth/login` | Autenticar um usuário |
| `POST` | `/api/auth/register` | Cadastrar um cliente |
| `POST` | `/api/auth/register/admin` | Cadastro de administrador, conforme as regras de segurança configuradas |

### API de produtos

Prefixo: `/api/produtos`

| Método | Endpoint | Finalidade |
|---|---|---|
| `GET` | `/api/produtos` | Listar produtos |
| `GET` | `/api/produtos/{id}` | Consultar um produto pelo ID |
| `POST` | `/api/produtos` | Criar um produto — acesso de administrador |
| `PUT` | `/api/produtos/{id}` | Atualizar um produto — acesso de administrador |
| `DELETE` | `/api/produtos/{id}` | Excluir um produto — acesso de administrador |

As regras efetivas de autorização são definidas pela configuração do Spring Security do projeto.

## Exemplo de requisição para cadastrar um produto

Exemplo de corpo JSON para `POST /api/produtos`:

```json
{
  "nome": "Teclado Mecânico",
  "descricao": "Teclado mecânico ABNT2",
  "preco": 249.90,
  "estoque": 15,
  "categoriaId": 1,
  "ativo": "S"
}
```

O campo `ativo` recebe um caractere: `"S"` para ativo e `"N"` para inativo, conforme as regras de validação do modelo de produto. O valor de `categoriaId` deve corresponder a uma categoria existente.

## Estrutura do projeto

O projeto segue a estrutura convencional de uma aplicação Spring Boot. Entre os diretórios relevantes estão:

```text
src/
└── main/
    ├── java/
    │   └── com/fiap/cp5pt/
    │       ├── config/
    │       ├── controller/
    │       ├── service/
    │       ├── repository/
    │       └── model/          # ou pacotes entity/dto, conforme a implementação
    └── resources/
        ├── templates/          # páginas HTML com Thymeleaf
        └── static/
            ├── css/
            └── js/
```

Os nomes dos pacotes e subdiretórios podem variar conforme a organização atual do código.

## Validação e segurança

- Os dados enviados à aplicação devem ser validados no servidor. A validação feita apenas no navegador não é suficiente para proteger o sistema.
- Mantenha as operações exclusivas de administradores protegidas pelo Spring Security, mesmo quando os botões correspondentes estiverem ocultos para clientes.
- Não deixe o cadastro de administradores publicamente acessível em produção, a menos que isso seja intencional.
- Mantenha as credenciais do banco de dados fora do controle de versão.
- Antes de publicar a aplicação, utilize HTTPS e revise as configurações de cookies e sessão.

## Testes

Execute os testes com:

```bash
mvn test
```

Ou, se o Maven Wrapper estiver disponível:

```powershell
.\mvnw.cmd test
```
