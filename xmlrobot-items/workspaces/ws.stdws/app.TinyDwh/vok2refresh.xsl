<?xml version="1.0" encoding="UTF-8"?>

<!-- Copyright © 2018-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="obj_typ" />
<xsl:param name="obj_tech_name2"/>
<xsl:param name="view_prefix" />
<xsl:param name="mv_prefix" />
<xsl:param name="mv_style" />
<xsl:param name="dwh_prefix" />
<xsl:param name="dbms" />

<xsl:output method="xml"/>

<xsl:include href="include-mv-naming.xsl" />

<xsl:template match="/">
<us>
<xsl:apply-templates select="root/ots/ot[@obj_typ=$obj_typ]"/>
</us> 
</xsl:template>

<xsl:template match="ot">
  <xsl:call-template name="ot2">
    <xsl:with-param name="obj_tech_name" select="@tech_name"/>
    <xsl:with-param name="obj_typ" select="$obj_typ"/>
  </xsl:call-template>
</xsl:template>

<xsl:template name="ot2">
  <xsl:param name="obj_tech_name"/>
  
  <xsl:choose>
    <xsl:when test="$dbms='postgres'">
      <!-- postgres -->
      <u><xsl:param name="obj_typ"/>REFRESH MATERIALIZED VIEW <xsl:value-of select="$mv_prefix" /><xsl:call-template name="get_name_mv" />;</u>
    </xsl:when>
    <xsl:otherwise>
      <!-- mysql -->    
      <u>TRUNCATE <xsl:value-of select="$mv_prefix" /><xsl:call-template name="get_name_mv" />;</u>
      <!-- <u><xsl:text>insert into </xsl:text><xsl:value-of select="$mv_prefix" /><xsl:value-of select="$obj_tech_name" /><xsl:text> select * from <xsl:value-of select="$view_prefix" />2_</xsl:text><xsl:value-of select="$obj_tech_name" /><xsl:text>;</xsl:text></u>-->
      <u><xsl:text>insert into </xsl:text><xsl:value-of select="$mv_prefix" /><xsl:call-template name="get_name_mv" /><xsl:text> select * from </xsl:text><xsl:value-of select="$view_prefix" />2_<xsl:call-template name="get_name_mv" /><xsl:text>;</xsl:text></u>         
     
    </xsl:otherwise>
  </xsl:choose>          
  
  
  
  
  </xsl:template>  

</xsl:stylesheet>

  
