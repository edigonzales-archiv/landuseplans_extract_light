package ch.so.agi.landuseplansextract.webservice.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ch.so.agi.landuseplansextract.webservice.models.ROL;

@Transactional
@Repository
public class RestrictionOnLandownershipDAOImpl implements RestrictionOnLandownershipDAO {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<ROL> getGrundnutzungByEgrid(String egrid) {
        String sql = "WITH parcel AS (\n" + 
                "  SELECT\n" + 
                "     flaechenmass AS area,\n" + 
                "     geometrie AS geometry\n" + 
                "  FROM\n" + 
                "    agi_mopublic_pub.mopublic_grundstueck\n" + 
                "  WHERE\n" + 
                "    egrid = :egrid\n" + 
                ")\n" + 
                "SELECT\n" + 
                "  foo.t_id,\n" + 
                "  'LandUsePlans' AS theme,\n" + 
                "  'Grundnutzung' AS subtheme,\n" + 
                "  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,\n" + 
                "  'inForce' AS lawstatuscode,\n" + 
                "  foo.typ_bezeichnung AS information,\n" + 
                "  ST_Area(foo.geom) AS areashare,\n" + 
                "  area AS parcelarea,\n" + 
                "  geometrytype(foo.geom)\n" + 
                "FROM\n" + 
                "(\n" + 
                "  SELECT\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,    \n" + 
                "    ST_Union(geom) AS geom,\n" + 
                "    area\n" + 
                "  FROM\n" + 
                "  (\n" + 
                "    SELECT\n" + 
                "      grundnutzung.t_id,\n" + 
                "      grundnutzung.typ_kt,\n" + 
                "      grundnutzung.typ_code_kommunal,\n" + 
                "      grundnutzung.typ_bezeichnung,\n" + 
                "      (ST_Dump(ST_CollectionExtract(ST_Intersection(parcel.geometry, grundnutzung.geometrie), 3))).geom,\n" + 
                "      area\n" + 
                "    FROM\n" + 
                "      parcel\n" + 
                "      INNER JOIN arp_npl_pub.nutzungsplanung_grundnutzung AS grundnutzung\n" + 
                "      ON ST_Intersects(parcel.geometry, grundnutzung.geometrie)\n" + 
                "  ) AS bar\n" + 
                "  GROUP BY\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,\n" + 
                "    area\n" + 
                ") AS foo\n" + 
                "ORDER BY\n" + 
                "  foo.typ_kt\n" + 
                ";";

        RowMapper<ROL> rowMapper = new BeanPropertyRowMapper<ROL>(ROL.class);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("egrid", egrid);
        List<ROL> restrictionOnLandownership = jdbcTemplate.query(sql, namedParameters, rowMapper);
                
        return restrictionOnLandownership;
    }

    @Override
    public List<ROL> getUeberlagerndeFestlegungByEgrid(String egrid) {
        String sql = "WITH parcel AS (\n" + 
                "  SELECT\n" + 
                "     flaechenmass AS area,\n" + 
                "     geometrie AS geometry\n" + 
                "  FROM\n" + 
                "    agi_mopublic_pub.mopublic_grundstueck\n" + 
                "  WHERE\n" + 
                "    egrid = :egrid\n" + 
                ")\n" + 
                "SELECT\n" + 
                "  foo.t_id,\n" + 
                "  'LandUsePlans' AS theme,\n" + 
                "  'Überlagernde Festlegung' AS subtheme,\n" + 
                "  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,\n" + 
                "  'inForce' AS lawstatuscode,\n" + 
                "  foo.typ_bezeichnung AS information,\n" + 
                "  ST_Area(foo.geom) AS areashare,\n" + 
                "  area AS parcelarea,\n" + 
                "  geometrytype(foo.geom)\n" +                 
                "FROM\n" + 
                "(\n" + 
                "  SELECT\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,    \n" + 
                "    ST_Union(geom) AS geom,\n" + 
                "    area\n" + 
                "  FROM\n" + 
                "  (\n" + 
                "    SELECT\n" + 
                "      ueberlagerndeFestlegung.t_id,\n" + 
                "      ueberlagerndeFestlegung.typ_kt,\n" + 
                "      ueberlagerndeFestlegung.typ_code_kommunal,\n" + 
                "      ueberlagerndeFestlegung.typ_bezeichnung,\n" + 
                "      (ST_Dump(ST_CollectionExtract(ST_Intersection(parcel.geometry, ueberlagerndeFestlegung.geometrie), 3))).geom,\n" + 
                "      area\n" + 
                "    FROM\n" + 
                "      parcel\n" + 
                "      INNER JOIN arp_npl_pub.nutzungsplanung_ueberlagernd_flaeche AS ueberlagerndeFestlegung\n" + 
                "      ON ST_Intersects(parcel.geometry, ueberlagerndeFestlegung.geometrie)\n" + 
                "  ) AS bar\n" + 
                "  GROUP BY\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,\n" + 
                "    area\n" + 
                ") AS foo\n" + 
                "WHERE\n" +
                "    substring(foo.typ_kt FROM 1 FOR 4) NOT IN ('N680','N681','N682','N683','N684','N685','N686')\n" +
                "ORDER BY\n" + 
                "  foo.typ_kt\n" + 
                ";";
        
        RowMapper<ROL> rowMapper = new BeanPropertyRowMapper<ROL>(ROL.class);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("egrid", egrid);
        List<ROL> restrictionOnLandownership = jdbcTemplate.query(sql, namedParameters, rowMapper);
                
        return restrictionOnLandownership;
    }

    @Override
    public List<ROL> getLinienbezogeneFestlegungByEgrid(String egrid) {
        String sql = "WITH parcel AS (\n" + 
                "  SELECT\n" + 
                "     flaechenmass AS area,\n" + 
                "     geometrie AS geometry\n" + 
                "  FROM\n" + 
                "    agi_mopublic_pub.mopublic_grundstueck\n" + 
                "  WHERE\n" + 
                "    egrid = :egrid\n" + 
                ")\n" + 
                "SELECT\n" + 
                "  foo.t_id,\n" + 
                "  'LandUsePlans' AS theme,\n" + 
                "  'Linienbezogene Festlegung' AS subtheme,\n" + 
                "  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,\n" + 
                "  'inForce' AS lawstatuscode,\n" + 
                "  foo.typ_bezeichnung AS information,\n" + 
                "  ST_Length(foo.geom) AS lengthshare,\n" + 
                "  area AS parcelarea,\n" + 
                "  geometrytype(foo.geom)\n" +                 
                "FROM\n" + 
                "(\n" + 
                "  SELECT\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,    \n" + 
                "    ST_Union(geom) AS geom,\n" + 
                "    area\n" + 
                "  FROM\n" + 
                "  (\n" + 
                "    SELECT\n" + 
                "      linienbezogeneFestlegung.t_id,\n" + 
                "      linienbezogeneFestlegung.typ_kt,\n" + 
                "      linienbezogeneFestlegung.typ_code_kommunal,\n" + 
                "      linienbezogeneFestlegung.typ_bezeichnung,\n" + 
                "      (ST_Dump(ST_CollectionExtract(ST_Intersection(parcel.geometry, linienbezogeneFestlegung.geometrie), 2))).geom,\n" + 
                "      area\n" + 
                "    FROM\n" + 
                "      parcel\n" + 
                "      INNER JOIN arp_npl_pub.nutzungsplanung_ueberlagernd_linie AS linienbezogeneFestlegung\n" + 
                "      ON ST_Intersects(parcel.geometry, linienbezogeneFestlegung.geometrie)\n" + 
                "  ) AS bar\n" + 
                "  GROUP BY\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,\n" + 
                "    area\n" + 
                ") AS foo\n" + 
                "ORDER BY\n" + 
                "  foo.typ_kt\n" + 
                ";";
        
        RowMapper<ROL> rowMapper = new BeanPropertyRowMapper<ROL>(ROL.class);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("egrid", egrid);
        List<ROL> restrictionOnLandownership = jdbcTemplate.query(sql, namedParameters, rowMapper);
                
        return restrictionOnLandownership;
    }

    @Override
    public List<ROL> getObjektbezogeneFestlegungByEgrid(String egrid) {
        String sql = "WITH parcel AS (\n" + 
                "  SELECT\n" + 
                "     flaechenmass AS area,\n" + 
                "     geometrie AS geometry\n" + 
                "  FROM\n" + 
                "    agi_mopublic_pub.mopublic_grundstueck\n" + 
                "  WHERE\n" + 
                "    egrid = :egrid\n" + 
                ")\n" + 
                "SELECT\n" + 
                "  foo.t_id,\n" + 
                "  'LandUsePlans' AS theme,\n" + 
                "  'Objektbezogene Festlegung' AS subtheme,\n" + 
                "  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,\n" + 
                "  'inForce' AS lawstatuscode,\n" + 
                "  foo.typ_bezeichnung AS information,\n" + 
                "  nrofpoints,\n" + 
                "  area AS parcelarea,\n" + 
                "  geometrytype(foo.geom)\n" + 
                "FROM\n" + 
                "(\n" + 
                "  SELECT\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,  \n" + 
                "    geom,\n" + 
                "    count(geom) AS nrofpoints,\n" + 
                "    area\n" + 
                "  FROM\n" + 
                "  (\n" + 
                "    SELECT\n" + 
                "      objektbezogeneFestlegung.t_id,\n" + 
                "      objektbezogeneFestlegung.typ_kt,\n" + 
                "      objektbezogeneFestlegung.typ_code_kommunal,\n" + 
                "      objektbezogeneFestlegung.typ_bezeichnung,\n" + 
                "      ST_Intersection(parcel.geometry, objektbezogeneFestlegung.geometrie) AS geom,\n" + 
                "      area\n" + 
                "    FROM\n" + 
                "      parcel\n" + 
                "      INNER JOIN arp_npl_pub.nutzungsplanung_ueberlagernd_punkt AS objektbezogeneFestlegung\n" + 
                "      ON ST_Intersects(parcel.geometry, objektbezogeneFestlegung.geometrie)\n" + 
                "  ) AS bar\n" + 
                "  GROUP BY\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,\n" + 
                "    bar.geom,\n" + 
                "    area\n" + 
                ") AS foo\n" + 
                "ORDER BY\n" + 
                "  foo.typ_kt\n" + 
                ";";

        RowMapper<ROL> rowMapper = new BeanPropertyRowMapper<ROL>(ROL.class);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("egrid", egrid);
        List<ROL> restrictionOnLandownership = jdbcTemplate.query(sql, namedParameters, rowMapper);
                
        return restrictionOnLandownership;
    }

    @Override
    public List<ROL> getErschliessungFlaecheByEgrid(String egrid) {
        String sql = "WITH parcel AS (\n" + 
                "  SELECT\n" + 
                "     flaechenmass AS area,\n" + 
                "     geometrie AS geometry\n" + 
                "  FROM\n" + 
                "    agi_mopublic_pub.mopublic_grundstueck\n" + 
                "  WHERE\n" + 
                "    egrid = :egrid\n" + 
                ")\n" + 
                "SELECT\n" + 
                "  foo.t_id,\n" + 
                "  'LandUsePlans' AS theme,\n" + 
                "  'Erschliessung (Flächenobjekte)' AS subtheme,\n" + 
                "  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,\n" + 
                "  'inForce' AS lawstatuscode,\n" + 
                "  foo.typ_bezeichnung AS information,\n" + 
                "  ST_Area(foo.geom) AS areashare,\n" + 
                "  area AS parcelarea,\n" + 
                "  geometrytype(foo.geom)\n" +                 
                "FROM\n" + 
                "(\n" + 
                "  SELECT\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,    \n" + 
                "    ST_Union(geom) AS geom,\n" + 
                "    area\n" + 
                "  FROM\n" + 
                "  (\n" + 
                "    SELECT\n" + 
                "      erschliessungFlaechenobjekt.t_id,\n" + 
                "      erschliessungFlaechenobjekt.typ_kt,\n" + 
                "      erschliessungFlaechenobjekt.typ_code_kommunal,\n" + 
                "      erschliessungFlaechenobjekt.typ_bezeichnung,\n" + 
                "      (ST_Dump(ST_CollectionExtract(ST_Intersection(parcel.geometry, erschliessungFlaechenobjekt.geometrie), 3))).geom,\n" + 
                "      area\n" + 
                "    FROM\n" + 
                "      parcel\n" + 
                "      INNER JOIN arp_npl_pub.nutzungsplanung_erschliessung_flaechenobjekt AS erschliessungFlaechenobjekt\n" + 
                "      ON ST_Intersects(parcel.geometry, erschliessungFlaechenobjekt.geometrie)\n" + 
                "  ) AS bar\n" + 
                "  GROUP BY\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,\n" + 
                "    area\n" + 
                ") AS foo\n" + 
                "ORDER BY\n" + 
                "  foo.typ_kt\n" + 
                ";";

        RowMapper<ROL> rowMapper = new BeanPropertyRowMapper<ROL>(ROL.class);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("egrid", egrid);
        List<ROL> restrictionOnLandownership = jdbcTemplate.query(sql, namedParameters, rowMapper);
                
        return restrictionOnLandownership;
    }

    @Override
    public List<ROL> getErschliessungLinieByEgrid(String egrid) {
        String sql = "WITH parcel AS (\n" + 
                "  SELECT\n" + 
                "     flaechenmass AS area,\n" + 
                "     geometrie AS geometry\n" + 
                "  FROM\n" + 
                "    agi_mopublic_pub.mopublic_grundstueck\n" + 
                "  WHERE\n" + 
                "    egrid = 'CH233287066989'\n" + 
                ")\n" + 
                "SELECT\n" + 
                "  foo.t_id,\n" + 
                "  'LandUsePlans' AS theme,\n" + 
                "  'Erschliessung (Linienobjekt)' AS subtheme,\n" + 
                "  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,\n" + 
                "  'inForce' AS lawstatuscode,\n" + 
                "  foo.typ_bezeichnung AS information,\n" + 
                "  ST_Length(foo.geom) AS lengthshare,\n" + 
                "  area AS parcelarea,\n" + 
                "  geometrytype(foo.geom)\n" + 
                "FROM\n" + 
                "(\n" + 
                "  SELECT\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,    \n" + 
                "    ST_Union(geom) AS geom,\n" + 
                "    area\n" + 
                "  FROM\n" + 
                "  (\n" + 
                "    SELECT\n" + 
                "      erschliessungLinienobjekt.t_id,\n" + 
                "      erschliessungLinienobjekt.typ_kt,\n" + 
                "      erschliessungLinienobjekt.typ_code_kommunal,\n" + 
                "      erschliessungLinienobjekt.typ_bezeichnung,\n" + 
                "      (ST_Dump(ST_CollectionExtract(ST_Intersection(parcel.geometry, erschliessungLinienobjekt.geometrie), 2))).geom,\n" + 
                "      area\n" + 
                "    FROM\n" + 
                "      parcel\n" + 
                "      INNER JOIN arp_npl_pub.nutzungsplanung_erschliessung_linienobjekt AS erschliessungLinienobjekt\n" + 
                "      ON ST_Intersects(parcel.geometry, erschliessungLinienobjekt.geometrie)\n" + 
                "  ) AS bar\n" + 
                "  GROUP BY\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,\n" + 
                "    area\n" + 
                ") AS foo\n" + 
                "ORDER BY\n" + 
                "  foo.typ_kt\n" + 
                ";\n" + 
                "";
        
        RowMapper<ROL> rowMapper = new BeanPropertyRowMapper<ROL>(ROL.class);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("egrid", egrid);
        List<ROL> restrictionOnLandownership = jdbcTemplate.query(sql, namedParameters, rowMapper);
                
        return restrictionOnLandownership;
    }

    @Override
    public List<ROL> getErschliessungPunktByEgrid(String egrid) {
        String sql = "WITH parcel AS (\n" + 
                "  SELECT\n" + 
                "     flaechenmass AS area,\n" + 
                "     geometrie AS geometry\n" + 
                "  FROM\n" + 
                "    agi_mopublic_pub.mopublic_grundstueck\n" + 
                "  WHERE\n" + 
                "    egrid = 'CH328910320651'\n" + 
                ")\n" + 
                "SELECT\n" + 
                "  foo.t_id,\n" + 
                "  'LandUsePlans' AS theme,\n" + 
                "  'Erschliessung (Punktobjekte)' AS subtheme,\n" + 
                "  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,\n" + 
                "  'inForce' AS lawstatuscode,\n" + 
                "  foo.typ_bezeichnung AS information,\n" + 
                "  nrofpoints,\n" + 
                "  area AS parcelarea,\n" + 
                "  geometrytype(foo.geom)\n" + 
                "FROM\n" + 
                "(\n" + 
                "  SELECT\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,  \n" + 
                "    geom,\n" + 
                "    count(geom) AS nrofpoints,\n" + 
                "    area\n" + 
                "  FROM\n" + 
                "  (\n" + 
                "    SELECT\n" + 
                "      erschliessungPunktobjekt.t_id,\n" + 
                "      erschliessungPunktobjekt.typ_kt,\n" + 
                "      erschliessungPunktobjekt.typ_code_kommunal,\n" + 
                "      erschliessungPunktobjekt.typ_bezeichnung,\n" + 
                "      ST_Intersection(parcel.geometry, erschliessungPunktobjekt.geometrie) AS geom,\n" + 
                "      area\n" + 
                "    FROM\n" + 
                "      parcel\n" + 
                "      INNER JOIN arp_npl_pub.nutzungsplanung_erschliessung_punktobjekt AS erschliessungPunktobjekt\n" + 
                "      ON ST_Intersects(parcel.geometry, erschliessungPunktobjekt.geometrie)\n" + 
                "  ) AS bar\n" + 
                "  GROUP BY\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,\n" + 
                "    bar.geom,\n" + 
                "    area\n" + 
                ") AS foo\n" + 
                "ORDER BY\n" + 
                "  foo.typ_kt\n" + 
                ";";

        RowMapper<ROL> rowMapper = new BeanPropertyRowMapper<ROL>(ROL.class);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("egrid", egrid);
        List<ROL> restrictionOnLandownership = jdbcTemplate.query(sql, namedParameters, rowMapper);
                
        return restrictionOnLandownership;
    }

    @Override
    public List<ROL> getEmpfindlichkeitsstufenByEgrid(String egrid) {
        String sql = "WITH parcel AS (\n" + 
                "  SELECT\n" + 
                "     flaechenmass AS area,\n" + 
                "     geometrie AS geometry\n" + 
                "  FROM\n" + 
                "    agi_mopublic_pub.mopublic_grundstueck\n" + 
                "  WHERE\n" + 
                "    egrid = :egrid\n" + 
                ")\n" + 
                "SELECT\n" + 
                "  foo.t_id,\n" + 
                "  'NoiseSensitivityLevels' AS theme,\n" + 
                "  'Lärmempfindlichkeitsstufen (in Nutzungszonen)' AS subtheme,\n" + 
                "  substring(foo.typ_kt FROM 1 FOR 4) AS typecode,\n" + 
                "  'inForce' AS lawstatuscode,\n" + 
                "  foo.typ_bezeichnung AS information,\n" + 
                "  ST_Area(foo.geom) AS areashare,\n" + 
                "  area AS parcelarea,\n" + 
                "  geometrytype(foo.geom)\n" +                 
                "FROM\n" + 
                "(\n" + 
                "  SELECT\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,    \n" + 
                "    ST_Union(geom) AS geom,\n" + 
                "    area\n" + 
                "  FROM\n" + 
                "  (\n" + 
                "    SELECT\n" + 
                "      ueberlagerndeFestlegung.t_id,\n" + 
                "      ueberlagerndeFestlegung.typ_kt,\n" + 
                "      ueberlagerndeFestlegung.typ_code_kommunal,\n" + 
                "      ueberlagerndeFestlegung.typ_bezeichnung,\n" + 
                "      (ST_Dump(ST_CollectionExtract(ST_Intersection(parcel.geometry, ueberlagerndeFestlegung.geometrie), 3))).geom,\n" + 
                "      area\n" + 
                "    FROM\n" + 
                "      parcel\n" + 
                "      INNER JOIN arp_npl_pub.nutzungsplanung_ueberlagernd_flaeche AS ueberlagerndeFestlegung\n" + 
                "      ON ST_Intersects(parcel.geometry, ueberlagerndeFestlegung.geometrie)\n" + 
                "  ) AS bar\n" + 
                "  GROUP BY\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,\n" + 
                "    area\n" + 
                ") AS foo\n" + 
                "WHERE\n" +
                "    substring(foo.typ_kt FROM 1 FOR 4) IN ('N680','N681','N682','N683','N684','N685','N686')\n" +
                "ORDER BY\n" + 
                "  foo.typ_kt\n" + 
                ";";    
        
        RowMapper<ROL> rowMapper = new BeanPropertyRowMapper<ROL>(ROL.class);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("egrid", egrid);
        List<ROL> restrictionOnLandownership = jdbcTemplate.query(sql, namedParameters, rowMapper);
        
        return restrictionOnLandownership;
    }

}
