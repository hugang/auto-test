@echo off
set JAVA_HOME=%USERPROFILE%\.jdks\corretto-11.0.19
set AUTO_TEST_HOME=C:/projs/auto-test
set PATH=%PATH%;%JAVA_HOME%\bin
cd %AUTO_TEST_HOME%
java -cp %AUTO_TEST_HOME%"/libs/*" io.hugang.RunAutoTest %*
