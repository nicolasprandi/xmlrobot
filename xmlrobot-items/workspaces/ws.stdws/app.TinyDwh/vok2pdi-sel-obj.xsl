<?xml version="1.0" encoding="UTF-8"?>

<!-- Copyright © 2018-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="obj_typ" />
<xsl:param name="usage"/>  

<xsl:template match="/">
<sel>
      <xsl:apply-templates select="root/ots/ot">
      </xsl:apply-templates>
</sel>
</xsl:template>

<xsl:template match="ot">
  <sel>
  
    <xsl:if test="$usage='tc'">
      <xsl:attribute name="label">
        <xsl:value-of select="@tech_name"/>
      </xsl:attribute>    
    </xsl:if>
    <xsl:if test="$usage='nm'">
      <xsl:attribute name="label">
        <xsl:value-of select="@name"/>
      </xsl:attribute>   
    </xsl:if>  

    <xsl:value-of select="@obj_typ" />
  </sel>
</xsl:template>



</xsl:stylesheet>

  
