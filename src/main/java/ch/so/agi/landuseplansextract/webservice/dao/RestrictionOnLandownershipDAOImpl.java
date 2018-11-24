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
    public List<ROL> getRestrictionOnLandownershipByEgrid(String egrid) {
        String sql = "WITH parcel AS (\n" + 
                "  SELECT\n" + 
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
                "  ST_Area(foo.geom) AS areashare\n" + 
                "FROM\n" + 
                "(\n" + 
                "  SELECT\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung,    \n" + 
                "    ST_Union(geom) AS geom\n" + 
                "  FROM\n" + 
                "  (\n" + 
                "    SELECT\n" + 
                "      grundnutzung.t_id,\n" + 
                "      grundnutzung.typ_kt,\n" + 
                "      grundnutzung.typ_code_kommunal,\n" + 
                "      grundnutzung.typ_bezeichnung,\n" + 
                "      (ST_Dump(ST_CollectionExtract(ST_Intersection(parcel.geometry, grundnutzung.geometrie), 3))).geom \n" + 
                "    FROM\n" + 
                "      parcel\n" + 
                "      LEFT JOIN arp_npl_pub.nutzungsplanung_grundnutzung AS grundnutzung\n" + 
                "      ON ST_Intersects(parcel.geometry, grundnutzung.geometrie)\n" + 
                "  ) AS bar\n" + 
                "  GROUP BY\n" + 
                "    bar.t_id,\n" + 
                "    bar.typ_kt,\n" + 
                "    bar.typ_code_kommunal,\n" + 
                "    bar.typ_bezeichnung\n" + 
                ") AS foo\n" + 
                "ORDER BY\n" + 
                "  foo.typ_kt\n" + 
                ";";
        
        RowMapper<ROL> rowMapper = new BeanPropertyRowMapper<ROL>(ROL.class);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("egrid", egrid);
        List<ROL> restrictionOnLandownership = jdbcTemplate.query(sql, namedParameters, rowMapper);
                
        return restrictionOnLandownership;
    }

}
