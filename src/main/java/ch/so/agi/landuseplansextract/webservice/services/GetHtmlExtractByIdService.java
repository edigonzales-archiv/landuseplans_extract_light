package ch.so.agi.landuseplansextract.webservice.services;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import net.sf.saxon.s9api.SaxonApiException;

public interface GetHtmlExtractByIdService {
    File getExtract(String egrid) throws IOException, DatatypeConfigurationException, JAXBException, SaxonApiException;
}
