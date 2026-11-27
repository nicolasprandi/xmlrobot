<?xml version="1.0" encoding="UTF-8"?><xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Copyright © [Write the year here ...] [Write your name here ...]. All rights reserved. -->
<xsl:param name="offset" />
<xsl:output method="xml" indent="yes"/>

<!-- once for every row -->
<xsl:template match="row">
  <row>
    <xsl:attribute name="a"><xsl:value-of select="$offset"/></xsl:attribute>
    <xsl:attribute name="b"><xsl:value-of select="position()"/></xsl:attribute>
    <xsl:attribute name="c"><xsl:value-of select="position()+$offset"/></xsl:attribute>       
    <xsl:copy-of select="@*|text()|node()"/>
  </row>
</xsl:template>

<!-- once for all rows -->
<xsl:template match="root">
  <root>  
    <xsl:copy-of select="@*"/>        
    <xsl:apply-templates select="row"/>
  </root>
</xsl:template>

<!-- once for xsl starter -->
<xsl:template match="/">
  <xsl:apply-templates select="node()"/>
</xsl:template>

</xsl:stylesheet>
 