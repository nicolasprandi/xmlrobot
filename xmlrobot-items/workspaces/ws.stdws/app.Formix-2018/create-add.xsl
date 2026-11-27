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

<!-- <job id="City Details" role="hidden">  -->
<job>
  <xsl:attribute name="id"><xsl:value-of select="s2/@job_city_add" /></xsl:attribute>
  <xsl:attribute name="level">
    <xsl:value-of select="s1/@job_level" />
  </xsl:attribute>    
  <xsl:attribute name="role">
    <xsl:choose>
      <xsl:when test="string-length(s1/@job_role_add)>0">
        <xsl:value-of select="s1/@job_role_add" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>hidden</xsl:text>
      </xsl:otherwise>
    </xsl:choose>    
  </xsl:attribute>  
  
  
    <xsl:attribute name="role">
    <xsl:value-of select="s1/@job_role_add" />
  </xsl:attribute>  
     
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">begin</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>
x<prop id="isAdd">1</prop>

                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">0</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>
  <need default="1">(prop,do_return_job)</need>   
  <xsl:choose>
    <xsl:when test="s2/@usage='app'">
      <call_job>
        <xsl:attribute name="job"><xsl:value-of select="s2/@job_city_editor" /></xsl:attribute>   
      </call_job>  
    </xsl:when>
    <xsl:when test="s2/@usage='service'">
      <call_service>
        <xsl:attribute name="job"><xsl:value-of select="s2/@job_city_editor" /></xsl:attribute>   
      </call_service> 
    </xsl:when>  
  </xsl:choose>   
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">1</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template> 
  <prop>
    <xsl:attribute name="id">
      <xsl:value-of select="s2/@key" />
    </xsl:attribute>
    <xsl:text>1</xsl:text>
  </prop>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">2</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>   
  <when>
    <xsl:attribute name="test"> 
      <xsl:text>(f,not,(f,exists,prop,</xsl:text>
      <xsl:value-of select="s2/@cities_offset_new_key" />
      <xsl:text>))</xsl:text>
    </xsl:attribute>  
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">3</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>     
    <prop id="cities_offset_new_key">
      <xsl:attribute name="id"> 
        <xsl:value-of select="s2/@cities_offset_new_key" />
      </xsl:attribute> 
      <xsl:text>//</xsl:text>
    </prop>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">4</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>     
  </when>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">5</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>     
  <loop id="max" limit="1" flag="back">  
    <xsl:attribute name="xml"> 
      <xsl:value-of select="s2/@file_cities" />
    </xsl:attribute> 
    <xsl:attribute name="sort">
      <xsl:text>@</xsl:text> 
      <xsl:value-of select="s1/@id_attribute_name" />
    </xsl:attribute> 
    <xsl:attribute name="number">
      <xsl:text>@</xsl:text> 
      <xsl:value-of select="s1/@id_attribute_name" />
    </xsl:attribute> 
    <xsl:attribute name="xpath"> 
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="s2/@cities_offset_new_key" />
      <xsl:text>)</xsl:text>
      <xsl:value-of select="s1/@singular_id" />
    </xsl:attribute>  
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">6</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>     
    <trace>
        <xsl:text>max key=(loop,max,@</xsl:text>
        <xsl:value-of select="s1/@id_attribute_name" />
        <xsl:text>)</xsl:text>      
    </trace>  
    <trace>
        <xsl:text>i=(loop,max,#i)</xsl:text>   
    </trace>    
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">7</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>              
    <prop>
        <xsl:attribute name="id">
          <xsl:value-of select="s2/@key" />
        </xsl:attribute>    
        <xsl:text>(f,add,(loop,max,@</xsl:text>
        <xsl:value-of select="s1/@id_attribute_name" />
        <xsl:text>),1)</xsl:text>  
    </prop>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">8</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>     
  </loop>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">9</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>   
  <i>
    <xsl:text>Generated Key for new </xsl:text> 
    <xsl:value-of select="s1/@singular_label" />
    <xsl:text>: #</xsl:text>
    <xsl:value-of select="s2/@prop_key" />
  </i>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">10</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>     
  <xsl:choose>
    <xsl:when test="s2/@usage='app'">
      <call_job>
        <xsl:attribute name="job"><xsl:value-of select="s2/@job_city_tofile" /></xsl:attribute>   
      </call_job>  
    </xsl:when>
    <xsl:when test="s2/@usage='service'">
      <call_service>
        <xsl:attribute name="job"><xsl:value-of select="s2/@job_city_tofile" /></xsl:attribute>   
      </call_service>
    </xsl:when>  
  </xsl:choose>  
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">11</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template> 
      <ti_break class_table="tabNavigator"/>                            
      <ti type="rec">      
        <xsl:if test="s2/@see_keys=1">
        <xsl:attribute name="key">                   
          <xsl:text>#</xsl:text>
          <xsl:value-of select="s2/@prop_key" />                   
        </xsl:attribute>
        </xsl:if>                                
      <ti align="right">

        <xsl:attribute name="x">
          <xsl:text>+ </xsl:text>
          <xsl:value-of select="s1/@singular_label" />
          <xsl:text> &apos;</xsl:text>     
          <xsl:value-of select="fields/@name_expression_unbold" />
          <xsl:text>&apos;</xsl:text> 
        </xsl:attribute>
        

      </ti>
      </ti>
        
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">12</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>  
                                  
  <choose>
    <when >
      <xsl:attribute name="test">(f,has,<xsl:value-of select="s2/@post_add_script_prop" />)</xsl:attribute>
      <file id="formix-posting" x="(dir,temp)(f,random_text,5)"/>
      <prop id="formix-posting-code" x="(f,random_text,16)"/>
      <create_file file="(file,formix-posting)" code="(prop,formix-posting-code)">(<xsl:value-of select="s2/@post_add_script_prop" />)</create_file>
      <include file="(file,formix-posting)" code="(prop,formix-posting-code)" />
      <delete file="(file,formix-posting)" />
      <remove type="file" id="formix-posting"/>
      <remove type="prop" id="formix-posting-code"/>
      
     
    
    </when>
    <otherwise>
    
     <xsl:if test="string-length(s1/@job_after_insert)>0">
      <call_job>
      <xsl:attribute name="job">
        <xsl:value-of select="s1/@job_after_insert" />
      </xsl:attribute>    
      </call_job>       
    </xsl:if>        
    
    </otherwise>
  
  </choose> 
    

                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">14</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>   

 

    
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">add</xsl:with-param>
                                    <xsl:with-param name="hook">end</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>   
</job>
</xsl:template>

</xsl:stylesheet>

  