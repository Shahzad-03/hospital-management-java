@echo off
cd /d "%~dp0src"

if not exist "HospitalApp.class" (
    echo Compiling Java files...
    javac *.java
)

echo Starting Hospital Management System...
java -cp ".;mariadb-java-client-3.5.7.jar" HospitalApp

echo.
pause
