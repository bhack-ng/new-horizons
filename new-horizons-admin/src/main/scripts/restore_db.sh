#!/usr/bin/env bash
 pg_restore -h ihc.simplex-software.ru -p 54322 -U postgres ./outdb.tar

 ##создать базу и
pg_restore -c -U postgres -d arbatbazadb -v  -W  -h ihc.simplex-software.ru -p 54322 ./outdb.tar
