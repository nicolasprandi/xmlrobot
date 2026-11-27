<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2023 Nicolas Prandstaetter.  All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="object_typ"/>
<xsl:param name="usage"/>
<xsl:param name="identonly"/>

<xsl:template match="/">
<sels>


<xsl:choose>
  <xsl:when test="$identonly='1'">
    <xsl:apply-templates select="root/aus/au[@obj_typ=$object_typ and @ident='1']"/>
  </xsl:when>
  <xsl:otherwise>
    <xsl:apply-templates select="root/aus/au[@obj_typ=$object_typ]"/>
  </xsl:otherwise>
</xsl:choose> 


</sels>

</xsl:template>

<xsl:template match="au">
  <xsl:call-template name="aus2">
    <xsl:with-param name="attr_typ"><xsl:value-of select="@attr_typ"/></xsl:with-param>
    <xsl:with-param name="ident"><xsl:value-of select="@ident"/></xsl:with-param>
  </xsl:call-template>
</xsl:template>
<xsl:template name="aus2">
  <xsl:param name="attr_typ"/>
  <xsl:param name="ident"/>
  <xsl:apply-templates select="//root/ats/at[@attr_typ=$attr_typ]">
    <xsl:with-param name="ident"><xsl:value-of select="$ident"/></xsl:with-param>
  </xsl:apply-templates>
</xsl:template>


<xsl:template match="at">
  <xsl:param name="ident"/>
  <sel>
    <xsl:attribute name="label">
      <xsl:choose>
      <xsl:when test="$usage='tc'">      
        <xsl:value-of select="@tech_name"/>    
      </xsl:when>
      <xsl:when test="$usage='nm'">
        <xsl:value-of select="@name"/>         
      </xsl:when>
      </xsl:choose>
      <xsl:choose>
        <xsl:when test="$identonly='1'">
          <!-- nur identifizierende sprich gleiches -->
            <xsl:text> *</xsl:text>
        </xsl:when>         
        <xsl:otherwise>
          <xsl:if test="$ident='1'">
            <xsl:text> *</xsl:text>
          </xsl:if>
        </xsl:otherwise>      
      </xsl:choose>

    </xsl:attribute>    
      <xsl:value-of select="@attr_typ"/>          
  </sel>

</xsl:template> 


<xsl:template match="ot">

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
 

</xsl:template> 

</xsl:stylesheet>

  
