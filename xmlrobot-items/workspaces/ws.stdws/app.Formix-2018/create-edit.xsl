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
<job role="hidden">
  <xsl:attribute name="id"><xsl:value-of select="s2/@job_city_edit" /></xsl:attribute>  
  
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">begin</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>  
            
  <need><xsl:value-of select="s2/@prop_key" /></need>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">0</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template> 
  <need default="1">(prop,do_return_job)</need>   
  <xsl:choose>
    <xsl:when test="s2/@usage='app'">
      <!-- <call job="City to_memory"/>  -->
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">1</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>        
      <call_job>
        <xsl:attribute name="job"><xsl:value-of select="s2/@job_city_tomemory" /></xsl:attribute>   
      </call_job>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">2</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>      
      <prop id="isAdd">0</prop>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">3</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>        
      <call_job>
        <xsl:attribute name="job"><xsl:value-of select="s2/@job_city_editor" /></xsl:attribute>   
      </call_job>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">4</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>        
      
      <call_job>
        <xsl:attribute name="job"><xsl:value-of select="s2/@job_city_tofile" /></xsl:attribute>   
      </call_job>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">5</xsl:with-param>
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
          <xsl:text>+/- </xsl:text>
          <xsl:value-of select="s1/@singular_label" />
          <xsl:text> &apos;</xsl:text>     
          <xsl:value-of select="fields/@name_expression_unbold" />
          <xsl:text>&apos;</xsl:text> 
        </xsl:attribute>      


      </ti>
      </ti>
     
           
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">6</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template> 
      <!-- 
      <when test="(prop,do_return_job)">
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">11</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>              
      <call_job>
        <xsl:attribute name="job"><xsl:value-of select="s2/@prop_return_job" /></xsl:attribute>   
      </call_job>  
      </when>
      -->
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">7</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template> 
      <!--                             
      <ti label=" ">
        <xsl:attribute name="x">
          <xsl:value-of select="fields/@name_expression" />
        </xsl:attribute>      
      </ti>    
      -->     
                                        <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">8</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>                                          
    </xsl:when>
    
    <xsl:when test="s2/@usage='service'">
      <!-- <call job="City to_memory"/>  -->
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">1</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template> 
                                               
      <call_service>
        <xsl:attribute name="job"><xsl:value-of select="s2/@job_city_tomemory" /></xsl:attribute>   
      </call_service>
     
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">2</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>             
    
      <prop id="isAdd">0</prop>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">3</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>             
      <call_service>
        <xsl:attribute name="job"><xsl:value-of select="s2/@job_city_editor" /></xsl:attribute>   
      </call_service>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">4</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>             
      <call_service>
        <xsl:attribute name="job"><xsl:value-of select="s2/@job_city_tofile" /></xsl:attribute>   
      </call_service>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">5</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>             

      
      <i>
        <xsl:attribute name="label"> 
          <xsl:text>Updated:</xsl:text>
        </xsl:attribute>
        <xsl:attribute name="x">
          <!-- <xsl:value-of select="fields/@name_expression" /> -->
          <xsl:value-of select="s1/@singular_label" />
          <xsl:text> #</xsl:text>
          <xsl:value-of select="s2/@prop_key" />
        </xsl:attribute>
      </i>
      <ti label=" ">
        <xsl:attribute name="x">
          <xsl:value-of select="fields/@name_expression" />
        </xsl:attribute>      
      </ti>       
                                        <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">6</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>       
      <!-- 
      <when test="(prop,do_return_job)"> 
                                        <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">11</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>      
      <call_service>
        <xsl:attribute name="job"><xsl:value-of select="s2/@prop_return_job" /></xsl:attribute>   
      </call_service> 
      </when> 
      -->
                                        <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">7</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>   
                                  
        
                                        <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">8</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>                                            
    </xsl:when>  
  </xsl:choose>     
  
                                        <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">end</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>   
  
   <xsl:if test="string-length(s1/@job_after_edit)>0">
    <call_job>
    <xsl:attribute name="job">
      <xsl:value-of select="s1/@job_after_edit" />
    </xsl:attribute>    
    </call_job>       
  </xsl:if>    
</job>
</xsl:template>

<!-- ******************************************* -->
<xsl:template match="field" mode="detail">
  <!--  <ti label="State:">(prop,city_state)</ti>
        <ti label="Population:">(f,format,(prop,city_population),#(entity:co)###)</ti>  -->
                                        <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">9</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>           
  <ti>
    <xsl:attribute name="label">
      <xsl:value-of select="@label" />
      <xsl:text>:</xsl:text>    
    </xsl:attribute>
    <xsl:choose>
      <xsl:when test="string-length(@number_format)>0">
        <xsl:text>(f,format,(prop,</xsl:text>
        <xsl:value-of select="//s1/@singular_id" />
        <xsl:text>_</xsl:text>
        <xsl:value-of select="@id" />
        <xsl:text>),</xsl:text>
        <xsl:value-of select="@number_format" /> 
        <xsl:text>)</xsl:text>
      </xsl:when>  
      <xsl:when test="@type='bool'"> 
        <xsl:text>(f,when,</xsl:text>
        <xsl:text>(prop,</xsl:text>
        <xsl:value-of select="//s1/@singular_id" />
        <xsl:text>_</xsl:text>
        <xsl:value-of select="@id" />
        <xsl:text>)</xsl:text>  
        <xsl:text>,Yes,No)</xsl:text>      
      </xsl:when>           
      <xsl:otherwise>  
        <xsl:text>(prop,</xsl:text>
        <xsl:value-of select="//s1/@singular_id" />
        <xsl:text>_</xsl:text>
        <xsl:value-of select="@id" />
        <xsl:text>)</xsl:text>                     
      </xsl:otherwise>
    </xsl:choose>      
  </ti>
                                        <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">edit</xsl:with-param>
                                    <xsl:with-param name="hook">10</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>     
 
</xsl:template>


</xsl:stylesheet>

  