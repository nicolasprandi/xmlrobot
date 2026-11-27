<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="ipath"/>
<xsl:param name="max_dir_level"/>
<xsl:output method="xml"/>

<xsl:template match="/">
  <xsl:apply-templates select="dir" mode="root" />
</xsl:template>

<xsl:template match="dir" mode="root">
  <dir>    
    <xsl:choose>
      <xsl:when test="string-length($ipath)>0">
        <xsl:copy-of select="@dir"/>
        <xsl:apply-templates select="//dir[@ipath=$ipath]" mode="ipath"/>
      </xsl:when>
      <xsl:otherwise>
      	 <xsl:copy-of select="@*|aggs[@level&lt;=$max_dir_level]"/>
         <xsl:apply-templates select="dir|file" mode="in"/> 
      </xsl:otherwise>
    </xsl:choose>
  </dir>
</xsl:template>

<xsl:template match="dir" mode="ipath">
	<dir>
	 <xsl:attribute name="upper_ipath">
	   <xsl:value-of select="../@ipath"/>
	 </xsl:attribute>	
	 <xsl:copy-of select="@*|aggs[@level&lt;=$max_dir_level]"/>

   <xsl:apply-templates select="dir|file" mode="in"/>  
	</dir>
</xsl:template>

<xsl:template match="dir" mode="in">
  <xsl:choose>
    <xsl:when test="@level&lt;=$max_dir_level">
    	<dir>
    	 <xsl:copy-of select="@*|aggs[@level&lt;=$max_dir_level]"/>
       <xsl:apply-templates select="dir|file" mode="in"/>  
    	</dir>
    </xsl:when>
    <xsl:otherwise>
    	<dir>
    	 <xsl:copy-of select="@*"/>
        <xsl:apply-templates select="dir" mode="out"/>  
    	</dir>    
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>
<xsl:template match="dir" mode="out">
    	<dir>
    	   <xsl:copy-of select="@*"/>
         <xsl:apply-templates select="dir" mode="out"/>  
    	</dir> 
</xsl:template>

<xsl:template match="file" mode="in">
	<file>
	 <xsl:copy-of select="@*"/>
   <xsl:text><xsl:apply-templates select="text()"/></xsl:text>  
	</file>
</xsl:template>



</xsl:stylesheet>

  