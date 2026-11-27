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

<!-- <job id="City Delete" role="hidden">  -->
<job role="hidden">
  <xsl:attribute name="id"><xsl:value-of select="s2/@job_city_delete" /></xsl:attribute>
  
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">delete</xsl:with-param>
          <xsl:with-param name="hook">begin</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>
              
  <need><xsl:value-of select="s2/@prop_key" /></need>
  <need default="1">(prop,do_return_job)</need> 
  <!-- <call job="City to_memory"/>  -->
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">delete</xsl:with-param>
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
          <xsl:with-param name="page">delete</xsl:with-param>
          <xsl:with-param name="hook">1</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>

  <!-- <tk id="sure_delete" label="Sure about Deleting (prop,city_name)(prop,cities_offset_label)?" type="bool" x="1"/> -->
		<tk 
			type="accept" 
			id="delete-it-1" 
			label_accept="Delete" 
			label_decline="Forget it" 
			width="300" height="150">
      <xsl:attribute name="label">
        <xsl:text>Sure about deleting </xsl:text>
        <xsl:value-of select="s1/@singular_label" />
        <xsl:text> [ </xsl:text>
        <xsl:value-of select="fields/@name_expression_unbold" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="s2/@prop_cities_offset_label" />
        <xsl:if test="s2/@see_keys=1">
          <xsl:text> #</xsl:text>
          <xsl:value-of select="s2/@prop_key" />
        </xsl:if>
        <xsl:text>] ?</xsl:text>
      </xsl:attribute> 				
      <xsl:attribute name="x">
        <xsl:text>Do you really want to delete that?</xsl:text>
        <xsl:text>&#xA;()If you are not sure, make a backup of the ()</xsl:text>
        <xsl:choose>
          <xsl:when test="s2/@usage='app'">
            <xsl:text>project.</xsl:text> 
          </xsl:when>
          <xsl:when test="s2/@usage='service'">
            <xsl:text>services.</xsl:text> 
          </xsl:when>  
        </xsl:choose> 
      </xsl:attribute> 			
				<q id="delete-it-2" label="()Yes, I know that the record will be gone forever()"/>
	</tk>

         <xsl:call-template name="hook2">
          <xsl:with-param name="page">delete</xsl:with-param>
          <xsl:with-param name="hook">2</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template> 
  <choose>
    <when test="(f,and,(prop,delete-it-1),(prop,delete-it-2))">
    
            <xsl:call-template name="hook2">
          <xsl:with-param name="page">delete</xsl:with-param>
          <xsl:with-param name="hook">3</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>
      <xml_update 
          how="remove" >
        <xsl:attribute name="what">   
          <xsl:value-of select="s2/@file_cities" />
        </xsl:attribute>
        <xsl:attribute name="where"> 
          <xsl:value-of select="s2/@prop_cities_offset" />
          <xsl:value-of select="s1/@singular_id" />
          <xsl:text>[@</xsl:text>
          <xsl:value-of select="s1/@id_attribute_name" />
          <xsl:text>='</xsl:text>
          <xsl:value-of select="s2/@prop_key" />
          <xsl:text>']</xsl:text>
        </xsl:attribute>                  
      </xml_update>
      
              <xsl:call-template name="hook2">
          <xsl:with-param name="page">delete</xsl:with-param>
          <xsl:with-param name="hook">4</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>
      
      <tp>
          <xsl:text>Deleted </xsl:text>
          <xsl:value-of select="fields/@name_expression_unbold" />
          <xsl:text> #</xsl:text>
          <xsl:value-of select="s2/@prop_key" />
         
      </tp>
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">delete</xsl:with-param>
          <xsl:with-param name="hook">5</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>      
      
      <!-- 
      <ti>
        <xsl:attribute name="label"> 
          <xsl:text>Did (f,html,bold,open)DELETE:(f,html,bold,close) </xsl:text>
        </xsl:attribute>
        <xsl:attribute name="x">     
          <xsl:text>(f,html,bold,open)</xsl:text>
          <xsl:value-of select="fields/@name_expression" />
          <xsl:text>(f,html,bold,close), </xsl:text>
          <xsl:value-of select="s1/@singular_label" />
          <xsl:text> #</xsl:text>
          <xsl:value-of select="s2/@prop_key" />                   
        </xsl:attribute>  
      </ti>      
      -->
      
      <ti>
        <xsl:attribute name="label"> 
          <xsl:text>- </xsl:text>
          <xsl:value-of select="s1/@singular_label" />
          <xsl:text>:</xsl:text>
        </xsl:attribute>
        <xsl:attribute name="x">     
          <xsl:text>(f,html,bold,open)</xsl:text>
          <xsl:value-of select="fields/@name_expression" />
          <xsl:text>(f,html,bold,close)</xsl:text>  
        </xsl:attribute>
        <xsl:if test="s2/@see_keys=1">
          <xsl:attribute name="key">                   
            <xsl:text>#</xsl:text>
            <xsl:value-of select="s2/@prop_key" />                   
          </xsl:attribute>  
        </xsl:if>
      </ti>
      <ti_hr/>          
      
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">delete</xsl:with-param>
          <xsl:with-param name="hook">6</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>      
    
      <when test="(prop,do_return_job)">
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">delete</xsl:with-param>
          <xsl:with-param name="hook">11</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>    
      <!--             
      <xsl:choose>
        <xsl:when test="s2/@usage='app'">
          <call_job>
            <xsl:attribute name="job"> 
              <xsl:value-of select="s2/@prop_return_job" />
            </xsl:attribute>    
           </call_job>  
        </xsl:when>
        <xsl:when test="s2/@usage='service'">
          <call_service>
            <xsl:attribute name="job"> 
              <xsl:value-of select="s2/@prop_return_job" />
            </xsl:attribute>    
           </call_service>  
        </xsl:when>  
      </xsl:choose> 
      -->
      </when>
      
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">delete</xsl:with-param>
          <xsl:with-param name="hook">8</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>         
    </when>
    <otherwise>
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">delete</xsl:with-param>
          <xsl:with-param name="hook">9</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>    
      <tp>No delete performed.</tp>
      <ti>No delete performed.</ti>
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">delete</xsl:with-param>
          <xsl:with-param name="hook">10</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>      
    </otherwise>
  </choose>  
          <xsl:call-template name="hook2">
          <xsl:with-param name="page">delete</xsl:with-param>
          <xsl:with-param name="hook">end</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>
  
  <!--
  -->
  
  <!--
  <xsl:attribute name="x"> 
    <xsl:text></xsl:text>
    <xsl:value-of select="s2/@" />
    <xsl:text></xsl:text>
    <xsl:value-of select="s2/@" />
    <xsl:text></xsl:text>
    <xsl:value-of select="s2/@" />
    <xsl:text></xsl:text>
    <xsl:value-of select="s2/@" />
  </xsl:attribute>
  -->
    
  <!-- <prop id="city_dt_upd">(f,xpathx,,(file,cities),(prop,cities_offset)city[@row='(prop,rec_id)'],@dt_upd)</prop>  
       <prop id="city_dt_add">(f,xpathx,,(file,cities),(prop,cities_offset)city[@row='(prop,rec_id)'],@dt_add)</prop> -->

  <xsl:if test="string-length(s1/@job_after_delete)>0">
    <call_job>
    <xsl:attribute name="job">
      <xsl:value-of select="s1/@job_after_delete" />
    </xsl:attribute>    
    </call_job>       
  </xsl:if>   
  

  
</job>
</xsl:template>

<!-- ******************************************* -->
<xsl:template match="field" mode="detail">
  <!--  <ti label="State:">(prop,city_state)</ti>
        <ti label="Population:">(f,format,(prop,city_population),#(entity:co)###)</ti>  -->

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
 
</xsl:template>


</xsl:stylesheet>

  