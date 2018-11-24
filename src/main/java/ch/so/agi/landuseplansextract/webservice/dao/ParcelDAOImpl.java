package ch.so.agi.landuseplansextract.webservice.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ch.so.agi.landuseplansextract.webservice.models.Parcel;

@Transactional
@Repository
public class ParcelDAOImpl implements ParcelDAO {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    @Override
    public Parcel getParcelByEgrid(String egrid) {
        String sql = "SELECT\n" + 
                "  grundstueck.t_id,\n" + 
                "  nummer AS number,\n" + 
                "  grundstueck.nbident AS identdn,\n" + 
                "  CASE \n" + 
                "    WHEN art_txt = 'Liegenschaft' THEN 'RealEstate'\n" + 
                "    ELSE 'Distinct_and_permanent_rights.BuildingRight'\n" + 
                "  END AS realestatetype,\n" + 
                "  egrid AS egrid,\n" + 
                "  bfs_nr AS municipalitycode,\n" + 
                "  flaechenmass AS area,\n" + 
                "  'valid' AS validitytype,\n" + 
                "  ST_AsBinary(grundstueck.geometrie) AS geomwkb,\n" + 
                "  ST_AsText(grundstueck.geometrie) AS geomwkt,\n" + 
                "  gemeindegrenze.gemeindename AS municipality,\n" + 
                "  grundbuchkreis.aname AS subunitOfLandRegister\n" + 
                "FROM\n" + 
                "  agi_mopublic_pub.mopublic_grundstueck AS grundstueck\n" + 
                "  LEFT JOIN agi_hoheitsgrenzen_pub.hoheitsgrenzen_gemeindegrenze AS gemeindegrenze\n" + 
                "  ON ST_Intersects(ST_PointOnSurface(grundstueck.geometrie), gemeindegrenze.geometrie)\n" + 
                "  LEFT JOIN agi_av_gb_admin_einteilung_pub.grundbuchkreise_grundbuchkreis AS grundbuchkreis\n" + 
                "  ON ST_Intersects(ST_PointOnSurface(grundstueck.geometrie), grundbuchkreis.perimeter)\n" + 
                "WHERE\n" + 
                "  egrid = :egrid";
        
        RowMapper<Parcel> rowMapper = new BeanPropertyRowMapper<Parcel>(Parcel.class);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("egrid", egrid);
        Parcel parcel = jdbcTemplate.queryForObject(sql, namedParameters, rowMapper);
        return parcel;
    }

    @Override
    public List<Parcel> getParcelByXY(double easting, double northing) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Parcel> getParcelByGNSS(double latitude, double longitude) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Parcel> getParcelByNumberAndIdentDN(String number, String identdn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Parcel> getParcelByPostalcodeAndLocalisationAndHousingNumber(int postalcode, String localisation,
            String housingNumber) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Parcel> getParcelByPostalcodeAndLocalisation(int postalcode, String localisation) {
        // TODO Auto-generated method stub
        return null;
    }
}
