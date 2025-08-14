# Executando o Sistema

Este guia descreve como executar a aplicação e seus serviços utilizando Docker e Docker Compose, ou executando cada projeto individualmente.

## Links Principais de Acesso

Após inicializar o sistema, você pode acessar os seguintes serviços:

### **Links Essenciais:**
- **API da Aplicação:** [http://localhost:8080](http://localhost:8080)
- **Documentação (Swagger):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Frontend da Aplicação:** [http://localhost:5173](http://localhost:5173)
- **pgAdmin (Gerenciador do Banco):** [http://localhost:5050](http://localhost:5050)

---

## Execução com Docker (Recomendado)

### Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

### 1. Iniciar os Contêineres

Abra um terminal na raiz do projeto e execute o seguinte comando para construir a imagem da aplicação e iniciar todos os serviços em segundo plano:
```bash
docker-compose up
```

### 2. Acessar os Serviços

Após a inicialização, os seguintes serviços estarão disponíveis:

- **API da Aplicação:** [http://localhost:8080](http://localhost:8080)
- **Documentação (Swagger):** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **pgAdmin (Gerenciador do Banco):** [http://localhost:5050](http://localhost:5050)
    - **E-mail:** `admin@admin.com`
    - **Senha:** `admin`
      
- **O frontend estará disponível em:** [http://localhost:5173](http://localhost:5173)

### 3. Parar os Contêineres

Para parar todos os serviços, execute o comando abaixo no mesmo terminal:

```bash
docker-compose down
```
### 4. Configuração Manual do pgAdmin (Opcional)

Se desejar usar o pgAdmin localmente:

1. Baixe e instale o [pgAdmin](https://www.pgadmin.org/download/)
2. Configure uma nova conexão com os dados do seu banco PostgreSQL local
3. Use as credenciais do banco que você criou anteriormente

---

##  Banco de Dados e Liquibase

Este projeto utiliza o **PostgreSQL** como sistema de gerenciamento de banco de dados.

Para garantir que o esquema do banco de dados esteja sempre consistente e atualizado com a versão da aplicação, utilizamos o **Liquibase**.

### O que é o Liquibase?

O Liquibase é uma ferramenta de código aberto para controle de versão de banco de dados. Ele permite que você gerencie e aplique alterações no esquema do seu banco de dados (como criar tabelas, adicionar colunas, etc.) de forma programática e controlada.

### Como funciona neste projeto?

1. **Changelogs:** As alterações do banco de dados são definidas em arquivos de `changelog` (geralmente em formato XML, YAML ou SQL), localizados no diretório de recursos da aplicação.
2. **Inicialização:** Quando a aplicação é iniciada, o Liquibase verifica o estado atual do banco de dados.
3. **Aplicação das Mudanças:** Ele compara o estado do banco com os `changelogs` e aplica automaticamente quaisquer alterações pendentes.

Isso automatiza o processo de migração do banco de dados, eliminando a necessidade de executar scripts SQL manualmente e garantindo que o ambiente de desenvolvimento, teste e produção estejam sempre sincronizados.

---

##  Solução de Problemas

### Problemas Comuns

- **Porta já em uso:** Verifique se as portas 8080, 5173 e 5432 não estão sendo usadas por outros processos
- **Erro de conexão com banco:** Verifique se o PostgreSQL está rodando e as credenciais estão corretas
- **Dependências do npm:** Execute `npm install` novamente se houver problemas com as dependências do frontend
