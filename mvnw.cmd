@echo off
setlocal

if not "%MAVEN_HOME%"=="" (
  set "MAVEN_BIN=%MAVEN_HOME%\bin\mvn.cmd"
) else (
  where mvn >nul 2>nul
  if errorlevel 1 (
    echo Maven is not installed or not on PATH.
    exit /b 1
  )
  set "MAVEN_BIN=mvn.cmd"
)

call "%MAVEN_BIN%" %*
