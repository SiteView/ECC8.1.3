/**
 * DeviceIdStruct.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cwmp_1_1.dslforum_org;

public class DeviceIdStruct  implements java.io.Serializable {
    private java.lang.String manufacturer;

    private java.lang.String OUI;

    private java.lang.String productClass;

    private java.lang.String serialNumber;

    public DeviceIdStruct() {
    }

    public DeviceIdStruct(
           java.lang.String manufacturer,
           java.lang.String OUI,
           java.lang.String productClass,
           java.lang.String serialNumber) {
           this.manufacturer = manufacturer;
           this.OUI = OUI;
           this.productClass = productClass;
           this.serialNumber = serialNumber;
    }


    /**
     * Gets the manufacturer value for this DeviceIdStruct.
     * 
     * @return manufacturer
     */
    public java.lang.String getManufacturer() {
        return manufacturer;
    }


    /**
     * Sets the manufacturer value for this DeviceIdStruct.
     * 
     * @param manufacturer
     */
    public void setManufacturer(java.lang.String manufacturer) {
        this.manufacturer = manufacturer;
    }


    /**
     * Gets the OUI value for this DeviceIdStruct.
     * 
     * @return OUI
     */
    public java.lang.String getOUI() {
        return OUI;
    }


    /**
     * Sets the OUI value for this DeviceIdStruct.
     * 
     * @param OUI
     */
    public void setOUI(java.lang.String OUI) {
        this.OUI = OUI;
    }


    /**
     * Gets the productClass value for this DeviceIdStruct.
     * 
     * @return productClass
     */
    public java.lang.String getProductClass() {
        return productClass;
    }


    /**
     * Sets the productClass value for this DeviceIdStruct.
     * 
     * @param productClass
     */
    public void setProductClass(java.lang.String productClass) {
        this.productClass = productClass;
    }


    /**
     * Gets the serialNumber value for this DeviceIdStruct.
     * 
     * @return serialNumber
     */
    public java.lang.String getSerialNumber() {
        return serialNumber;
    }


    /**
     * Sets the serialNumber value for this DeviceIdStruct.
     * 
     * @param serialNumber
     */
    public void setSerialNumber(java.lang.String serialNumber) {
        this.serialNumber = serialNumber;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DeviceIdStruct)) return false;
        DeviceIdStruct other = (DeviceIdStruct) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.manufacturer==null && other.getManufacturer()==null) || 
             (this.manufacturer!=null &&
              this.manufacturer.equals(other.getManufacturer()))) &&
            ((this.OUI==null && other.getOUI()==null) || 
             (this.OUI!=null &&
              this.OUI.equals(other.getOUI()))) &&
            ((this.productClass==null && other.getProductClass()==null) || 
             (this.productClass!=null &&
              this.productClass.equals(other.getProductClass()))) &&
            ((this.serialNumber==null && other.getSerialNumber()==null) || 
             (this.serialNumber!=null &&
              this.serialNumber.equals(other.getSerialNumber())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getManufacturer() != null) {
            _hashCode += getManufacturer().hashCode();
        }
        if (getOUI() != null) {
            _hashCode += getOUI().hashCode();
        }
        if (getProductClass() != null) {
            _hashCode += getProductClass().hashCode();
        }
        if (getSerialNumber() != null) {
            _hashCode += getSerialNumber().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DeviceIdStruct.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:dslforum-org:cwmp-1-1", "DeviceIdStruct"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("manufacturer");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Manufacturer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("OUI");
        elemField.setXmlName(new javax.xml.namespace.QName("", "OUI"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("productClass");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ProductClass"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serialNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("", "SerialNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("manufacturer=");
		sb.append(this.manufacturer);
		sb.append("#OUI=");
		sb.append(this.OUI);
		sb.append("#productClass=");
		sb.append(this.productClass);
		sb.append("#serialNumber=");
		sb.append(this.serialNumber);
		return sb.toString();
	}
	private boolean equalsMyDefine(DeviceIdStruct deviceId){
		if (deviceId == null) return false;
		if (deviceId.getManufacturer() == null || deviceId.getOUI() == null || deviceId.getProductClass() == null || deviceId.getSerialNumber() == null) return false;
		if (
				deviceId.getManufacturer().equals(this.getManufacturer())
				&& deviceId.getOUI().equals(this.getOUI())
				&& deviceId.getProductClass().equals(this.getProductClass())
				&& deviceId.getSerialNumber().equals(this.getSerialNumber())
		) return true;
		return false;
	}

}
