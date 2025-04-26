package io.hugang.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.hugang.bean.NetworkAccessLog;
import io.hugang.config.AutoTestConfig;
import io.hugang.exceptions.AutoTestException;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v135.network.Network;
import org.openqa.selenium.devtools.v135.network.model.RequestId;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.http.HttpHeaders;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class WebDriverUtil {
    private static final Map<String, NetworkAccessLog> NetworkAccessLogMap = new HashMap<>();

    /**
     * Initialize the web driver with configurations from AutoTestConfig.
     */
    public void init() {
        AutoTestConfig autoTestConfig = ThreadContext.getAutoTestConfig();

        validateWebDriverPath(autoTestConfig);
        configureProxySettings(autoTestConfig);
        configureSelenideSettings(autoTestConfig);

        WebDriver driver = initializeWebDriver(autoTestConfig);
        configureBrowserWindow(driver, autoTestConfig);

        WebDriverRunner.setWebDriver(driver);
    }

    /**
     * Validate the WebDriver path.
     */
    private void validateWebDriverPath(AutoTestConfig autoTestConfig) {
        if (StrUtil.isEmpty(autoTestConfig.getWebDriverPath())) {
            throw new AutoTestException("WebDriver path is not configured or empty");
        }
        if (!FileUtil.exist(autoTestConfig.getWebDriverPath())) {
            throw new AutoTestException("Invalid WebDriver path or file does not exist: " + autoTestConfig.getWebDriverPath());
        }
    }

    /**
     * Configure proxy settings if provided.
     */
    private void configureProxySettings(AutoTestConfig autoTestConfig) {
        if (StrUtil.isNotEmpty(autoTestConfig.getProxyHost()) && StrUtil.isNotEmpty(String.valueOf(autoTestConfig.getProxyPort()))) {
            System.setProperty("http.proxyHost", autoTestConfig.getProxyHost());
            System.setProperty("http.proxyPort", String.valueOf(autoTestConfig.getProxyPort()));
            System.setProperty("https.proxyHost", autoTestConfig.getProxyHost());
            System.setProperty("https.proxyPort", String.valueOf(autoTestConfig.getProxyPort()));

            if (StrUtil.isNotEmpty(autoTestConfig.getProxyUser()) && StrUtil.isNotEmpty(autoTestConfig.getProxyPassword())) {
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(autoTestConfig.getProxyUser(), autoTestConfig.getProxyPassword().toCharArray());
                    }
                });
            }
        }
    }

    /**
     * Configure Selenide settings.
     */
    private void configureSelenideSettings(AutoTestConfig autoTestConfig) {
        Configuration.browser = autoTestConfig.getWebDriverPath();
        Configuration.reportsFolder = autoTestConfig.getFileDownloadPath();
        Configuration.savePageSource = false;
    }

    /**
     * Initialize the appropriate WebDriver based on the configuration.
     */
    private WebDriver initializeWebDriver(AutoTestConfig autoTestConfig) {
        Map<String, Object> optionsMap = new HashMap<>();
        optionsMap.put("plugins.always_open_pdf_externally", true);
        optionsMap.put("download.default_directory", autoTestConfig.getFileDownloadPath());

        return switch (autoTestConfig.getWebDriverName()) {
            case "chrome" -> initializeChromeDriver(autoTestConfig, optionsMap);
            case "edge" -> initializeEdgeDriver(autoTestConfig, optionsMap);
            case "firefox" -> initializeFirefoxDriver(autoTestConfig);
            default -> throw new RuntimeException("Unsupported WebDriver");
        };
    }

    /**
     * Configure the browser window size.
     */
    private void configureBrowserWindow(WebDriver driver, AutoTestConfig autoTestConfig) {
        Dimension targetWindowSize = new Dimension(autoTestConfig.getWidth(), autoTestConfig.getHeight());
        driver.manage().window().setSize(targetWindowSize);
    }

    /**
     * Initialize Chrome WebDriver.
     */
    private WebDriver initializeChromeDriver(AutoTestConfig config, Map<String, Object> optionsMap) {
        System.setProperty("webdriver.chrome.driver", config.getWebDriverPath());
        ChromeOptions options = new ChromeOptions();
        if (StrUtil.isNotEmpty(config.getUserProfilePath())) {
            options.addArguments("--user-data-dir=" + config.getUserProfilePath());
        }
        if (ObjectUtil.isNotEmpty(config.getBrowserBinaryPath())) {
            options.setBinary(config.getBrowserBinaryPath());
        }
        options.addArguments("--remote-allow-origins=*");
        options.setExperimentalOption("prefs", optionsMap);
        ChromeDriver chromeDriver = new ChromeDriver(options);
        if (config.getLogApi() != null) {
            initializeDevtoolForChrome(config, chromeDriver);
        }
        return chromeDriver;
    }

    private void initializeDevtoolForChrome(AutoTestConfig config, ChromeDriver chromeDriver) {
        // 获取 DevTools 实例
        DevTools devTools = chromeDriver.getDevTools();
        devTools.createSession();

        // 启用网络监听
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        String[] logApi = config.getLogApi().split(",");
        // 添加监听器，捕获请求
        devTools.addListener(Network.requestWillBeSent(), request -> {
            String url = request.getRequest().getUrl();
            if (StrUtil.isEmpty(url) || Arrays.stream(logApi).noneMatch(api -> StrUtil.isNotEmpty(api) && url.contains(api))) {
                return;
            }
            RequestId requestId = request.getRequestId();
            NetworkAccessLog networkAccessLog = NetworkAccessLogMap.get(requestId.toString());
            if (networkAccessLog == null) {
                networkAccessLog = new NetworkAccessLog();
            }
            networkAccessLog.setRequestId(requestId.toString());
            networkAccessLog.setRequestWillBeSent(request);
            if (networkAccessLog.getRequestWillBeSent() != null && networkAccessLog.getResponseReceived() != null) {
                generateAccessLog(networkAccessLog);
            } else {
                NetworkAccessLogMap.put(requestId.toString(), networkAccessLog);
            }
        });

        // 添加监听器，捕获响应事件
        devTools.addListener(Network.responseReceived(), response -> {
            String url = response.getResponse().getUrl();
            if (StrUtil.isEmpty(url) || Arrays.stream(logApi).noneMatch(api -> StrUtil.isNotEmpty(api) && url.contains(api))) {
                return;
            }
            // 获取响应的 RequestId
            RequestId requestId = response.getRequestId();
            NetworkAccessLog networkAccessLog = NetworkAccessLogMap.get(requestId.toString());
            if (networkAccessLog == null) {
                networkAccessLog = new NetworkAccessLog();
            }
            networkAccessLog.setRequestId(requestId.toString());
            networkAccessLog.setResponseReceived(response);
            if (response.getResponse().getHeaders().get(HttpHeaders.CONTENT_TYPE.toLowerCase()) != null) {
                if (response.getResponse().getHeaders().get(HttpHeaders.CONTENT_TYPE.toLowerCase()).toString().equals("application/json")) {
                    String responseBody = devTools.send(Network.getResponseBody(requestId)).getBody();
                    networkAccessLog.setResponseBody(responseBody);
                }
            }
            if (networkAccessLog.getRequestWillBeSent() != null && networkAccessLog.getResponseReceived() != null) {
                generateAccessLog(networkAccessLog);
            } else {
                NetworkAccessLogMap.put(requestId.toString(), networkAccessLog);
            }
        });
    }

    private void generateAccessLog(NetworkAccessLog networkAccessLog) {
        // output access log
        FileUtil.appendString(networkAccessLog.toString() + "\n", ThreadContext.getAutoTestConfig().getFileDownloadPath() + "/access.log", CharsetUtil.CHARSET_UTF_8);
        // remove
        NetworkAccessLogMap.remove(networkAccessLog.getRequestId());
    }

    /**
     * Initialize Edge WebDriver.
     */
    private WebDriver initializeEdgeDriver(AutoTestConfig config, Map<String, Object> optionsMap) {
        System.setProperty("webdriver.edge.driver", config.getWebDriverPath());
        EdgeOptions options = new EdgeOptions();
        if (StrUtil.isNotEmpty(config.getUserProfilePath())) {
            options.addArguments("--user-data-dir=" + config.getUserProfilePath());
        }
        if (ObjectUtil.isNotEmpty(config.getBrowserBinaryPath())) {
            options.setBinary(config.getBrowserBinaryPath());
        }
        options.addArguments("--remote-allow-origins=*");
        options.setExperimentalOption("prefs", optionsMap);
        return new EdgeDriver(options);
    }

    /**
     * Initialize Firefox WebDriver.
     */
    private WebDriver initializeFirefoxDriver(AutoTestConfig config) {
        System.setProperty("webdriver.gecko.driver", config.getWebDriverPath());
        FirefoxOptions options = new FirefoxOptions();
        if (ObjectUtil.isNotEmpty(config.getBrowserBinaryPath())) {
            options.setBinary(config.getBrowserBinaryPath());
        }
        return new FirefoxDriver(options);
    }

    /**
     * destroy the executor
     */
    public void destroy() {
        // Deprecated WebDriverRunner.closeWebDriver();
        Selenide.closeWebDriver();
    }

}
