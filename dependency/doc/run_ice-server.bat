
@echo off
echo ������ϵͳģ�� user city team
set /p input=
echo input: %input%
F:
cd F:\deploy\java\sns-ICE-server
cd %input%-service
svn update
echo self path: %CD%
call mvn clean package -Dmaven.test.skip=true -U

echo  ����������������  BootUserStrap  BootCityStrap BootCommonStrap BootTeamStrap
set /p mainclass=
cd target
WinRAR x %input%-service.zip
cd %input%-service
java -cp ./;lib/* com.hhly.sns.%input%.%mainclass%

echo. & pause









