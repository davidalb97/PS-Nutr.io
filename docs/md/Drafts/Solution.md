# Solutions

* Usar store_id de uber eats para obter menus de uma store

* Por cozinha:
  * Dar a escolher a cozinha e depois mostrar:
    * Pratos por ordem de popularidade
      * Ingredientes do prato, caso não exista o prato
  
* Por daily meals da Zomato:
  * Assumir que daily meals estão sempre presentes e representam os menus na sua totalidade
  * Usar as fotos da zomato e relacionar com uma AI de reconhecimento para inserir restaurante e respectivos menus 
  
* Datamine UberEats website to API requests

* Fornecer a nossa própria API para inserção de restaurantes e menus

  | Method | Path                        | Query Params | Body Params                              | Description                                        |
  | ------ | --------------------------- | ------------ | ---------------------------------------- | -------------------------------------------------- |
  | GET    | rest/:rest_id/menus         |              |                                          | Gets menus array                                   |
  | POST   | rest/:rest_id/menu          |              | client_id, Menu                          | Inserts a new restaurant menu, returns the menu_id |
  | PUT    | rest/:rest_id/menu/:menu_id |              | client_id, Menu                          | Updates a restaurant menu                          |
  | POST   | rest                        |              | client_key, name, site,  locations array | The response contains rest_id  array on the body   |
  | PUT    | rest                        |              | client_id, name, site,  locations array  | Updates restaurant  locations,  name,  etc         |
  | GET    | rest                        |              |                                          | Gets local restaurants                             |