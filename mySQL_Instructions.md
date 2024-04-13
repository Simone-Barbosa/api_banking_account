# Exemplo de configuração do arquivo application.properties de conexão com o banco de dados

server.port=8080

spring.datasource.url=jdbc:mysql://localhost:3306/bank_account
spring.datasource.username=root
spring.datasource.password=***** (sua senha)
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.show-sql=true


# Crie o Schema chamado bank_account

CREATE SCHEMA `bank_account` ;

# Instruções para criação das tabelas no banco de dados mySQL

### Criar tabela account

CREATE TABLE `bank_account`.`account` (
`id_account` INT NOT NULL AUTO_INCREMENT,
`number_account` VARCHAR(45) NOT NULL,
`owner_account` VARCHAR(100) NOT NULL,
`balance_account` DECIMAL(32,2) NULL,
PRIMARY KEY (`id_account`),
UNIQUE INDEX `number_account_UNIQUE` (`number_account` ASC) VISIBLE);

### Adicionar valores na tabela account

INSERT INTO `bank_account`.`account` (`id_account`, `number_account`, `owner_account`, `balance_account`) VALUES ('1', '1234-5', 'Simone', '210.50');
INSERT INTO `bank_account`.`account` (`id_account`, `number_account`, `owner_account`, `balance_account`) VALUES ( 2, '6789-1', 'Rodrigo', '214.56');
INSERT INTO `bank_account`.`account` (`id_account`, `number_account`, `owner_account`, `balance_account`) VALUES ( 3, '3456-9', 'Eliane', '360.27');

### Criar tabela extract_transactions

CREATE TABLE `bank_account`.`extract_transactions` (
`id_transaction` INT NOT NULL AUTO_INCREMENT,
`account_main` VARCHAR(45) NOT NULL,
`type_operation` VARCHAR(45) NOT NULL,
`value_transaction` DECIMAL(32,2) NOT NULL,
`related_account` VARCHAR(45) NOT NULL,
`date_transaction` DATETIME NOT NULL,
`balance_account_main` DECIMAL(32,2) NULL,
PRIMARY KEY (`id_transaction`),
INDEX `idx_account_main` (`account_main`),
INDEX `idx_date_transaction` (`date_transaction`)
);
