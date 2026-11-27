<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015 Nicolas Prandstaetter. All rights reserved. 






 -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">
   <xsl:apply-templates/>
</xsl:template>

<xsl:template match="conf">
  <prolog>
  

    <xsl:choose>
      <xsl:when test="s2/@usage='app'">
        <call_job>
          <xsl:attribute name="job">
            <xsl:value-of select="s2/@jobname_prolog" />
          </xsl:attribute>
        </call_job>
      </xsl:when>
      <xsl:when test="s2/@usage='service'">
        <call_service>
          <xsl:attribute name="job">
            <xsl:value-of select="s2/@jobname_prolog" />
          </xsl:attribute>
        </call_service>
      </xsl:when>  
    </xsl:choose>

  </prolog>
</xsl:template>

</xsl:stylesheet>

  