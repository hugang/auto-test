@echo off
@REM set AUTO_TEST_HOME=%cd%
@REM set JAVA_HOME=

java -Dfile.encoding=UTF-8 -cp %AUTO_TEST_HOME%/libs/* io.hugang.server.AutoTestServer
