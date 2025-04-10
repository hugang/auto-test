# Auto-Test Documentation

## Installation

### Folder Structure
```
auto-test
│  auto-server.bat
│  libs\auto-test.jar
│  driver
│  conf
```

### Prerequisites
1. **Java 17 or newer** is required.
2. Set the `AUTO_TEST_HOME` environment variable (default is the `auto-test` folder).
3. Download the appropriate WebDriver and place it in the `driver` directory:
   - [Chrome WebDriver](https://chromedriver.chromium.org/downloads) for Chrome 115 or newer ([Chrome for Testing](https://googlechromelabs.github.io/chrome-for-testing/)).
   - [Edge WebDriver](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/).

For manual downloads, refer to:
```
https://googlechromelabs.github.io/chrome-for-testing/known-good-versions-with-downloads.json
```

---

## Running Auto-Test

### Starting the Server
1. Run `auto-server.bat` to start the server.
2. Use the local server URL: [http://localhost:9191](http://localhost:9191).
3. Test cases (e.g., `case_template.xlsm`) can be found in the [testcases_example](doc/testcases_example) directory.

### Running Tests in cli
Use `auto-test.bat` to execute test cases:
```
Usage: auto-test [options]
  Options:
    -b, --baseDir
      Base directory of the program.
    -c, --cases
      Test cases to execute, separated by commas (e.g., 1-2,13).
    -f, --file
      Path to the test case file (absolute or relative to the work directory).
    -h, --help
      Display help message.
    -m, --mode
      Test mode (default: xlsx). Options: xlsx, csv, json.
    -d, --workDir
      Directory to store test case and result files.
```

---

## Features

### Design Test Cases in Web UI
![Design Test Cases](doc/design.png)

### Execute Test Cases in Web UI
![Execute Test Cases](doc/execute.gif)

---

## Commands

Refer to the [Commands Documentation](doc/commands.md) for a list of supported commands.

---

## Additional Tools

- **Selenium IDE**: [Download Selenium IDE](https://github.com/SeleniumHQ/selenium-ide/releases)