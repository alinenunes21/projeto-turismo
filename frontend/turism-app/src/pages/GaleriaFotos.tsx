import { useState, useEffect } from 'react';
import { FaTrash, FaTimes } from 'react-icons/fa';
import { toast } from 'react-toastify';
import authService from '../service/auth';
import { fotoService } from '../service/avaliacao';

const GaleriaFotos = ({ pontoId }: { pontoId: any }) => {
  const [fotos, setFotos] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [fotoSelecionada, setFotoSelecionada] = useState<any>(null);
  const currentUser = authService.getCurrentUser();
  const isAdmin = authService.isAdmin();

  useEffect(() => {
    carregarFotos();
  }, [pontoId]);

  const carregarFotos = async () => {
    try {
      const data = await fotoService.listarPorPonto(pontoId);
      console.log('Fotos carregadas:', data);
      setFotos(data);
    } catch (error) {
      console.error('Erro ao carregar fotos:', error);
      toast.error('Erro ao carregar fotos');
    } finally {
      setLoading(false);
    }
  };

  const handleDeletar = async (fotoId: any, usuarioId: any) => {
    if (!isAdmin) {
      toast.error('Você não pode deletar esta foto');
      return;
    }

    if (window.confirm('Deseja realmente deletar esta foto?')) {
      try {
        await fotoService.deletar(fotoId);
        toast.success('Foto deletada com sucesso!');
        carregarFotos();
        setFotoSelecionada(null);
      } catch (error) {
        toast.error('Erro ao deletar foto');
      }
    }
  };

  // Função para construir URL completa da foto
  const getFotoUrl = (foto: any) => {
    // Se já tem a URL completa no objeto
    if (foto.url && foto.url.startsWith('/api/fotos/arquivo/')) {
      return `http://localhost:8080${foto.url}`;
    }
    // Se tem apenas o filename
    if (foto.filename) {
      return `http://localhost:8080/api/fotos/arquivo/${foto.filename}`;
    }
    // Fallback
    return '';
  };

  // Função para formatar data
  const formatarData = (data: string) => {
    // Se já está no formato "08/12/2025 23:18", retorna direto
    if (data && data.includes('/')) {
      return data;
    }
    // Se está em outro formato, tenta converter
    try {
      return new Date(data).toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch {
      return data;
    }
  };

  if (loading) {
    return (
      <div className="text-center py-8">
        <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
        <p className="text-gray-600 mt-2">Carregando fotos...</p>
      </div>
    );
  }

  if (fotos.length === 0) {
    return (
      <div className="text-center py-8 text-gray-500">
        <p className="text-lg">Nenhuma foto ainda.</p>
        <p className="text-sm">Seja o primeiro a adicionar!</p>
      </div>
    );
  }

  return (
    <>
      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
        {fotos.map((foto: any) => (
          <div
            key={foto.id}
            className="relative group cursor-pointer overflow-hidden rounded-lg aspect-square bg-gray-100"
            onClick={() => setFotoSelecionada(foto)}
          >
            <img
              src={getFotoUrl(foto)}
              alt={foto.titulo || 'Foto do ponto turístico'}
              className="w-full h-full object-cover transition-transform duration-300 group-hover:scale-110"
              onError={(e) => {
                console.error('Erro ao carregar imagem:', foto);
                e.currentTarget.src = 'https://via.placeholder.com/400x400?text=Erro+ao+carregar';
              }}
            />
            
            {/* Overlay com informações */}
            <div className="absolute inset-0 bg-gradient-to-t from-black/70 via-black/0 to-black/0 opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex flex-col justify-end p-3">
              <p className="text-white text-sm font-medium line-clamp-2">
                {foto.titulo}
              </p>
              <p className="text-white/80 text-xs mt-1">
                {foto.nomeUsuario}
              </p>
            </div>
          </div>
        ))}
      </div>

      {/* Modal de visualização */}
      {fotoSelecionada && (
        <div
          className="fixed inset-0 bg-black bg-opacity-95 flex items-center justify-center z-50 p-4"
          onClick={() => setFotoSelecionada(null)}
        >
          <div className="max-w-6xl w-full" onClick={(e) => e.stopPropagation()}>
            <div className="relative bg-white rounded-lg overflow-hidden">
              {/* Imagem */}
              <div className="relative bg-black">
                <img
                  src={getFotoUrl(fotoSelecionada)}
                  alt={fotoSelecionada.titulo}
                  className="w-full h-auto max-h-[70vh] object-contain mx-auto"
                  onError={(e) => {
                    console.error('Erro ao carregar imagem modal:', fotoSelecionada);
                    e.currentTarget.src = 'https://via.placeholder.com/800x600?text=Erro+ao+carregar';
                  }}
                />
                
                {/* Botão fechar */}
                <button
                  onClick={() => setFotoSelecionada(null)}
                  className="absolute top-4 right-4 bg-white text-gray-800 p-3 rounded-full hover:bg-gray-200 transition shadow-lg"
                  title="Fechar"
                >
                  <FaTimes size={20} />
                </button>

                {/* Botão deletar (se for dono ou admin) */}
                {( isAdmin) && (
                  <button
                    onClick={() => handleDeletar(fotoSelecionada.id, fotoSelecionada.usuarioId)}
                    className="absolute top-4 left-4 bg-red-500 text-white p-3 rounded-full hover:bg-red-600 transition shadow-lg"
                    title="Deletar foto"
                  >
                    <FaTrash size={20} />
                  </button>
                )}
              </div>

              {/* Informações da foto */}
              <div className="bg-white p-6">
                <h3 className="font-bold text-xl text-gray-800 mb-2">
                  {fotoSelecionada.titulo}
                </h3>
                
                <div className="flex items-center justify-between text-sm text-gray-600">
                  <div>
                    <p>
                      <span className="font-medium">Por:</span> {fotoSelecionada.nomeUsuario}
                    </p>
                    <p className="mt-1">
                      <span className="font-medium">Data:</span> {formatarData(fotoSelecionada.createdAt)}
                    </p>
                  </div>
                  
                  <div className="text-right">
                    <p className="text-xs text-gray-500">
                      {(fotoSelecionada.tamanho / 1024).toFixed(1)} KB
                    </p>
                    <p className="text-xs text-gray-500">
                      {fotoSelecionada.contentType?.replace('image/', '').toUpperCase()}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default GaleriaFotos;