<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 
<xsl:output method="xml" indent="yes"/>
<xsl:template match="/">

  <xsl:element name="xsl:stylesheet">
    <xsl:attribute name="version">1.0</xsl:attribute>
    <!-- 
    <xsl:element name="xsl:include">
      <xsl:attribute name="href"><xsl:value-of select="$include1"/></xsl:attribute>
    </xsl:element>    
    -->
    <xsl:element name="xsl:output">
      <xsl:attribute name="method">text</xsl:attribute>
      <xsl:attribute name="encoding">UTF-8</xsl:attribute>
    </xsl:element>
    <xsl:element name="xsl:strip-space">
      <xsl:attribute name="elements">*</xsl:attribute>
    </xsl:element> 
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">/</xsl:attribute>
      <xsl:apply-templates select="cs/coltype" mode="header"/><xsl:text>,
</xsl:text>
      <xsl:element name="xsl:apply-templates">
        <xsl:attribute name="select">root/row</xsl:attribute>
      </xsl:element>      
    </xsl:element>

    <!-- 
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">rows</xsl:attribute>
      <xsl:element name="{cs/@sheet_name}"> 
        <xsl:element name="xsl:apply-templates">
          <xsl:attribute name="select">r</xsl:attribute>      
        </xsl:element>
      </xsl:element>      
    </xsl:element>
    -->
    <xsl:comment>Begin of line</xsl:comment> 
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">row</xsl:attribute>  
      <xsl:apply-templates select="cs/coltype" mode="data"/>
      <xsl:element name="xsl:text">,
</xsl:element>
    </xsl:element>  
    
<!-- Named template r for replacement -->  
<xsl:comment>Named Template r for replace</xsl:comment>
<xsl:element name="xsl:template">
  <xsl:attribute name="name">rep</xsl:attribute>
    <xsl:element name="xsl:param">
      <xsl:attribute name="name">s</xsl:attribute>
    </xsl:element>
    <xsl:element name="xsl:param">
      <xsl:attribute name="name">r</xsl:attribute>
    </xsl:element>
    <xsl:element name="xsl:param">
      <xsl:attribute name="name">w</xsl:attribute>
    </xsl:element>    
    <xsl:element name="xsl:choose">
        <xsl:element name="xsl:when">
          <xsl:attribute name="test">contains($s, $r)</xsl:attribute>        
            <xsl:element name="xsl:value-of">
              <xsl:attribute name="select">substring-before($s,$r)</xsl:attribute>
            </xsl:element>  
            <xsl:element name="xsl:value-of">
              <xsl:attribute name="select">$w</xsl:attribute>
            </xsl:element> 
                                    
            <xsl:element name="xsl:call-template">
              <xsl:attribute name="name">rep</xsl:attribute>
                <xsl:element name="xsl:with-param">
                  <xsl:attribute name="name">s</xsl:attribute>
                  <xsl:attribute name="select">substring-after($s,$r)</xsl:attribute>
                </xsl:element> 
                <xsl:element name="xsl:with-param">
                  <xsl:attribute name="name">r</xsl:attribute>
                  <xsl:attribute name="select">$r</xsl:attribute>
                </xsl:element> 
                <xsl:element name="xsl:with-param">
                  <xsl:attribute name="name">w</xsl:attribute>
                  <xsl:attribute name="select">$w</xsl:attribute>
                </xsl:element>                                               
            </xsl:element>    
        </xsl:element>
        <xsl:element name="xsl:otherwise">
            <xsl:element name="xsl:value-of">
              <xsl:attribute name="select">$s</xsl:attribute>
            </xsl:element>
        </xsl:element>
    </xsl:element>
</xsl:element>  <!-- End of named template r -->
  </xsl:element> <!-- </xsl_stylesheet> -->
</xsl:template>

<xsl:template match="coltype" mode="data">
      <xsl:element name="xsl:text">&quot;</xsl:element>
      <xsl:comment>Field: <xsl:value-of select="@name" /></xsl:comment> 
      <xsl:element name="xsl:call-template">
          <xsl:attribute name="name">rep</xsl:attribute>
          <xsl:text>
  </xsl:text>
          <xsl:element name="xsl:with-param">
            <xsl:attribute name="name">s</xsl:attribute>
            <xsl:attribute name="select">@<xsl:value-of select="@name"/></xsl:attribute>
          </xsl:element>
          <xsl:text>
  </xsl:text>          
          <xsl:element name="xsl:with-param">
            <xsl:attribute name="name">r</xsl:attribute>
            <xsl:text>&quot;</xsl:text>
          </xsl:element>
          <xsl:text>
  </xsl:text>          
          <xsl:element name="xsl:with-param">
            <xsl:attribute name="name">w</xsl:attribute>
            <xsl:text>&quot;&quot;</xsl:text>
          </xsl:element>                              
      </xsl:element>
      <xsl:comment>end of <xsl:value-of select="@name" />, separators ...</xsl:comment> 
      <xsl:element name="xsl:text">&quot;</xsl:element>
      
<!--

      <xsl:element name="xsl:value-of">
          <xsl:attribute name="select">translate(@<xsl:value-of select="translate(@name,' ','')"/>,',','_')</xsl:attribute>
      </xsl:element>
translate(@<xsl:value-of select="translate(@name,' ','')"/>,',','_')</xsl:attribute>
<xsl:call-template name="string-replace-all">
        <xsl:with-param name="s" select="@bad" />
        <xsl:with-param name="r">,</xsl:with-param>
        <xsl:with-param name="w">_</xsl:with-param>
    </xsl:call-template>

-->
  
  <xsl:choose>
    <xsl:when test="not(position()=last())">
      <xsl:element name="xsl:text">,</xsl:element>
    </xsl:when>
    <xsl:otherwise>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="coltype" mode="header">
  <xsl:value-of select="@name"/>
  <xsl:if test="not(position()=last())">
    <xsl:text>,</xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="coltype" mode="second">
  <xsl:text>'</xsl:text>
      <xsl:element name="xsl:value-of">
          <xsl:attribute name="select">@<xsl:value-of select="@name"/></xsl:attribute>
      </xsl:element>
  <xsl:text>'</xsl:text>
  
  <xsl:if test="not(position()=last())">
    <xsl:text>, </xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="coltype">
  <xsl:element name="{@name}">
      <xsl:element name="xsl:value-of">
          <xsl:attribute name="select">@<xsl:value-of select="@name"/></xsl:attribute>
      </xsl:element>
  </xsl:element>
</xsl:template>

</xsl:stylesheet>

  