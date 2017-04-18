
@echo off
echo 请输入系统模块 user city team
set /p input=
echo input: %input%
F:
cd F:\deploy\java\sns-ICE-server
cd %input%-service
svn update
echo self path: %CD%
call mvn clean package -Dmaven.test.skip=true -U

echo  请输入启动类类名  BootUserStrap  BootCityStrap BootCommonStrap BootTeamStrap
set /p mainclass=
cd target
WinRAR x %input%-service.zip
cd %input%-service
java -cp ./;lib/* com.hhly.sns.%input%.%mainclass%

echo. & pause









