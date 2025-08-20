import './App.css';
import { useState, useEffect } from 'react';
import Sidebar from './components/Sidebar';
import VeiculoList from './components/VeiculoList';
import VeiculoForm from './components/VeiculoForm';
import Pesquisar from './components/Pesquisa';
import Alerta from './alertas/Alerta';
import { VeiculoService } from './services/VeiculoService';
import type { Veiculo } from './types/Veiculo';

function App() {
  const [veiculos, setVeiculos] = useState<Veiculo[]>([]);
  const [veiculosFiltrados, setVeiculosFiltrados] = useState<Veiculo[]>([]);
  const [showForm, setShowForm] = useState(false);
  const [editingVeiculo, setEditingVeiculo] = useState<Veiculo | null>(null);
  const [alerta, setAlerta] = useState<{ mensagem: string; tipo: 'sucesso' | 'erro' } | null>(null);
  const [termoPesquisa, setTermoPesquisa] = useState('');
  const [marcasFiltro, setMarcasFiltro] = useState<string[]>([]);

  useEffect(() => {
    carregarVeiculos();
  }, []);

  useEffect(() => {
    let veiculosFiltrados = veiculos;

    if (termoPesquisa) {
      const termoLower = termoPesquisa.toLowerCase();
      veiculosFiltrados = veiculosFiltrados.filter(veiculo =>
        veiculo.modelo.toLowerCase().includes(termoLower) ||
        veiculo.marca.toLowerCase().includes(termoLower)
      );
    }

    if (marcasFiltro.length > 0) {
      veiculosFiltrados = veiculosFiltrados.filter(veiculo =>
        marcasFiltro.includes(veiculo.marca)
      );
    }

    setVeiculosFiltrados(veiculosFiltrados);
  }, [veiculos, termoPesquisa, marcasFiltro]);

  const carregarVeiculos = async () => {
    try {
      const lista = await VeiculoService.listar();
      setVeiculos(lista);
      setVeiculosFiltrados(lista);
    } catch (error) {
      setAlerta({
        mensagem: error instanceof Error ? error.message : 'Erro ao carregar veículos',
        tipo: 'erro'
      });
    }
  };

  const handleSearchChange = (termo: string) => {
    setTermoPesquisa(termo);
  };

  const handleFilterChange = (marcas: string[]) => {
    setMarcasFiltro(marcas);
  };

  const handleCadastrarClick = () => {
    setEditingVeiculo(null);
    setShowForm(true);
  };

  const handleSairClick = () => {
    console.log('Sair');
  };

  const handleFormSubmit = async (veiculo: Veiculo) => {
    try {
      if (editingVeiculo && editingVeiculo.id) {
        await VeiculoService.atualizar(editingVeiculo.id, veiculo);
      } else {
        await VeiculoService.criar(veiculo);
      }
      setAlerta({
        mensagem: `Veículo ${editingVeiculo ? 'atualizado' : 'cadastrado'} com sucesso!`,
        tipo: 'sucesso'
      });
      setShowForm(false);
      carregarVeiculos();
    } catch (error) {
      setAlerta({
        mensagem: error instanceof Error ? error.message : `Erro ao ${editingVeiculo ? 'atualizar' : 'cadastrar'} veículo`,
        tipo: 'erro'
      });
    }
  };

  const handleEdit = (veiculo: Veiculo) => {
    setEditingVeiculo(veiculo);
    setShowForm(true);
  };

  const handleDelete = async (id: number) => {
    try {
      await VeiculoService.excluir(id);
      setAlerta({
        mensagem: 'Veículo removido com sucesso!',
        tipo: 'sucesso'
      });
      carregarVeiculos();
    } catch (error) {
      setAlerta({
        mensagem: error instanceof Error ? error.message : 'Erro ao remover veículo',
        tipo: 'erro'
      });
    }
  };

  return (
    <div className="app-container">
      <Sidebar 
        onCadastrarClick={handleCadastrarClick} 
        onSairClick={handleSairClick} 
      />
      <main className="main-content">
        {alerta && (
          <Alerta 
            mensagem={alerta.mensagem} 
            tipo={alerta.tipo} 
            onClose={() => setAlerta(null)} 
          />
        )}
        
        <Pesquisar 
          veiculos={veiculos}
          onSearchChange={handleSearchChange}
          onFilterChange={handleFilterChange}
        />
        
        <VeiculoList 
          veiculos={veiculosFiltrados} 
          onEdit={handleEdit} 
          onDelete={handleDelete} 
        />
        
        {showForm && (
          <VeiculoForm 
            veiculo={editingVeiculo || undefined}
            onSubmit={handleFormSubmit}
            onCancel={() => setShowForm(false)}
            isOpen={showForm}
          />
        )}
      </main>
    </div>
  );
}

export default App;