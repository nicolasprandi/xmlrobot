<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2021 Nicolas Prandstaetter.  All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="usage"/> <!-- tech_name name-->

<xsl:template match="/">
<search>
<ots>
<xsl:apply-templates select="root/ots/ot"/> 
</ots>
<ats>
<xsl:apply-templates select="root/ats/at"/> 
</ats>
</search>

</xsl:template>

<xsl:template match="at">
  <at>
    <xsl:attribute name="tp">
      <xsl:value-of select="@attr_typ"/>
    </xsl:attribute>
    <xsl:if test="$usage='tc'">
      <xsl:attribute name="x">
        <xsl:value-of select="@tech_name"/>
      </xsl:attribute>    
    </xsl:if>
    <xsl:if test="$usage='nm'">
      <xsl:attribute name="x">
        <xsl:value-of select="@name"/>
      </xsl:attribute>   
    </xsl:if>
     
    <!--    
    <xsl:attribute name="np">
      <xsl:value-of select="@name_plural"/>
    </xsl:attribute>       
    -->

  </at>

</xsl:template> 

<xsl:template match="ot">
  <ot>
    <xsl:attribute name="tp">
      <xsl:value-of select="@obj_typ"/>
    </xsl:attribute>
    
    <xsl:if test="$usage='tc'">    
      <xsl:attribute name="x">
        <xsl:value-of select="@tech_name"/>
      </xsl:attribute>  
    </xsl:if>
    
    <xsl:if test="$usage='nm'">  
      <xsl:attribute name="x">
        <xsl:value-of select="@name"/>
      </xsl:attribute>  
    </xsl:if>  


  </ot>

</xsl:template>    

</xsl:stylesheet>

  
