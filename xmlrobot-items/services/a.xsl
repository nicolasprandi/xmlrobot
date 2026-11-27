<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="day"/>
<xsl:output method="xml"/>

<xsl:template match="/">
  <xsl:apply-templates select="ws"/>
</xsl:template>

<xsl:template match="ws">
  <ws>
    <xsl:copy-of select="@*"/>
    <xsl:apply-templates select="apps"/>
    <xsl:apply-templates select="services"/>
  </ws>
</xsl:template>

<xsl:template match="apps">
  <apps>
    <xsl:copy-of select="@*"/>
    <xsl:attribute name="day">
      <xsl:value-of select="$day" />
    </xsl:attribute>
    <xsl:copy-of select="@*|node()|text()"/>
  </apps>
</xsl:template>

<xsl:template match="services">
  <services>
    <xsl:copy-of select="@*|node()|text()"/>
  </services>
</xsl:template>

</xsl:stylesheet>

  