ECHO OFF
set WINSCP_HOME=c:\Program Files (x86)\WinSCP
set TARGET_FILE=..\..\..\target\generated-sources\hibernate3\sql\schema.sql

IF "%1"=="" GOTO PWD_ERROR

set PASSWORD=%1

mysqldump wwwdw --protocol=tcp -u root  > data.sql

"%WINSCP_HOME%\WinSCP.com" /script=ftpDataScript.txt

rm data.sql

GOTO END


:PWD_ERROR
ECHO input password

:MVN_ERROR
ECHO mvn command error has occured

:END

