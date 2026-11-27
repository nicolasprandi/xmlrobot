<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>
<xsl:template match="/">

<sels>
  <xsl:apply-templates select="sheets/sheet"/>    
</sels>
</xsl:template>

<xsl:template match="sheet">
  <sel>
  <xsl:value-of select="@name" />
  </sel>
</xsl:template>

</xsl:stylesheet>

  