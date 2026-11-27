<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="computer" />
<xsl:param name="os" />
<xsl:output method="xml" indent="yes"/>
<xsl:template match="/">

<cs>
  <xsl:attribute name="sheet_name">
    <xsl:value-of select="root/@sheet_name" />
  </xsl:attribute>
  <xsl:attribute name="sheet_index">
    <xsl:value-of select="root/@sheet_index" />
  </xsl:attribute>  
  <xsl:attribute name="nbr_cols">
    <xsl:value-of select="count(root/dim_col/col)" />
  </xsl:attribute>
  <xsl:apply-templates select="root/dim_col/col" mode="col_1">    
  </xsl:apply-templates>
  
  <dims>
    <xsl:apply-templates select="root/dim_col" mode="index"/> 
    <xsl:apply-templates select="root/dim_row" mode="index"/>   
  </dims>

</cs>

</xsl:template>

<xsl:template match="dim_col" mode="index">
  <dim_col>
  <xsl:copy-of select="@*|node()"/>
  </dim_col>
</xsl:template>

<xsl:template match="dim_row" mode="index">
  <dim_row>
  <xsl:copy-of select="@*|node()"/>
  </dim_row>
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
  <!-- 
  <xsl:attribute name="col_index_matrix">
    <xsl:value-of select="$col_index" />
  </xsl:attribute> 
  -->   
  
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
    <!--   
    <xsl:attribute name="row_index_matrix">
      <xsl:value-of select="@row" />
    </xsl:attribute> 
    -->
    
    
    <xsl:value-of select="." />
  </value>

</xsl:template>


</xsl:stylesheet>

  