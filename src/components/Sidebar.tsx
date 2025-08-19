// components/Sidebar.tsx
import React from 'react';
import './Sidebar.css';

interface SidebarProps {
  onCadastrarClick: () => void;
  onSairClick: () => void;
}

const Sidebar: React.FC<SidebarProps> = ({ onCadastrarClick }) => {
  return (
    <div className="sidebar">
      <div className="sidebar-header">
        <h2>Veículos</h2>
      </div>
      <div className="sidebar-content">
        <button className="sidebar-btn cadastrar-btn" onClick={onCadastrarClick}>
          <span className="icon">+</span> Cadastrar
        </button>
      </div>
    </div>
  );
};

export default Sidebar;