# Agregador de Investimentos

Este é um projeto simples criado para praticar conceitos de desenvolvimento com Java e Spring Boot. Ele simula um sistema de cadastro e consulta de usuários, como se fosse uma parte de um aplicativo de investimentos. 

## O que o projeto faz

- Permite cadastrar novos usuários
- Permite consultar os dados dos usuários via API
- Organiza o código em partes claras: controlador, serviço, repositório e entidade
- Pode ser executado tanto localmente quanto via Docker

## Tecnologias utilizadas

- Java 17
- Spring Boot
- Maven 
- Docker

## Como rodar

### Requisitos

- Ter o Java 17 instalado
- Ter o Maven instalado
- Docker (opcional)

### Rodando localmente (sem Docker)
```bash
# Clone o repositório
git clone <url-do-repositorio>
cd AgregadorInvestimentos-master

# Roda o projeto
./mvnw spring-boot:run
```

### Rodando com Docker
```bash
# Rode o projeto com Docker
docker-compose up
```

Depois disso, você pode acessar a aplicação em: `http://localhost:8080`

