<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015 Nicolas Prandstaetter. All rights reserved. 
 -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template name="an-up-details-button">

  <xsl:param name="position"/> <!-- top or bottom -->
  <xsl:param name="from"/> <!-- info or help -->
  <xsl:if test="( $position='top' and s2/@off_up_details_top=0 ) or ( $position='bottom' and s2/@off_up_details_bottom=0 ) ">
    <!-- 
    <xsl:if test="$position='bottom'">
      <ti_break class_table="tabNavigator"/>
    </xsl:if>
    -->
    
    <ti_break class_table="tabNavigator"/>
    
    <choose>
      <when>
        <!-- wenn wir aus der hilfe kommen-->
        <xsl:attribute name="test">
          <xsl:text>(f,has_flag,</xsl:text>
          <xsl:value-of select="s2/@prop_tk_control" />
          <xsl:text>,print_all_fields)</xsl:text>
        </xsl:attribute>    
        
<ti type="rec" label=" " >   
 <ti type="call_job" explain="(prop,_x_go_up_sys2)" align="right" class="justback2">
 
         <xsl:if test="not ( s1/@button_rule='off' )">
        
          <xsl:attribute name="flag"><xsl:text>button</xsl:text></xsl:attribute>
        
        </xsl:if>  
        
        <xsl:attribute name="job">
          <xsl:value-of select="s2/@job_city_details" />
        </xsl:attribute>  
        
          <prop>
            <xsl:attribute name="id">
              <xsl:value-of select="s2/@key" /> 
            </xsl:attribute>           
            <xsl:value-of select="s2/@prop_key" />
          </prop>
          <xsl:call-template name="introduction"/>        
 </ti>
</ti>        
      
      </when>
      
      <otherwise>
      
        <xsl:choose>
          <xsl:when test="count (//parents/parent) > 0">
            <!-- <ti>INFO MIT PARENT</ti> -->

<ti type="rec" label=" ">   
 <ti type="call_job" explain="(prop,_x_go_up_sys)" align="right" class="justup">
        <xsl:attribute name="job">
         <xsl:if test="not ( s1/@button_rule='off' )">
        
          <xsl:attribute name="flag"><xsl:text>button</xsl:text></xsl:attribute>
        
        </xsl:if>          
          <xsl:value-of select="//parents/parent[position() = last()]/@parent_singular_id" />
          <xsl:text>-see</xsl:text>
        </xsl:attribute>  
        
          <xsl:call-template name="introduction"/>        
 </ti>
</ti>                                 
          
          </xsl:when>
          
          <xsl:otherwise>
            <!-- <ti>INFO OHNE PARENT</ti> -->
<ti type="rec" label=" ">   
 <ti type="call_job" explain="(prop,_x_go_up_sys)"  align="right" class="justup">
         <xsl:if test="not ( s1/@button_rule='off' )">        
          <xsl:attribute name="flag"><xsl:text>button</xsl:text></xsl:attribute>        
        </xsl:if>   
        <xsl:attribute name="job">
          <xsl:value-of select="s2/@job_city_view" />
        </xsl:attribute>  
        
          <xsl:call-template name="introduction"/>        
 </ti>
</ti>              
          </xsl:otherwise>
        
        
        
        </xsl:choose>
      
      
      </otherwise>                                                   
    
    </choose>
    <ti_break/>
    <xsl:if test="$position='top'">
    <xsl:if test="$from='info'">
      <!-- <ti_break/> -->
    </xsl:if>
    </xsl:if>    
    
  </xsl:if>

</xsl:template>  


<xsl:template name="an-delete-button-details">


    <xsl:if test="s1/@details_delete=1">
        <ti type="rec">
          <xsl:if test="s1/@detail_actions_right=1">
            <xsl:attribute name="label">
              <xsl:text> </xsl:text>
            </xsl:attribute>           
          </xsl:if>  
          <xsl:if test="string-length(s1/@test_delete)>0">
            <xsl:attribute name="test">
              <xsl:value-of select="s1/@test_delete" />
            </xsl:attribute>           
          </xsl:if>                  
    
        <ti>
        
                              
          <xsl:attribute name="type">
            <xsl:value-of select="s2/@ti_call" />
          </xsl:attribute>          
          
          <xsl:attribute name="flag"><xsl:text>button</xsl:text></xsl:attribute> 
          <xsl:attribute name="class"><xsl:text>fx fxDelete fxDelete</xsl:text><xsl:value-of select="s2/@firstcase_singular_id" /></xsl:attribute>                     
          
          <xsl:attribute name="explain">
            <xsl:text>[ </xsl:text>
            <xsl:value-of select="s2/@label_delete" /> 
            <xsl:text> ]</xsl:text>
          </xsl:attribute> 
          <xsl:attribute name="job">
            <xsl:value-of select="s2/@job_city_delete" /> 
          </xsl:attribute>                
          <prop>
            <xsl:attribute name="id">
              <xsl:value-of select="s2/@key" /> 
            </xsl:attribute>           
            <xsl:value-of select="s2/@prop_key" />
          </prop>
          <xsl:call-template name="introduction"/>
        </ti> 
        <ti/> 
        </ti>
      </xsl:if>
</xsl:template>
      
<xsl:template name="an-edit-button-details">

<xsl:choose>
  <xsl:when test="s2/@details_edit_float=1">
  
   <ti type="menu" info="an-edit-button-details">
      <xsl:attribute name="first_cell">
        <xsl:choose>   
          <xsl:when test="s1/@detail_actions_right=1">
            <xsl:text>2</xsl:text>         
          </xsl:when>  
          <xsl:otherwise>
            <xsl:text>1</xsl:text>
          </xsl:otherwise>
        </xsl:choose>              
      </xsl:attribute>
      
      <xsl:choose>   
        <xsl:when test="string-length(s1/@test_edit_detail)>0">
          <xsl:attribute name="test">
            <xsl:value-of select="s1/@test_edit_detail" />
          </xsl:attribute>           
        </xsl:when>                         
        <xsl:when test="string-length(s1/@test_edit)>0">
          <xsl:attribute name="test">
            <xsl:value-of select="s1/@test_edit" />
          </xsl:attribute>           
        </xsl:when>        
      </xsl:choose>      
    

      <!-- der edit button -->    
      <ti> 
                               
        <xsl:attribute name="type">
          <xsl:value-of select="s2/@ti_call" />
        </xsl:attribute>  
                
        <xsl:attribute name="flag"><xsl:text>button</xsl:text></xsl:attribute> 
        
        <xsl:attribute name="class"><xsl:text>fb fbEdit fbEditDetail fbEditDetail</xsl:text><xsl:value-of select="s2/@firstcase_singular_id" /></xsl:attribute>                     
        
        <xsl:attribute name="explain">

          <xsl:value-of select="s2/@label_edit" />

          <xsl:if test="s1/@details_edit_named = 1">
            <xsl:text> &apos;</xsl:text>
            <xsl:value-of select="fields/@name_expression_unbold" />
            <xsl:text>&apos;</xsl:text>
          </xsl:if> 
                     
        </xsl:attribute> 
        <xsl:attribute name="job">
          <xsl:value-of select="s2/@job_city_edit" /> 
        </xsl:attribute>                
        <prop>
          <xsl:attribute name="id">
            <xsl:value-of select="s2/@key" /> 
          </xsl:attribute>           
          <xsl:value-of select="s2/@prop_key" />
        </prop>
        <xsl:call-template name="introduction"/>
      </ti>
      
      <!-- der ask-button -->          
  
       <ti >      
          <xsl:attribute name="type">
            <xsl:value-of select="s2/@ti_call" />
          </xsl:attribute>     
          <xsl:attribute name="flag"><xsl:text>button</xsl:text></xsl:attribute> 
          <xsl:attribute name="class"><xsl:text>fb fbAskAsk fbAskAsk</xsl:text><xsl:value-of select="s2/@firstcase_singular_id" /></xsl:attribute>                     
          <xsl:attribute name="explain">
            <xsl:value-of select="s1/@label_askask" />                        
          </xsl:attribute> 
      
          <xsl:attribute name="job">
            <xsl:value-of select="s2/@job_city_details" /><xsl:text>-more</xsl:text>
          </xsl:attribute>                
          <prop>
            <xsl:attribute name="id">
              <xsl:value-of select="s2/@key" /> 
            </xsl:attribute>           
            <xsl:value-of select="s2/@prop_key" />
          </prop>
          <xsl:call-template name="introduction"/>
        </ti>      
          
              
    </ti> <!-- das ende vom tirec <ti_break/>-->   
  
  </xsl:when>
  <xsl:otherwise>
    <!-- das ist die alte version die unter details_edit_float=0 noch verwendet werden kann -->
  

<ti type="rec" info="an-edit-button">
          <xsl:if test="s1/@detail_actions_right=1">
            <xsl:attribute name="label">
              <xsl:text> </xsl:text>
            </xsl:attribute>           
          </xsl:if>   
          
          <xsl:choose>   
            <xsl:when test="string-length(s1/@test_edit_detail)>0">
              <xsl:attribute name="test">
                <xsl:value-of select="s1/@test_edit_detail" />
              </xsl:attribute>           
            </xsl:when>  
                           
            <xsl:when test="string-length(s1/@test_edit)>0">
              <xsl:attribute name="test">
                <xsl:value-of select="s1/@test_edit" />
              </xsl:attribute>           
            </xsl:when>  
          
          </xsl:choose>
         
                            
          <ti> 
                                   
            <xsl:attribute name="type">
              <xsl:value-of select="s2/@ti_call" />
            </xsl:attribute>          
            <xsl:attribute name="flag"><xsl:text>button</xsl:text></xsl:attribute> 
            <xsl:attribute name="class"><xsl:text>fx fxEdit fxEditDetail fxEditDetail</xsl:text><xsl:value-of select="s2/@firstcase_singular_id" /></xsl:attribute>                     
            
            <xsl:attribute name="explain">

              <xsl:value-of select="s2/@label_edit" />

              <!-- 
              <xsl:text>- </xsl:text>
              <xsl:value-of select="s2/@label_edit" />
              <xsl:text> -</xsl:text>              
              -->
              <xsl:if test="s1/@details_edit_named = 1">
                <xsl:text> &apos;</xsl:text>
                <xsl:value-of select="fields/@name_expression_unbold" />
                <xsl:text>&apos;</xsl:text>
              </xsl:if> 
                         
            </xsl:attribute> 
            <xsl:attribute name="job">
              <xsl:value-of select="s2/@job_city_edit" /> 
            </xsl:attribute>                
            <prop>
              <xsl:attribute name="id">
                <xsl:value-of select="s2/@key" /> 
              </xsl:attribute>           
              <xsl:value-of select="s2/@prop_key" />
            </prop>
            <xsl:call-template name="introduction"/>
          </ti>
          
             <ti >      
                <xsl:attribute name="type">
                  <xsl:value-of select="s2/@ti_call" />
                </xsl:attribute>     
                <xsl:attribute name="flag"><xsl:text>button</xsl:text></xsl:attribute> 
                <xsl:attribute name="class"><xsl:text>fx fxAskAsk fxAskAsk</xsl:text><xsl:value-of select="s2/@firstcase_singular_id" /></xsl:attribute>                     
                <xsl:attribute name="explain">
                  <xsl:value-of select="s1/@label_askask" />                        
                </xsl:attribute> 
                <xsl:attribute name="align">
                  <xsl:text>right</xsl:text>                        
                </xsl:attribute>             
                <xsl:attribute name="job">
                  <xsl:value-of select="s2/@job_city_details" /><xsl:text>-more</xsl:text>
                </xsl:attribute>                
                <prop>
                  <xsl:attribute name="id">
                    <xsl:value-of select="s2/@key" /> 
                  </xsl:attribute>           
                  <xsl:value-of select="s2/@prop_key" />
                </prop>
                <xsl:call-template name="introduction"/>
              </ti>              
          
        
        </ti> <!-- das ende vom tirec <ti_break/>-->  
    
  </xsl:otherwise>
  
</xsl:choose>
        
        
</xsl:template> 

<xsl:template name="an-edit-button-just">

<ti type="rec" info="an-edit-button-just">
          <xsl:if test="s1/@detail_actions_right=1">
            <xsl:attribute name="label">
              <xsl:text> </xsl:text>
            </xsl:attribute>           
          </xsl:if>   
          
          <!--         
          <xsl:if test="string-length(s1/@test_edit)>0">
            <xsl:attribute name="test">
              <xsl:value-of select="s1/@test_edit" />
            </xsl:attribute>           
          </xsl:if>  
          
          
          -->
          
         
                            
          <ti> 
                                   
            <xsl:attribute name="type">
              <xsl:value-of select="s2/@ti_call" />
            </xsl:attribute>          
            <xsl:attribute name="flag"><xsl:text>button</xsl:text></xsl:attribute> 
            <xsl:attribute name="class"><xsl:text>fx fxEdit fxEditDetail fxEditDetail</xsl:text><xsl:value-of select="s2/@firstcase_singular_id" /></xsl:attribute>                     
            
            <xsl:attribute name="explain">

              <xsl:value-of select="s2/@label_edit" />

              <!-- 
              <xsl:text>- </xsl:text>
              <xsl:value-of select="s2/@label_edit" />
              <xsl:text> -</xsl:text>              
              -->
              <xsl:if test="s1/@details_edit_named = 1">
                <xsl:text> &apos;</xsl:text>
                <xsl:value-of select="fields/@name_expression_unbold" />
                <xsl:text>&apos;</xsl:text>
              </xsl:if> 
                         
            </xsl:attribute> 
            <xsl:attribute name="job">
              <xsl:value-of select="s2/@job_city_edit" /> 
            </xsl:attribute>                
            <prop>
              <xsl:attribute name="id">
                <xsl:value-of select="s2/@key" /> 
              </xsl:attribute>           
              <xsl:value-of select="s2/@prop_key" />
            </prop>
            <xsl:call-template name="introduction"/>
          </ti>
          
      
          
        
        </ti> <!-- das ende vom tirec <ti_break/>--> 
        
        
</xsl:template> 

<xsl:template name="an-help-button">

<ti type="rec">
                    
    <ti> 

    </ti>
          
     <ti >      
        <xsl:attribute name="type">
          <xsl:value-of select="s2/@ti_call" />
        </xsl:attribute>          
        <xsl:attribute name="flag"><xsl:text>button</xsl:text></xsl:attribute> 
        <xsl:attribute name="class"><xsl:text>fx fxAskAsk fxAskAsk</xsl:text><xsl:value-of select="s2/@firstcase_singular_id" /></xsl:attribute>                                    
        <xsl:attribute name="explain">
          <xsl:value-of select="s1/@label_askask" />                       
        </xsl:attribute> 
        <xsl:attribute name="align">
          <xsl:text>right</xsl:text>                        
        </xsl:attribute>             
        <xsl:attribute name="job">
          <xsl:value-of select="s2/@job_city_details" /><xsl:text>-more</xsl:text>
        </xsl:attribute>                
        <prop>
          <xsl:attribute name="id">
            <xsl:value-of select="s2/@key" /> 
          </xsl:attribute>           
          <xsl:value-of select="s2/@prop_key" />
        </prop>
        <xsl:call-template name="introduction"/>
      </ti>        
          
        
        </ti> <!-- das ende vom tirec <ti_break/>--> 
        
        
</xsl:template> 

</xsl:stylesheet>