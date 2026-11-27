<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015 Nicolas Prandstaetter. All rights reserved. 

 -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  


<xsl:output method="xml"/>

<xsl:template match="/">
   <xsl:apply-templates/>
</xsl:template>

<xsl:template name="FirstCase">
  <xsl:param name="text"/>
  <xsl:value-of select="translate(substring($text,1,1),'abcdefghijklmnopqrstuvsxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
  <xsl:value-of select="substring($text,2)"/>
</xsl:template>

<xsl:template match="action">
  <action>
    <xsl:copy-of select="@*"/>

    
    <xsl:attribute name="firstcase_action_id">
      <xsl:call-template name="FirstCase">
        <xsl:with-param name="text"><xsl:value-of select="@id" /></xsl:with-param>
      </xsl:call-template>
    </xsl:attribute> 
        
              
  </action>
</xsl:template>

<xsl:template match="actions">
  <actions>
    <xsl:copy-of select="@*"/>
    <xsl:apply-templates select="action" />
  </actions>
</xsl:template>



</xsl:stylesheet>

  