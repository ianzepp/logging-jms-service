# logging-jms-service

Note: Project was originally written in 2008/2009, saved in Google Code until the service was shut down, and then archived to Github.

# Description

This project is an implementation of a logging service using Java + Spring Framework. It is designed to receive XML messages from the associated Logging Appender project, as routed to it using a Mule ESB backbone.

Key features:
- Quite simple and straightforward.
- Does not use Hibernate or OpenJPA but direct JDBC + parameterized SQL instead.
- This project is designed to be deployed to an Apache Geronimo server, but should work with any compliant J2EE application server.

http://code.google.com/p/ianzepp/source/browse/trunk/logging-jms-service SVN Source Tree

The Logging Appender is the flip side of the logging service. It provides a custom Log4j class to hook into log messages, and forward them to the logging service over JMS + Mule.

http://code.google.com/p/ianzepp/source/browse/trunk/logging-jms-appender SVN Source Tree


