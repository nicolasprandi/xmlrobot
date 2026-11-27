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
  <xsl:attribute name="id"><xsl:value-of select="s2/@job_city_tk" /></xsl:attribute>
  
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
    
      <xsl:call-template name="hook2">
        <xsl:with-param name="page">tk</xsl:with-param>
        <xsl:with-param name="hook">begin</xsl:with-param>
        <xsl:with-param name="station">0</xsl:with-param>
      </xsl:call-template>     
           
  <need default=""><xsl:value-of select="s2/@prop_tk_control" /></need>
  
                                  
      <xsl:call-template name="hook2">
        <xsl:with-param name="page">tk</xsl:with-param>
        <xsl:with-param name="hook">0</xsl:with-param>
        <xsl:with-param name="station">0</xsl:with-param>
      </xsl:call-template>                                       
                         
   <when>
    <!-- <xsl:if test="string-length(s1/@tk_id) > 0">    -->
      <xsl:attribute name="test">(f,has_flag,<xsl:value-of select="s2/@prop_tk_control" />,print_only)</xsl:attribute>
    <!-- </xsl:if>  -->
    
      <choose>
        <when>
          <xsl:attribute name="test">(f,has_flag,<xsl:value-of select="s2/@prop_tk_control" />,print_all_fields)</xsl:attribute>
          <ti>
            <xsl:attribute name="label">
              <xsl:value-of select="s2/@prop_label_class" />
              <xsl:text> </xsl:text>
              <xsl:value-of select="s2/@prop_label_name" />
              <xsl:if test="string-length(s1/@see_key_name) > 0">
                <xsl:text> </xsl:text>
                <xsl:value-of select="substring(s1/@see_key_name,1,1)" />
                <xsl:value-of select="s2/@prop_key" />     
                <xsl:value-of select="substring(s1/@see_key_name,2,1)" />           
              </xsl:if>   
            </xsl:attribute>
          </ti>
        </when>
        <otherwise>
          <xsl:choose>  
            <xsl:when test="s1/@detail_header_pos='left'">
              <ti>not implemented yet: s1/@detail_header_pos='left'</ti>
            </xsl:when>
            <xsl:when test="s1/@detail_header_pos='right'">
              <ti>not implemented yet: s1/@detail_header_pos='right'</ti>
            </xsl:when>      
            <xsl:otherwise>
              <!-- center -->
              <xsl:choose>
                <xsl:when test="((string-length(s1/@details_header_class)=0 or s1/@details_header_class='1') and (not(s1/@details_header_class='0'))) and ((string-length(s1/@details_header_name)=0 or s1/@details_header_name='1') and (not(s1/@details_header_name='0')))">
                  <ti flag="label_right">
                    <xsl:attribute name="label"><xsl:value-of select="s2/@prop_label_class" /></xsl:attribute>
                    <xsl:attribute name="x">
                      <xsl:text>(f,html,b,o)</xsl:text>
                      <xsl:value-of select="s2/@prop_label_name" />
                      <xsl:text>(f,html,b,c)</xsl:text>
                      <xsl:if test="string-length(s1/@see_key_name) > 0">
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="substring(s1/@see_key_name,1,1)" />
                        <xsl:value-of select="s2/@prop_key" />     
                        <xsl:value-of select="substring(s1/@see_key_name,2,1)" />           
                      </xsl:if>                   
                      </xsl:attribute>
                  
                  </ti>
                
                </xsl:when>
                
                <xsl:when test="((string-length(s1/@details_header_class)=0 or s1/@details_header_class='1') and not(s1/@details_header_class='0'))">
                  <ti >
                    <xsl:attribute name="label"> </xsl:attribute>
                    <xsl:attribute name="x">
                      <xsl:text>(f,html,b,o)</xsl:text>
                      <xsl:value-of select="s2/@prop_label_class" />
                      <xsl:text>(f,html,b,c)</xsl:text>
                      <xsl:if test="string-length(s1/@see_key_name) > 0">
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="substring(s1/@see_key_name,1,1)" />
                        <xsl:value-of select="s2/@prop_key" />     
                        <xsl:value-of select="substring(s1/@see_key_name,2,1)" />           
                      </xsl:if>                  
                    </xsl:attribute>            
                  </ti>          
                </xsl:when>       
                
                <xsl:when test="((string-length(s1/@details_header_name)=0 or s1/@details_header_name='1') and not(s1/@details_header_name='0'))">
                  <ti >
                    <xsl:attribute name="label"> </xsl:attribute>
                    <xsl:attribute name="x">
                      <xsl:text>(f,html,b,o)</xsl:text>
                      <xsl:value-of select="s2/@prop_label_name" />
                      <xsl:text>(f,html,b,c)</xsl:text>
                    </xsl:attribute> 
                    <xsl:if test="string-length(s1/@see_key_name) > 0">
                      <xsl:text> </xsl:text>
                      <xsl:value-of select="substring(s1/@see_key_name,1,1)" />
                      <xsl:value-of select="s2/@prop_key" />     
                      <xsl:value-of select="substring(s1/@see_key_name,2,1)" />           
                    </xsl:if>                         
                  </ti>          
                   
      
                </xsl:when>               
              
              </xsl:choose>
            
            </xsl:otherwise>
          
          </xsl:choose>        
        </otherwise>
      </choose>

    <!-- <ti label="mylabel for test"/> -->    

    
   </when> 
   <!-- die update-felder beim echten update nic123 -->                       
   <xsl:if test="count( //fields/field[@update=1] )"> 
   <when>
    <xsl:attribute name="test">
      <xsl:text>(f,not,(f,has_flag,</xsl:text>
      <xsl:value-of select="s2/@prop_tk_control" />
      <xsl:text>,print_only))</xsl:text>
    </xsl:attribute>  
    
   <when>    
    <xsl:attribute name="test">
      <xsl:text>(f,not,(prop,isAdd))</xsl:text>
    </xsl:attribute>    
    
    <xsl:apply-templates select="fields/field[@update='1']" mode="editor">
     <!--> <xsl:sort select="@position"fields/field[@update='1']/>  -->
    </xsl:apply-templates>
   
   
   </when>
   </when>
   
   </xsl:if>
   
    
   <tk >
    <xsl:attribute name="label">
      <xsl:text>(f,when,(f,has_flag,</xsl:text>
      <xsl:value-of select="s2/@prop_tk_control" />
      <xsl:text>,print_only),,</xsl:text>
      <xsl:value-of select="s2/@prop_label_class" />
      <xsl:text>(f,when,(f,has,prop,</xsl:text>
      <xsl:value-of select="s2/@label_name" />
      <xsl:text>), </xsl:text>
      <xsl:value-of select="s2/@prop_label_name" />
      <xsl:text>,)</xsl:text>
      <xsl:if test="string-length(s1/@see_key_name) > 0">
        <xsl:text>(f,when,(f,has,prop,</xsl:text>
        <xsl:value-of select="s2/@key" />
        <xsl:text>), </xsl:text>
        <xsl:value-of select="substring(s1/@see_key_name,1,1)" />
        <xsl:text>(f,vob,prop,</xsl:text>
        <xsl:value-of select="s2/@key" />
        <xsl:text>)</xsl:text>     
        <xsl:value-of select="substring(s1/@see_key_name,2,1)" />
        <xsl:text>,)</xsl:text>           
      </xsl:if>         
      <xsl:text>)</xsl:text>
    </xsl:attribute>
    <xsl:if test="string-length(s1/@tip) > 0">
      <xsl:attribute name="tip"><xsl:value-of select="s1/@tip" /></xsl:attribute>
    </xsl:if>
    <xsl:if test="string-length(s1/@tk_id) > 0">
      <xsl:attribute name="tk_id"><xsl:value-of select="s1/@tk_id" /></xsl:attribute>
    </xsl:if>    
    <xsl:attribute name="flag"> 
      <xsl:value-of select="s2/@prop_tk_control" />
      <xsl:text> wrap </xsl:text>
      <xsl:if test="string-length(s1/@tk_flag) > 0">
        <xsl:value-of select="s1/@tk_flag" />
      </xsl:if>        
    
    </xsl:attribute> 
    <xsl:apply-templates select="fields/field" mode="editor">
     <!--> <xsl:sort select="@position"/>  -->
    </xsl:apply-templates>
  </tk>     
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">tk</xsl:with-param>
                                    <xsl:with-param name="hook">1</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>
                                  <xsl:call-template name="hook2">
                                    <xsl:with-param name="page">tk</xsl:with-param>
                                    <xsl:with-param name="hook">end</xsl:with-param>
                                    <xsl:with-param name="station">0</xsl:with-param>
                                  </xsl:call-template>                                       
  <!--   
  <xsl:choose>
    <xsl:when test="s1/@validate_input='1'">
        <call_job>
          <xsl:attribute name="job">
            <xsl:value-of select="s2/@job_city_validate"/>
          </xsl:attribute>
        </call_job>
        <prop>
          <xsl:attribute name="id">
            <xsl:value-of select="s2/@city_input_valid"/>
          </xsl:attribute>
          <xsl:attribute name="x">
            <xsl:text>0</xsl:text>
          </xsl:attribute>
        </prop>  
        <loop id="validate_input" while="()(f,not,(prop,folder_input_valid))()">
          <xsl:attribute name="id">
            <xsl:value-of select="s2/@city_input_loop"/>
          </xsl:attribute>
          <xsl:attribute name="while">
            <xsl:text>(f,not,</xsl:text>
            <xsl:value-of select="s2/@prop_city_input_valid"/>
            <xsl:text>)</xsl:text>
          </xsl:attribute>
          <tk label="(prop,label)" flag="sum_only">
            <xsl:apply-templates select="fields/field" mode="editor">
              <xsl:sort select="@position"/>
            </xsl:apply-templates>
          </tk>
          <call_job>
            <xsl:attribute name="job">
              <xsl:value-of select="s2/@job_city_validate"/>
            </xsl:attribute>
          </call_job>                                
        </loop>        
    </xsl:when>
    <xsl:otherwise>

    </xsl:otherwise>
  
  </xsl:choose>   
  -->                               
  

  
</job>
</xsl:template>

<!-- ******************************************* -->
<xsl:template match="field" mode="editor">
  <!--      
    <tk label="Name" id="city_name" x="Stadt"/>
    <tk label="State" id="city_state" x="Staat" />
    <tk label="Population" id="city_population" x="766" />
    <tk type="bool"  label="Is in Europe" id="city_isEurope" x="1"/>  -->
  <tk>
    <xsl:if test="string-length(@label)>0">
      <xsl:attribute name="label">
        <xsl:value-of select="@label" />
      </xsl:attribute>
    </xsl:if>
    <xsl:if test="not( @type='msg')">
      <xsl:attribute name="id"> 
        <xsl:value-of select="//s1/@singular_id" />
        <xsl:text>_</xsl:text>
        <xsl:value-of select="@id" />
      </xsl:attribute>
    </xsl:if>
    
    <xsl:choose>
    
      <xsl:when test="not( @type='msg')">
        <xsl:attribute name="x"> 
          <xsl:value-of select="@default_value" />
        </xsl:attribute> 
        <xsl:if test="not(string-length(@x2)=0)">
          <xsl:attribute name="x2"> 
            <xsl:value-of select="@x2" />
          </xsl:attribute>         
        </xsl:if>
      </xsl:when>  
      <xsl:otherwise>
        <xsl:attribute name="x"> 
          <xsl:value-of select="@label" />
        </xsl:attribute>       
      </xsl:otherwise>  
    
    </xsl:choose>
    <!-- 
    <xsl:if test="string-length(@default_value)>0">
      <xsl:attribute name="x"> 
        <xsl:value-of select="@default_value" />
      </xsl:attribute>   
    </xsl:if>
    -->
    
    <xsl:choose>
      <xsl:when test="string-length(@add_edit)>0">
        <xsl:attribute name="test_first"> 
          <xsl:value-of select="@add_edit" />
        </xsl:attribute>   
      </xsl:when>
      <xsl:otherwise>
        <xsl:attribute name="test_first"> 
          <xsl:text>0</xsl:text>
        </xsl:attribute>        
      </xsl:otherwise>
    </xsl:choose>
        
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
    
    <xsl:if test="string-length(@base)>0">
      
      <xsl:attribute name="base"> 
        <xsl:value-of select="@base" />     
      </xsl:attribute>
         
    </xsl:if>        
    
    <xsl:if test="string-length(@base2)>0">
      
      <xsl:attribute name="base2"> 
        <xsl:value-of select="@base2" />     
      </xsl:attribute>
         
    </xsl:if>                 
    <xsl:attribute name="type"> 
      <xsl:value-of select="@type" />
    </xsl:attribute> 
    
    <xsl:if test="@type='text' or @type='textblock'">
      <xsl:if test="string-length(@cols)>0">        
        <xsl:attribute name="cols"> 
          <xsl:value-of select="@cols" />     
        </xsl:attribute>           
      </xsl:if>         
    </xsl:if>    
    
    <xsl:if test="string-length(@follow_job)>0">      
      <xsl:attribute name="follow_job"> 
        <xsl:value-of select="@follow_job" />     
      </xsl:attribute>         
    </xsl:if>    
        
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

  