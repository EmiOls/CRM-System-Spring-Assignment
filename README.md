# Spring Framework
## Assignment-1
### Introduction
We want to write a customer relationship management system (CRM) for our client who has problems with managing their customers.

The problem is that when their customers call the call center, the staff promise to do something for them but they forget, because the system is ad-hoc. Each staff keeps his own to-do list, calendar, post-it notes, papers, etc.

We want to write a system that stores details about all the customers and everytime we get a new customer, we record their details.

And also everytime a customer calls the company, we should be able to record the details of the customer's call. For example 10.30 15 November Jim from HB.
And the system must be able to manage a diary for all the users of the system like email alerts.

Download the CRM System project from here or from Google-classroom under the Spring Assignment1.

All the jar files are in the lib directory. You can use the jar files in the lib folder or you can use this pom file and create a maven project instead (you need to add more dependencies to this pom file in the later assignments).

In this project, you find all the java files that you need to get started. All the routine java code is there, but there is no Spring code.

All the business logics are in the domain package.

The customer class represents a single customer and it has all the details such as the company name, the customer’s telephone number, email address and so on.

The call class represents a single call made by a customer. For example: Jim called from HB at 10.30 15 November 2019.

The action class represents a job that we have to perform as the result of a call. For example: Check with Jim that the new patch will be ready by 15 December 2019.

Each customer can make many calls.
Each call is owned by a single customer.

There is an interface that we want to work in this assignment:
CustomerManagement and its implementation CustomerManagementMockimpl.
The interface and the implementation are already in the project.

There are three things that you need to do to make this project a Spring project:
Task 1.
Complete and implement all the methods in the mock implementation of interface CustomerManagement with the name: CustomerManagementMockImpl which already exists in the project
Task 2.
You have a file application.xml in the src which is empty. If you make a maven project, move this file to resources under the main folder.

Configure the mock implementation of the interface in the applicaton.xml file using <beans> tag.
Task 3.
Write a class called SimpleClient in the client package that gets the service beans and tests their functionality.
First create a container type  ClassPathXmlApplicationContext which accepts application.xml as an argument.
Then create an object type CustomerManagementService with the help of the container (xml file).
Make a list of the customers and print them all.

### Submission:
A link to your github repository with the whole project.

Grade: IG /G

Deadline:6/11/2023