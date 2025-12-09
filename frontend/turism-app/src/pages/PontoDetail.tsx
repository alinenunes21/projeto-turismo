import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { FaMapMarkerAlt, FaStar, FaCamera, FaHotel, FaEdit, FaTrash } from 'react-icons/fa';
import { toast } from 'react-toastify';
import authService from '../service/auth';
import pontoService from '../service/point';
import AvaliacoesList from './AvaliacoesList';
import ComentariosList from './ComentariosList';
import ModalAvaliacao from './ModalAvaliacao';
import ModalUploadFoto from './ModalUploadDeFotos';
import GaleriaFotos from './GaleriaFotos';
import Mapa from './Mapa';
import HospedagensList from './HospedagemList';


const PontoDetail = () => {
  const { id } = useParams() || "";
  const navigate = useNavigate();
  const [ponto, setPonto] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [modalAvaliacao, setModalAvaliacao] = useState(false);
  const [modalUpload, setModalUpload] = useState(false);
  const isAuthenticated = authService.isAuthenticated() || true
  const isAdmin = authService.isAdmin();

  useEffect(() => {
    carregarPonto();
  }, [id]);

  const carregarPonto = async () => {
    setLoading(true);
    try {
      const data = await pontoService.buscarPorId(id || '');
      setPonto(data);
    } catch (error) {
      toast.error('Erro ao carregar ponto turístico');
      navigate('/pontos');
    } finally {
      setLoading(false);
    }
  };

  const handleDeletar = async () => {
    if (window.confirm('Tem certeza que deseja deletar este ponto turístico?')) {
      try {
        await pontoService.deletar(id|| '');
        toast.success('Ponto turístico deletado com sucesso!');
        navigate('/pontos');
      } catch (error) {
        toast.error('Erro ao deletar ponto turístico');
      }
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-purple-600"></div>
      </div>
    );
  }

  if (!ponto) return null;

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero Section */}
      <div className="bg-gradient-to-r from-purple-600 to-purple-800 text-white py-12">
        <div className="container mx-auto px-4">
          <div className="flex items-start justify-between">
            <div className="flex-1">
              <h1 className="text-4xl font-bold mb-4">{ponto.nome}</h1>
              <div className="flex items-center space-x-4 text-lg">
                <div className="flex items-center space-x-2">
                  <FaMapMarkerAlt />
                  <span>{ponto.cidade}, {ponto.estado} - {ponto.pais}</span>
                </div>
                <div className="flex items-center space-x-2">
                  <div className="flex items-center">
                    {[...Array(5)].map((_, i) => (
                      <FaStar
                        key={i}
                        className={i < Math.round(ponto.notaMedia || 0) ? 'text-yellow-400' : 'text-gray-300'}
                      />
                    ))}
                  </div>
                  <span>({ponto.quantidadeAvaliacoes || 0} avaliações)</span>
                </div>
              </div>
            </div>

            {/* {isAdmin && ( */}
              <div className="flex space-x-2">
                <button
                  onClick={() => navigate(`/admin/pontos/editar/${id}`)}
                  className="bg-yellow-500 hover:bg-yellow-600 px-4 py-2 rounded-lg flex items-center space-x-2"
                >
                  <FaEdit />
                  <span>Editar</span>
                </button>
                <button
                  onClick={handleDeletar}
                  className="bg-red-500 hover:bg-red-600 px-4 py-2 rounded-lg flex items-center space-x-2"
                >
                  <FaTrash />
                  <span>Deletar</span>
                </button>
              </div>
            {/* )} */}
          </div>
        </div>
      </div>

      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Coluna Principal */}
          <div className="lg:col-span-2 space-y-8">
            {/* Descrição */}
            <div className="bg-white rounded-xl shadow-md p-6">
              <h2 className="text-2xl font-bold mb-4">Sobre</h2>
              <p className="text-gray-700 leading-relaxed">{ponto.descricao}</p>
            </div>

            {/* Galeria de Fotos */}
            <div className="bg-white rounded-xl shadow-md p-6">
              <div className="flex items-center justify-between mb-4">
                <h2 className="text-2xl font-bold">Fotos</h2>
                {isAuthenticated && (
                  <button
                    onClick={() => setModalUpload(true)}
                    className="bg-purple-600 hover:bg-purple-700 text-white px-4 py-2 rounded-lg flex items-center space-x-2"
                  >
                    <FaCamera />
                    <span>Adicionar Foto</span>
                  </button>
                )}
              </div>
              <GaleriaFotos pontoId={id} />
            </div>

            {/* Avaliações */}
            <div className="bg-white rounded-xl shadow-md p-6">
              <div className="flex items-center justify-between mb-4">
                <h2 className="text-2xl font-bold">Avaliações</h2>
                {isAuthenticated && (
                  <button
                    onClick={() => setModalAvaliacao(true)}
                    className="bg-purple-600 hover:bg-purple-700 text-white px-4 py-2 rounded-lg"
                  >
                    Avaliar
                  </button>
                )}
              </div>
              <AvaliacoesList pontoId={id || ""} />
            </div>

            {/* Comentários */}
            <div className="bg-white rounded-xl shadow-md p-6">
              <h2 className="text-2xl font-bold mb-4">Comentários</h2>
              <ComentariosList pontoId={id} />
            </div>
          </div>

          {/* Sidebar */}
          <div className="space-y-6">
            {/* Localização */}
            <div className="bg-white rounded-xl shadow-md p-6">
              <h3 className="text-xl font-bold mb-4">Localização</h3>
              <p className="text-gray-700 mb-4">{ponto.endereco}</p>
              <Mapa latitude={ponto.latitude} longitude={ponto.longitude} nome={ponto.nome} />
            </div>

            {/* Hospedagens */}
            <div className="bg-white rounded-xl shadow-md p-6">
              <div className="flex items-center justify-between mb-4">
                <h3 className="text-xl font-bold flex items-center space-x-2">
                  <FaHotel />
                  <span>Hospedagens</span>
                </h3>
              </div>
              <HospedagensList pontoId={Number(id)} />
            </div>
          </div>
        </div>
      </div>

      {/* Modais */}
      {modalAvaliacao && (
        <ModalAvaliacao
          pontoId={id}
          onClose={() => setModalAvaliacao(false)}
          onSuccess={carregarPonto}
        />
      )}

      {modalUpload && (
        <ModalUploadFoto
          pontoId={id}
          onClose={() => setModalUpload(false)}
        />
      )}
    </div>
  );
};

export default PontoDetail;