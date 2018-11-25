package ch.so.agi.landuseplansextract.webservice.services;

import java.util.List;

import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.RestrictionOnLandownership;

public interface RestrictionOnLandownershipService {
    List<RestrictionOnLandownership> getRestrictionOnLandownership(String egrid);
}
