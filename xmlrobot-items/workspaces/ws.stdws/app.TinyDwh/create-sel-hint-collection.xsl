<?xml version="1.0" encoding="UTF-8"?>

<!-- Copyright © 2018-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml"/>

<xsl:template match="/">
<sels>
<xsl:apply-templates select="dir/file"/>
</sels> 
</xsl:template>

<xsl:template match="file">
  <sel><xsl:attribute name="label"><xsl:value-of select="@pre" /></xsl:attribute><xsl:value-of select="node()" /></sel>
</xsl:template>  

</xsl:stylesheet>

  
