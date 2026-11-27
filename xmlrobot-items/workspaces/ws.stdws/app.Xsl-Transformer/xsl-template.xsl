<?xml version="1.0" encoding="UTF-8"?><xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->

<xsl:param name="start_id" />
<xsl:output method="xml" indent="yes"/>

<!-- once for every row -->
<xsl:template match="row">
  <row>
    <xsl:attribute name="id"><xsl:value-of select="position()"/></xsl:attribute>  
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
 
