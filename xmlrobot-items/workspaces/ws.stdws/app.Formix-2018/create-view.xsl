<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015 Nicolas Prandstaetter. All rights reserved. 

 -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>
<xsl:include href="include-introduction.xsl" />
<xsl:include href="include-hooks2.xsl" />
<xsl:template match="/">
   <xsl:apply-templates/>
</xsl:template>

<xsl:template match="conf">
<xsl:variable name="test">1</xsl:variable>

<!-- <job id="View Cities" joblevel="app" >  -->
<job>
  <xsl:attribute name="id"><xsl:value-of select="s2/@job_city_view" /></xsl:attribute>
  <xsl:attribute name="level">
    <xsl:value-of select="s1/@job_level" />
  </xsl:attribute>  
  <xsl:attribute name="role">
    <xsl:value-of select="s1/@job_role_view" />
  </xsl:attribute>    
  <!-- 
  <xsl:attribute name="tip">
    <xsl:text>Lists all </xsl:text>
    <xsl:value-of select="s1/@plural_label" />
  </xsl:attribute>   
  --> 
  <need default=""><xsl:value-of select="s2/@prop_cities_sort_attribute" /></need>              
  <need default=""><xsl:value-of select="s2/@prop_cities_sort_number" /></need>  
  <need default=""><xsl:value-of select="s2/@prop_cities_sort_ignore_case" /></need>
  <need default=""><xsl:value-of select="s2/@prop_cities_sort_desc" /></need>
  <need default=""><xsl:value-of select="s2/@prop_cities_where" /></need>
  <need default=""><xsl:value-of select="s2/@prop_cities_offset_label" /></need>                     
  <need default="./"><xsl:value-of select="s2/@prop_cities_offset" /></need>  
  <need default=""><xsl:value-of select="s2/@prop_return_job" /></need>  
  <need default="//">(prop,<xsl:value-of select="s2/@cities_offset_new_key" />)</need>
  <need default="0">(prop,<xsl:value-of select="s1/@plural_id" />_no_header)</need>
         
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
    <xsl:with-param name="page">view</xsl:with-param>
    <xsl:with-param name="hook">begin</xsl:with-param>
    <xsl:with-param name="station">0</xsl:with-param>
  </xsl:call-template>
  

  
  <!--  <ti type="call" explain="Add new City(prop,cities_offset_label)" job="City Add" /> -->
  

  <xsl:if test="s1/@view_add=1 and (s1/@view_add_location='top')">
    <xsl:call-template name="add_button"/>
  </xsl:if>


  
  <prop id="nothing">(f,not,(prop,<xsl:value-of select="s1/@plural_id" />_no_header))</prop>
  
  <xsl:call-template name="hook2">
    <xsl:with-param name="page">view</xsl:with-param>
    <xsl:with-param name="hook">0</xsl:with-param>
    <xsl:with-param name="station">0</xsl:with-param>
  </xsl:call-template>
    
  <!-- <loop id="i" xml="(file,cities)" xpath="(prop,cities_offset)city" sort="id" number="id">  -->
  <loop  missing_strategy="empty">
    <xsl:attribute name="id">
      <xsl:value-of select="s2/@view_loop_id" />
    </xsl:attribute>  
    <xsl:attribute name="xml">
      <xsl:value-of select="s2/@file_cities" />
    </xsl:attribute>  
    <xsl:attribute name="xpath">
      <xsl:value-of select="s2/@prop_cities_offset" />
      <xsl:value-of select="s1/@singular_id" />
      <xsl:value-of select="s2/@prop_cities_where" />
    </xsl:attribute>

    <xsl:attribute name="sort">
      <xsl:value-of select="s2/@prop_cities_sort_attribute" />
    </xsl:attribute>
  
    <xsl:attribute name="number">
      <xsl:value-of select="s2/@prop_cities_sort_number" />
    </xsl:attribute>  
    
    <xsl:attribute name="ignore_case">
      <xsl:value-of select="s2/@prop_cities_sort_ignore_case" />
    </xsl:attribute>
    
    <xsl:attribute name="desc">
      <xsl:value-of select="s2/@prop_cities_sort_desc" />
    </xsl:attribute>          
    
    <!-- nothing sagt aus ob es keine ueberschrift gibt -->
    <when test="(prop,nothing)">
        <!-- ueberschrift -->
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">view</xsl:with-param>
          <xsl:with-param name="hook">1</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>
        
        <!-- bei view_padding_top gibt es einen abstand nach oben -->
        <xsl:if test="s1/@view_padding_top='1'">
          <ti><ti/></ti>       
        </xsl:if>   
        
        <!-- ueberschrifts-label wenn es einen offset_label gibt -->
        <when>
          <xsl:attribute name="test">
            <xsl:text>(f,gt,(f,len,</xsl:text>
            <xsl:value-of select="s2/@prop_cities_offset_label" />
            <xsl:text>),0)</xsl:text>
          </xsl:attribute>
          <!-- <ti>(f,html,bold,open)<xsl:value-of select="s2/@prop_cities_offset_label" /><xsl:text>: </xsl:text>(f,html,bold,close)</ti> -->
          <ti>
            <xsl:attribute name="label">
              <xsl:value-of select="s2/@prop_cities_offset_label" />
            </xsl:attribute>          
          </ti>
          <!-- actions mit kennzeichen view_prolog in ueberschriftenhedaer einbinden 
                das schaut doppelt aus, wird unten eh gemacht im rec, jessas 
          <xsl:apply-templates select="//conf/actions/action[@view_prolog=1]" mode="header">    
            <xsl:sort select="@sort"/>
          </xsl:apply-templates>           -->

        </when>
        
        <!-- jessas: so war das vorher, aber das schaut falsch aus
        <xsl:if test="count(fields/field[@view_flag=1]) and ( not ( s1/@view_header=0 ) )">
        not ( s1/@view_header=0 )
        -->
        <xsl:if test="count(fields/field[@view_flag=1]) and ( not ( s1/@view_header=0 ) )">
        <!-- 
          <ti type="rec">
            <ti>ueberschrift</ti>
          </ti>                       -->
        </xsl:if>
        
        
              <xsl:if test="1=9">  
           
          <ti type="rec">       
            <!-- view_trailer_span
            <ti>hallo halli</ti>   count (//conf/actions/action[@view_prolog='1']) > 0  -->
            
            <xsl:if test="count (//conf/actions/action[@view_prolog='1']) > 0">
            
                <xsl:apply-templates select="//conf/actions/action[@view_prolog=1]" mode="header">    
                  <xsl:sort select="@sort"/>
                </xsl:apply-templates>  
            </xsl:if>
             <!-- ueberschrift fuer desc nik 123 -->
            <xsl:if test="count (fields/field[@desc_flag=1]) > 0">
              <ti></ti>      
            </xsl:if>      
                       
            <xsl:call-template name="view_trailer_span"/>
            
            <!-- view_edit plus view_edit_very_first -->
            <xsl:if test="s1/@view_edit=1">    
              <xsl:if test="s1/@view_edit_very_first=1">
                <ti> </ti>  
              </xsl:if>      
            </xsl:if>            
            
            <!-- name gegebenenfalls mit view_name_span -->                  
            <ti x=" ">
              <xsl:if test="string-length(s1/@view_name_span) > 0">
                <xsl:attribute name="span">      
                  <xsl:value-of select="s1/@view_name_span" />                    
                </xsl:attribute>
              </xsl:if>              
            </ti> 

            
            <!-- view_edit plus view_edit_first -->
            <xsl:if test="s1/@view_edit=1">  
              <xsl:if test="s1/@view_edit_first=1">
                <ti></ti>  
              </xsl:if>         
            </xsl:if> 
            
            <!-- ueberschrift fuer desc -->
            <xsl:if test="count (fields/field[@desc_flag=1]) > 0">
              <ti></ti>      
            </xsl:if>               
            
            <!-- spalten fuer felder  -->
            <xsl:choose>
              <xsl:when test="count (fields/field[string-length(@view_position) > 0])">
                <xsl:apply-templates select="fields/field" mode="header">    
                  <xsl:sort select="@view_position"/>
                </xsl:apply-templates>                   
              </xsl:when>
              <xsl:otherwise>
                <xsl:apply-templates select="fields/field" mode="header">    
                  <xsl:sort select="@position"/>
                </xsl:apply-templates>              
              </xsl:otherwise>
            </xsl:choose>   
              
              
            <!-- edit button diesmal nach den feldern -->
            <xsl:if test="s1/@view_edit=1">
              <xsl:if test="not ( s1/@view_edit_first=1 )">
                <ti></ti>  
              </xsl:if>   
             </xsl:if> 
             
             <!-- delete button -->
             <xsl:if test="s1/@view_delete=1">        
              <ti x=" ">    </ti> 
             </xsl:if>    
            
            <!-- aktionen -->
            <xsl:apply-templates select="//conf/actions/action[@view=1]" mode="header">    
              <xsl:sort select="@sort"/>
            </xsl:apply-templates>     
  
            
          </ti>  
        
        </xsl:if>
        
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">view</xsl:with-param>
          <xsl:with-param name="hook">1</xsl:with-param>
          <xsl:with-param name="station">1</xsl:with-param>
        </xsl:call-template> 
      <prop id="nothing">0</prop> 
    </when>
    
    <xsl:choose>
      <xsl:when test="count (fields/field[string-length(@view_position) > 0])">
        <xsl:apply-templates select="fields/field" mode="data_prep">    
          <xsl:sort select="@view_position"/>
        </xsl:apply-templates>                   
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="fields/field" mode="data_prep">    
          <xsl:sort select="@position"/>
        </xsl:apply-templates>              
      </xsl:otherwise>
    </xsl:choose>    
        
    <xsl:call-template name="hook2">
      <xsl:with-param name="page">view</xsl:with-param>
      <xsl:with-param name="hook">2</xsl:with-param>
      <xsl:with-param name="station">0</xsl:with-param>
    </xsl:call-template> 
    
    
    <!-- das ist jetzt der echte datensatz -->
    <ti type="rec" >                
      <!-- jessas: sinnlos               
      <xsl:attribute name="type">
      <xsl:choose>
        <xsl:when test="s1/@view_div_on_web=1">
          <xsl:text>(f,when,(f,guicontroller,general,is-web,),menu,rec)</xsl:text> 
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>rec</xsl:text>
        </xsl:otherwise>      
      </xsl:choose>
      </xsl:attribute>    -->      
    
      <xsl:if test="s2/@see_keys=1">
        <xsl:attribute name="key">
          <xsl:text>#</xsl:text><xsl:value-of select="//s2/@view_loop_prefix_attribute" /><xsl:value-of select="s1/@id_attribute_name" /><xsl:text>)</xsl:text>
        </xsl:attribute>    
      </xsl:if>
      
      <!-- view_trailer_span  --> 
      <xsl:call-template name="view_trailer_span"/>   
     
      <!-- view_edit plus view_edit_very_first  
      jessas: edit als action! 
      <xsl:if test="s1/@view_edit=1">    
        <xsl:if test="s1/@view_edit_very_first=1">
          <xsl:call-template name="add_edit">
            <xsl:with-param name="wants_edit">v</xsl:with-param>
            <xsl:with-param name="flag_plus"> block </xsl:with-param>
          </xsl:call-template>  
        </xsl:if>      
      </xsl:if>           -->
      
      <!-- todo: actons_view_first -->
      
      <xsl:if test="count (//conf/actions/action[@view_first='1']) > 0">         
      
        <xsl:choose>
          <xsl:when test="s2/@actions_view_first_style='rec'">  
           
            <!-- sortieren nach @sort_view oder sort
            anpassen:
              @view -> view_first, view_prolog, view_last 
              A1 anfang -->
            <xsl:choose>
              <xsl:when test="count(//conf/actions/action[@view_first='1'][string-length(@sort_view)]) > 0">
                <xsl:apply-templates select="//conf/actions/action[@view_first='1']" mode="ti_call_view">
                  <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                  <xsl:sort select="@sort_view"/>                      
                </xsl:apply-templates>                    
              </xsl:when>
              <xsl:otherwise>
                <xsl:apply-templates select="//conf/actions/action[@view_first='1']" mode="ti_call_view">
                  <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                  <xsl:sort select="@sort"/>                      
                </xsl:apply-templates>                
              </xsl:otherwise>
            </xsl:choose>          
            <!-- A1 ende -->                         
                                             
           
          
                      
          </xsl:when>
          <xsl:when test="s2/@actions_view_first_style='menu'">
            <ti type="menu">
              <!-- womoeglich span -->
              <xsl:if test="s2/@actions_view_first_span>1">             
                <xsl:attribute name="span">
                  <xsl:value-of select="s2/@actions_view_first_span" />
                </xsl:attribute>                          
              </xsl:if>
              
              <!-- kopie von A1 -->   
 
               <xsl:choose>
                  <xsl:when test="count(//conf/actions/action[@view_first='1'][string-length(@sort_view)]) > 0">
                    <xsl:apply-templates select="//conf/actions/action[@view_first='1']" mode="ti_call_view">
                      <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                      <xsl:sort select="@sort_view"/>                      
                    </xsl:apply-templates>                    
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:apply-templates select="//conf/actions/action[@view_first='1']" mode="ti_call_view">
                      <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                      <xsl:sort select="@sort"/>                      
                    </xsl:apply-templates>                
                  </xsl:otherwise>
                </xsl:choose>                   
            </ti>                        
          </xsl:when>
        </xsl:choose>                
      
      </xsl:if>      
             
      <!-- name -->
      <xsl:if test="not ( s1/@view_no_main='1')">
      <ti>
        <xsl:attribute name="flag">      
          <xsl:if test="string-length(s1/@view_label_flag) > 0">  
            <xsl:value-of select="s1/@view_label_flag" />                         
          </xsl:if>
          <!-- <xsl:text> button </xsl:text> -->
        </xsl:attribute>                      
        <xsl:if test="s1/@view_links_right=1">
          <xsl:attribute name="align">
            <xsl:text>right</xsl:text>
          </xsl:attribute>           
        </xsl:if>
        <xsl:attribute name="type">
          <xsl:value-of select="s2/@ti_call" />
        </xsl:attribute>       
        
        <xsl:choose>
          <xsl:when test="not (s1/@view_links_right=1)">
            <xsl:attribute name="prefix"><xsl:text>  </xsl:text></xsl:attribute>           
          </xsl:when>
          <xsl:otherwise>
            <xsl:attribute name="postfix"><xsl:text>:</xsl:text></xsl:attribute>
          </xsl:otherwise>
        </xsl:choose>
                          
        <xsl:attribute name="explain">
          <xsl:value-of select="//fields/@name_expression_loop" />
          <xsl:if test="string-length(s1/@see_key_name) > 0">
            <xsl:text> </xsl:text>
            <xsl:value-of select="substring(s1/@see_key_name,1,1)" />
            <xsl:value-of select="s2/@loop_i_row" />              
            <xsl:value-of select="substring(s1/@see_key_name,2,1)" />           
          </xsl:if>
        </xsl:attribute>           
        
        <xsl:if test="string-length(s1/@view_name_span) > 0">
          <xsl:attribute name="span">      
            <xsl:value-of select="s1/@view_name_span" />                    
          </xsl:attribute>
        </xsl:if>          
        
        <xsl:attribute name="job">
          <xsl:value-of select="s2/@job_city_details" />
        </xsl:attribute>          
          
        <prop>
          <xsl:attribute name="id">
            <xsl:value-of select="s2/@key" /> 
          </xsl:attribute>          
          <xsl:value-of select="s2/@loop_i_row" />
        </prop>
        <xsl:call-template name="introduction"/>
      </ti>
      </xsl:if>
      
      <!-- todo: actions_view_prolog  -->
      
      
      <xsl:if test="count (//conf/actions/action[@view_prolog='1']) > 0">         
      
        <xsl:choose>
          <xsl:when test="s2/@actions_view_prolog_style='rec'">  
           
            <!-- sortieren nach @sort_view oder sort
            anpassen:
              @view -> view_prolog, view_prolog, view_last 
              A1 anfang -->
            <xsl:choose>
              <xsl:when test="count(//conf/actions/action[@view_prolog='1'][string-length(@sort_view)]) > 0">
                <xsl:apply-templates select="//conf/actions/action[@view_prolog='1']" mode="ti_call_view">
                  <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                  <xsl:sort select="@sort_view"/>                      
                </xsl:apply-templates>                    
              </xsl:when>
              <xsl:otherwise>
                <xsl:apply-templates select="//conf/actions/action[@view_prolog='1']" mode="ti_call_view">
                  <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                  <xsl:sort select="@sort"/>                      
                </xsl:apply-templates>                
              </xsl:otherwise>
            </xsl:choose>          
            <!-- A1 ende -->                         
                                             
           
          
                      
          </xsl:when>
          <xsl:when test="s2/@actions_view_prolog_style='menu'">
            <ti type="menu">
              <!-- womoeglich span -->
              <xsl:if test="s2/@actions_view_prolog_span>1">             
                <xsl:attribute name="span">
                  <xsl:value-of select="s2/@actions_view_prolog_span" />
                </xsl:attribute>                          
              </xsl:if>
              
              <!-- kopie von A1 -->   
 
               <xsl:choose>
                  <xsl:when test="count(//conf/actions/action[@view_prolog='1'][string-length(@sort_view)]) > 0">
                    <xsl:apply-templates select="//conf/actions/action[@view_prolog='1']" mode="ti_call_view">
                      <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                      <xsl:sort select="@sort_view"/>                      
                    </xsl:apply-templates>                    
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:apply-templates select="//conf/actions/action[@view_prolog='1']" mode="ti_call_view">
                      <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                      <xsl:sort select="@sort"/>                      
                    </xsl:apply-templates>                
                  </xsl:otherwise>
                </xsl:choose>                   
            </ti>                        
          </xsl:when>
        </xsl:choose>                
      
      </xsl:if>      
            
     
      <!--
       jessas: edit button zu actions
       edit button bei view_edit_first, view_edit  
      <xsl:if test="s1/@view_edit=1">    
        <xsl:if test="s1/@view_edit_first=1">
          <xsl:call-template name="add_edit">
            <xsl:with-param name="wants_edit">v</xsl:with-param>
            <xsl:with-param name="flag_plus"></xsl:with-param>
          </xsl:call-template>  
        </xsl:if>      
      </xsl:if>          -->
      
      <!-- felder -->
      <xsl:choose>
        <xsl:when test="count (fields/field[string-length(@view_position) > 0])">
          <xsl:apply-templates select="fields/field" mode="data">    
            <xsl:sort select="@view_position"/>
          </xsl:apply-templates>                   
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="fields/field" mode="data">    
            <xsl:sort select="@position"/>
          </xsl:apply-templates>              
        </xsl:otherwise>
      </xsl:choose>      
      
      <!-- actions: view -->       
    
      
      <!-- eingemachte -->
      <xsl:if test="count (//conf/actions/action[@view='1']) > 0">         
      
        <xsl:choose>
          <xsl:when test="s2/@actions_view_style='rec'">  
           
            <!-- sortieren nach @sort_view oder sort
            anpassen:
              @view -> view_first, view_prolog, view_last 
              A1 anfang -->
            <xsl:choose>
              <xsl:when test="count(//conf/actions/action[@view='1'][string-length(@sort_view)]) > 0">
                <xsl:apply-templates select="//conf/actions/action[@view='1']" mode="ti_call_view">
                  <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                  <xsl:sort select="@sort_view"/>                      
                </xsl:apply-templates>                    
              </xsl:when>
              <xsl:otherwise>
                <xsl:apply-templates select="//conf/actions/action[@view='1']" mode="ti_call_view">
                  <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                  <xsl:sort select="@sort"/>                      
                </xsl:apply-templates>                
              </xsl:otherwise>
            </xsl:choose>          
            <!-- A1 ende -->                         
                                             
           
          
                      
          </xsl:when>
          <xsl:when test="s2/@actions_view_style='menu'">
            <ti type="menu">
              <!-- womoeglich span -->
              <xsl:if test="s2/@actions_view_span>1">             
                <xsl:attribute name="span">
                  <xsl:value-of select="s2/@actions_view_span" />
                </xsl:attribute>                          
              </xsl:if>
              
              <!-- kopie von A1 -->   
             <xsl:choose>
              <xsl:when test="count(//conf/actions/action[@view='1'][string-length(@sort_view)]) > 0">
                <xsl:apply-templates select="//conf/actions/action[@view='1']" mode="ti_call_view">
                  <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                  <xsl:sort select="@sort_view"/>                      
                </xsl:apply-templates>                    
              </xsl:when>
              <xsl:otherwise>
                <xsl:apply-templates select="//conf/actions/action[@view='1']" mode="ti_call_view">
                  <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                  <xsl:sort select="@sort"/>                      
                </xsl:apply-templates>                
              </xsl:otherwise>
            </xsl:choose>    
            
            </ti>                        
          </xsl:when>
        </xsl:choose>                
      
      </xsl:if>
      
      <!-- desc -->                                
      <xsl:if test="count (fields/field[@desc_flag=1]) > 0">
        <ti>
          <xsl:if test="(not(s1/@oldstyle=1))">
            <xsl:attribute name="flag">
              <xsl:text>(f,when,(f,guicontroller,general,is-web,),wrap,)</xsl:text>
            </xsl:attribute>    
          </xsl:if>           
          <xsl:value-of select="//fields/@desc_expression_loop" />
        </ti>      
      </xsl:if>      
      
    
      <!-- actions view_last -->
      <xsl:if test="count (//conf/actions/action[@view_last='1']) > 0">         
      
        <xsl:choose>
          <xsl:when test="s2/@actions_view_last_style='rec'">  
           
            <!-- sortieren nach @sort_view oder sort
            anpassen:
              @view -> view_last, view_last, view_last 
              A1 anfang -->
            <xsl:choose>
              <xsl:when test="count(//conf/actions/action[@view_last='1'][string-length(@sort_view)]) > 0">
                <xsl:apply-templates select="//conf/actions/action[@view_last='1']" mode="ti_call_view">
                  <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                  <xsl:sort select="@sort_view"/>                      
                </xsl:apply-templates>                    
              </xsl:when>
              <xsl:otherwise>
                <xsl:apply-templates select="//conf/actions/action[@view_last='1']" mode="ti_call_view">
                  <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                  <xsl:sort select="@sort"/>                      
                </xsl:apply-templates>                
              </xsl:otherwise>
            </xsl:choose>          
            <!-- A1 ende -->                         
                                             
           
          
                      
          </xsl:when>
          <xsl:when test="s2/@actions_view_last_style='menu'">
            <ti type="menu">
              <!-- womoeglich span -->
              <xsl:if test="s2/@actions_view_last_span>1">             
                <xsl:attribute name="span">
                  <xsl:value-of select="s2/@actions_view_last_span" />
                </xsl:attribute>                          
              </xsl:if>
              
              <!-- kopie von A1 -->   
 
               <xsl:choose>
                  <xsl:when test="count(//conf/actions/action[@view_last='1'][string-length(@sort_view)]) > 0">
                    <xsl:apply-templates select="//conf/actions/action[@view_last='1']" mode="ti_call_view">
                      <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                      <xsl:sort select="@sort_view"/>                      
                    </xsl:apply-templates>                    
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:apply-templates select="//conf/actions/action[@view_last='1']" mode="ti_call_view">
                      <xsl:with-param name="page"><xsl:text>view</xsl:text></xsl:with-param>
                      <xsl:sort select="@sort"/>                      
                    </xsl:apply-templates>                
                  </xsl:otherwise>
                </xsl:choose>                   
            </ti>                        
          </xsl:when>
        </xsl:choose>                
      
      </xsl:if>            
      <!-- jessas: zu den actions dazu 
      edit-button bei view_edit, jene die nicht auf view_edit_very_first und view_edit_first sind  
      <xsl:if test="s1/@view_edit=1">
        <xsl:if test="not ( s1/@view_edit_first=1 ) and not ( s1/@view_edit_very_first=1 )">
          <xsl:call-template name="add_edit">
            <xsl:with-param name="wants_edit">v</xsl:with-param>
            <xsl:with-param name="flag_plus"></xsl:with-param>
          </xsl:call-template> 
        </xsl:if>        
      </xsl:if>                      --> 
              
      <!-- delete button             
      <xsl:if test="s1/@view_delete=1">
        <xsl:call-template name="add_delete"/>
      </xsl:if>              -->
      
       
    </ti>  
    
    <xsl:call-template name="hook2">
      <xsl:with-param name="page">view</xsl:with-param>
      <xsl:with-param name="hook">2</xsl:with-param>
      <xsl:with-param name="station">1</xsl:with-param>
    </xsl:call-template> 
               
  </loop>

  <xsl:call-template name="hook2">
    <xsl:with-param name="page">view</xsl:with-param>
    <xsl:with-param name="hook">3</xsl:with-param>
    <xsl:with-param name="station">0</xsl:with-param>
  </xsl:call-template>
      
  <when test="(prop,nothing)">
  
    <xsl:choose>
      <xsl:when test="string-length(s1/@nodata_goes_right) > 0">
        <ti type="rec">
          <ti>
            <xsl:attribute name="span">
              <xsl:text><xsl:value-of select="s1/@nodata_goes_right" /></xsl:text>
            </xsl:attribute><xsl:text> </xsl:text> 
          </ti>
          <ti flag="wrap_word"><xsl:value-of select="s2/@label_nodata" /></ti>
        </ti>                        
      </xsl:when>
      <xsl:otherwise>
        <ti type="rec">
          <ti flag="wrap_word"><xsl:value-of select="s2/@label_nodata" /></ti>
        </ti>           
      </xsl:otherwise>
    </xsl:choose>  
              
  <xsl:call-template name="hook2">
    <xsl:with-param name="page">view</xsl:with-param>
    <xsl:with-param name="hook">4</xsl:with-param>
    <xsl:with-param name="station">0</xsl:with-param>
  </xsl:call-template>
  </when>

  <xsl:call-template name="hook2">
    <xsl:with-param name="page">view</xsl:with-param>
    <xsl:with-param name="hook">5</xsl:with-param>
    <xsl:with-param name="station">0</xsl:with-param>
  </xsl:call-template>
    
  <xsl:if test="s1/@view_add=1 and s1/@view_add_location='bottom'">
    <xsl:call-template name="add_button"/>
  </xsl:if>  

  <xsl:call-template name="hook2">
    <xsl:with-param name="page">view</xsl:with-param>
    <xsl:with-param name="hook">6</xsl:with-param>
    <xsl:with-param name="station">0</xsl:with-param>
  </xsl:call-template>
      
  <!--   <ti label="Data" padding_top="6px" size="2">
    <ti label="File:" type="file">(file,cities)</ti>
    <ti label="Xml-Offset:" test="(f,gt,(f,len,(prop,cities_offset)),0)">(prop,cities_offset)</ti>
  </ti>  -->

  <when test="(f,has_flag,debug trace all,(f,interpreter,level))">
    <!--
    <when test="(f,role,dev)"> 
    <xsl:if test="not(s1/@no_break_file=1)">
      <ti_break/>
    </xsl:if> 
    -->   
         
      <xsl:call-template name="hook2">
        <xsl:with-param name="page">view</xsl:with-param>
        <xsl:with-param name="hook">8</xsl:with-param>
        <xsl:with-param name="station">0</xsl:with-param>
      </xsl:call-template>
        <ti_hr/>
        <ti label="Debug Information">
        <ti label="File:" type="file"><xsl:value-of select="s2/@file_cities" /></ti>

        <ti label="Xml-Offset:">
          <xsl:attribute name="test">
            <xsl:text>(f,gt_num,(f,len,</xsl:text>
            <xsl:value-of select="s2/@prop_cities_offset" />
            <xsl:text>),0)</xsl:text>
          </xsl:attribute>     
          <xsl:value-of select="s2/@prop_cities_offset" />    
        </ti>
        </ti>



  </when>
  
  <xsl:call-template name="hook2">
    <xsl:with-param name="page">view</xsl:with-param>
    <xsl:with-param name="hook">end</xsl:with-param>
    <xsl:with-param name="station">0</xsl:with-param>
  </xsl:call-template>     

  
</job>
</xsl:template>


<!-- ******************************************* -->
<xsl:template match="field" mode="data_prep">
   <xsl:if test="@view_flag=1">
   
    <xsl:if test="@type='dir' or @type='file'"> 
      <xsl:choose>
        <xsl:when test="@type='file'">
          <file flag="cool" z="a">
            <xsl:attribute name="id">
              <xsl:value-of select="@view_temp_var_name" />
            </xsl:attribute>
            <xsl:attribute name="x">          
              <xsl:value-of select="@field_expression2" />
            </xsl:attribute>            
          </file>
        </xsl:when>
        <xsl:when test="@type='dir'">
        
          <dir flag="cool" z="b">
            <xsl:attribute name="id">
              <xsl:value-of select="@view_temp_var_name" />
            </xsl:attribute>
            <xsl:attribute name="x">          
              <xsl:value-of select="@field_expression2" />
            </xsl:attribute>             
          </dir>        
        </xsl:when>  
      </xsl:choose>  
      <unescape>
        <xsl:attribute name="id">
          <xsl:value-of select="@view_temp_var_name" />
        </xsl:attribute>
        <xsl:attribute name="type">
          <xsl:value-of select="@vartype" />
        </xsl:attribute>              
      </unescape>  
      
      <translate>
        <xsl:attribute name="id">
          <xsl:value-of select="@view_temp_var_name" />
        </xsl:attribute>
        <xsl:attribute name="type">
          <xsl:value-of select="@vartype" />
        </xsl:attribute>              
      </translate>         

      
    </xsl:if>  
               
   </xsl:if>
</xsl:template>
<!-- ******************************************* -->
<xsl:template match="field" mode="data">
  <!-- <ti x="(f,when,(f,exxists,(loop,i,@name)),(f,substring,(loop,i,@name),0,30),?)" width="100px" />  
  <ti x="(f,when,(f,exxists,(loop,i,@name)),(loop,i,@name),?)" width="100px" />
  -->
  <xsl:if test="@view_flag=1">
     <ti>
        <!-- test -->
        <xsl:attribute name="test">
          <xsl:text>(f,has,</xsl:text>
              <xsl:value-of select="//s2/@view_loop_prefix_attribute2" />
              <xsl:value-of select="@id" />
              <xsl:text>)</xsl:text>
          <xsl:text>)</xsl:text>
        </xsl:attribute>
        
        <xsl:if test="string-length(@view_span) > 0 ">
          <xsl:attribute name="span">
            <xsl:value-of select="@view_span" />
          </xsl:attribute>          
        </xsl:if>           
        
        <!-- type -->       
        <xsl:attribute name="type">  <!-- 14.4 -->
          <xsl:choose>
            <xsl:when test="@type='bool'">
              <xsl:text>text</xsl:text>
            </xsl:when>
            <xsl:when test="@type='sel'">
              <xsl:text>text</xsl:text>
            </xsl:when>            
            <xsl:otherwise>
              <xsl:value-of select="@type" />
            </xsl:otherwise>
          </xsl:choose>                
        </xsl:attribute>
        
        <xsl:attribute name="x">
          <xsl:choose>
            <xsl:when test="@type='dir' or @type='file'"> 
              <xsl:text>(</xsl:text>
              <xsl:value-of select="@vartype" />
              <xsl:text>,</xsl:text>
              <xsl:value-of select="@view_temp_var_name" />   
              <xsl:text>)</xsl:text>        
            </xsl:when>    
            <xsl:otherwise>
                <xsl:value-of select="@field_expression" />
            </xsl:otherwise>    
           </xsl:choose>      
          
          
          <!-- 
          <xsl:choose>
            <xsl:when test="string-length(@view_len)>0 and (@type='text' or @type='textblock')">
              <xsl:text>(f,when,(loop,i,exists?,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>),(f,substring,(loop,i,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>),0,</xsl:text>
              <xsl:value-of select="@view_len" />
              <xsl:text>),?)</xsl:text>
            </xsl:when>
            <xsl:when test="string-length(@number_format)>0">
              <xsl:text>(f,when,(loop,i,exists?,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>),(f,format,(loop,i,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>),</xsl:text>
              <xsl:value-of select="@number_format" />
              <xsl:text>),?)</xsl:text>
            </xsl:when>       
            
            <xsl:when test="@type='bool'"> 
              <xsl:text>(f,when,(loop,i,exists?,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>),(f,when,(f,try,()(loop,i,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>)(),0,0),Yes,No),?)</xsl:text>          
            </xsl:when>              

                      

            <xsl:otherwise>  
              <xsl:text>(loop,i,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>)</xsl:text>                     
            </xsl:otherwise>             
          </xsl:choose>
          -->
        </xsl:attribute>

        <xsl:if test="string-length (@align) > 0 ">
          <xsl:attribute name="align">
            <xsl:value-of select="@align" />
          </xsl:attribute>          
        </xsl:if>    
        <xsl:if test="(@type='dir' or @type='file') and string-length(@view_len)>0">
          <xsl:attribute name="len">
            <xsl:value-of select="@view_len" />
          </xsl:attribute>          
        </xsl:if>  
        <xsl:if test="string-length(@flag_view)>0">
        <xsl:attribute name="flag">
          <!-- 
          <xsl:if test="(@type='dir' or @type='file')">
            <xsl:text>translate</xsl:text>
          </xsl:if>
          -->
          
            <xsl:value-of select="@flag_view" />
          
        </xsl:attribute>
        </xsl:if>
                  
        <!-- bis 3.4.2020
        <xsl:if test="(@type='dir' or @type='file')">
          <xsl:attribute name="flag">
            <xsl:text>translate</xsl:text>
          </xsl:attribute>          
        </xsl:if>      
        -->               
      </ti>
     
     <!-- wenn es das nicht gibt --> 
     <ti>
        <xsl:attribute name="test">
          <xsl:text>(f,not,(f,has,</xsl:text>
              <xsl:value-of select="//s2/@view_loop_prefix_attribute2" />
              <xsl:value-of select="@id" />
              <xsl:text>)</xsl:text>
          <xsl:text>))</xsl:text>
        </xsl:attribute>
     </ti>      
            
  </xsl:if>
</xsl:template>

<!-- ******************************************* -->
<xsl:template match="field" mode="header">
  <!-- loop <ti x="(f,html,bold,open)Name(f,html,bold,close)" width="100px" />  -->
  <xsl:if test="@view_flag=1">
     <ti>
        <xsl:attribute name="x">
          <xsl:choose>
            <xsl:when test="string-length(@view_label)>0">
              <xsl:value-of select="@view_label" />
            </xsl:when>
            <xsl:otherwise>
              <xsl:value-of select="@label" />
            </xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
        <xsl:if test="string-length(@align) > 0 ">
          <xsl:attribute name="align">
            <xsl:value-of select="@align" />
          </xsl:attribute>          
        </xsl:if>  
        <xsl:if test="string-length(@view_span) > 0 ">
          <xsl:attribute name="span">
            <xsl:value-of select="@view_span" />
          </xsl:attribute>          
        </xsl:if>               
      </ti>      
  </xsl:if>
</xsl:template>

<xsl:template name="add_delete" >
      <ti>
          <xsl:if test="string-length(s1/@test_delete)>0">
            <xsl:attribute name="test">
              <xsl:value-of select="s1/@test_delete" />
            </xsl:attribute>           
          </xsl:if>   
                
        <xsl:attribute name="type">
          <xsl:value-of select="s2/@ti_call" />
        </xsl:attribute>          
        <xsl:if test="not ( s1/@button_rule='off' )">
                    <xsl:attribute name="flag"><xsl:text>button</xsl:text></xsl:attribute> 
                    <xsl:attribute name="class"><xsl:text>fx fxDelete fxDeleteView fxDeleteView</xsl:text><xsl:value-of select="s2/@firstcase_singular_id" /></xsl:attribute> 
        </xsl:if>  
 
                    
        
        <xsl:attribute name="explain">
          <xsl:value-of select="s2/@label_delete" />
        </xsl:attribute>   
        <xsl:attribute name="job">
          <xsl:value-of select="s2/@job_city_delete" />
        </xsl:attribute>            
        <prop>
          <xsl:attribute name="id">
            <xsl:value-of select="s2/@key" /> 
          </xsl:attribute>          
          <xsl:value-of select="s2/@loop_i_row" />
        </prop>
        <xsl:call-template name="introduction"/>
      </ti>
</xsl:template>

<xsl:template name="add_edit" >
<xsl:param name="wants_edit"/>
<xsl:param name="flag_plus"/>      <!-- v=view d=details -->
      <ti>    
          
          <xsl:choose>   
            <xsl:when test="string-length(s1/@test_edit_view)>0">
              <xsl:attribute name="test">
                <xsl:value-of select="s1/@test_edit_view" />
              </xsl:attribute>           
            </xsl:when>  
                           
            <xsl:when test="string-length(s1/@test_edit)>0">
              <xsl:attribute name="test">
                <xsl:value-of select="s1/@test_edit" />
              </xsl:attribute>           
            </xsl:when>  
          
          </xsl:choose>
                        
        <xsl:attribute name="type">
          <xsl:value-of select="s2/@ti_call" />
        </xsl:attribute>          
        <xsl:attribute name="flag">        
        <xsl:if test="not ( s1/@button_rule='off' )"><xsl:text>button</xsl:text></xsl:if><xsl:value-of select="$flag_plus" /></xsl:attribute> 

     
           
        <xsl:choose>                
          <xsl:when test="$wants_edit='v'">
            <xsl:attribute name="explain">
              <xsl:value-of select="s2/@label_edit" />
            </xsl:attribute>  
            <xsl:attribute name="class"><xsl:text>fx fxEdit fxEditView fxEditView</xsl:text><xsl:value-of select="s2/@firstcase_singular_id" /></xsl:attribute>                     
          
          </xsl:when>
          
          <xsl:otherwise>
            <xsl:attribute name="explain">
              <xsl:text>[ </xsl:text>
              <xsl:value-of select="s2/@label_edit" />
              <xsl:text> ]</xsl:text>
            </xsl:attribute>      
            <xsl:attribute name="class"><xsl:text>fx fxEdit fxEditDetail fxEditDetail</xsl:text><xsl:value-of select="s2/@firstcase_singular_id" /></xsl:attribute>                     
                  
          </xsl:otherwise>
        
        </xsl:choose>
        
 
        <xsl:attribute name="job">
          <xsl:value-of select="s2/@job_city_edit" />
        </xsl:attribute>            
        <prop>
          <xsl:attribute name="id">
            <xsl:value-of select="s2/@key" /> 
          </xsl:attribute>          
          <xsl:value-of select="s2/@loop_i_row" />
        </prop>
        <xsl:call-template name="introduction"/>
      </ti>
</xsl:template>

<xsl:template name="add_button" >                
  <when>
      <xsl:attribute name="test">
        <xsl:text>(f,not,(f,contains,</xsl:text>
        <xsl:value-of select="s2/@prop_cities_offset" />
        <xsl:text>,node(e:o)(e:c)))</xsl:text>   
      </xsl:attribute>
      <xsl:if test="s1/@view_add_padding_top = '1'">
        <ti><ti/></ti>
      </xsl:if>
      <ti type="rec" >
          <xsl:if test="not ( s1/@add_left='1')"> 
             <xsl:attribute name="label"><xsl:text> </xsl:text></xsl:attribute>
          </xsl:if>               
      
          <xsl:if test="string-length(s1/@test_insert)>0 and string-length(s1/@test_orga)>0">
            <xsl:attribute name="test">
              <xsl:text>(f,and,</xsl:text>
              <xsl:value-of select="s1/@test_insert" />
              <xsl:text>,</xsl:text>
              <xsl:value-of select="s1/@test_orga" />
              <xsl:text>)</xsl:text>
            </xsl:attribute>           
          </xsl:if>   
      <ti type="menu" span="10">          
      <ti >
      
          <xsl:if test="string-length(s1/@test_insert)>0">
            <xsl:attribute name="test">
              <xsl:value-of select="s1/@test_insert" />
            </xsl:attribute>           
          </xsl:if>   
                  
          <xsl:if test="s1/@view_add_location='bottom'">
            <xsl:attribute name="label">
              <xsl:text> </xsl:text>
            </xsl:attribute> 
          </xsl:if>
          
          <xsl:attribute name="type">
            <xsl:value-of select="s2/@ti_call" />
          </xsl:attribute>      
          <xsl:attribute name="explain_nix">
            <xsl:value-of select="s2/@job_city_add" />
          </xsl:attribute>   
          
          <xsl:if test="not ( s1/@button_rule='off' )">
                     <xsl:attribute name="flag">x<xsl:text>button </xsl:text>
                    <xsl:if test="string-length(s1/@view_add_flag) > 0">  
                      <xsl:value-of select="s1/@view_add_flag" />                         
                    </xsl:if>
                    </xsl:attribute> 
                    <xsl:attribute name="class"><xsl:text>fb fbAdd fbAdd</xsl:text><xsl:value-of select="s2/@firstcase_singular_id" /></xsl:attribute>  
</xsl:if> 
   
          <xsl:attribute name="explain">
            <xsl:value-of select="s1/@label_add" />
          </xsl:attribute>      
          <xsl:attribute name="job">
            <xsl:value-of select="s2/@job_city_add" />
          </xsl:attribute>     
          <xsl:call-template name="introduction"/>
    
      </ti> 
      
      <ti >
          <xsl:attribute name="test">
            <xsl:choose>
              <xsl:when test="not(string-length(s1/@test_orga) = 0)">
                  <xsl:text>(f,and,(f,not,(prop,nothing)),</xsl:text>
                  <xsl:value-of select="s1/@test_orga" />
                  <xsl:text>)</xsl:text>                 
              </xsl:when>
              <xsl:otherwise>
                <xsl:text>(f,not,(prop,nothing))</xsl:text>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:attribute>
                  
          <xsl:if test="s1/@view_add_location='bottom'">
            <xsl:attribute name="label">
              <xsl:text> </xsl:text>
            </xsl:attribute> 
          </xsl:if>

          <xsl:attribute name="type">
              <xsl:value-of select="s2/@ti_call" />
            </xsl:attribute>  
          <!--             
          <xsl:attribute name="type">
            <xsl:text>call_job</xsl:text>
          </xsl:attribute>  -->    
          <xsl:if test="not ( s1/@button_rule='off' )">
                      <xsl:attribute name="flag"><xsl:text>button</xsl:text></xsl:attribute> 
                      <xsl:attribute name="class"><xsl:text>fb fbOrg fbOrg</xsl:text><xsl:value-of select="s2/@firstcase_singular_id" /></xsl:attribute>    
          </xsl:if>                  
        
          <xsl:attribute name="explain">
            <xsl:value-of select="s1/@label_org" />
          </xsl:attribute>      
          <xsl:attribute name="job">
            <xsl:text>do-org-me</xsl:text>            
          </xsl:attribute>     
          <xsl:call-template name="introduction2"/>
    
      </ti> 
      </ti>

      </ti>      
  </when>
</xsl:template>

<xsl:template name="view_trailer_span" >
  <!-- jessas: das sollte doch anders überprüft werden, doch nicht, das ist die span-anzahl  -->
  <xsl:if test="string-length(//s1/@view_trailer_span)>0">

    <ti x=" ">
      <xsl:if test="//s1/@view_trailer_span > 1">
      <xsl:attribute name="span">
        <xsl:value-of select="//s1/@view_trailer_span" />
      </xsl:attribute>    
      </xsl:if>    
    </ti>
    
  </xsl:if>
</xsl:template>

</xsl:stylesheet>

  