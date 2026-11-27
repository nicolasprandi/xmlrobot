<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="xml" indent="yes"/>
<!--<xsl:strip-space elements="*"/>-->
<xsl:template match="/">

  <xsl:element name="xsl:stylesheet">
    <xsl:attribute name="version">1.0</xsl:attribute>
  
    <xsl:element name="xsl:output">
      <xsl:attribute name="method">html</xsl:attribute>
    </xsl:element>
    <!--
    <xsl:element name="xsl:strip-space">
      <xsl:attribute name="elements">*</xsl:attribute>
    </xsl:element> 
     -->
    
    
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">/</xsl:attribute>
      <html>
      <header>
      </header>
      <body>
      <table border="1">
      <tr>
      <td>#</td>
      <xsl:apply-templates select="cs/coltype" mode="header"/><xsl:text>
</xsl:text>
      </tr>
      <xsl:element name="xsl:apply-templates">
        <xsl:attribute name="select">root/row</xsl:attribute>
      </xsl:element>   
      </table>
      </body>
      </html>   
    </xsl:element>

    <!-- 
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">rows</xsl:attribute>
      <xsl:element name="{cs/@sheet_name}"> 
        <xsl:element name="xsl:apply-templates">
          <xsl:attribute name="select">r</xsl:attribute>      
        </xsl:element>
      </xsl:element>      
    </xsl:element>
    -->

    <xsl:element name="xsl:template">
      <xsl:attribute name="match">row</xsl:attribute>  
      <tr>
      <td><b>
      <xsl:element name="xsl:value-of">
      <xsl:attribute name="select">position()</xsl:attribute>
      </xsl:element>

      </b>
      </td>
      <xsl:apply-templates select="cs/coltype" mode="data"/>
      </tr>
      
    </xsl:element>  
   
    
  </xsl:element> <!-- </xsl_stylesheet> -->
</xsl:template>

<xsl:template match="coltype" mode="data">
    <td>
      <xsl:element name="xsl:value-of">
          <xsl:attribute name="select">translate(translate(@<xsl:value-of select="@name"/>,'&gt;','_'),'&lt;','_')</xsl:attribute>
      </xsl:element>
    </td>

  
  <xsl:choose>
    <xsl:when test="not(position()=last())">
      <xsl:text>,</xsl:text>
    </xsl:when>
    <xsl:otherwise>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="coltype" mode="header">
  <td><b><xsl:value-of select="@name"/></b></td>
</xsl:template>

<xsl:template match="coltype" mode="second">
  <xsl:text>'</xsl:text>
      <xsl:element name="xsl:value-of">
          <xsl:attribute name="select">@<xsl:value-of select="@name"/></xsl:attribute>
      </xsl:element>
  <xsl:text>'</xsl:text>
  
  <xsl:if test="not(position()=last())">
    <xsl:text>, </xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="coltype">
  <xsl:element name="{@name}">
      <xsl:element name="xsl:value-of">
          <xsl:attribute name="select">@<xsl:value-of select="@name"/></xsl:attribute>
      </xsl:element>
  </xsl:element>
</xsl:template>

</xsl:stylesheet>

  