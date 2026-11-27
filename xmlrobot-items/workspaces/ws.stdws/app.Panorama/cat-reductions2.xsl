<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="type"/>
<xsl:param name="sammeln"/>  

<xsl:output method="xml"/>

<xsl:template match="dir">
  <dir>
    <xsl:copy-of select="@*"/>
    <xsl:apply-templates/>
  </dir>
</xsl:template>

<xsl:template match="file">

  <xsl:call-template name="do_file">
    <xsl:with-param name="g"><xsl:text>-</xsl:text><xsl:value-of select="@g"/><xsl:text>-</xsl:text></xsl:with-param>
  </xsl:call-template>

</xsl:template>

<xsl:template name="do_file">
  <xsl:param name="g2"/>

  <xsl:choose>
    <xsl:when test="$type=1">
      <!-- positive filter, evaluate @g for group of file-->
      <xsl:if test="contains($sammeln,$g2)">
        <file>
          <xsl:copy-of select="@*|text()"/>
        </file>
      </xsl:if>
    </xsl:when>
    <xsl:when test="$type=2">
      <!-- positive filter, evaluate @g for group of file-->
      <xsl:if test="not(contains($sammeln,$g2))">
        <file>
          <xsl:copy-of select="@*|text()"/>
        </file>
      </xsl:if>
    </xsl:when>    
  </xsl:choose>

</xsl:template>

</xsl:stylesheet>

  