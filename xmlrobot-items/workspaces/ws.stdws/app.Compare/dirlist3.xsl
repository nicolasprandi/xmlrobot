<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2019 Nicolas Prandstaetter. All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml"/>

<xsl:template match="/">
  <xsl:apply-templates select="dir" mode="root"/>
</xsl:template>

<xsl:template match="dir" mode="root">
  <dir>
    <xsl:copy-of select="@*"/>
    <xsl:apply-templates select="dir" mode="second"/>
  </dir>
</xsl:template>

<xsl:template match="dir" mode="second">
  <dir>
    <xsl:copy-of select="@*|node()"/>
    
    <xsl:if test="@exists=1">         <!-- following -->
      <xsl:apply-templates select="preceding-sibling::dir[1][@exists=1]" mode="next"/>
    </xsl:if>                                     <!-- @exists=1  -->
  </dir>
</xsl:template>

<xsl:template match="dir" mode="next">
  <next>
    <xsl:copy-of select="@*|node()"/>
  </next>
</xsl:template>

</xsl:stylesheet>

  