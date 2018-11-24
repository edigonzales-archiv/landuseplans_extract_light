# landuseplans_extract_light

# Vagrant (Pub-DB)
```
sudo -u postgres psql -d postgres -f globals_geodb.rootso.org.dmp
sudo su postgres
pg_restore --role=postgres --exit-on-error -d pub pub_geodb.rootso.org.dmp
```

```
dropdb pub
createdb pub
pg_restore -v --role=postgres --exit-on-error -d pub pub_geodb.rootso.org.dmp -n arp_npl_public
psql -d postgres -c "ALTER DATABASE pub OWNER TO admin;"
```


## Beispielrequests
```
http://localhost:8888/landuseplans/extract/xml/?XY=2630631,1241307 (Liegenschaft und Baurecht)
http://localhost:8888/landuseplans/extract/xml/CH540632891337 (Liegenschaft)
http://localhost:8888/landuseplans/extract/xml/CH670687653294 (Baurecht)

http://localhost:8888/landuseplans/extract/xml/CH987632068201 (überlagernde Flächen)
CH154332870676 (zwei Grundnutzungen)

http://localhost:8888/landuseplans/extract/xml/CH987632068201 (viele kleine Strasseflächen -> ST_Union eingeführt)
http://localhost:8888/landuseplans/extract/xml/CH154332870676
```


