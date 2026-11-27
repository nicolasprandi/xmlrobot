<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>


<xsl:template match="/">
  <sels>
   <sel label="Others">o</sel>
   <xsl:apply-templates select="groups/group" >
    <xsl:sort select="@sort" data-type="number"/>
   </xsl:apply-templates>
  </sels>
</xsl:template>

<xsl:template match="group">
	<sel>
	 <xsl:attribute name="label">
	   <xsl:value-of select="@title"/>
	 </xsl:attribute>
	 <xsl:value-of select="@id"/>  
	</sel>
</xsl:template>

</xsl:stylesheet>

  