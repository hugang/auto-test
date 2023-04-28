@echo off
set JAVA_HOME=C:\tools\jdk8u312-b07
set PATH=%PATH%;%JAVA_HOME%\bin
java -cp "dependency/*" io.hugang.RunAutoTest
