<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes"/>
<xsl:template match="/">
<sel>
  <xsl:apply-templates select="dir/dir"/>    
</sel>

</xsl:template>

<xsl:template match="dir">
  <xsl:if test="starts-with(@rpath,'compile-')">
    <sel><xsl:value-of select="substring-after(@rpath,'compile-')" /></sel>
  </xsl:if>
</xsl:template>

</xsl:stylesheet>

  