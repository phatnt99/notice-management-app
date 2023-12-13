# notice-management-app
A Spring Boot based Notice Management Application

![example workflow](https://github.com/phatnt99/notice-management-app/actions/workflows/maven.yml/badge.svg)
## Table of contents
- [Features](#features)
- [Core Technologies](#core-technoloies)
- [Integrated Technologies](#integrated-technologies)
- [How to get started](#how-to-get-started)
- [Key problem solving strategies](#key-problem-solving-strategies)

<a id="features"></a>
## Features
### REST APIs
After successfully run the application, please enter http://localhost:8080/swagger-ui/index.html to get the Swagger UI
#### Authentication
- register new user:
  * **payload:** username, password
  * **validation:**
    - username cannot be blank
    - password cannot be blank
- login with username/password
#### Notice
##### Prerequisite: must be logged in and have access token ready in Authorization header
- get all notices
- search notices by: title, content, startTime and endTime in date time range
- get a notice by Id
- create a notice:
    * **payload:**
        - request: title, content, startTime, endTime
        - attachments: array of files
    * **validation:**
        - title cannot be blank, max character length of 250
        - content cannot be blank, max character length of 1500
        - startTime must have pattern "yyyy-MM-dd HH:mm:ss", in future or present
        - endTime must have pattern "yyyy-MM-dd HH:mm:ss", in future or present
        - startTime and endTime must form a date range, for instance, startTime must be equal or less than endTime
- update a notice by Id:
    * **pre-author check:** current user must be the author of this notice
    * **payload:** title, content, startTime, endTime
    * **validation:**
        - title cannot be blank, max character length of 250
        - content cannot be blank, max character length of 1500
        - startTime must have pattern "yyyy-MM-dd HH:mm:ss", in future or present
        - endTime must have pattern "yyyy-MM-dd HH:mm:ss", in future or present
        - startTime and endTime must form a date range, for instance, startTime must be equal or less than endTime
- delete a notice by Id:
    * **pre-author check:** current user must be the author of this notice
#### Attachment
##### Prerequisite: must be logged in and have access token ready in Authorization header
- get all attachments (meta-data) of a notice by notice Id
- get attachment file (return downloadable file)
- delete an attachment:
  * **pre-author check:** current user must be the author of this notice
#### Notice View
##### Prerequisite: must be logged in and have access token ready in Authorization header
- increase view by 1 and get the current view of a notice by notice Id: based on the app-config.allow-self-view and app-config.allow-multiple-times-view to dertermine the view count strategy. More details in [How to get started](#How-to-get-started)

<a id="core-technoloies"></a>
## Core Technologies
- Java JDK 17
- Spring Boot 3 ecosystem
  + Web
  + Security
  + JPA with Hibernate implementation
  + Validation
  + Cache
- Maven 3
- PostgresQL
- Third party dependencies: Lombok, Mapstruct, JWT, Springdoc-OpenAPI, Junit, Flyway

<a id="integrated-technologies"></a>
## Integrated Technologies
- Authentication/Authorizaiton using JWT token
- Auditing with JPA (automatically assigned createdBy, createdDate, updatedBy, updatedDate)
- Soft delete (isDeleted) with Hibernate support (@Filter, @FilterDef, @SQLDelete)
- Using JPA Specification to provide complex and flexible query (applied in the search Notice API)
- Apply caching on various service methods to robust the API response speed
- Validate request payload, custom Validator provided (@ValidDateRange)
- Global exception handler, custom Exceptions provided (NoticeViewFileException, UserAlreadyRegisteredException, AppGenericException)
- Using Mapstruct as mapper engine
- Using Flyway to provide powerful database integration and management
- Using Springdoc OpenAPI to provide Swagger UI
- Unit testing with JUnit (42 test cases was added)

<a id="how-to-get-started"></a>
## How to get started
- Before starting the application, please refer `credentials.example` to get the sample credential to fulfill `application.properties`
- Additional configurations:
  + app-config.upload-dir: simulate the attachment file system, default is folder `attachments` under current project directory
  + app-config.allow-self-view: if disabled, the view counter will not count the author of that notice as one view
  + app-config.allow-multiple-times-view: if disabled, the view counter will not count (the second time onwards) if a user access a notice multiple times
- Once the application started, please enter http://localhost:8080/swagger-ui/index.html and start exploring

<a id="key-problem-solving-strategies"></a>
## Key problem solving strategies
Following are some top key strategies that was applied in our application:
- Prevent saving file content in the database (only store file metadata in the database for refering purpose), my current solution is using one folder (attachments) to simulate a simple file storage, but I recommeded to save the attachment files on the actual separate File Server (for instance, AWS S3, Google Cloud Storage) to free-up the space for the application. In addition, cloud provider services also provide addition caching, better management strategy and help us save more money while still stay stuned with top performance.
- Prevent permanent deletion. Currently I applied Soft delete strategy, which will use additional column is_deleted to indicate whether that resource is deleted or not, by using Hibernate supported @SQLDelete. Other action such as inquiry, update will first check the is_deleted column, by using Hibernate supported annotation @Filter.
- Apply caching to robust the API response speeed. By using Spring Boot Cache, I applied caching for various methods (mostly for the inquiry data) to speed up the response time, using @Cachable. When entities were modifying (update, delete), using propriate annotation @CachePut (for updating) and @CacheEvict (for deleting) to update the caching data.
- For searching Notice, currently JPA specification is used for easy write complex and flexible queries. When come in high volume traffic context, I would like to use ElasticSearch which is a powered search engine, we can save the DB connections go through main PostgresQL DB and speed up the search API since ElasticSearch is NoSQL bult-in.

For further improvement, I will consider following points:
- Apply Microservice architecture
- Add API Gateway
- Containerized our applications
...
  

