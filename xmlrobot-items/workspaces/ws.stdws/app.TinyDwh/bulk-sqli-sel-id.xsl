<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2018-2020 Nicolas Prandstaetter. All rights reserved. -->
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="sql_len"/>
<xsl:param name="script_len"/>
<!-- <xsl:param name="doku_len"/>  -->
<xsl:param name="doku"/>
<xsl:param name="dokuw1"/>
<xsl:param name="dokuw2"/>

<xsl:param name="each"/>
<xsl:param name="head"/>
<xsl:param name="first"/>

<xsl:variable name="lowercase" select="'abcdefghijklmnopqrstuvwxyz'" />
<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />  

<xsl:include href="include-doku-cut.xsl" />

<xsl:template match="/">
<sel>
  <xsl:apply-templates select="//sqli"/>
</sel>
</xsl:template>

<xsl:template match="sqli ">
  <sel>
    <xsl:attribute name="label">               <!-- select="translate(@name,$lowercase,$uppercase)" -->
      <xsl:text>&apos;</xsl:text><xsl:value-of select="@name" /><xsl:text>&apos; </xsl:text>
      
      <xsl:if test="@class1='update'">                    
        <xsl:text> (type=</xsl:text><xsl:value-of select="@type" /><xsl:text>) </xsl:text>
      </xsl:if>   
            
      <!-- docu -->      
      <xsl:call-template name="get_doku">
        <xsl:with-param name="doku_to_cut" select="@docu"/>
      </xsl:call-template> 
      
      <!-- bei typ script -->
      <xsl:if test="@type='private'">
      
        <!-- scripte -->      
        <xsl:choose>
          <xsl:when test="string-length(@sc_main) > 0">
            <xsl:text> &#xA;[ </xsl:text>
            <xsl:value-of select="$each"/>
            <xsl:text> ] &#xA;</xsl:text>
            <xsl:value-of select="substring( @sc_main, 0, $script_len)" />
              <xsl:if test="string-length(@sc_main) > $script_len">
                <xsl:text> ... </xsl:text>
              </xsl:if>    
          </xsl:when>
          
          <xsl:when test="string-length(@sc_top) > 0">
            <xsl:text> &#xA;[ </xsl:text>
            <xsl:value-of select="$head"/>
            <xsl:text> ] &#xA;</xsl:text>
            <xsl:value-of select="substring( @sc_top, 0, $script_len)" />
              <xsl:if test="string-length(@sc_top) > $script_len">
                <xsl:text> ... </xsl:text>
              </xsl:if>    
          </xsl:when>
          
          <xsl:when test="string-length(@sc_intro) > 0">
            <xsl:text> &#xA;[ </xsl:text>
            <xsl:value-of select="$first"/>
            <xsl:text> ] &#xA;</xsl:text>
            <xsl:value-of select="substring( @sc_intro, 0, $script_len)" />
              <xsl:if test="string-length(@sc_intro) > $script_len">
                <xsl:text> ... </xsl:text>
              </xsl:if>    
          </xsl:when>          
                    
        </xsl:choose>          
      
      </xsl:if>  
            
    
<!--   


<xsl:text> // </xsl:text>
<xsl:value-of select="substring( ../@sql, 0, 66)" />
<xsl:text> = </xsl:text>
<xsl:value-of select="../@id" />       -->

    </xsl:attribute>
    <xsl:value-of select="@row" />
  </sel>
</xsl:template>


</xsl:stylesheet>

  
