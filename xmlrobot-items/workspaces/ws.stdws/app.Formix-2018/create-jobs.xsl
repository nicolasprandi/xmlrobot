<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015 Nicolas Prandstaetter. All rights reserved. 






 -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">
   <xsl:apply-templates/>
</xsl:template>

<xsl:template match="conf">
  <s2>
    <xsl:attribute name="a">
      <xsl:value-of select="xx" />
    </xsl:attribute>
  </s2>
</xsl:template>

</xsl:stylesheet>

  