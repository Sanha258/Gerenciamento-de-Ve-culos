// components/VeiculoForm.tsx
import React, { useState } from 'react';
import './VeiculoForm.css';
import Alerta from '../alertas/Alerta';
import type { Veiculo } from '../types/Veiculo';

interface VeiculoFormProps {
  veiculo?: {
    modelo: string;
    marca: string;
    ano: number;
    placa: string;
    cor: string;
  };
  onSubmit: (veiculo: Veiculo) => void;
  onCancel: () => void;
}

const VeiculoForm: React.FC<VeiculoFormProps> = ({ veiculo, onSubmit, onCancel }) => {
  const [formData, setFormData] = useState({
    modelo: veiculo?.modelo || '',
    marca: veiculo?.marca || '',
    ano: veiculo?.ano || new Date().getFullYear(),
    placa: veiculo?.placa || '',
    cor: veiculo?.cor || '',
  });
  const [alerta, setAlerta] = useState<{ mensagem: string; tipo: 'sucesso' | 'erro' } | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: name === 'ano' ? Number(value) : value });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.marca || !formData.modelo || !formData.placa || !formData.ano || !formData.cor) {
      setAlerta({ mensagem: 'Todos os campos são obrigatórios', tipo: 'erro' });
      return;
    }
    
    onSubmit(formData);
  };

  return (
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
            onChange={handleChange}
            placeholder="Ex: ABC1D23"
            required
          />
        </div>
        
        <div className="form-group">
          <label htmlFor="cor">Cor</label>
          <input
            type="text"
            id="cor"
            name="cor"
            value={formData.cor}
            onChange={handleChange}
            placeholder="Ex: Prata"
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
  );
};

export default VeiculoForm;