<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml"/>

<xsl:template match="groups">
  <groups>
    <xsl:copy-of select="@*"/>
    <xsl:apply-templates/>
  </groups>
</xsl:template>

<xsl:template match="group">
<group>
  <xsl:attribute name="sum_kb_group">
    <xsl:value-of select="sum(extension/@sum_kb)"/>
  </xsl:attribute>
  <xsl:attribute name="sum_count_group">
    <xsl:value-of select="sum(extension/@sum_count)"/>
  </xsl:attribute>
  <xsl:copy-of select="@*|node()"/>  
</group>
</xsl:template>

</xsl:stylesheet>

  