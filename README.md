# AsteroidRadar 

Will Required you to request a Unique Nasa API Key. A key is generated after you register with the https://api.nasa.gov/ site.

How to user API Key

To fetch asteroids and pictures from Nasa, you will use the API from api.nasa.gov. If you don’t already have an account, you will need to create one in order to request an API Key. In your request for a key, fill in the require fields and click generate  Once you submit your request, you should receive your key via email shortly after. In order to request popular movies you will want to request data from the /movie/popular and /movie/top_rated endpoints (documentation). An API Key is required. Once you obtain your key, you append it to your HTTP request as a URL parameter like so: https://api.nasa.gov/neo/rest/v1/feed?start_date=START_DATE&end_date=END_DATE&api_key=[YOUR_API_KEY]. You will need this in subsequent requests.

## Location of Nasa API Key

First, create a file apikey.properties in your root directory with the values for different secret keys:

CONSUMER_KEY=XXXXXXXXXXX  To avoid these keys showing up in your repository, make sure to exclude the file from being checked in by adding to your .gitignore file:

apikey.properties Next, add this section to read from this file in your app/build.gradle file. You'll also create compile-time options that will be generated from this file by using the buildConfigField definition:

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


### inside of your application's code, set up a constant field that can be passed as parameter to your retrofit api
const val key =  BuildConfig.CONSUMER_KEY

