import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const authService = {
  register: async (userData: any) => {
    const response = await axios.post(`${API_URL}/auth/register`, userData);
    return response.data;
  },

  login: async (credentials: any) => {
    const response = await axios.post(`${API_URL}/auth/login`, credentials);
    if (response.data) {
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('isAuthenticated', 'true');
      localStorage.setItem('role', response.data.role);
    }
    return response.data;
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  getCurrentUser: () => {
    // const userStr = localStorage?.getItem('user');
    // return !!userStr ? JSON?.parse(userStr) : null;
    // return null
  },

  getToken: () => {
    return localStorage.getItem('token');
  },

  isAuthenticated: () => {
    return localStorage.getItem('isAuthenticated') === 'true';
  },

  isAdmin: () => {
    return localStorage.getItem('role') === 'ADMIN';
  }
};

export default authService;