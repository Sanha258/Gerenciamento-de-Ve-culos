import React, { useState, useEffect, useRef } from 'react';
import './Pesquisa.css';
import type { Veiculo } from '../types/Veiculo';

interface PesquisarProps {
  veiculos: Veiculo[];
  onSearchChange: (termo: string) => void;
  onFilterChange: (marcas: string[]) => void;
}

const Pesquisar: React.FC<PesquisarProps> = ({ veiculos, onSearchChange, onFilterChange }) => {
  const [termoPesquisa, setTermoPesquisa] = useState('');
  const [marcasSelecionadas, setMarcasSelecionadas] = useState<string[]>([]);
  const [dropdownAberto, setDropdownAberto] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  // Extrair marcas únicas dos veículos
  const marcasUnicas = Array.from(new Set(veiculos.map(v => v.marca)))
    .filter(marca => marca && marca.trim() !== '')
    .sort();

  const handlePesquisaChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const termo = e.target.value;
    setTermoPesquisa(termo);
    onSearchChange(termo);
  };

  const handleMarcaChange = (marca: string) => {
    const novasMarcas = marcasSelecionadas.includes(marca)
      ? marcasSelecionadas.filter(m => m !== marca)
      : [...marcasSelecionadas, marca];
    
    setMarcasSelecionadas(novasMarcas);
    onFilterChange(novasMarcas);
  };

  const limparFiltros = () => {
    setTermoPesquisa('');
    setMarcasSelecionadas([]);
    setDropdownAberto(false);
    onSearchChange('');
    onFilterChange([]);
    inputRef.current?.focus();
  };

  const toggleDropdown = () => {
    setDropdownAberto(!dropdownAberto);
  };

  const fecharDropdown = () => {
    setDropdownAberto(false);
  };

  // Fechar dropdown ao clicar fora
  useEffect(() => {
    const handleClickFora = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        fecharDropdown();
      }
    };

    document.addEventListener('mousedown', handleClickFora);
    return () => document.removeEventListener('mousedown', handleClickFora);
  }, []);

  const temFiltrosAtivos = termoPesquisa || marcasSelecionadas.length > 0;

  return (
    <div className="pesquisar-container">
      <div className="pesquisar-header">
        <h3>Filtrar Veículos</h3>
        {temFiltrosAtivos && (
          <button 
            className="limpar-filtros-btn" 
            onClick={limparFiltros}
            type="button"
          >
            Limpar Filtros
          </button>
        )}
      </div>

      <div className="filtros-row">
        {/* Campo de pesquisa */}
        <div className="pesquisa-input-container">
          <input
            ref={inputRef}
            type="text"
            placeholder="Pesquisar por modelo..."
            value={termoPesquisa}
            onChange={handlePesquisaChange}
            className="pesquisa-input"
          />
          <span className="pesquisa-icon">🔍</span>
        </div>

        {/* Filtro por marca */}
        <div className="filtro-marca-container" ref={dropdownRef}>
          <button 
            className={`filtro-marca-btn ${marcasSelecionadas.length > 0 ? 'ativo' : ''}`}
            onClick={toggleDropdown}
            type="button"
          >
            <span>Filtrar Marcas</span>
            {marcasSelecionadas.length > 0 && (
              <span className="marcas-count">
                {marcasSelecionadas.length}
              </span>
            )}
            <span className={`dropdown-arrow ${dropdownAberto ? 'aberto' : ''}`}>▼</span>
          </button>

          {dropdownAberto && (
            <div className="marcas-dropdown">
              <div className="marcas-list">
                {marcasUnicas.length > 0 ? (
                  marcasUnicas.map(marca => (
                    <label key={marca} className="marca-option">
                      <input
                        type="checkbox"
                        checked={marcasSelecionadas.includes(marca)}
                        onChange={() => handleMarcaChange(marca)}
                      />
                      <span className="checkmark"></span>
                      <span>{marca}</span>
                    </label>
                  ))
                ) : (
                  <div className="marca-option" style={{ cursor: 'default', opacity: 0.7 }}>
                    Nenhuma marca disponível
                  </div>
                )}
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Tags de filtros ativos */}
      {(termoPesquisa || marcasSelecionadas.length > 0) && (
        <div className="filtros-ativos">
          {termoPesquisa && (
            <span className="filtro-tag">
              Pesquisa: "{termoPesquisa}"
              <button 
                onClick={() => {
                  setTermoPesquisa('');
                  onSearchChange('');
                  inputRef.current?.focus();
                }}
                type="button"
              >
                ×
              </button>
            </span>
          )}
          {marcasSelecionadas.map(marca => (
            <span key={marca} className="filtro-tag">
              {marca}
              <button 
                onClick={() => handleMarcaChange(marca)}
                type="button"
              >
                ×
              </button>
            </span>
          ))}
        </div>
      )}
    </div>
  );
};

export default Pesquisar;