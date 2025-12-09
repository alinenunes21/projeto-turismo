import axios from 'axios';
import api from './api';

// avaliacaoService.js
export const avaliacaoService = {
  criar: async (avaliacaoData: any) => {
    const response = await api.post('/avaliacoes', avaliacaoData);
    return response.data;
  },

  listarPorPonto: async (pontoId: any) => {
    const response = await api.get(`/avaliacoes/ponto/${pontoId}`);
    return response.data;
  },

  deletar: async (id: any) => {
    const response = await api.delete(`/avaliacoes/${id}`);
    return response.data;
  }
};

// comentarioService.js
export const comentarioService = {
  criar: async (comentarioData: any) => {
    const response = await api.post('/comentarios', comentarioData);
    return response.data;
  },

  listarPorPonto: async (pontoId: any) => {
    const response = await api.get(`/comentarios/ponto/${pontoId}`);
    return response.data;
  },

  atualizar: async (id: any, texto: any) => {
    const response = await api.put(`/comentarios/${id}`, { texto });
    return response.data;
  },

  deletar: async (id: any) => {
    const response = await api.delete(`/comentarios/${id}`);
    return response.data;
  }
};

// fotoService.js
export const fotoService = {
   upload: async (pontoId: number, formData: FormData) => {
    try {
      // O FormData já deve conter:
      // - arquivo (File)
      // - pontoId (string)
      // - titulo (string)
      
      console.log('Fazendo upload para:', `/fotos/ponto/upload`);
      
      const response = await api.post(`/fotos/upload`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        // Aumentar timeout para uploads grandes
        timeout: 30000,
      });
      
      return response.data;
    } catch (error: any) {
      console.error('Erro no upload:', error.response?.data || error.message);
      throw error;
    }
  },

  listarPorPonto: async (pontoId: any) => {
    const response = await api.get(`/fotos/ponto/${pontoId}`);
    return response.data;
  },

  deletar: async (id: any) => {
    const response = await api.delete(`/fotos/${id}`);
    return response.data;
  },

  getUrl: (filename: any) => {
    return `${api.defaults.baseURL}/fotos/arquivo/${filename}`;
  }
};

// hospedagemService.js
export const hospedagemService = {
  criar: async (hospedagemData: any) => {
    const response = await api.post('/hospedagens', hospedagemData);
    return response.data;
  },

  listarPorPonto: async (pontoId: any) => {
    const response = await api.get(`/hospedagens/ponto/${pontoId}`);
    return response.data;
  },

  atualizar: async (id: any, hospedagemData: any) => {
    const response = await api.put(`/hospedagens/${id}`, hospedagemData);
    return response.data;
  },

  deletar: async (id: any) => {
    const response = await api.delete(`/hospedagens/${id}`);
    return response.data;
  },

  /**
   * Obtém a URL completa de uma foto pelo filename
   */
  getUrl: (filename: string) => {
    return `http://localhost:8080/api/fotos/arquivo/${filename}`;
  },

  /**
   * Obtém a URL de uma foto a partir do objeto foto completo
   */
  getFotoUrl: (foto: any) => {
    if (foto.url && foto.url.startsWith('/api/fotos/arquivo/')) {
      return `http://localhost:8080${foto.url}`;
    }
    if (foto.filename) {
      return `http://localhost:8080/api/fotos/arquivo/${foto.filename}`;
    }
    return '';
  }
};
