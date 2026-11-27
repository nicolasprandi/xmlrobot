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
      <xsl:attribute name="method">html</xsl:attribute>
    </xsl:element>

    <axsl:template>             
      <xsl:attribute name="match">/</xsl:attribute>
      <html>
      <body>
      <table>
      <axsl:apply-templates>
        <xsl:attribute name="select">//row</xsl:attribute>
      </axsl:apply-templates>
      </table>
      </body>
      </html>      
    </axsl:template>
    
    
    <xsl:comment> Template row is called for each row </xsl:comment>
    <axsl:template>
      <xsl:attribute name="match">row</xsl:attribute>  
      <tr>
      <xsl:comment> Row Number </xsl:comment>
      <td><b><axsl:value-of>
      <xsl:attribute name="select">position()</xsl:attribute>
      </axsl:value-of></b></td>
      <xsl:apply-templates select="cs/coltype" mode="data"/>
      </tr>      
    </axsl:template>  
    

  
    
  </xsl:element> <!-- </xsl_stylesheet> -->
</xsl:template>

<xsl:template match="coltype" mode="data">
    <xsl:comment><xsl:text> Column: </xsl:text><xsl:value-of select="@name"/> </xsl:comment>
    <td>      
      <axsl:value-of>
        <xsl:attribute name="select">@<xsl:value-of select="@name"/></xsl:attribute>      
      </axsl:value-of>            
    </td>
</xsl:template>

<xsl:template match="coltype" mode="header">
  <th><xsl:value-of select="@name"/></th>
</xsl:template>





</xsl:stylesheet>

  