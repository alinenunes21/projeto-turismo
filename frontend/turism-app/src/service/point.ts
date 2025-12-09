import api from './api';

const pontoService = {
  listar: async (params: any = {}) => {
    const { page = 0, size = 12, cidade, estado, avaliacaoMinima, nome, ordenacao } = params;
    const response = await api.get('/pontos', {
      params: { page, size, cidade, estado, avaliacaoMinima, nome, ordenacao }
    });
    return response.data;
  },

  buscarPorId: async (id:string) => {
    const response = await api.get(`/pontos/${id}`);
    return response.data;
  },

  criar: async (pontoData: any) => {
    const response = await api.post('/pontos', pontoData);
    return response.data;
  },

  atualizar: async (id: string, pontoData: any) => {
    const response = await api.put(`/pontos/${id}`, pontoData);
    return response.data;
  },

  deletar: async (id: string) => {
    const response = await api.delete(`/pontos/${id}`);
    return response.data;
  },

  exportar: async (formato = 'json') => {
    const response = await api.get(`/pontos/export/${formato}`, {
      responseType: 'blob'
    });
    return response.data;
  }
};

export default pontoService;