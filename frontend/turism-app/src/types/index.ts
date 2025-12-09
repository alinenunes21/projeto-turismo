// Types para Usuario
export interface Usuario {
  id: number;
  login: string;
  email: string;
  role: 'USER' | 'ADMIN';
  createdAt: string;
}

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
  user: Usuario;
}

export interface RegisterRequest {
  login: string;
  email: string;
  senha: string;
}

// Types para PontoTuristico
export interface PontoTuristico {
  id: number;
  nome: string;
  descricao: string;
  cidade: string;
  estado: string;
  pais: string;
  latitude: number;
  longitude: number;
  endereco?: string;
  notaMedia?: number;
  quantidadeAvaliacoes?: number;
  criadoPor?: number;
  createdAt: string;
}

export interface PontoTuristicoRequest {
  nome: string;
  descricao: string;
  cidade: string;
  estado: string;
  pais: string;
  latitude: number;
  longitude: number;
  endereco?: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface PontoFilterParams {
  page?: number;
  size?: number;
  cidade?: string;
  estado?: string;
  avaliacaoMinima?: number;
  nome?: string;
  ordenacao?: string;
}

// Types para Avaliacao
export interface Avaliacao {
  id: number;
  pontoId: number;
  usuarioId: number;
  usuarioNome: string;
  nota: number;
  comentario: string;
  createdAt: string;
}

export interface AvaliacaoRequest {
  pontoId: number;
  nota: number;
  comentario: string;
}

// Types para Comentario
export interface Comentario {
  id: string;
  pontoId: number;
  usuarioId: number;
  usuarioNome: string;
  texto: string;
  createdAt: string;
}

export interface ComentarioRequest {
  pontoId: number;
  texto: string;
}

// Types para Foto
export interface Foto {
  id: string;
  pontoId: number;
  usuarioId: number;
  usuarioNome: string;
  filename: string;
  titulo: string;
  path: string;
  createdAt: string;
}

export interface FotoUploadRequest {
  pontoId: number;
  file: File;
  titulo: string;
}

// Types para Hospedagem
export interface Hospedagem {
  id: number;
  pontoId: number;
  usuarioId: number;
  nome: string;
  endereco: string;
  telefone: string;
  precoMedio: number;
  tipo: string;
  linkReserva?: string;
  createdAt: string;
}

export interface HospedagemRequest {
  pontoId: number;
  nome: string;
  endereco: string;
  telefone: string;
  precoMedio: number;
  tipo: string;
  linkReserva?: string;
}

// Types para Erros
export interface ApiError {
  message: string;
  status: number;
  timestamp: string;
}