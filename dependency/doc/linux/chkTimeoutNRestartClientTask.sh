#!/bin/sh
#lingzc,20140428,check ClientTask, start if timeout

HOME="/app/serverOperation"
cd $HOME
log=$HOME/chkTimeoutClientTask.log

dt=`date +%Y%m%d_%T`

indexF=`ps -ef|grep java|grep -w 'payonline.ClientTask'|awk '{print $2}'`
if [ $indexF ]
then
echo "[${dt}]ClientTask is ok !"
echo "[${dt}]ClientTask is ok !" >> ${log}
else
echo "[${dt}]ClientTask is dead,restart..."
echo "[${dt}]ClientTask is dead,restart..." >> ${log}
cd /app/serverOperation
_pid=`ps -ef|grep java|grep -w 'payonline.ClientTask'|awk '{print $2}'`
kill -9 $_pid
sleep 5
cd /app/payOnlineClientTask
sh ./run.sh
fi
