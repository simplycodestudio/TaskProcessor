<h1>Asynchronous Task Processor</h1>

<h6>This application is designed to provide a simple and reliable way to process asynchronous tasks. It allows for the
execution of time-consuming tasks without blocking the main thread and ensures that they are performed in a background
thread.
<h6>Application is working with H2 database. Application has other instance for test case.

## Features

| *target* | *Description* | 
| -------------  | ------------- | 
| Creating task | User can add tasks that will be processed asynchronously, which means that more than 1 task can be processed at the same time. | 
| Checking progress of task | User can read progress of processing task with given id. |
| Checking final result of task | User can check final output of task for given id. If task is not done yet, the user will get the progress of the task. |
| Getting all tasks | User can get all tasks with it's current statuses |
| Getting task for given id | User can get task for given with it's current statuses |
| Setting delay for longer task processing | For debugging purposes or checking if progress feature work, user can add delay in ms. For short inputs it may be about 10000 ms.  |

## Swagger Documentation

<h3>http://localhost:8080/swagger-ui/

## How to start

<h4>1. Perform `mvn clean package`
<h4>2. Perform `docker build -t taskprocessor .`
<h4>3. Perform `docker compose up`

## or

<h4>Use `startup.sh`

## Used libraries

| *name* | *usecase* | 
| -------------  | ------------- | 
| Lombok | Generating constructors, getters and setters. | 
| springfox-swagger2 | API documentation. | 
| Apache commons-lang3 | String manipulation. | 
| javax.validation | Validation of rest controller inputs | 

## Summary

<h4>Due to the fact that the application is essentially small, it was decided to use the `READ_UNCOMMITED` isolation
level.
<br>The data recorded during the transaction is not information affecting the final result of the task, so the possible
occurrence of phantom reads should not be a problem.
<br>No unexpected results were noted during the development of the task.