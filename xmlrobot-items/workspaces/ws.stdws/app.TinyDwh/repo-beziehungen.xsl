<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2021 Nicolas Prandstaetter.  All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="usage"/>  

<xsl:template match="/">
<bez>
  <ots>
  <xsl:apply-templates select="root/ots/ot"/> 
  </ots>
  
  <bts>
  <xsl:apply-templates select="root/bts/bt"/> 
  </bts>  
</bez>
</xsl:template>

<xsl:template match="bt">
  <bt>
    <xsl:attribute name="tp">
      <xsl:value-of select="@bez_typ"/>
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
    <xsl:attribute name="satz_ab">
      <xsl:value-of select="@satz_ab"/>
    </xsl:attribute>
    <xsl:attribute name="satz_ba">
      <xsl:value-of select="@satz_ba"/>
    </xsl:attribute>    
  </bt>

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

  
