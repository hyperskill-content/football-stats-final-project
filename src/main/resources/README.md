# PostgreSQL Database Configuration Guide

This document provides information about the PostgreSQL database configuration for the Football Statistics Application.

## Configuration Overview

The application is configured to use PostgreSQL as its database. The configuration is defined in the `application.properties` file in this directory.

## Environment Variables

The following environment variables can be set to configure the database connection:

- `POSTGRES_URL`: The JDBC URL for the PostgreSQL database (default: `jdbc:postgresql://localhost:5432/football_stats`)
- `POSTGRES_USERNAME`: The username for the PostgreSQL database (default: `postgres`)
- `POSTGRES_PASSWORD`: The password for the PostgreSQL database (default: `postgres`)

## Setting Up the Database

1. Install PostgreSQL 12 or higher
2. Create a database named `football_stats`
3. Set the environment variables or use the default values

## Hibernate Configuration

The application uses Hibernate with the following settings:

- `spring.jpa.hibernate.ddl-auto=update`: Automatically updates the database schema
- PostgreSQL dialect for Hibernate
- Standard naming strategies
- SQL logging enabled for debugging

## Connection Pool

The application uses HikariCP for connection pooling with the following configuration:

- Maximum pool size: 10
- Minimum idle connections: 5
- Idle timeout: 30000ms

## Performance Optimization

The following settings are configured for better performance:

- JDBC batching enabled with batch size of 20
- Order inserts and updates for better batching

## Troubleshooting

If you encounter database connection issues:

1. Verify that PostgreSQL is running
2. Check that the database `football_stats` exists
3. Verify that the environment variables are set correctly
4. Check the application logs for detailed error messages

## Additional Resources

- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Spring Boot JPA Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data.sql.jpa-and-spring-data)
- [HikariCP Documentation](https://github.com/brettwooldridge/HikariCP)