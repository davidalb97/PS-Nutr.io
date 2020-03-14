# Proposta

## Titulo:

## Background

The idea that every field of study can be digitalized in order to ease monotonous tasks is continuously growing in the modern world. Our project aims to tackle the field of Type 1 diabetes, given its growing prevalence in Portugal [1] and the world.

One of those monotonous tasks is the count and measurement of carbohydrates in meals used to administer the correspondent amount of insulin, along with their blood levels, to maintain a healthy lifestyle. A task that heavily relies on having access to food databases[link] and knowledge of how much portions the meal has - usually by using a digital balance or doing estimations.

Eating in restaurants is the perfect example that showcases a gap in this field, that our project, 
Nutr.io, aims to fill.  Many nutritional applications do not provide data for restaurants' meals [3] nor does the user bring his digital balance from home - resulting in a faulty carbohydrate count and therefore administering an incorrect insulin value.

The main goal of this project is to design a system that offers the Type 1 diabetic community a way to facilitate difficult carbohydrate measurement situations, like restaurants, by providing a database of nutritional information for restaurants; and by calibrating the accuracy of given data based on the community's contribution.

## Goals

Given the gaps previously stated, Nutr.io aims to develop a system that offers to the users the carbohydrates values of meals served in restaurants. The information is collected from external APIs and gradually calibrated by using other sources, such as user's feedback. All data gathered from these sources is stored in an internal systems database.

This project aims to achieve this by fulfilling the following goals by the end of development:

* Use external APIs to obtain data from meals and restaurants;
* Maintain a database that contains the baseline nutritional values and their calibration provided by other sources;
* Obtain baseline nutritional information about meals of any given restaurant. (Even when restaurants don't supply their menus to any API);
* A contribution system that uses information from various sources to calibrate the system's data such as:    
  - System's community (mandatory objective)    
  - Nutritionists (optional)    
  - Restaurant owners (optional)
* Implement registration and authentication support, so that authenticated users can contribute to the community.

## Analysis and approach

This project will be deployed using **Heroku** and coded in four system components - an HTTP-based API, a relational-based database, a mobile application and a browser-based application; which can be analyzed in **[image]**.

The HTTP-based API will be a component written in Kotlin that uses the Spring framework to provide endpoints that allow the users to read and write data. Said data can be the following:

**[REFER WHAT THE API OFFERS]**

- Restaurants' cuisines, which are obtained from external APIs (such as **Google Places** , **Zomato** and **Yelp**) and are read by the users;
- Restaurants' meals, which are obtained from the external APIs mentioned above and the system's database, seeing as the community can write their own meals; 
- Meals' nutritional values, where their baseline values are obtained from nutritional APIs (such as **Nutritionix**) and additional calibration done by the community is saved to the system's database.

**Taking into consideration the complexity of the previously mentioned data and how they relate to each other, a relational-based database approach was chosen over a non-relational one.** 

The mobile application will be coded (...)

Finally, the browser-based application will be coded using a **single-page application** approach due (...)  







https://docs.microsoft.com/en-us/dotnet/architecture/modern-web-apps-azure/choose-between-traditional-web-and-single-page-apps



Out of all deployment tools [link], we chose **Heroku** and as such our database will be coded and maintained using **Postgres**. We chose a relational model over a non-relational due to (...)





*We chose a single-page application approach over multi-page application as we will access our API endpoints on both our web and mobile implementations. This means that we will only require the **Nutr.io's Web API** for the initial web setup and then use the **Nutr.io's API** for the following json requests.*



We chose a single-page application approach over multi-page application as (...).

For native application, we chose it over (...) because (...).

With all these approaches in considered, our system would be structured like this:



