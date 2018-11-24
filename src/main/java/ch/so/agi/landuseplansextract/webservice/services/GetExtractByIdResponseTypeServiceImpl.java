package ch.so.agi.landuseplansextract.webservice.services;

import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.admin.geo.schemas.v_d.oereb._1_0.extract.GetExtractByIdResponseType;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.Extract;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.RealEstateDPR;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.RealEstateType;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.CantonCode;
import ch.so.agi.landuseplansextract.webservice.models.Parcel;
import ch.so.agi.landuseplansextract.webservice.models.ROL;
import ch.so.agi.landuseplansextract.webservice.dao.ParcelDAOImpl;
import ch.so.agi.landuseplansextract.webservice.dao.RestrictionOnLandownershipDAOImpl;

@Service
public class GetExtractByIdResponseTypeServiceImpl implements GetExtractByIdResponseTypeService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ParcelDAOImpl parcelDAO;
    
    @Autowired
    private RestrictionOnLandownershipDAOImpl restrictionOnLandownershipDAO;

    @Override
    public GetExtractByIdResponseType getExtractById(String egrid) throws DatatypeConfigurationException {
        ch.admin.geo.schemas.v_d.oereb._1_0.extract.ObjectFactory objectFactoryExtract = 
                new ch.admin.geo.schemas.v_d.oereb._1_0.extract.ObjectFactory();
        
        ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.ObjectFactory objectFactoryExtractData =
                new ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.ObjectFactory();

        // Root element of response type.
        GetExtractByIdResponseType extractByIdResponseType = objectFactoryExtract.createGetExtractByIdResponseType();

        // Root element of extract type.
        Extract extract = objectFactoryExtractData.createExtract();

        // Information about parcel.
        Parcel parcel = parcelDAO.getParcelByEgrid(egrid);
        
        // RealEstateDPRType
        RealEstateDPR realEstateDPR = objectFactoryExtractData.createRealEstateDPR();
        realEstateDPR.setEGRID(parcel.getEgrid());
        realEstateDPR.setCanton(CantonCode.SO);
        realEstateDPR.setFosNr(parcel.getMunicipalityCode());
        realEstateDPR.setIdentDN(parcel.getIdentdn());
        realEstateDPR.setLandRegistryArea(parcel.getArea());
        realEstateDPR.setMunicipality(parcel.getMunicipality());
        realEstateDPR.setNumber(parcel.getNumber());
        realEstateDPR.setSubunitOfLandRegister(parcel.getSubunitOfLandRegister());
        realEstateDPR.setType(RealEstateType.fromValue(parcel.getRealEstateType()));
        
        List<ROL> restrictionOnLandownershipList = restrictionOnLandownershipDAO.getRestrictionOnLandownershipByEgrid(egrid);
        for (ROL restrictionOnLandownership : restrictionOnLandownershipList) {
            log.info(restrictionOnLandownership.getInformation());
        }
        
        log.info(String.valueOf(restrictionOnLandownershipList.size()));

        
        extract.setRealEstate(realEstateDPR);
        
        

        // Creation date
        GregorianCalendar cal = new GregorianCalendar();
        DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        XMLGregorianCalendar creationDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        extract.setCreationDate(creationDate);
        


        extract.setIsReduced(true);
        extractByIdResponseType.setExtract(extract);
        return extractByIdResponseType;
    }

}
