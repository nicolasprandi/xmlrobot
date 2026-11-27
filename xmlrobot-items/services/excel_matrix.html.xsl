<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns="http://www.w3.org/TR/REC-html40">

<xsl:output method="html"/>

<xsl:template match="/">
<html>

  <head>
    <title>
      <xsl:text>Daynames of lang '</xsl:text>
      <xsl:value-of select="//root/@sheet_name" />
      <xsl:text>'</xsl:text>      
    </title>
  </head>
  
  <body>

    <table border="1">
      <tr>
        <td></td>
        <xsl:apply-templates select="//dims_col/col" mode="header"/>
      </tr>
      <xsl:apply-templates select="//dims_row/row" mode="data"/>
    </table>
    
  </body>
  
</html>

</xsl:template>
<xsl:template match="col" mode="header">
  <td><b><xsl:value-of select="./@col" /></b></td>
</xsl:template>

<xsl:template match="row" mode="data">
  <xsl:call-template name="row2">
    <xsl:with-param name="p_row" select="./@row"/>    
  </xsl:call-template>
</xsl:template>

<xsl:template name="row2">
  <xsl:param name="p_row"/>
  <tr>
    <td><b><xsl:value-of select="$p_row" /></b></td>
    <xsl:apply-templates select="//dims_col/col" mode="data">
      <xsl:with-param name="p_row" select="$p_row"/>
    </xsl:apply-templates>
    
  </tr>
</xsl:template>

<xsl:template match="col" mode="data">
  <xsl:param name="p_row"/>
  
  <xsl:call-template name="col2">
    <xsl:with-param name="p_row" select="$p_row"/>
    <xsl:with-param name="p_col" select="./@col"/>    
  </xsl:call-template>
  
</xsl:template>

<xsl:template name="col2">
  <xsl:param name="p_row"/>
  <xsl:param name="p_col"/>
  <td>
  
    <xsl:apply-templates select="//cells/cell[@col=$p_col and @row=$p_row]" mode="data"/>
    
  </td>
</xsl:template>

<xsl:template match="cell" mode="data">
      <xsl:value-of select="text()" />
</xsl:template>

  
<xsl:template match="dayname">
  <tr><td>
    <xsl:value-of select="text()"/>
  </td></tr>
</xsl:template>

</xsl:stylesheet>

  