<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="ebfile" />
<xsl:param name="rowlimit" />
<xsl:output method="xml" indent="yes"/>
<xsl:include href="updater.xsl"/>
<xsl:template match="/">
  <dataset>
    <xsl:copy-of select="@*"/>
    <xsl:apply-templates select="dataset/cs"/>
    <xsl:apply-templates select="dataset/rows"/>
  </dataset>
</xsl:template>

<xsl:template match="cs">
  <cs>
    <xsl:copy-of select="@*"/>
    <xsl:apply-templates select="coltype"/>
    <xsl:apply-templates select="dims"/>
  </cs>
</xsl:template>
<xsl:template match="dims">
  <dims>
    <xsl:copy-of select="@*|node()"/>
  </dims>
</xsl:template>
<xsl:template match="coltype">
  <coltype>
    <xsl:copy-of select="@*"/>
  </coltype>
</xsl:template>

<xsl:template match="rows">
  <rows>
    <xsl:copy-of select="@*"/>
    <xsl:choose>
      <xsl:when test="string-length($rowlimit)&gt;0">
        <xsl:apply-templates select="r[position()&lt;=$rowlimit]"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="r"/>
      </xsl:otherwise>
    </xsl:choose>
  </rows>
</xsl:template>

<xsl:template match="r">
  <r>
    <xsl:copy-of select="@*"/>
    <xsl:copy-of select="./v"/>
    <!-- <xsl:apply-templates select="v"/>  -->
    <xsl:call-template name="do-missing-2">
      <xsl:with-param name="rid"><xsl:value-of select="@ri"/></xsl:with-param>
    </xsl:call-template>
  </r>
</xsl:template>
    <!--

    
<xsl:template name="do-missing">
  <xsl:param name="rid"/>
  <xsl:apply-templates select="document($ebfile,/)/root/row[position()=$rid]"/>
</xsl:template>

<xsl:template match="row">

  <v ci="3">
    <xsl:attribute name="x">
      <xsl:value-of select="@a"/>
    </xsl:attribute>
  </v>
  
  <v ci="4">
    <xsl:attribute name="x">
      <xsl:value-of select="@b"/>
    </xsl:attribute>
  </v>
  
  <v ci="5">
    <xsl:attribute name="x">
      <xsl:value-of select="@c"/>
    </xsl:attribute>
  </v>
  
</xsl:template>



<xsl:template match="v">
  <v>
    <xsl:copy-of select="@*"/>
  </v>
</xsl:template>

-->
</xsl:stylesheet>

  