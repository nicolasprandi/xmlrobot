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
  <xsl:attribute name="id"><xsl:value-of select="s2/@job_city_editor" /></xsl:attribute>
  
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">editor</xsl:with-param>
                                    <xsl:with-param name="hook">begin</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>          
  <need>(prop,isAdd)</need>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">editor</xsl:with-param>
                                    <xsl:with-param name="hook">0</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>    
  <xsl:if test="string-length(hooks/@hook_job_editor_begin) > 0">  
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">editor</xsl:with-param>
                                    <xsl:with-param name="hook">1</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>      
  <xsl:choose>
    <xsl:when test="s2/@usage='app'">
      <call_job>
        <xsl:attribute name="job">
          <xsl:value-of select="hooks/@hook_job_editor_begin" />
        </xsl:attribute>
      </call_job>
    </xsl:when>
    <xsl:when test="s2/@usage='service'">
      <call_service>
        <xsl:attribute name="job">
          <xsl:value-of select="hooks/@hook_job_editor_begin" />
        </xsl:attribute>
      </call_service>
    </xsl:when>  
  </xsl:choose> 
  </xsl:if>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">editor</xsl:with-param>
                                    <xsl:with-param name="hook">2</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>      
  <choose>
    <when test="(prop,isAdd)">
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">editor</xsl:with-param>
                                    <xsl:with-param name="hook">3</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>        
                                  
      <prop>
        <xsl:attribute name="id">
          <xsl:value-of select="s2/@label_class" />
        </xsl:attribute>
        <xsl:attribute name="x">
          <xsl:text>+ </xsl:text>
          <xsl:value-of select="s1/@singular_label" />
        </xsl:attribute>    
      </prop>  
      <prop>
        <xsl:attribute name="id">
          <xsl:value-of select="s2/@label_name" />
        </xsl:attribute>
        <xsl:attribute name="x">
          <xsl:text>(f,when,(f,has,</xsl:text>
          <xsl:value-of select="s2/@prop_cities_offset_label" />
          <xsl:text>), | </xsl:text>
          <xsl:value-of select="s2/@prop_cities_offset_label" />
          <xsl:text>,)</xsl:text>
        </xsl:attribute>    
      </prop>   
      
      <prop>
        <xsl:attribute name="id">
          <xsl:value-of select="s2/@tk_control" />
        </xsl:attribute>
        <xsl:attribute name="x">
          <xsl:value-of select="s1/@tk_flag_add" />
        </xsl:attribute>        
      </prop> 
            
      <!--                                
      <prop>
        <xsl:attribute name="id"> 
          <xsl:value-of select="s2/@label" />
        </xsl:attribute>
        <xsl:attribute name="x"> 
          <xsl:text>+ </xsl:text>
          <xsl:value-of select="s1/@singular_label" />
          <xsl:value-of select="s2/@prop_cities_offset_label" />
        </xsl:attribute>      
      </prop>      
      
      <prop>
        <xsl:attribute name="x">
          <xsl:value-of select="s1/@edit_tk_flag" />
        </xsl:attribute>      
        <xsl:attribute name="id">
          <xsl:value-of select="s2/@tk_control" />
        </xsl:attribute>
      </prop>  -->    
    </when> <!-- end: if isadd -->
    <otherwise> <!-- no add, updating -->
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">editor</xsl:with-param>
                                    <xsl:with-param name="hook">7</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>        
      <need><xsl:value-of select="s2/@prop_key" /></need>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">editor</xsl:with-param>
                                    <xsl:with-param name="hook">8</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>          
      <prop>
        <xsl:attribute name="id">
          <xsl:value-of select="s2/@label_class" />
        </xsl:attribute>
        <xsl:attribute name="x">
          <xsl:value-of select="s1/@singular_label" />
        </xsl:attribute>    
      </prop>  
      <prop>
        <xsl:attribute name="id">
          <xsl:value-of select="s2/@label_name" />
        </xsl:attribute>
        <xsl:attribute name="x">
          <xsl:value-of select="fields/@name_expression_unbold" />
          <xsl:text>(f,when,(f,has,</xsl:text>
          <xsl:value-of select="s2/@prop_cities_offset_label" />
          <xsl:text>), - </xsl:text>
          <xsl:value-of select="s2/@prop_cities_offset_label" />
          <xsl:text>,)</xsl:text>
        </xsl:attribute>    
      </prop>   
      <prop>
        <xsl:attribute name="id">
          <xsl:value-of select="s2/@tk_control" />
        </xsl:attribute>
        <xsl:attribute name="x">
          <xsl:value-of select="s1/@tk_flag_update" />
        </xsl:attribute>        
      </prop>                            
     
                                        <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">editor</xsl:with-param>
                                    <xsl:with-param name="hook">9</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>    
    
    </otherwise> <!-- end noadd, update -->
  </choose>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">editor</xsl:with-param>
                                    <xsl:with-param name="hook">10</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>   
                                  
   <xsl:choose>
    <xsl:when test="s2/@usage='app'">
      <call_job>
        <xsl:attribute name="job"><xsl:value-of select="s2/@job_city_tk" /></xsl:attribute>   
      </call_job>
    </xsl:when>
    <xsl:when test="s2/@usage='service'">
      <call_service>
        <xsl:attribute name="job"><xsl:value-of select="s2/@job_city_tk" /></xsl:attribute>   
      </call_service>
    </xsl:when>  
  </xsl:choose>                                    

    <!-- <tk label="xxxx (prop,label)">
      <xsl:attribute name="flag">
        <xsl:value-of select="s1/@edit_tk_flag" /> 
      </xsl:attribute>
      <xsl:apply-templates select="fields/field" mode="editor">
        <xsl:sort select="@position"/>
      </xsl:apply-templates>
    </tk> -->
  
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">editor</xsl:with-param>
                                    <xsl:with-param name="hook">11</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>    

  <xsl:if test="string-length(hooks/@hook_job_editor_end) > 0">  
                                    <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">editor</xsl:with-param>
                                    <xsl:with-param name="hook">12</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>    
  <xsl:choose>
    <xsl:when test="s2/@usage='app'">
      <call_job>
        <xsl:attribute name="job">
          <xsl:value-of select="hooks/@hook_job_editor_end" />
        </xsl:attribute>
      </call_job>
    </xsl:when>
    <xsl:when test="s2/@usage='service'">
      <call_service>
        <xsl:attribute name="job">
          <xsl:value-of select="hooks/@hook_job_editor_end" />
        </xsl:attribute>
      </call_service>
    </xsl:when>  
  </xsl:choose> 
  </xsl:if>

                                    <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">editor</xsl:with-param>
                                    <xsl:with-param name="hook">end</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>    

  
</job>
</xsl:template>

<!-- ******************************************* -->
<xsl:template match="field" mode="editor">
  <!--      

  <tk>
    <xsl:attribute name="label">
      <xsl:value-of select="@label" />

    </xsl:attribute>
    <xsl:attribute name="id"> 
      <xsl:value-of select="//s1/@singular_id" />
      <xsl:text>_</xsl:text>
      <xsl:value-of select="@id" />
    </xsl:attribute>
    <xsl:if test="string-length(@default_value)>0">
      <xsl:attribute name="x"> 
        <xsl:value-of select="@default_value" />
      </xsl:attribute>   
    </xsl:if>
    

  
    <xsl:if test="string-length(@tip)>0">
      
      <xsl:attribute name="tip">
        <xsl:value-of select="@tip" />    
      </xsl:attribute>
         
    </xsl:if>  
    <xsl:if test="string-length(@tk_test)>0">
      <xsl:attribute name="test"> 
        <xsl:value-of select="@tk_test" />     
      </xsl:attribute>   
    </xsl:if>  
    <xsl:if test="string-length(@tk_test_dyn)>0">
      <xsl:attribute name="test_dyn"> 
        <xsl:value-of select="@tk_test_dyn" />     
      </xsl:attribute>   
    </xsl:if>         
    <xsl:if test="string-length(@tk_test_first)>0">
      <xsl:attribute name="test_first"> 
        <xsl:value-of select="@tk_test_first" />     
      </xsl:attribute>   
    </xsl:if>            
    <xsl:if test="string-length(@regex)>0">
      
      <xsl:attribute name="regex"> 
        <xsl:value-of select="@regex" />      
      </xsl:attribute>
        
    </xsl:if>   
    <xsl:if test="string-length(@regex_msg)>0">
      
      <xsl:attribute name="regex_msg"> 
        <xsl:value-of select="@regex_msg" />     
      </xsl:attribute>
         
    </xsl:if>        
    

        
    <xsl:attribute name="type"> 
      <xsl:value-of select="@type" />
    </xsl:attribute> 
    
    <xsl:choose>
      <xsl:when test="@type='sel'">
        <xsl:attribute name="flag">
          <xsl:if test="@sel_multi_flag='1'">             
            <xsl:text>multi</xsl:text>               
          </xsl:if>
          <xsl:call-template name="nono_flag"/>
        </xsl:attribute>    
        <xsl:choose>
          <xsl:when test="string-length(@sel_ref) > 0">
            <xsl:call-template name="sel_header">
              <xsl:with-param name="sel_ref" select="@sel_ref"/>
            </xsl:call-template>          
          </xsl:when>
          <xsl:when test="string-length(@sel_file) > 0">
            <xsl:attribute name="xml"> 
              <xsl:value-of select="@sel_file" />
            </xsl:attribute>  
          </xsl:when>        
        </xsl:choose>      
      </xsl:when>
      <xsl:otherwise>
        <xsl:attribute name="flag">
          <xsl:call-template name="nono_flag"/>
        </xsl:attribute>
      </xsl:otherwise>
    </xsl:choose>

  </tk>
 --> 
</xsl:template>

<xsl:template name="nono_flag">
  <xsl:choose>
    <xsl:when test="@nono='1'">
      <xsl:text> nono </xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <xsl:text> </xsl:text>
    </xsl:otherwise>
  </xsl:choose>  
  <xsl:if test="string-length(@tk_flag) > 0">
    <xsl:text> </xsl:text> 
    <xsl:value-of select="@tk_flag"/> 
    <xsl:text> </xsl:text>          
  </xsl:if>
</xsl:template>	

<xsl:template name="sel_header">
	<xsl:param name="sel_ref"/>
  <xsl:apply-templates select="//sel[@ref=$sel_ref]"/>
</xsl:template>	

<xsl:template match="sel">

  <sel>
      <xsl:attribute name="label">  
        <xsl:value-of select="@label" /> 
      </xsl:attribute>   
      <xsl:value-of select="@value" />
  </sel>
  
</xsl:template>

</xsl:stylesheet>

  