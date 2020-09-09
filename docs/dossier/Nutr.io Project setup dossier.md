# Nutr.io Project setup dossier

To execute this project, following conditions and instructions must be fulfilled. For troubleshooting or further information please contact one of the developers:

- **Pedro Pires:** A42206@alunos.isel.pt
- **David Albuquerque:** A43566@alunos.isel.pt
- **Miguel Luís:** A43504@alunos.isel.pt

## Software required

The following software is required on your machine in order for the project to be executed:

- **Git** - Required in order to obtain project's code via [Github](github.com).
- **PostgreSQL 11** - Dependency used by project's HTTP-based server.
  - Optionally, **psql** command via command line, which allows an easier database setup. 
- **PostGIS** - A **PostgreSQL 11** add-on that enables Geolocation-based queries, which can be found [here](https://postgis.net/install/).
- **Java JDK 11.06** (LTS) - Required in order to execute generated project's *jar*.
- **Gradle** - Required in order to build and generate project's server *jar*.
- **Android Studio** - Required in order to execute the android application.

## Setup

Once previously required software is installed, all steps must be followed in given order to allow project's build and execution for both the project's HTTP-based server** and the **android application**:

### Retrieve project's code using Git

   Retrieve project's code, either using a browser and accessing [the project's Github Repository](https://github.com/davidalb97/PS-Nutr.io); or via the command line, using the following git command:

   ```bash
   git clone https://github.com/davidalb97/PS-Nutr.io.git
   ```
### Database and HTTP server

**Setup database dependency**

After **PostgreSQL 11** has been downloaded, **a database that will contain all of the system's information needs to be created.** Make sure to remember the created database name, database creator name and password, as they will be needed.

Once the database has been created, declare the following environment variables on your system:

```
PS_POSTGRES_DB={created database name}
PS_POSTGRES_USER={database creator name}
PS_POSTGRES_PASSWORD={database creator password}
PS_SERVER_SECRET={server secret password}
PS_INSULIN_PROFILES_SECRET={secret password for insulin profiles' encryption}
```

Without these variables, the project's HTTP-based server will be unable to connect to the database and thus fail.

Afterwards, enable **PostGIS** on the newly created database using the following *psql* command:

```plsql
CREATE EXTENSION postgis;
```

Finally, all provided database setup scripts must be executed. To do so, first navigate to the `\PS-Nutr.io\database\v2` path and either: 

- execute the shell script `runScripts.sh` *(this requires the **psql** command and a way to execute shell scripts)*;
- run all `.sql` scripts in the numeric order they are given *(e.g. `1_Nutrio_Create_Model.sql`, then `2_insertCuisines.sql`, and so on)* in the newly created database.

**Build, generate and run http-based server executable**

To run the HTTP-based server, an executable `PS - httpserver-0.0.1-SNAPSHOT.jar` must first be created using `gradle`. 

To do so, navigate to the `\PS-Nutr.io\httpserver` path and execute the following `gradle` command:

```bash
gradle bootjar
```

Afterwards, find the generated `.jar` in the `\PS-Nutr.io\httpserver\build\libs` folder and execute it to run the HTTP-based server with the following *java* command:

```bash
java -jar "PS - httpserver-0.0.1-SNAPSHOT.jar"
```

The HTTP-based server will now be running on port **8080** and can be accessed via a valid endpoint, such as `GET http://localhost:8080/ingredients`. 

For more information about valid endpoints, please check the annexed project's report.

**Unit tests**

To run the unit tests, the following environment variable must be created:

```
PS_POSTGRES_DB_TEST={test database creator name}
```

After creating the variable, run the script `runScripts-testDatabase.sh` to fill the test database.

### Mobile application

To run the mobile application, an emulator with the proper API level is needed. Although the application was developed in **Android 10 (API level 29)**, any version below it, down to **Android 7 (API level 24)** can be used when creating a virtual device *(via Android Studio Manager)*.

Afterwards, select created virtual device and press the start button on Android Studio's top bar to launch the emulator, build the project, install all the required dependencies and execute the Android application.

### Web client

To run the web client locally, the **Node.Js** framework must be installed first.

After the installation, go to the `webclient` inside the project's source, open a terminal and run `npm install` to install all the required dependencies.

To start the client just type `npm dev`.

## Demo

The server was later deployed to **Heroku** along with the web client for demonstration proposes.

[**Nutr.io-app**](https://nutrio-app.herokuapp.com/) 

To test the web client you can use the following credentials:

- **Email**: TestModerator@email.com

- **Password**: Nutrisel