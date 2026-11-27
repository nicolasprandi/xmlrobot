<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias">
<xsl:namespace-alias stylesheet-prefix="axsl" result-prefix="xsl"/>   
<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">
        
  <xsl:element name="xsl:stylesheet">
    <xsl:attribute name="version">1.0</xsl:attribute>   
    <xsl:element name="xsl:output">
      <xsl:attribute name="method">text</xsl:attribute>
    </xsl:element>
        
    <xsl:comment>Json Template for each data-row</xsl:comment>
    <xsl:comment>Make your updates here</xsl:comment>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">row</xsl:attribute>
      <axsl:text>{</axsl:text>  
      <xsl:apply-templates select="cs/coltype" mode="third"/>
      <axsl:text>}</axsl:text>
      <axsl:if>
        <xsl:attribute name="test">
          <xsl:text>position() != last()</xsl:text>
        </xsl:attribute>
      <axsl:text>,
</axsl:text>
      </axsl:if>
    </xsl:element>
    <xsl:comment>These are basic templates, dont make updates here</xsl:comment>
    <xsl:comment>Template for Main-Entry: basic xml entry</xsl:comment>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">/</xsl:attribute>
      <xsl:element name="xsl:apply-templates">
        <xsl:attribute name="select">root</xsl:attribute>
      </xsl:element>
    </xsl:element>

    <xsl:comment>Template for Main-Entry: root</xsl:comment>
    <xsl:element name="xsl:template">
      <xsl:attribute name="match">root</xsl:attribute>
      <axsl:text>[</axsl:text> 
        <xsl:element name="xsl:apply-templates">
          <xsl:attribute name="select">row</xsl:attribute>       
        </xsl:element>
      <axsl:text>]</axsl:text>  
    </xsl:element>

    <xsl:comment>Template for Json String values</xsl:comment>
    <axsl:template name="ojson_string">
      <axsl:param name="x"/>
      <axsl:choose>
        <axsl:when>
          <xsl:attribute name="test"><xsl:text>string-length($x)&gt;0</xsl:text></xsl:attribute>
            <axsl:call-template>
                <xsl:attribute name="name">escape-string</xsl:attribute>
                <axsl:with-param name="s">
                  <xsl:attribute name="select"><xsl:text>$x</xsl:text></xsl:attribute>
                </axsl:with-param>                              
            </axsl:call-template>      
        </axsl:when>  
        <axsl:otherwise>
          <axsl:text>null</axsl:text> 
        </axsl:otherwise>
      
      </axsl:choose>         
      
    </axsl:template>
    
    <xsl:comment>Template for Json Number values</xsl:comment>
    <axsl:template name="ojson_number">
      <axsl:param name="x"/>
      <axsl:choose>
        <axsl:when>
          <xsl:attribute name="test"><xsl:text>string-length($x)&gt;0</xsl:text></xsl:attribute>          
            <axsl:value-of>
              <xsl:attribute name="select"><xsl:text>$x</xsl:text></xsl:attribute>
            </axsl:value-of>                
        </axsl:when>  
        <axsl:otherwise>
          <axsl:text>null</axsl:text> 
        </axsl:otherwise>
      
      </axsl:choose>         
      
    </axsl:template>    
    
    <xsl:comment>Template for Json Boolean values</xsl:comment>
    <axsl:template name="ojson_bool">
      <axsl:param name="x"/>
      <axsl:choose>
        <axsl:when>
          <xsl:attribute name="test"><xsl:text>string-length($x)&gt;0</xsl:text></xsl:attribute>          
            <axsl:choose>
              <axsl:when>
                <xsl:attribute name="test"><xsl:text>$x=1</xsl:text></xsl:attribute>          
                  <axsl:text>true</axsl:text>                 
              </axsl:when>  
              <axsl:otherwise>
                <axsl:text>false</axsl:text> 
              </axsl:otherwise>            
            </axsl:choose>              
        </axsl:when>  
        <axsl:otherwise>
          <axsl:text>null</axsl:text> 
        </axsl:otherwise>
      
      </axsl:choose>         
      
    </axsl:template>       
    
    <xsl:comment>Basic template for json string conversion with special characters</xsl:comment>
    <xsl:call-template name="get-rep" />
    

     
  </xsl:element> <!-- </xsl_stylesheet> -->
</xsl:template>
<xsl:include href="rep-json-escape.xsl"/>

<xsl:template match="coltype" mode="third">

  <xsl:call-template name="coltype2">
    <xsl:with-param name="name"><xsl:value-of select="@name"/></xsl:with-param>
  </xsl:call-template> 
  
</xsl:template>

<xsl:template name="coltype2">
<xsl:param name="name"/>

  <xsl:text>&quot;</xsl:text>
  <xsl:value-of select="$name"/>
  <xsl:text>&quot;:</xsl:text>
 
  <xsl:choose>
    <xsl:when test="/cs/tips/numeric/split[@x=$name]">
      <!-- this is a numeric value -->
      <axsl:call-template name="ojson_number">
          <axsl:with-param name="x">
            <xsl:attribute name="select">@<xsl:value-of select="$name"/></xsl:attribute>
          </axsl:with-param>                               
      </axsl:call-template>   
    </xsl:when>
    <xsl:when test="/cs/tips/bool/split[@x=$name]">
      <!-- this is a bool value -->
      <axsl:call-template name="ojson_bool">
          <axsl:with-param name="x">
            <xsl:attribute name="select">@<xsl:value-of select="$name"/></xsl:attribute>
          </axsl:with-param>                               
      </axsl:call-template>   
    </xsl:when>    
    <xsl:otherwise>
      <!-- this is a string value -->
      <axsl:call-template name="ojson_string">
          <axsl:with-param name="x">
            <xsl:attribute name="select">@<xsl:value-of select="$name"/></xsl:attribute>
          </axsl:with-param>                               
      </axsl:call-template>        
    </xsl:otherwise>
  </xsl:choose>
  
  <xsl:if test="not(position()=last())">
    <xsl:text>, </xsl:text>
  </xsl:if>
</xsl:template>



</xsl:stylesheet>

  