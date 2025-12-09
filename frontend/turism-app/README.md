# TurismoApp - Frontend

Frontend da aplicação de pontos turísticos desenvolvido com React + Vite.

## 🚀 Tecnologias

- **React 18** - Biblioteca JavaScript para interfaces
- **Vite** - Build tool e dev server
- **React Router DOM** - Roteamento
- **Axios** - Cliente HTTP
- **React Hook Form** - Gerenciamento de formulários
- **React Icons** - Ícones
- **React Toastify** - Notificações
- **Leaflet** - Mapas interativos
- **Tailwind CSS** - Estilização

## 📁 Estrutura de Pastas

```
frontend/
├── public/
├── src/
│   ├── components/       # Componentes reutilizáveis
│   │   ├── Header.jsx
│   │   ├── Footer.jsx
│   │   ├── GaleriaFotos.jsx
│   │   ├── AvaliacoesList.jsx
│   │   ├── ComentariosList.jsx
│   │   ├── HospedagensList.jsx
│   │   ├── Mapa.jsx
│   │   ├── ModalAvaliacao.jsx
│   │   ├── ModalUploadFoto.jsx
│   │   ├── ModalHospedagem.jsx
│   │   └── PrivateRoute.jsx
│   ├── pages/            # Páginas
│   │   ├── Home.jsx
│   │   ├── Login.jsx
│   │   ├── Cadastro.jsx
│   │   ├── PontosList.jsx
│   │   ├── PontoDetail.jsx
│   │   ├── Admin.jsx
│   │   └── PontoForm.jsx
│   ├── services/         # Serviços e APIs
│   │   ├── api.js
│   │   ├── authService.js
│   │   ├── pontoService.js
│   │   └── services.js
│   ├── App.jsx           # Componente principal
│   ├── main.jsx          # Entry point
│   └── index.css         # Estilos globais
├── .env                  # Variáveis de ambiente
├── package.json
├── vite.config.js
├── tailwind.config.js
└── Dockerfile
```

## 🛠️ Instalação e Execução

### Pré-requisitos
- Node.js 18+
- npm ou yarn

### Passos

1. **Clone o repositório**
```bash
git clone <url-do-repositorio>
cd frontend
```

2. **Instale as dependências**
```bash
npm install
```

3. **Configure as variáveis de ambiente**
```bash
# Crie o arquivo .env na raiz do projeto
VITE_API_URL=http://localhost:8080/api
```

4. **Execute em modo desenvolvimento**
```bash
npm run dev
```

A aplicação estará disponível em `http://localhost:5173`

5. **Build para produção**
```bash
npm run build
```

## 🐳 Docker

### Build da imagem
```bash
docker build -t turismo-frontend .
```

### Executar container
```bash
docker run -p 80:80 turismo-frontend
```

## 📱 Funcionalidades

### Usuário Comum
- ✅ Cadastro e login
- ✅ Visualizar pontos turísticos
- ✅ Filtrar e buscar pontos
- ✅ Ver detalhes, fotos e localização
- ✅ Avaliar pontos (nota + comentário)
- ✅ Adicionar comentários detalhados
- ✅ Fazer upload de fotos
- ✅ Cadastrar hospedagens
- ✅ Editar/deletar próprios conteúdos

### Administrador
- ✅ Todas as funcionalidades de usuário comum
- ✅ Criar novos pontos turísticos
- ✅ Editar pontos existentes
- ✅ Deletar pontos turísticos
- ✅ Deletar qualquer conteúdo
- ✅ Exportar dados (JSON, CSV, XML)

## 🎨 Componentes Principais

### Header
Navegação principal com logo, menu e autenticação.

### Footer
Informações do projeto e links úteis.

### GaleriaFotos
Exibe fotos do ponto turístico em grid com modal de visualização.

### AvaliacoesList
Lista de avaliações com notas e comentários.

### ComentariosList
Sistema completo de comentários com CRUD.

### HospedagensList
Lista e gerenciamento de hospedagens.

### Mapa
Integração com Leaflet para exibir localização.

### Modais
- **ModalAvaliacao**: Formulário de avaliação com seletor de estrelas
- **ModalUploadFoto**: Upload de imagens com preview
- **ModalHospedagem**: Cadastro de hospedagens

## 🔐 Autenticação

O sistema usa JWT (JSON Web Token) armazenado no localStorage:
- Token enviado em todas requisições via interceptor
- Redirecionamento automático para login em caso de token expirado
- Proteção de rotas administrativas com `PrivateRoute`

## 🗺️ Rotas

| Rota | Componente | Proteção |
|------|-----------|----------|
| `/` | Home | Pública |
| `/login` | Login | Pública |
| `/cadastro` | Cadastro | Pública |
| `/pontos` | PontosList | Pública |
| `/pontos/:id` | PontoDetail | Pública |
| `/admin` | Admin | Admin |
| `/admin/pontos/novo` | PontoForm | Admin |
| `/admin/pontos/editar/:id` | PontoForm | Admin |

## 📦 Scripts Disponíveis

```bash
# Desenvolvimento
npm run dev

# Build para produção
npm run build

# Preview da build
npm run preview
```

## 🔧 Configuração

### Vite
Configurado para proxy reverso e hot reload.

### Tailwind CSS
Configuração customizada com paleta de cores do projeto.

### Axios Interceptors
- **Request**: Adiciona token JWT automaticamente
- **Response**: Trata erros 401/403 e exibe notificações

## 🌐 Integrações

### Backend API
Todas as rotas apontam para `VITE_API_URL` configurada no `.env`.

### Leaflet Maps
Mapas interativos com marcadores e link para Google Maps.

## 📱 Responsividade

O projeto é totalmente responsivo usando Tailwind CSS:
- **Mobile**: Layout adaptado com menu hamburguer
- **Tablet**: Grid de 2 colunas
- **Desktop**: Grid de 3 colunas e layout completo

## 🎯 Boas Práticas

- ✅ Componentes funcionais com Hooks
- ✅ Separação de responsabilidades (services, components, pages)
- ✅ Validação de formulários com React Hook Form
- ✅ Tratamento de erros e loading states
- ✅ Feedback visual com toasts
- ✅ Código limpo e organizado
- ✅ TypeScript-ready (interfaces comentadas)

## 👥 Desenvolvedora

**Aline Ayumi** - Frontend Developer

## 📄 Licença

Este projeto faz parte do trabalho acadêmico da disciplina de Desenvolvimento Web.