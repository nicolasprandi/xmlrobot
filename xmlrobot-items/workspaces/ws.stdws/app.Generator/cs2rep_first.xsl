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
<steps>

  <ti label="[ 2 ] Columns and Values"/>
  <xsl:apply-templates select="cs/coltype"/>    



</steps>

</xsl:template>

<xsl:template match="coltype">
  <ti>
    <xsl:attribute name="label">
      <xsl:text>()</xsl:text>
      <xsl:value-of select="@name" />
      <xsl:text>() (e:o)</xsl:text>
      <xsl:value-of select="@value_count" />
      <xsl:text>(e:c)</xsl:text>
    </xsl:attribute>  
    <xsl:attribute name="x">
      <xsl:call-template name="coltype2">
        <xsl:with-param name="col_index" select="@col_index"/>
      </xsl:call-template>    
    </xsl:attribute>
  </ti>
</xsl:template>

<xsl:template name="coltype2">
  <xsl:param name="col_index"/>
  <xsl:apply-templates select="values/value[position()&lt;=$max]">
    <xsl:with-param name="total" select="count(values/value)"/>
  </xsl:apply-templates>
  <xsl:if test="count(values/value)&gt;$max">
    <xsl:text>; ...</xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="value">
  <xsl:param name="total"/>
  <xsl:text>()</xsl:text>
  <xsl:value-of select="." />
  <xsl:text>()</xsl:text>
  <xsl:choose>
    <xsl:when test="position()=$total">
    </xsl:when>
    <xsl:when test="position()&lt;$max">
      <xsl:text>; </xsl:text>
    </xsl:when>  
  </xsl:choose>

  

</xsl:template>





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

  