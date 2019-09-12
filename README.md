# Wahlzeit: Open Source Software for Photo Rating Sites


## PART I: INTRODUCTION

Wahlzeit is an open source web application that lets users upload photos and rate photos of other users on a 1..10 scale. Users get to present their best photos and learn what other users thought of theirs. 

Wahlzeit is used to teach agile methods and open source software development at the Professorship of Open Source Software at the Friedrich-Alexander-University of Erlangen-Nürnberg.

It is an easy-to-learn yet complete Java web application that is available under the GNU Affero Public License v3 license, see the [LICENSE.txt](/LICENSE.txt) file.

Starting Wahlzeit 2.0, Wahlzeit is a Google App Engine app. It can be run on your local machine or be deployed to Google App Engine for broader availability.

For more information, please see http://github.com/dirkriehle/wahlzeit and http://osr.cs.fau.de.


## PART II: WAHLZEIT SETUP

### Set-up development for Wahlzeit

  1. Install **Java JDK**¹ (version 8 or higher)
  2. Set ``JAVA_HOME``
  2. Install ``git``
  3. If you don't have one yet, create a GitHub account (required)
  4. Create your own repository by forking Wahlzeit from **dirkriehle** to your GitHub-account
  5. On the command line, create or choose a project directory and go there 
  6. Run ```git clone https://github.com/<yourname>/wahlzeit.git```


### Run Wahlzeit on your local machine
  1. On the command line, ```cd wahlzeit```
  2. Run ```./gradlew appengineRun```
  3. Wait until all gradle and project dependencies have been downloaded and the local instance has been started, which could take some minutes
  4. Open [``http://localhost:8080``](http://localhost:8080) to try out Wahlzeit on your machine


### Debug Wahlzeit on your local machine
  1. Run Wahlzeit on your local machine (see above)
  2. Create a remote java debug configuration in your IDE with host ``localhost`` and port ``8000`` (not ``8080``)


### Run Wahlzeit inside a Docker container
  1. Build the Docker image ``docker build -t wahlzeit .``
  2. Run the Docker container ``docker-compose up``
  3. Open [``http://localhost:8080``](http://localhost:8080) to try out Wahlzeit inside a Docker container
  
  
### Deploy Wahlzeit to Google App Engine

**Create a Google App Engine instance:**
  1. If you don't have one yet, create a Google account (required)
  2. Go to [https://console.developers.google.com](https://console.developers.google.com) and login with your Google account
  3. In the developers console, select **create a project**
    1. Choose a project name, called below ``yourprojectname``. If your ``yourprojectname`` is not unique, a UID will be added: ``yourprojectname = yourprojectname + <some Google UID>`` 
    2. Accept the terms of service, for better or worse

**Configure your repository and deploy Wahlzeit**
  1. Configure your project:
    1. Open the file [``/src/main/webapp/WEB-INF/appengine-web.xml``](/src/main/webapp/WEB-INF/appengine-web.xml)
    2. Replace ``<application>dirkriehle-wahlzeit</application>`` with `<application>yourprojectname</application>` where `yourprojectname` is your previously chosen project name
    3. Save and close the ``appengine-web.xml``
  2. Run ```./gradlew appengineUpdate```
  3. If a browser window pops up and asks for permission, accept it
  4. Copy the code from the browser window to your gradle console and hit enter
  5. If everything works out, you will find your project at [``https://yourprojectname.appspot.com``](https://yourprojectname.appspot.com)

Done!

--

¹ Gradle compiles automatically to JDK 1.8, because later versions are not yet supported by Google App Engine Standard Environment
