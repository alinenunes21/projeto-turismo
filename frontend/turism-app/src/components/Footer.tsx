import { FaGithub, FaHeart } from 'react-icons/fa';

const Footer = () => {
  return (
    <footer className="bg-gray-800 text-white mt-auto">
      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div>
            <h3 className="text-xl font-bold mb-4">TurismoApp</h3>
            <p className="text-gray-400">
              Descubra os melhores pontos turísticos e compartilhe suas experiências de viagem.
            </p>
          </div>

          <div>
            <h4 className="text-lg font-semibold mb-4">Links Rápidos</h4>
            <ul className="space-y-2 text-gray-400">
              <li><a href="/pontos" className="hover:text-white transition">Pontos Turísticos</a></li>
              <li><a href="/sobre" className="hover:text-white transition">Sobre</a></li>
              <li><a href="/contato" className="hover:text-white transition">Contato</a></li>
            </ul>
          </div>

          <div>
            <h4 className="text-lg font-semibold mb-4">Tecnologias</h4>
            <p className="text-gray-400 text-sm">
              React + Vite • Spring Boot • PostgreSQL • MongoDB • Redis • Docker
            </p>
          </div>
        </div>

        <div className="border-t border-gray-700 mt-8 pt-6 flex flex-col md:flex-row justify-between items-center">
          <p className="text-gray-400 text-sm">
            Desenvolvido com <FaHeart className="inline text-red-500" /> por Aline Nunes & Aline Ayumi
          </p>
          <a
            href="https://github.com"
            target="_blank"
            rel="noopener noreferrer"
            className="flex items-center space-x-2 text-gray-400 hover:text-white transition mt-4 md:mt-0"
          >
            <FaGithub className="text-xl" />
            <span>GitHub</span>
          </a>
        </div>
      </div>
    </footer>
  );
};

export default Footer;