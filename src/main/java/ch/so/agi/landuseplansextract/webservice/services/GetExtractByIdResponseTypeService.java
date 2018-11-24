package ch.so.agi.landuseplansextract.webservice.services;

import javax.xml.datatype.DatatypeConfigurationException;

import ch.admin.geo.schemas.v_d.oereb._1_0.extract.GetExtractByIdResponseType;

public interface GetExtractByIdResponseTypeService {
    GetExtractByIdResponseType getExtractById(String egrid) throws DatatypeConfigurationException;
}
