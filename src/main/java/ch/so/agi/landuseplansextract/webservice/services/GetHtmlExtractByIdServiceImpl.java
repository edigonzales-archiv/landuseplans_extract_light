package ch.so.agi.landuseplansextract.webservice.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import ch.admin.geo.schemas.v_d.oereb._1_0.extract.GetExtractByIdResponseType;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

@Service
public class GetHtmlExtractByIdServiceImpl implements GetHtmlExtractByIdService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    GetExtractByIdResponseTypeServiceImpl getExtractByIdResponseTypeService;

    @Override
    public File getExtract(String egrid) throws IOException, DatatypeConfigurationException, JAXBException, SaxonApiException {
        Path tempDir = Files.createTempDirectory("landuseplans_extract_");

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Resource xsltFileResource = resolver.getResource("classpath:xslt/landuseplans_extract_html.xslt");
        InputStream xsltFileInputStream = xsltFileResource.getInputStream();
        File xsltFile = new File(Paths.get(tempDir.toFile().getAbsolutePath(), "landuseplans_extract_html.xslt").toFile().getAbsolutePath());
        Files.copy(xsltFileInputStream, xsltFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        xsltFileInputStream.close();

        File xmlFile = new File(Paths.get(tempDir.toFile().getAbsolutePath(), egrid + ".xml").toFile().getAbsolutePath());
        File htmlFile = new File(Paths.get(tempDir.toFile().getAbsolutePath(), egrid + ".html").toFile().getAbsolutePath());
        log.info(htmlFile.getAbsolutePath());

        // create JAXB object
        GetExtractByIdResponseType getExtractByIdResponseType = getExtractByIdResponseTypeService.getExtractById(egrid);

        // create JAXB context
        JAXBContext jaxbContext = JAXBContext.newInstance(GetExtractByIdResponseType.class);
        
        // create marshaller
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        
        // writes XML file to file-system
        jaxbMarshaller.marshal(getExtractByIdResponseType, xmlFile); 

        // xslt transformation
        Processor proc = new Processor(false);
        XsltCompiler comp = proc.newXsltCompiler();
        XsltExecutable exp = comp.compile(new StreamSource(xsltFile));
        XdmNode source = proc.newDocumentBuilder().build(new StreamSource(xmlFile));
        Serializer out = proc.newSerializer(htmlFile);
        XsltTransformer trans = exp.load();
        trans.setInitialContextNode(source);
        trans.setDestination(out);
        trans.transform();
        
        return htmlFile;
    }

}
