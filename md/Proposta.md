# Proposta

## Titulo:



Keywords: Diabetes; Nutrition; Contribution

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

#### Lower Priority Goals

Due to the diversity of features that must be implemented, ~~there are also some lower priority objectives, which completion might depend on the state of the project, such as:~~ the following objectives were also considered but marked with a lower priority, making their completion depend on the state of the project:

* Allow users to vote whether a new dish submission is accurate;

* Allow users to vote whether a new restaurant submission is valid, if our APIs do not supply it, warning future users about non-valid locations;

* Reward points to users according to submission votes;

* Establish minimum required points for each submission type;

* Custom dishes and meals with CRUD implementation;

* Display restaurant meals in the following order:

  * Voted dishes (ordered by vote count);
  * Custom dishes that belong the restaurant's cuisine;
  * Generic dishes that belong restaurant's cuisine.

* Nutritionists authentication for special contributions;

* Sync local user data with our database;

* Access and display of synced user data on both platforms.

* UI Android application configuration.

  

## Analysis and approach

This project will be deployed using **Heroku** and coded in four system components - an HTTP-based API, a relational-based database, a mobile application and a browser-based application; which can be analyzed in **[image]**.

The HTTP-based API will be implemented in Kotlin using the Spring framework to provide endpoints that allow the users to read and write data. Said data can be the following:

**[REFER WHAT THE API OFFERS]**

- Restaurants' cuisines, which are obtained from external APIs (such as **Google Places**[[r+1]](https://cloud.google.com/maps-platform/places), **Zomato**[[r+2]](https://www.zomato.com) and **Yelp**[[r+3]](https://www.yelp.pt)) and are read by the users; 
- Restaurants' meals, which are obtained from the external APIs mentioned above and the system's database, seeing as the community can write their own meals;  
- Meals' nutritional values, where their baseline values are obtained from nutritional APIs (such as **Nutritionix**[[r+4]](https://www.nutritionix.com/)) and additional calibration done by the community is saved to the system's database.

Taking into consideration the complexity of the previously mentioned data and how they relate to each other, a relational-based database approach was chosen over a non-relational one. 

The mobile application will be implemented using Kotlin **(AHHH finish me)**

Finally, the browser-based application will be implemented using the React Framework in a **single-page application** approach since we aim to offer a rich user interface with many features and  its fast and responsive properties. [1 & 2]

https://rubygarage.org/blog/single-page-app-vs-multi-page-app

https://docs.microsoft.com/en-us/dotnet/architecture/modern-web-apps-azure/choose-between-traditional-web-and-single-page-apps



## Risks

The main risk to the project is the heavy dependency in obtaining restaurants' meals. If no external API is capable of providing accurate and reliable data *(such as **Zomato**, which despite offering an endpoint for restaurants' daily menus [1], not enough owners utilize this feature [1, 2, 3])*, alternate approaches must be taken - such as assigning an immutable list of meals to cuisines, serving as a default for restaurants that do not provide their meals.

The other significant risk is that the project relies on skills that the group has yet to acquire and that are being taught in ISEL's courses (namely **DAW**). If said courses suffer a delay in providing their knowledge, then the scope of the implementation will be delayed or restricted.

Lastly, due to the recent outbreak of *COVID-19* and the resultant epidemic situation, the group expresses concerns about any possible setbacks that the virus can cause. These setbacks could include canceled meetings with our tutor, canceled classes (reinforcing the previously mentioned risk) and delays in our project plan due to a member being infected.

 