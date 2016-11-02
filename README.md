##Introduction

Clover will direct logged-in merchants to your site with a code in the url query parameters. The code value can be used to obtain an authcode by calling the /oauth/token endpoint with your client_id (the ID of your app), client_secret (the app secret listed on your dashboard apps page), and the value of the code query parameter. 

The response to this request will include a JSON object containing an access_token


###Audience

This document is intended for the developer organization to understand the system both functionally as well as technically. Also, it may be used to develop the user training content.

###Architectural Model

The application is designed using typical event-driven architectural pattern and uses Node.js library for the implementation.

###Tools & Technologies

```
S.No.	Tools & Technologies	Description
1		Node 					'0.10.33'
2		NPM 					'1.4.3'
* May change later during development for better technology choice, if possible.

```

###Prerequisites
Before starting to use the API, you must have the following:
•	client_id 
•	client_secret 
•	code

###Modules/JS files implementation details

The purpose of this app is to be able to store OAuth client ID and a Secret in the config file(config.json) and avoid passing them through requests, and securing them better.
This way, all that needs to be passed is the code, which is enough to obtain access token, which can be then used for making transactions

###Config.json 
client_id and client_secret can be modified in config.json.

###Clovergo.js – 
Clover.js is sending get request to clover and for GET requests there should be query parameters. 
client_id:_ Your application’s unique ID , client_id is configurable and it is the part of config.json
client_secret: Your client secret key which can also be found in your application properties and should not be publicly shared, Here client_secret is configurable and it is the part of config.json.
code: The value of the code parameter from the URL that the merchant was redirected to after authorization via the Authorization Code Method..

Follow below URL to obtain the code 

https://docs.clover.com/build/oauth-2-0/

###Sample URL – 
http://node-oauth.cfapps.io/oAuthRequest?code=0cb4cd95-219b-8f33-d5ab-bcaa0c10fc95

###Response
On a successful request, the server responds with a JSON object that contains an access_token.

###Example

```
-{"access_token": ”[your access token]”}

```

###Obtaining the App

To begin using the app, go to the GitHub repository to obtain it.

<<GitHub URL – To be decided>> 

###Build/Run node- oAuth application
1.	Open command prompt and go to project location
Ex - C:\Program Files\node\node-oAuth
2.	Set nodejs path as below 
Ex - SET PATH=C:\Program Files\nodejs;%PATH% (depends on your nodejs installation)
3.	Setup Node.js and Npm behind a corporate web proxy
(If needed)


```
npm config set proxy http://proxy.company.com:8080
npm config set https-proxy http://proxy.company.com:8080


```

4.Delete “node_modules” folder (Manually or using rm command)
5.	Run npm install command(in package directory, no arguments):
It Installs the dependencies in the local node_modules folder.
Ex - C:\Program Files\node\ node-oAuth >npm install

###  Test node- oAuth application in local

1)	Once developer runs the application using command
         	node clovergo.js
Ex - C:\node-oAuth>node clovergo.js
	Below text will be displayed on cmd.
 	Running on port 3000
2)	Open browser and hit the URL to send get request to clover with code
Example URL - 
http://localhost:3000/oAuthRequest?code=cc8da649-88fe-6938-769a-c098f8f5efaa


```
Running on port 3000
express deprecated req.param(name): Use req.params, req.body, or req.query inst
ad clovergo.js:13:17
client id  -> : KKRHSHN2FPM9M secreat : 88514be7-ce4c-50e8-5d14-ee9ac03ef74d
success{"access_token":"5f641774-3a7e-5917-c07e-e381bbc6f27c"}


```

###Push Project in to Cloud Foundry
1)	Open cmd tool / terminal
2)	Set proxy http://proxy.company.com:8080 (if needed)
3)	 Type  "cf login -a api.run.pivotal.io”
4)	It will then prompt you for email and password, once authenticated, It will ask you to choose a space either Prod / Dev. So pick an appropriate space where you app currently is.
5)	Type “cf apps”, this will return all the app names in your space.
6)	Go into the root folder where you have the code to push
7)	Type “cf push <app-name>” ex- cf push  node-oAuth
Once the push is successful it will say "complete"

###Troubleshooting 

1)	Category: - Node version Issue 
Error - npm http GET https://registry.npmjs.org/cordova-wp8/3.8.0 TypeError: Request path contains unescaped characters".
Solution - Downgrade from node version node-v4.2.1-x64 to Node v0.10.33 (Stable) 

2)	Category :- Node Path related issue
Error – The node identifier for L-CNU347C4XR is rb6q3jt4hr.
Solution – Set nodejs path using below command
SET PATH=C:\Program Files\nodejs;%PATH%

3)	Category :-  NPM install error due to corporate proxy
Error – npm ERR! network connect ETIMEDOUT
npm ERR! network This is most likely not a problem with npm itself
npm ERR! network and is related to network connectivity.
Solution – Need to configure the npm config file and set Proxy using below command

```
npm config set proxy http://proxy.company.com:8080
npm config set https-proxy http://proxy.company.com:8080


```

execute command “ npm config list” to check  proxy 






