@echo off
netsh interface portproxy show v4tov4

goto init

goto flushPort
::------------------------------------------------------------
:init
set auto=
echo  请输入是否全部自动 1=自动到10.46 2=自动到10.45
set /p auto=
if auto == "1" (
echo aaaaaaaaaaaaaa
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=40100 connectaddress=192.168.10.46 connectport=40100
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=40200 connectaddress=192.168.10.46 connectport=40200
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=40300 connectaddress=192.168.10.46 connectport=40300
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=40400 connectaddress=192.168.10.46 connectport=40400
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=40500 connectaddress=192.168.10.46 connectport=40500
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=40600 connectaddress=192.168.10.46 connectport=40600
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=40700 connectaddress=192.168.10.46 connectport=40700
)
if auto == "2" (
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=40100 connectaddress=192.168.10.45 connectport=40100
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=40200 connectaddress=192.168.10.45 connectport=40200
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=40300 connectaddress=192.168.10.45 connectport=40300
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=40400 connectaddress=192.168.10.45 connectport=40400
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=40500 connectaddress=192.168.10.45 connectport=40500
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=40600 connectaddress=192.168.10.45 connectport=40600
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=40700 connectaddress=192.168.10.45 connectport=40700
)

::------------------------------------------------------------

:flushPort
echo team=40400 city=40100 user=40500 common=40200
echo  请输入需要转发ip的端口
set self_port=
set /p self_port=

netsh interface portproxy delete v4tov4 listenaddress=127.0.0.1 listenport=%self_port%

set self_ip=
echo  请输入需要转发的ip
set /p self_ip=

if defined self_ip (
::增加 不为空
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=%self_port% connectaddress=%self_ip% connectport=%self_port%
) else (
echo 变量 self_ip 为空值
)


echo  当前已有转发端口列表
netsh interface portproxy show v4tov4
goto flushPort
::------------------------------------------------------------