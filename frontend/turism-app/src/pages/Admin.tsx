import { useState, useEffect } from 'react';
import { Link, Navigate } from 'react-router-dom';
import { FaPlus, FaEdit, FaTrash, FaDownload } from 'react-icons/fa';
import { toast } from 'react-toastify';
import authService from '../service/auth';
import pontoService from '../service/point';


const Admin = () => {
  const [pontos, setPontos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const isAdmin = authService.isAdmin();

  useEffect(() => {
    // if (isAdmin) {
      carregarPontos();
    // }
  }, [page]);

  const carregarPontos = async () => {
    setLoading(true);
    try {
      const response = await pontoService.listar({ page, size: 10 });
      setPontos(response.content || []);
      setTotalPages(response.totalPages);
    } catch (error) {
      toast.error('Erro ao carregar pontos turísticos');
    } finally {
      setLoading(false);
    }
  };

  const handleDeletar = async (id: any) => {
    if (window.confirm('Tem certeza que deseja deletar este ponto turístico?')) {
      try {
        await pontoService.deletar(id);
        toast.success('Ponto turístico deletado com sucesso!');
        carregarPontos();
      } catch (error) {
        toast.error('Erro ao deletar ponto turístico');
      }
    }
  };

  const handleExportar = async (formato: any) => {
    try {
      const blob = await pontoService.exportar(formato);
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `pontos-turisticos.${formato}`;
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);
      toast.success(`Arquivo ${formato.toUpperCase()} baixado com sucesso!`);
    } catch (error) {
      toast.error('Erro ao exportar dados');
    }
  };

//   if (!isAdmin) {
//     return <Navigate to="/" />;
//   }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between mb-8">
          <h1 className="text-4xl font-bold">Administração</h1>
          <div className="flex space-x-3">
            <button
              onClick={() => handleExportar('json')}
              className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 flex items-center space-x-2"
            >
              <FaDownload />
              <span>JSON</span>
            </button>
            <button
              onClick={() => handleExportar('csv')}
              className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 flex items-center space-x-2"
            >
              <FaDownload />
              <span>CSV</span>
            </button>
            <button
              onClick={() => handleExportar('xml')}
              className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 flex items-center space-x-2"
            >
              <FaDownload />
              <span>XML</span>
            </button>
          </div>
        </div>

        <div className="bg-white rounded-xl shadow-md p-6 mb-6">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-2xl font-semibold">Pontos Turísticos</h2>
            <Link
              to="/admin/pontos/novo"
              className="bg-purple-600 text-white px-6 py-3 rounded-lg hover:bg-purple-700 flex items-center space-x-2"
            >
              <FaPlus />
              <span>Novo Ponto</span>
            </Link>
          </div>

          {loading ? (
            <div className="text-center py-12">
              <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-purple-600"></div>
            </div>
          ) : pontos.length === 0 ? (
            <div className="text-center py-12 text-gray-500">
              Nenhum ponto turístico cadastrado
            </div>
          ) : (
            <>
              <div className="overflow-x-auto">
                <table className="w-full">
                  <thead className="bg-gray-50">
                    <tr>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Nome
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Localização
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Avaliação
                      </th>
                      <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                        Ações
                      </th>
                    </tr>
                  </thead>
                  <tbody className="bg-white divide-y divide-gray-200">
                    {pontos.map((ponto: any) => (
                      <tr key={ponto.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4">
                          <div className="font-medium text-gray-900">{ponto.nome}</div>
                        </td>
                        <td className="px-6 py-4 text-sm text-gray-600">
                          {ponto.cidade}, {ponto.estado}
                        </td>
                        <td className="px-6 py-4">
                          <div className="text-sm">
                            <span className="font-semibold">{ponto.notaMedia?.toFixed(1) || '0.0'}</span>
                            <span className="text-gray-500"> ({ponto.quantidadeAvaliacoes || 0})</span>
                          </div>
                        </td>
                        <td className="px-6 py-4">
                          <div className="flex space-x-2">
                            <Link
                              to={`/pontos/${ponto.id}`}
                              className="text-purple-600 hover:text-purple-700"
                              title="Ver"
                            >
                              Ver
                            </Link>
                            <Link
                              to={`/admin/pontos/editar/${ponto.id}`}
                              className="text-yellow-600 hover:text-yellow-700"
                              title="Editar"
                            >
                              <FaEdit />
                            </Link>
                            <button
                              onClick={() => handleDeletar(ponto.id)}
                              className="text-red-600 hover:text-red-700"
                              title="Deletar"
                            >
                              <FaTrash />
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              {totalPages > 1 && (
                <div className="flex justify-center items-center space-x-2 mt-6">
                  <button
                    onClick={() => setPage(p => p - 1)}
                    disabled={page === 0}
                    className="px-4 py-2 bg-purple-600 text-white rounded-lg disabled:opacity-50"
                  >
                    Anterior
                  </button>
                  <span className="text-gray-600">
                    Página {page + 1} de {totalPages}
                  </span>
                  <button
                    onClick={() => setPage(p => p + 1)}
                    disabled={page >= totalPages - 1}
                    className="px-4 py-2 bg-purple-600 text-white rounded-lg disabled:opacity-50"
                  >
                    Próxima
                  </button>
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default Admin;