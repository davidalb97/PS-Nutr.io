# Proposta

## Titulo:

## Background

Em Portugal existem X pessoas com diabetes, Y no mundo e uma das principais necessidades no
seu dia a dia é a contagem de Hidratos de Carbono, de forma a manusear as suas doses de insulina.

Apesar de existirem diversos recursos (livros, apps, etc.) que mostram a informação nutricional, 
estes requerem que o utilizador faça uma medição precisa dos alimentos ingeridos - uma atividade impossivel 
no contexto de comer em restaurantes.

Esta dificuldade gerou a ideia de desenvolver uma aplicação que facilita esta tarefa, fornecendo ao utilizador
valores calibrados por outros utilizadores.

## Objectives

Given the gaps previously stated, Nutr.io aims to develop a system that offers the users carbohydrates values of meals served in restaurants. The information is collected from external APIs and gradually calibrated  by using other sources, which is stored in the system's database.

This project aims to achieve this by fulfilling the following goals by the end of development:

* Use external APIs to obtain data from meals and restaurants;
* Maintain a database that contains the baseline nutritional values and their calibration provided by other sources;
* Obtain baseline nutritional information about meals of any given restaurant. (Even when restaurants don't supply their menus to any API);
* A contribution system that uses information from various sources to calibrate the system's data such as:    
  - System's community (mandatory objective)    
  - Nutritionists (optional)    
  - Restaurant owners (optional)
* Implement registration and authentication support, so that authenticated users can contribute to the community.

## Justification

