<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="rows_to_list" />
<xsl:param name="fastlane" />
<xsl:param name="name1" />
<xsl:param name="name2" />
<xsl:output method="xml" indent="yes"/>
<xsl:template match="/">
<steps>
  <ti_hr/>
  <ti>
    <xsl:attribute name="label">
      <xsl:choose>
        <xsl:when test="$fastlane=0">
          <xsl:text>Data Before Modification(e:s) first </xsl:text> 
        </xsl:when>  
        <xsl:when test="$fastlane=1">
          <xsl:text>Data Before Modification(e:s) first </xsl:text>
        </xsl:when>              
      </xsl:choose>        
      <xsl:choose>
        <xsl:when test="count(dataset/rows/r)&lt;$rows_to_list">
          <xsl:value-of select="count(dataset/rows/r)"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$rows_to_list"/>
        </xsl:otherwise>
      </xsl:choose>
      
      <xsl:text> records</xsl:text>
    </xsl:attribute>    
  </ti>
  <xsl:apply-templates select="dataset/rows/r[position()&lt;=$rows_to_list]">
  </xsl:apply-templates>    



</steps>

</xsl:template>

<xsl:template match="r">
  <ti flag="label_right">
    <xsl:attribute name="label">
      <xsl:text>(f,html,bold,open)</xsl:text>
      <xsl:value-of select="@ri" />
      <xsl:text>(f,html,bold,close)</xsl:text>
      <xsl:text>(e:c) </xsl:text>
    </xsl:attribute>
    <xsl:attribute name="x">
      <xsl:text>(f,html,bold,open)</xsl:text>
      <xsl:call-template name="record_header">
        <xsl:with-param name="ri" select="@ri"/>
      </xsl:call-template>
      <xsl:text>(f,html,bold,close)</xsl:text>
    </xsl:attribute>  
  </ti>
  
  <xsl:apply-templates select="//cs/coltype">
    <xsl:with-param name="ri" select="@ri"/>
  </xsl:apply-templates> 

  <ti_hr style="2" />

</xsl:template>

<xsl:template name="record_header">
  <xsl:param name="ri"/>
  <xsl:call-template name="esc_xr">
    <xsl:with-param name="x" select="//rows/r[@ri=$ri]/v[position()=$name1]/@x"/>
  </xsl:call-template>  
  <xsl:if test="$name2>0">
    <xsl:text>(e:s) </xsl:text>
    <xsl:call-template name="esc_xr">
      <xsl:with-param name="x" select="//rows/r[@ri=$ri]/v[position()=$name2]/@x"/>
    </xsl:call-template>      
  </xsl:if>
</xsl:template>

<xsl:template match="coltype">
  <xsl:param name="ri"/>

  <xsl:call-template name="coltype2">
    <xsl:with-param name="col_index" select="@col_index"/>
    <xsl:with-param name="ri" select="$ri"/>
    <xsl:with-param name="name" select="@name"/>
  </xsl:call-template>    

</xsl:template>

<xsl:template name="coltype2">
  <xsl:param name="col_index"/>
  <xsl:param name="ri"/>
  <xsl:param name="name"/>
  
  <xsl:apply-templates select="//rows/r[@ri=$ri]/v[@ci=$col_index]">
    <xsl:with-param name="ri" select="$ri"/>
    <xsl:with-param name="col_index" select="$col_index"/>
    <xsl:with-param name="name" select="$name"/>
    <xsl:with-param name="nbr_cols" select="//cs/@nbr_cols"/>
  </xsl:apply-templates> 
  

</xsl:template>

<xsl:template match="v">
  <xsl:param name="col_index"/>
  <xsl:param name="ri"/>
  <xsl:param name="name"/>
  <xsl:param name="nbr_cols"/>
  
  <xsl:if test="$col_index &lt; $nbr_cols">
  
  <ti flag="label_right">
    <xsl:attribute name="label">
      
      <xsl:value-of select="$name" />
      <xsl:text>:</xsl:text>
    </xsl:attribute>  
    <xsl:attribute name="x">
      <xsl:if test="$col_index &gt;= $nbr_cols">
        <xsl:text>(f,html,bold,open)</xsl:text>
      </xsl:if>
      <xsl:text>(f,html,i,open)</xsl:text>
      <xsl:call-template name="esc_xr">
        <xsl:with-param name="x" select="@x"/>
      </xsl:call-template>
      <xsl:text>(f,html,i,close)</xsl:text> 
      <xsl:if test="$col_index &gt;= $nbr_cols">
        <xsl:text>(f,html,bold,close)</xsl:text>
      </xsl:if>       
    </xsl:attribute>
  </ti>
  
  </xsl:if>

</xsl:template>

<xsl:include href="esc_xr.xsl"/>



<xsl:template match="col" mode="col_1">

  <xsl:call-template name="col_2">
    <xsl:with-param name="col_index" select="@col"/>
  </xsl:call-template>

</xsl:template>

<xsl:template name="col_2">
<xsl:param name="col_index"/>

<coltype>
  <xsl:attribute name="col_index">
    <xsl:value-of select="$col_index - 1" />
  </xsl:attribute>
  
  <xsl:attribute name="col_index_matrix">
    <xsl:value-of select="$col_index" />
  </xsl:attribute>    
  
  <xsl:attribute name="value_count">
    <xsl:value-of select="count(//c[@row>1 and @col=$col_index])" />
  </xsl:attribute> 
  
  <xsl:attribute name="name">
    <xsl:value-of select="//c[@row=1 and @col=$col_index]" />
  </xsl:attribute>   
  
  <values>  
  
      <xsl:apply-templates select="//c[@row>1 and @col=$col_index]" mode="value">    
      </xsl:apply-templates>   
  </values>  
  
</coltype>

</xsl:template>

<xsl:template match="c" mode="value">
  <value>
  
    <xsl:attribute name="row_index">
      <xsl:value-of select="@row - 2" />
    </xsl:attribute> 
      
    <xsl:attribute name="row_index_matrix">
      <xsl:value-of select="@row" />
    </xsl:attribute> 
    
    
    <xsl:value-of select="." />
  </value>

</xsl:template>



</xsl:stylesheet>

  