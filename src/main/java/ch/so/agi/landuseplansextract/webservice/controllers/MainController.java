package ch.so.agi.landuseplansextract.webservice.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import ch.admin.geo.schemas.v_d.oereb._1_0.extract.GetEGRIDResponseType;
import ch.admin.geo.schemas.v_d.oereb._1_0.extract.GetExtractByIdResponseType;
import ch.so.agi.landuseplansextract.webservice.services.GetExtractByIdResponseTypeServiceImpl;
import ch.so.agi.landuseplansextract.webservice.services.GetHtmlExtractByIdServiceImpl;
import ch.so.agi.landuseplansextract.webservice.services.GetEGRIDResponseTypeServiceImpl;
import net.sf.saxon.s9api.SaxonApiException;

@RestController
public class MainController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private GetEGRIDResponseTypeServiceImpl getEGRIDResponseTypeService;

    @Autowired
    GetExtractByIdResponseTypeServiceImpl getExtractByIdResponseTypeService;
    
    @Autowired
    GetHtmlExtractByIdServiceImpl htmlExtractByIdService;

    @RequestMapping(value="/extract/html/", method=RequestMethod.GET,
            produces={MediaType.TEXT_HTML_VALUE},
            params={"XY"})
    public RedirectView getHtmlExtractByXY (
            @RequestParam(value = "XY") String xy) {
                
        double[] coords = validateCoordinateRequestParam(xy);
        GetEGRIDResponseType getEGRIDResponseType = getEGRIDResponseTypeService.getGetEGRIDResponseTypeByXY(coords[0], coords[1]);
        
        if (getEGRIDResponseType.getEgridAndNumberAndIdentDN().size() == 0) {
            log.warn("No egrid found at: " + xy);
            return null;
        }
        
        String egrid = getEGRIDResponseType.getEgridAndNumberAndIdentDN().get(0).getValue();
        return new RedirectView(contextPath+"/extract/html/"+egrid); 
    }

    @RequestMapping(value="/extract/xml/{egrid:.{14,14}}", method=RequestMethod.GET,
            produces={MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<?> getXmlExtractById (
            @PathVariable("egrid") String egrid) throws DatatypeConfigurationException {        
        GetExtractByIdResponseType getExtractByIdResponseType = getExtractByIdResponseTypeService.getExtractById(egrid);
        return ResponseEntity.ok(getExtractByIdResponseType);
    }
    
    @RequestMapping(value="/extract/html/{egrid:.{14,14}}", method=RequestMethod.GET,
            produces={MediaType.TEXT_HTML_VALUE})
    public ResponseEntity<?> getHtmlExtractById (
            @PathVariable("egrid") String egrid) throws DatatypeConfigurationException, IOException, JAXBException, SaxonApiException {        
        File html = htmlExtractByIdService.getExtract(egrid);
        String content = new String(Files.readAllBytes(html.toPath()));
        return ResponseEntity.ok(content);
    }

    private double[] validateCoordinateRequestParam(String coordinate) {
        String[] parts = coordinate.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Request parameter 'XY' is not in correct format.");
        }
        
        double[] coords = new double[2];
        coords[0] = Double.parseDouble(parts[0]);
        coords[1] = Double.parseDouble(parts[1]);

        return coords;
    }

    @ExceptionHandler({IllegalArgumentException.class, DatatypeConfigurationException.class, IOException.class, JAXBException.class, SaxonApiException.class})
    private ResponseEntity<?> handleBadRequests(Exception e) {
        log.error(e.getMessage());      
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
