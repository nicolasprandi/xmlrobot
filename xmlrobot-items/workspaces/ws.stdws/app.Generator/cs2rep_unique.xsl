<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="max_unique" />
<xsl:output method="xml" indent="yes"/>
<xsl:template match="/">
<steps>
  <ti_break/>
  <ti label="Unique Values per Column"/>
  <xsl:apply-templates select="cs/unique_rows/coltype_u[string-length(@value_count)&gt;0]">
    <xsl:sort select="count(uniques/unique)"/>
  </xsl:apply-templates>    
  <ti_hr/>
  <ti label="Report Limitiation">
    <ti label="Available:"><xsl:value-of select="count(cs/dims/dim_row/row)-1" /><xsl:text> Data-Records for this statistic available</xsl:text></ti>
    <ti label="Limitation:"><xsl:value-of select="$max_unique" /><xsl:text> Records(e:s) Configuration-parameter 'Nbr Unique Values'</xsl:text></ti>
    <ti label=" " type="call_job" job="See Unique Values of Columns?" explain="Read more about limitations on this report"/>    
    <ti label=" " type="call_job" job="Project Configuration"/>
  </ti>
</steps>

</xsl:template>

<xsl:template match="coltype_u">
  <ti>
    <xsl:attribute name="label">
      <xsl:text>(f,html,bold,open)</xsl:text>
      <xsl:value-of select="count(uniques/unique)" />
      <xsl:text>(f,html,bold,close)</xsl:text>
      <xsl:text> variation(f,plural,</xsl:text><xsl:value-of select="count(uniques/unique)" /><xsl:text>) in </xsl:text>
    </xsl:attribute>
    <xsl:attribute name="x">
      <xsl:text>(f,html,bold,open)</xsl:text>
      <xsl:value-of select="@name" />
      <xsl:text>(f,html,bold,close)</xsl:text>      
    </xsl:attribute>


      <xsl:text> </xsl:text>

  </ti>
    <xsl:call-template name="coltype2"/>
 
</xsl:template>

<xsl:template name="coltype2">
  <xsl:param name="col_index"/>
  <xsl:apply-templates select="uniques/unique">
    <xsl:sort select="@count" order="descending" data-type="number"/>
    <xsl:with-param name="total" select="count(uniques/unique)"/>
  </xsl:apply-templates>
  <xsl:if test="count(uniques/unique)&gt;$max_unique">
    <ti label=" ">...</ti>
  </xsl:if>
</xsl:template>

<xsl:template match="unique">
  <xsl:param name="total"/>
  
  <xsl:if test="position()&lt;=$max_unique">
    <ti flag="label_right">
      <xsl:attribute name="label">
        <xsl:value-of select="@count"/>
        <xsl:text> x </xsl:text>
      </xsl:attribute>
      <xsl:call-template name="esc_xr">
        <xsl:with-param name="x" select="."/>
      </xsl:call-template>
      
    </ti>
  </xsl:if>

      
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

<xsl:include href="esc_xr.xsl"/>

</xsl:stylesheet>

  