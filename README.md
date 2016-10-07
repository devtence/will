Will
==================

Endpoints project generator for Google App Engine using Datastore

## Products
- [App Engine][1]

## Language
- [Java][2]

## APIs
- [Google Cloud Endpoints][3]
- [Google App Engine Maven plugin][4]

## Frameworks
- [AngularJS][7]

## Setup Instructions

1. Create your project at [Google Console][6]

1. In the console navigate to the head of the project

1. Execute the willify script

1. Enter the route where you want your project to be created

1. Enter the `appengine version` that you want to use, the default used by will is `1.9.38` and the recomendation is to use this or above

1. Choose the `com.google.appengine.archetypes:endpoints-skeleton-archetype` archetype, option 2

1. Select the most recent archetype version from the displayed list of available archetype versions by accepting the default.

1. When prompted to Define value for property 'groupId', supply the namespace for your app; for example, com.mycompany.myapp.

1. When prompted to Define value for property 'artifactId', supply the project name; for example, myapp.

1. When prompted to Define value for property 'version', accept the default value.

1. When prompted to Define value for property 'package', accept the default value.

1. When prompted to Define value for property 'gcloud-version', supply the value: 2.0.9.74.v20150814 .

1. When prompted to confirm your choices, accept the default value (Y).
 
1. When prompted to  enter the project name resupply the 'artifactId'.

1. Wait for the project to finish generating.

1. Change route to the directory you defined to create the project.

1. Run the application with `mvn appengine:devserver`, and ensure it's
   running by visiting your local server's api explorer's address (by
   default [localhost:8080/_ah/api/explorer][5].)

1. Deploy your application to Google App Engine with

   $ mvn appengine:update

[1]: https://developers.google.com/appengine
[2]: http://java.com/en/
[3]: https://developers.google.com/appengine/docs/java/endpoints/
[4]: https://developers.google.com/appengine/docs/java/tools/maven
[5]: https://localhost:8080/_ah/api/explorer
[6]: https://console.developers.google.com/
[7]: https://angularjs.org/

License

Will is distributed under the Apache License Version 2.0
