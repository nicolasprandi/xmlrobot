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

<!-- <job id="City to_memory" role="hidden" >  -->
<job role="hidden">
  <xsl:attribute name="id"><xsl:value-of select="s2/@job_city_tofile" /></xsl:attribute>  
  
                <xsl:call-template name="hook2">
                  <xsl:with-param name="page">tofile</xsl:with-param>
                  <xsl:with-param name="hook">begin</xsl:with-param>
                  <xsl:with-param name="station">0</xsl:with-param>
                </xsl:call-template>       
  <need><xsl:value-of select="s2/@prop_key" /> (prop,isAdd)</need>     
  <need default="./"><xsl:value-of select="s2/@prop_cities_offset" /></need> 
                <xsl:call-template name="hook2">
                  <xsl:with-param name="page">tofile</xsl:with-param>
                  <xsl:with-param name="hook">0</xsl:with-param>
                  <xsl:with-param name="station">0</xsl:with-param>
                </xsl:call-template>    
  <spirit act="open">
      <xsl:attribute name="id">
        <xsl:value-of select="s2/@spirit" />
      </xsl:attribute>
      <xsl:attribute name="xml">
        <xsl:value-of select="s2/@file_cities" />
      </xsl:attribute>      
  </spirit>   
  <when test="(prop,isAdd)">
               <xsl:call-template name="hook2">
                <xsl:with-param name="page">tofile</xsl:with-param>
                <xsl:with-param name="hook">1</xsl:with-param>
                <xsl:with-param name="station">0</xsl:with-param>
              </xsl:call-template>        
    <!--   
    <choose>
      <when test="(f,ends_with,(prop,cities_offset),/)">
        <prop id="offset2">(f,substring,(prop,cities_offset),0,(f,sub,(f,len,(prop,cities_offset)),1))</prop>
      </when>
      <otherwise>
        <prop id="offset2">(prop,cities_offset)</prop>
      </otherwise>
    </choose>  -->
    
    <choose>
      <when>
        <xsl:attribute name="test">           
          <xsl:text>(f,ends_with,</xsl:text>
          <xsl:value-of select="//s2/@prop_cities_offset" />
          <xsl:text>,/)</xsl:text>         
        </xsl:attribute>
              <xsl:call-template name="hook2">
                <xsl:with-param name="page">tofile</xsl:with-param>
                <xsl:with-param name="hook">2</xsl:with-param>
                <xsl:with-param name="station">0</xsl:with-param>
              </xsl:call-template>               
        <prop id="offset2">
          <xsl:attribute name="x"> 
            <xsl:text>(f,substring,</xsl:text>
            <xsl:value-of select="//s2/@prop_cities_offset" />
            <xsl:text>,0,(f,sub,(f,len,</xsl:text>
            <xsl:value-of select="//s2/@prop_cities_offset" />
            <xsl:text>),1))</xsl:text>
          </xsl:attribute>
        </prop>
              <xsl:call-template name="hook2">
                <xsl:with-param name="page">tofile</xsl:with-param>
                <xsl:with-param name="hook">3</xsl:with-param>
                <xsl:with-param name="station">0</xsl:with-param>
              </xsl:call-template>               
      </when>
      <otherwise>
                <xsl:call-template name="hook2">
                  <xsl:with-param name="page">tofile</xsl:with-param>
                  <xsl:with-param name="hook">4</xsl:with-param>
                  <xsl:with-param name="station">0</xsl:with-param>
                </xsl:call-template>             
        <prop id="offset2">
          <xsl:attribute name="x"> 
            <xsl:value-of select="//s2/@prop_cities_offset" />
          </xsl:attribute>
        </prop>        
                <xsl:call-template name="hook2">
                  <xsl:with-param name="page">tofile</xsl:with-param>
                  <xsl:with-param name="hook">5</xsl:with-param>
                  <xsl:with-param name="station">0</xsl:with-param>
                </xsl:call-template>               
      </otherwise>
    </choose>    
                <xsl:call-template name="hook2">
                  <xsl:with-param name="page">tofile</xsl:with-param>
                  <xsl:with-param name="hook">6</xsl:with-param>
                  <xsl:with-param name="station">0</xsl:with-param>
                </xsl:call-template>           
  
    <!-- neu -->
    <spirit act="xml_update" how="insert_in">
      <xsl:attribute name="id">
        <xsl:value-of select="//s2/@spirit" />
      </xsl:attribute> 
      <xsl:attribute name="where"> 
        <xsl:text>(prop,offset2)</xsl:text>
      </xsl:attribute>
      <inline attribute="with"> 
        <xsl:text>&lt;</xsl:text>
        <xsl:value-of select="//s1/@singular_id" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="//s1/@id_attribute_name" />
        <xsl:text>="</xsl:text>
        <xsl:value-of select="//s2/@prop_key" />
        <xsl:text>" /&gt;</xsl:text>  
      </inline>            
    </spirit> 
    <!-- alt 
    <xml_update how="insert_in">
      <xsl:attribute name="what"> 
        <xsl:value-of select="//s2/@file_cities" />
      </xsl:attribute>    
      <xsl:attribute name="where"> 
        <xsl:text>(prop,offset2)</xsl:text>
      </xsl:attribute>
      <inline attribute="with"> 
        <xsl:text>&lt;</xsl:text>
        <xsl:value-of select="//s1/@singular_id" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="//s1/@id_attribute_name" />
        <xsl:text>="</xsl:text>
        <xsl:value-of select="//s2/@prop_key" />
        <xsl:text>" /&gt;</xsl:text>  
      </inline>
    </xml_update>   --> 
                    <xsl:call-template name="hook2">
                      <xsl:with-param name="page">tofile</xsl:with-param>
                      <xsl:with-param name="hook">7</xsl:with-param>
                      <xsl:with-param name="station">0</xsl:with-param>
                    </xsl:call-template>  
                                  
  <xsl:if test="string-length(s1/@dt_add)>0">
                    <xsl:call-template name="hook2">
                      <xsl:with-param name="page">tofile</xsl:with-param>
                      <xsl:with-param name="hook">16</xsl:with-param>
                      <xsl:with-param name="station">0</xsl:with-param>
                    </xsl:call-template>  
    <!-- neu 
    <spirit act="xml_update" how="set_attribute" attribute="dt_add">
      <xsl:attribute name="id">
        <xsl:value-of select="//s2/@spirit" />
      </xsl:attribute> 
      <xsl:attribute name="where"> 
        <xsl:value-of select="s2/@prop_cities_offset" />
        <xsl:value-of select="s1/@singular_id" />
        <xsl:text>[@</xsl:text>
        <xsl:value-of select="s1/@id_attribute_name" />
        <xsl:text>=(f,xpath_string,</xsl:text>
        <xsl:value-of select="s2/@prop_key" />
        <xsl:text>)]</xsl:text>
      </xsl:attribute>        
      <xsl:attribute name="value"> 
        <xsl:value-of select="s1/@dt_add" />
      </xsl:attribute>              
    </spirit> -->    
    <!-- alt     
    <xml_update how="set_attribute" attribute="dt_add">
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
      <xsl:attribute name="attribute_value"> 
        <xsl:value-of select="s1/@dt_add" />
      </xsl:attribute>    
    </xml_update> -->
                        <xsl:call-template name="hook2">
                          <xsl:with-param name="page">tofile</xsl:with-param>
                          <xsl:with-param name="hook">17</xsl:with-param>
                          <xsl:with-param name="station">0</xsl:with-param>
                        </xsl:call-template>       
  </xsl:if>                                           
    <i>Added a new record</i>
                        <xsl:call-template name="hook2">
                          <xsl:with-param name="page">tofile</xsl:with-param>
                          <xsl:with-param name="hook">8</xsl:with-param>
                          <xsl:with-param name="station">0</xsl:with-param>
                        </xsl:call-template>           
  </when>
                        <xsl:call-template name="hook2">
                          <xsl:with-param name="page">tofile</xsl:with-param>
                          <xsl:with-param name="hook">9</xsl:with-param>
                          <xsl:with-param name="station">0</xsl:with-param>
                        </xsl:call-template>       
  <!--
    <xml_update 
    how="set_attribute" 
    what="(file,cities)" 
    where="(prop,cities_offset)city[@row='(prop,rec_id)']"
    attribute="dt_upd"
    value="(f,time)" />
  -->      
  <xsl:if test="string-length(s1/@dt_upd)>0">
                        <xsl:call-template name="hook2">
                          <xsl:with-param name="page">tofile</xsl:with-param>
                          <xsl:with-param name="hook">10</xsl:with-param>
                          <xsl:with-param name="station">0</xsl:with-param>
                        </xsl:call-template> 
    <!-- neu 
    <spirit act="xml_update" how="set_attribute" attribute="dt_upd">
      <xsl:attribute name="id">
        <xsl:value-of select="//s2/@spirit" />
      </xsl:attribute> 
      <xsl:attribute name="where"> 
        <xsl:value-of select="s2/@prop_cities_offset" />
        <xsl:value-of select="s1/@singular_id" />
        <xsl:text>[@</xsl:text>
        <xsl:value-of select="s1/@id_attribute_name" />
        <xsl:text>=(f,xpath_string,</xsl:text>
        <xsl:value-of select="s2/@prop_key" />
        <xsl:text>)]</xsl:text>
      </xsl:attribute>        
      <xsl:attribute name="value"> 
        <xsl:value-of select="s1/@dt_upd" />
      </xsl:attribute>              
    </spirit> -->     
    <!-- alt       
    <xml_update how="set_attribute" attribute="dt_upd">
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
      <xsl:attribute name="attribute_value"> 
        <xsl:value-of select="s1/@dt_upd" />
      </xsl:attribute>    
    </xml_update>   -->
                      <xsl:call-template name="hook2">
                        <xsl:with-param name="page">tofile</xsl:with-param>
                        <xsl:with-param name="hook">11</xsl:with-param>
                        <xsl:with-param name="station">0</xsl:with-param>
                      </xsl:call-template>       
  </xsl:if>
                      <xsl:call-template name="hook2">
                        <xsl:with-param name="page">tofile</xsl:with-param>
                        <xsl:with-param name="hook">12</xsl:with-param>
                        <xsl:with-param name="station">0</xsl:with-param>
                      </xsl:call-template>       
  <xsl:apply-templates select="fields/field" mode="tofile_upd"/>
                      <xsl:call-template name="hook2">
                        <xsl:with-param name="page">tofile</xsl:with-param>
                        <xsl:with-param name="hook">18</xsl:with-param>
                        <xsl:with-param name="station">0</xsl:with-param>
                      </xsl:call-template>  
  <spirit act="close" flag="save pretty">
      <xsl:attribute name="id">
        <xsl:value-of select="s2/@spirit" />
      </xsl:attribute>   
  </spirit>              
                      <xsl:call-template name="hook2">
                        <xsl:with-param name="page">tofile</xsl:with-param>
                        <xsl:with-param name="hook">end</xsl:with-param>
                        <xsl:with-param name="station">0</xsl:with-param>
                      </xsl:call-template>       
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
    
</job>
</xsl:template>

<!-- ******************************************* -->
<xsl:template match="field" mode="tofile_upd">
  <xsl:if test="not( @type = 'msg' )"> 
   <xsl:choose>
      <xsl:when test="string-length(@tk_test)>0">
         <when>
            <xsl:attribute name="test">
               <xsl:text>(f,exists,</xsl:text>              
               <xsl:value-of select="@vartype" />
               <xsl:text>,</xsl:text>
               <xsl:value-of select="//s1/@singular_id" />
               <xsl:text>_</xsl:text>
               <xsl:value-of select="@id" />              
               <xsl:text>)</xsl:text>              
            </xsl:attribute>
            <xsl:call-template name="do_the_update"/>
         </when>
      </xsl:when>
      <xsl:otherwise>
         <xsl:call-template name="do_the_update"/>
      </xsl:otherwise>
      
   </xsl:choose>
   
      </xsl:if>
   
</xsl:template>

<!-- ******************************************* -->
<xsl:template name="do_the_update">
  <!--   
  <xml_update 
    how="set_attribute" 
    what="(file,cities)" 
    where="(prop,cities_offset)city[@row='(prop,rec_id)']"
    attribute="name"
    value="casdfadfity_name))" />
  -->
                        <xsl:call-template name="hook2">
                          <xsl:with-param name="page">tofile</xsl:with-param>
                          <xsl:with-param name="hook">14</xsl:with-param>
                          <xsl:with-param name="station">0</xsl:with-param>
                        </xsl:call-template>  
  <xsl:choose>
    <xsl:when test="@vartype='dir'">


        <dir id="jack-in">     
          <xsl:attribute name="x">
            <xsl:text>(</xsl:text>
            <xsl:value-of select="@vartype" />
            <xsl:text>,</xsl:text>
            <xsl:value-of select="//s1/@singular_id" />
            <xsl:text>_</xsl:text>
            <xsl:value-of select="@id" />
            <xsl:text>)</xsl:text>
          </xsl:attribute>                              
        </dir> 
        
        <xsl:choose>
          <xsl:when test="//s1/@oldstyle='1'">
            <prop id="jack-result-prop" x="anton"/>
            <prop id="jack-result-did-prop" x="berta"/>          
          </xsl:when>
          <xsl:otherwise>
            <prop id="jack-result-prop" x="(f,random_text,5)"/>
            <prop id="jack-result-did-prop" x="(f,random_text,5)"/>          
          </xsl:otherwise>
        </xsl:choose>
              
           

          
      <call_service job="go-jack-do-dir" flag="silent">
      
        <xsl:if test="//s2/@usage='app'">
          <dir id="jack-in" x="(dir,jack-in)"/>
          <prop id="jack-result-prop" x="(prop,jack-result-prop)"/>
          <prop id="jack-result-did-prop" x="(prop,jack-result-did-prop)"/>
        </xsl:if>
              

      </call_service>  
       
      <global act="get" as_id="jack-result" id="(prop,jack-result-prop)"/>
      <!-- neu -->
      <spirit act="xml_update" how="set_attribute" flag="remove">
        <xsl:attribute name="id">
          <xsl:value-of select="//s2/@spirit" />
        </xsl:attribute> 
        <xsl:attribute name="where"> 
          <xsl:value-of select="//s2/@prop_cities_offset" />
          <xsl:value-of select="//s1/@singular_id" />
          <xsl:text>[@</xsl:text>
          <xsl:value-of select="//s1/@id_attribute_name" />
          <xsl:text>=(f,xpath_string,</xsl:text>
          <xsl:value-of select="//s2/@prop_key" />
          <xsl:text>)]</xsl:text>
        </xsl:attribute>        
        <xsl:attribute name="attribute"> 
          <xsl:value-of select="@id" />
        </xsl:attribute>            
        <xsl:attribute name="value"> 
          <xsl:text>(prop,jack-result)</xsl:text>
        </xsl:attribute>                  
      </spirit>         
   
    
    </xsl:when>                 
    
    <xsl:when test="@vartype='file'">
      <!-- <xsl:if test="//s2/@usage='service'">      -->
  
      <!-- </xsl:if>  
      <xsl:choose>
        <xsl:when test="//s1/@oldstyle='1'">
        
          <file id="jack-in">     
            <xsl:attribute name="x">
              <xsl:text>(</xsl:text>
              <xsl:value-of select="@vartype" />
              <xsl:text>,</xsl:text>
              <xsl:value-of select="//s1/@singular_id" />
              <xsl:text>_</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>)</xsl:text>
            </xsl:attribute>                              
          </file>   
          <call_service job="go-jack-do-file" flag="silent">
            <file id="jack-in">(file,jack-in)</file>
          </call_service>
          <global act="get" id="jack-result"/>       
         
        </xsl:when>
        <xsl:otherwise>
        -->
          <file id="jack-in">     
            <xsl:attribute name="x">
              <xsl:text>(</xsl:text>
              <xsl:value-of select="@vartype" />
              <xsl:text>,</xsl:text>
              <xsl:value-of select="//s1/@singular_id" />
              <xsl:text>_</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>)</xsl:text>
            </xsl:attribute>                              
          </file>         
          <prop id="jack-result-prop" x="(f,random_text,5)"/>
          <prop id="jack-result-did-prop" x="(f,random_text,5)"/>      
          <call_service job="go-jack-do-file" flag="silent">
          
            <xsl:if test="//s2/@usage='app'">
              <file id="jack-in" x="(file,jack-in)"/>
              <prop id="jack-result-prop" x="(prop,jack-result-prop)"/>
              <prop id="jack-result-did-prop" x="(prop,jack-result-did-prop)"/>           
            </xsl:if>                 
          </call_service>
             
          <global act="get" as_id="jack-result" id="(prop,jack-result-prop)"/>   
      <!--            
        </xsl:otherwise>
      </xsl:choose>    -->


      

      <!-- neu, das task_friend am 5.2.2016 eingebaut, war vorher nicht da -->
      <spirit act="xml_update" how="set_attribute" flag="remove">
        <xsl:attribute name="id">
          <xsl:value-of select="//s2/@spirit" />
        </xsl:attribute> 
        <xsl:attribute name="where"> 
          <xsl:value-of select="//s2/@prop_cities_offset" />
          <xsl:value-of select="//s1/@singular_id" />
          <xsl:text>[@</xsl:text>
          <xsl:value-of select="//s1/@id_attribute_name" />
          <xsl:text>='</xsl:text>
          <xsl:value-of select="//s2/@prop_key" />
          <xsl:text>']</xsl:text>
        </xsl:attribute> 
        <xsl:attribute name="attribute"> 
          <xsl:value-of select="@id" />
        </xsl:attribute>            
        <xsl:attribute name="value"> 
          <xsl:text>(prop,jack-result)</xsl:text>
        </xsl:attribute>                  
      </spirit>        
     
    
    </xsl:when>    
    <xsl:otherwise>
      <!-- neu -->
      <spirit act="xml_update" how="set_attribute" flag="remove">
        <xsl:attribute name="id">
          <xsl:value-of select="//s2/@spirit" />
        </xsl:attribute> 
        <xsl:attribute name="where"> 
          <xsl:value-of select="//s2/@prop_cities_offset" />
          <xsl:value-of select="//s1/@singular_id" />
          <xsl:text>[@</xsl:text>
          <xsl:value-of select="//s1/@id_attribute_name" />
          <xsl:text>='</xsl:text>
          <xsl:value-of select="//s2/@prop_key" />
          <xsl:text>']</xsl:text>
        </xsl:attribute> 
        <xsl:attribute name="attribute"> 
          <xsl:value-of select="@id" />
        </xsl:attribute>            
        <xsl:attribute name="value"> 
          <xsl:text>(f,vob,</xsl:text>
          <xsl:value-of select="@vartype" />
          <xsl:text>,</xsl:text>
          <xsl:value-of select="//s1/@singular_id" />
          <xsl:text>_</xsl:text>
          <xsl:value-of select="@id" />
          <xsl:text>)</xsl:text>
        </xsl:attribute>                    
      </spirit>        
      <!-- alt    
      <xml_update how="set_attribute">
        <xsl:attribute name="what"> 
          <xsl:value-of select="//s2/@file_cities" />
        </xsl:attribute>    
        <xsl:attribute name="where"> 
          <xsl:value-of select="//s2/@prop_cities_offset" />
          <xsl:value-of select="//s1/@singular_id" />
          <xsl:text>[@</xsl:text>
          <xsl:value-of select="//s1/@id_attribute_name" />
          <xsl:text>='</xsl:text>
          <xsl:value-of select="//s2/@prop_key" />
          <xsl:text>']</xsl:text>
        </xsl:attribute> 
        <xsl:attribute name="attribute_name"> 
          <xsl:value-of select="@id" />
        </xsl:attribute>            
        <xsl:attribute name="attribute_value"> 
          <xsl:text>(</xsl:text>
          <xsl:value-of select="@vartype" />
          <xsl:text>,</xsl:text>
          <xsl:value-of select="//s1/@singular_id" />
          <xsl:text>_</xsl:text>
          <xsl:value-of select="@id" />
          <xsl:text>)</xsl:text>
        </xsl:attribute>    
      </xml_update>        --> 
          
    </xsl:otherwise>
  
  </xsl:choose>
        
              <xsl:call-template name="hook2">
                <xsl:with-param name="page">tofile</xsl:with-param>
                <xsl:with-param name="hook">15</xsl:with-param>
                <xsl:with-param name="station">0</xsl:with-param>
              </xsl:call-template>       
</xsl:template>


</xsl:stylesheet>

  