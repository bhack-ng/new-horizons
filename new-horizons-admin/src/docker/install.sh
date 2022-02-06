#!/usr/bin/env bash
echo create site http://$1/
docker network create new-horizons-network
docker run --name new-horizons-postgres -e POSTGRES_PASSWORD=Niep5AP1 --restart=always -d --net=new-horizons-network postgres
docker run -d -p 80:80 --name new-horizons-nginx --restart=always -v /var/run/docker.sock:/tmp/docker.sock:ro --net=new-horizons-network  jwilder/nginx-proxy
docker run -d --restart=always --name new-horizons-web  --net=new-horizons-network -e VIRTUAL_HOST=$1 wwwsimplexsoftwareru/new-horizons-web:v1.4.2
docker logs -f new-horizons-web
