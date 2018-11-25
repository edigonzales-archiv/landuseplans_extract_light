package ch.so.agi.landuseplansextract.webservice.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.CantonCode;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.Document;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.LanguageCode;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.Lawstatus;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.LawstatusCode;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.LocalisedMText;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.LocalisedText;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.LocalisedUri;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.MultilingualMText;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.MultilingualText;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.MultilingualUri;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.ObjectFactory;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.RestrictionOnLandownership;
import ch.admin.geo.schemas.v_d.oereb._1_0.extractdata.Theme;
import ch.so.agi.landuseplansextract.webservice.dao.DocDAOImpl;
import ch.so.agi.landuseplansextract.webservice.dao.RestrictionOnLandownershipDAOImpl;
import ch.so.agi.landuseplansextract.webservice.models.Doc;
import ch.so.agi.landuseplansextract.webservice.models.ROL;

@Service
public class RestrictionOnLandownershipServiceImpl implements RestrictionOnLandownershipService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestrictionOnLandownershipDAOImpl restrictionOnLandownershipDAO;

    @Autowired
    private DocDAOImpl docDAO;

    @Override
    public List<RestrictionOnLandownership> getRestrictionOnLandownership(String egrid) {
        ObjectFactory objectFactory = new ObjectFactory();
        List<RestrictionOnLandownership> restrictionOnLandownershipList = new ArrayList<RestrictionOnLandownership>();
        
        List<ROL> rolListGrundnutzung = restrictionOnLandownershipDAO.getGrundnutzungByEgrid(egrid);
        List<ROL> rolListUeberlagerndFestlegung = restrictionOnLandownershipDAO.getUeberlagerndeFestlegungByEgrid(egrid);
        List<ROL> rolListLinienbezogeneFestlegung = restrictionOnLandownershipDAO.getLinienbezogeneFestlegungByEgrid(egrid);
        List<ROL> rolListObjektbezogeneFestlegung = restrictionOnLandownershipDAO.getObjektbezogeneFestlegungByEgrid(egrid);
        List<ROL> rolListErschliessungFlaeche = restrictionOnLandownershipDAO.getErschliessungFlaecheByEgrid(egrid);
        List<ROL> rolListErschliessungLinie = restrictionOnLandownershipDAO.getErschliessungLinieByEgrid(egrid);
        List<ROL> rolListErschliessungPunkt = restrictionOnLandownershipDAO.getErschliessungPunktByEgrid(egrid);
        
        List<ROL> rolList = new ArrayList<ROL>();
        rolList.addAll(rolListGrundnutzung);
        rolList.addAll(rolListUeberlagerndFestlegung);
        rolList.addAll(rolListLinienbezogeneFestlegung);
        rolList.addAll(rolListObjektbezogeneFestlegung);
        rolList.addAll(rolListErschliessungFlaeche);
        rolList.addAll(rolListErschliessungLinie);
        rolList.addAll(rolListErschliessungPunkt);
       
        for (ROL rol : rolList) {
            log.info(rol.getInformation());
            RestrictionOnLandownership restrictionOnLandownership = objectFactory.createRestrictionOnLandownership();
            
            if (rol.getGeometryType().contains("POLYGON")) {
                restrictionOnLandownership.setAreaShare(rol.getAreaShare());
            } else if (rol.getGeometryType().contains("LINESTRING")) {
                restrictionOnLandownership.setLengthShare(rol.getLengthShare());
            } else if (rol.getGeometryType().contains("POINT")) {
                restrictionOnLandownership.setNrOfPoints(rol.getNrOfPoints());
            }
            
            LocalisedMText localisedMText = objectFactory.createLocalisedMText();
            localisedMText.setLanguage(LanguageCode.fromValue("de"));
            localisedMText.setText(rol.getInformation());
            MultilingualMText multilingualMText = objectFactory.createMultilingualMText();
            multilingualMText.getLocalisedText().add(localisedMText);
            restrictionOnLandownership.setInformation(multilingualMText);
            
            Lawstatus lawStatus = objectFactory.createLawstatus();
            lawStatus.setCode(LawstatusCode.fromValue(rol.getLawStatusCode()));
            
            LocalisedText localisedText = objectFactory.createLocalisedText();
            localisedText.setLanguage(LanguageCode.fromValue("de"));  
            localisedText.setText("in Kraft");
            lawStatus.setText(localisedText);
            restrictionOnLandownership.setLawstatus(lawStatus);
            
            restrictionOnLandownership.setPartInPercent(new BigDecimal(100.0 * rol.getAreaShare() / rol.getParcelArea()));
            restrictionOnLandownership.setSubTheme(rol.getSubTheme());
            
            Theme theme = objectFactory.createTheme();
            theme.setCode(rol.getTheme());
            localisedText.setLanguage(LanguageCode.fromValue("de"));  
            localisedText.setText("Nutzungsplanung");
            theme.setText(localisedText);
            restrictionOnLandownership.setTheme(theme);
            
            restrictionOnLandownership.setTypeCode(rol.getTypeCode());
            
            List<Doc> docList = docDAO.getDocumentsByRestrictionOnLandownershipId(rol.getT_id());
            for (Doc doc : docList) {
                log.info(doc.getOfficialtitle());
                
                Document legalProvision = objectFactory.createDocument();
                
                MultilingualUri multiLingualUri = objectFactory.createMultilingualUri();
                LocalisedUri localisedUri = objectFactory.createLocalisedUri();
                localisedUri.setLanguage(LanguageCode.fromValue("de"));
                localisedUri.setText(doc.getTextatweb());
                multiLingualUri.getLocalisedText().add(localisedUri);
                legalProvision.setTextAtWeb(multiLingualUri);
                
                MultilingualText multilingualText = objectFactory.createMultilingualText();
                localisedText = objectFactory.createLocalisedText();
                localisedText.setLanguage(LanguageCode.fromValue("de"));
                localisedText.setText(doc.getAbbreviation());
                multilingualText.getLocalisedText().add(localisedText);
                legalProvision.setAbbreviation(multilingualText);
                
                legalProvision.setCanton(CantonCode.SO);
                legalProvision.setDocumentType(doc.getDocumenttype());
                
                lawStatus = objectFactory.createLawstatus();
                lawStatus.setCode(LawstatusCode.fromValue(doc.getLawstatuscode()));
                legalProvision.setLawstatus(lawStatus);
                
                legalProvision.setMunicipality(doc.getMunicipality());
                legalProvision.setOfficialNumber(doc.getOfficialnumber());
                
                multilingualText = objectFactory.createMultilingualText();
                localisedText = objectFactory.createLocalisedText();
                localisedText.setLanguage(LanguageCode.fromValue("de"));
                localisedText.setText(doc.getOfficialtitle());
                multilingualText.getLocalisedText().add(localisedText);
                legalProvision.setOfficialTitle(multilingualText);
                
                multilingualText = objectFactory.createMultilingualText();
                localisedText = objectFactory.createLocalisedText();
                localisedText.setLanguage(LanguageCode.fromValue("de"));
                localisedText.setText(doc.getTitle());
                multilingualText.getLocalisedText().add(localisedText);
                legalProvision.setTitle(multilingualText);
                
                restrictionOnLandownership.getLegalProvisions().add(legalProvision);
            }
            restrictionOnLandownershipList.add(restrictionOnLandownership);
        }
        return restrictionOnLandownershipList;
    }
}
