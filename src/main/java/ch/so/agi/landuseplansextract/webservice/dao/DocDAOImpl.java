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

import ch.so.agi.landuseplansextract.webservice.models.Doc;

@Transactional
@Repository
public class DocDAOImpl implements DocDAO {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Doc> getDocumentsByRestrictionOnLandownershipId(int t_id) {
        String sql = "WITH docs AS \n" + 
                "(\n" + 
                "SELECT\n" + 
                "  json_array_elements(dok_id::json) AS obj,\n" + 
                "  dok_id::json \n" + 
                "FROM\n" + 
                "  arp_npl_pub.nutzungsplanung_grundnutzung\n" + 
                "WHERE\n" + 
                "  t_id = :t_id\n" + 
                "UNION ALL\n" + 
                "SELECT\n" + 
                "  json_array_elements(dok_id::json) AS obj,\n" + 
                "  dok_id::json \n" + 
                "FROM\n" + 
                "  arp_npl_pub.nutzungsplanung_ueberlagernd_flaeche\n" + 
                "WHERE\n" + 
                "  t_id = :t_id\n" + 
                "UNION ALL\n" + 
                "SELECT\n" + 
                "  json_array_elements(dok_id::json) AS obj,\n" + 
                "  dok_id::json \n" + 
                "FROM\n" + 
                "  arp_npl_pub.nutzungsplanung_ueberlagernd_linie\n" + 
                "WHERE\n" + 
                "  t_id = :t_id\n" + 
                "UNION ALL\n" + 
                "SELECT\n" + 
                "  json_array_elements(dok_id::json) AS obj,\n" + 
                "  dok_id::json \n" + 
                "FROM\n" + 
                "  arp_npl_pub.nutzungsplanung_ueberlagernd_punkt\n" + 
                "WHERE\n" + 
                "  t_id = :t_id\n" + 
                "UNION ALL\n" + 
                "SELECT\n" + 
                "  json_array_elements(dok_id::json) AS obj,\n" + 
                "  dok_id::json \n" + 
                "FROM\n" + 
                "  arp_npl_pub.nutzungsplanung_erschliessung_flaechenobjekt\n" + 
                "WHERE\n" + 
                "  t_id = :t_id\n" + 
                "UNION ALL\n" + 
                "SELECT\n" + 
                "  json_array_elements(dok_id::json) AS obj,\n" + 
                "  dok_id::json \n" + 
                "FROM\n" + 
                "  arp_npl_pub.nutzungsplanung_erschliessung_linienobjekt\n" + 
                "WHERE\n" + 
                "  t_id = :t_id\n" + 
                "UNION ALL\n" + 
                "SELECT\n" + 
                "  json_array_elements(dok_id::json) AS obj,\n" + 
                "  dok_id::json \n" + 
                "FROM\n" + 
                "  arp_npl_pub.nutzungsplanung_erschliessung_punktobjekt\n" + 
                "WHERE\n" + 
                "  t_id = :t_id\n" + 
                ") \n" + 
                "SELECT\n" + 
                "  obj->>'titel' AS title,\n" + 
                "  obj->>'offiziellertitel' AS officialtitle,\n" + 
                "  obj->>'abkuerzung' AS abbreviation,\n" + 
                "  obj->>'offiziellenr' AS officialnumber,\n" + 
                "  CAST(obj->>'gemeinde' AS integer) AS municipality,\n" + 
                "  obj->>'publiziertab' AS publishedat,\n" + 
                "  'inForce' AS lawstatuscode,\n" + 
                "  obj->>'textimweb_absolut' AS textatweb,\n" + 
                "  'LegalProvision' AS documenttype\n" + 
                "FROM\n" + 
                "  docs\n" + 
                ";";
        
        RowMapper<Doc> rowMapper = new BeanPropertyRowMapper<Doc>(Doc.class);
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("t_id", t_id);
        List<Doc> docs = jdbcTemplate.query(sql, namedParameters, rowMapper);
                
        return docs;
    }

}
