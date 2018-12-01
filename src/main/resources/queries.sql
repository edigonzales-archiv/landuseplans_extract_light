/*
WITH parcel AS (
  SELECT
     flaechenmass AS area,
     geometrie AS geometry
  FROM
    agi_mopublic_pub.mopublic_grundstueck
  WHERE
    egrid = 'CH970687433258'
)
SELECT
  foo.t_id,
  'NoiseSensitivityLevels' AS theme,
  'Lärmempfindlichkeitsstufen (in Nutzungszonen)' AS subtheme,
  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,
  'inForce' AS lawstatuscode,
  foo.typ_bezeichnung AS information,
  ST_Area(foo.geom) AS areashare,
  area AS parcelarea
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
      ueberlagerndeFestlegung.t_id,
      ueberlagerndeFestlegung.typ_kt,
      ueberlagerndeFestlegung.typ_code_kommunal,
      ueberlagerndeFestlegung.typ_bezeichnung,
      (ST_Dump(ST_CollectionExtract(ST_Intersection(parcel.geometry, ueberlagerndeFestlegung.geometrie), 3))).geom,
      area
    FROM
      parcel
      INNER JOIN arp_npl_pub.nutzungsplanung_ueberlagernd_flaeche AS ueberlagerndeFestlegung
      ON ST_Intersects(parcel.geometry, ueberlagerndeFestlegung.geometrie)
  ) AS bar
  GROUP BY
    bar.t_id,
    bar.typ_kt,
    bar.typ_code_kommunal,
    bar.typ_bezeichnung,
    area
) AS foo
WHERE
  substring(foo.typ_kt FROM 1 FOR 4) IN ('N680','N681','N682','N683','N684','N685','N686')
WHERE
  ST_Length(foo.geom) > 0.5  
ORDER BY
  foo.typ_kt
;
*/

/*
WITH parcel AS (
  SELECT
     flaechenmass AS area,
     geometrie AS geometry
  FROM
    agi_mopublic_pub.mopublic_grundstueck
  WHERE
    egrid = 'CH970687433258'
)
SELECT
  foo.t_id,
  'LandUsePlans' AS theme,
  'Erschliessung (Punktobjekte)' AS subtheme,
  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,
  'inForce' AS lawstatuscode,
  foo.typ_bezeichnung AS information,
  nrofpoints,
  area AS parcelarea,
  geometrytype(foo.geom)
FROM
(
  SELECT
    bar.t_id,
    bar.typ_kt,
    bar.typ_code_kommunal,
    bar.typ_bezeichnung,  
    geom,
    count(geom) AS nrofpoints,
    area
  FROM
  (
    SELECT
      erschliessungPunktobjekt.t_id,
      erschliessungPunktobjekt.typ_kt,
      erschliessungPunktobjekt.typ_code_kommunal,
      erschliessungPunktobjekt.typ_bezeichnung,
      ST_Intersection(parcel.geometry, erschliessungPunktobjekt.geometrie) AS geom,
      area
    FROM
      parcel
      INNER JOIN arp_npl_pub.nutzungsplanung_erschliessung_punktobjekt AS erschliessungPunktobjekt
      ON ST_Intersects(parcel.geometry, erschliessungPunktobjekt.geometrie)
  ) AS bar
  GROUP BY
    bar.t_id,
    bar.typ_kt,
    bar.typ_code_kommunal,
    bar.typ_bezeichnung,
    bar.geom,
    area
) AS foo
ORDER BY
  foo.typ_kt
;
*/

/*
WITH parcel AS (
  SELECT
     flaechenmass AS area,
     geometrie AS geometry
  FROM
    agi_mopublic_pub.mopublic_grundstueck
  WHERE
    egrid = 'CH908806533280'
)
SELECT
  foo.t_id,
  'LandUsePlans' AS theme,
  'Erschliessung (Linienobjekt)' AS subtheme,
  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,
  'inForce' AS lawstatuscode,
  foo.typ_bezeichnung AS information,
  ST_Length(foo.geom) AS lengthshare,
  area AS parcelarea,
  geometrytype(foo.geom)
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
      erschliessungLinienobjekt.t_id,
      erschliessungLinienobjekt.typ_kt,
      erschliessungLinienobjekt.typ_code_kommunal,
      erschliessungLinienobjekt.typ_bezeichnung,
      (ST_Dump(ST_CollectionExtract(ST_Intersection(parcel.geometry, erschliessungLinienobjekt.geometrie), 2))).geom,
      area
    FROM
      parcel
      INNER JOIN arp_npl_pub.nutzungsplanung_erschliessung_linienobjekt AS erschliessungLinienobjekt
      ON ST_Intersects(parcel.geometry, erschliessungLinienobjekt.geometrie)
  ) AS bar
  GROUP BY
    bar.t_id,
    bar.typ_kt,
    bar.typ_code_kommunal,
    bar.typ_bezeichnung,
    area
) AS foo
WHERE
  ST_Length(foo.geom) > 0.5  
ORDER BY
  foo.typ_kt
;
*/
/*
WITH parcel AS (
  SELECT
     flaechenmass AS area,
     geometrie AS geometry
  FROM
    agi_mopublic_pub.mopublic_grundstueck
  WHERE
    egrid = 'CH233287066989'
)
SELECT
  foo.t_id,
  'LandUsePlans' AS theme,
  'Erschliessung (Flächenobjekte)' AS subtheme,
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
      erschliessungFlaechenobjekt.t_id,
      erschliessungFlaechenobjekt.typ_kt,
      erschliessungFlaechenobjekt.typ_code_kommunal,
      erschliessungFlaechenobjekt.typ_bezeichnung,
      (ST_Dump(ST_CollectionExtract(ST_Intersection(parcel.geometry, erschliessungFlaechenobjekt.geometrie), 3))).geom,
      area
    FROM
      parcel
      INNER JOIN arp_npl_pub.nutzungsplanung_erschliessung_flaechenobjekt AS erschliessungFlaechenobjekt
      ON ST_Intersects(parcel.geometry, erschliessungFlaechenobjekt.geometrie)
  ) AS bar
  GROUP BY
    bar.t_id,
    bar.typ_kt,
    bar.typ_code_kommunal,
    bar.typ_bezeichnung,
    area
) AS foo
WHERE
  ST_Area(foo.geom) > 0.5  
ORDER BY
  foo.typ_kt
;
*/
/*
WITH parcel AS (
  SELECT
     flaechenmass AS area,
     geometrie AS geometry
  FROM
    agi_mopublic_pub.mopublic_grundstueck
  WHERE
    egrid = 'CH328910320651'
)
SELECT
  foo.t_id,
  'LandUsePlans' AS theme,
  'Objektbezogene Festlegung' AS subtheme,
  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,
  'inForce' AS lawstatuscode,
  foo.typ_bezeichnung AS information,
  nrofpoints,
  area AS parcelarea,
  geometrytype(foo.geom)
FROM
(
  SELECT
    bar.t_id,
    bar.typ_kt,
    bar.typ_code_kommunal,
    bar.typ_bezeichnung,  
    geom,
    count(geom) AS nrofpoints,
    area
  FROM
  (
    SELECT
      objektbezogeneFestlegung.t_id,
      objektbezogeneFestlegung.typ_kt,
      objektbezogeneFestlegung.typ_code_kommunal,
      objektbezogeneFestlegung.typ_bezeichnung,
      ST_Intersection(parcel.geometry, objektbezogeneFestlegung.geometrie) AS geom,
      area
    FROM
      parcel
      INNER JOIN arp_npl_pub.nutzungsplanung_ueberlagernd_punkt AS objektbezogeneFestlegung
      ON ST_Intersects(parcel.geometry, objektbezogeneFestlegung.geometrie)
  ) AS bar
  GROUP BY
    bar.t_id,
    bar.typ_kt,
    bar.typ_code_kommunal,
    bar.typ_bezeichnung,
    bar.geom,
    area
) AS foo
ORDER BY
  foo.typ_kt
;
*/

/*
WITH parcel AS (
  SELECT
     flaechenmass AS area,
     geometrie AS geometry
  FROM
    agi_mopublic_pub.mopublic_grundstueck
  WHERE
    egrid = 'CH908806773208'
)
SELECT
  foo.t_id,
  'LandUsePlans' AS theme,
  'Linienbezogene Festlegung' AS subtheme,
  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,
  'inForce' AS lawstatuscode,
  foo.typ_bezeichnung AS information,
  ST_Length(foo.geom) AS lengthshare,
  area AS parcelarea,
  geometrytype(foo.geom)
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
      linienbezogeneFestlegung.t_id,
      linienbezogeneFestlegung.typ_kt,
      linienbezogeneFestlegung.typ_code_kommunal,
      linienbezogeneFestlegung.typ_bezeichnung,
      (ST_Dump(ST_CollectionExtract(ST_Intersection(parcel.geometry, linienbezogeneFestlegung.geometrie), 2))).geom,
      area
    FROM
      parcel
      INNER JOIN arp_npl_pub.nutzungsplanung_ueberlagernd_linie AS linienbezogeneFestlegung
      ON ST_Intersects(parcel.geometry, linienbezogeneFestlegung.geometrie)
  ) AS bar
  GROUP BY
    bar.t_id,
    bar.typ_kt,
    bar.typ_code_kommunal,
    bar.typ_bezeichnung,
    area
) AS foo
WHERE
  ST_Length(foo.geom) > 0.5  
ORDER BY
  foo.typ_kt
;
*/

/*
WITH parcel AS (
  SELECT
     flaechenmass AS area,
     geometrie AS geometry
  FROM
    agi_mopublic_pub.mopublic_grundstueck
  WHERE
    egrid = 'CH669932068034'
)
SELECT
  foo.t_id,
  'LandUsePlans' AS theme,
  'Überlagernde Festlegung' AS subtheme,
  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,
  'inForce' AS lawstatuscode,
  foo.typ_bezeichnung AS information,
  ST_Area(foo.geom) AS areashare,
  area AS parcelarea
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
      ueberlagerndeFestlegung.t_id,
      ueberlagerndeFestlegung.typ_kt,
      ueberlagerndeFestlegung.typ_code_kommunal,
      ueberlagerndeFestlegung.typ_bezeichnung,
      (ST_Dump(ST_CollectionExtract(ST_Intersection(parcel.geometry, ueberlagerndeFestlegung.geometrie), 3))).geom,
      area
    FROM
      parcel
      INNER JOIN arp_npl_pub.nutzungsplanung_ueberlagernd_flaeche AS ueberlagerndeFestlegung
      ON ST_Intersects(parcel.geometry, ueberlagerndeFestlegung.geometrie)
  ) AS bar
  GROUP BY
    bar.t_id,
    bar.typ_kt,
    bar.typ_code_kommunal,
    bar.typ_bezeichnung,
    area
) AS foo
WHERE
  substring(foo.typ_kt FROM 1 FOR 4) NOT IN ('N680','N681','N682','N683','N684','N685','N686')
AND
  ST_Area(foo.geom) > 0.5  
ORDER BY
  foo.typ_kt
;
*/
/*
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
      INNER JOIN arp_npl_pub.nutzungsplanung_grundnutzung AS grundnutzung
      ON ST_Intersects(parcel.geometry, grundnutzung.geometrie)
  ) AS bar
  GROUP BY
    bar.t_id,
    bar.typ_kt,
    bar.typ_code_kommunal,
    bar.typ_bezeichnung,
    area
) AS foo
WHERE
  ST_Area(foo.geom) > 0.5  
ORDER BY
  foo.typ_kt
;
*/


--16503
/*
WITH docs AS 
(
SELECT
  json_array_elements(dok_id::json) AS obj,
  dok_id::json 
FROM
  arp_npl_pub.nutzungsplanung_grundnutzung
WHERE
  t_id = 15130
UNION ALL
SELECT
  json_array_elements(dok_id::json) AS obj,
  dok_id::json 
FROM
  arp_npl_pub.nutzungsplanung_ueberlagernd_flaeche
WHERE
  t_id = 15130
UNION ALL
SELECT
  json_array_elements(dok_id::json) AS obj,
  dok_id::json 
FROM
  arp_npl_pub.nutzungsplanung_ueberlagernd_linie
WHERE
  t_id = 15130
UNION ALL
SELECT
  json_array_elements(dok_id::json) AS obj,
  dok_id::json 
FROM
  arp_npl_pub.nutzungsplanung_ueberlagernd_punkt
WHERE
  t_id = 15130
UNION ALL
SELECT
  json_array_elements(dok_id::json) AS obj,
  dok_id::json 
FROM
  arp_npl_pub.nutzungsplanung_erschliessung_flaechenobjekt
WHERE
  t_id = 15130
UNION ALL
SELECT
  json_array_elements(dok_id::json) AS obj,
  dok_id::json 
FROM
  arp_npl_pub.nutzungsplanung_erschliessung_linienobjekt
WHERE
  t_id = 15130
UNION ALL
SELECT
  json_array_elements(dok_id::json) AS obj,
  dok_id::json 
FROM
  arp_npl_pub.nutzungsplanung_erschliessung_punktobjekt
WHERE
  t_id = 15130
) 
SELECT
  obj->>'titel' AS title,
  obj->>'offiziellertitel' AS officialtitle,
  obj->>'abkuerzung' AS abbreviation,
  obj->>'offiziellenr' AS officialnumber,
  CAST(obj->>'gemeinde' AS integer) AS municipality,
  obj->>'publiziertab' AS publishedat,
  'inForce' AS lawstatuscode,
  obj->>'textimweb_absolut' AS textatweb,
  'LegalProvision' AS documenttype
FROM
  docs
;
*/
 --CH987632068201
 
/*
SELECT
  t_id,
  nummer AS number,
  CASE
    WHEN egrid IS NULL THEN 'CH-DUMMY'
    ELSE egrid
  END AS egrid,
  nbident AS identdn,
  'valid' AS validitytype,
  'RealEstate' AS realestatetype
FROM
  agi_mopublic_pub.mopublic_grundstueck
WHERE
  art = 0
AND
  ST_Intersects(ST_SetSRID(ST_MakePoint(:easting, :northing), 2056), geometrie)
;
*/ 
