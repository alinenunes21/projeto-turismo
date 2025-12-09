import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaMapMarkerAlt, FaStar, FaSearch, FaFilter } from 'react-icons/fa';
import pontoService from '../service/point';

const PontosList = () => {
  const [pontos, setPontos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filtros, setFiltros] = useState({
    nome: '',
    cidade: '',
    estado: '',
    avaliacaoMinima: ''
  });
  const [paginacao, setPaginacao] = useState({
    page: 0,
    size: 12,
    totalPages: 0,
    totalElements: 0
  });

  useEffect(() => {
    carregarPontos();
  }, [paginacao.page, filtros]);

  const carregarPontos = async () => {
    setLoading(true);
    try {
      const response = await pontoService.listar({
        page: paginacao.page,
        size: paginacao.size,
        ...filtros
      });
      setPontos(response.content || []);
      setPaginacao(prev => ({
        ...prev,
        totalPages: response.totalPages,
        totalElements: response.totalElements
      }));
    } catch (error) {
      console.error('Erro ao carregar pontos:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleFiltroChange = (e:any): any => {
    const { name, value } = e.target;
    setFiltros(prev => ({ ...prev, [name]: value }));
    setPaginacao(prev => ({ ...prev, page: 0 }));
  };

  const limparFiltros = () => {
    setFiltros({ nome: '', cidade: '', estado: '', avaliacaoMinima: '' });
    setPaginacao(prev => ({ ...prev, page: 0 }));
  };

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="container mx-auto px-4">
        <h1 className="text-4xl font-bold text-center mb-8">Pontos Turísticos</h1>

        {/* Filtros */}
        <div className="bg-white rounded-lg shadow-md p-6 mb-8">
          <div className="flex items-center mb-4">
            <FaFilter className="text-purple-600 mr-2" />
            <h2 className="text-xl font-semibold">Filtros</h2>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Nome
              </label>
              <div className="relative">
                <FaSearch className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" />
                <input
                  type="text"
                  name="nome"
                  value={filtros.nome}
                  onChange={handleFiltroChange}
                  placeholder="Buscar por nome..."
                  className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Cidade
              </label>
              <input
                type="text"
                name="cidade"
                value={filtros.cidade}
                onChange={handleFiltroChange}
                placeholder="Filtrar por cidade"
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Estado
              </label>
              <input
                type="text"
                name="estado"
                value={filtros.estado}
                onChange={handleFiltroChange}
                placeholder="Filtrar por estado"
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Avaliação Mínima
              </label>
              <select
                name="avaliacaoMinima"
                value={filtros.avaliacaoMinima}
                onChange={handleFiltroChange}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
              >
                <option value="">Todas</option>
                <option value="4">4+ estrelas</option>
                <option value="3">3+ estrelas</option>
                <option value="2">2+ estrelas</option>
              </select>
            </div>
          </div>

          <div className="mt-4">
            <button
              onClick={limparFiltros}
              className="text-purple-600 hover:text-purple-700 font-medium"
            >
              Limpar Filtros
            </button>
          </div>
        </div>

        {/* Resultados */}
        <div className="mb-4 text-gray-600">
          {paginacao.totalElements} {paginacao.totalElements === 1 ? 'resultado' : 'resultados'} encontrado(s)
        </div>

        {loading ? (
          <div className="text-center py-20">
            <div className="inline-block animate-spin rounded-full h-16 w-16 border-b-2 border-purple-600"></div>
          </div>
        ) : pontos.length === 0 ? (
          <div className="text-center py-20">
            <FaMapMarkerAlt className="text-gray-400 text-6xl mx-auto mb-4" />
            <p className="text-xl text-gray-600">Nenhum ponto turístico encontrado</p>
          </div>
        ) : (
          <>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {pontos.map((ponto: any) => (
                <Link
                  key={ponto.id}
                  to={`/pontos/${ponto.id}`}
                  className="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-xl transition transform hover:-translate-y-1"
                >
                  <div className="h-48 bg-gradient-to-br from-purple-400 to-purple-600 flex items-center justify-center">
                    <FaMapMarkerAlt className="text-white text-6xl" />
                  </div>
                  <div className="p-5">
                    <h3 className="text-xl font-bold mb-2 line-clamp-1">{ponto.nome}</h3>
                    <p className="text-gray-600 mb-3">
                      {ponto.cidade}, {ponto.estado}
                    </p>
                    <div className="flex items-center justify-between">
                      <div className="flex items-center space-x-1">
                        {[...Array(5)].map((_, i) => (
                          <FaStar
                            key={i}
                            className={i < Math.round(ponto.notaMedia || 0) ? 'text-yellow-500' : 'text-gray-300'}
                            size={16}
                          />
                        ))}
                      </div>
                      <span className="text-sm text-gray-600">
                        {ponto.quantidadeAvaliacoes || 0} avaliações
                      </span>
                    </div>
                  </div>
                </Link>
              ))}
            </div>

            {/* Paginação */}
            {paginacao.totalPages > 1 && (
              <div className="flex justify-center items-center space-x-2 mt-8">
                <button
                  onClick={() => setPaginacao(prev => ({ ...prev, page: prev.page - 1 }))}
                  disabled={paginacao.page === 0}
                  className="px-4 py-2 bg-purple-600 text-white rounded-lg disabled:opacity-50 disabled:cursor-not-allowed hover:bg-purple-700"
                >
                  Anterior
                </button>
                
                <span className="text-gray-600">
                  Página {paginacao.page + 1} de {paginacao.totalPages}
                </span>

                <button
                  onClick={() => setPaginacao(prev => ({ ...prev, page: prev.page + 1 }))}
                  disabled={paginacao.page >= paginacao.totalPages - 1}
                  className="px-4 py-2 bg-purple-600 text-white rounded-lg disabled:opacity-50 disabled:cursor-not-allowed hover:bg-purple-700"
                >
                  Próxima
                </button>
              </div>
            )}
          </>
        )}
      </div>
    </div>
  );
};

export default PontosList;