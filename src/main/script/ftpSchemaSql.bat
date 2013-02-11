ECHO OFF
set WINSCP_HOME=c:\Program Files (x86)\WinSCP

set TARGET_FILE=..\..\..\target\generated-sources\hibernate3\sql\schema.sql
set CATALINA_HOME=/usr/share/apache-tomcat-7.0.12

IF "%1"=="" GOTO PWD_ERROR

set PASSWORD=%1

"%WINSCP_HOME%\WinSCP.com" /script=ftpSchemaSqlScript.txt

GOTO END


:PWD_ERROR
ECHO input password

:MVN_ERROR
ECHO mvn command error has occured

:END

