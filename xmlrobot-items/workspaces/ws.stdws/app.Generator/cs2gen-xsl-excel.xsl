<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias">
<xsl:namespace-alias stylesheet-prefix="axsl" result-prefix="xsl"/>  
<xsl:output method="xml" indent="yes"/>  

<xsl:template match="/">

  <xsl:element name="xsl:stylesheet">
    <xsl:attribute name="version">1.0</xsl:attribute>
  
    <xsl:element name="xsl:output">
      <xsl:attribute name="method">xml</xsl:attribute>
      <xsl:attribute name="indent">yes</xsl:attribute>
    </xsl:element>

    <xsl:element name="xsl:template">
      <xsl:attribute name="match">/</xsl:attribute>
      <xsl:element name="xsl:apply-templates">
        <xsl:attribute name="select">root</xsl:attribute>
      </xsl:element>
    </xsl:element>

    <xsl:element name="xsl:template">
      <xsl:attribute name="match">root</xsl:attribute>
      <rows>
        <xsl:attribute name="sheet_name">
          <xsl:value-of select="translate(cs/@sheet_name,' ','')"/>
        </xsl:attribute>
        <row>
          <xsl:apply-templates select="cs/coltype" mode="header"/>
        </row>        
        <xsl:element name="xsl:apply-templates">
          <xsl:attribute name="select">row</xsl:attribute>       
        </xsl:element>          
      </rows>
      <!-- 
      <xsl:element name="{translate(cs/@sheet_name,' ','')}"> 
        <xsl:element name="xsl:apply-templates">
          <xsl:attribute name="select">row</xsl:attribute>       
        </xsl:element>
      </xsl:element>   
      -->   
    </xsl:element>

    <xsl:element name="xsl:template">
      <xsl:attribute name="match">row</xsl:attribute>  
      <row>
        <axsl:attribute name="r">
          <axsl:value-of><xsl:attribute name="select"><xsl:text>position()</xsl:text></xsl:attribute></axsl:value-of>
        </axsl:attribute>          
        <xsl:apply-templates select="cs/coltype"/>     
      </row>
    </xsl:element>  
  
  
  </xsl:element> <!-- </xsl_stylesheet> -->
</xsl:template>

<xsl:template match="coltype" mode="header">
  <c><xsl:value-of select="@name"/></c>
</xsl:template>

<xsl:template match="coltype">
  <c>
    <axsl:value-of><xsl:attribute name="select">@<xsl:value-of select="@name"/></xsl:attribute></axsl:value-of>
  </c>
</xsl:template>

</xsl:stylesheet>

  