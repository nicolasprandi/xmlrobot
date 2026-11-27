<?xml version="1.0" encoding="UTF-8"?><xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->
<xsl:param name="rez_key" />
<xsl:output method="xml" indent="yes"/>

<!-- once for every row -->
<xsl:template match="komp">
  <sel>
    <xsl:attribute name="label"><xsl:value-of select="@bez"/></xsl:attribute>
    <xsl:text><xsl:value-of select="@row"/></xsl:text>
  </sel>
</xsl:template>

<!-- once for all rows -->
<xsl:template match="rezs">
  <sel>       
    <xsl:apply-templates select="rez[@row=$rez_key]/komp"/>
  </sel>
</xsl:template>

<!-- once for xsl starter -->
<xsl:template match="/">
  <xsl:apply-templates select="node()"/>
</xsl:template>

</xsl:stylesheet>
 