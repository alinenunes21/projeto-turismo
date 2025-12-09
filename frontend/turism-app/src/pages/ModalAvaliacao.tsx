import { useState } from 'react';
import { FaStar, FaTimes } from 'react-icons/fa';
import { toast } from 'react-toastify';
import { avaliacaoService } from '../service/avaliacao';

const ModalAvaliacao = ({ pontoId, avaliacaoExistente, onClose, onSuccess }: { pontoId: any, avaliacaoExistente?: any, onClose: any, onSuccess: any }) => {
  const [nota, setNota] = useState(avaliacaoExistente?.nota || 0);
  const [comentario, setComentario] = useState(avaliacaoExistente?.comentario || '');
  const [hoverNota, setHoverNota] = useState(0);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    
    if (nota === 0) {
      toast.error('Selecione uma nota de 1 a 5 estrelas');
      return;
    }

    if (!comentario.trim()) {
      toast.error('Adicione um comentário');
      return;
    }

    setLoading(true);
    try {
      await avaliacaoService.criar({
        pontoId,
        nota,
        comentario
      });
      toast.success(avaliacaoExistente ? 'Avaliação atualizada!' : 'Avaliação criada com sucesso!');
      onSuccess();
      onClose();
    } catch (error) {
      toast.error('Erro ao salvar avaliação');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-2xl max-w-md w-full p-6">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold">
            {avaliacaoExistente ? 'Editar Avaliação' : 'Avaliar Ponto Turístico'}
          </h2>
          <button
            onClick={onClose}
            className="text-gray-500 hover:text-gray-700"
          >
            <FaTimes size={24} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Seletor de Estrelas */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-3">
              Sua Nota
            </label>
            <div className="flex items-center space-x-2">
              {[1, 2, 3, 4, 5].map((star) => (
                <button
                  key={star}
                  type="button"
                  onClick={() => setNota(star)}
                  onMouseEnter={() => setHoverNota(star)}
                  onMouseLeave={() => setHoverNota(0)}
                  className="focus:outline-none transition-transform hover:scale-110"
                >
                  <FaStar
                    size={40}
                    className={
                      star <= (hoverNota || nota)
                        ? 'text-yellow-500'
                        : 'text-gray-300'
                    }
                  />
                </button>
              ))}
              <span className="ml-4 text-lg font-semibold text-gray-700">
                {nota > 0 ? `${nota}/5` : 'Selecione'}
              </span>
            </div>
          </div>

          {/* Comentário */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Seu Comentário
            </label>
            <textarea
              value={comentario}
              onChange={(e) => setComentario(e.target.value)}
              placeholder="Conte sobre sua experiência..."
              rows={5}
              maxLength={500}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 resize-none"
            />
            <p className="text-sm text-gray-500 mt-1">
              {comentario.length}/500 caracteres
            </p>
          </div>

          {/* Botões */}
          <div className="flex space-x-3">
            <button
              type="submit"
              disabled={loading || nota === 0 || !comentario.trim()}
              className="flex-1 bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Salvando...' : 'Salvar Avaliação'}
            </button>
            <button
              type="button"
              onClick={onClose}
              className="px-6 bg-gray-200 text-gray-700 py-3 rounded-lg font-semibold hover:bg-gray-300"
            >
              Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ModalAvaliacao;