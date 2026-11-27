<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015 Nicolas Prandstaetter. All rights reserved. 

 -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
<xsl:param name="singular_id"/>   
<xsl:param name="name_separator"/>
<xsl:param name="view_loop_id"/> 

<xsl:output method="xml"/>

<xsl:template match="/">
   <xsl:apply-templates/>
</xsl:template>


<xsl:template match="field" mode="data">
  <field>
    <xsl:copy-of select="@*"/>
    <xsl:attribute name="vartype">
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
     </xsl:attribute>   
    <xsl:attribute name="var">
      <xsl:text>(</xsl:text>
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
      <xsl:value-of select="$singular_id" />
      <xsl:text>_</xsl:text>
      <xsl:value-of select="@id" />
      <xsl:text>)</xsl:text> 
     </xsl:attribute>      
     
    <xsl:attribute name="view_temp_var_name">
      <xsl:value-of select="$singular_id" /><xsl:text>_<xsl:value-of select="@id" /></xsl:text><xsl:text>_temp_var_formix</xsl:text>
    </xsl:attribute>
    
    <xsl:attribute name="field_expression">
        <xsl:call-template name="composer">
          <xsl:with-param name="sel_ref" select="@sel_ref"/>
          <xsl:with-param name="how"><xsl:text>loop</xsl:text></xsl:with-param>
          <xsl:with-param name="bold"><xsl:text>0</xsl:text></xsl:with-param>
          <xsl:with-param name="name_len"><xsl:value-of select="@view_len" /></xsl:with-param>  <!-- updated 3.4.2020 -->
          <xsl:with-param name="type"><xsl:value-of select="@type" /></xsl:with-param>
          <xsl:with-param name="id"><xsl:value-of select="@id" /></xsl:with-param>
          <xsl:with-param name="pos">1</xsl:with-param>
          <xsl:with-param name="last">1</xsl:with-param>
          <xsl:with-param name="sep"><xsl:value-of select="$name_separator" /></xsl:with-param>
        </xsl:call-template>
    </xsl:attribute>    
    
    <xsl:attribute name="field_expression2">
        <xsl:text>(loop,</xsl:text><xsl:value-of select="$view_loop_id" /><xsl:text>,@</xsl:text>
        <xsl:value-of select="@id" />
        <xsl:text>)</xsl:text>          
    </xsl:attribute>       
              
  </field>
</xsl:template>

<xsl:template match="fields">
  <fields>
    <xsl:copy-of select="@*"/>
    <xsl:attribute name="name_expression">
    
      <xsl:call-template name="composer_begin">
        <xsl:with-param name="cut"><xsl:value-of select="s1/@cut_view" /></xsl:with-param>
        <xsl:with-param name="bold"><xsl:text>1</xsl:text></xsl:with-param>
      </xsl:call-template>      
   
                  
      <xsl:for-each select="field[@name_flag=1]" >
        <!-- <xsl:sort select="@position" data-type="number"/> -->
      
        <xsl:call-template name="composer">
          <xsl:with-param name="sel_ref" select="@sel_ref"/>        
          <xsl:with-param name="how"><xsl:text>detail</xsl:text></xsl:with-param>
          <xsl:with-param name="bold"><xsl:text>1</xsl:text></xsl:with-param>
          <xsl:with-param name="name_len"><xsl:value-of select="@name_len" /></xsl:with-param>
          <xsl:with-param name="type"><xsl:value-of select="@type" /></xsl:with-param>
          <xsl:with-param name="id"><xsl:value-of select="@id" /></xsl:with-param>
          <xsl:with-param name="pos"><xsl:value-of select="position()" /></xsl:with-param>
          <xsl:with-param name="last"><xsl:value-of select="last()" /></xsl:with-param>
          <xsl:with-param name="sep"><xsl:value-of select="$name_separator" /></xsl:with-param>
        </xsl:call-template>

      </xsl:for-each>
      
      <xsl:call-template name="composer_end">
        <xsl:with-param name="cut"><xsl:value-of select="s1/@cut_view" /></xsl:with-param>
        <xsl:with-param name="bold"><xsl:text>1</xsl:text></xsl:with-param>
      </xsl:call-template>     

    </xsl:attribute>
    
    <xsl:attribute name="name_expression_unbold">
    
      <xsl:call-template name="composer_begin">
        <xsl:with-param name="cut"><xsl:value-of select="s1/@cut_view" /></xsl:with-param>
        <xsl:with-param name="bold"><xsl:text>0</xsl:text></xsl:with-param>
      </xsl:call-template>      
   
                  
      <xsl:for-each select="field[@name_flag=1]" >
        <!-- <xsl:sort select="@position" data-type="number"/> -->
      
        <xsl:call-template name="composer">
            <xsl:with-param name="sel_ref" select="@sel_ref"/>
          <xsl:with-param name="how"><xsl:text>detail</xsl:text></xsl:with-param>
          <xsl:with-param name="bold"><xsl:text>0</xsl:text></xsl:with-param>
          <xsl:with-param name="name_len"><xsl:value-of select="@name_len" /></xsl:with-param>
          <xsl:with-param name="type"><xsl:value-of select="@type" /></xsl:with-param>
          <xsl:with-param name="id"><xsl:value-of select="@id" /></xsl:with-param>
          <xsl:with-param name="pos"><xsl:value-of select="position()" /></xsl:with-param>
          <xsl:with-param name="last"><xsl:value-of select="last()" /></xsl:with-param>
          <xsl:with-param name="sep"><xsl:value-of select="$name_separator" /></xsl:with-param>
        </xsl:call-template>

      </xsl:for-each>
      
      <xsl:call-template name="composer_end">
        <xsl:with-param name="cut"><xsl:value-of select="s1/@cut_view" /></xsl:with-param>
        <xsl:with-param name="bold"><xsl:text>0</xsl:text></xsl:with-param>
      </xsl:call-template>     

    </xsl:attribute>    
    <xsl:attribute name="name_expression_loop">

      <xsl:call-template name="composer_begin">
        <xsl:with-param name="cut"><xsl:value-of select="s1/@cut_view" /></xsl:with-param>
        <xsl:with-param name="bold"><xsl:text>1</xsl:text></xsl:with-param>
      </xsl:call-template>      
   
       
      <xsl:for-each select="field[@name_flag=1]" >
        <!-- <xsl:sort select="@position" data-type="number"/> -->
       
        <xsl:call-template name="composer">
          <xsl:with-param name="sel_ref" select="@sel_ref"/>
          <xsl:with-param name="how"><xsl:text>loop</xsl:text></xsl:with-param>
          <xsl:with-param name="bold"><xsl:text>1</xsl:text></xsl:with-param>          
          <xsl:with-param name="name_len"><xsl:value-of select="@name_len" /></xsl:with-param>
          <xsl:with-param name="type"><xsl:value-of select="@type" /></xsl:with-param>
          <xsl:with-param name="id"><xsl:value-of select="@id" /></xsl:with-param>
          <xsl:with-param name="pos"><xsl:value-of select="position()" /></xsl:with-param>
          <xsl:with-param name="last"><xsl:value-of select="last()" /></xsl:with-param>
          <xsl:with-param name="sep"><xsl:value-of select="$name_separator" /></xsl:with-param>
        </xsl:call-template>

      </xsl:for-each>
      
      <xsl:call-template name="composer_end">
        <xsl:with-param name="cut"><xsl:value-of select="s1/@cut_view" /></xsl:with-param>
        <xsl:with-param name="bold"><xsl:text>1</xsl:text></xsl:with-param>
      </xsl:call-template>          
    </xsl:attribute>   
    
    <xsl:attribute name="name_expression_loop_unbold">

      <xsl:call-template name="composer_begin">
        <xsl:with-param name="cut"><xsl:value-of select="s1/@cut_view" /></xsl:with-param>
        <xsl:with-param name="bold"><xsl:text>0</xsl:text></xsl:with-param>
      </xsl:call-template>      
   
       
      <xsl:for-each select="field[@name_flag=1]" >
        <!-- <xsl:sort select="@position" data-type="number"/> -->
       
        <xsl:call-template name="composer">
          <xsl:with-param name="sel_ref" select="@sel_ref"/>
          <xsl:with-param name="how"><xsl:text>loop</xsl:text></xsl:with-param>
          <xsl:with-param name="bold"><xsl:text>0</xsl:text></xsl:with-param>          
          <xsl:with-param name="name_len"><xsl:value-of select="@name_len" /></xsl:with-param>
          <xsl:with-param name="type"><xsl:value-of select="@type" /></xsl:with-param>
          <xsl:with-param name="id"><xsl:value-of select="@id" /></xsl:with-param>
          <xsl:with-param name="pos"><xsl:value-of select="position()" /></xsl:with-param>
          <xsl:with-param name="last"><xsl:value-of select="last()" /></xsl:with-param>
          <xsl:with-param name="sep"><xsl:value-of select="$name_separator" /></xsl:with-param>
        </xsl:call-template>

      </xsl:for-each>
      
      <xsl:call-template name="composer_end">
        <xsl:with-param name="cut"><xsl:value-of select="s1/@cut_view" /></xsl:with-param>
        <xsl:with-param name="bold"><xsl:text>0</xsl:text></xsl:with-param>
      </xsl:call-template>          
    </xsl:attribute>         
    <!-- asdf -->
    <xsl:attribute name="desc_expression">
    
      <xsl:call-template name="composer_begin">
        <xsl:with-param name="cut"><xsl:value-of select="s1/@cut_desc" /></xsl:with-param>
        <xsl:with-param name="bold"><xsl:text>0</xsl:text></xsl:with-param>
      </xsl:call-template>      
   
                  
      <xsl:for-each select="field[@desc_flag=1]" >
        <xsl:sort select="@desc_position" data-type="number"/>
      
        <xsl:call-template name="composer">
          <xsl:with-param name="sel_ref" select="@sel_ref"/>
          <xsl:with-param name="how"><xsl:text>detail</xsl:text></xsl:with-param>
          <xsl:with-param name="bold"><xsl:text>0</xsl:text></xsl:with-param>
          <xsl:with-param name="name_len"><xsl:value-of select="@desc_len" /></xsl:with-param>
          <xsl:with-param name="type"><xsl:value-of select="@type" /></xsl:with-param>
          <xsl:with-param name="id"><xsl:value-of select="@id" /></xsl:with-param>
          <xsl:with-param name="pos"><xsl:value-of select="position()" /></xsl:with-param>
          <xsl:with-param name="last"><xsl:value-of select="last()" /></xsl:with-param>
          <xsl:with-param name="sep"><xsl:value-of select="$name_separator" /></xsl:with-param>
        </xsl:call-template>

      </xsl:for-each>
      
      <xsl:call-template name="composer_end">
        <xsl:with-param name="cut"><xsl:value-of select="s1/@cut_desc" /></xsl:with-param>
        <xsl:with-param name="bold"><xsl:text>0</xsl:text></xsl:with-param>
      </xsl:call-template>     

    </xsl:attribute>
    <xsl:attribute name="desc_expression_loop">
      
      <xsl:call-template name="composer_begin">
        <xsl:with-param name="cut"><xsl:value-of select="s1/@cut_desc" /></xsl:with-param>
        <xsl:with-param name="bold"><xsl:text>0</xsl:text></xsl:with-param>
      </xsl:call-template>      
   
                  
      <xsl:for-each select="field[@desc_flag=1]" >
        <xsl:sort select="@desc_position" data-type="number"/>
      
        <xsl:call-template name="composer">
          <xsl:with-param name="sel_ref" select="@sel_ref"/>
          <xsl:with-param name="how"><xsl:text>loop</xsl:text></xsl:with-param>
          <xsl:with-param name="bold"><xsl:text>0</xsl:text></xsl:with-param>
          <xsl:with-param name="name_len"><xsl:value-of select="@desc_len" /></xsl:with-param>
          <xsl:with-param name="type"><xsl:value-of select="@type" /></xsl:with-param>
          <xsl:with-param name="id"><xsl:value-of select="@id" /></xsl:with-param>
          <xsl:with-param name="pos"><xsl:value-of select="position()" /></xsl:with-param>
          <xsl:with-param name="last"><xsl:value-of select="last()" /></xsl:with-param>
          <xsl:with-param name="sep"><xsl:value-of select="$name_separator" /></xsl:with-param>
        </xsl:call-template>

      </xsl:for-each>
      
      <xsl:call-template name="composer_end">
        <xsl:with-param name="cut"><xsl:value-of select="s1/@cut_desc" /></xsl:with-param>
        <xsl:with-param name="bold"><xsl:text>0</xsl:text></xsl:with-param>
      </xsl:call-template>          
    </xsl:attribute>        
    
    <xsl:apply-templates select="field" mode="data"/>
  </fields>
</xsl:template>

<xsl:template name="composer_begin">
  <xsl:param name="cut" />
  <xsl:param name="bold" />
  
      <xsl:if test="$bold = 1">
        <xsl:text>(f,html,bold,open)</xsl:text>      
      </xsl:if>      
  
      <xsl:if test="string-length($cut)>0">
        <xsl:text>(f,text,cut_nice,</xsl:text>         
      </xsl:if>
           
</xsl:template>  

<xsl:template name="composer_end">
  <xsl:param name="cut" />
  <xsl:param name="bold" />
  
      <xsl:if test="string-length($cut)>0">
        <xsl:text>,</xsl:text>
        <xsl:value-of select="$cut" />
        <xsl:text>)</xsl:text>                
      </xsl:if>
      
      <xsl:if test="$bold = 1">
        <xsl:text>(f,html,bold,close)</xsl:text>      
      </xsl:if> 
</xsl:template> 

<xsl:template name="sel_translator_internal">
	<xsl:param name="sel_ref"/>
  <xsl:param name="id"/>
  <!-- 
  <xsl:text>selref=[</xsl:text>
  <xsl:value-of select="$sel_ref"/>
  <xsl:text>] id=[</xsl:text>
  <xsl:value-of select="$id"/>
  <xsl:text>] </xsl:text>       <xsl:text>;</xsl:text>
  -->
        <xsl:apply-templates select="//sel" >
          <xsl:with-param name="ogi" select="$sel_ref"/>
          <xsl:with-param name="id" select="$id"/>
        </xsl:apply-templates>
      
  
</xsl:template>	 

<xsl:template name="sel_translator_internalxx">
	<xsl:param name="sel_file"/>
  <xsl:param name="id"/>


      
      <xsl:text>(f,tk_sel_translate,(loop,</xsl:text><xsl:value-of select="$view_loop_id" /><xsl:text>,@</xsl:text>
      <xsl:value-of select="$id" />
      <xsl:text>),</xsl:text>
      <xsl:value-of select="$sel_file" />
      <xsl:text>)</xsl:text>

</xsl:template>	 

<xsl:template name="sel_translator_internalxxdetail">
	<xsl:param name="sel_file"/>
  <xsl:param name="id"/>

      
      <xsl:text>(f,tk_sel_translate,(loop,</xsl:text><xsl:value-of select="$view_loop_id" /><xsl:text>,@</xsl:text>
      <xsl:value-of select="$id" />
      <xsl:text>),</xsl:text>
      <xsl:value-of select="$sel_file" />
      <xsl:text>)</xsl:text>

</xsl:template>	

<xsl:template match="sel" >
<xsl:param name="ogi"/>
<xsl:param name="id"/>
<xsl:if test="@ref = $ogi">
      <xsl:text>(f,when,(f,has_flag,(loop,</xsl:text><xsl:value-of select="$view_loop_id" /><xsl:text>,@</xsl:text>
      <xsl:value-of select="$id" />
      <xsl:text>),</xsl:text> 
      <xsl:value-of select="@value" />
      <xsl:text>),</xsl:text> 
      <xsl:value-of select="@label" />
      <!-- <xsl:text>(e:s),)</xsl:text> -->
      <xsl:text>,)</xsl:text>
</xsl:if>

                

</xsl:template>	 
  
<xsl:template name="composer">
  <xsl:param name="sel_ref" />
  <xsl:param name="bold" />
  <xsl:param name="how" />
  <xsl:param name="name_len" />
  <xsl:param name="type" />
  <xsl:param name="id" />  
  <xsl:param name="pos" />
  <xsl:param name="last" />
  <xsl:param name="sep" />  
        
        <xsl:if test="string-length($name_len)>0 ">
          <xsl:text>(f,text,cut_nice,</xsl:text>      
        </xsl:if>  
        
        <xsl:choose>
          <xsl:when test="$type='file'">
            <xsl:text>(f,try,()(f,name_of_file,(f,try,</xsl:text>  
          </xsl:when>
          <xsl:when test="$type='dir'">
            <xsl:text>(f,try,()(f,name_of_dir,(f,try,</xsl:text>  
          </xsl:when>          
        </xsl:choose> 
        
        <xsl:choose>                   
          <xsl:when test="$how='loop'">
            
            <xsl:choose>
              <xsl:when test="string-length(@sel_ref) > 0">
                <xsl:call-template name="sel_translator_internal">     
                  <xsl:with-param name="sel_ref" select="$sel_ref"/>
                  <xsl:with-param name="id" select="$id"/>
                </xsl:call-template>    
  
              </xsl:when>
              <xsl:when test="string-length(@sel_file) > 0">
                <xsl:call-template name="sel_translator_internalxx">     nic123
                  <xsl:with-param name="sel_file" select="@sel_file"/>
                  <xsl:with-param name="id" select="$id"/>
                </xsl:call-template>        
              </xsl:when>              
              <xsl:otherwise>
                <xsl:text>(loop,</xsl:text><xsl:value-of select="$view_loop_id" /><xsl:text>,@</xsl:text>
                <xsl:value-of select="$id" />
                <xsl:text>)</xsl:text>               
              </xsl:otherwise>
            </xsl:choose>

          </xsl:when>
          <xsl:when test="$how='detail'">
          
             <xsl:choose>
              <!-- 
              <xsl:when test="string-length(@sel_ref) > 0">
                <xsl:call-template name="sel_translator_internal">     
                  <xsl:with-param name="sel_ref" select="$sel_ref"/>
                  <xsl:with-param name="id" select="$id"/>
                </xsl:call-template>        
              </xsl:when>
              -->
              <xsl:when test="string-length(@sel_file) > 0">
                <xsl:text>(f,tk_sel_translate,(prop,</xsl:text>
                <xsl:value-of select="$singular_id" />
                <xsl:text>_</xsl:text>
                <xsl:value-of select="$id" /> 
                <xsl:text>),</xsl:text>
                <xsl:value-of select="@sel_file" />
                <xsl:text>)</xsl:text>
              </xsl:when>              
              <xsl:otherwise>
                <xsl:choose>
                  <xsl:when test="$type='dir'">
                    <xsl:text>(dir,</xsl:text>
                  </xsl:when>
                  <xsl:when test="$type='file'">
                    <xsl:text>(file,</xsl:text>
                  </xsl:when>      
                  <xsl:otherwise>
                    <xsl:text>(prop,</xsl:text>
                  </xsl:otherwise>
                </xsl:choose>            
                <xsl:value-of select="$singular_id" />
                <xsl:text>_</xsl:text>
                <xsl:value-of select="$id" />
                <xsl:text>)</xsl:text>               
              </xsl:otherwise>
            </xsl:choose>
                     
    
          </xsl:when>          
        </xsl:choose>           
        

        
        <xsl:choose>
          <xsl:when test="$type='file'">
            <xsl:text>,))(),)</xsl:text>       
          </xsl:when>
          <xsl:when test="$type='dir'">
            <xsl:text>,))(),)</xsl:text>       
          </xsl:when>          
        </xsl:choose>  
        
        <xsl:if test="string-length($name_len)>0 ">
          <xsl:text>,</xsl:text>
          <xsl:value-of select="$name_len" />
          <xsl:text>)</xsl:text>      
        </xsl:if>      
  
                
   
        <xsl:if test="not($pos=$last)">
          <xsl:choose>
            <xsl:when test="@keinpunkt=1">
              <xsl:text> </xsl:text>       
            </xsl:when>
            <xsl:otherwise>
              <xsl:text>.</xsl:text>       
            </xsl:otherwise>          
          </xsl:choose>  
        </xsl:if>                     
            
</xsl:template>

<!-- 
<xsl:template match="field" mode="expression">
  <xsl:text>(prop,</xsl:text>
  <xsl:value-of select="$singular_id" />
  <xsl:text>_</xsl:text>
  <xsl:value-of select="@id" />
  <xsl:text>) </xsl:text>
</xsl:template>
-->

</xsl:stylesheet>

  