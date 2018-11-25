SELECT
  grundstueck.t_id,
  nummer AS number,
  grundstueck.nbident AS identdn,
  CASE 
    WHEN art_txt = 'Liegenschaft' THEN 'RealEstate'
    ELSE 'Distinct_and_permanent_rights.BuildingRight'
  END AS realestatetype,
  egrid AS egrid,
  bfs_nr AS municipalitycode,
  flaechenmass AS area,
  'valid' AS validitytype,
  ST_AsBinary(grundstueck.geometrie) AS geomwkb,
  ST_AsText(grundstueck.geometrie) AS geomwkt,
  gemeindegrenze.gemeindename AS municipality,
  grundbuchkreis.aname AS subunitOfLandRegister
FROM
  agi_mopublic_pub.mopublic_grundstueck AS grundstueck
  LEFT JOIN agi_hoheitsgrenzen_pub.hoheitsgrenzen_gemeindegrenze AS gemeindegrenze
  ON ST_Intersects(ST_PointOnSurface(grundstueck.geometrie), gemeindegrenze.geometrie)
  LEFT JOIN agi_av_gb_admin_einteilung_pub.grundbuchkreise_grundbuchkreis AS grundbuchkreis
  ON ST_Intersects(ST_PointOnSurface(grundstueck.geometrie), grundbuchkreis.perimeter)
WHERE
  egrid = 'CH154332870676'
 ;
 
WITH parcel AS (
  SELECT
     flaechenmass AS area,
     geometrie AS geometry
  FROM
    agi_mopublic_pub.mopublic_grundstueck
  WHERE
    egrid = 'CH987632068201'
)
SELECT
  foo.t_id,
  'LandUsePlans' AS theme,
  'Grundnutzung' AS subtheme,
  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,
  'inForce' AS lawstatuscode,
  foo.typ_bezeichnung AS information,
  ST_Area(foo.geom) AS areashare,
  area
FROM
(
  SELECT
    bar.t_id,
    bar.typ_kt,
    bar.typ_code_kommunal,
    bar.typ_bezeichnung,    
    ST_Union(geom) AS geom,
    area
  FROM
  (
    SELECT
      grundnutzung.t_id,
      grundnutzung.typ_kt,
      grundnutzung.typ_code_kommunal,
      grundnutzung.typ_bezeichnung,
      (ST_Dump(ST_CollectionExtract(ST_Intersection(parcel.geometry, grundnutzung.geometrie), 3))).geom,
      area
    FROM
      parcel
      LEFT JOIN arp_npl_pub.nutzungsplanung_grundnutzung AS grundnutzung
      ON ST_Intersects(parcel.geometry, grundnutzung.geometrie)
  ) AS bar
  GROUP BY
    bar.t_id,
    bar.typ_kt,
    bar.typ_code_kommunal,
    bar.typ_bezeichnung,
    area
) AS foo
ORDER BY
  foo.typ_kt
;
 