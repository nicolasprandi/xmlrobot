<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2021 Nicolas Prandstaetter.  All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="new"/> 

<xsl:template match="/">
<sel>
<xsl:apply-templates select="//dsi"/>

 
<sel> 
  <xsl:attribute name="label"><xsl:value-of select="$new"/></xsl:attribute>
  <xsl:text>%new</xsl:text>
</sel>
<!--
<sel> 
  <xsl:attribute name="label"><xsl:value-of select="$end"/></xsl:attribute>
  <xsl:text>%end</xsl:text>
</sel>

<sel> 
  <xsl:attribute name="label"><xsl:value-of select="$back"/></xsl:attribute>
  <xsl:text>%back</xsl:text>
</sel>
<sel id="%back"><xsl:value-of select="$back"/></sel>            
<sel id="%end"><xsl:value-of select="$end"/></sel>
</sel>                 -->
</sel>
</xsl:template>

<xsl:template match="dsi">

  <sel>
    <xsl:attribute name="label"><xsl:value-of select="@name"/></xsl:attribute>
    <xsl:text><xsl:value-of select="@row"/></xsl:text>
  </sel>

</xsl:template> 
 

</xsl:stylesheet>

  
