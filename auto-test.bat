@echo off
set CURRENT_DIR=%cd%
set JAVA_HOME=%USERPROFILE%\.jdks\corretto-11.0.19
rem set AUTO_TEST_HOME=C:/projs/auto-test to environment variable and path
set PATH=%PATH%;%JAVA_HOME%\bin

set found=false
for %%i in (%*) do (
    if "%%i"=="-d" (
        set found=true
    )
)
if %found%==true (
    echo "java -Dfile.encoding=UTF-8 -cp %AUTO_TEST_HOME%/libs/* io.hugang.RunAutoTest -b %AUTO_TEST_HOME% %*"
    java -Dfile.encoding=UTF-8 -cp %AUTO_TEST_HOME%/libs/* io.hugang.RunAutoTest -b %AUTO_TEST_HOME% %*
) else (
    echo "java -Dfile.encoding=UTF-8 -cp %AUTO_TEST_HOME%/libs/* io.hugang.RunAutoTest -b %AUTO_TEST_HOME% -d %CURRENT_DIR% %*"
    java -Dfile.encoding=UTF-8 -cp %AUTO_TEST_HOME%/libs/* io.hugang.RunAutoTest -b %AUTO_TEST_HOME% -d %CURRENT_DIR% %*
)
