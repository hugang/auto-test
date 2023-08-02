@echo off
set JAVA_HOME=%USERPROFILE%\.jdks\corretto-11.0.19
set PATH=%PATH%;%JAVA_HOME%\bin
java -cp "C:/projs/auto-test/libs/*" io.hugang.RunAutoTest %*
