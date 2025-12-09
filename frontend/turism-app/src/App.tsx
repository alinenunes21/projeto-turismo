import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import Header from './components/Header';
import Footer from './components/Footer';
import Home from './pages/Home';
import Login from './pages/Login';
import Cadastro from './pages/Cadastro';
import PontosList from './pages/PontosList';
import PontoDetail from './pages/PontoDetail';
import Admin from './pages/Admin';
import PontoForm from './pages/PontoForm';
import PrivateRoute from './route/PrivateRoute';

function App() {
  return (
    <BrowserRouter>
      <div className="flex flex-col min-h-screen">
        <Header />
        <main className="flex-grow">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/cadastro" element={<Cadastro />} />
            <Route path="/pontos" element={<PontosList />} />
            <Route path="/pontos/:id" element={<PontoDetail />} />
            
            {/* Rotas Admin */}
            <Route
              path="/admin"
              element={
                // <PrivateRoute adminOnly>
                  <Admin />
                // </PrivateRoute>
              }
            />
            <Route
              path="/admin/pontos/novo"
              element={
                // <PrivateRoute adminOnly>
                  <PontoForm />
                // </PrivateRoute>
              }
            />
            <Route
              path="/admin/pontos/editar/:id"
              element={
                // <PrivateRoute adminOnly>
                  <PontoForm />
                // </PrivateRoute>
              }
            />
          </Routes>
        </main>
        <Footer />
      </div>
      
      <ToastContainer
        position="top-right"
        autoClose={3000}
        hideProgressBar={false}
        newestOnTop
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
      />
    </BrowserRouter>
  );
}

export default App;