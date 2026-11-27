<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2021 Nicolas Prandstaetter.  All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="attr_typ"/>

<xsl:template match="/">
<at>

<xsl:apply-templates select="root/ats/at[@attr_typ=$attr_typ]"/> 

</at>

</xsl:template>                   

<xsl:template match="at">
  <xsl:copy-of select="@*"/>
</xsl:template>


</xsl:stylesheet>

  
