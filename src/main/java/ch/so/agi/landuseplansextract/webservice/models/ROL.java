package ch.so.agi.landuseplansextract.webservice.models;

public class ROL {
    private int t_id;

    private String information;
   
    private String theme;
    
    private String subTheme;
    
    private String typeCode;
    
    private String lawStatusCode;
    
    private int areaShare;
    
    private int parcelArea;
    
    private int lengthShare;
    
    private int nrOfPoints;
    
    private double partInPercent;
    
    private String geometryType;

    public int getT_id() {
        return t_id;
    }

    public void setT_id(int t_id) {
        this.t_id = t_id;
    }
    
    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getSubTheme() {
        return subTheme;
    }

    public void setSubTheme(String subTheme) {
        this.subTheme = subTheme;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getLawStatusCode() {
        return lawStatusCode;
    }

    public void setLawStatusCode(String lawStatusCode) {
        this.lawStatusCode = lawStatusCode;
    }

    public int getAreaShare() {
        return areaShare;
    }

    public void setAreaShare(int areaShare) {
        this.areaShare = areaShare;
    }

    public int getParcelArea() {
        return parcelArea;
    }

    public void setParcelArea(int parcelArea) {
        this.parcelArea = parcelArea;
    }

    public int getLengthShare() {
        return lengthShare;
    }

    public void setLengthShare(int lengthShare) {
        this.lengthShare = lengthShare;
    }

    public int getNrOfPoints() {
        return nrOfPoints;
    }

    public void setNrOfPoints(int nrOfPoints) {
        this.nrOfPoints = nrOfPoints;
    }

    public double getPartInPercent() {
        return partInPercent;
    }

    public void setPartInPercent(double partInPercent) {
        this.partInPercent = partInPercent;
    }

    public String getGeometryType() {
        return geometryType;
    }

    public void setGeometryType(String geometryType) {
        this.geometryType = geometryType;
    }
}
