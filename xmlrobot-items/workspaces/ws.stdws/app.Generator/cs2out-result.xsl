<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="max" />
<xsl:param name="computer" />
<xsl:param name="os" />
<xsl:param name="wish" />
<xsl:param name="sheet" />
<xsl:output method="xml" indent="yes"/>
<xsl:template match="/">
  <xsl:apply-templates select="dataset/cs"/>
</xsl:template>

<xsl:template match="cs">
  <rows>
    <xsl:attribute name="sheet_name">
      <xsl:value-of select="@sheet_name" />
    </xsl:attribute>     
    <row> 
    <xsl:apply-templates select="coltype"/>
    </row>      
    <xsl:apply-templates select="//rows/r"/>
  </rows>
</xsl:template>

<xsl:template match="coltype">
  <c>
    <xsl:value-of select="@name" />
  </c>
</xsl:template>

<xsl:template match="r">
    <xsl:call-template name="row2">
      <xsl:with-param name="row_index" select="@ri"/>
    </xsl:call-template>
</xsl:template>

<xsl:template name="row2">
  <xsl:param name="row_index"/>
  <row>
    <xsl:attribute name="r">
      <xsl:value-of select="$row_index" />
    </xsl:attribute>

    <xsl:apply-templates select="//dims/dim_col/col">
      <xsl:with-param name="row_index" select="$row_index"/>
    </xsl:apply-templates>

  
  </row>
</xsl:template>

<xsl:template match="col">
  <xsl:param name="row_index"/>
  
  <xsl:call-template name="col2">
    <xsl:with-param name="col_index" select="@col - 1"/>
    <xsl:with-param name="row_index" select="$row_index"/>
  </xsl:call-template>  
  
</xsl:template>

<xsl:template name="col2">
  <xsl:param name="row_index"/>
  <xsl:param name="col_index"/>

  <xsl:choose>
    <xsl:when test="count(//rows/r[@ri=$row_index]/v[@ci=$col_index]) &gt; 0">
      <xsl:apply-templates select="//rows/r[@ri=$row_index]/v[@ci=$col_index]"/>
    </xsl:when>
    
    <xsl:otherwise>
      <c><xsl:text></xsl:text></c>
    </xsl:otherwise>
  
  </xsl:choose>
  
</xsl:template>

<xsl:template match="v">
  <c><xsl:value-of select="@x"/></c>
</xsl:template>



</xsl:stylesheet>

  