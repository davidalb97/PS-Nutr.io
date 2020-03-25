# Reuniões - Orientador PS

## Reunião #1

### Tópicos a discutir

- Discutir a parte do servidor - usar react native ou ktor? Perguntar qual é a mais adequada para o caso de se querer UI diferentes para web e mobile;
- Falar da adenda da descrição geral do projeto, previamente enviada na sugestão de projeto ao eng. Fernando Sousa;
- Perguntar o que se deve realçar mais para a proposta de projeto a entregar dia 16 de março (?)

### Notas da reunião

- 



## Tecnologias

### Android

* Kotlin
* API gráfica para o gráfico
* ~~Base de dados relacional para metadados offline: Room~~
* Auth para autenticação (google, facebook)

### Server (Same instance)

* Deploy: 
  * Heroku (Postgres/Redis/Kafka) 
  * ~~Google cloud - Cloud SQL (MySQL, PostgreSQL, and SQL Server) ou Cloud firebase~~

#### 	Backend (nutr.io-api)

* Kotlin (~~ktor~~, spring) ou ~~javascript (express nodejs)~~
* Base de dados relacional: Postgres
* APIS:
  * Food API (Fornecer o valor genérico de cada prato)
  * APIs que fornecem o relacionamento entre o restaurante e o prato:
    * Zomato

#### 	FrontEnd (nutr.io-web-api)

- Single page application (Para pormos enviar react packed so pra web)

* js (React, ~~angular, vue?~~)
* API gráfica para o gráfico
* Auth para autenticação (google, facebook)

## Lacunas

* Obter informações nutricionais fidedignos de um certo prato servido num estabelecimento de restauração

## Solução

* Uso da comunidade para inferir os valores certos

* Uso de APIs legit para a obtenção de valores default

* Rapidez na obtenção dos resultados utilizando a APIs para a obtenção do prato num dado estabelecimento de restauração

* Recompensa dos contribuidores utilizando

  

  

  ------

  Que informações o utilizador recebe, quem insere, como insere, propor restaurante q nao existe, eiste votações/n existe? etc.

* Input utilizadores tem que estar descrito

------

* O uso de Google Vision API para reconhecimento de pratos não é viavel devido a labels vagas

## Proposta

A Nutr.io consiste numa aplicação móvel e web que irá fornecer dados nutricionais rigorosos sobre
refeições e pratos servidos nos estabelicimentos de restauração, com o principal objetivo de calibrar
os dados da aplicação consoante o input dos utilizadores de modo a auxiliá-los nas suas escolhas
nutricionais.

--------------------------------------------
A Nutr.io consiste numa aplicação móvel e web que irá fornecer dados nutricionais rigorosos sobre 
refeições servidas nos estabelicimentos de restauração. Dados estes que são obtidos através de API's externas 
e calibrados ao longo do tempo através do input dos utilizadores, 
para que o valor nutricional denote uma distribuição normal no prato servido.

---

Esta aplicação tem como principal objetivo calibrar os dados presentes na mesma, 
conciliando APIs externas com os inputs dos utilizadores, 
de modo a auxiliá-los nas suas escolhas nutricionais e contribuindo para o ecossistema
informático da app.