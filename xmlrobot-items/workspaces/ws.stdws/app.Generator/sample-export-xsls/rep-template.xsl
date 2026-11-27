<?xml version="1.0" encoding="UTF-8"?>
<!-- This file was inspired by the website:
      http://stackoverflow.com/questions/3067113/xslt-string-replace
     and was modified by nicolas prandstaetter
-->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias">
<!-- Named template r for replacement -->
  <xsl:template name="get-rep">
  <xsl:comment>rep, replace template</xsl:comment>
  <axsl:template name="rep">
  <axsl:param name="s"/>
  <axsl:param name="r"/>
  <axsl:param name="w"/>
  <axsl:choose>
  <axsl:when test="contains($s, $r)">
  <axsl:value-of select="substring-before($s,$r)"/>
  <axsl:value-of select="$w"/>
  <axsl:call-template name="rep">
  <axsl:with-param name="s" select="substring-after($s,$r)"/>
  <axsl:with-param name="r" select="$r"/>
  <axsl:with-param name="w" select="$w"/>
  </axsl:call-template>
  </axsl:when>
  <axsl:otherwise>
  <axsl:value-of select="$s"/>
  </axsl:otherwise>
  </axsl:choose>
  </axsl:template>
</xsl:template>


</xsl:stylesheet>

  
