package com.sauce.utils;

import com.sauce.config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;

public class DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final String BROWSER;
    private static final boolean HEADLESS;
    private static final int IMPLICIT_WAIT;
    private static final int PAGE_LOAD_TIMEOUT;

    static {
        String configBrowser = Config.browser();
        BROWSER = System.getProperty("browser", configBrowser != null ? configBrowser : "chrome").toLowerCase();
        HEADLESS = Boolean.parseBoolean(System.getProperty("headless", String.valueOf(Config.headlessMode())));
        IMPLICIT_WAIT = Integer.parseInt(System.getProperty("implicitWait", String.valueOf(Config.defaultTimeout())));
        PAGE_LOAD_TIMEOUT = Integer.parseInt(System.getProperty("pageLoadTimeout", String.valueOf(Config.longTimeout())));

        logger.info("DriverManager initialized with BROWSER={}, HEADLESS={}", BROWSER, HEADLESS);
    }

    private DriverManager() {
        // Private constructor to prevent instantiation
    }

    public static WebDriver getDriver() {
        if (Objects.isNull(driverThreadLocal.get())) {
            initializeDriver();
        }
        return driverThreadLocal.get();
    }

    private static void initializeDriver() {
        WebDriver driver = null;

        try {
            switch (BROWSER) {
                case "firefox":
                    driver = setupFirefoxDriver();
                    break;
                case "chrome":
                default:
                    driver = setupChromeDriver();
                    break;
            }

            // Common configurations
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));

            driverThreadLocal.set(driver);
            logger.info("Initialized {} WebDriver in {} mode",
                    BROWSER.toUpperCase(), HEADLESS ? "HEADLESS" : "NORMAL");

        } catch (Exception e) {
            logger.error("Failed to initialize WebDriver: {}", e.getMessage(), e);
            throw new RuntimeException("WebDriver initialization failed", e);
        }
    }

    private static WebDriver setupChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (HEADLESS) {
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        // Common Chrome options
        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-notifications",
                "--disable-infobars",
                "--remote-allow-origins=*",
                "--incognito",
                "--start-maximized",
                "--disable-features=PasswordLeakDetection,PasswordManagerOnboarding"
        );

        // Disable password manager and autofill
        java.util.Map<String, Object> prefs = new java.util.HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("autofill.profile_enabled", false);
        prefs.put("autofill.credit_card_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        return new ChromeDriver(options);
    }

    private static WebDriver setupFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        if (HEADLESS) {
            options.addArguments("--headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }

        // Start in private browsing mode (incognito equivalent)
        options.addArguments("-private");

        // Disable password manager and autofill
        options.addPreference("signon.rememberSignons", false);
        options.addPreference("signon.autofillForms", false);
        options.addPreference("extensions.formautofill.available", "off");
        options.addPreference("extensions.formautofill.creditCards.available", false);

        return new FirefoxDriver(options);
    }


    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (Objects.nonNull(driver)) {
            try {
                driver.quit();
                logger.info("WebDriver session ended");
            } catch (Exception e) {
                logger.warn("Error while closing WebDriver: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }

    public static String getBrowserType() {
        return BROWSER;
    }

    public static boolean isHeadless() {
        return HEADLESS;
    }
}