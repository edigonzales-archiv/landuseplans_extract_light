package ch.so.agi.landuseplansextract.webservice.models;

public class Doc {
    private int t_id;
    
    private String title;
    
    private String officialtitle;
    
    private String abbreviation;
    
    private String officialnumber;
    
    private int municipality;
    
    private String publishedat;
    
    private String lawstatuscode;
    
    private String textatweb;
    
    private String documenttype;

    public int getT_id() {
        return t_id;
    }

    public void setT_id(int t_id) {
        this.t_id = t_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOfficialtitle() {
        return officialtitle;
    }

    public void setOfficialtitle(String officialtitle) {
        this.officialtitle = officialtitle;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getOfficialnumber() {
        return officialnumber;
    }

    public void setOfficialnumber(String officialnumber) {
        this.officialnumber = officialnumber;
    }

    public int getMunicipality() {
        return municipality;
    }

    public void setMunicipality(int municipality) {
        this.municipality = municipality;
    }

    public String getPublishedat() {
        return publishedat;
    }

    public void setPublishedat(String publishedat) {
        this.publishedat = publishedat;
    }

    public String getLawstatuscode() {
        return lawstatuscode;
    }

    public void setLawstatuscode(String lawstatuscode) {
        this.lawstatuscode = lawstatuscode;
    }

    public String getTextatweb() {
        return textatweb;
    }

    public void setTextatweb(String textatweb) {
        this.textatweb = textatweb;
    }

    public String getDocumenttype() {
        return documenttype;
    }

    public void setDocumenttype(String documenttype) {
        this.documenttype = documenttype;
    }
}
