Sistema de Gerenciamento de Veículos

Funcionalidades principais:
Visualizar lista de veículos: Exibe veículos em tabela com colunas: ID, Marca, Modelo, Ano, Placa e Ações.
Adicionar veículo: Usuário preenche formulário (Marca, Modelo, Ano, Placa). Sistema valida campos obrigatórios, formato da placa e duplicidade. Exibe mensagem de sucesso.

Visualizar detalhes: Tooltip mostra conteúdo completo de textos truncados.
Editar veículo: Permite alterar dados existentes com validações similares à adição.
Remover veículo: Exclusão mediante confirmação em modal; exibe mensagem de sucesso.

Fluxos alternativos:
Cancelar adição ou exclusão fecha o modal sem alterar dados.
Formulário com dados inválidos destaca erros para correção.
Placa inválida ou duplicada exibe mensagens específicas para correção.

Regras de negócio:
Campos obrigatórios: Marca, Modelo, Ano e Placa.
Placa deve seguir o padrão brasileiro (7 caracteres, formato antigo ou Mercosul).
Placa deve ser única no sistema.
Exclusões sempre confirmadas pelo usuário.

----------------------------------------------------------------------------------------------------------------------
Obs: USEI VSCODE PARA FRONT E BACK
----------------------------------------------------------------------------------------------------------------------

Configuração do Banco de Dados

Pré-requisito:
MySQL Workbench instalado

Como executar/configurar o banco (passo a passo):
Abrir o MySQL Workbench.
Criar um novo Schema com o nome: dbveiculo
Observação: Este nome já está configurado no arquivo application.properties do projeto back-end.

# em caso de uso de outro banco, vai ter que baixar as dependencias daquele banco e configurar arquivo application.properties.
-------------------------------------------------------------------------------------------------------------
Back-end do Projeto

Tecnologias utilizadas:
Java 21+
Spring Boot

Pré-requisitos:
Java versão 21 ou superior

Como executar o projeto (passo a passo):
Baixar o projeto no computador.
Configurar conexão com o banco:
Abrir a pasta resources no projeto.
Abrir o arquivo application.properties.
Alterar username e password para os dados do seu MySQL.
Para Executar a aplicação:
Abrir o arquivo VeiculoApplication.java.
Clicar em Run para subir o projeto e criar a tabela no banco (automatica).

Executar testes:
Abrir o terminal na pasta do projeto (usa bash, powershel versão 7 não funcionou).
Rodar os testes com o comando: ./mvnw test (roda todos os testes).
------------------------------------------------------------------------------------------------------------------
Front-end do Projeto

Tecnologias utilizadas:
Vite: Ferramenta de build e desenvolvimento.
React 18: Biblioteca para construção da interface de usuário.
TypeScript: Superset do JavaScript com tipagem estática.
CSS3: Estilização da aplicação.

Pré-requisitos:
Node.js (versão 16 ou superior)
npm (gerenciador de pacotes do Node.js)
Como executar o projeto (passo a passo):
Baixar o projeto no seu computador.
Instalar dependências :
Abrir o terminal na pasta do projeto, usa comando: npm install
Depois para executar em modo desenvolvimento usa comando: npm run dev
Abrir o navegador e ir para: http://localhost:5173 (obs: Observação: O Vite normalmente usa a porta 5173 por padrão)
-----------------------------------------------------------------------------------------------------------------------
