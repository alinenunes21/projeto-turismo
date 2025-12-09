import { Link, useNavigate } from 'react-router-dom';
import { FaMapMarkerAlt, FaUser, FaSignOutAlt } from 'react-icons/fa';
import authService from '../service/auth';

const Header = () => {
  const navigate = useNavigate();
  const isAuthenticated = authService.isAuthenticated();
  const isAdmin = authService.isAdmin();
  const user = authService.getCurrentUser();

  const handleLogout = () => {
    authService.logout();
    navigate('/login');
  };

  return (
    <header className="bg-gradient-to-r from-purple-800 to-purple-800 text-white shadow-lg">
      <div className="container mx-auto px-4 py-4">
        <div className="flex items-center justify-between">
          <Link to="/" className="flex items-center space-x-2 text-2xl font-bold hover:opacity-90 transition">
            <FaMapMarkerAlt className="text-white-300" />
            <span>TurismoApp</span>
          </Link>

          <nav className="hidden md:flex items-center space-x-6">
            <Link to="/pontos" className="hover:text-yellow-300 transition font-medium">
              Pontos Turísticos
            </Link>
            
            {/* {isAdmin && ( */}
              <Link to="/admin" className="hover:text-white-300 transition font-medium">
                Administração
              </Link>
            {/* )} */}

            {isAuthenticated ? (
              <div className="flex items-center space-x-4">
                <div className="flex items-center space-x-2 bg-purple-700 px-4 py-2 rounded-full">
                  <FaUser />
                  <span className="font-medium">{user?.login}</span>
                </div>
                <button
                  onClick={handleLogout}
                  className="flex items-center space-x-2 bg-red-500 hover:bg-red-600 px-4 py-2 rounded-lg transition"
                >
                  <FaSignOutAlt />
                  <span>Sair</span>
                </button>
              </div>
            ) : (
              <div className="flex space-x-3">
                <Link
                  to="/login"
                  className="bg-white text-purple-600 px-4 py-2 rounded-lg font-medium hover:bg-purple-50 transition"
                >
                  Entrar
                </Link>
                <Link
                  to="/cadastro"
                  className="bg-yellow-400 text-purple-900 px-4 py-2 rounded-lg font-medium hover:bg-yellow-300 transition"
                >
                  Cadastrar
                </Link>
              </div>
            )}
          </nav>

          {/* Mobile menu button */}
          <button className="md:hidden">
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
            </svg>
          </button>
        </div>
      </div>
    </header>
  );
};

export default Header;