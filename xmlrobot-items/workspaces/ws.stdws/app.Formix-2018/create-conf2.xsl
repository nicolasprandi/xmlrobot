<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015 Nicolas Prandstaetter. All rights reserved.  -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">
   <xsl:apply-templates/>
</xsl:template>

<xsl:template name="FirstCase">
  <xsl:param name="text"/>
  <xsl:value-of select="translate(substring($text,1,1),'abcdefghijklmnopqrstuvsxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ')"/>
  <xsl:value-of select="substring($text,2)"/>
</xsl:template>

<xsl:template match="s1">
  <s2>

    <xsl:attribute name="firstcase_singular_id">
      <xsl:call-template name="FirstCase">
        <xsl:with-param name="text"><xsl:value-of select="@singular_id" /></xsl:with-param>
      </xsl:call-template>
    </xsl:attribute> 
      
    <xsl:attribute name="post_edit_script_propid">
      <xsl:text>post_edit_script_</xsl:text><xsl:value-of select="@singular_id" />
    </xsl:attribute> 
    
    <xsl:attribute name="post_add_script_propid">
      <xsl:text>post_add_script_</xsl:text><xsl:value-of select="@singular_id" />
    </xsl:attribute>  
    
    <xsl:attribute name="post_del_script_propid">
      <xsl:text>post_del_script_</xsl:text><xsl:value-of select="@singular_id" />
    </xsl:attribute>     
    
    <xsl:attribute name="post_edit_script_prop">
      <xsl:text>prop,post_edit_script_</xsl:text><xsl:value-of select="@singular_id" />
    </xsl:attribute>   
    
    <xsl:attribute name="post_add_script_prop">
      <xsl:text>prop,post_add_script_</xsl:text><xsl:value-of select="@singular_id" />
    </xsl:attribute>    

    <xsl:attribute name="post_del_script_prop">
      <xsl:text>prop,post_del_script_</xsl:text><xsl:value-of select="@singular_id" />
    </xsl:attribute>                 

    <xsl:attribute name="prop_cities_sort_attribute">
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@plural_id" />
      <xsl:text>_sort_attribute)</xsl:text>
    </xsl:attribute>

    <xsl:attribute name="prop_cities_sort_number">
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@plural_id" />
      <xsl:text>_sort_number)</xsl:text>
    </xsl:attribute>
    
    <xsl:attribute name="prop_cities_sort_ignore_case">
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@plural_id" />
      <xsl:text>_sort_ignore_case)</xsl:text>
    </xsl:attribute>
    
    <xsl:attribute name="prop_cities_sort_desc">
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@plural_id" />
      <xsl:text>_sort_desc)</xsl:text>
    </xsl:attribute>        
    
    <xsl:attribute name="prop_cities_where">
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@plural_id" />
      <xsl:text>_where)</xsl:text>
    </xsl:attribute>    
        
    <xsl:attribute name="prop_key">
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@singular_id" />
      <xsl:text>_key)</xsl:text>
    </xsl:attribute>

    <xsl:attribute name="key">
      <xsl:value-of select="@singular_id" />
      <xsl:text>_key</xsl:text>
    </xsl:attribute>
    
    <xsl:attribute name="spirit">
      <xsl:value-of select="@plural_id" />
      <xsl:text>_spirit_tc</xsl:text>
    </xsl:attribute>    
    
    <xsl:attribute name="read_loop_id">
      <xsl:value-of select="@plural_id" />
      <xsl:text>_det_loop</xsl:text>
    </xsl:attribute>        
          
    <xsl:attribute name="file_cities">
      <xsl:text>(file,</xsl:text><xsl:value-of select="@plural_id" /><xsl:text>)</xsl:text>
    </xsl:attribute>
    
    <xsl:attribute name="prop_cities_offset_label">
      <xsl:text>(prop,</xsl:text><xsl:value-of select="@plural_id" /><xsl:text>_offset_label)</xsl:text>
    </xsl:attribute>
    
    <xsl:attribute name="prop_cities_offset">
      <xsl:text>(prop,</xsl:text><xsl:value-of select="@plural_id" /><xsl:text>_offset)</xsl:text>
    </xsl:attribute>
    
    <xsl:attribute name="cities_offset_new_key">
      <xsl:value-of select="@plural_id" /><xsl:text>_offset_new_key</xsl:text>
    </xsl:attribute>
    
    <xsl:attribute name="job_city_details">
      <xsl:value-of select="@singular_id" /><xsl:text>-see</xsl:text>
    </xsl:attribute>
    
    <xsl:attribute name="job_city_delete">
      <xsl:value-of select="@singular_id" /><xsl:text>-del</xsl:text>
    </xsl:attribute>
    
    <xsl:attribute name="job_city_edit">
      <xsl:value-of select="@singular_id" /><xsl:text>-edit</xsl:text>
    </xsl:attribute> 
    
    <xsl:attribute name="job_city_editor">
      <xsl:value-of select="@singular_id" /><xsl:text>-editor</xsl:text>
    </xsl:attribute>     
    
    <xsl:attribute name="job_city_view">
      <xsl:value-of select="@singular_id" /><xsl:text>-see-all</xsl:text>
    </xsl:attribute> 
     
    
    <xsl:attribute name="job_city_add">
      <xsl:value-of select="@singular_id" /><xsl:text>-add</xsl:text>
    </xsl:attribute>  
    
    <xsl:attribute name="job_city_tomemory">
      <xsl:value-of select="@singular_id" /><xsl:text>-read</xsl:text>
    </xsl:attribute>  
    
    <xsl:attribute name="job_city_tofile">
      <xsl:value-of select="@singular_id" /><xsl:text>-write</xsl:text>
    </xsl:attribute>  
    
    <xsl:attribute name="job_city_validate">
      <xsl:value-of select="@singular_id" /><xsl:text>-validate</xsl:text>
    </xsl:attribute>
    
    <xsl:attribute name="city_input_valid">
      <xsl:value-of select="@singular_id" /><xsl:text>_input_valid</xsl:text>
    </xsl:attribute>
    <xsl:attribute name="city_input_loop">
      <xsl:value-of select="@singular_id" /><xsl:text>_loop</xsl:text>
    </xsl:attribute>        
    <xsl:attribute name="prop_city_input_valid">
      <xsl:text>(prop,</xsl:text><xsl:value-of select="@singular_id" /><xsl:text>_input_valid)</xsl:text>
    </xsl:attribute>                   

    <xsl:attribute name="job_city_tk">
      <xsl:value-of select="@singular_id" /><xsl:text>-tk</xsl:text>
    </xsl:attribute> 
    
    <xsl:attribute name="prop_tk_control">
      <xsl:text>(prop,</xsl:text><xsl:value-of select="@plural_id" /><xsl:text>_tk_control)</xsl:text>
    </xsl:attribute>   
    
    <xsl:attribute name="tk_control">
      <xsl:value-of select="@plural_id" /><xsl:text>_tk_control</xsl:text>
    </xsl:attribute>  
    
    <xsl:attribute name="prop_label">
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@singular_id" /><xsl:text>_default_label</xsl:text>
      <xsl:text>)</xsl:text>
    </xsl:attribute>   
    
    <xsl:attribute name="label">
      <xsl:value-of select="@singular_id" /><xsl:text>_default_label</xsl:text>
    </xsl:attribute>        
    
    <xsl:attribute name="prop_label_class">
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@singular_id" /><xsl:text>_default_label_class</xsl:text>
      <xsl:text>)</xsl:text>
    </xsl:attribute>   
    
    <xsl:attribute name="label_class">
      <xsl:value-of select="@singular_id" /><xsl:text>_default_label_class</xsl:text>
    </xsl:attribute>        
    
    <xsl:attribute name="prop_label_name">
      <xsl:text>(prop,</xsl:text>
      <xsl:value-of select="@singular_id" /><xsl:text>_default_label_name</xsl:text>
      <xsl:text>)</xsl:text>
    </xsl:attribute>   
    
    <xsl:attribute name="label_name">
      <xsl:value-of select="@singular_id" /><xsl:text>_default_label_name</xsl:text>
    </xsl:attribute>      
    
    
    <!-- 
    <xsl:attribute name="prop_label">
      <xsl:text>(prop,</xsl:text><xsl:value-of select="@plural_id" /><xsl:text>_label)</xsl:text>
    </xsl:attribute>   
    
    <xsl:attribute name="label">
      <xsl:value-of select="@plural_id" /><xsl:text>_label</xsl:text>
    </xsl:attribute> 
    -->        

    <xsl:attribute name="jobname_prolog">
      <xsl:choose>
        <xsl:when test="string-length(@alternative_prolog)>0">
          <xsl:value-of select="@alternative_prolog" />
        </xsl:when>
        <xsl:otherwise>
        
          <xsl:choose>
            <xsl:when test="@usage='service'">
              <xsl:text>prolog-</xsl:text>
              <xsl:value-of select="@singular_id" />
            </xsl:when>
            <xsl:otherwise>
              <xsl:text>prolog</xsl:text>
            </xsl:otherwise>
          </xsl:choose>        
               
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>     
    
    <xsl:attribute name="label_nodata">
      <xsl:choose>
        <xsl:when test="string-length(@label_nodata)>0">
          <xsl:value-of select="@label_nodata" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>No </xsl:text><xsl:value-of select="@plural_label" /><xsl:text> found</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>      
    
    <xsl:attribute name="label_edit">
      <xsl:choose>
        <xsl:when test="string-length(@label_edit)>0">
          <xsl:value-of select="@label_edit" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>Edit</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>     
    
    <xsl:attribute name="label_delete">
      <xsl:choose>
        <xsl:when test="string-length(@label_delete)>0">
          <xsl:value-of select="@label_delete" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>Delete</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>   
        
    <xsl:attribute name="see_keys">
      <xsl:choose>
        <xsl:when test="string-length(@see_keys)>0">
          <xsl:value-of select="@see_keys" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>1</xsl:text>        
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>            

    <xsl:attribute name="view_loop_id">
      <xsl:choose>
        <xsl:when test="string-length(@view_loop_id)>0">
          <xsl:value-of select="@view_loop_id" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="@singular_id" /><xsl:text>_loop</xsl:text>        
        </xsl:otherwise>
      </xsl:choose>    
    
    </xsl:attribute> 
    
    <xsl:attribute name="view_loop_prefix">
      <xsl:text>(f,loop,</xsl:text>
      <xsl:choose>
        <xsl:when test="string-length(@view_loop_id)>0">
          <xsl:value-of select="@view_loop_id" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="@singular_id" /><xsl:text>_loop</xsl:text>        
        </xsl:otherwise>
      </xsl:choose>       
      <xsl:text>,</xsl:text>
    </xsl:attribute>   
    
    <xsl:attribute name="view_loop_prefix_attribute">
      <xsl:text>(f,loop,</xsl:text>
      <xsl:choose>
        <xsl:when test="string-length(@view_loop_id)>0">
          <xsl:value-of select="@view_loop_id" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="@singular_id" /><xsl:text>_loop</xsl:text>        
        </xsl:otherwise>
      </xsl:choose>       
      <xsl:text>,@</xsl:text>
    </xsl:attribute> 
    
    <xsl:attribute name="view_loop_prefix_attribute2">
      <xsl:text>(loop,</xsl:text>
      <xsl:choose>
        <xsl:when test="string-length(@view_loop_id)>0">
          <xsl:value-of select="@view_loop_id" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="@singular_id" /><xsl:text>_loop</xsl:text>        
        </xsl:otherwise>
      </xsl:choose>       
      <xsl:text>,@</xsl:text>
    </xsl:attribute>            
        
    <xsl:attribute name="loop_i_row">
      <xsl:text>(loop,</xsl:text>
      <xsl:choose>
        <xsl:when test="string-length(@view_loop_id)>0">
          <xsl:value-of select="@view_loop_id" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="@singular_id" /><xsl:text>_loop</xsl:text>        
        </xsl:otherwise>
      </xsl:choose> 
      <xsl:text>,@</xsl:text><xsl:value-of select="@id_attribute_name" /><xsl:text>)</xsl:text>
    </xsl:attribute>  
    
    <xsl:attribute name="city_dt_upd">
      <xsl:value-of select="@singular_id" /><xsl:text>_dt_upd</xsl:text>
    </xsl:attribute> 
    
    <xsl:attribute name="city_dt_add">
      <xsl:value-of select="@singular_id" /><xsl:text>_dt_add</xsl:text>
    </xsl:attribute>            
    
    <xsl:attribute name="prop_return_job">
      <xsl:text>(prop,</xsl:text><xsl:value-of select="@plural_id" /><xsl:text>_return_job)</xsl:text>
    </xsl:attribute>   
    
    <xsl:attribute name="return_job">
      <xsl:value-of select="@plural_id" /><xsl:text>_return_job</xsl:text>
    </xsl:attribute>    
    
    <xsl:attribute name="ti_call">
      <xsl:choose>
        <xsl:when test="@usage='service'">
          <xsl:text>call_service</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>call_job</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute> 
    
    <xsl:attribute name="usage">
      <xsl:choose>
        <xsl:when test="@usage='service'">
          <xsl:text>service</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>app</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>   
    
    <xsl:attribute name="datafile">
      <xsl:choose>
        <xsl:when test="@usage='service'">
          <xsl:text>(dir,services)</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>(dir,pro)</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:value-of select="@plural_id" />
      <xsl:text>.xml</xsl:text>
    </xsl:attribute>         
    
    <!-- off_up_details_top schaltet den up-button in den details ein/aus, default 1 -->
    <xsl:attribute name="off_up_details_top">
      <xsl:choose>
        <xsl:when test="string-length(@off_up_details_top)>0">
          <xsl:value-of select="@off_up_details_top" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>0</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>  
    
    <!-- off_up_details_bottom schaltet den up-button in den details ein/aus, default 1 -->
    <xsl:attribute name="off_up_details_bottom">
      <xsl:choose>
        <xsl:when test="string-length(@off_up_details_bottom)>0">
          <xsl:value-of select="@off_up_details_bottom" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>0</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>          
    
    <!-- off_up_view_top schaltet den up-button in der list-view ein/aus, default 1 -->
    <xsl:attribute name="off_up_view_top">
      <xsl:choose>
        <xsl:when test="string-length(@off_up_view_top)>0">
          <xsl:value-of select="@off_up_view_top" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>0</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>      
    
    <xsl:attribute name="off_sorter">
      <xsl:choose>
        <xsl:when test="string-length(@off_sorter)>0">
          <xsl:value-of select="@off_sorter" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>0</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>          
    
    <xsl:attribute name="details_edit_float">
      <xsl:choose>
        <xsl:when test="string-length(@details_edit_float)>0">
          <xsl:value-of select="@details_edit_float" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>1</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>   
    
    <!-- view -->
    
  <xsl:attribute name="actions_view_style">
      <xsl:choose>
        <xsl:when test="string-length(@actions_view_style)>0">
          <xsl:value-of select="@actions_view_style" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>menu</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>              
    
    <xsl:attribute name="actions_view_span">
      <xsl:choose>
        <xsl:when test="string-length(@actions_view_span)>0">
          <xsl:value-of select="@actions_view_span" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>1</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>          
    
   <!-- view_first -->
    
  <xsl:attribute name="actions_view_first_style">
    <xsl:choose>
      <xsl:when test="string-length(@actions_view_first_style)>0">
        <xsl:value-of select="@actions_view_first_style" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>menu</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:attribute>              
  
  <xsl:attribute name="actions_view_first_span">
    <xsl:choose>
      <xsl:when test="string-length(@actions_view_first_span)>0">
        <xsl:value-of select="@actions_view_first_span" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>1</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:attribute>         
  
  <xsl:attribute name="actions_view_prolog_style">
    <xsl:choose>
      <xsl:when test="string-length(@actions_view_prolog_style)>0">
        <xsl:value-of select="@actions_view_prolog_style" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>menu</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:attribute>              
  
  <xsl:attribute name="actions_view_prolog_span">
    <xsl:choose>
      <xsl:when test="string-length(@actions_view_prolog_span)>0">
        <xsl:value-of select="@actions_view_prolog_span" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>1</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:attribute>   
  
  <xsl:attribute name="actions_view_last_style">
    <xsl:choose>
      <xsl:when test="string-length(@actions_view_last_style)>0">
        <xsl:value-of select="@actions_view_last_style" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>menu</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:attribute>              
  
  <xsl:attribute name="actions_view_last_span">
    <xsl:choose>
      <xsl:when test="string-length(@actions_view_last_span)>0">
        <xsl:value-of select="@actions_view_last_span" />
      </xsl:when>
      <xsl:otherwise>
        <xsl:text>1</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:attribute>     
  
                 
                 
  </s2>
</xsl:template>

</xsl:stylesheet>

  