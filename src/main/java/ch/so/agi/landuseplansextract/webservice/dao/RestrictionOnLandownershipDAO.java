package ch.so.agi.landuseplansextract.webservice.dao;

import java.util.List;

import ch.so.agi.landuseplansextract.webservice.models.ROL;

public interface RestrictionOnLandownershipDAO {
    /** 
     * Finds restrictions on landownership (land use plans only) by a given egrid.
     * 
     * @param egrid Egrid 
     * @return RestrictionOnLandownership 
     */
    List<ROL> getRestrictionOnLandownershipByEgrid(String egrid);
}
