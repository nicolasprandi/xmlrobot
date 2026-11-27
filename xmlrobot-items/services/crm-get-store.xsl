<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="action" />
<xsl:output method="xml"/>

<xsl:template match="/">
  <xsl:apply-templates select="history/action[@id=$action]"/>
</xsl:template>
<xsl:template match="action" >
    <xsl:copy-of select="steps"/>
</xsl:template>
</xsl:stylesheet>

  