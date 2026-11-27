<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015 Nicolas Prandstaetter. All rights reserved. 
 -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
<xsl:template name="get_doku">
<xsl:param name="doku_to_cut"/>

  <xsl:if test="string-length($doku_to_cut) > 0">
    <xsl:text> &#xA;[ </xsl:text><xsl:value-of select="$doku" /><xsl:text> ] &#xA;</xsl:text>
    <xsl:choose>
      <xsl:when test="contains(substring($doku_to_cut,0,$dokuw1),'.')">
        <!-- es gibt einen punkt in dokuw1-->    
        <xsl:value-of select="substring-before($doku_to_cut,'.')" />
      </xsl:when>    
      <xsl:when test="string-length($doku_to_cut)>$dokuw2">
        <xsl:value-of select="substring($doku_to_cut,0,$dokuw2)" />
        <xsl:text> ... </xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$doku_to_cut" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:if>
    
</xsl:template>

</xsl:stylesheet>