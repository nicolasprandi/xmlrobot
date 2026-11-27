<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2019 Nicolas Prandstaetter. All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="do_none" />
<xsl:output method="xml"/>

<xsl:template match="/">
  <jobsteps>    
    <tk type="sel" id="pkg" label="Select a Package">
      <xsl:apply-templates select="//file"/>      
      <xsl:if test="$do_none='1'">
        <sel label="+ None of these, Something New!">none_2871</sel>
      </xsl:if>      
    </tk>      
  </jobsteps>
</xsl:template>

<xsl:template match="file">
  <sel>
    <xsl:value-of select="@pre"/>
  </sel>
</xsl:template>

</xsl:stylesheet>

  