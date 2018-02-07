# Email Service
This web application is a simple implementation of email send request service via multiple providers([`Mailgun`](https://www.mailgun.com) and [`Sendgrid`](https://sendgrid.com/marketing/sendgrid-services/)). This app contains single page front-end UI using [`VueJS`](https://vuejs.org) framework and [`Springboot`](https://projects.spring.io/spring-boot/) server side imlementation with [`Spring(Hystrix)`](https://spring.io/guides/gs/circuit-breaker/) fail over.

# Setup & Build
## Prerequisites
 * Java 1.8 jdk
 * maven 3
 * Node (>= v8.9.x)
 * npm (>= 3.0.0)

## Build & Run
#### ![#f03c15](https://placehold.it/15/f03c15/000000?text=+) NOTE before running the Springboot app
 * Private APIKEYS for both Mailgun and Sendgrid providers should be set to environment variables.
 You can either choose to save it in your bashrc profile or save it in a file and execute before running the app.
 This is also noted in Sendgrid api guideline.

 Create file `apikey.env` in root project path (`.gitignore` includes this file name and will be ignored during commit)
 ```
export SENDGRID_API_KEY='${YOUR_SENDGRID_API_KEY}' 
export MAILGUN_API_KEY='${YOUR_MAILGUN_API_KEY}'
 ```
 and before starting the app run 
 ```
 source ./apikey.env
 ```

### 1. Running front-end development mode
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
   * Default port is set to 8080 (make sure `webpack-dev-server` is not running or change ports to avoid conflict)
```
mvn --projects backend spring-boot:run
```
## Accessing the webpage
  * If everything is setup as default, you can now goto `http://localhost:8080` to access page
 1. From, To, Subject and Content fields are required due to providers' limitation
 2. CC and BCC fields are initially hidden and can be toggled. 
 3. TO, CC and BCC accepts multiple values up to 10 each. Input email value will be validated when `,` `;` `space` `enter` key or `blur` event is fired.
 
## Running Unit tests
  * JUnit test is already part of maven project build, but to run only tests, simply run below from project base
     * (Front-end test is not yet forced on maven project build process. Added this in todo section)
  ```
  mvn test
  ```
  * Frontent unit tests are written in Jasmine and Chai. Run below from `email-server/email-web`
  ```
  npm run unit
  ```
  and you can also check test coverage in console
```
=============================== Coverage summary ===============================
Statements   : 83.18% ( 89/107 )
Branches     : 88.06% ( 59/67 )
Functions    : 87.5% ( 21/24 )
Lines        : 83.18% ( 89/107 )
================================================================================

```

#### Browser Support (Tested) 
`Chrome`, `Firefox`, `IE EDGE`, `Safari`, `>= IE 10`
 * For best experience recommended to use modern web browsers. 


# API document
 #### Call  
`POST`          | `${HOST_NAME}:8080/api/v1/send` 

 `Content-Type` | application/json
 
 #### Data
 ```json
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
 * **ResponseBody** will be empty(null) if both provider fails to send email. In this case, status code will be 500. 
 * `responseCode` can either be `SUCCESS` or `ERROR` depending on email submission status from email providers.
 * `body` contains submit status message from email providers if exists.
 ```json
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
   * To use Mailgun free api, email addresses should be added in whitelist
 * Sendgrid : https://sendgrid.com/docs/index.html
 
# TODO
 * `Bootstrap-Vue` is used for the UI but currently UI is not fully responsive on smallest view port
 * All core-js libraries are added in `main.js` file due to `Internet Explorer` support. However, not all packages are needed, and need to clean this up to have smaller js package. 
 * Unit test execution should be included as part of the project build
 * Kindly display error notice during maven build that API_KEYs are required to be set in environment variable
 * Consider using Swagger UI for automated rest API documentation when application gets complicated.
