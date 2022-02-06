#!/usr/bin/env bash
pg_dump -F t -f outdb.tar  -O -x -c -C  -b  --oids arbatbazadb;

