package com.hyperskill.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DatabaseConnectionTester {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionTester.class);

    @Bean
    public CommandLineRunner testDatabaseConnection(JdbcTemplate jdbcTemplate) {
        return args -> {
            logger.info("⏳ Testing database connection...");
            try {
                // Simple query to test connection
                String dbVersion = jdbcTemplate.queryForObject(
                        "SELECT version()", String.class);

                logger.info("✅ DATABASE CONNECTION SUCCESSFUL!");
                logger.info("Database version: {}", dbVersion);

                // Check if any tables are available (optional)
                try {
                    Integer tableCount = jdbcTemplate.queryForObject(
                            "SELECT count(*) FROM information_schema.tables WHERE table_schema = current_schema()",
                            Integer.class);
                    logger.info("Found {} tables in the current schema", tableCount);
                } catch (Exception e) {
                    logger.warn("Could not query table information: {}", e.getMessage());
                }

            } catch (Exception e) {
                logger.error("❌ DATABASE CONNECTION FAILED: {}", e.getMessage());
                logger.error("Make sure your PostgreSQL server is running and environment variables are set correctly");
                logger.error("Required environment variables: POSTGRES_DB, POSTGRES_USER, POSTGRES_PASSWORD");

                // Print stack trace for detailed debugging
                if (logger.isDebugEnabled()) {
                    e.printStackTrace();
                }

                // Uncomment the following line if you want the application to fail when DB connection fails
                // throw e;
            }
        };
    }
}