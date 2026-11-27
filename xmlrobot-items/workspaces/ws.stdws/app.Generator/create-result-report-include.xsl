<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias">
<xsl:namespace-alias stylesheet-prefix="axsl" result-prefix="xsl"/> 
<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">
  <xsl:apply-templates select="cs"/>
</xsl:template>

<xsl:template match="cs">
  <axsl:stylesheet version="1.0">
  <axsl:template name="row_header">
    <xsl:if test="count(coltype)&gt;0">
      <axsl:call-template name="esc_xr">
        <axsl:with-param name="x">
          <xsl:attribute name="select">
            <xsl:text>@</xsl:text>
            <xsl:value-of select="coltype[@col_index=0]/@name"/>
          </xsl:attribute>
        </axsl:with-param>
      </axsl:call-template>
    </xsl:if>  
       
    <xsl:if test="count(coltype)&gt;1">
      <axsl:text>(e:s) </axsl:text>
      <axsl:call-template name="esc_xr">
        <axsl:with-param name="x">
          <xsl:attribute name="select">
            <xsl:text>@</xsl:text>
            <xsl:value-of select="coltype[@col_index=1]/@name"/>
          </xsl:attribute>
        </axsl:with-param>
      </axsl:call-template>  
     </xsl:if>      
  </axsl:template>
  </axsl:stylesheet>
</xsl:template>

<xsl:template match="coltype">
    <axsl:call-template name="value">
      <axsl:with-param name="name">
          <xsl:value-of select="@name"/>
      </axsl:with-param>
      <axsl:with-param name="value">
        <xsl:attribute name="select">
          <xsl:text>@</xsl:text>
          <xsl:value-of select="@name"/>
        </xsl:attribute>
      </axsl:with-param>      
      <axsl:with-param name="col_index">
        <xsl:value-of select="@col_index"/>
      </axsl:with-param>
      <axsl:with-param name="nbr_cols">
        <xsl:value-of select="/cs/@nbr_cols"/>
      </axsl:with-param>         
    </axsl:call-template> 
</xsl:template>


</xsl:stylesheet>

  