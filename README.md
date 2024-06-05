# api_banking_account
Uma API para operações simples entre contas bancárias usando banco de dados MySQL
Abaixo estão marcadas os requisitos completados no projeto até a presente data.

Requisitos Funcionais:

- [x] 1. Cadastro de Contas:
    - [x] Crie uma entidade `Conta` com campos como número da conta, nome do titular e saldo.
    - [x] Implemente um endpoint para criar uma conta.

- [x] 2. Transferência:

    - [x] Implemente um endpoint que permita a transferência de dinheiro entre duas contas.
    - [x] Considere validar se a conta de origem possui saldo suficiente.

- [x] 3. Depósito:

    - [x] Implemente um endpoint para realizar depósitos em uma conta.

- [x] 4. Consulta de saldo e extrato:

    - [x] Crie uma entidade `Extrato`.
    - [x] Crie um endpoint que permita consultar o saldo e extrato de uma conta.

Requisitos Técnicos:

- [x] 1. Spring Boot:

    - Utilize o Spring Boot como base para o desenvolvimento do projeto.

- [ ] 2. Segurança de Endpoints:

    - Implemente autenticação e autorização nos endpoints, garantindo que apenas usuários autenticados possam realizar operações bancárias.

- [x] 3. Bean Validation:

    - Faça uso extensivo das anotações de validação do Bean Validation para garantir a integridade dos dados.

- [x] 4. Clean Code:

    - Siga boas práticas de programação e adote um estilo de código limpo e legível.

- [x] 5. Persistência em Banco de Dados:

    - Utilize um banco de dados relacional para armazenar as informações das contas.

- [ ] 6. Testes unitários:

   - O projeto precisa ter testes unitários.

- [x] 7. Documentação:

   - [x] Crie um Readme para a aplicação.
   - [x] Crie uma collection com as chamadas da API.