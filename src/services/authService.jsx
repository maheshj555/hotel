
import axios from 'axios';

export const login = async (email, password) => {
  try {
    const response = await axios.post("http://localhost:8080/api/auth/login", {
      email,
      password
    });
    return response.data; 
  } catch (error) {
   
    if (error.response && error.response.data) {
      throw error.response.data; 
    } else {
      throw { message: "Network error or server is down" };
    }
  }
};
