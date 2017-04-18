#!/bin/sh
#adam,20131202,check tomcat, restart if timeout

HOME="/app/serverOperation"
cd $HOME
log=$HOME/chkTimeoutTomcat.log

maxSeconds=10

#date >> ${log}
dt=`date +%Y%m%d_%T`
t1=`date +%s`
indexF=/tmp/login.htm
>$indexF
curl --speed-time ${maxSeconds} "http://192.168.0.81:9000/login.action" -o $indexF
t2=`date +%s`
tSpan=`expr $t2 - $t1`

if [ -s $indexF ]
then
echo "[${dt}]tomcat is ok, time take ${tSpan} seconds"
echo "[${dt}]tomcat is ok, time take ${tSpan} seconds" >> ${log}
else
echo "[${dt}]tomcat is dead,timeout ${maxSeconds} seconds,restart..."
echo "[${dt}]tomcat is dead,timeout ${maxSeconds} seconds,restart..." >> ${log}
cd /app/serverOperation
_pid=`ps -ef|grep Tomcat6/|grep -v grep|awk '{print $2}'`
kill -9 $_pid
#killall -9 java
sleep 5
cd /app/Tomcat6/bin/
./catalina.sh start
#tsocks ./catalina.sh start
#proxychains ./catalina.sh start
fi
