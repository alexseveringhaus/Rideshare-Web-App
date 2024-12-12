# Rideshare-Web-App
This repository contains the project where I built a Java-based, AJAX-integrated web app to promote USC rideshare.

## Prerequisites
- React.js (version 18+)
- Node.js (version 19+)
- Java (version 1.8)
- Tomcat (version 9 or 10)
- mySQL (version 5.6+)

## Installations
### Front end:
1. Installing Node.js and the package manager
  - Install directly from the Node.js website or perform the following commands:
  - winget install Schniz.fnm
  - fnm env --use-on-cd | Out-String | Invoke-Expression
  - fnm use --install-if-missing 22
  - node -v # should print `v22.11.0`
  - npm -v # should print `10.9.0`
2. Local Application Setup:
  - Open the MapClickable and MapTest components and enter your Google Maps API Key
  - Open a terminal and move to the "react-app" folder inside of the “src/main” folder of the application
  - Type the command “npm install” 
  - Enter the command “npm start” or “npm run start” to get the webpage up and running

### Backend:
1. Installing Java
  - Refer to the java installation website to download version 1.8
  - https://www.java.com/en/download/help/download_options.html 
2. Installing MySQL
  - Refer to the MYSQL installation website to download version 5.6+
  - https://www.mysql.com/downloads/ 
  - Make sure the JDBC connector is installed
  - https://dev.mysql.com/downloads/connector/j/ 
3. Installing Apache Tomcat
  - Refer to the installation txt within apache Tomcat’s main website to download version 9 or 10
  - https://tomcat.apache.org/tomcat-8.5-doc/RUNNING.txt

## Starting Server + Database
1. Make sure the proper versions of Java and mySQL are installed locally as specified above. Additionally, make sure the JDBC connector for mySQL is installed. 
2. Set up mySQL with a root administrator account with a password, and start the mySQL service if it is not running already.
3. Run the database-setup.sql script found in the scripts folder of the project repository.
- This can be done through mySQL Workbench:
- Under the 'File' tab, click on 'Open SQL Script' and select the database-setup.sql script.
- Once the script opens in the window, click on the lightning bolt icon in the top bar to run it.
- Alternatively, this can be done through the terminal:
- Connect to the server using the command mysql -u username -p password using the username and password for the root administrator account
- Run source /absolute/path/to/database-setup.sql
- If mysql is not recognized as a valid command in the terminal, update the PATH environment variable to the directory containing the mySQL installation; alternatively, navigate to mySQL installation directory before running the commands
4. Open the Eclipse IDE and import the project through Import > Git > Projects from Git.
5. Navigate to the backend directory and click “run” on your Tomcat server

## Viewing Website
1. After starting the Java server, go to your preferred browser
2. Type in localhost:8080/ or check other port availability to the address bar to view the website 
3. Alternatively, navigate to the project directory in the command line and then run npm start
