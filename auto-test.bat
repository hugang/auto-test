@echo off
call env.bat
set CURRENT_DIR=%cd%

java -Dfile.encoding=UTF-8 -cp %AUTO_TEST_HOME%/libs/* io.hugang.RunAutoTest -b %AUTO_TEST_HOME% %*
