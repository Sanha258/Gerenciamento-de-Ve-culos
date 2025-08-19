import React, { useState } from 'react';
import './VeiculoForm.css';
import Alerta from '../alertas/Alerta';
import type { Veiculo } from '../types/Veiculo';
import { VeiculoService } from '../services/VeiculoService';

interface VeiculoFormProps {
  veiculo?: Veiculo;
  onSubmit: (veiculo: Veiculo) => void;
  onCancel: () => void;
  isOpen: boolean;
}

const VeiculoForm: React.FC<VeiculoFormProps> = ({ veiculo, onSubmit, onCancel, isOpen }) => {
  const [formData, setFormData] = useState({
    modelo: veiculo?.modelo || '',
    marca: veiculo?.marca || '',
    ano: veiculo?.ano || new Date().getFullYear(),
    placa: veiculo?.placa || '',
  });
  const [alerta, setAlerta] = useState<{ mensagem: string; tipo: 'sucesso' | 'erro' } | null>(null);

  const validarPlaca = (placa: string): boolean => {
    const padraoAntigo = /^[A-Z]{3}[0-9]{4}$/;
    const padraoMercosul = /^[A-Z]{3}[0-9][A-Z][0-9]{2}$/;
    return padraoAntigo.test(placa) || padraoMercosul.test(placa);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: name === 'ano' ? Number(value) : value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.marca || !formData.modelo || !formData.placa || !formData.ano ) {
      setAlerta({ mensagem: 'Todos os campos são obrigatórios', tipo: 'erro' });
      return;
    }
    
    const placaFormatada = formData.placa.toUpperCase().replace(/-/g, '').replace(/\s/g, '');
    
    if (!validarPlaca(placaFormatada)) {
      setAlerta({ 
        mensagem: 'Formato de placa inválido. Use o padrão brasileiro (AAA1B23 ou AAA1234)', 
        tipo: 'erro' 
      });
      return;
    }

    try {
      const veiculos = await VeiculoService.listar();
      const placaExistente = veiculos.some(v => 
        v.placa.replace(/-/g, '') === placaFormatada && 
        (!veiculo?.id || v.id !== veiculo.id)
      );
      
      if (placaExistente) {
        setAlerta({ mensagem: 'Placa já cadastrada!', tipo: 'erro' });
        return;
      }
      
      const placaFormatadaExibicao = placaFormatada.replace(/([A-Z]{3})([0-9A-Z]{4})/, '$1-$2');
      
      onSubmit({
        ...formData,
        placa: placaFormatadaExibicao
      });
    } catch (error) {
      setAlerta({
        mensagem: 'Erro ao verificar placa. Tente novamente.',
        tipo: 'erro'
      });
    }
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="veiculo-form-container">
        <div className="veiculo-form-header">
          <h2 className="veiculo-form-title">
            {veiculo ? 'Editar Veículo' : 'Cadastrar Veículo'}
          </h2>
        </div>
        
        {alerta && (
          <Alerta 
            mensagem={alerta.mensagem} 
            tipo={alerta.tipo} 
            onClose={() => setAlerta(null)} 
          />
        )}
        
        <form className="veiculo-form" onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="marca">Marca</label>
            <input
              type="text"
              id="marca"
              name="marca"
              value={formData.marca}
              onChange={handleChange}
              placeholder="Ex: Toyota"
              required
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="modelo">Modelo</label>
            <input
              type="text"
              id="modelo"
              name="modelo"
              value={formData.modelo}
              onChange={handleChange}
              placeholder="Ex: Corolla"
              required
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="ano">Ano</label>
            <input
              type="number"
              id="ano"
              name="ano"
              value={formData.ano}
              onChange={handleChange}
              min="1900"
              max={new Date().getFullYear() + 1}
              placeholder="Ex: 2022"
              required
            />
          </div>
          
          <div className="form-group">
            <label htmlFor="placa">Placa</label>
            <input
              type="text"
              id="placa"
              name="placa"
              value={formData.placa}
              onChange={(e) => {
                const value = e.target.value.toUpperCase().replace(/[^A-Z0-9]/g, '');
                setFormData({ ...formData, placa: value });
              }}
              placeholder="Ex: ABC1D23 ou ABC1234"
              maxLength={7}
              required
            />
          </div>
          
          <div className="form-actions">
            <button type="button" className="cancel-btn" onClick={onCancel}>
              Cancelar
            </button>
            <button type="submit" className="submit-btn">
              {veiculo ? 'Salvar Alterações' : 'Cadastrar Veículo'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default VeiculoForm;