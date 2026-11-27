<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="wish" />
<xsl:param name="sheet" />
<xsl:param name="fastlane" />
<xsl:param name="nbr_fastlane_rows" />
<xsl:output method="xml" indent="yes"/>
<xsl:template match="/">
<steps>

  <!-- <ti_break/>   -->
  <ti labxel=" ">
    
    <xsl:apply-templates select="cs/coltype">
        <xsl:with-param name="maxi"><xsl:value-of select="count(cs/coltype) - cs/@nbr_cols" /></xsl:with-param>           
    </xsl:apply-templates>
 
    <ti label="Column Statistics:">    
      <xsl:attribute name="x">
        <xsl:text>original spreadsheet: </xsl:text>
        <xsl:value-of select="cs/@nbr_cols" />
        <xsl:text> columns</xsl:text>         
      </xsl:attribute>   
    </ti>
    
    <xsl:if test="((count(cs/coltype))-(cs/@nbr_cols))&gt;0">
      <ti label=" ">      
        <xsl:attribute name="x">
          <xsl:text>additional: </xsl:text>
          <xsl:value-of select="(count(cs/coltype))-(cs/@nbr_cols)" />
          <xsl:text> columns</xsl:text>           
        </xsl:attribute>   
      </ti>  
    </xsl:if>       
    <ti type="call_job" label=" " job="See Unique Values of Columns" explain="See Unique Values of Columns"/>
    

         
    <!--
    <ti label="Avg Nbr of Values per Column:">
      <xsl:attribute name="x">
        <xsl:value-of select="round(count(//value) div cs/@nbr_cols)" /> 
      </xsl:attribute>   
    </ti>    
    -->
    
   
  </ti>
  <!-- <ti_break/>    -->



</steps>

</xsl:template>
<xsl:template match="coltype">
  <xsl:param name="maxi"/>

  <ti>
    <xsl:choose>
      <xsl:when test="position()=1">
        <xsl:attribute name="label">Columns:</xsl:attribute>
      </xsl:when>
      <xsl:otherwise>
        <xsl:attribute name="label"><xsl:text> </xsl:text></xsl:attribute>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:text></xsl:text><xsl:value-of select="position()" />
    <xsl:text>(e:c) ()</xsl:text>
    <xsl:value-of select="@name" />
    <xsl:text>()</xsl:text>
    <xsl:if test="position() &lt;= $maxi">
      <xsl:text> +</xsl:text>
    </xsl:if>  
    
  </ti>
    
  <!-- 
  <ti type="rec" label=" ">
    <ti>  
    <xsl:text></xsl:text><xsl:value-of select="position()" />
    <xsl:text>) </xsl:text>
    <xsl:if test="position() &gt; $maxi">
      <xsl:text>+</xsl:text>
    </xsl:if>  
    </ti>
    <ti>
    <xsl:value-of select="@name" />
    </ti>
    <ti><xsl:text> </xsl:text></ti>
  </ti>
  -->
    
  <!-- 
  <ti flag="label_right">
    <xsl:attribute name="label">
    <xsl:if test="position() &gt; $maxi">
      <xsl:text>+ </xsl:text>
    </xsl:if>    
    <xsl:text></xsl:text><xsl:value-of select="position()" />

    <xsl:text>:</xsl:text></xsl:attribute>
    
    <xsl:value-of select="@name" />
  </ti>
  -->
</xsl:template>
<xsl:include href="esc_xr.xsl"/>
</xsl:stylesheet>

  