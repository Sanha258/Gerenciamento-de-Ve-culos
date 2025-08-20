import axios from 'axios';
import type { Veiculo } from '../types/Veiculo';

const API_URL = 'http://localhost:8080/api/veiculos'; 

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
    const { response, request, message } = error;
    if (response) {
      console.error('Server Error:', {
        status: response.status,
        data: response.data,
        headers: response.headers
      });
    } else if (request) {
      console.error('Network Error:', request);
    } else {
      console.error('Request Error:', message);
    }
    return Promise.reject(error);
  }
);

// Função utilitária para extrair mensagens de erro
function extrairMensagemErro(error: unknown, padrao: string): string {
  if (axios.isAxiosError(error)) {
    const data = error.response?.data;
    if (typeof data === 'string') return data;
    if (typeof data === 'object' && data?.message) return data.message;
  }
  return padrao;
}

export const VeiculoService = {
  async listar(): Promise<Veiculo[]> {
    const response = await api.get<Veiculo[]>('');
    return response.data;
  },

  async criar(veiculo: Omit<Veiculo, 'id'>): Promise<Veiculo> {
    try {
      const response = await api.post<Veiculo>('', veiculo);
      return response.data;
    } catch (error) {
      throw new Error(extrairMensagemErro(error, 'Erro ao cadastrar veículo.'));
    }
  },

  async atualizar(id: number, veiculo: Partial<Veiculo>): Promise<Veiculo> {
    try {
      const response = await api.put<Veiculo>(`/${id}`, veiculo);
      return response.data;
    } catch (error) {
      throw new Error(extrairMensagemErro(error, 'Erro ao atualizar veículo.'));
    }
  },

  async excluir(id: number): Promise<void> {
    try {
      await api.delete(`/${id}`);
    } catch (error) {
      throw new Error(extrairMensagemErro(error, 'Erro ao excluir veículo.'));
    }
  },

};
