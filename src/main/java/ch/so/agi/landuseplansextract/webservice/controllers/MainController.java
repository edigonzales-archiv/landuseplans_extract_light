package ch.so.agi.landuseplansextract.webservice.controllers;

import javax.xml.datatype.DatatypeConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.admin.geo.schemas.v_d.oereb._1_0.extract.GetExtractByIdResponseType;
import ch.so.agi.landuseplansextract.webservice.services.GetExtractByIdResponseTypeServiceImpl;

@RestController
public class MainController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GetExtractByIdResponseTypeServiceImpl getExtractByIdResponseTypeService;

    @RequestMapping(value="/extract/xml/{egrid:.{14,14}}", method=RequestMethod.GET,
            produces={MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getExtractByXY (
            @PathVariable("egrid") String egrid) throws DatatypeConfigurationException {
                     
        log.info("fubar");
        log.info(egrid);
        
        GetExtractByIdResponseType getExtractByIdResponseType = getExtractByIdResponseTypeService.getExtractById(egrid);
        return ResponseEntity.ok(getExtractByIdResponseType);
    }

    @ExceptionHandler({IllegalArgumentException.class, DatatypeConfigurationException.class})
    private ResponseEntity<?> handleBadRequests(Exception e) {
        log.error(e.getMessage());      
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
