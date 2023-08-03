@echo off
set current_dir=%cd%
set JAVA_HOME=%USERPROFILE%\.jdks\corretto-11.0.19
rem set AUTO_TEST_HOME=C:/projs/auto-test to environment variable and path
set PATH=%PATH%;%JAVA_HOME%\bin
java -Dfile.encoding=UTF-8 -cp "%AUTO_TEST_HOME%/libs/*" io.hugang.RunAutoTest -c %current_dir% %*
