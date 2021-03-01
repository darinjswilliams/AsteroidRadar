# AsteroidRadar  Overview

Asteroid Radar is an app to view the asteroids detected by NASA that pass near Earth, you can view all the detected asteroids given a period of time with data such as the size, velocity, distance to earth and if they are potentially hazardous. In this project, you will apply the skills such as fetching data from the internet, saving data to a database, and display the data in a clear, compelling UI.

You will need the NEoWs API which is a free, open source API provided by NASA JPL Asteroid team, as they explain it: “Is a RESTful web service for near earth Asteroid information. With NeoWs a user can: search for Asteroids based on their closest approach date to Earth, lookup a specific Asteroid with its NASA JPL small body id, as well as browse the overall data-set.”

The resulting output of the project will be two screens: a Main screen with a list of all the detected asteroids and a Details screen that is going to display the data of that asteroid once it´s selected in the Main screen list. The main screen will also show the NASA image of the day to make the app more striking

## Dependencies
- Android JetPack - (navigation-fragment-ktx, navigation-ui-ktx,)
- Retrofit
- Room
- Picasso
- Kotlin - (core-ktx, fragment-ktx, kotlinx-coroutines-core, kotlinx-coroiutines-android, kotlin-coroutines-adapter)
- Expresso
- JUnit
- Timber
- Moshi
- WorkManager
- Recyclerview - Has been refactor to use ListAdapter
- Lifecycle - ( lifecycle-viewmodel, lifecycle-livedata)
 


## Screenshots
![Screenshot1](screenshots/screen_1.png)
![Screenshot2](screenshots/screen_2.png)
![Screenshot3](screenshots/screen_3.png)
![Screenshot4](screenshots/screen_4.png)

## Api Key

Will Required you to request a Unique Nasa API Key. A key is generated after you register with the https://api.nasa.gov/ site.

How to user API Key

To fetch asteroids and pictures from Nasa, you will use the API from api.nasa.gov. If you don’t already have an account, you will need to create one in order to request an API Key. In your request for a key, fill in the require fields and click generate  Once you submit your request, you should receive your key via email shortly after. In order to request popular movies you will want to request data from the /movie/popular and /movie/top_rated endpoints (documentation). An API Key is required. Once you obtain your key, you append it to your HTTP request as a URL parameter like so: https://api.nasa.gov/neo/rest/v1/feed?start_date=START_DATE&end_date=END_DATE&api_key=[YOUR_API_KEY]. You will need this in subsequent requests.

## Location of Nasa API Key

First, create a file apikey.properties in your root directory with the values for different secret keys:

CONSUMER_KEY=XXXXXXXXXXX  To avoid these keys showing up in your repository, make sure to exclude the file from being checked in by adding to your .gitignore file:

apikey.properties Next, add this section to read from this file in your app/build.gradle file. You'll also create compile-time options that will be generated from this file by 
using the buildConfigField definition:

- def apikeyPropertiesFile = rootProject.file("apikey.properties") 
- def apikeyProperties = new Properties() 
- apikeyProperties.load(new FileInputStream(apikeyPropertiesFile))

```
android {

defaultConfig {

 // should correspond to key/value pairs inside the file   
buildConfigField("String", "CONSUMER_KEY", apikeyProperties['CONSUMER_KEY'])

} } You can now access this field anywhere within your source code with the BuildConfig object provided by Gradle:
```


### Setup a Constant

Inside of your application's code, set up a constant field that can be passed as parameter to your retrofit api
- const val key =  BuildConfig.CONSUMER_KEY

