package ch.so.agi.landuseplansextract.webservice.dao;

import java.util.List;

import ch.so.agi.landuseplansextract.webservice.models.ROL;

public interface RestrictionOnLandownershipDAO {
    /** 
     * Finds restrictions on landownership (Grundnutzung) by a given egrid.
     * 
     * @param egrid Egrid 
     * @return RestrictionOnLandownership 
     */
    List<ROL> getGrundnutzungByEgrid(String egrid);
    
    /** 
     * Finds restrictions on landownership (Ueberlagernde_Festlegung) by a given egrid.
     * 
     * @param egrid Egrid 
     * @return RestrictionOnLandownership 
     */
    List<ROL> getUeberlagerndeFestlegungByEgrid(String egrid);

    /** 
     * Finds restrictions on landownership (Linienbezogene Festlegung) by a given egrid.
     * 
     * @param egrid Egrid 
     * @return RestrictionOnLandownership 
     */
    List<ROL> getLinienbezogeneFestlegungByEgrid(String egrid);

    /** 
     * Finds restrictions on landownership (Objektbezogene Festlegung) by a given egrid.
     * 
     * @param egrid Egrid 
     * @return RestrictionOnLandownership 
     */
    List<ROL> getObjektbezogeneFestlegungByEgrid(String egrid);

    /** 
     * Finds restrictions on landownership (Erschliessung Flaechenobjekt) by a given egrid.
     * 
     * @param egrid Egrid 
     * @return RestrictionOnLandownership 
     */
    List<ROL> getErschliessungFlaecheByEgrid(String egrid);

    /** 
     * Finds restrictions on landownership (Erschliessung Linienobjekt) by a given egrid.
     * 
     * @param egrid Egrid 
     * @return RestrictionOnLandownership 
     */
    List<ROL> getErschliessungLinieByEgrid(String egrid);

    /** 
     * Finds restrictions on landownership (Erschliessung Punktobjekt) by a given egrid.
     * 
     * @param egrid Egrid 
     * @return RestrictionOnLandownership 
     */
    List<ROL> getErschliessungPunktByEgrid(String egrid);

}
