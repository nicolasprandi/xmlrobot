<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2021 Nicolas Prandstaetter.  All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="object_typ"/>
<xsl:param name="usage"/>

<xsl:template match="/">
<ot>

<xsl:apply-templates select="root/ots/ot[@obj_typ=$object_typ]"/> 

 

<xsl:apply-templates select="root/aus/au[@obj_typ=$object_typ]"/> 


</ot>

</xsl:template>

<xsl:template match="au">
  <xsl:call-template name="aus2">
    <xsl:with-param name="attr_typ"><xsl:value-of select="@attr_typ"/></xsl:with-param>
  </xsl:call-template>
</xsl:template>
<xsl:template name="aus2">
  <xsl:param name="attr_typ"/>
  <xsl:apply-templates select="//root/ats/at[@attr_typ=$attr_typ]"/>
</xsl:template>


<xsl:template match="at">
  <at>
    <xsl:attribute name="tp">
      <xsl:value-of select="@attr_typ"/>
    </xsl:attribute>
    <xsl:attribute name="vt">
      <xsl:value-of select="@final_typ"/>
    </xsl:attribute>    
    <xsl:if test="string-length(@unit) &gt; 0">
      <xsl:attribute name="u">
        <xsl:value-of select="@unit"/>
      </xsl:attribute>   
    </xsl:if>   
    
    <xsl:if test="string-length(@len) &gt; 0">
      <xsl:attribute name="l">
        <xsl:value-of select="@len"/>
      </xsl:attribute>   
    </xsl:if>     
                           
    <xsl:if test="$usage='tc'">
      <xsl:attribute name="x">
        <xsl:value-of select="@tech_name"/>
      </xsl:attribute>    
    </xsl:if>
    <xsl:if test="$usage='nm'">
      <xsl:attribute name="x">
        <xsl:value-of select="@name"/>
      </xsl:attribute>   
    </xsl:if>    
    
    <xsl:if test="string-length(@gui_typ) &gt; 0">
      <xsl:attribute name="gt">
        <xsl:value-of select="@gui_typ"/>
      </xsl:attribute>   
    </xsl:if>       
                
  </at>

</xsl:template> 


<xsl:template match="ot">

  <xsl:attribute name="tp">
    <xsl:value-of select="@obj_typ"/>
  </xsl:attribute>
   
  <xsl:if test="$usage='tc'">
    <xsl:attribute name="x">
      <xsl:value-of select="@tech_name"/>
    </xsl:attribute>    
  </xsl:if>
  
  <xsl:if test="$usage='nm'">
    <xsl:attribute name="x">
      <xsl:value-of select="@name"/>
    </xsl:attribute>   
  </xsl:if>  
 

</xsl:template> 

</xsl:stylesheet>

  
