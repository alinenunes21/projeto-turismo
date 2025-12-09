import { useState, useEffect, FC } from 'react';
import { FaHotel, FaPhone, FaExternalLinkAlt, FaPlus, FaTrash, FaEdit } from 'react-icons/fa';
import { toast } from 'react-toastify';
import { Hospedagem } from '../types';
import authService from '../service/auth';
import { hospedagemService } from '../service/avaliacao';
import ModalHospedagem from './ModalHospedagem';


interface HospedagensListProps {
  pontoId: number;
}

const HospedagensList: FC<HospedagensListProps> = ({ pontoId }) => {
  const [hospedagens, setHospedagens] = useState<Hospedagem[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [modalAberto, setModalAberto] = useState<boolean>(false);
  const [editando, setEditando] = useState<Hospedagem | null>(null);
  const isAuthenticated = authService.isAuthenticated();
  const currentUser = authService.getCurrentUser();
  const isAdmin = authService.isAdmin();

  useEffect(() => {
    carregarHospedagens();
  }, [pontoId]);

  const carregarHospedagens = async (): Promise<void> => {
    try {
      const data = await hospedagemService.listarPorPonto(pontoId);
      setHospedagens(data);
    } catch (error) {
      console.error('Erro ao carregar hospedagens:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDeletar = async (id: number): Promise<void> => {
    if (!isAdmin) {
      toast.error('Você não pode deletar esta hospedagem');
      return;
    }

    if (window.confirm('Deseja realmente deletar esta hospedagem?')) {
      try {
        await hospedagemService.deletar(id);
        toast.success('Hospedagem deletada com sucesso!');
        carregarHospedagens();
      } catch (error) {
        toast.error('Erro ao deletar hospedagem');
      }
    }
  };

  if (loading) {
    return (
      <div className="text-center py-4">
        <div className="inline-block animate-spin rounded-full h-6 w-6 border-b-2 border-purple-600"></div>
      </div>
    );
  }

  return (
    <div className="space-y-4">
      {isAuthenticated && (
        <button
          onClick={() => setModalAberto(true)}
          className="w-full bg-purple-600 text-white py-2 rounded-lg hover:bg-purple-700 flex items-center justify-center space-x-2 transition"
        >
          <FaPlus />
          <span>Adicionar Hospedagem</span>
        </button>
      )}

      {hospedagens.length === 0 ? (
        <div className="text-center py-4 text-gray-500 text-sm">
          Nenhuma hospedagem cadastrada
        </div>
      ) : (
        <div className="space-y-3">
          {hospedagens.map((hospedagem) => (
            <div key={hospedagem.id} className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition">
              <div className="flex items-start justify-between mb-2">
                <div className="flex items-center space-x-2">
                  <FaHotel className="text-purple-600" />
                  <h4 className="font-semibold">{hospedagem.nome}</h4>
                </div>
                
                {( isAdmin) && (
                  <div className="flex space-x-2">
                    <button
                      onClick={() => setEditando(hospedagem)}
                      className="text-purple-600 hover:text-purple-700"
                      title="Editar"
                    >
                      <FaEdit size={14} />
                    </button>
                    <button
                      onClick={() => handleDeletar(hospedagem.id, hospedagem.usuarioId)}
                      className="text-red-600 hover:text-red-700"
                      title="Deletar"
                    >
                      <FaTrash size={14} />
                    </button>
                  </div>
                )}
              </div>

              <p className="text-sm text-gray-600 mb-2">{hospedagem.endereco}</p>
              
              <div className="flex items-center space-x-2 text-sm text-gray-600 mb-2">
                <FaPhone size={12} />
                <span>{hospedagem.telefone}</span>
              </div>

              <div className="flex items-center justify-between">
                <div>
                  <span className="text-sm text-gray-500">A partir de </span>
                  <span className="text-lg font-bold text-green-600">
                    R$ {hospedagem.precoMedio.toFixed(2)}
                  </span>
                  <span className="text-sm text-gray-500">/noite</span>
                </div>

                {hospedagem.linkReserva && (
                  <a
                    href={hospedagem.linkReserva}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="bg-purple-600 text-white px-3 py-2 rounded text-sm hover:bg-purple-700 flex items-center space-x-1 transition"
                  >
                    <span>Reservar</span>
                    <FaExternalLinkAlt size={12} />
                  </a>
                )}
              </div>

              <div className="mt-2">
                <span className="inline-block bg-gray-100 text-gray-700 text-xs px-2 py-1 rounded">
                  {hospedagem.tipo}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}

      {modalAberto && (
        <ModalHospedagem
          pontoId={pontoId}
          onClose={() => setModalAberto(false)}
          onSuccess={() => {
            carregarHospedagens();
            setModalAberto(false);
          }}
        />
      )}

      {editando && (
        <ModalHospedagem
          pontoId={pontoId}
          hospedagemExistente={editando}
          onClose={() => setEditando(null)}
          onSuccess={() => {
            carregarHospedagens();
            setEditando(null);
          }}
        />
      )}
    </div>
  );
};

export default HospedagensList;