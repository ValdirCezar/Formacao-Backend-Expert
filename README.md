# Formação Backend Expert

<img width="908" alt="image" src="https://github.com/ValdirCezar/Formacao-Backend-Expert/assets/58149445/47fbd128-e28a-4dc0-8cd6-3d34dac2189b">

## Visão Geral

O projeto "Formação Backend Expert" tem como objetivo fornecer uma formação completa para desenvolvedores backend, focando em uma arquitetura de microsserviços utilizando tecnologias modernas e práticas avançadas de desenvolvimento. Este repositório contém o código-fonte e os componentes desenvolvidos durante o curso.

## Tecnologias Utilizadas

- **Spring Boot**: Framework Java para criação de aplicações web e microsserviços.
- **Spring Cloud**: Conjunto de ferramentas para construir sistemas distribuídos.
- **Spring Security**: Framework de autenticação e autorização.
- **JWT (JSON Web Tokens)**: Para autenticação e autorização segura.
- **RabbitMQ**: Sistema de mensageria para comunicação assíncrona.
- **MongoDB Atlas**: Serviço de banco de dados NoSQL gerenciado.
- **Amazon RDS**: Serviço de banco de dados relacional gerenciado.
- **Docker**: Plataforma para criação e gerenciamento de contêineres.
- **GitHub**: Plataforma de hospedagem de código e controle de versão.

## Estrutura do Projeto

### Componentes Principais

1. **API Gateway**: Gerencia todas as requisições para os microsserviços.
2. **Config Server**: Armazena e distribui configurações centralizadas.
3. **Service Discovery**: Localiza e registra instâncias de serviço dinamicamente.
4. **Auth Service API**: Gerencia tokens de acesso e autenticação de usuários.
5. **Commons Lib**: Biblioteca compartilhada entre os microsserviços.
6. **Email Service**: Envia e-mails utilizando RabbitMQ para gerenciamento de filas.
7. **User Service API**: Gerencia informações de usuários.
8. **Order Service API**: Gerencia ordens de serviço.

## Funcionalidades

- **Autenticação e Autorização**: Utilizando Spring Security e JWT.
- **Balanceamento de Carga**: Distribuição automática de solicitações entre serviços.
- **Configurações Centralizadas**: Atualização dinâmica de configurações em tempo de execução.
- **Mensageria**: Comunicação assíncrona com RabbitMQ.
- **Integração Contínua**: Utilizando GitHub e Docker para versionamento e implantação.

## Instalação

### Pré-requisitos

- [Java 17 ou superior](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Docker](https://www.docker.com/get-started)
- [Maven](https://maven.apache.org/install.html)
- [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)



## Contribuição

1. Fork o projeto.
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`).
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`).
4. Push para a branch (`git push origin feature/nova-feature`).
5. Abra um Pull Request.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).

## Contato

Valdir Cezar - [valdircezar312@gmail.com](mailto:valdircezar312@gmail.com)




