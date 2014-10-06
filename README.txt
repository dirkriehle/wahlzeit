Wahlzeit - An Open Source Package to create Photo Rating Sites



PART I - INTRODUCTION

Wahlzeit is an open source web application that lets users upload photos and rate photos of other users, usually on a 1..10 scale. Users get to present their best photos and learn what other users thought of their photos. 

Wahlzeit is used to teach agile methods and open source software development at the Professorship of Open Source Software at the University of Erlangen.

It is an easy-to-learn yet complete Java web application that is available under the GNU Affero Public License v3 license, see the LICENSE.txt file.

Wahlzeit requires a servlet engine like Tomcat and a relational database like PostgreSQL.

For more information, see http:/github.com/dirkriehle/wahlzeit and http://osr.cs.fau.de.



PART II: WAHLZEIT SETUP

1. Login using your database administrative user, e.g. postgres.

	sudo su - postgres

2. Create a new user for the Wahlzeit application, for example using the PostgreSQL command line tools:

	createuser wahlzeit -SDRP
	
	When asked, enter "wahlzeit" (without the quotes) as the password.

	See: http://www.postgresql.org/docs/8.3/static/app-createuser.html

3. Create the database for the application and give it to the new user:

	createdb wahlzeit -O wahlzeit

	See http://www.postgresql.org/docs/8.3/static/app-createdb.html

4. Download and compile Wahlzeit

	Check out the source code from https://github.com/dirkriehle/wahlzeit.git into your Eclipse environment
	
5. [Optional] Populate the database from Eclipse:

	Run as Java Application org.wahlzeit.tools.SetUpFlowers
	
	Alternatively, export SetUpFlowers as a JAR-file, then run it from within the wahlzeit directory.
	
	This is optional, because running Wahlzeit will ensure an empty database setup.



PART III: RUNNING WAHLZEIT
	
6. Run Wahlzeit itself from Eclipse Java EE (Tomcat):

	Start-up your Tomcat server, and Wahlzeit should be running at localhost:8080/wahlzeit/
	
	Login using admin/admin as the user/password combination.
	
	Change the admin password to something safer.
	
	Wahlzeit plays nicely with Apache virtual hosts.
	
	Alternatively, export the whole project as a WAR-file, then run it in your favorite container.

Done!



