<?xml version="1.0" encoding="UTF-8"?>

<!-- Copyright © 2018-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="dbms" />
<xsl:param name="view_prefix" />
<xsl:param name="mv_prefix" />
<xsl:param name="mv_style" />
<xsl:output method="text"/>
<!-- <xsl:include href="include-mv-naming.xsl" /> -->

<xsl:template name="get_name_mv">
  <xsl:choose>
    <xsl:when test="$mv_style='name'">
      <xsl:value-of select="@tech_name" />    
    </xsl:when>
    <xsl:when test="$mv_style='incog'">
      <xsl:text>_</xsl:text><xsl:value-of select="@obj_typ" />
    </xsl:when>    
  </xsl:choose>
</xsl:template>

<xsl:template match="/">
<xsl:apply-templates select="root/ots/ot"/>
</xsl:template>


<xsl:template match="ot">

<xsl:choose>

<xsl:when test="$dbms='postgres'">

<xsl:text>select 
&apos;</xsl:text><xsl:value-of select="@tech_name" /><xsl:text>&apos; as obj_tech_name,
&apos;</xsl:text><xsl:value-of select="@name" /><xsl:text>&apos; as obj_name,
&apos;</xsl:text><xsl:value-of select="$mv_prefix" /><xsl:call-template name="get_name_mv" /><xsl:text>&apos; as mv_name,
Cast ( exists ( 
 select
   matviewname 
 from 
  pg_matviews 
where
  matviewname= &apos;</xsl:text><xsl:value-of select="$mv_prefix" /><xsl:call-template name="get_name_mv" /><xsl:text>&apos;) as integer) as ok</xsl:text>
  
</xsl:when>

<xsl:when test="$dbms='mysql'">

<xsl:text>select 
&apos;</xsl:text><xsl:value-of select="@tech_name" /><xsl:text>&apos; as obj_tech_name,
&apos;</xsl:text><xsl:value-of select="@name" /><xsl:text>&apos; as obj_name,
&apos;</xsl:text><xsl:value-of select="$mv_prefix" /><xsl:call-template name="get_name_mv" /><xsl:text>&apos; as mv_name , 
exists ( 
 select
   TABLE_NAME 
 from 
  information_schema.tables 
where
  TABLE_SCHEMA = database() and
  TABLE_NAME= &apos;</xsl:text><xsl:value-of select="$mv_prefix" /><xsl:call-template name="get_name_mv" /><xsl:text>&apos;) as ok </xsl:text>

</xsl:when>

</xsl:choose>  
<xsl:choose>  
  <xsl:when test="position()=last()"><xsl:text>;</xsl:text>
  </xsl:when>
  <xsl:otherwise><xsl:text> 
UNION 
</xsl:text>
  </xsl:otherwise>
</xsl:choose> 
</xsl:template>  

</xsl:stylesheet>

  
