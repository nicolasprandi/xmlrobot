<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015 Nicolas Prandstaetter. All rights reserved. 
 -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
<xsl:template name="get_name_mv">
  <xsl:choose>
    <xsl:when test="$mv_style='name'">
      <xsl:value-of select="$obj_tech_name2" />    
    </xsl:when>
    <xsl:when test="$mv_style='incog'">
      <xsl:text>_</xsl:text><xsl:value-of select="$obj_typ" />
    </xsl:when>    
  </xsl:choose>
</xsl:template>

<xsl:template name="get_name_field">
  <xsl:choose>
    <xsl:when test="$mv_style='name'">
      <xsl:value-of select="@tech_name" />    
    </xsl:when>
    <xsl:when test="$mv_style='incog'">
      <xsl:text>_</xsl:text><xsl:value-of select="@attr_typ" />
    </xsl:when>    
  </xsl:choose>
</xsl:template>

<xsl:template name="get_name_field_au">
  <xsl:choose>
    <xsl:when test="$mv_style='name'">
      <xsl:value-of select="@attr" />    
    </xsl:when>
    <xsl:when test="$mv_style='incog'">
      <xsl:text>_</xsl:text><xsl:value-of select="@attr_typ" />
    </xsl:when>    
  </xsl:choose>
</xsl:template>

<xsl:template name="get_name_obj_au">
  <xsl:choose>
    <xsl:when test="$mv_style='name'">
      <xsl:value-of select="@obj" />    
    </xsl:when>
    <xsl:when test="$mv_style='incog'">
      <xsl:text>_</xsl:text><xsl:value-of select="@obj_typ" />
    </xsl:when>    
  </xsl:choose>
</xsl:template>

</xsl:stylesheet>