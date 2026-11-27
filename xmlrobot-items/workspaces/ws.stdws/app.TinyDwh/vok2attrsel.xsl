<?xml version="1.0" encoding="UTF-8"?>

<!-- Copyright © 2018-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="obj_typ" /> 
<xsl:param name="nav_naming_attribute" />  

<xsl:template match="/">
<sel>
  <xsl:choose>
    <xsl:when test="string-length($obj_typ) &gt; 0">
      <xsl:apply-templates select="root/aus/au[@obj_typ=$obj_typ]">
        <xsl:sort select="@attr"/>
      </xsl:apply-templates>       
    </xsl:when>
    <xsl:otherwise>
    
      <xsl:choose>
        <xsl:when test="$nav_naming_attribute='1'">
          <!-- name -->
          <xsl:apply-templates select="root/ats/at">
            <xsl:sort select="@name"/>
          </xsl:apply-templates>      
        </xsl:when>
        <xsl:otherwise>
          <!-- technischer feldname -->
        
          <xsl:apply-templates select="root/ats/at">
            <xsl:sort select="@tech_name"/>
          </xsl:apply-templates>     
        </xsl:otherwise>
      </xsl:choose>
      
    </xsl:otherwise>
  </xsl:choose>
</sel>
</xsl:template>

<xsl:template match="au">
  <sel><xsl:value-of select="@attr" /></sel>
</xsl:template>

<xsl:template match="at">
  <sel>
    <xsl:attribute name="label">
      <xsl:choose>
        <xsl:when test="$nav_naming_attribute='1'">
          <xsl:choose>
            <xsl:when test="string-length(@name) &gt; 0">
              <xsl:value-of select="@name" />      
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="@tech_name" /><xsl:text> *</xsl:text>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise>
          <!-- technischer feldname -->
          <xsl:value-of select="@tech_name" />     
        </xsl:otherwise>
      </xsl:choose>  
    </xsl:attribute>
    <xsl:value-of select="@attr_typ" />  
  </sel>
</xsl:template>

</xsl:stylesheet>

  
