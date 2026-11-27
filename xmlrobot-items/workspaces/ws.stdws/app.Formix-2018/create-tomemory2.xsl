<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015 Nicolas Prandstaetter. All rights reserved. 






 -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>
<xsl:include href="include-hooks2.xsl" />

<xsl:template match="/">
   <xsl:apply-templates/>
</xsl:template>

<xsl:template match="conf">

<!-- <job id="City to_memory" role="hidden" >  -->
<job role="hidden">
  <xsl:attribute name="id"><xsl:value-of select="s2/@job_city_tomemory" /></xsl:attribute> 
  
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">tomemory</xsl:with-param>
          <xsl:with-param name="hook">begin</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>
             
  <need><xsl:value-of select="s2/@prop_key" /></need>
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">tomemory</xsl:with-param>
          <xsl:with-param name="hook">0</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>
  <!-- 
  <xsl:text></xsl:text>
  <xsl:value-of select="s2/@" />
  <xsl:text></xsl:text>
  <xsl:value-of select="s2/@" />
  <xsl:text></xsl:text>
  <xsl:value-of select="s2/@" />
  <xsl:text></xsl:text>
  <xsl:value-of select="s2/@" />
  -->
    
  <!-- <prop id="city_dt_upd">(f,xpathx,,(file,cities),(prop,cities_offset)city[@row='(prop,rec_id)'],@dt_upd)</prop>  
       <prop id="city_dt_add">(f,xpathx,,(file,cities),(prop,cities_offset)city[@row='(prop,rec_id)'],@dt_add)</prop> -->
  <spirit act="open" missing_strategy="empty">
      <xsl:attribute name="id">
        <xsl:value-of select="s2/@spirit" />
      </xsl:attribute>
      <xsl:attribute name="xml">
        <xsl:value-of select="s2/@file_cities" />
      </xsl:attribute>      
  </spirit>
      
  <xsl:if test="string-length(s1/@dt_upd)>0">
  
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">tomemory</xsl:with-param>
          <xsl:with-param name="hook">0</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>  

    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="s2/@city_dt_upd" />
      </xsl:attribute>
      <xsl:text>(f,spirit,</xsl:text>
      <xsl:value-of select="s2/@spirit" />
      <xsl:text>,</xsl:text>
      <xsl:value-of select="s2/@prop_cities_offset" />
      <xsl:value-of select="s1/@singular_id" />
      <xsl:text>[@</xsl:text>
      <xsl:value-of select="s1/@id_attribute_name" />
      <xsl:text>=(f,xpath_string,</xsl:text> 
      <xsl:value-of select="s2/@prop_key" /> 
      <xsl:text>)],@dt_upd)</xsl:text>          
    </prop>    
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">tomemory</xsl:with-param>
          <xsl:with-param name="hook">0</xsl:with-param>
          <xsl:with-param name="station">1</xsl:with-param>
        </xsl:call-template>
  </xsl:if>
  
  <xsl:if test="string-length(s1/@dt_add)>0">
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">tomemory</xsl:with-param>
          <xsl:with-param name="hook">1</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>  
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="s2/@city_dt_add" />
      </xsl:attribute>
      <xsl:text>(f,spirit,</xsl:text>
      <xsl:value-of select="s2/@spirit" />
      <xsl:text>,</xsl:text>
      <xsl:value-of select="s2/@prop_cities_offset" />
      <xsl:value-of select="s1/@singular_id" />
      <xsl:text>[@</xsl:text>
      <xsl:value-of select="s1/@id_attribute_name" />
      <xsl:text>=(f,xpath_string,</xsl:text> 
      <xsl:value-of select="s2/@prop_key" /> 
      <xsl:text>)],@dt_add)</xsl:text>          
    </prop>    
    
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">tomemory</xsl:with-param>
          <xsl:with-param name="hook">1</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>
  </xsl:if>  
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">tomemory</xsl:with-param>
          <xsl:with-param name="hook">2</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>  
  <xsl:apply-templates select="fields/field" mode="tomemory"/>
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">tomemory</xsl:with-param>
          <xsl:with-param name="hook">2</xsl:with-param>
          <xsl:with-param name="station">1</xsl:with-param>
        </xsl:call-template> 
  <spirit act="close">
      <xsl:attribute name="id">
        <xsl:value-of select="s2/@spirit" />
      </xsl:attribute>   
  </spirit>  
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">tomemory</xsl:with-param>
          <xsl:with-param name="hook">end</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>          
</job>
</xsl:template>

<!-- ******************************************* -->
<xsl:template match="field" mode="tomemory">
  <!-- <prop id="city_name">(f,xpathx,,(file,cities),(prop,cities_offset)city[@row='(prop,rec_id)'],@name)</prop>
  -->
  <xsl:if test="not( @type = 'msg' )">
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">tomemory</xsl:with-param>
          <xsl:with-param name="hook">3</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>  
      <xsl:choose>
        <xsl:when test="@vartype='dir'">
        
          <dir flag="cool">
            <xsl:attribute name="id">
              <xsl:value-of select="//s1/@singular_id" />
              <xsl:text>_</xsl:text>
              <xsl:value-of select="@id" />    
            </xsl:attribute>
            <xsl:call-template name="x2" />       
          </dir>
          <!-- 
          <echo label="pos 1: gelesen" type="dir">
            <xsl:attribute name="id">
              <xsl:value-of select="//s1/@singular_id" />
              <xsl:text>_</xsl:text>
              <xsl:value-of select="@id" />    
            </xsl:attribute>       
          </echo>
          -->
          <unescape type="dir">
            <xsl:attribute name="id">
              <xsl:value-of select="//s1/@singular_id" />
              <xsl:text>_</xsl:text>
              <xsl:value-of select="@id" />    
            </xsl:attribute>          
          </unescape>
          <!--
          <echo label="pos 2: exported" type="dir">
            <xsl:attribute name="id">
              <xsl:value-of select="//s1/@singular_id" />
              <xsl:text>_</xsl:text>
              <xsl:value-of select="@id" />    
            </xsl:attribute>       
          </echo>
          -->                    
          <translate type="dir">
            <xsl:attribute name="id">
              <xsl:value-of select="//s1/@singular_id" />
              <xsl:text>_</xsl:text>
              <xsl:value-of select="@id" />    
            </xsl:attribute>          
          </translate>
          <!--
          <echo label="pos 3: translated" type="dir">
            <xsl:attribute name="id">
              <xsl:value-of select="//s1/@singular_id" />
              <xsl:text>_</xsl:text>
              <xsl:value-of select="@id" />    
            </xsl:attribute>       
          </echo>          
          -->
        </xsl:when>
        <xsl:when test="@vartype='file'">
          <file flag="cool">
            <xsl:attribute name="id">
              <xsl:value-of select="//s1/@singular_id" />
              <xsl:text>_</xsl:text>
              <xsl:value-of select="@id" />    
            </xsl:attribute>
            <xsl:call-template name="x2" />       
          </file>

          <unescape type="file">
            <xsl:attribute name="id">
              <xsl:value-of select="//s1/@singular_id" />
              <xsl:text>_</xsl:text>
              <xsl:value-of select="@id" />    
            </xsl:attribute>          
          </unescape>
          
          <translate type="file">
            <xsl:attribute name="id">
              <xsl:value-of select="//s1/@singular_id" />
              <xsl:text>_</xsl:text>
              <xsl:value-of select="@id" />    
            </xsl:attribute>          
          </translate>
          
        </xsl:when>      
        <xsl:otherwise>
          <prop>
            <xsl:call-template name="x1"/>
          </prop>
        </xsl:otherwise>
      </xsl:choose>  
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">tomemory</xsl:with-param>
          <xsl:with-param name="hook">3</xsl:with-param>
          <xsl:with-param name="station">1</xsl:with-param>
        </xsl:call-template>    
   </xsl:if>
       
</xsl:template>

<xsl:template name="x1">
    <xsl:attribute name="id">
      <xsl:value-of select="//s1/@singular_id" />
      <xsl:text>_</xsl:text>
      <xsl:value-of select="@id" />    
    </xsl:attribute>
    <xsl:call-template name="x2" />

</xsl:template>

<xsl:template name="x2">
    <!-- 
    <xsl:text>(f,xpathx,,</xsl:text>
    <xsl:value-of select="//s2/@file_cities" />
    <xsl:text>,</xsl:text>
    <xsl:value-of select="//s2/@prop_cities_offset" />
    <xsl:value-of select="//s1/@singular_id" />
    <xsl:text>[@</xsl:text>
    <xsl:value-of select="//s1/@id_attribute_name" />   
    <xsl:text>='</xsl:text>
    <xsl:value-of select="//s2/@prop_key" />
    <xsl:text>'],@</xsl:text>
    <xsl:value-of select="@id" />
    <xsl:text>)</xsl:text> 
    -->
    
    <xsl:text>(f,spirit,</xsl:text>
    <xsl:value-of select="//s2/@spirit" />
    <xsl:text>,</xsl:text>
    <xsl:value-of select="//s2/@prop_cities_offset" />
    <xsl:value-of select="//s1/@singular_id" />
    <xsl:text>[@</xsl:text>
    <xsl:value-of select="//s1/@id_attribute_name" />
    <xsl:text>=(f,xpath_string,</xsl:text> 
    <xsl:value-of select="//s2/@prop_key" /> 
    <xsl:text>)],@</xsl:text>
    <xsl:value-of select="@id" />
    <xsl:text>)</xsl:text>             

</xsl:template>


</xsl:stylesheet>

  