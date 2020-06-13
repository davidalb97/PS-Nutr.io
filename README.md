# PS-Nutr.io

## University

- **ISEL - Instituto Superior de Engenharia de Lisboa**, Lisbon, Portugal 

## Group Constitution

- **Pedro Pires**, Number 42206
  - University email: A42206@alunos.isel.pt
  - GitHub page: https://github.com/Pedrokase
- **Miguel Luís**, Number 43504
  - University email: A43504@alunos.isel.pt
  - GitHub page: https://github.com/mig07
- **David Albuquerque**, Number 43566
  - University email: A43566@alunos.isel.pt
  - GitHub page: https://github.com/davidalb97

## Course URLs

- Moodle Link: https://1920moodle.isel.pt/course/view.php?id=4726

## Introduction

This project is our bachelor's final project and you are viewing our GitHub's remote repository.

The main goal of this project is to design a system that offers a way to facilitate difficult carbohydrate measurement situations, like in restaurants. To that end, a system that stores meals’ nutritional information will be developed, where users can use and calibrate its data with their feedback.

## Environment Setup & Work Practices

This section shows and explains how you should setup your work environment, so you can start the development as soon as possible.

### Software required:

- **Git**
- **Java JDK 11.06** (LTS)
- **JetBrains Intellij** IDE 2019.4.3 or any other Java/Kotlin IDE with gradle integradtion
- **PostgreSQL 11** - For database
- **Android Studio** - For android 

### Recommended software:
- **Visual Studio Code**, with the following extensions:
  - Microsoft Live Share - Editing the same files and projects alongside your peers
  - LaTeX Workshop - Producing LaTeX documents with Studio Code
- **Postman** - Endpoints' tests
- **Inkscape** - Vector image editing software
- **MiKTeX** - LaTeX for Windows (documentation and reports production)
- **Typora** - For .md documentation (Being used right now :P)
- **Dia** - Relational database diagrams
- **JustInMind** - For Android / Web UI mocks
- (...)

### Local repository setup:

- Cloning:

```bash
git clone https://github.com/davidalb97/PS-Nutr.io.git
```

- Hard reset local repository:

```bash
git reset --hard origin
```

- (...)

### Notes about software installation and development:

- Installs:
  - To use LaTeX workshop, you will need to install MiKTeX first and make sure that the path ```C:\Program Files\Git\usr\bin``` is added to the system variables (PATH), inside the environment variables, so it can use the perl.exe.
  - PostgreSQL requires PostGIS spatial extension to run our geolocation queries, installed by PostGreSQL's Stackbuilder application.
- Development:
  - When opening and building, for the first time, a .tex document inside the LaTeX workshop you will need to accept the packages' installations that will pop up after the build. Those are the packages included in the document's header. They are needed for additional LaTeX functions and required for the file compilation.
- Documentation:
  - Always using vector images inside reports and documentation is really appreciated
- (...)
