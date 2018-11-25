package ch.so.agi.landuseplansextract.webservice.dao;

import java.util.List;

import ch.so.agi.landuseplansextract.webservice.models.Doc;

public interface DocDAO {
    /** 
     * Finds documents by a given restriction on landownership (database) identifier.
     * 
     * @param t_id Identifiere 
     * @return Doc
     */
    List<Doc> getDocumentsByRestrictionOnLandownershipId(int t_id);
}
