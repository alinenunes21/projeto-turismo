import { useState, useEffect } from 'react';
import { useNavigate, useParams, Navigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import pontoService from '../service/point';
import authService from '../service/auth';


const PontoForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [carregando, setCarregando] = useState(!!id);
  const { register, handleSubmit, formState: { errors }, setValue } = useForm();
  const isAdmin = authService.isAdmin();

  useEffect(() => {
    if (id) {
      carregarPonto();
    }
  }, [id]);

  const carregarPonto = async () => {
    try {
      const data = await pontoService.buscarPorId(id || '');
      Object.keys(data).forEach(key => {
        setValue(key, data[key]);
      });
    } catch (error) {
      toast.error('Erro ao carregar ponto turístico');
      navigate('/admin');
    } finally {
      setCarregando(false);
    }
  };

  const onSubmit = async (data: any) => {
    setLoading(true);
    try {
      const payload = {
        ...data,
        latitude: parseFloat(data.latitude),
        longitude: parseFloat(data.longitude)
      };

      if (id) {
        await pontoService.atualizar(id, payload);
        toast.success('Ponto turístico atualizado com sucesso!');
      } else {
        await pontoService.criar(payload);
        toast.success('Ponto turístico cadastrado com sucesso!');
      }
      navigate('/admin');
    } catch (error) {
      toast.error('Erro ao salvar ponto turístico');
    } finally {
      setLoading(false);
    }
  };

//   if (!isAdmin) {
//     return <Navigate to="/" />;
//   }

  if (carregando) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-purple-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="container mx-auto px-4 max-w-3xl">
        <h1 className="text-4xl font-bold mb-8">
          {id ? 'Editar Ponto Turístico' : 'Novo Ponto Turístico'}
        </h1>

        <form onSubmit={handleSubmit(onSubmit)} className="bg-white rounded-xl shadow-md p-8 space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Nome *
            </label>
            <input
              type="text"
              {...register('nome', { required: 'Nome é obrigatório' })}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
              placeholder="Ex: Cristo Redentor"
            />
            {errors.nome && <p className="text-red-500 text-sm mt-1"></p>}
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Descrição *
            </label>
            <textarea
              {...register('descricao', { required: 'Descrição é obrigatória' })}
              rows={5}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 resize-none"
              placeholder="Descreva o ponto turístico..."
            />
            {errors.descricao && <p className="text-red-500 text-sm mt-1">Campo obrigatório</p>}
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Cidade *
              </label>
              <input
                type="text"
                {...register('cidade', { required: 'Cidade é obrigatória' })}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
                placeholder="Rio de Janeiro"
              />
              {errors.cidade && <p className="text-red-500 text-sm mt-1">Campo obrigatório</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Estado *
              </label>
              <input
                type="text"
                {...register('estado', { required: 'Estado é obrigatório' })}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
                placeholder="RJ"
                maxLength={2}
              />
              {errors.estado && <p className="text-red-500 text-sm mt-1">Campo obrigatório</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                País *
              </label>
              <input
                type="text"
                {...register('pais', { required: 'País é obrigatório' })}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
                placeholder="Brasil"
              />
              {errors.pais && <p className="text-red-500 text-sm mt-1">Campo obrigatório</p>}
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Endereço
            </label>
            <input
              type="text"
              {...register('endereco')}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
              placeholder="Endereço completo"
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Latitude *
              </label>
              <input
                type="number"
                step="any"
                {...register('latitude', { 
                  required: 'Latitude é obrigatória',
                  valueAsNumber: true 
                })}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
                placeholder="-22.951916"
              />
              {errors.latitude && <p className="text-red-500 text-sm mt-1">Campo obrigatório</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Longitude *
              </label>
              <input
                type="number"
                step="any"
                {...register('longitude', { 
                  required: 'Longitude é obrigatória',
                  valueAsNumber: true 
                })}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500"
                placeholder="-43.210487"
              />
              {errors.longitude && <p className="text-red-500 text-sm mt-1">Campo obrigatório</p>}
            </div>
          </div>

          <div className="bg-purple-50 border border-purple-200 rounded-lg p-4">
            <p className="text-sm text-purple-800">
              <strong>Dica:</strong> Para obter latitude e longitude, busque o local no Google Maps, 
              clique com o botão direito no marcador e copie as coordenadas.
            </p>
          </div>

          <div className="flex space-x-4 pt-4">
            <button
              type="submit"
              disabled={loading}
              className="flex-1 bg-purple-600 text-white py-3 rounded-lg font-semibold hover:bg-purple-700 disabled:opacity-50"
            >
              {loading ? 'Salvando...' : id ? 'Atualizar' : 'Cadastrar'}
            </button>
            <button
              type="button"
              onClick={() => navigate('/admin')}
              className="px-8 bg-gray-200 text-gray-700 py-3 rounded-lg font-semibold hover:bg-gray-300"
            >
              Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default PontoForm;