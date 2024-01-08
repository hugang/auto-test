# Commands

reference: https://www.selenium.dev/selenium-ide/docs/en/api/commands

## add selection [〇]

Add a selection to the set of options in a multi-select element.

- arguments
    - locator(target): An element locator.
    - value: The value to input.

## answer on next prompt [〇]

Affects the next alert prompt. This command will send the specified answer string to it. If the alert is already
present, then use "webdriver answer on visible prompt" instead.

- arguments
    - answer(target): The answer to give in response to the prompt pop-up.

## assert [〇]

Check that a variable is an expected value. The variable's value will be converted to a string for comparison. The test
will stop if the assert fails.

- arguments
    - variable name(target): The name of a variable without brackets.
    - expected value(value): The result you expect a variable to contain (e.g., true, false, or some other value).

## assert alert [〇]

Confirm that an alert has been rendered with the provided text. The test will stop if the assert fails.

- arguments
    - alert text(target): text to check

## assert checked [〇]

Confirm that the target element has been checked. The test will stop if the assert fails.

- arguments
    - locator(target): An element locator.

## assert confirmation

Confirm that a confirmation has been rendered. The test will stop if the assert fails.

- arguments
    - text: The text to use.

## assert editable [〇]

Confirm that the target element is editable. The test will stop if the assert fails.

- arguments
    - locator: An element locator.

## assert element present [〇]

Confirm that the target element is present somewhere on the page. The test will stop if the assert fails.

- arguments
    - locator(target): An element locator.

## assert element not present [〇]

Confirm that the target element is not present anywhere on the page. The test will stop if the assert fails.

- arguments
    - locator(target): An element locator.

## assert not checked [〇]

Confirm that the target element has not been checked. The test will stop if the assert fails.

- arguments
    - locator(target): An element locator.

## assert not editable

Confirm that the target element is not editable. The test will stop if the assert fails.

- arguments
    - locator: An element locator.

## assert not selected value

Confirm that the value attribute of the selected option in a dropdown element does not contain the provided value. The
test will stop if the assert fails.

- arguments
    - select locator: An element locator identifying a drop-down menu.
    - text: An exact string match. Support for pattern matching is in the works.
      See https://github.com/SeleniumHQ/selenium-ide/issues/141 for details.

## assert not text

Confirm that the text of an element does not contain the provided value. The test will stop if the assert fails.

- arguments
    - locator: An element locator.
    - text: An exact string match. Support for pattern matching is in the works.
      See https://github.com/SeleniumHQ/selenium-ide/issues/141 for details.

## assert prompt

Confirm that a JavaScript prompt has been rendered. The test will stop if the assert fails.

- arguments
    - text: The text to use.

## assert selected value

Confirm that the value attribute of the selected option in a dropdown element contains the provided value. The test will
stop if the assert fails.

- arguments
    - select locator: An element locator identifying a drop-down menu.
    - text: An exact string match. Support for pattern matching is in the works.
      See https://github.com/SeleniumHQ/selenium-ide/issues/141 for details.

## assert selected label

Confirm that the label of the selected option in a dropdown element contains the provided value. The test will stop if
the assert fails.

- arguments
    - select locator: An element locator identifying a drop-down menu.
    - text: An exact string match. Support for pattern matching is in the works.
      See https://github.com/SeleniumHQ/selenium-ide/issues/141 for details.

## assert text

Confirm that the text of an element contains the provided value. The test will stop if the assert fails.

- arguments
    - locator: An element locator.
    - text: An exact string match. Support for pattern matching is in the works.
      See https://github.com/SeleniumHQ/selenium-ide/issues/141 for details.

## assert title

Confirm the title of the current page contains the provided text. The test will stop if the assert fails.

- arguments
    - text: An exact string match. Support for pattern matching is in the works.
      See https://github.com/SeleniumHQ/selenium-ide/issues/141 for details.

## assert value

Confirm the (whitespace-trimmed) value of an input field (or anything else with a value parameter). For checkbox/radio
elements, the value will be "on" or "off" depending on whether the element is checked or not. The test will stop if the
assert fails.

- arguments
    - locator: An element locator.
    - text: An exact string match. Support for pattern matching is in the works.
      See https://github.com/SeleniumHQ/selenium-ide/issues/141 for details.

## check [〇]

Check a toggle-button (checkbox/radio).

- arguments
    - locator(target): An element locator.

## choose cancel on next confirmation

Affects the next confirmation alert. This command will cancel it. If the alert is already present, then use "webdriver
choose cancel on visible confirmation" instead.

## choose cancel on next prompt

Affects the next alert prompt. This command will cancel it. If the alert is already present, then use "webdriver choose
cancel on visible prompt" instead.

## choose ok on next confirmation

Affects the next confirmation alert. This command will accept it. If the alert is already present, then use "webdriver
choose ok on visible confirmation" instead.

## click [〇]

Clicks on a target element (e.g., a link, button, checkbox, or radio button).

- arguments
    - locator(target): An element locator.

## click at [〇]

Clicks on a target element (e.g., a link, button, checkbox, or radio button). The coordinates are relative to the target
element (e.g., 0,0 is the top left corner of the element) and are mostly used to check effects that relay on them, for
example the material ripple effect.

- arguments
    - locator(target): An element locator.
    - coord string(value): Specifies the x,y position (e.g., - 10,20) of the mouse event relative to the element found
      from a
      locator.

## close [〇]

Closes the current window. There is no need to close the initial window, IDE will re-use it; closing it may cause a
performance penalty on the test.

## debugger

Breaks the execution and enters debugger

## do [-]

not needed, just use `while` or `if` instead

Create a loop that executes the proceeding commands at least once. Terminate the branch with the repeat if command.

## double click [〇]

Double-clicks on an element (e.g., a link, button, checkbox, or radio button).

- arguments
    - locator(target): An element locator.

## double click at [〇]

Double-clicks on a target element (e.g., a link, button, checkbox, or radio button). The coordinates are relative to the
target element (e.g., 0,0 is the top left corner of the element) and are mostly used to check effects that relay on
them, for example the material ripple effect.

- arguments
    - locator(target): An element locator.
    - coord string(value): Specifies the x,y position (e.g., - 10,20) of the mouse event relative to the element found
      from a
      locator.

## drag and drop to object

Drags an element and drops it on another element.

- arguments
  locator of object to be dragged: The locator of element to be dragged.
  locator of drag destination object: The locator of an element whose location (e.g., the center-most pixel within it)
  will be the point where locator of object to be dragged is dropped.

## echo [〇]

Prints the specified message into the third table cell in your Selenese tables. Useful for debugging.

- arguments
    - message(target): The message to print.

## edit content

Sets the value of a content editable element as if you typed in it.

- arguments
    - locator: An element locator.
    - value: The value to input.

## else

Part of an if block. Execute the commands in this branch when an if and/or else if condition are not met. Terminate the
branch with the end command.

## else if

Part of an if block. Execute the commands in this branch when an if condition has not been met. Terminate the branch
with the end command.

- arguments
  conditional expression: JavaScript expression that returns a boolean result for use in control flow commands.

## end [〇]

Terminates a control flow block for if, while, and times.

## execute script [〇]

Executes a snippet of JavaScript in the context of the currently selected frame or window. The script fragment will be
executed as the body of an anonymous function. To store the return value, use the 'return' keyword and provide a
variable name in the value input field.

- arguments
    - script(target): The JavaScript snippet to run.
    - variable name(value): The name of a variable without brackets.

## execute async script [〇]

Executes an async snippet of JavaScript in the context of the currently selected frame or window. The script fragment
will be executed as the body of an anonymous function and must return a Promise. The Promise result will be saved on the
variable if you use the 'return' keyword.

- arguments
    - script(target): The JavaScript snippet to run.
    - variable name(value): The name of a variable without brackets.

## for each [〇]

Create a loop that executes the proceeding commands for each item in a given collection.

- arguments
    - array variable name(target): The name of a variable containing a JavaScript array.
    - iterator variable name(value): The name of the variable used when iterating over a collection in a looping control
      flow
      command (e.g., for each).

## if [〇]

Create a conditional branch in your test. Terminate the branch with the end command.

- arguments
  conditional expression(target): JavaScript expression that returns a boolean result for use in control flow commands.

## mouse down [〇]

Simulates a user pressing the left mouse button (without releasing it yet).

- arguments
    - locator: An element locator.

## mouse down at

Simulates a user pressing the left mouse button (without releasing it yet) at the specified location.

- arguments
    - locator: An element locator.
    - coord string: Specifies the x,y position (e.g., - 10,20) of the mouse event relative to the element found from a
      locator.

## mouse move at

Simulates a user pressing the mouse button (without releasing it yet) on the specified element.

- arguments
    - locator: An element locator.
    - coord string: Specifies the x,y position (e.g., - 10,20) of the mouse event relative to the element found from a
      locator.

## mouse out [〇]

Simulates a user moving the mouse pointer away from the specified element.

- arguments
    - locator(target): An element locator.

## mouse over [〇]

Simulates a user hovering a mouse over the specified element.

- arguments
    - locator(target): An element locator.

## mouse up [〇]

Simulates the event that occurs when the user releases the mouse button (e.g., stops holding the button down).

- arguments
    - locator: An element locator.

## mouse up at

Simulates the event that occurs when the user releases the mouse button (e.g., stops holding the button down) at the
specified location.

- arguments
    - locator: An element locator.
    - coord string: Specifies the x,y position (e.g., - 10,20) of the mouse event relative to the element found from a
      locator.

## open [〇]

Opens a URL and waits for the page to load before proceeding. This accepts both relative and absolute URLs.

- arguments
    - url(target): The URL to open (maybe relative or absolute).

## pause [〇]

Wait for the specified amount of time.

- arguments
    - wait time(target): The amount of time to wait (in milliseconds).

## remove selection [〇]

Remove a selection from the set of selected options in a multi-select element using an option locator.

- arguments
    - locator(target): An element locator.
    - option(value): An option locator, typically just an option label (e.g. "John Smith").

## repeat if [-]

not needed, use `while`

Terminate a 'do' control flow branch conditionally. If the result of the provided conditional expression is true, it
starts the do loop over. Otherwise, it ends the loop.

- arguments
    - conditional expression: JavaScript expression that returns a boolean result for use in control flow commands.

## run [〇]

```
# old
Runs a test case from the current project.

- arguments
    - test case: Test case name from the project.
```

Runs a windows bat file, or a shell script on OS X and Linux

- arguments
    - type(target): The type of script to run, either "bat" or "sh" or "cmd".
    - value(value): The path to the file to be executed, or script.

## run case [〇]

Runs a test case from the current project.

- arguments
    - test case file(target): Test case path.
    - test cases(value): Test cases. default all cases.
    - test case mode(type): Test case type. default xlsx.

## run script [〇]

Creates a new "script" tag in the body of the current test window, and adds the specified text into the body of the
command. Beware that JS exceptions thrown in these script tags aren't managed by Selenium, so you should probably wrap
your script in try/catch blocks if there is any chance that the script will throw an exception.

- arguments
    - target: The web element.
    - script(value): The JavaScript snippet to run.
      imcompatible with selenium ide: add target and move script to value.

## select [〇]

Select an element from a drop-down menu using an option locator. Option locators provide different ways of specifying a
select element (e.g., label=, value=, id=, index=). If no option locator prefix is provided, a match on the label will
be attempted.

- arguments
    - select locator(target): An element locator identifying a drop-down menu.
    - option(value): An option locator, typically just an option label (e.g. "John Smith").

## select frame [〇]

Selects a frame within the current window. You can select a frame by its 0-based index number (e.g., select the first
frame with "index=0", or the third frame with "index=2"). For nested frames you will need to invoke this command
multiple times (once for each frame in the tree until you reach your desired frame). You can select the parent frame
with "relative=parent". To return to the top of the page use "relative=top".

- arguments
    - locator(target): An element locator.

## select window [〇]

Selects a popup window using a window locator. Once a popup window has been selected, all commands will go to that
window. Window locators use handles to select windows.

- arguments
    - window handle(target): A handle representing a specific page (tab, or window).

    * tab=1 or contains page title, or contains url

## send keys [〇]

Simulates keystroke events on the specified element, as though you typed the value key-by-key. This simulates a real
user typing every character in the specified string; it is also bound by the limitations of a real user, like not being
able to type into an invisible or read only elements. This is useful for dynamic UI widgets (like auto-completing combo
boxes) that require explicit key events. Unlike the simple "type" command, which forces the specified value into the
page directly, this command will not replace the existing content.

- arguments
    - locator(target): An element locator.
    - key sequence(value): A sequence of keys to type, can be used to send key strokes (e.g. ${KEY_ENTER}).

## set speed

Set execution speed (e.g., set the millisecond length of a delay which will follow each Selenium operation). By default,
there is no such delay, e.g., the delay is 0 milliseconds. This setting is global, and will affect all test runs, until
changed.

- arguments
    - wait time: The amount of time to wait (in milliseconds).

## set window size [〇]

Set the browser's window size, including the browser's interface.

- arguments
    - resolution(target): Specify a window resolution using Width`x`Height. (e.g., 1280x800). or max, min

## store [〇]

Save a target string as a variable for easy re-use.

- arguments
    - text(target): The text to use.
    - variable name(value): The name of a variable without brackets.

## store attribute [〇]

Gets the value of an element attribute. The value of the attribute may differ across browsers (this is the case for
the "style" attribute, for example).

- arguments
    - attribute locator(target): An element locator followed by an @ sign and then the name of the attribute, e.g. "
      foo@bar".
    - variable name(value): The name of a variable without brackets.

## store json [〇]

undefined

- arguments
    - json(target): A string representation of a JavaScript object.
    - variable name(value): The name of a variable without brackets.

## store text [〇]

Gets the text of an element and stores it for later use. This works for any element that contains text.

- arguments
    - locator(target): An element locator.
    - variable name(value): The name of a variable without brackets.

## store title [〇]

Gets the title of the current page.

- arguments
    - text(-): The text to use. not used
    - variable name(value): The name of a variable without brackets.

## store value [〇]

Gets the value of element and stores it for later use. This works for any input type element.

- arguments
    - locator(target): An element locator.
    - variable name(value): The name of a variable without brackets.

## store window handle

Gets the handle of the current page.

- arguments
    - window handle: A handle representing a specific page (tab, or window).

## store xpath count [〇]

Gets the number of nodes that match the specified xpath (e.g. "//table" would give the number of tables).

- arguments
    - xpath(target): The xpath expression to evaluate.
    - variable name(value): The name of a variable without brackets.

## submit [〇]

Submit the specified form. This is particularly useful for forms without submit buttons, e.g. single-input "Search"
forms.

- arguments
    - form locator(target): An element locator for the form you want to submit.

## times [〇]

Create a loop that executes the proceeding commands n number of times.

- arguments
    - times(target): The number of attempts a times control flow loop will execute the commands within its block.
    - loop limit(value): An optional argument that specifies the maximum number of times a looping control flow command
      can
      execute.
      This protects against infinite loops. The defaults value is set to 1000.

## type [〇]

Sets the value of an input field, as though you typed it in. Can also be used to set the value of combo boxes, check
boxes, etc. In these cases, value should be the value of the option selected, not the visible text. Chrome only: If a
file path is given it will be uploaded to the input (for type=file), NOTE: XPath locators are not supported.

- arguments
    - locator(target): An element locator.
    - value: The value to input.

## uncheck [〇]

Uncheck a toggle-button (checkbox/radio).

- arguments
    - locator(value): An element locator.

## verify

Soft assert that a variable is an expected value. The variable's value will be converted to a string for comparison. The
test will continue even if the verify fails.

- arguments
    - variable name: The name of a variable without brackets.
    - expected value: The result you expect a variable to contain (e.g., true, false, or some other value).

## verify checked

Soft assert that a toggle-button (checkbox/radio) has been checked. The test will continue even if the verify fails.

- arguments
    - locator: An element locator.

## verify editable

Soft assert whether the specified input element is editable (e.g., hasn't been disabled). The test will continue even if
the verify fails.

- arguments
    - locator: An element locator.

## verify element present

Soft assert that the specified element is somewhere on the page. The test will continue even if the verify fails.

- arguments
    - locator: An element locator.

## verify element not present

Soft assert that the specified element is not somewhere on the page. The test will continue even if the verify fails.

- arguments
    - locator: An element locator.

## verify not checked

Soft assert that a toggle-button (checkbox/radio) has not been checked. The test will continue even if the verify fails.

- arguments
    - locator: An element locator.

## verify not editable

Soft assert whether the specified input element is not editable (e.g., hasn't been disabled). The test will continue
even if the verify fails.

- arguments
    - locator: An element locator.

## verify not selected value

Soft assert that the expected element has not been chosen in a select menu by its option attribute. The test will
continue even if the verify fails.

- arguments
    - select locator: An element locator identifying a drop-down menu.
    - option: An option locator, typically just an option label (e.g. "John Smith").

## verify not text

Soft assert the text of an element is not present. The test will continue even if the verify fails.

- arguments
    - locator: An element locator.
    - text: The text to use.

## verify selected label

Soft assert the visible text for a selected option in the specified select element. The test will continue even if the
verify fails.

- arguments
    - select locator: An element locator identifying a drop-down menu.
    - text: An exact string match. Support for pattern matching is in the works.
      See https://github.com/SeleniumHQ/selenium-ide/issues/141 for details.

## verify selected value

Soft assert that the expected element has been chosen in a select menu by its option attribute. The test will continue
even if the verify fails.

- arguments
    - select locator: An element locator identifying a drop-down menu.
    - option: An option locator, typically just an option label (e.g. "John Smith").

## verify text

Soft assert the text of an element is present. The test will continue even if the verify fails.

- arguments
    - locator: An element locator.
    - text: The text to use.

## verify title

Soft assert the title of the current page contains the provided text. The test will continue even if the verify fails.

- arguments
    - text: The text to use.

## verify value

Soft assert the (whitespace-trimmed) value of an input field (or anything else with a value parameter). For
checkbox/radio elements, the value will be "on" or "off" depending on whether the element is checked or not. The test
will continue even if the verify fails.

- arguments
    - locator: An element locator.
    - text: An exact string match. Support for pattern matching is in the works.
      See https://github.com/SeleniumHQ/selenium-ide/issues/141 for details.

## wait for element editable [〇]

Wait for an element to be editable.

- arguments
    - locator: An element locator.
    - wait time: The amount of time to wait (in milliseconds).

## wait for element not editable

Wait for an element to not be editable.

- arguments
    - locator: An element locator.
    - wait time: The amount of time to wait (in milliseconds).

## wait for element not present

Wait for a target element to not be present on the page.

- arguments
    - locator: An element locator.
    - wait time: The amount of time to wait (in milliseconds).

## wait for element not visible

Wait for a target element to not be visible on the page.

- arguments
    - locator: An element locator.
    - wait time: The amount of time to wait (in milliseconds).

## wait for element present [〇]

Wait for a target element to be present on the page.

- arguments
    - locator(target): An element locator.
    - wait time(value): The amount of time to wait (in milliseconds).

## wait for element visible [〇]

Wait for a target element to be visible on the page.

- arguments
    - locator(target): An element locator.
    - wait time(value): The amount of time to wait (in milliseconds).

## webdriver answer on visible prompt

Affects a currently showing alert prompt. This command instructs Selenium to provide the specified answer to it. If the
alert has not appeared yet then use "answer on next prompt" instead.

- arguments
    - answer: The answer to give in response to the prompt pop-up.

## webdriver choose cancel on visible confirmation

Affects a currently showing confirmation alert. This command instructs Selenium to cancel it. If the alert has not
appeared yet then use "choose cancel on next confirmation" instead.

## webdriver choose cancel on visible prompt

Affects a currently showing alert prompt. This command instructs Selenium to cancel it. If the alert has not appeared
yet then use "choose cancel on next prompt" instead.

## webdriver choose ok on visible confirmation

Affects a currently showing confirmation alert. This command instructs Selenium to accept it. If the alert has not
appeared yet then use "choose ok on next confirmation" instead.

## while [〇]

Create a loop that executes the proceeding commands repeatedly for as long as the provided conditional expression is
true.

- arguments
    - conditional expression: JavaScript expression that returns a boolean result for use in control flow commands.
    - loop limit: An optional argument that specifies the maximum number of times a looping control flow command can
      execute.
      This protects against infinite loops. The defaults value is set to 1000.

## read properties [〇]

read properties from a file and set them as variables.

- arguments
    - type(target): The type of the file. Currently only json is supported.
    - file path(value): The path to the json file.

## set property [〇]

set properties to environment variables.

- arguments
    - type(target): The type of the file. Currently only json is supported.
    - value(value): The json string of the variable.

## save properties [〇]

save current environment variables to a file.

- arguments
    - type(target): The type of the file. Currently only json is supported.
    - file path(value): The path to the json file.

## call api [〇]

call api and set the values of response to environment variables.

- arguments
    - url(target): The url of the api.
    - value: the json string or json file of the request body.
        - method: The method of the api. Currently only get and post are supported.
        - params: The params of the api.
        - headers: The headers of the api.
        - body: The body of the api.
        - proxy: The proxy of the api.
        - store: array, response values to environment variables.

## jenkins job [〇]

run jenkins job.

- arguments
    - url(target): The url of the jenkins.
    - value: the json string or json file of the request body.
        - job: The job name of the jenkins.
        - params: The params of the jenkins.
        - token: The token of the jenkins.
        - store: array, response values to environment variables.

## generate code [〇]

generate code for development.

- arguments
    - target: The database group which defined in conf/db.conf.
    - value: the json string or json file of the request body.
        - author: author name.
        - package: package name.
        - basePackage: base package name.
        - tables: split with comma.

## export db [〇]

export db by sql.

- arguments
    - target: The database group which defined in conf/db.conf.
    - value: sqls.
    - path: the path of the exported file.
