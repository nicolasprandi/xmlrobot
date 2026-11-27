<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias">
<xsl:namespace-alias stylesheet-prefix="axsl" result-prefix="xsl"/>  
<xsl:output method="xml" indent="yes"/> 

<xsl:template match="/">

  <axsl:stylesheet version="1.0"> 
    <axsl:output method="text"/>
    
    <xsl:comment>Sql Template for each data-row</xsl:comment>
    <xsl:comment>Make your updates here</xsl:comment>
    <axsl:template match="row">  
  <xsl:text>INSERT INTO </xsl:text>
     <xsl:value-of select="translate(cs/@sheet_name_good,' ','')"/>
      <xsl:text> (</xsl:text><xsl:apply-templates select="cs/coltype" mode="first"/><xsl:text>) VALUES (</xsl:text><axsl:text>
      </axsl:text><xsl:apply-templates select="cs/coltype" mode="second"/><axsl:text>);
</axsl:text></axsl:template>  

    <xsl:comment>These are basic templates, dont make updates here</xsl:comment>
    <xsl:comment>Template for Main-Entry: basic xml entry</xsl:comment>
    <axsl:template match="/">
      <axsl:apply-templates select="root"/>
    </axsl:template>

    <xsl:comment>Template for Main-Entry: root</xsl:comment>
    <axsl:template match="root">
      <!-- <xsl:element name="{translate(cs/@sheet_name,' ','')}">   -->
        <axsl:apply-templates select="row"/>
      <!-- </xsl:element>     --> 
    </axsl:template>
    
    <xsl:comment>Template for Sql String values</xsl:comment>
    <axsl:template name="osql_string">
      <axsl:param name="x"/>
      <axsl:choose>
      <axsl:when test="string-length($x)&gt;0">'<axsl:call-template name="rep"><axsl:with-param name="s" select="$x"/>
          <axsl:with-param name="r">'</axsl:with-param>
          <axsl:with-param name="w">''</axsl:with-param>
      </axsl:call-template>'</axsl:when>
      <axsl:otherwise>null</axsl:otherwise>
      </axsl:choose>
    </axsl:template>   
    
    <xsl:comment>Template for Sql Number values</xsl:comment>
    <axsl:template name="osql_number">
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
    
    <xsl:comment>Template for Sql Boolean (true,false) values</xsl:comment>
    <axsl:template name="osql_bool_tf">
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
    
    <xsl:comment>Template for Sql Boolean (0,1) values</xsl:comment>
    <axsl:template name="osql_bool_01">
      <axsl:param name="x"/>
      <axsl:choose>
        <axsl:when>
          <xsl:attribute name="test"><xsl:text>string-length($x)&gt;0</xsl:text></xsl:attribute>          
            <axsl:choose>
              <axsl:when>
                <xsl:attribute name="test"><xsl:text>$x=1</xsl:text></xsl:attribute>          
                  <axsl:text>1</axsl:text>                 
              </axsl:when>  
              <axsl:otherwise>
                <axsl:text>0</axsl:text> 
              </axsl:otherwise>            
            </axsl:choose>                
        </axsl:when>  
        <axsl:otherwise>
          <axsl:text>null</axsl:text> 
        </axsl:otherwise>
      
      </axsl:choose>         
      
    </axsl:template>          
    
    <!-- Named template r for replacement -->
    <xsl:call-template name="get-rep" />

  
  </axsl:stylesheet>
</xsl:template>
<!-- here comes the rep template -->
<xsl:include href="rep-template.xsl"/>


<xsl:template match="coltype" mode="first">
  <xsl:value-of select="@name"/>
  <xsl:if test="not(position()=last())">
    <xsl:text>, </xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="coltype" mode="second">

  <xsl:call-template name="coltype2">
    <xsl:with-param name="name"><xsl:value-of select="@name"/></xsl:with-param>
  </xsl:call-template> 
  
</xsl:template>

<xsl:template name="coltype2">
<xsl:param name="name"/>

  <xsl:comment><xsl:value-of select="$name"/></xsl:comment>
  
  <xsl:choose>
    <xsl:when test="/cs/tips/numeric/split[@x=$name]">
      <!-- this is a numeric value -->
      <axsl:call-template name="osql_number">
          <axsl:with-param name="x">
            <xsl:attribute name="select">@<xsl:value-of select="$name"/></xsl:attribute>
          </axsl:with-param>                             
      </axsl:call-template>  
            
    </xsl:when>
    <xsl:when test="/cs/tips/bool/split[@x=$name]">
      <!-- this is a boolean value -->
      <xsl:choose>
        <xsl:when test="/cs/tips/bool[@sql_strategy='true_false']">
          <axsl:call-template name="osql_bool_tf">
              <axsl:with-param name="x">
                <xsl:attribute name="select">@<xsl:value-of select="$name"/></xsl:attribute>
              </axsl:with-param>                             
          </axsl:call-template>          
        </xsl:when>
        <xsl:otherwise>
          <axsl:call-template name="osql_bool_01">
              <axsl:with-param name="x">
                <xsl:attribute name="select">@<xsl:value-of select="$name"/></xsl:attribute>
              </axsl:with-param>                             
          </axsl:call-template>          
        </xsl:otherwise>
      </xsl:choose>

            
    </xsl:when>    
    <xsl:otherwise>
      <axsl:call-template name="osql_string">
          <axsl:with-param name="x">
            <xsl:attribute name="select">@<xsl:value-of select="$name"/></xsl:attribute>
          </axsl:with-param>                             
      </axsl:call-template>  
    </xsl:otherwise>
  </xsl:choose>
  
  <xsl:if test="not(position()=last())"><axsl:text>, </axsl:text></xsl:if>
</xsl:template>

<xsl:template match="coltype">
  <xsl:element name="{translate(@name,' ','')}">
      <xsl:element name="xsl:value-of">
          <xsl:attribute name="select">@<xsl:value-of select="@name"/></xsl:attribute>
      </xsl:element>
  </xsl:element>
</xsl:template>

</xsl:stylesheet>

  