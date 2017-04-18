cd E:\work\CampusSportsSNS\01Code\java\lib\build\bat
E:
mkdir jar-hhly
mkdir jar-system
mkdir all
del jar-system\*.jar /f /q
del jar-hhly\*.jar /f /q
del all\*.jar /f /q
::-----------------------------------
::-----------------------------------
call mvn  package
::-----------------------------------
::-----------------------------------
move jar-system\sns-*.jar jar-hhly\
move jar-system\slice-*.jar jar-hhly\
move jar-system\ice-*.jar jar-hhly\
move jar-system\redis-cache-server-*.jar jar-hhly\
move jar-system\clean-cache*.jar jar-hhly\
move jar-system\video-utils*.jar jar-hhly\
move jar-system\membercenter-*.jar jar-hhly\
::-----------------------------------
::-----------------------------------
xcopy jar-system\*.jar all\  /e /y

xcopy jar-hhly\membercenter-*.jar all\  /e /y
xcopy jar-hhly\slice-*.jar all\  /e /y
xcopy jar-hhly\ice-*.jar all\  /e /y
xcopy jar-hhly\video-utils*.jar all\  /e /y
xcopy jar-hhly\sns-*.jar all\  /e /y

del all\ice-server*.jar /f /q
::-----------------------------------
::-----------------------------------
rd target /s /q
echo. & pause




