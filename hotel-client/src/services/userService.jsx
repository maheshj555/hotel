import api from '../api/axios';

export const getUserDetails = async () => {
  const response = await api.get('/user/me');
  return response.data;
};
