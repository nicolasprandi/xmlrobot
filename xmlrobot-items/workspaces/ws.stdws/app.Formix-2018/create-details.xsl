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
  <xsl:attribute name="id"><xsl:value-of select="s2/@job_city_details" /></xsl:attribute>    
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">begin</xsl:with-param>
          <xsl:with-param name="station">-1</xsl:with-param>
        </xsl:call-template>
        
  <need><xsl:value-of select="s2/@prop_key" /></need>
  
  <need default=""><xsl:value-of select="s2/@prop_cities_sort_attribute" /></need>              
  <need default=""><xsl:value-of select="s2/@prop_cities_sort_number" /></need>  
  <need default=""><xsl:value-of select="s2/@prop_cities_sort_ignore_case" /></need>
  <need default=""><xsl:value-of select="s2/@prop_cities_sort_desc" /></need>
  <need default=""><xsl:value-of select="s2/@prop_cities_where" /></need>
  <need default=""><xsl:value-of select="s2/@prop_cities_offset_label" /></need>                     
  <need default=""><xsl:value-of select="s2/@prop_cities_offset" /></need>  
  <need default=""><xsl:value-of select="s2/@prop_return_job" /></need>  
  <need default="//">(prop,<xsl:value-of select="s2/@cities_offset_new_key" />)</need>        
  <!-- hier die parent defaults rein -->
  <xsl:apply-templates select="//parents/parent" mode="parent_defaults"/>
  
     <!-- translator job -->
  <xsl:if test="string-length(s1/@tr_job) > 0">
    <xsl:choose>
      <xsl:when test="s2/@usage='app'">
        <call_job>
          <xsl:attribute name="job"><xsl:value-of select="s1/@tr_job" /></xsl:attribute>   
        </call_job>
      </xsl:when>
      <xsl:when test="s2/@usage='service'">
        <call_service>
          <xsl:attribute name="job"><xsl:value-of select="s1/@tr_job" /></xsl:attribute>   
        </call_service>
      </xsl:when>  
    </xsl:choose>
  </xsl:if>    
        
  <prop x="print print_only">
    <xsl:attribute name="id">
      <xsl:value-of select="s2/@tk_control" />
    </xsl:attribute>
  </prop>
  
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">begin</xsl:with-param>
          <xsl:with-param name="station">-2</xsl:with-param>
        </xsl:call-template>  
  
  <!-- hier der up button -->
  <xsl:call-template name="an-up-details-button">
    <xsl:with-param name="position">top</xsl:with-param>
    <xsl:with-param name="from">info</xsl:with-param>
  </xsl:call-template>
          
    
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">begin</xsl:with-param>
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
  

    <xsl:if test="s1/@details_see_key='1'">
      <ti>

        <xsl:attribute name="key">
          <xsl:text>#</xsl:text>
          <xsl:value-of select="s2/@prop_key" />
        </xsl:attribute>
       </ti> 
    </xsl:if>   

  
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">5</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>    
  
  

  <!-- eigenes -->                 
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
    
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">14</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>           
  <!--
  <xsl:if test="s1/@details_header='1'">
  <prop>
    <xsl:attribute name="id">
      <xsl:value-of select="s2/@label" />
    </xsl:attribute>
    <xsl:attribute name="x">
      <xsl:value-of select="s1/@singular_label" />
      <xsl:text>: </xsl:text>
      <xsl:value-of select="fields/@name_expression_unbold" />
      <xsl:text>(f,when,(f,has,</xsl:text>
      <xsl:value-of select="s2/@prop_cities_offset_label" />
      <xsl:text>), - </xsl:text>
      <xsl:value-of select="s2/@prop_cities_offset_label" />
      <xsl:text>,)</xsl:text>
    </xsl:attribute>    
  </prop>  
  
  <xsl:choose>
    <xsl:when test="string-length(s1/@details_header_class)=0 or s1/@details_header_class='1'">
      <prop>
        <xsl:attribute name="id">
          <xsl:value-of select="s2/@label_class" />
        </xsl:attribute>
        <xsl:attribute name="x">
          <xsl:value-of select="s1/@singular_label" />
        </xsl:attribute>    
      </prop>  
    </xsl:when>
    <xsl:otherwise>
      <prop x="">
        <xsl:attribute name="id">
          <xsl:value-of select="s2/@label_class" />
        </xsl:attribute>  
      </prop>      
    </xsl:otherwise>
   </xsl:choose>  
  <xsl:choose>  
    <xsl:when test="string-length(s1/@details_header_name)=0 or s1/@details_header_name='1'">
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
    </xsl:when>   
    <xsl:otherwise>
      <prop x="">
        <xsl:attribute name="id">
          <xsl:value-of select="s2/@label_name" />
        </xsl:attribute>  
      </prop>      
    </xsl:otherwise>
   </xsl:choose>  
  </xsl:if>
  -->
  

 
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">15</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>  
            
  <!-- eigenes ende -->
  
  <xsl:if test="s1/@details_edit=1 and ( s1/@details_edit_loc='top' or s1/@details_edit_loc='both' or string-length(s1/@details_edit_loc)=0)">
    <xsl:call-template name="an-edit-button-details">
    </xsl:call-template>              
  </xsl:if>

  <xsl:if test="s1/@details_delete=1 and ( s1/@details_delete_loc='top' or s1/@details_delete_loc='both')">
    <xsl:call-template name="an-delete-button-details">
    </xsl:call-template>              
  </xsl:if>
  
  <xsl:if test="s1/@details_actions_loc='top' or s1/@details_actions_loc='both'">
    <xsl:apply-templates select="//conf/actions/action[@details='1']" mode="ti_call_detail"> 
      <xsl:with-param name="page"><xsl:text>details</xsl:text></xsl:with-param>          
      <xsl:sort select="@sort"/>
    </xsl:apply-templates>
  </xsl:if>     
  
  <xsl:call-template name="hook2">
    <xsl:with-param name="page">details</xsl:with-param>
    <xsl:with-param name="hook">99</xsl:with-param>
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


  <xsl:call-template name="hook2">
    <xsl:with-param name="page">details</xsl:with-param>
    <xsl:with-param name="hook">98</xsl:with-param>
    <xsl:with-param name="station">0</xsl:with-param>
  </xsl:call-template>      
    
  <xsl:call-template name="hook2">
    <xsl:with-param name="page">details</xsl:with-param>
    <xsl:with-param name="hook">8</xsl:with-param>
    <xsl:with-param name="station">0</xsl:with-param>
  </xsl:call-template>  
  
  <xsl:call-template name="hook2">
    <xsl:with-param name="page">details</xsl:with-param>
    <xsl:with-param name="hook">97</xsl:with-param>
    <xsl:with-param name="station">0</xsl:with-param>
  </xsl:call-template>    
                        
  <xsl:if test="s1/@details_delete=1 or s1/@details_edit=1 or count(//conf/actions/action[@details='1'])>0">
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">9</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>
    <!--     
    <ti_hr/>
    <ti padding_top="6px">
    -->
    <ti_space height="3px" />

      <xsl:if test="s1/@details_edit=1 and ( s1/@details_edit_loc='bottom' or s1/@details_edit_loc='both' )">
        <xsl:call-template name="an-edit-button-details">
        </xsl:call-template>      
        
      </xsl:if>   
      
       
      
      <xsl:if test="s1/@details_delete=1 and ( s1/@details_delete_loc='bottom' or s1/@details_delete_loc='both' )">
        <xsl:call-template name="an-delete-button-details">
        </xsl:call-template>      
        
      </xsl:if>        

  
      <xsl:if test="s1/@details_actions_loc='bottom' or s1/@details_actions_loc='both' or string-length(s1/@details_actions_loc)=0">
        <xsl:apply-templates select="//conf/actions/action[@details='1']" mode="ti_call_detail"> 
          <xsl:with-param name="page"><xsl:text>details</xsl:text></xsl:with-param>          
          <xsl:sort select="@sort"/>
        </xsl:apply-templates>
      </xsl:if>       
                
    <!-- </ti> -->    
         <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">12</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template> 
  </xsl:if>
          <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">13</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>

  <xsl:if test="s1/@details_see_all='1'">
    <ti explain="See all">
      <xsl:attribute name="type">
        <xsl:value-of select="s2/@ti_call" />
      </xsl:attribute>     
      <xsl:attribute name="job">
        <xsl:value-of select="s2/@job_city_view" />  
      </xsl:attribute>     
    </ti>

  </xsl:if>           
 

         <xsl:call-template name="hook2">
          <xsl:with-param name="page">details</xsl:with-param>
          <xsl:with-param name="hook">end</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template> 


  <!-- hier der up button -->
  <xsl:call-template name="an-up-details-button">
    <xsl:with-param name="position">bottom</xsl:with-param>
    <xsl:with-param name="from">info</xsl:with-param>
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

  