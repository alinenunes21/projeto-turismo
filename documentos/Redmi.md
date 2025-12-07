# 🏖️ PROJETO TURISMO - DOCUMENTAÇÃO BACKEND

## 📋 INFORMAÇÕES DO PROJETO

**Desenvolvido por:** Aline Nunes (Backend) + Ayumi (Frontend)  
**Data de Entrega:**10/12/2025 
---

## 🚀 TECNOLOGIAS UTILIZADAS

### **Backend Stack:**
- ☕ **Java 17** - Linguagem principal
- 🌱 **Spring Boot 3.4.12** - Framework principal
- 🗄️ **PostgreSQL** - Banco principal (usuários, pontos, avaliações)
- 🍃 **MongoDB** - Banco NoSQL (comentários, fotos)
- 🔥 **Redis** - Cache (configurado)
- 🏗️ **Hibernate/JPA** - ORM
- 📊 **Spring Data** - Repositórios

### **Estrutura Arquitetural:**
```
Controller → Service → Repository → Database
```

---

## 🛠️ CONFIGURAÇÃO DO AMBIENTE

### **1. Pré-requisitos para Desenvolvimento:**
- ✅ **Java 17** instalado
- ✅ **PostgreSQL** rodando (localhost:5432)
- ✅ **MongoDB** instalado e rodando (localhost:27017)
- ✅ **IDE** (IntelliJ, Eclipse, VS Code)

### **2. Banco de Dados:**

#### **PostgreSQL:**
- **Host:** localhost:5432
- **Database:** turismo_db
- **Username:** postgres
- **Password:** (vazio)

#### **MongoDB:**
- **Host:** localhost:27017
- **Database:** turismo_db
- **Interface:** MongoDB Compass (opcional)

### **3. Inicialização:**
```bash
# Navegar para pasta do backend
cd projeto-turismo/backend

# Executar Spring Boot
mvn spring-boot:run
# OU
./mvnw spring-boot:run

# Aplicação rodará em: http://localhost:8080
```

---

## 📊 ESTRUTURA DO BANCO DE DADOS

### **PostgreSQL (Dados Estruturados):**

#### **Tabela: usuarios**
```sql
id (BIGINT, PK, AUTO_INCREMENT)
nome (VARCHAR(200), NOT NULL)
email (VARCHAR(200), UNIQUE, NOT NULL)  
senha_hash (VARCHAR(255), NOT NULL)
role (VARCHAR(50), DEFAULT 'ROLE_USER')
created_at (TIMESTAMP)
```

#### **Tabela: pontos_turisticos**
```sql
id (BIGINT, PK, AUTO_INCREMENT)
nome (VARCHAR(200), NOT NULL)
descricao (TEXT)
cidade (VARCHAR(100), NOT NULL)
estado (VARCHAR(100), NOT NULL) 
pais (VARCHAR(100), DEFAULT 'Brasil')
latitude (DOUBLE)
longitude (DOUBLE)
endereco (VARCHAR(300))
criado_por (BIGINT, FK → usuarios.id)
created_at (TIMESTAMP)
```

#### **Tabela: avaliacoes**
```sql
id (BIGINT, PK, AUTO_INCREMENT)
ponto_id (BIGINT, FK → pontos_turisticos.id)
usuario_id (BIGINT, FK → usuarios.id)
nota (INTEGER, 1-5, NOT NULL)
comentario (TEXT)
created_at (TIMESTAMP)
```

### **MongoDB (Dados Flexíveis):**

#### **Collection: comentarios**
```json
{
  "_id": "ObjectId",
  "pontoId": "Long",
  "usuarioId": "Long", 
  "texto": "String",
  "createdAt": "LocalDateTime",
  "metadata": "Object",
  "respostas": [
    {
      "usuarioId": "Long",
      "nomeUsuario": "String", 
      "texto": "String",
      "data": "String"
    }
  ]
}
```

#### **Collection: fotos**
```json
{
  "_id": "ObjectId",
  "pontoId": "Long",
  "usuarioId": "Long",
  "filename": "String",
  "titulo": "String", 
  "path": "String",
  "contentType": "String",
  "tamanho": "Long",
  "createdAt": "LocalDateTime"
}
```

---

## 🌐 API ENDPOINTS COMPLETOS

### **🔐 AUTENTICAÇÃO**

#### **Cadastro de Usuário**
```http
POST /api/auth/register
Content-Type: application/json

{
    "nome": "Nome Completo",
    "email": "email@exemplo.com", 
    "senha": "123456"
}

Response 201:
{
    "id": 1,
    "nome": "Nome Completo",
    "email": "email@exemplo.com",
    "role": "ROLE_USER"
}
```

#### **Login**
```http
POST /api/auth/login
Content-Type: application/json

{
    "email": "email@exemplo.com",
    "senha": "123456"
}

Response 200:
{
    "id": 1,
    "nome": "Nome Completo", 
    "email": "email@exemplo.com",
    "role": "ROLE_USER",
    "message": "Login realizado com sucesso!"
}
```

---

### **🏖️ PONTOS TURÍSTICOS**

#### **Listar Todos (com Paginação e Filtros)**
```http
GET /api/pontos
GET /api/pontos?page=0&size=10&sortBy=nome&sortDir=asc
GET /api/pontos?cidade=Rio&estado=RJ&nome=Cristo

Response 200:
{
    "content": [
        {
            "id": 1,
            "nome": "Cristo Redentor",
            "descricao": "Monumento famoso",
            "cidade": "Rio de Janeiro", 
            "estado": "RJ",
            "pais": "Brasil",
            "latitude": -22.9519,
            "longitude": -43.2105, 
            "endereco": "Parque Nacional da Tijuca",
            "criadoPor": 1,
            "createdAt": "2025-12-07T16:04:15"
        }
    ],
    "pageable": {...},
    "totalElements": 1,
    "totalPages": 1
}
```

#### **Buscar por ID**
```http
GET /api/pontos/{id}

Response 200: {objeto do ponto turístico}
```

#### **Criar Ponto Turístico**
```http
POST /api/pontos
Content-Type: application/json

{
    "nome": "Cristo Redentor",
    "descricao": "Monumento famoso do Rio de Janeiro",
    "cidade": "Rio de Janeiro",
    "estado": "RJ", 
    "latitude": -22.9519,
    "longitude": -43.2105,
    "endereco": "Parque Nacional da Tijuca"
}

Response 201: {objeto criado com id}
```

#### **Atualizar Ponto**
```http
PUT /api/pontos/{id}
Content-Type: application/json

{dados atualizados}

Response 200: {objeto atualizado}
```

#### **Deletar Ponto**
```http
DELETE /api/pontos/{id}

Response 204: No Content
```

---

### **⭐ AVALIAÇÕES**

#### **Criar Avaliação**
```http
POST /api/avaliacoes
Content-Type: application/json

{
    "pontoId": 1,
    "nota": 5,
    "comentario": "Lugar incrível!"
}

Response 201:
{
    "id": 1,
    "pontoId": 1, 
    "usuarioId": 1,
    "nomeUsuario": null,
    "nota": 5,
    "comentario": "Lugar incrível!",
    "createdAt": "2025-12-07T16:07:46"
}
```

#### **Listar Avaliações de um Ponto**
```http
GET /api/avaliacoes/ponto/{pontoId}

Response 200: [array de avaliações]
```

#### **Listar Avaliações do Usuário**
```http
GET /api/avaliacoes/usuario/{usuarioId}

Response 200: [array de avaliações]
```

#### **Estatísticas do Ponto**
```http
GET /api/avaliacoes/estatisticas/{pontoId}

Response 200:
{
    "totalAvaliacoes": 10,
    "notaMedia": 4.2,
    "distribuicao": {
        "1": 0,
        "2": 1, 
        "3": 2,
        "4": 3,
        "5": 4
    }
}
```

#### **Deletar Avaliação**
```http
DELETE /api/avaliacoes/{id}

Response 204: No Content
```

---

### **💬 COMENTÁRIOS (MongoDB)**

#### **Criar Comentário**
```http
POST /api/comentarios
Content-Type: application/json

{
    "pontoId": 1,
    "texto": "Comentário sobre este local incrível!"
}

Response 201:
{
    "id": "67554abc123...",
    "pontoId": 1,
    "usuarioId": 1, 
    "nomeUsuario": "Nome Usuario",
    "texto": "Comentário sobre este local incrível!",
    "createdAt": "2025-12-07T16:10:30",
    "respostas": [],
    "totalRespostas": 0
}
```

#### **Listar Comentários de um Ponto**
```http
GET /api/comentarios/ponto/{pontoId}

Response 200: [array de comentários]
```

#### **Listar Comentários do Usuário**
```http
GET /api/comentarios/usuario/{usuarioId}

Response 200: [array de comentários]
```

#### **Atualizar Comentário**
```http
PUT /api/comentarios/{id}
Content-Type: application/json

{
    "texto": "Texto atualizado"
}

Response 200: {comentário atualizado}
```

#### **Responder Comentário**
```http
POST /api/comentarios/{id}/resposta
Content-Type: application/json

{
    "texto": "Resposta ao comentário"
}

Response 201: {comentário com nova resposta}
```

#### **Pesquisar por Texto**
```http
GET /api/comentarios/pesquisar?texto=incrível

Response 200: [comentários que contém o texto]
```

#### **Estatísticas de Comentários**
```http
GET /api/comentarios/estatisticas/{pontoId}

Response 200:
{
    "totalComentarios": 15,
    "totalRespostas": 8
}
```

#### **Deletar Comentário**
```http
DELETE /api/comentarios/{id}

Response 204: No Content
```

---

### **📷 FOTOS (MongoDB)**

#### **Upload de Foto**
```http
POST /api/fotos/upload
Content-Type: multipart/form-data

FormData:
- arquivo: {file}
- pontoId: 1
- titulo: "Vista do Cristo Redentor"

Response 201:
{
    "id": "67554def456...",
    "pontoId": 1,
    "usuarioId": 1,
    "filename": "cristo_123.jpg",
    "titulo": "Vista do Cristo Redentor",
    "path": "uploads/cristo_123.jpg", 
    "contentType": "image/jpeg",
    "tamanho": 2048576,
    "createdAt": "2025-12-07T16:15:20"
}
```

#### **Listar Fotos de um Ponto**
```http
GET /api/fotos/ponto/{pontoId}

Response 200: [array de fotos]
```

#### **Baixar Arquivo**
```http
GET /api/fotos/arquivo/{filename}

Response 200: {arquivo binário}
```

#### **Deletar Foto**
```http
DELETE /api/fotos/{fotoId}

Response 204: No Content

### **🔧 Como Testar no Insomnia/Postman:**
1. **Sempre adicionar Header:** `Content-Type: application/json`
2. **URLs base:** `http://localhost:8080`
3. **Para POST/PUT:** Usar aba "Body" → "JSON"
4. **Para upload:** Usar "multipart/form-data"

---

## 🎯 RECURSOS IMPLEMENTADOS

### **🏗️ Arquitetura:**
- ✅ **Clean Architecture** (Controller → Service → Repository)
- ✅ **DTOs** (Request/Response separados)
- ✅ **Validações** (@NotBlank, @Size, etc.)
- ✅ **Tratamento de Erros** (GlobalExceptionHandler)
- ✅ **Paginação** (Pageable/Page)
- ✅ **Filtros** (Query parameters)

### **🔐 Segurança:**
- ✅ **Autenticação Básica** (Login/Register funcionando)
- ✅ **Hash de Senhas** (BCryptPasswordEncoder configurado)
- ✅ **Roles de Usuário** (ROLE_USER estruturado)
- ✅ **Spring Security** (configuração básica)
- ✅ **CORS** configurado para desenvolvimento

### **🗄️ Persistência:**
- ✅ **Multi-database** (PostgreSQL + MongoDB)
- ✅ **JPA/Hibernate** para PostgreSQL
- ✅ **Spring Data MongoDB**
- ✅ **Redis** configurado (pronto para cache)

### **📊 Funcionalidades:**
- ✅ **CRUD Completo** para todas entidades
- ✅ **Sistema de Avaliações** (nota 1-5)
- ✅ **Comentários com Respostas**
- ✅ **Upload de Fotos**
- ✅ **Busca e Filtros**
- ✅ **Estatísticas**

## ⚠️ OBSERVAÇÕES IMPORTANTES

### **🔧 Configurações:**
- **Porta:** Spring Boot roda na porta **8080**
- **CORS:** Configurado para aceitar todas origens (desenvolvimento)
- **Database:** Auto-criação de tabelas habilitada (ddl-auto: update)
- **Logs:** Warnings de null safety são normais (não impedem funcionamento)

### **📁 Estrutura de Pastas:**
```
backend/
├── src/main/java/com/turismo/
│   ├── controller/     # APIs REST
│   ├── service/        # Lógica de negócio  
│   ├── repository/     # Acesso a dados
│   ├── model/          # Entidades
│   ├── dto/           # Data Transfer Objects
│   └── config/        # Configurações
├── src/main/resources/
│   └── application.yml # Configurações da aplicação
└── pom.xml            # Dependências Maven
