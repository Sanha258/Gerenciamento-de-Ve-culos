import React, { useEffect, useState } from 'react';
import './Alerta.css';

interface AlertaProps {
  mensagem: string;
  tipo?: 'sucesso' | 'erro';
  onClose?: () => void;
  autoClose?: boolean;
  autoCloseTime?: number;
}

const Alerta: React.FC<AlertaProps> = ({
  mensagem,
  tipo = 'sucesso',
  onClose,
  autoClose = true,
  autoCloseTime = 5000,
}) => {
  const [isVisible, setIsVisible] = useState(true);

  useEffect(() => {
    if (autoClose) {
      const timer = setTimeout(() => {
        handleClose();
      }, autoCloseTime);

      return () => clearTimeout(timer);
    }
  }, [autoClose, autoCloseTime]);

  const handleClose = () => {
    setIsVisible(false);
    setTimeout(() => {
      if (onClose) onClose();
    }, 300);
  };

  if (!isVisible) return null;

  return (
    <div className="alerta-container">
      <div className={`alerta ${tipo} ${!isVisible ? 'fade-out' : ''}`}>
        <span>{mensagem}</span>
        <button
          className="alerta-fechar"
          onClick={handleClose}
          aria-label="Fechar alerta"
        >
          &times;
        </button>
      </div>
    </div>
  );
};

export default Alerta;