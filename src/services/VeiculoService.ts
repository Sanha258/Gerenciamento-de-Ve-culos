// VeiculoService.ts
import axios from 'axios';
import type { Veiculo } from '../types/Veiculo';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  },
  timeout: 10000
});

api.interceptors.response.use(
  response => response,
  error => {
    if (error.response) {
      console.error('Server Error:', {
        status: error.response.status,
        data: error.response.data,
        headers: error.response.headers
      });
    } else if (error.request) {
      console.error('Network Error:', error.request);
    } else {
      console.error('Request Error:', error.message);
    }
    return Promise.reject(error);
  }
);

export const VeiculoService = {
  async listar(): Promise<Veiculo[]> {
    try {
      const response = await api.get<Veiculo[]>('/veiculos');
      return response.data;
    } catch (error) {
      console.error('Error fetching vehicles:', error);
      throw new Error('Falha ao carregar veículos. Tente novamente mais tarde.');
    }
  },

  async criar(veiculo: Omit<Veiculo, 'id'>): Promise<Veiculo> {
    try {
      const response = await api.post<Veiculo>('/veiculos', veiculo);
      return response.data;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        let serverMessage = 'Erro ao cadastrar veículo.';
        if (error.response?.data) {
          if (typeof error.response.data === 'string') {
            serverMessage = error.response.data;
          } else if (typeof error.response.data === 'object') {
            serverMessage = error.response.data.message || serverMessage;
          }
        }
        throw new Error(serverMessage);
      }
      throw new Error('Erro desconhecido ao cadastrar veículo');
    }
  },
  
  async atualizar(id: number, veiculo: Partial<Veiculo>): Promise<Veiculo> {
    try {
      const response = await api.put<Veiculo>(`/veiculos/${id}`, veiculo);
      return response.data;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        let serverMessage = 'Erro ao atualizar veículo.';
        if (error.response?.data) {
          if (typeof error.response.data === 'string') {
            serverMessage = error.response.data;
          } else if (typeof error.response.data === 'object') {
            serverMessage = error.response.data.message || serverMessage;
          }
        }
        throw new Error(serverMessage);
      }
      throw new Error('Erro desconhecido ao atualizar veículo');
    }
  },

  async excluir(id: number): Promise<void> {
    try {
      await api.delete(`/veiculos/${id}`);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        let serverMessage = 'Erro ao excluir veículo.';
        if (error.response?.data) {
          if (typeof error.response.data === 'string') {
            serverMessage = error.response.data;
          } else if (typeof error.response.data === 'object') {
            serverMessage = error.response.data.message || serverMessage;
          }
        }
        throw new Error(serverMessage);
      }
      throw new Error('Erro desconhecido ao excluir veículo');
    }
  }
};