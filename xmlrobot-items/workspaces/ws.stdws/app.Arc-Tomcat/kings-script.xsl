<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:param name="ti_test" />
  <!-- 

<xsl:param name="os" /> -->

<xsl:output method="xml" indent="yes"/>
<xsl:template match="/">

  <steps>
  
        <xsl:apply-templates select="//vars/var">    
          <!-- <xsl:sort select="@sort"/> -->
        </xsl:apply-templates> 
  </steps>

</xsl:template>

<xsl:template match="var">
  
  <xsl:choose>
    <xsl:when test="@type='dir'">
      <dir>
        <xsl:attribute name="id">
          <xsl:value-of select="@id"/>
        </xsl:attribute>
        <xsl:attribute name="x">
          <xsl:value-of select="@x"/>
        </xsl:attribute>               
      </dir>
      
      <ti type="dir">
        <xsl:attribute name="label">
          <xsl:value-of select="@label"/>
        </xsl:attribute>  
        <xsl:attribute name="test">
          <xsl:value-of select="$ti_test"/>
        </xsl:attribute>               
        <xsl:text>(dir,</xsl:text>
        <xsl:value-of select="@id" />
        <xsl:text>)</xsl:text>
      </ti>

    </xsl:when>
    
    <xsl:when test="@type='file'">
      <file>
        <xsl:attribute name="id">
          <xsl:value-of select="@id"/>
        </xsl:attribute>
        <xsl:attribute name="x">
          <xsl:value-of select="@x"/>
        </xsl:attribute>               
      </file>
    </xsl:when>    
    
    <xsl:when test="@type='prop'">
      <prop>
        <xsl:attribute name="id">
          <xsl:value-of select="@id"/>
        </xsl:attribute>
        <xsl:attribute name="x">
          <xsl:value-of select="@x"/>
        </xsl:attribute>               
      </prop>
    </xsl:when>      
  
  </xsl:choose>
    <!-- 
    <xsl:call-template name="var2">
      <xsl:with-param name="type" select="@type" />
    </xsl:call-template>  -->
    
</xsl:template>


<xsl:template name="var2">
<!-- 
  <xsl:choose>
    <xsl:when test="$type=">
  
  </xsl:choose>
  <var2/>
-->
</xsl:template>


<xsl:template match="var3">

</xsl:template>

<xsl:template match="var4">

  <echo>
    <xsl:attribute name="id">
      <xsl:value-of select="@id"/>
    </xsl:attribute>
    <xsl:attribute name="type">
      <xsl:value-of select="@type"/>
    </xsl:attribute> 
    <xsl:attribute name="x">
      <xsl:value-of select="@x"/>
    </xsl:attribute>        
  </echo>

</xsl:template>

<xsl:template name="inner-var">

    <xsl:attribute name="id">
      <xsl:value-of select="@id"/>
    </xsl:attribute>
    <xsl:attribute name="type">
      <xsl:value-of select="@type"/>
    </xsl:attribute> 
    <xsl:attribute name="x">
      <xsl:value-of select="@x"/>
    </xsl:attribute>        


</xsl:template>


</xsl:stylesheet>

  
