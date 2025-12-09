import { Navigate } from 'react-router-dom';
import authService from '../service/auth';

const PrivateRoute = ({ children, adminOnly = false } : { children: any, adminOnly: boolean }) => {
  const isAuthenticated = authService.isAuthenticated();
  const isAdmin = authService.isAdmin();

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  if (adminOnly && !isAdmin) {
    return <Navigate to="/" />;
  }

  return children;
};

export default PrivateRoute;