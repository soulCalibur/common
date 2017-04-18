F:

cd  F:\deploy\java\sns-parent\sns-vo\
svn update
call mvn clean deploy

cd  F:\deploy\java\sns-parent\sns-base\
svn update
call mvn clean deploy


cd  F:\deploy\java\sns-ICE-slice\
svn update

cd slice-city
del src\main\java\* /f /q
slice2java --ice src/main/resources/*.ice --output-dir src/main/java
call mvn clean deploy

cd ..
cd slice-commons
del src\main\java\* /f /q
slice2java --ice src/main/resources/*.ice --output-dir src/main/java
call mvn clean deploy

cd ..
cd slice-info
del src\main\java\* /f /q
slice2java --ice src/main/resources/*.ice --output-dir src/main/java
call mvn clean deploy



cd ..
cd slice-team
del src\main\java\* /f /q
slice2java --ice src/main/resources/*.ice --output-dir src/main/java
call mvn clean deploy


cd ..
cd slice-user
del src\main\java\* /f /q
slice2java --ice src/main/resources/*.ice --output-dir src/main/java
call mvn clean deploy


cd ..
cd slice-video
del src\main\java\* /f /q
slice2java --ice src/main/resources/*.ice --output-dir src/main/java
call mvn clean deploy


cd ..
cd slice-org
del src\main\java\* /f /q
slice2java --ice src/main/resources/*.ice --output-dir src/main/java
call mvn clean deploy


echo. & pause









