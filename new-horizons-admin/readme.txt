docker run --name arbat-baza-2-postgres -e POSTGRES_PASSWORD=ohGie4ah --restart=always -d -p 54322:5432 postgres


mkdir -p /var/lib/arbat-baza/jetty/webapps
docker run -d -p 8092:8080 --name arbat-baza.web --read-only -v /tmp/jetty -v /run/jetty -v /var/lib/arbat-baza/jetty/webapps:/var/lib/jetty/webapps --restart=always jetty

0)установить локаль dpkg-reconfigure locales
1)Установка postgres
2)unrar (https://wiki.debian.org/ru/SourcesList)
3)apt-get install openjdk-8-jdk



jetty:
1)Add to jetty xml
<New id="podborDb" class="org.eclipse.jetty.plus.jndi.Resource">
    <Arg></Arg>
    <Arg>jdbc/podborDb</Arg>
    <Arg>
        <New class="com.mchange.v2.c3p0.ComboPooledDataSource">
            <Set name="driverClass">org.postgresql.Driver</Set>
            <Set name="jdbcUrl">jdbc:postgresql://host:port/databasename</Set>
            <Set name="user">username</Set>
            <Set name="password">password</Set>
        </New>
    </Arg>
</New>
2)add jars: postgres jdbc  and c3p0 datasource to $JETTRY_HOME/lib/jndi/


docker network create new-horizons-network

docker run --name new-horizons-postgres -e POSTGRES_PASSWORD=Niep5AP1 --restart=always -d --net=new-horizons-network postgres
docker run -d -p 80:80 --name new-horizons-nginx --restart=always -v /var/run/docker.sock:/tmp/docker.sock:ro --net=new-horizons-network  jwilder/nginx-proxy
docker run -d --restart=always --name new-horizons-web  --net=new-horizons-network -e "VIRTUAL_HOST=ng2.simplex-software.ru" -e "LETSENCRYPT_HOST=ng2.simplex-software.ru" -e "LETSENCRYPT_EMAIL=info@simplex-software.ru"  wwwsimplexsoftwareru/new-horizons-web:v1.4.2

docker logs -f new-horizons-web



docker stop new-horizons-web
docker rm new-horizons-web
docker stop new-horizons-postgres
docker rm new-horizons-postgres
docker ps -a




set -Dnet.sf.ehcache.skipUpdateCheck=true

