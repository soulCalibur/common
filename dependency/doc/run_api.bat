F:
cd F:\deploy\java\sns-parent\sns-api
del target\* /f /q
svn update
call mvn clean package  -Dmaven.test.skip=true -U

if exist target\sns-api.zip (

echo "²âÊÔ²¿Êğ"
cd target
WinRAR x sns-api.zip
cd F:\deploy\apache-tomcat-8.5.4_x64\webapps
rd /s/q api
mkdir api
xcopy /s /e /i /y F:\deploy\java\sns-parent\sns-api\target\sns-api\* api
cd F:\deploy\apache-tomcat-8.5.4_x64\bin
startup.bat

) else ( 
echo ±àÒë´íÎó 
)

















