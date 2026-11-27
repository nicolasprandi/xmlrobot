<?xml version="1.0" encoding="UTF-8"?><xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->
<xsl:output method="xml" indent="yes"/>

<!-- once for every row      -->
<xsl:template match="dir" mode="b">
  <sel>
    <xsl:text><xsl:value-of select="@rpath"/></xsl:text>
  </sel>
</xsl:template>


<xsl:template match="dir" mode="a">
  <sel>       
    <xsl:apply-templates select="dir" mode="b"/>
  </sel>
</xsl:template>        

<!-- once for xsl starter -->
<xsl:template match="/">
  <xsl:apply-templates select="dir" mode="a" />
</xsl:template>

</xsl:stylesheet>
 