<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>


<xsl:template match="/">
  <groups>
   <xsl:apply-templates select="groups/group[@active=1]" >
    <xsl:sort select="@sort" data-type="number"/>
   </xsl:apply-templates>
  </groups>
</xsl:template>

<xsl:template match="group">
	<group>
	 <xsl:copy-of select="@title|@sort|@active"/>
   <xsl:apply-templates select="extension[@active=1]">
    <xsl:sort select="@sort" data-type="number"/>
   </xsl:apply-templates>   
	</group>
</xsl:template>

<xsl:template match="extension">
  <extension>
    <xsl:copy-of select="@extension|@sort|@active"/>
  </extension>
</xsl:template>



</xsl:stylesheet>

  