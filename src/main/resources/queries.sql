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