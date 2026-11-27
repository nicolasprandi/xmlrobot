<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- 
<xsl:param name="max" />
<xsl:param name="computer" />
<xsl:param name="os" />
<xsl:param name="wish" />
<xsl:param name="sheet" />
-->
<xsl:output method="xml" indent="yes"/>
<xsl:template match="/">
  <xsl:apply-templates select="cs"/>
</xsl:template>

<xsl:template match="cs">
  <unique_rows>
    <xsl:apply-templates select="coltype"/>
  </unique_rows>
</xsl:template>

<xsl:template match="coltype">
  <coltype_u>
    <xsl:copy-of select="@col_index|@value_count|@name|@name_orig" />
    <uniques>
      <xsl:apply-templates select="values/value"/>
    </uniques>   
  </coltype_u>
</xsl:template>

<xsl:template match="value">
  <xsl:call-template name="value2">
    <xsl:with-param name="value" select="."/>
  </xsl:call-template>
</xsl:template>

<xsl:template name="value2">
  <xsl:param name="value"/>
  <xsl:choose>
    <xsl:when test="count(preceding-sibling::value[node()=$value])=0">
      <!-- this is the first one -->
      <unique>      
        <xsl:attribute name="count">
          <xsl:value-of select="count(../value[node()=$value])"/>
        </xsl:attribute>
        <xsl:choose>
          <xsl:when test="string(number(.)) != 'NaN'">
            <xsl:attribute name="isNumeric">
              <xsl:text>1</xsl:text>
            </xsl:attribute>            
          </xsl:when>
          <xsl:otherwise>
            <xsl:attribute name="isNumeric">
              <xsl:text>0</xsl:text>
            </xsl:attribute>            
          </xsl:otherwise>
        </xsl:choose>
        
        <xsl:copy-of select="text()" />
      </unique>
    </xsl:when>
  </xsl:choose>

</xsl:template>

</xsl:stylesheet>

  