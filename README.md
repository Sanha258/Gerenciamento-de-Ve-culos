Caso de Uso: Gerenciamento de Veículos
Objetivo
Permitir o cadastro, consulta, atualização e exclusão de veículos no sistema de locação.

Atores
Administrador do sistema
API REST
Fluxo Principal
Cadastrar Veículo

O usuário informa marca, modelo, ano, placa e cor.
O sistema valida os dados e verifica se a placa já existe.
Se válido, o veículo é salvo e retornado.
Listar Veículos

O usuário solicita a lista de todos os veículos.
O sistema retorna todos os veículos cadastrados.
Buscar Veículo por ID

O usuário informa o ID do veículo.
O sistema retorna os dados do veículo ou erro se não existir.
Atualizar Veículo

O usuário informa o ID e os novos dados.
O sistema valida e atualiza o veículo, retornando o atualizado.
Excluir Veículo

O usuário informa o ID do veículo.
O sistema exclui o veículo se existir.
Regras de Negócio
Não pode haver duas placas iguais.
Todos os campos são obrigatórios.
O ano deve estar entre 1900 e o ano atual.
Exceções
Placa já cadastrada.
Veículo não encontrado.
Dados inválidos.

Este projeto é composto por três principais tecnologias:

- **Front-end:** Desenvolvido em **ReactJS**, proporcionando uma interface moderna, responsiva e fácil de usar para o usuário final.
- **Back-end:** Implementado com **Spring Boot**, garantindo robustez, segurança e escalabilidade na gestão dos dados e regras de negócio.
- **Banco de Dados:** Utiliza **MySQL** para armazenamento eficiente e seguro das informações dos veículos.
