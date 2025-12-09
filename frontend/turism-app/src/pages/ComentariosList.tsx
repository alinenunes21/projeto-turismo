import { useState, useEffect } from 'react';
import { FaUser, FaTrash, FaEdit } from 'react-icons/fa';
import { toast } from 'react-toastify';
import authService from '../service/auth';
import { comentarioService } from '../service/avaliacao';


const ComentariosList = ({ pontoId } :{pontoId: any} ) => {
  const [comentarios, setComentarios] = useState([]);
  const [loading, setLoading] = useState(true);
  const [novoComentario, setNovoComentario] = useState('');
  const [editando, setEditando] = useState(null);
  const [textoEditado, setTextoEditado] = useState('');
  const currentUser = authService.getCurrentUser();
  const isAuthenticated = authService.isAuthenticated();
  const isAdmin = authService.isAdmin();

  useEffect(() => {
    carregarComentarios();
  }, [pontoId]);

  const carregarComentarios = async () => {
    try {
      const data = await comentarioService.listarPorPonto(pontoId);
      setComentarios(data);
    } catch (error) {
      console.error('Erro ao carregar comentários:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCriar = async (e: any) => {
    e.preventDefault();
    if (!novoComentario.trim()) return;

    try {
      await comentarioService.criar({
        pontoId,
        texto: novoComentario
      });
      toast.success('Comentário adicionado com sucesso!');
      setNovoComentario('');
      carregarComentarios();
    } catch (error) {
      toast.error('Erro ao adicionar comentário');
    }
  };

  const handleAtualizar = async (id: any) => {
    if (!textoEditado.trim()) return;

    try {
      await comentarioService.atualizar(id, textoEditado);
      toast.success('Comentário atualizado com sucesso!');
      setEditando(null);
      setTextoEditado('');
      carregarComentarios();
    } catch (error) {
      toast.error('Erro ao atualizar comentário');
    }
  };

  const handleDeletar = async (id: any, usuarioId: any) => {
    if (currentUser?.id !== usuarioId && !isAdmin) {
      toast.error('Você não pode deletar este comentário');
      return;
    }

    if (window.confirm('Deseja realmente deletar este comentário?')) {
      try {
        await comentarioService.deletar(id);
        toast.success('Comentário deletado com sucesso!');
        carregarComentarios();
      } catch (error) {
        toast.error('Erro ao deletar comentário');
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

  return (
    <div className="space-y-6">
      {/* Formulário de novo comentário */}
      {isAuthenticated && (
        <form onSubmit={handleCriar} className="bg-gray-50 rounded-lg p-4">
          <textarea
            value={novoComentario}
            onChange={(e) => setNovoComentario(e.target.value)}
            placeholder="Compartilhe sua experiência..."
            maxLength={500}
            rows={4}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 resize-none"
          />
          <div className="flex items-center justify-between mt-2">
            <span className="text-sm text-gray-500">
              {novoComentario.length}/500 caracteres
            </span>
            <button
              type="submit"
              disabled={!novoComentario.trim()}
              className="bg-purple-600 text-white px-6 py-2 rounded-lg hover:bg-purple-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Comentar
            </button>
          </div>
        </form>
      )}

      {/* Lista de comentários */}
      {comentarios.length === 0 ? (
        <div className="text-center py-8 text-gray-500">
          Nenhum comentário ainda. Seja o primeiro a comentar!
        </div>
      ) : (
        <div className="space-y-4">
          {comentarios.map((comentario: any) => (
            <div key={comentario.id} className="border border-gray-200 rounded-lg p-4">
              <div className="flex items-start justify-between mb-3">
                <div className="flex items-center space-x-3">
                  <div className="bg-purple-100 rounded-full p-3">
                    <FaUser className="text-purple-600" />
                  </div>
                  <div>
                    <p className="font-semibold">{comentario.usuarioNome}</p>
                    <p className="text-sm text-gray-500">
                      {new Date(comentario.createdAt).toLocaleDateString('pt-BR', {
                        day: '2-digit',
                        month: 'long',
                        year: 'numeric'
                      })}
                    </p>
                  </div>
                </div>

                {( isAdmin) && (
                  <div className="flex space-x-2">
                    {currentUser?.id === comentario.usuarioId && (
                      <button
                        onClick={() => {
                          setEditando(comentario.id);
                          setTextoEditado(comentario.texto);
                        }}
                        className="text-purple-600 hover:text-purple-700"
                        title="Editar"
                      >
                        <FaEdit />
                      </button>
                    )}
                    <button
                      onClick={() => handleDeletar(comentario.id, comentario.usuarioId)}
                      className="text-red-600 hover:text-red-700"
                      title="Deletar"
                    >
                      <FaTrash />
                    </button>
                  </div>
                )}
              </div>

              {editando === comentario.id ? (
                <div>
                  <textarea
                    value={textoEditado}
                    onChange={(e) => setTextoEditado(e.target.value)}
                    maxLength={500}
                    rows={4}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 resize-none"
                  />
                  <div className="flex space-x-2 mt-2">
                    <button
                      onClick={() => handleAtualizar(comentario.id)}
                      className="bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700"
                    >
                      Salvar
                    </button>
                    <button
                      onClick={() => {
                        setEditando(null);
                        setTextoEditado('');
                      }}
                      className="bg-gray-300 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-400"
                    >
                      Cancelar
                    </button>
                  </div>
                </div>
              ) : (
                <p className="text-gray-700 leading-relaxed whitespace-pre-wrap">
                  {comentario.texto}
                </p>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default ComentariosList;