# Email Service
This web application is a simple implementation of email send request service via multiple providers([`Mailgun`](https://www.mailgun.com) and [`Sendgrid`](https://sendgrid.com/marketing/sendgrid-services/)). This app contains single page front-end UI using [`VueJS`](https://vuejs.org) framework and [`Springboot`](https://projects.spring.io/spring-boot/) server side imlementation with [`Spring(Hystrix)`](https://spring.io/guides/gs/circuit-breaker/) fail over.

# Setup & Build
## Prerequisites
 * Java 1.8 jdk
 * maven 3
 * Node (>= v8.9.x)
 * npm (>= 3.0.0)

## Build & Run
#### NOTE before running the Springboot app
 * Private APIKEYS for both Mailgun and Sendgrid providers should be set in envrionment variables.
 You can either choose to save it in your bashrc profile or save it in a file and execute before running the app.
 This is also noted in Sendgrid api guideline.

 In file - apikey.env
 ```
export SENDGRID_API_KEY='${YOUR_SENDGRID_API_KEY}' 
export MAILGUN_API_KEY='${YOUR_MAILGUN_API_KEY}'
 ```
 and before starting the app run  
 ```
 source ./sendgrid.env
 ```

### 1. Running front-end development mode web application
   * default port for  [`webpack-dev-server`](https://www.npmjs.com/package/webpack-dev-server) is set to 8080. To run, goto `email-service/email-web` path and execute ...
```
npm install   → Install node modules
npm run dev   → Run webpack-dev-server
```
### 2. Build web & server project with maven
   * Maven will build web module and copy static resources to spring boot public resource path
```
mvn -U clean install 
```
### 3. Run complete Springboot app
   * Default port is set to 8080, goto `http://localhost:8080` to access the page
```
mvn --projects backend spring-boot:run
```

# API document
 #### Call  
`POST` | `${HOST_NAME}:8080/api/v1/send` 
 
 #### Data
 ```
{
  "from":"sender@testmail.com",
  "subject":"Email Subject",
  "text":"email content",
  "to":[
    "recipient@testmail.com"
  ],
  "cc":[
    "cc_user1@testmail.com","cc_user2@testmail.com","cc_user3@testmail.com"
  ],
  "bcc":[
    "bcc_user1@testmail.com"
  ]
}
 ```
 
 #### ResponseBody
 * **ResponseBody** will be empty(null) if both provider fails to send email. In this case, status code of response will be 500 Internal Server Error. 
 * `responseCode` can either be `SUCCESS` or `ERROR` depending on email submission status from email providers.
 * `body` contains submit status message from email providers if exists.
 ```
{
  "responseCode":"SUCCESS",
  "body":null
}
 ```

# Tech references & link
 * VueJS : https://vuejs.org/v2/guide/
 * Bootstrap-Vue : https://bootstrap-vue.js.org/docs/reference/color-variants
 * Springboot : https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/
## Email Providers
 * Mailgun : https://documentation.mailgun.com/en/latest/
 * Sendgrid : https://sendgrid.com/docs/index.html
