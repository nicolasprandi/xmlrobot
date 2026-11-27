<?xml version="1.0" encoding="UTF-8"?>

<!-- Copyright © 2018-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:template match="/">
<sel>
  <xsl:apply-templates select="dir/dir">
    <xsl:sort select="@rpath"/>
  </xsl:apply-templates>
</sel>
</xsl:template>

<xsl:template match="dir">
  
  <xsl:if test="file[text()='official-book']">


    <sel><xsl:value-of select="@rpath" /></sel>

  </xsl:if>
                                               <!-- 
   
       -->
    
</xsl:template>


</xsl:stylesheet>

  
