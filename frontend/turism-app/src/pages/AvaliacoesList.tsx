import { useState, useEffect } from 'react';
import { FaStar, FaUser, FaTrash, FaEdit } from 'react-icons/fa';
import { toast } from 'react-toastify';
import authService from '../service/auth';
import { avaliacaoService } from '../service/avaliacao';
import ModalAvaliacao from './ModalAvaliacao';


const AvaliacoesList = ({ pontoId } : {pontoId: string}) => {
  const [avaliacoes, setAvaliacoes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [editando, setEditando] = useState(null);
  const currentUser = authService.getCurrentUser();
  const isAdmin = authService.isAdmin();

  useEffect(() => {
    carregarAvaliacoes();
  }, [pontoId]);

  const carregarAvaliacoes = async () => {
    try {
      const data = await avaliacaoService.listarPorPonto(pontoId);
      setAvaliacoes(data);
    } catch (error) {
      console.error('Erro ao carregar avaliações:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDeletar = async (id: any, usuarioId: any) => {
    // if (currentUser?.id !== usuarioId && !isAdmin) {
    //   toast.error('Você não pode deletar esta avaliação');
    //   return;
    // }

    if (window.confirm('Deseja realmente deletar esta avaliação?')) {
      try {
        await avaliacaoService.deletar(id);
        toast.success('Avaliação deletada com sucesso!');
        carregarAvaliacoes();
      } catch (error) {
        toast.error('Erro ao deletar avaliação');
      }
    }
  };

  if (loading) {
    return (
      <div className="text-center py-8">
        <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-purple-600"></div>
      </div>
    );
  }

  if (avaliacoes.length === 0) {
    return (
      <div className="text-center py-8 text-gray-500">
        Nenhuma avaliação ainda. Seja o primeiro a avaliar!
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {avaliacoes.map((avaliacao: any) => (
        <div key={avaliacao.id} className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition">
          <div className="flex items-start justify-between mb-3">
            <div className="flex items-center space-x-3">
              <div className="bg-purple-100 rounded-full p-3">
                <FaUser className="text-purple-600" />
              </div>
              <div>
                <p className="font-semibold">{avaliacao.usuarioNome}</p>
                <p className="text-sm text-gray-500">
                  {new Date(avaliacao.createdAt).toLocaleDateString('pt-BR')}
                </p>
              </div>
            </div>

            <div className="flex items-center space-x-2">
              <div className="flex items-center space-x-1">
                {[...Array(5)].map((_, i) => (
                  <FaStar
                    key={i}
                    className={i < avaliacao.nota ? 'text-yellow-500' : 'text-gray-300'}
                    size={18}
                  />
                ))}
              </div>

              {/* {(currentUser?.id === avaliacao.usuarioId || isAdmin) && ( */}
                <div className="flex space-x-2 ml-4">
                  <button
                    onClick={() => setEditando(avaliacao)}
                    className="text-purple-600 hover:text-purple-700"
                    title="Editar"
                  >
                    <FaEdit />
                  </button>
                  <button
                    onClick={() => handleDeletar(avaliacao.id, avaliacao.usuarioId)}
                    className="text-red-600 hover:text-red-700"
                    title="Deletar"
                  >
                    <FaTrash />
                  </button>
                </div>
              {/* )} */}
            </div>
          </div>

          <p className="text-gray-700 leading-relaxed">{avaliacao.comentario}</p>
        </div>
      ))}

      {editando && (
        <ModalAvaliacao
          pontoId={pontoId}
          avaliacaoExistente={editando}
          onClose={() => setEditando(null)}
          onSuccess={() => {
            carregarAvaliacoes();
            setEditando(null);
          }}
        />
      )}
    </div>
  );
};

export default AvaliacoesList;