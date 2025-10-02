package com.sauce.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Config {

    private static final Logger logger = LoggerFactory.getLogger(Config.class);
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "application.properties";

    static {
        loadProperties();
        loadEnvFile();
    }

    private static void loadProperties() {
        try (InputStream input = Config.class.getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                logger.warn("Unable to find {} file", CONFIG_FILE);
                return;
            }

            properties.load(input);
            logger.info("Configuration loaded successfully from {}", CONFIG_FILE);

        } catch (IOException e) {
            logger.error("Error loading configuration file: {}", CONFIG_FILE, e);
        }
    }

    private static void loadEnvFile() {
        java.io.File envFile = new java.io.File(".env");
        if (!envFile.exists()) {
            logger.debug(".env file not found, skipping");
            return;
        }

        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.FileReader(envFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    properties.setProperty(parts[0].trim(), parts[1].trim());
                }
            }
            logger.info("Loaded credentials from .env file");
        } catch (IOException e) {
            logger.warn("Failed to load .env file: {}", e.getMessage());
        }
    }


    public static String baseUrl() {
        return properties.getProperty("base.url");
    }

    public static String apiBase() { return properties.getProperty("api.base.url"); }
    public static String browser() { return properties.getProperty("browser"); }

    public static long slowMoMs() {
        return getIntProperty("slow.mo", 500);
    }

    public static boolean headlessMode(){ return getBooleanProperty("headless.mode", false);}

    public static Integer defaultTimeout(){ return getIntProperty("default.timeout", 10); }

    public static Integer longTimeout(){
        return getIntProperty("long.timeout", 30);
    }

    public static String getProperty(String key) { return properties.getProperty(key); }

    public static String getProperty(String key, String defaultValue) { return properties.getProperty(key, defaultValue); }

    public static Integer getIntProperty(String key) {
        String value = getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                logger.error("Invalid integer value for key: {} = {}", key, value);
            }
        }
        return null;
    }

    public static Integer getIntProperty(String key, Integer defaultValue) {
        Integer value = getIntProperty(key);
        return value != null ? value : defaultValue;
    }

    public static Boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return null;
    }

    public static Boolean getBooleanProperty(String key, Boolean defaultValue) {
        Boolean value = getBooleanProperty(key);
        return value != null ? value : defaultValue;
    }
}