call mvn install -f=base.xml
call mvn package -f=__core.xml 
call mvn package -f=__mq.xml 
call mvn package -f=__redis.xml 
call mvn package -f=__web.xml 
call mvn package -f=s_boot_1.3.xml 
call mvn package -f=__jfree.xml