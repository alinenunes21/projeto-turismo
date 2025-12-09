import { useState, FC, FormEvent, ChangeEvent } from 'react';
import { FaTimes } from 'react-icons/fa';
import { toast } from 'react-toastify';
import { Hospedagem } from '../types';
import { hospedagemService } from '../service/avaliacao';

interface ModalHospedagemProps {
  pontoId: number;
  hospedagemExistente?: Hospedagem;
  onClose: () => void;
  onSuccess: () => void;
}

interface HospedagemFormData {
  nome: string;
  endereco: string;
  telefone: string;
  precoMedio: string;
  tipo: string;
  linkReserva: string;
}

const ModalHospedagem: FC<ModalHospedagemProps> = ({ 
  pontoId, 
  hospedagemExistente, 
  onClose, 
  onSuccess 
}) => {
  const [dados, setDados] = useState<HospedagemFormData>({
    nome: hospedagemExistente?.nome || '',
    endereco: hospedagemExistente?.endereco || '',
    telefone: hospedagemExistente?.telefone || '',
    precoMedio: hospedagemExistente?.precoMedio?.toString() || '',
    tipo: hospedagemExistente?.tipo || 'Hotel',
    linkReserva: hospedagemExistente?.linkReserva || ''
  });
  const [loading, setLoading] = useState<boolean>(false);

  const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLSelectElement>): void => {
    const { name, value } = e.target;
    setDados(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: FormEvent<HTMLFormElement>): Promise<void> => {
    e.preventDefault();

    if (!dados.nome.trim() || !dados.endereco.trim() || !dados.telefone.trim()) {
      toast.error('Preencha todos os campos obrigatórios');
      return;
    }

    setLoading(true);
    try {
      const payload = {
        pontoId,
        nome: dados.nome,
        endereco: dados.endereco,
        telefone: dados.telefone,
        precoMedio: parseFloat(dados.precoMedio) || 0,
        tipo: dados.tipo,
        linkReserva: dados.linkReserva || undefined
      };

      if (hospedagemExistente) {
        await hospedagemService.atualizar(hospedagemExistente.id, payload);
        toast.success('Hospedagem atualizada com sucesso!');
      } else {
        await hospedagemService.criar(payload);
        toast.success('Hospedagem cadastrada com sucesso!');
      }
      
      onSuccess();
    } catch (error) {
      toast.error('Erro ao salvar hospedagem');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-2xl max-w-lg w-full p-6 max-h-[90vh] overflow-y-auto">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold">
            {hospedagemExistente ? 'Editar Hospedagem' : 'Adicionar Hospedagem'}
          </h2>
          <button onClick={onClose} className="text-gray-500 hover:text-gray-700 transition">
            <FaTimes size={24} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Nome *
            </label>
            <input
              type="text"
              name="nome"
              value={dados.nome}
              onChange={handleChange}
              placeholder="Ex: Hotel Praia Grande"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Endereço *
            </label>
            <input
              type="text"
              name="endereco"
              value={dados.endereco}
              onChange={handleChange}
              placeholder="Ex: Rua das Flores, 123"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Telefone *
            </label>
            <input
              type="tel"
              name="telefone"
              value={dados.telefone}
              onChange={handleChange}
              placeholder="Ex: (11) 99999-9999"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Tipo
            </label>
            <select
              name="tipo"
              value={dados.tipo}
              onChange={handleChange}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            >
              <option value="Hotel">Hotel</option>
              <option value="Pousada">Pousada</option>
              <option value="Hostel">Hostel</option>
              <option value="Resort">Resort</option>
              <option value="Airbnb">Airbnb</option>
              <option value="Camping">Camping</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Preço Médio (R$/noite)
            </label>
            <input
              type="number"
              name="precoMedio"
              value={dados.precoMedio}
              onChange={handleChange}
              placeholder="Ex: 150.00"
              step="0.01"
              min="0"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Link de Reserva (opcional)
            </label>
            <input
              type="url"
              name="linkReserva"
              value={dados.linkReserva}
              onChange={handleChange}
              placeholder="https://exemplo.com/reservar"
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-transparent"
            />
          </div>

          <div className="flex space-x-3 pt-4">
            <button
              type="submit"
              disabled={loading}
              className="flex-1 bg-purple-600 text-white py-3 rounded-lg font-semibold hover:bg-purple-700 disabled:opacity-50 disabled:cursor-not-allowed transition"
            >
              {loading ? 'Salvando...' : 'Salvar'}
            </button>
            <button
              type="button"
              onClick={onClose}
              className="px-6 bg-gray-200 text-gray-700 py-3 rounded-lg font-semibold hover:bg-gray-300 transition"
            >
              Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ModalHospedagem;