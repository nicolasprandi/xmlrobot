<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->

<!--
<xsl:template name="do-missing">
  <xsl:param name="rid"/>
  <v ci="14">
    <xsl:attribute name="x">
    <xsl:value-of select="document($ebfile,/)/rows/r[@ri=$rid]/@desc" />
    </xsl:attribute>
  </v>
  <v ci="15" x="b"/>
  <v ci="16" x="c"/>

</xsl:template>

-->
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias">
<xsl:namespace-alias stylesheet-prefix="axsl" result-prefix="xsl"/>   
<xsl:param name="start_col" />
<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">

  <xsl:element name="xsl:stylesheet">
    <xsl:attribute name="version">1.0</xsl:attribute>
  
    <xsl:element name="xsl:output">
      <xsl:attribute name="method">xml</xsl:attribute>
      <xsl:attribute name="indent">yes</xsl:attribute>
    </xsl:element>

    <axsl:template>
      <xsl:attribute name="name">do-missing-2</xsl:attribute>
      <xsl:element name="xsl:param">
        <xsl:attribute name="name">rid</xsl:attribute>
      </xsl:element>
      <axsl:apply-templates select="document($ebfile,/)/root/row[position()=$rid]"/>      
    </axsl:template>
    <axsl:template match="row">
      <xsl:apply-templates select="splits/split"/>
    </axsl:template>
  </xsl:element> <!-- </xsl_stylesheet> -->
</xsl:template>

<xsl:template match="split">
  <v>
    <xsl:attribute name="ci"><xsl:value-of select="position()+$start_col"/></xsl:attribute>
    <xsl:element name="xsl:attribute">
      <xsl:attribute name="name">x</xsl:attribute>
      <xsl:element name="xsl:value-of">
        <xsl:attribute name="select">
          <xsl:text>@</xsl:text>
          <xsl:value-of select="@x"/>
        </xsl:attribute>
      

      </xsl:element>
      <!-- 
   <xsl:attribute name="x2"> 
      <xsl:element name="xsl:value-of">
        <xsl:attribute name="select">x</xsl:attribute>        
              <xsl:attribute name="select"><xsl:text>document($ebfile,/)/rows/r[@ri=$rid]/@</xsl:text><xsl:value-of select="@x"/></xsl:attribute>
    <xsl:text>document($ebfile,/)/rows/r[@ri=$rid]/@</xsl:text>
    <xsl:value-of select="document($ebfile,/)/rows/r[@ri=$rid]/@x" />
    </xsl:attribute> -->
    
    </xsl:element>
        
  </v>
</xsl:template>

    
    <!-- 
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">rows</xsl:attribute>
      <xsl:element name="{translate(cs/@sheet_name,' ','')}"> 
        <xsl:element name="xsl:apply-templates">
          <xsl:attribute name="select">r</xsl:attribute>       
        </xsl:element>
      </xsl:element>      
    </xsl:element>

    <xsl:element name="xsl:template">
      <xsl:attribute name="match">r</xsl:attribute>  
      <record>
        <xsl:element name="xsl:attribute">
          <xsl:attribute name="name">id</xsl:attribute>
          <xsl:element name="xsl:value-of">
            <xsl:attribute name="select">@ri</xsl:attribute>
          </xsl:element>
        </xsl:element>
          
        <xsl:apply-templates select="cs/coltype"/>     
      </record>
    </xsl:element>  
    -->
  
  


<xsl:template match="coltype">
  <xsl:element name="{translate(@name,' ','')}">
      <xsl:element name="xsl:value-of"><xsl:attribute name="select">@<xsl:value-of select="@name"/></xsl:attribute></xsl:element>
  </xsl:element>
</xsl:template>

</xsl:stylesheet>

  