<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2021 Nicolas Prandstaetter.  All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
<ots>
<xsl:apply-templates select="root/ots/ot"/> 
</ots>
</xsl:template>

<xsl:template match="ot">
  <ot>
    <xsl:attribute name="tp">
      <xsl:value-of select="@obj_typ"/>
    </xsl:attribute>
    <xsl:attribute name="tc">
      <xsl:value-of select="@tech_name"/>
    </xsl:attribute>    
    <xsl:attribute name="nm">
      <xsl:value-of select="@name"/>
    </xsl:attribute>       
    <xsl:attribute name="np">
      <xsl:value-of select="@name_plural"/>
    </xsl:attribute>       
     <!-- <xsl:copy-of select="@typ|@tec|@name|"/> -->
  </ot>
  <!-- 
  <xsl:call-template name="ot2">
    <xsl:with-param name="obj_tech_name" select="@tech_name"/>
    <xsl:with-param name="obj_typ" select="$obj_typ"/>
  </xsl:call-template>     -->
</xsl:template>    

</xsl:stylesheet>

  
