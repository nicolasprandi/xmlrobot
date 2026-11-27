<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015 Nicolas Prandstaetter. All rights reserved. 






 -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>
<xsl:include href="include-introduction.xsl" />
<xsl:include href="include-hooks2.xsl" />
<xsl:include href="include-buttons.xsl" />

<xsl:template match="/">
   <xsl:apply-templates/>
</xsl:template>

<xsl:template match="conf">

<!-- <job id="City Details" role="hidden">  -->
<job role="hidden">
  <xsl:attribute name="id"><xsl:value-of select="s2/@job_city_details" /><xsl:text>-more</xsl:text></xsl:attribute>  
  
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">begin</xsl:with-param>
          <xsl:with-param name="station">-1</xsl:with-param>
        </xsl:call-template>    
  
  <prop x="print_only print_with_tips print_all_fields">
    <xsl:attribute name="id">
      <xsl:value-of select="s2/@tk_control" />
    </xsl:attribute>
  </prop>
  
  <!-- hier der up button -->
  <xsl:call-template name="an-up-details-button">
    <xsl:with-param name="position">top</xsl:with-param>
    <xsl:with-param name="from">help</xsl:with-param>
  </xsl:call-template>
    
  
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">begin</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>
    
  <need><xsl:value-of select="s2/@prop_key" /></need>
  <!-- <call job="City to_memory"/>  -->
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">0</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>  
  <xsl:choose>
    <xsl:when test="s2/@usage='app'">
      <call_job>
        <xsl:attribute name="job"><xsl:value-of select="s2/@job_city_tomemory" /></xsl:attribute>   
      </call_job>
    </xsl:when>
    <xsl:when test="s2/@usage='service'">
      <call_service>
        <xsl:attribute name="job"><xsl:value-of select="s2/@job_city_tomemory" /></xsl:attribute>   
      </call_service>
    </xsl:when>  
  </xsl:choose>  

        <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">1</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>
    
  <!-- <ti label="(prop,city_name)(prop,cities_offset_label) ((prop,rec_id))">  -->
  



  
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">details2</xsl:with-param>
          <xsl:with-param name="hook">5</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>    
  
  

  <!-- 
  <xsl:if test="s1/@details_header='1'">
  <prop>
    <xsl:attribute name="id">
      <xsl:value-of select="s2/@label" />
    </xsl:attribute>
    <xsl:attribute name="x">
      <xsl:value-of select="fields/@name_expression" />
      <xsl:text>(f,when,(f,has,</xsl:text>
      <xsl:value-of select="s2/@prop_cities_offset_label" />
      <xsl:text>), - </xsl:text>
      <xsl:value-of select="s2/@prop_cities_offset_label" />
      <xsl:text>,)</xsl:text>
    </xsl:attribute>    
  </prop>  
  </xsl:if>
  -->
  
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
        <ti_hr/>
        
        <xsl:call-template name="an-edit-button-just"/>
    
   
 

         <xsl:call-template name="hook2">
          <xsl:with-param name="page">details2</xsl:with-param>
          <xsl:with-param name="hook">end</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template> 
        
  <xsl:call-template name="an-up-details-button">
    <xsl:with-param name="position">bottom</xsl:with-param>
    <xsl:with-param name="from">help</xsl:with-param>
  </xsl:call-template>        

  
</job>

</xsl:template>

<!-- ******************************************* -->
<xsl:template match="field" mode="detail">
  <!--  <ti label="State:">(prop,city_state)</ti>
        <ti label="Population:">(f,format,(prop,city_population),#(entity:co)###)</ti>  -->
      
  <ti>
    <xsl:choose>
      <xsl:when test="@type = 'msg'">
        <xsl:attribute name="label">   
           <xsl:text>(f,html,b,o)</xsl:text>
          <xsl:value-of select="@default_value" />
          <xsl:text>(f,html,b,c)</xsl:text> 
        </xsl:attribute>        
      </xsl:when>
      <xsl:otherwise>
        <xsl:attribute name="label">   
          <xsl:value-of select="@label" /> 
          <xsl:text>:</xsl:text>   
        </xsl:attribute>      
      </xsl:otherwise>
    </xsl:choose>

    <xsl:attribute name="flag">   
      <xsl:text>label_right</xsl:text>   
    </xsl:attribute>    
    
    <xsl:attribute name="type">  
      <xsl:choose>    
        <xsl:when test="@type='dir'"> 
          <xsl:text>dir</xsl:text>     
        </xsl:when>  
        <xsl:when test="@type='file'"> 
          <xsl:text>file</xsl:text>     
        </xsl:when>       
             
        <xsl:otherwise>
          <xsl:text>text</xsl:text>
        </xsl:otherwise>
      </xsl:choose> 
    </xsl:attribute>
    
    <xsl:choose>    
      <xsl:when test="string-length(@ti_test)>0">
        <xsl:attribute name="test"> 
          <xsl:value-of select="@ti_test" />     
        </xsl:attribute>   
      </xsl:when>  
      <xsl:otherwise>
        <xsl:if test="not( @type = 'msg' )">
        <xsl:attribute name="test"> 
          <xsl:text>(f,has,</xsl:text>
          <xsl:choose>
            <xsl:when test="@type='dir'">
              <xsl:text>dir</xsl:text>
            </xsl:when>
            <xsl:when test="@type='file'">
              <xsl:text>file</xsl:text>
            </xsl:when>
            <xsl:otherwise>
              <xsl:text>prop</xsl:text>
            </xsl:otherwise>
          </xsl:choose> 
          <xsl:text>,</xsl:text>                       
          <xsl:value-of select="//s1/@singular_id" />
          <xsl:text>_</xsl:text>
          <xsl:value-of select="@id" />
          <xsl:text>)</xsl:text>   
        </xsl:attribute> 
        </xsl:if>      
      </xsl:otherwise>
    </xsl:choose>         
    <!-- 
    <xsl:if test="@name_flag='1'">
      <xsl:text>(f,html,bold,open)</xsl:text>
    </xsl:if>  
    -->
    
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
          <xsl:text>(f,when,(f,has,prop,</xsl:text>
          <xsl:value-of select="//s1/@singular_id" />
          <xsl:text>_</xsl:text>
          <xsl:value-of select="@id" />
          <xsl:text>),(f,vob,prop,</xsl:text> 
          <xsl:value-of select="//s1/@singular_id" />
          <xsl:text>_</xsl:text>
          <xsl:value-of select="@id" /> 
          <xsl:text>),0)</xsl:text>                  
      <!-- <xsl:text>(prop,</xsl:text>
        <xsl:value-of select="//s1/@singular_id" />
        <xsl:text>_</xsl:text>
        <xsl:value-of select="@id" />
        <xsl:text>)</xsl:text>      -->
        
        
        <xsl:text>,Yes,No)</xsl:text>      
      </xsl:when>  
      <xsl:when test="@type='msg'"> 
        <!-- 
        <xsl:text>(f,html,b,o)</xsl:text>
        <xsl:value-of select="@default_value" />
        <xsl:text>(f,html,b,c)</xsl:text>
        -->
      </xsl:when>               
      <xsl:otherwise>  
        <xsl:text>(f,vob,</xsl:text>
        <xsl:value-of select="@vartype" />
        <xsl:text>,</xsl:text>
        <xsl:value-of select="//s1/@singular_id" />
        <xsl:text>_</xsl:text>
        <xsl:value-of select="@id" />
        <xsl:text>)</xsl:text>                     
      </xsl:otherwise>
    </xsl:choose> 
    <!--                  nic123
    <xsl:if test="@name_flag='1'">
      <xsl:text>(f,html,bold,open)</xsl:text>
    </xsl:if>     
    -->     
   
  </ti> 
  <prop id="formix-field-locator-hook">
          <xsl:value-of select="//s1/@singular_id" />
          <xsl:text>_</xsl:text>
          <xsl:value-of select="@id" />
    
  </prop>
         <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">fieldend</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>     

</xsl:template>


</xsl:stylesheet>

  