<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2018-2020 Nicolas Prandstaetter. All rights reserved. -->
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="offline"/>
<xsl:param name="sql_len"/>
<xsl:param name="script_len"/>
<!-- <xsl:param name="doku_len"/>  -->
<xsl:param name="doku"/>
<xsl:param name="dokuw1"/>
<xsl:param name="dokuw2"/>
<xsl:variable name="lowercase" select="'abcdefghijklmnopqrstuvwxyz'" />
<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />

<xsl:include href="include-doku-cut.xsl" />

<xsl:template match="/">
<sel>
  <xsl:apply-templates select="//squery"/>
</sel>
</xsl:template>

<xsl:template match="squery ">
  <sel>
    <xsl:attribute name="label">
      <xsl:text>&apos;</xsl:text><xsl:value-of select="@id" /><xsl:text>&apos;</xsl:text>
 
      <xsl:if test="@class1='update'">                    
        <xsl:text> (Update)</xsl:text>
      </xsl:if>   
      <!-- 
      <xsl:if test="string-length(@prolog)>0">
        <xsl:text> (Script)</xsl:text>
      </xsl:if>            -->   
      
      <xsl:if test="$offline=1">
        <xsl:text>: </xsl:text>
      </xsl:if>        
      
      <!-- doku -->
      <xsl:call-template name="get_doku">
        <xsl:with-param name="doku_to_cut" select="@desc"/>
      </xsl:call-template>             
      
      <!-- sql -->
      <xsl:choose>
        <xsl:when test="string-length(@sql) > 0">
          <xsl:text> &#xA;[ Sql ] &#xA;</xsl:text>
          <xsl:value-of select="substring( @sql, 0, $sql_len)" />
          <xsl:if test="string-length(@sql) > $sql_len">
          <xsl:text> ... </xsl:text>
          </xsl:if>            
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>NO QUERY-ABFRAGE specified</xsl:text>
        </xsl:otherwise>      
      </xsl:choose>
      

      <!-- prolog script  
      <xsl:if test="string-length(@prolog) > 0">
        <xsl:text> &#xA;[ Script ] &#xA;</xsl:text>
        <xsl:value-of select="substring( @prolog, 0, $script_len)" />
          <xsl:if test="string-length(@prolog) > $script_len">
            <xsl:text> ... </xsl:text>
          </xsl:if>    
      </xsl:if>        -->    
      
      

      
  
    </xsl:attribute>
    <xsl:value-of select="@row" />
  </sel>
</xsl:template>

<!-- 
<xsl:template name="get_doku">
  <xsl:choose>
    <xsl:when test="$mv_style='name'">
      <xsl:value-of select="$obj_tech_name2" />    
    </xsl:when>
    <xsl:when test="$mv_style='incog'">
      <xsl:text>_</xsl:text><xsl:value-of select="$obj_typ" />
    </xsl:when>    
  </xsl:choose>
</xsl:template>
-->


</xsl:stylesheet>

  
