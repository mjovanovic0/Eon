@ECHO OFF
CLS
ECHO Checking environment...
IF "%MYSQL%"=="" (
	ECHO     ENV:MYSQL is NOT defined. [Need: ENV:MYSQL\bin\mysql.exe]
	GOTO:end
) ELSE (
	ECHO     Environment is OK!
)

ECHO Finding mysql.exe...
IF NOT EXIST "%MYSQL%\bin\mysql.exe" (
	ECHO     mysql.exe NOT found. [Looking in: ENV:MYSQL\bin\mysql.exe]
	GOTO:end
) ELSE (
	ECHO     mysql.exe FOUNDED.
)

ECHO Switching to workspace location...
CD "C:\workspace-sts-3.1.0.RELEASE\Eon\EonAdmin"

ECHO Creating database...
%MYSQL%\bin\mysql.exe -p -u root < .\database\mysql\eon.ddl

ECHO Crearing databse done!

:end
PAUSE