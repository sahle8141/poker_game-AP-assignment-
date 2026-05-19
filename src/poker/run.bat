@echo off
:: ─────────────────────────────────────────────────────────────────────────────
::  run.bat  —  Compile & launch Five-Card Draw Poker (no Maven / Gradle needed)
::
::  Usage:
::       set JAVAFX_HOME=C:\javafx-sdk-21\lib
::       run.bat
:: ─────────────────────────────────────────────────────────────────────────────

set FX=%JAVAFX_HOME%
if "%FX%"=="" set FX=C:\path\to\javafx-sdk\lib

set OUT=out
set MAIN=poker.ui.PokerApp

if not exist "%FX%" (
    echo ERROR: JavaFX lib not found at: %FX%
    echo   set JAVAFX_HOME=C:\javafx-sdk-21\lib
    exit /b 1
)

if not exist %OUT% mkdir %OUT%

echo Compiling...
javac ^
  --module-path "%FX%" ^
  --add-modules javafx.controls ^
  -d %OUT% ^
  --source-path src ^
  src\poker\ui\PokerApp.java

if errorlevel 1 ( echo Compilation failed. & exit /b 1 )

echo Launching Five-Card Draw Poker...
java ^
  --module-path "%FX%" ^
  --add-modules javafx.controls ^
  -cp %OUT% ^
  %MAIN%
