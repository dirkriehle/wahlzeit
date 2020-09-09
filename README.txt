# Wahlzeit: Open Source Software for Photo Rating Sites


## PART I: INTRODUCTION

Wahlzeit is an open source web application that lets users upload photos and rate photos of other users on a 1..10 scale. Users get to present their best photos and learn what other users thought of theirs. 

Wahlzeit is used to teach agile methods and open source software development at the Professorship of Open Source Software at the Friedrich-Alexander-University of Erlangen-Nürnberg.

It is an easy-to-learn yet complete Java web application that is available under the GNU Affero Public License v3 license, see the [LICENSE.txt](/LICENSE.txt) file.

For more information, please see http://github.com/dirkriehle/wahlzeit and http://osr.cs.fau.de.


## PART II: WAHLZEIT SETUP

### Set-up development for Wahlzeit

  1. Install **Java JDK**¹ (version 8 or higher)
  2. Set ``JAVA_HOME``
  3. Install ``git``
  4. Install ``docker`` and ``docker-compose`` (optional, else install local postgres)
  5. If you don't have one yet, create a GitHub account (required)
  6. Create your own repository by forking Wahlzeit from **dirkriehle** to your GitHub-account
  7. On the command line, create or choose a project directory and go there 
  8. Run ```git clone https://github.com/<yourname>/wahlzeit.git```


### Run Wahlzeit on your local machine
  1. On the command line, ```cd wahlzeit```
  2. Start the database, e.g. with ```docker-compose up db```
  3. Run ```./gradlew appRun```
  4. Wait until all gradle and project dependencies have been downloaded and the local instance has been started, which could take some minutes
  5. Open [``http://localhost:8080/wahlzeit``](http://localhost:8080/wahlzeit) to try out Wahlzeit on your machine


### Debug Wahlzeit on your local machine
  1. Run Wahlzeit on your local machine (see above)
  2. TODO: Screenshot

### Test Wahlzeit
  1. Run ```./gradlew test```

### Run Wahlzeit inside a Docker container
  1. Build the Docker image ``docker-compose build`` or ``docker build -t wahlzeit .``
  2. Run the Docker container ``docker-compose up`` or ``docker run --network=host -p 8080:8080 wahlzeit``
  Note: with `docker-compose` the database starts up automatically. The standalone docker command requires a running postgres instance (e.g. like shown above via docker-compose)
  3. Open [``http://localhost:8080/wahlzeit``](http://localhost:8080/wahlzeit) to try out Wahlzeit inside a Docker container
  
--

Gradle compiles automatically to JDK 1.8.
