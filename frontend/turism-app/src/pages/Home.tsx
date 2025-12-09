import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaMapMarkerAlt, FaStar, FaSearch } from 'react-icons/fa';
import pontoService from '../service/point';

const Home = () => {
  const [destaques, setDestaques] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    carregarDestaques();
  }, []);

  const carregarDestaques = async () => {
    try {
      const response = await pontoService.listar({ size: 6, ordenacao: 'nota_media' });
      setDestaques(response.content || []);
    } catch (error) {
      console.error('Erro ao carregar destaques:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen">
      {/* Hero Section */}
      <section className="bg-gradient-to-r from-purple-800 to-purple-800 text-white py-24">
        <div className="container mx-auto px-4 text-center">
          <h1 className="text-5xl md:text-6xl font-bold mb-6">
            Descubra Destinos Incríveis
          </h1>
          <p className="text-xl md:text-2xl mb-8 text-purple-100">
            Explore os melhores pontos turísticos e compartilhe suas experiências
          </p>
          <Link
            to="/pontos"
            className="inline-flex items-center space-x-2 bg-yellow-400 text-purple-900 px-8 py-4 rounded-full text-lg font-semibold hover:bg-yellow-300 transition transform hover:scale-105"
          >
            <FaSearch />
            <span>Explorar Pontos Turísticos</span>
          </Link>
        </div>
      </section>

      {/* Stats Section */}
      <section className="bg-white py-12 shadow-md">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 text-center">
            <div>
              <div className="text-4xl font-bold text-purple-600 mb-2">500+</div>
              <div className="text-gray-600">Pontos Turísticos</div>
            </div>
            <div>
              <div className="text-4xl font-bold text-purple-600 mb-2">10k+</div>
              <div className="text-gray-600">Avaliações</div>
            </div>
            <div>
              <div className="text-4xl font-bold text-purple-600 mb-2">5k+</div>
              <div className="text-gray-600">Usuários Ativos</div>
            </div>
          </div>
        </div>
      </section>

      {/* Destaques */}
      <section className="py-16 bg-gray-50">
        <div className="container mx-auto px-4">
          <h2 className="text-3xl font-bold text-center mb-12">Destinos em Destaque</h2>
          
          {loading ? (
            <div className="text-center py-12">
              <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-purple-600"></div>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
              {destaques.map((ponto: any) => (
                <Link
                  key={ponto.id}
                  to={`/pontos/${ponto.id}`}
                  className="bg-white rounded-xl shadow-md overflow-hidden hover:shadow-xl transition transform hover:-translate-y-2"
                >
                  <div className="h-48 bg-gradient-to-br from-purple-600 to-purple-600 flex items-center justify-center">
                    <FaMapMarkerAlt className="text-white text-6xl" />
                  </div>
                  <div className="p-6">
                    <h3 className="text-xl font-bold mb-2 line-clamp-1">{ponto.nome}</h3>
                    <p className="text-gray-600 mb-3">
                      {ponto.cidade}, {ponto.estado}
                    </p>
                    <div className="flex items-center space-x-2">
                      <div className="flex items-center text-yellow-500">
                        {[...Array(5)].map((_, i) => (
                          <FaStar
                            key={i}
                            className={i < Math.round(ponto.notaMedia || 0) ? 'text-yellow-500' : 'text-gray-300'}
                          />
                        ))}
                      </div>
                      <span className="text-gray-600 text-sm">
                        ({ponto.quantidadeAvaliacoes || 0} avaliações)
                      </span>
                    </div>
                  </div>
                </Link>
              ))}
            </div>
          )}

          <div className="text-center mt-12">
            <Link
              to="/pontos"
              className="inline-block bg-purple-600 text-white px-8 py-3 rounded-lg font-semibold hover:bg-purple-700 transition"
            >
              Ver Todos os Pontos
            </Link>
          </div>
        </div>
      </section>

      {/* Features */}
      <section className="py-16 bg-white">
        <div className="container mx-auto px-4">
          <h2 className="text-3xl font-bold text-center mb-12">Por que usar o TurismoApp?</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="bg-purple-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <FaMapMarkerAlt className="text-purple-600 text-2xl" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Descubra Lugares</h3>
              <p className="text-gray-600">
                Encontre os melhores pontos turísticos com avaliações de viajantes reais
              </p>
            </div>
            <div className="text-center">
              <div className="bg-purple-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <FaStar className="text-purple-600 text-2xl" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Avalie e Comente</h3>
              <p className="text-gray-600">
                Compartilhe suas experiências e ajude outros viajantes
              </p>
            </div>
            <div className="text-center">
              <div className="bg-purple-100 w-16 h-16 rounded-full flex items-center justify-center mx-auto mb-4">
                <FaSearch className="text-purple-600 text-2xl" />
              </div>
              <h3 className="text-xl font-semibold mb-2">Planeje sua Viagem</h3>
              <p className="text-gray-600">
                Encontre hospedagens e informações úteis sobre cada destino
              </p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;