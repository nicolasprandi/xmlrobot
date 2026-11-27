<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="max" />
<xsl:param name="computer" />
<xsl:param name="os" />
<xsl:param name="wish" />
<xsl:param name="sheet" />
<xsl:output method="xml" indent="yes"/>
<xsl:template match="/">
  <xsl:apply-templates select="cs"/>
</xsl:template>

<xsl:template match="cs">
  <cs>
    <xsl:copy-of select="@*|coltype|dims"/>    
    <xsl:apply-templates select="unique_rows"/>
  </cs>
</xsl:template>

<xsl:template match="unique_rows">
  <unique_rows>
    <xsl:copy-of select="@*"/>
    <xsl:apply-templates select="coltype_u"/>
  </unique_rows>
</xsl:template>

<xsl:template match="coltype_u">
  <coltype_u>
    <xsl:copy-of select="@*"/>
    <xsl:apply-templates select="uniques"/>
  </coltype_u>
</xsl:template>

<xsl:template match="uniques">
  <uniques>
    <xsl:copy-of select="@*"/>
    <xsl:apply-templates select="unique"/>
  </uniques>
</xsl:template>

<xsl:template match="unique">
  <unique>
    <xsl:copy-of select="@*"/>
    <xsl:attribute name="row_index_u">
      <xsl:value-of select="position()-1" />
    </xsl:attribute>
    <xsl:value-of select="."/>
  </unique>
</xsl:template>

</xsl:stylesheet>

  