# Boilerplate for AWS Lambda in Java

## What is it?

This project is meant to be cloned and changed to suit your moods, but it includes:

  * A suitable pom file, creating an uber jar
  * References the AWS Lambda Runtime (needed for writing a lambda function in Java)
  * A Simple deployment script

## Using it

  * First, rename your App.java class to suit your mood, then change your pom.xml accordingly
  * After that, modify and tweak deploy.sh to mention your code
  
Notice that your code is upload to S3 and then called. As such, you can easily add multiple functions into a single
file, and simply copy the deploy_function_code lines at the bottom to reflect your extra modules. 

## A Client Stub

If you want a client Stub, there's one at http://plnkr.co/r823d0VTmvD2qDHuwnVj (bootstrap, jQuery, AWS SDK for JavaScript in the Browser, and Amazon Cognito). 

See its README.md file to configure it properly

## Using on Codeship

In order to use it in your codeship.io projects, then:

  * As for your setup commands: 

```
$ pip install --upgrade awscli
$ jdk_switcher use oraclejdk8
```
  * Set the test commands for the ```master``` branch as: 

```
$ mvn clean package -Pdeploy
```

  * Use this as your Custom Script in your deployment pipeline: 

```
$ bash deploy.sh
```

  * Finally, ensure your environment settings are set, which are:
    * ```AWS_ACCESS_KEY_ID```
    * ```AWS_SECRET_ACCESS_KEY```
    * ```AWS_DEFAULT_REGION```

