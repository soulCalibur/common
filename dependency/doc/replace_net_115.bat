@echo off
netsh interface portproxy show v4tov4

goto init

goto flushPort
::------------------------------------------------------------
:init
set auto=
echo  �������Ƿ�ȫ���Զ� 1=�Զ���10.46 2=�Զ���10.45
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
echo  ��������Ҫת��ip�Ķ˿�
set self_port=
set /p self_port=

netsh interface portproxy delete v4tov4 listenaddress=127.0.0.1 listenport=%self_port%

set self_ip=
echo  ��������Ҫת����ip
set /p self_ip=

if defined self_ip (
::���� ��Ϊ��
netsh interface portproxy add v4tov4 listenaddress=127.0.0.1 listenport=%self_port% connectaddress=%self_ip% connectport=%self_port%
) else (
echo ���� self_ip Ϊ��ֵ
)


echo  ��ǰ����ת���˿��б�
netsh interface portproxy show v4tov4
goto flushPort
::------------------------------------------------------------