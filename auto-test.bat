@echo off
set CURRENT_DIR=%cd%

echo %* | findstr /C:"-d" 1>nul
if errorlevel 1 (
    java -Dfile.encoding=UTF-8 -cp %AUTO_TEST_HOME%/libs/* io.hugang.RunAutoTest -b %AUTO_TEST_HOME% -d %CURRENT_DIR% %*
) else (
    java -Dfile.encoding=UTF-8 -cp %AUTO_TEST_HOME%/libs/* io.hugang.RunAutoTest -b %AUTO_TEST_HOME% %*
)
