<?xml version="1.0" encoding="UTF-8"?>

<!-- Copyright © 2018-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:template match="/">
<sel>
  <xsl:apply-templates select="bulks/bulk"/>
</sel>
</xsl:template>

<xsl:template match="bulk">
  <sel>
    <xsl:attribute name="label">
      <xsl:value-of select="@name" />
    </xsl:attribute>
  <xsl:value-of select="@row" />
  </sel>
</xsl:template>


</xsl:stylesheet>

  
