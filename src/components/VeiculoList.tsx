import React, { useState } from 'react';
import './VeiculoList.css';
import type { Veiculo } from '../types/Veiculo';

interface VeiculoListProps {
  veiculos: Veiculo[];
  onEdit: (veiculo: Veiculo) => void;
  onDelete: (id: number) => void;
}

const VeiculoList: React.FC<VeiculoListProps> = ({ veiculos, onEdit, onDelete }) => {
  const [showConfirm, setShowConfirm] = useState<number | null>(null);

  const handleDeleteClick = (id: number) => {
    setShowConfirm(id);
  };

  const confirmDelete = (id: number) => {
    onDelete(id);
    setShowConfirm(null);
  };

  const cancelDelete = () => {
    setShowConfirm(null);
  };

  return (
    <div className="veiculo-list-container">
      <div className="veiculo-list-header">
        <div className="header-cell">ID</div>
        <div className="header-cell">Marca</div>
        <div className="header-cell">Modelo</div>
        <div className="header-cell">Ano</div>
        <div className="header-cell">Placa</div>
        <div className="header-cell">Ações</div>
      </div>
      <div className="veiculo-list-body">
        {veiculos.map((veiculo, index) => (
          <div key={veiculo.id} className={`veiculo-row ${index % 2 === 0 ? 'even' : 'odd'}`}>
            <div className="row-cell" data-label="ID">{veiculo.id}</div>
            <div className="row-cell" data-label="Marca">{veiculo.marca}</div>
            <div className="row-cell" data-label="Modelo">{veiculo.modelo}</div>
            <div className="row-cell" data-label="Ano">{veiculo.ano}</div>
            <div className="row-cell" data-label="Placa">{veiculo.placa}</div>
            <div className="row-cell actions">
              <button className="edit-btn" onClick={() => onEdit(veiculo)}>
                <span className="icon">✏️</span> Editar
              </button>
              <button className="delete-btn" onClick={() => veiculo.id && handleDeleteClick(veiculo.id)}>
                <span className="icon">🗑️</span> Remover
              </button>
            </div>
            
            {showConfirm === veiculo.id && (
              <div className="confirm-dialog">
                <div className="confirm-content">
                  <p>Tem certeza que deseja remover este veículo?</p>
                  <div className="confirm-buttons">
                    <button className="confirm-btn confirm-yes" onClick={() => confirmDelete(veiculo.id!)}>
                      Sim
                    </button>
                    <button className="confirm-btn confirm-no" onClick={cancelDelete}>
                      Não
                    </button>
                  </div>
                </div>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default VeiculoList;