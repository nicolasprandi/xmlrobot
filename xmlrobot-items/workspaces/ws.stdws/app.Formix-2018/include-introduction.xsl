<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015 Nicolas Prandstaetter. All rights reserved. 






 -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
<xsl:template match="parent" mode="parent_defaults">

<need default=""><xsl:text>(prop,</xsl:text><xsl:value-of select="@parent_plural_id" /><xsl:text>_sort_attribute)</xsl:text></need>

<need default=""><xsl:text>(prop,</xsl:text><xsl:value-of select="@parent_plural_id" /><xsl:text>_sort_number)</xsl:text></need>

<need default=""><xsl:text>(prop,</xsl:text><xsl:value-of select="@parent_plural_id" /><xsl:text>_sort_ignore_case)</xsl:text></need>

<need default=""><xsl:text>(prop,</xsl:text><xsl:value-of select="@parent_plural_id" /><xsl:text>_sort_desc)</xsl:text></need>

<need default=""><xsl:text>(prop,</xsl:text><xsl:value-of select="@parent_plural_id" /><xsl:text>_where)</xsl:text></need>

<need default=""><xsl:text>(prop,</xsl:text><xsl:value-of select="@parent_plural_id" /><xsl:text>_offset_label)</xsl:text></need>

<need default=""><xsl:text>(prop,</xsl:text><xsl:value-of select="@parent_plural_id" /><xsl:text>_offset)</xsl:text></need>

<need default=""><xsl:text>(prop,</xsl:text><xsl:value-of select="@parent_plural_id" /><xsl:text>_return_job)</xsl:text></need>

<need default="//"><xsl:text>(prop,</xsl:text><xsl:value-of select="@parent_plural_id" /><xsl:text>_offset_new_key)</xsl:text></need>

<need default="">
  <xsl:attribute name="default">
    <xsl:value-of select="//s2/@file_cities" />
  </xsl:attribute>
  <xsl:text>(file,</xsl:text><xsl:value-of select="@parent_plural_id" /><xsl:text>)</xsl:text></need>



</xsl:template>  
  
<xsl:template name="introduction2">

    <!-- ins include - begin -->
    <file>
      <xsl:attribute name="id">
        <xsl:text>orga</xsl:text>
      </xsl:attribute>
      <xsl:text>(file,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" />
      <xsl:text>)</xsl:text>
    </file>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga</xsl:text><xsl:text>_where</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_where</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>      
    
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga</xsl:text><xsl:text>_view_loop_id</xsl:text>
      </xsl:attribute>
      <xsl:value-of select="//conf/s2/@view_loop_id" />
      <!-- 
      <xsl:text>(prop,</xsl:text>
      
      <xsl:text>)</xsl:text>
      -->
    </prop>  
    
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga</xsl:text><xsl:text>_finished_job</xsl:text>
      </xsl:attribute>
      <xsl:value-of select="//conf/s1/@orga_finished_job" />
      <!-- 
      <xsl:text>(prop,</xsl:text>
      
      <xsl:text>)</xsl:text>
      -->
    </prop>            
    
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga</xsl:text><xsl:text>_offset</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_offset</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>    
    
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga</xsl:text><xsl:text>_offset_label</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_offset_label</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga</xsl:text><xsl:text>_offset_new_key</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_offset_new_key</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga_segment_plural</xsl:text>

      </xsl:attribute>
      <xsl:value-of select="//conf/s1/@plural_id" />
    </prop>    
    
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga_segment_singular</xsl:text>

      </xsl:attribute>
      <xsl:value-of select="//conf/s1/@singular_id" />
    </prop>      
    
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga_segment_plural_label</xsl:text>

      </xsl:attribute>
      <xsl:value-of select="//conf/s1/@plural_label" />
    </prop>    
    
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga_segment_singular_label</xsl:text>

      </xsl:attribute>
      <xsl:value-of select="//conf/s1/@singular_label" />
    </prop>       
    
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga</xsl:text>
        <xsl:text>_sort_attribute</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_sort_attribute</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga</xsl:text>
        <xsl:text>_sort_number</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_sort_number</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga</xsl:text>
        <xsl:text>_where</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_where</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga</xsl:text>
        <xsl:text>_name_expression_loop</xsl:text>
      </xsl:attribute>
      <xsl:text>()</xsl:text>
      <xsl:value-of select="//conf/fields/@name_expression_loop_unbold" />
          <xsl:if test="string-length(//s1/@see_key_name) > 0">
            <xsl:text> </xsl:text>
            <xsl:value-of select="substring(//s1/@see_key_name,1,1)" />
            <xsl:value-of select="s2/@loop_i_row" />              
            <xsl:value-of select="substring(//s1/@see_key_name,2,1)" />           
          </xsl:if>           
      <xsl:text>()</xsl:text>
 
    </prop>    
    <!-- wird eigentlich nicht verwendet      , oder schon ?? -->
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga</xsl:text>
        <xsl:text>_desc_expression_loop</xsl:text>
      </xsl:attribute>
      <xsl:text>()</xsl:text>
      <xsl:value-of select="//conf/fields/@desc_expression_loop" />
      <xsl:text>()</xsl:text>
    </prop>              
    
    <xsl:if test="string-length(//s1/@tr_job) > 0">
    
        <prop>
          <xsl:attribute name="id">
            <xsl:text>orga_tr_job</xsl:text>
          </xsl:attribute>
          <xsl:value-of select="//s1/@tr_job" />
        </prop>    
            
    </xsl:if>    
    
    <!--
    <prop>
      <xsl:attribute name="id">
        <xsl:text>orga</xsl:text>
        <xsl:text>_return_job</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s2/@return_job" />
      <xsl:text>)</xsl:text>
    </prop> 
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="//conf/s1/@singular_id" />
        <xsl:text>_key23</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@singular_id" /><xsl:text>_key</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>      -->  
    
    <xsl:apply-templates select="//parents/parent" mode="introduction"/>
    
    <!-- ins include - end -->

</xsl:template>  

<xsl:template match="action" mode="header">
  <ti>
      <xsl:if test="string-length(@test) > 0">
        <xsl:attribute name="test">
          <xsl:value-of select="@test" />
        </xsl:attribute>           
      </xsl:if>   
            
      <xsl:attribute name="x">
        <xsl:text> </xsl:text>
      </xsl:attribute>         
        
  </ti>
</xsl:template>

<xsl:template name="action_view">
  <xsl:call-template name="action_both">
    <xsl:with-param name="typus">View</xsl:with-param>
  </xsl:call-template>
  


</xsl:template>

<xsl:template name="action_detail">   
  <xsl:call-template name="action_both">
    <xsl:with-param name="typus">Detail</xsl:with-param>
  </xsl:call-template>

</xsl:template>

<xsl:template name="action_both">
  <xsl:param name="typus"/>            <!-- View oder Detail -->


    <xsl:if test="not ( //s1/@button_rule='off' )">
    <xsl:attribute name="flag"><xsl:text>button</xsl:text></xsl:attribute> 
    
    <xsl:attribute name="class">
    
      <xsl:choose> 
        <xsl:when test="$typus='View'">    
        
          <xsl:choose> 
            <xsl:when test="string-length(@classView)>0">                   
              <xsl:value-of select="@classView" />
            </xsl:when>  
            <xsl:when test="string-length(@classAddView)>0">                   
              <xsl:text>fb fbAction fbAction</xsl:text><xsl:value-of select="//s2/@firstcase_singular_id" /><xsl:value-of select="@id" /><xsl:text> </xsl:text><xsl:value-of select="@classAddView" />
            </xsl:when>      
            <xsl:otherwise>
              <xsl:text>fb fbAction fbAction</xsl:text><xsl:value-of select="//s2/@firstcase_singular_id" /><xsl:value-of select="@id" />
            </xsl:otherwise>                           
          </xsl:choose>                          
      
        </xsl:when>  
        
        <xsl:when test="$typus='Detail'">    
        
          <xsl:choose> 
            <xsl:when test="string-length(@classDetails)>0">                   
              <xsl:value-of select="@classDetails" />                                         
            </xsl:when>  
            <xsl:when test="string-length(@classAddDetails)>0">                   
              <xsl:text>fx fxAction fxAction</xsl:text><xsl:value-of select="//s2/@firstcase_singular_id" /><xsl:value-of select="@id" /><xsl:text> </xsl:text><xsl:value-of select="@classAddDetails" />
            </xsl:when>        
            <xsl:otherwise>
              <xsl:text>fx fxAction fxAction</xsl:text><xsl:value-of select="//s2/@firstcase_singular_id" /><xsl:value-of select="@id" />
            </xsl:otherwise>                        
          </xsl:choose>                          
      
        </xsl:when>                    
        
      </xsl:choose> 
      
    </xsl:attribute>
    </xsl:if>  
 

    
    <xsl:attribute name="explain">
      <xsl:value-of select="@label" />
    </xsl:attribute>  
    <xsl:attribute name="job">
      <xsl:value-of select="@hook_job" />
    </xsl:attribute>  

    <xsl:if test="string-length(@call_flag)>0">
      <xsl:attribute name="flag">
        <xsl:value-of select="@call_flag" />
      </xsl:attribute>              
    </xsl:if>         

</xsl:template>



<xsl:template name="inner_hook">
<xsl:param name="page"/>
            <prop>           
              <xsl:attribute name="id">
                <xsl:text>greetings_from_page</xsl:text>
              </xsl:attribute>          
              <xsl:value-of select="$page" />
            </prop>   
            <prop>           
              <xsl:attribute name="id">
                <xsl:text>greetings_from_action</xsl:text>
              </xsl:attribute>          
              <xsl:value-of select="@id" />
            </prop>             
  <xsl:choose>
    <xsl:when test="$page='view'">
            <prop>
        <xsl:attribute name="page">
          <xsl:value-of select="$page" />
        </xsl:attribute>              
              <xsl:attribute name="id">
                <xsl:value-of select="//conf/s2/@key" /> 
              </xsl:attribute>          
              <xsl:value-of select="//conf/s2/@loop_i_row" />
            </prop>    
            
    </xsl:when>
    <xsl:when test="$page='details'">

      <prop>
        <xsl:attribute name="page">
          <xsl:value-of select="$page" />
        </xsl:attribute>      
        <xsl:attribute name="id">
          <xsl:value-of select="//conf/s1/@singular_id" /><xsl:text>_key</xsl:text>
        </xsl:attribute>
        <xsl:text>(prop,</xsl:text>
        <xsl:value-of select="//conf/s1/@singular_id" /><xsl:text>_key</xsl:text>
        <xsl:text>)</xsl:text>
      </prop>       
    
    </xsl:when>    
  </xsl:choose>
  
   
</xsl:template>
  
<!-- action:ti_call springt nach action_view -->
<xsl:template match="action" mode="ti_call_view">
<xsl:param name="page"/>
    
        <ti isFromAction="1">
        
        
          <xsl:choose>
            <xsl:when test="string-length(@test_view) > 0 ">
              <xsl:attribute name="test">
                <xsl:value-of select="@test_view" />
              </xsl:attribute>           
            </xsl:when>               
            
            <xsl:when test="string-length(@test) > 0">
              <xsl:attribute name="test">
                <xsl:value-of select="@test" />
              </xsl:attribute>           
            </xsl:when>               
          
          </xsl:choose>           
          <!--      
          <xsl:if test="//s1/@detail_actions_right=1">
            <xsl:attribute name="label">
              <xsl:text> </xsl:text>
            </xsl:attribute>           
          </xsl:if>
          -->      
          <xsl:attribute name="type">
            <xsl:value-of select="//conf/s2/@ti_call" />
          </xsl:attribute>
          <xsl:attribute name="page">
            <xsl:value-of select="$page" />
          </xsl:attribute>          
       
          <xsl:call-template name="action_view"/>  
          
          <xsl:call-template name="inner_hook">          
            <xsl:with-param name="page" select="$page"/>
          </xsl:call-template>
          
          <xsl:call-template name="introduction"  />                                     
        </ti>
            
</xsl:template>

<!-- action:ti_call_detail springt nach action_detail -->
<xsl:template match="action" mode="ti_call_detail">
<xsl:param name="page"/>
       <ti type="rec">
          
          <xsl:choose>
            <xsl:when test="string-length(@test_detail) > 0">
              <xsl:attribute name="test">
                <xsl:value-of select="@test_detail" />
              </xsl:attribute>           
            </xsl:when>   
            
            <xsl:when test="string-length(@test) > 0">
              <xsl:attribute name="test">
                <xsl:value-of select="@test" />
              </xsl:attribute>           
            </xsl:when>               
          
          </xsl:choose>
                 
          <xsl:if test="//s1/@detail_actions_right=1">
            <xsl:attribute name="label">
              <xsl:text> </xsl:text>
            </xsl:attribute>           
          </xsl:if>             

        <ti >
                  

          <xsl:attribute name="type">
            <xsl:value-of select="//conf/s2/@ti_call" />
          </xsl:attribute>
          <xsl:attribute name="page">
            <xsl:value-of select="$page" />
          </xsl:attribute>          
     
          <xsl:choose>
            <xsl:when test="//s1/@steinzeit=1">
              <xsl:call-template name="action_view"/>
            </xsl:when>
            <xsl:otherwise> 
              <xsl:call-template name="action_detail"/>
            </xsl:otherwise>
          </xsl:choose>  
          
          <xsl:call-template name="inner_hook">          
            <xsl:with-param name="page" select="$page"/>
          </xsl:call-template>
          
          <xsl:call-template name="introduction"  />                                     
        </ti>
        <ti/>
      </ti>
            
</xsl:template>


<xsl:template name="introduction">

    <!-- ins include - begin -->
    <file>
      <xsl:attribute name="id">
        <xsl:value-of select="//conf/s1/@plural_id" />
      </xsl:attribute>
      <xsl:text>(file,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" />
      <xsl:text>)</xsl:text>
    </file>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_offset</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_offset</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>    
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_offset_label</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_offset_label</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_offset_new_key</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_offset_new_key</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="//conf/s1/@plural_id" />
        <xsl:text>_sort_attribute</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_sort_attribute</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="//conf/s1/@plural_id" />
        <xsl:text>_sort_number</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_sort_number</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="//conf/s1/@plural_id" />
        <xsl:text>_sort_ignore_case</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_sort_ignore_case</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>   
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="//conf/s1/@plural_id" />
        <xsl:text>_sort_desc</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_sort_desc</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>        
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="//conf/s1/@plural_id" />
        <xsl:text>_where</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@plural_id" /><xsl:text>_where</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="//conf/s2/@return_job" />
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s2/@return_job" />
      <xsl:text>)</xsl:text>
    </prop> 
    <!--
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="//conf/s1/@singular_id" />
        <xsl:text>_key23</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="//conf/s1/@singular_id" /><xsl:text>_key</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>      -->  
    
    <xsl:apply-templates select="//parents/parent" mode="introduction"/>
    
    <!-- ins include - end -->

</xsl:template>

<xsl:template match="parent" mode="introduction">

    <file>
      <xsl:attribute name="id">
        <xsl:value-of select="@parent_plural_id" />
      </xsl:attribute>
      <xsl:text>(file,</xsl:text>
      <xsl:value-of select="@parent_plural_id" />
      <xsl:text>)</xsl:text>
    </file>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="@parent_plural_id" /><xsl:text>_offset</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@parent_plural_id" /><xsl:text>_offset</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>    
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="@parent_plural_id" /><xsl:text>_offset_label</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@parent_plural_id" /><xsl:text>_offset_label</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="@parent_plural_id" /><xsl:text>_offset_new_key</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@parent_plural_id" /><xsl:text>_offset_new_key</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="@parent_plural_id" />
        <xsl:text>_sort_attribute</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@parent_plural_id" /><xsl:text>_sort_attribute</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="@parent_plural_id" />
        <xsl:text>_sort_number</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@parent_plural_id" /><xsl:text>_sort_number</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="@parent_plural_id" />
        <xsl:text>_sort_ignore_case</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@parent_plural_id" /><xsl:text>_sort_ignore_case</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>    
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="@parent_plural_id" />
        <xsl:text>_sort_desc</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@parent_plural_id" /><xsl:text>_sort_desc</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>        
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="@parent_plural_id" />
        <xsl:text>_where</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@parent_plural_id" /><xsl:text>_where</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="@parent_plural_id" />
        <xsl:text>_return_job</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@parent_plural_id" /><xsl:text>_return_job</xsl:text>
      <xsl:text>)</xsl:text>
    </prop>
    
    <prop>
      <xsl:attribute name="id">
        <xsl:value-of select="@parent_singular_id" />
        <xsl:text>_key</xsl:text>
      </xsl:attribute>
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@parent_singular_id" /><xsl:text>_key</xsl:text>
      <xsl:text>)</xsl:text>
    </prop> 

</xsl:template>

</xsl:stylesheet>