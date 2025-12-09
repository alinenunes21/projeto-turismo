import { useState } from 'react';
import { FaTimes, FaCamera, FaUpload } from 'react-icons/fa';
import { toast } from 'react-toastify';
import { fotoService } from '../service/avaliacao';

const ModalUploadFoto = ({ pontoId, onClose, onSuccess } : { pontoId: any, onClose: any, onSuccess?: any }) => {
  const [arquivo, setArquivo] = useState(null);
  const [preview, setPreview] = useState(null);
  const [titulo, setTitulo] = useState('');
  const [loading, setLoading] = useState(false);

  const handleFileChange = (e: any) => {
    const file = e.target.files[0];
    if (!file) return;

    // Validar tipo
    const tiposPermitidos = ['image/jpeg', 'image/jpg', 'image/png', 'image/webp'];
    if (!tiposPermitidos.includes(file.type)) {
      toast.error('Formato não permitido. Use JPG, PNG ou WEBP');
      return;
    }

    // Validar tamanho (5MB)
    if (file.size > 5 * 1024 * 1024) {
      toast.error('Arquivo muito grande. Máximo 5MB');
      return;
    }

    setArquivo(file);

    // Criar preview
    const reader: any = new FileReader();
    reader.onloadend = () => {
      setPreview(reader.result);
    };
    reader.readAsDataURL(file);
  };

  const handleSubmit = async (e: any) => {
    e.preventDefault();

    if (!arquivo) {
      toast.error('Selecione uma foto');
      return;
    }

    if (!titulo.trim()) {
      toast.error('Digite um título para a foto');
      return;
    }

    setLoading(true);
    try {
      const formData = new FormData();
      formData.append('arquivo', arquivo);
      formData.append('titulo', titulo);
       formData.append('pontoId', pontoId);

      await fotoService.upload(pontoId, formData);
      toast.success('Foto enviada com sucesso!');
      onSuccess?.();
      onClose();
    } catch (error) {
      toast.error('Erro ao enviar foto');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-2xl max-w-lg w-full p-6">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold flex items-center space-x-2">
            <FaCamera className="text-blue-600" />
            <span>Adicionar Foto</span>
          </h2>
          <button
            onClick={onClose}
            className="text-gray-500 hover:text-gray-700"
          >
            <FaTimes size={24} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Upload Area */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Selecione a Foto
            </label>
            
            {preview ? (
              <div className="relative">
                <img
                  src={preview}
                  alt="Preview"
                  className="w-full h-64 object-cover rounded-lg"
                />
                <button
                  type="button"
                  onClick={() => {
                    setArquivo(null);
                    setPreview(null);
                  }}
                  className="absolute top-2 right-2 bg-red-500 text-white p-2 rounded-full hover:bg-red-600"
                >
                  <FaTimes />
                </button>
              </div>
            ) : (
              <label className="flex flex-col items-center justify-center w-full h-64 border-2 border-dashed border-gray-300 rounded-lg cursor-pointer hover:bg-gray-50 transition">
                <div className="flex flex-col items-center justify-center pt-5 pb-6">
                  <FaUpload className="text-gray-400 text-5xl mb-4" />
                  <p className="text-sm text-gray-600 mb-2">
                    Clique para selecionar ou arraste uma foto
                  </p>
                  <p className="text-xs text-gray-500">
                    JPG, PNG ou WEBP (máx. 5MB)
                  </p>
                </div>
                <input
                  type="file"
                  className="hidden"
                  accept="image/jpeg,image/jpg,image/png,image/webp"
                  onChange={handleFileChange}
                />
              </label>
            )}
          </div>

          {/* Título */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Título da Foto
            </label>
            <input
              type="text"
              value={titulo}
              onChange={(e) => setTitulo(e.target.value)}
              placeholder="Digite um título descritivo"
              maxLength={100}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
            />
          </div>

          {/* Info */}
          <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
            <p className="text-sm text-blue-800">
              <strong>Dica:</strong> Máximo de 10 fotos por ponto turístico. 
              Certifique-se de que a foto está bem iluminada e mostra o local claramente.
            </p>
          </div>

          {/* Botões */}
          <div className="flex space-x-3">
            <button
              type="submit"
              disabled={loading || !arquivo || !titulo.trim()}
              className="flex-1 bg-blue-600 text-white py-3 rounded-lg font-semibold hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center space-x-2"
            >
              {loading ? (
                <>
                  <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white"></div>
                  <span>Enviando...</span>
                </>
              ) : (
                <>
                  <FaUpload />
                  <span>Enviar Foto</span>
                </>
              )}
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

export default ModalUploadFoto;