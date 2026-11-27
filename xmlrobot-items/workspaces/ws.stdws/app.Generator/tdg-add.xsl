<?xml version="1.0" encoding="UTF-8"?>

<!-- This file was inspired by the website:
      http://stackoverflow.com/questions/3067113/xslt-string-replace
     and was modified by nicolas prandstaetter
-->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>
<xsl:template name="rep">
    <xsl:param name="s" />
    <xsl:param name="r" />
    <xsl:param name="w" />
    <xsl:choose>
        <xsl:when test="contains($s, $r)">
            <xsl:value-of select="substring-before($s,$r)" />
            <xsl:value-of select="$w" />
            <xsl:call-template name="rep">
                <xsl:with-param name="s" select="substring-after($s,$r)" />
                <xsl:with-param name="r" select="$r" />
                <xsl:with-param name="w" select="$w" />
            </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
            <xsl:value-of select="$s" />
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

</xsl:stylesheet>

  
