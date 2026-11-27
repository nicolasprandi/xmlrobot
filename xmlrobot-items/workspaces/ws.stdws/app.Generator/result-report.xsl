<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="rows_to_list" />
<xsl:param name="fastlane" />
<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">
  <xsl:apply-templates select="root"/>
</xsl:template>

<xsl:template match="root">
<steps>
  <ti_hr/>
  <ti>
    <xsl:attribute name="label">
      <xsl:choose>
        <xsl:when test="$fastlane=0">
          <xsl:text>Results(e:s) first </xsl:text> 
        </xsl:when>  
        <xsl:when test="$fastlane=1">
          <xsl:text>Results(e:s) first </xsl:text>
        </xsl:when>              
      </xsl:choose>        
      <xsl:choose>
        <xsl:when test="count(row)&lt;$rows_to_list">
          <xsl:value-of select="count(row)"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$rows_to_list"/>
        </xsl:otherwise>
      </xsl:choose>
      
      <xsl:text> records</xsl:text>
    </xsl:attribute>    
  </ti>
  
  <xsl:apply-templates select="row[position()&lt;=$rows_to_list]" mode="complete"/>
</steps>
</xsl:template>

<xsl:template match="row" mode="complete">
  <try_catch>
  <try>
  <ti flag="label_right">
    <xsl:attribute name="label">
      <xsl:text>(f,html,bold,open)</xsl:text>
      <xsl:value-of select="position()" />
      <xsl:text>(f,html,bold,close)</xsl:text>
      <xsl:text>(e:c) </xsl:text>
    </xsl:attribute>
    <xsl:attribute name="x">
      <xsl:text>(f,html,bold,open)</xsl:text>
      <xsl:call-template name="row_header"/>
      <xsl:text>(f,html,bold,close)</xsl:text>
    </xsl:attribute>  
  </ti>
  
  <xsl:call-template name="row_data"/>
  
  </try>
  <catch>
    <ti>had a problem with this one</ti>
  </catch>
  </try_catch>

</xsl:template>



<xsl:template name="value">
  <xsl:param name="name"/>
  <xsl:param name="value"/>
  <xsl:param name="col_index"/>
  <xsl:param name="nbr_cols"/>  
  <ti flag="label_right">
    <xsl:attribute name="label">
      <xsl:text>()</xsl:text>
      <xsl:value-of select="$name"/>
      <xsl:text>():</xsl:text>
    </xsl:attribute>
    
    <xsl:attribute name="x">
      <xsl:if test="$col_index &gt;= $nbr_cols">
        <xsl:text>(f,html,bold,open)</xsl:text>
      </xsl:if>
      <xsl:text>(f,html,i,open)()</xsl:text>
      <xsl:value-of select="$value"/>
      <!-- 
      <xsl:call-template name="oloxo">
        <xsl:with-param name="x" select="$value"/>
      </xsl:call-template>
      -->
      <xsl:text>()(f,html,i,close)</xsl:text> 
      
      <xsl:if test="$col_index &gt;= $nbr_cols">
        <xsl:text>(f,html,bold,close)</xsl:text>
      </xsl:if>       
    </xsl:attribute>    
  </ti>
  
</xsl:template>
<xsl:include href="esc_xr.xsl"/>
<xsl:include href="result-report-include.xsl"/>
<xsl:include href="result-report-include2.xsl"/>
</xsl:stylesheet>

  