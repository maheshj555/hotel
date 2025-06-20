import axios from './axios';

const refreshTokenEndpoint = '/auth/refresh';

let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
  failedQueue.forEach(prom => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

axios.interceptors.response.use(
  res => res,
  async err => {
    const originalRequest = err.config;

   
    if (err.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        })
          .then(token => {
            originalRequest.headers.Authorization = `Bearer ${token}`;
            return axios(originalRequest);
          })
          .catch(Promise.reject);
      }

      originalRequest._retry = true;
      isRefreshing = true;

      const refreshToken = sessionStorage.getItem('refreshToken');

      try {
        const response = await axios.post(refreshTokenEndpoint, { refreshToken });
        const newAccessToken = response.data.accessToken;

        sessionStorage.setItem('accessToken', newAccessToken);
        axios.defaults.headers.Authorization = `Bearer ${newAccessToken}`;
        processQueue(null, newAccessToken);

        return axios(originalRequest);
      } catch (error) {
        processQueue(error, null);
        sessionStorage.clear(); 
        window.location.href = '/'; 
        return Promise.reject(error);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(err);
  }
);
