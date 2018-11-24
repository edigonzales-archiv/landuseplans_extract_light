
package net.opengis.gml.v_3_2_1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * This abstract type supplies the root or base type for all topological elements including primitives and complexes. It inherits AbstractGMLType and hence can be identified using the gml:id attribute.
 * 
 * <p>Java class for AbstractTopologyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AbstractTopologyType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://www.opengis.net/gml/3.2}AbstractGMLType"&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractTopologyType")
@XmlSeeAlso({
    AbstractTopoPrimitiveType.class,
    TopoPointType.class,
    TopoCurveType.class,
    TopoSurfaceType.class,
    TopoVolumeType.class,
    TopoComplexType.class
})
public abstract class AbstractTopologyType
    extends AbstractGMLType
{


}
