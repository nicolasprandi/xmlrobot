<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="do_none" />
<xsl:output method="xml"/>

<xsl:template match="/">
  <sel>    

      <xsl:apply-templates select="dir/file"/>      
    
    
  </sel>
</xsl:template>

<xsl:template match="file">
  <sel>
    <xsl:value-of select="."/>
  </sel>
</xsl:template>

</xsl:stylesheet>

  