OCMJD - URLyBird
================

The Oracle Certified Master Java Developer (OCMJD) project I was assigned, URLyBird.

The Assignment
==============

Background
----------

URLyBird is a broker of discount hotel rooms. They sell accomodations for business and pleasure travellers at short notice, helping hotels to fill rooms that would otherwise be left empty. They take bookings only within 48 hours of the start of room occupancy. Curently, URLyBird sells the rooms over the phone using a team of customer service representatives (CSRs). The CSRs interact with an aging custom-written application that has been drawing increasing criticism from the CSRs. In the future, URLyBird wants to move into Internet-based marketing, and hopes to be able to accept bookings direct from customers over the web.

The company's IT director has decided to migrate the existing application to a Java technology based system. Initially, the system will support only the CSRs, although the hope is that this interim step will give them a starting point for migrating the system to the web. The IT director does not anticipate much reuse of the first Java technology system, but intends to use that system as a learning exercise before going on to a web based system.

The company's IT department has a data file that contains the essential information for the company, but because the data must continue to be manipulated for reports using another custom-written application, the new system must reimplement the database code from scratch without altering the data file format.

The new application, using the existing data file format, must allow the CSRs to generate a list of accomodations that match a customer's criteria. This is the project that you have been commissioned to implement.

What you must do
----------------

The following are the "top level" features that must be implemented:

A client program with a graphical user interface that connects to the database
A data access system that provides record locking and a flexible search mechanism
Network server functionality for the database system

The work involves a number of design choices that have to be made. In all such cases, the following principles should be applied.

Clarity and Maintainability
---------------------------

A clear design, such as will be readily understood by junior programmers, will be preferred to a complex one, even if the complex one is a little more efficient. Code complexity, including nesting depth, argument passing, and the number of classes and interfaces, should be reasonable.

Documentation
-------------

The code itself should be as clear as possible; do not provide comments that do not add to the comprehensibility of the code. Awkward or complex code should have descriptive comments, and javadoc style comments must be used for each element of the public interface of each class. You must create a full suite of documentation for the classes of the completed project. This must be generated using the tool "javadoc" and must be in HTML format.  Provide javadoc documentation for all classes you write.
You must provide basic user documentation. This should be sufficient to allow a user who is familiar with the broad purpose of the project to use the application. This documentation must be in one of these three formats:

 * HTML
 * Plain text (not a wordprocessor format)
 * Within the application as a help system.

Correctness
-----------

Your project must conform to this specification.  Features that deviate from specification will not receive full credit.  You will not receive extra credit points for work beyond the requirements of the specification.
Use of Standard Elements

Use of functionality provided by the core Java classes will be preferred to your own implementation of that functionality, unless there is a specific advantage to providing your own implementation.
