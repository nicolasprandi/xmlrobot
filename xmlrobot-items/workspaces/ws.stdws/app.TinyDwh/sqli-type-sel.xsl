<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2018-2020 Nicolas Prandstaetter. All rights reserved. -->
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- <xsl:param name="offline"/> 
<xsl:variable name="lowercase" select="'abcdefghijklmnopqrstuvwxyz'" />
<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />    -->


<xsl:template match="/">
<sels>
  <sel label="Private Script">private</sel>
  <xsl:apply-templates select="//bulk"/>
</sels>
</xsl:template>

<xsl:template match="bulk ">
  <xsl:if test="string-length(@sqli_file) > 0">
  <xsl:if test="string-length(@sqli_row) > 0">
  <sel>
    <xsl:attribute name="label">
      <xsl:value-of select="@name" /> 
    </xsl:attribute>
    <xsl:value-of select="@row" />
  </sel>
  </xsl:if>
  </xsl:if>
</xsl:template>


</xsl:stylesheet>

  
