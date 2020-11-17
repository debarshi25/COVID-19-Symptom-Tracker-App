# COVID-19 Symptom Tracker Android Application
This application collects COVID-19 related symptoms from the user and stores them in a database.

##### 1. Heart Rate Sensor
For heart rate sensing, the rear camera of the smartphone is utilized with flashlight. User should place the index finger softly on the camera lens while covering the flashlight for a period of 45 seconds. The heart rate is derived from the variation in red pixels in the frames.

##### 2. Respiratory Rate Senor
For respiratory rate sensing, the accelerometer sensor of the smartphone is utilized. User should lie down and place the smartphone on the chest for a period of 45 seconds. The respiratory rate is derived from the variation in sensor values along z-axis.

##### 3. Location Sensor
For location sensing, the GPS sensor of the smartphone is utilized. The current location of the user is retrieved in terms of latitude and longitude.

##### 4. Symptom Monitoring
User should click on "Measure Heart Rate", "Measure Respiratory Rate" and "Get Location" buttons one by one to collect the respective data from the smartphone sensors. User should then click on "Upload Signs" button which will create a database in the smartphone. The entry of the database is a table with columns as "Heart Rate", "Respiratory Rate", 10 columns for symptoms' ratings and "Location" respectively. User should then click on "Symptoms" button to navigate to the Symptom Logging page where user should select symptom(s) and corresponding rating(s). User need not select all the symptoms. After this, user should click on "Upload Symptoms" button to upload the values into the database. Finally, user should click on "Upload to Server" button to upload the database into the server.

##### 5. Server-side
For the server, the local XAMPP server is used. upload_file.php is used to upload the database from the smartphone into the databases folder inside xampp/htdocs directory.