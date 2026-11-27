<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">

<jobs>
<job id="Computer Check" role="hidden" joblevel="ws">

  <file id="cc_computers">(dir,filetemp)cc_computers.xml</file>
  <create_file out="(file,cc_computers)">
    <xsl:text>&lt;computers&gt;</xsl:text>
    <xsl:apply-templates select="computers/computer" mode="c1"/>
   
    <xsl:text>&lt;/computers&gt;</xsl:text>
  </create_file>

  <prop id="nbr-files" x="(f,xsl,(file,cc_computers),count(e:o)//file(e:c))"/>
  <tk type="msg" x="nbr = (prop,nbr-files)" test="0"/>
  <when test="(f,gt,(prop,nbr-files),0)">
  <loop id="c" xml="(file,cc_computers)" xpath="computer">
    <prop id="count">0</prop>
    <loop id="f" xml="(file,cc_computers)" xpath="computer[label[text(e:o)(e:c)=(f,xpath_string,(loop,c,label))]]/file">
      <when test="(f,file_exists,(loop,f))">
        <prop id="count">(f,add,(prop,count),1)</prop>
      </when>      
    </loop>
    <when test="(f,gt,(prop,count),0)">
      <xml_update 
        what="(file,cc_computers)" 
        where="computer[label[text(e:o)(e:c)=(f,xpath_string,(loop,c,label))]]" 
        how="set_attribute" 
        attribute="count" 
        value="(prop,count)" />
    </when>    
  </loop>
  
  <prop id="found_anything">0</prop>
  <loop id="d" xml="(file,cc_computers)" xpath="computer" sort="@count" number="@count" limit="1" desc="@count">
    
    <when test="(f,gt,(loop,d,@count),0)">                
      <warn>Computer should be: (loop,d,label)</warn>
      <prop id="found_anything">1</prop>
      <when test="(f,not,(f,eq,(prop,computer),(loop,d,label)))">
      
        <call_service job="go-joe-read!"/>
        <file id="joe-in-store" x="(dir,status)lc-switch-computer.lsx"/>
        <call_service job="go-joe-read"/>
        <tk label="Computer Recognition">
        <tk type="bool" id="switch" label="Switch Computer to '(loop,d,label)'" x="1" flag="nono"
        />
        </tk>
        
        <call_service job="go-joe-write!"/>
        
        <file id="joe-in-store" x="(dir,status)lc-switch-computer.lsx"/>
        <prop id="joe-in-regex" x="switch"/>
        <call_service job="go-joe-write-srv"/>
                
        <when test="(prop,switch)">
          <restart computer="(loop,d,label)"/>
        </when>
      </when>
    </when>
 
  </loop>  

    <when test="(f,and,(f,not,(prop,found_anything)),(f,has,prop,computer))">
        
        <call_service job="go-joe-read!"/>
        <file id="joe-in-store" x="(dir,status)lc-switch-unknown-computer.lsx"/>
        <call_service job="go-joe-read"/>
        <tk label="Computer Recognition">
        <tk type="bool" id="switch2" label="()This computer is not registered,  so this is not ()(prop,computer). Is that true ?" x="1" flag="nono"/>
        </tk>
        
        <call_service job="go-joe-write!"/>        
        <file id="joe-in-store" x="(dir,status)lc-switch-unknown-computer.lsx"/>
        <prop id="joe-in-regex" x="switch2"/>
        <call_service job="go-joe-write-srv"/>
        
        <when test="(prop,switch2)">
            <restart computer=""/>
        </when>
    </when>
  </when>
</job>
</jobs>

</xsl:template>

<xsl:template match="computer" mode="c1">

    <xsl:call-template name="computer-c2">
      <xsl:with-param name="computer" select="@computer" />
    </xsl:call-template>     

</xsl:template>

<xsl:template name="computer-c2" >
    <xsl:param name="computer"/>
    
  <xsl:if test="count(document('compi-takeoff.xml')/takeoff/computers/computer[text()=$computer])>0">
  

    <xsl:text>&lt;computer count=&quot;0&quot; </xsl:text>
    
    <xsl:text> os=&quot;</xsl:text>
    <xsl:value-of select="@os" />
    <xsl:text>&quot;&gt;&lt;label&gt;&lt;![CDATA[()</xsl:text>
    <xsl:value-of select="@computer" />
    <xsl:text>()]]&gt;&lt;/label&gt;</xsl:text>
    <xsl:apply-templates select="recognition"/>
    <xsl:text>&lt;/computer&gt;</xsl:text> 
  
  </xsl:if>  
</xsl:template>

<xsl:template match="recognition">
  <xsl:text>&lt;file&gt;&lt;![CDATA[</xsl:text>
  <xsl:value-of select="@file" />
  <xsl:text>]]&gt;&lt;/file&gt;</xsl:text>
</xsl:template>

<xsl:template name="execution">
<job id="sel_open_file_typical">
  <xsl:attribute name="computer">
    <xsl:value-of select="computer" />
  </xsl:attribute>
  <xsl:attribute name="os">
    <xsl:value-of select="os" />
  </xsl:attribute>  
  <call_service job="sel_open_file_memo_in" />
  <xsl:call-template name="execution"/>
</job>  
  <choose>
      <xsl:apply-templates select="softwares/software[computer=computer]" mode="exe">    
        <xsl:sort select="position"/>
      </xsl:apply-templates>
      <when test="(f,eq,(prop,sel),System Default)">  
        <xsl:choose>
          <xsl:when test="os='win'">
            <exe>
              <arg>explorer.exe</arg>
              <arg>(file,with)</arg>
            </exe>      
          </xsl:when>
          <xsl:when test="os='mac'">
            <exe>
          	  <arg>open</arg>
          	  <arg>(file,with)</arg>
            </exe>	     
          </xsl:when>
          <xsl:when test="os='linux'">
          </xsl:when>
        </xsl:choose>
      </when>         
      <otherwise>
        <error>There is a serious problem</error>
      </otherwise>   
  </choose>
  
</xsl:template>

<xsl:template match="software" mode="exe">
  <when>
    <xsl:attribute name="test">
      <xsl:text>(f,eq,(prop,sel),</xsl:text>
      <xsl:value-of select="label" />
      <xsl:text>)</xsl:text>
    </xsl:attribute>   
    <xsl:choose>
      <xsl:when test="os='win'">
        <exe>
          <arg><xsl:value-of select="pgm_file" /></arg>
          <arg>(file,with)</arg>
        </exe>      
      </xsl:when>
      <xsl:when test="os='mac'">
        <exe>
      	  <arg>open</arg>
      	  <arg>-a</arg>
      	  <arg><xsl:value-of select="pgm_file" /></arg>
      	  <arg>(file,with)</arg>
        </exe>      
      </xsl:when>
      <xsl:when test="os='linux'">
      </xsl:when>
    </xsl:choose>
  </when>   
</xsl:template>

<xsl:template match="software" mode="tk_sel">
  <sel><xsl:value-of select="label" /></sel>
</xsl:template>

<!-- <job id="View Cities" joblevel="app" >  -->
<xsl:template match="cirgendwasonf">
<job>
  <xsl:attribute name="id"><xsl:value-of select="s2/@job_city_view" /></xsl:attribute>
  <xsl:attribute name="level">
    <xsl:value-of select="s1/@job_level" />
  </xsl:attribute>  
  <xsl:attribute name="role">
    <xsl:value-of select="s1/@job_role" />
  </xsl:attribute>    
  
  <!--  <ti type="call" explain="Add new City(prop,cities_offset_label)" job="City Add" /> -->
  
  <xsl:if test="s1/@view_add=1">
  <when>
  <xsl:attribute name="test">
    <xsl:text>(f,not,(f,contains,</xsl:text>
    <xsl:value-of select="s2/@prop_cities_offset" />
    <xsl:text>,node(e:o)(e:c)))</xsl:text>   
  </xsl:attribute>
  <ti type="call">
    <xsl:attribute name="explain">
      <xsl:value-of select="s1/@label_add" />
    </xsl:attribute>
    <xsl:attribute name="explain_alt">
      <xsl:value-of select="s1/@label_add" /><xsl:text> </xsl:text><xsl:value-of select="s1/@singular_label" /><xsl:value-of select="s2/@prop_cities_offset_label" />
    </xsl:attribute>      
    <xsl:attribute name="job">
      <xsl:value-of select="s2/@job_city_add" />
    </xsl:attribute>     
    

  </ti> 
  </when>
  </xsl:if>
  
  <prop id="nothing">1</prop>
  
  
  <!-- <loop id="i" xml="(file,cities)" xpath="(prop,cities_offset)city" sort="id" number="id">  -->
  <loop id="i">
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
    
    <when test="(prop,nothing)">
      <!-- <ti label="List of Cities(prop,cities_offset_label)" size="2" padding_top="6px">  -->
      <ti size="2" padding_top="10px">
        <xsl:attribute name="lXXabel">
          <xsl:text>List of </xsl:text><xsl:value-of select="s1/@plural_label" /><xsl:value-of select="s2/@prop_cities_offset_label" />
        </xsl:attribute>
        <ti type="rec" label=" ">
          <xsl:attribute name="label_width">
            <xsl:value-of select="s1/@id_width_px" /><xsl:text>px</xsl:text>
          </xsl:attribute>
          
          <!-- loop <ti x="(f,html,bold,open)Name(f,html,bold,close)" width="100px" />  -->
          <xsl:apply-templates select="fields/field" mode="header">    
            <xsl:sort select="@position"/>
          </xsl:apply-templates> 
          
          <xsl:if test="s1/@view_details=1">
            <ti x=" ">
              <xsl:attribute name="width">
                <xsl:value-of select="s1/@label_details_width_px" /><xsl:text>px</xsl:text>
              </xsl:attribute>          
            </ti>
          </xsl:if>
          
          <xsl:if test="s1/@view_edit=1">
            <ti x=" ">
              <xsl:attribute name="width">
                <xsl:value-of select="s1/@label_edit_width_px" /><xsl:text>px</xsl:text>
              </xsl:attribute>          
            </ti> 
           </xsl:if> 
           
           <xsl:if test="s1/@view_delete=1">        
            <ti x=" ">
              <xsl:attribute name="width">
                <xsl:value-of select="s1/@label_delete_width_px" /><xsl:text>px</xsl:text>
              </xsl:attribute>          
            </ti> 
           </xsl:if>    
        </ti>  
        
      </ti>
      <prop id="nothing">0</prop> 
    </when>
    
    <!-- <ti type="rec" label="((loop,i,@row))"  label_width="20px"> -->

    <ti type="rec">
      <xsl:attribute name="key">
        <xsl:text>#(loop,i,@</xsl:text><xsl:value-of select="s1/@id_attribute_name" /><xsl:text>)</xsl:text>
      </xsl:attribute>    
      <xsl:attribute name="label"> 
        <xsl:text>(f,html,bold,open)</xsl:text>
        <xsl:value-of select="//fields/@name_expression_loop" />
        <xsl:text>(f,html,bold,close)</xsl:text>
      </xsl:attribute>        
      <xsl:attribute name="label_width">
        <xsl:value-of select="s1/@id_width_px" /><xsl:text>px</xsl:text>
      </xsl:attribute>
      
      <xsl:apply-templates select="fields/field" mode="data">    
        <xsl:sort select="@position"/>
      </xsl:apply-templates>  
      
      <!-- 
      <ti type="call" explain="Details" width="50px" job="City Details" >
        <prop id="rec_id">(loop,i,@row)</prop>
      </ti>
      -->
      <xsl:if test="s1/@view_details=1">   
      <ti type="call">
        <xsl:attribute name="width">
          <xsl:value-of select="s1/@label_details_width_px" /><xsl:text>px</xsl:text>
        </xsl:attribute>         
        <xsl:attribute name="explain">
          <xsl:value-of select="s1/@label_details" />
        </xsl:attribute>   
        <xsl:attribute name="job">
          <xsl:value-of select="s2/@job_city_details" />
        </xsl:attribute>            
        <prop id="rec_id"><xsl:value-of select="s2/@loop_i_row" /></prop>
       
      </ti>
      </xsl:if>      
      
      <!--
      <ti type="call" explain="Edit" width="50px" job="City Edit" >
        <prop id="rec_id">(loop,i,@row)</prop>
      </ti>  
      --> 
      <xsl:if test="s1/@view_edit=1">  
      <ti type="call">
        <xsl:attribute name="width">
          <xsl:value-of select="s1/@label_edit_width_px" /><xsl:text>px</xsl:text>
        </xsl:attribute>         
        <xsl:attribute name="explain">
          <xsl:value-of select="s1/@label_edit" />
        </xsl:attribute>   
        <xsl:attribute name="job">
          <xsl:value-of select="s2/@job_city_edit" />
        </xsl:attribute>            
        <prop id="rec_id"><xsl:value-of select="s2/@loop_i_row" /></prop>
      
      </ti>
      </xsl:if>        
                  
      <!--
      <ti type="call" explain="Delete" width="50px" job="City Delete" >
        <prop id="rec_id">(loop,i,@row)</prop>
      </ti> 
      --> 
      <xsl:if test="s1/@view_delete=1">
      <ti type="call">
        <xsl:attribute name="width">
          <xsl:value-of select="s1/@label_delete_width_px" /><xsl:text>px</xsl:text>
        </xsl:attribute>         
        <xsl:attribute name="explain">
          <xsl:value-of select="s1/@label_delete" />
        </xsl:attribute>   
        <xsl:attribute name="job">
          <xsl:value-of select="s2/@job_city_delete" />
        </xsl:attribute>            
        <prop id="rec_id"><xsl:value-of select="s2/@loop_i_row" /></prop>
 
      </ti>
      </xsl:if>        
     
      
    </ti>  
               
  </loop>
    
  <when test="(prop,nothing)">
    <ti><xsl:text>No </xsl:text><xsl:value-of select="s1/@singular_label" /><xsl:text> found</xsl:text></ti>
  </when>
    
  <!--   <ti label="Data" padding_top="6px" size="2">
    <ti label="File:" type="file">(file,cities)</ti>
    <ti label="Xml-Offset:" test="(f,gt,(f,len,(prop,cities_offset)),0)">(prop,cities_offset)</ti>
  </ti>  -->
  <xsl:choose>
  <xsl:when test="s1/@view_filelink=1">
    <ti_break/>
    <ti label=" " padding_top="20px" sixze="2">
      <ti label="File:" type="file"><xsl:value-of select="s2/@file_cities" /></ti>
      <ti label="Xml-Offset:">
        <xsl:attribute name="test">
          <xsl:text>(f,gt,(f,len,</xsl:text>
          <xsl:value-of select="s2/@prop_cities_offset" />
          <xsl:text>),0)</xsl:text>
        </xsl:attribute>     
        <xsl:value-of select="s2/@prop_cities_offset" />    
      </ti>
    </ti>
  </xsl:when>
  <xsl:otherwise>
    <ti><ti/></ti>
  </xsl:otherwise>
  </xsl:choose>
  
  
  <!--   -->
  
</job>
</xsl:template>

<!-- ******************************************* -->
<xsl:template match="field" mode="data">

  <xsl:if test="@view_flag=1">
     <ti>
        <xsl:attribute name="x">
          <xsl:choose>
            <xsl:when test="string-length(@view_nbr_characters)>0">
              <xsl:text>(f,when,(loop,i,exists?,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>),(f,substring,(loop,i,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>),0,</xsl:text>
              <xsl:value-of select="@view_nbr_characters" />
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
              <xsl:text>),(f,when,(loop,i,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>),Yes,No),?)</xsl:text>          
            </xsl:when>   
            <xsl:otherwise>  
              <xsl:text>(f,when,(loop,i,exists?,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>),(loop,i,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>),?)</xsl:text>                     
            </xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
        <xsl:attribute name="width">
          <xsl:value-of select="@view_width_px" /><xsl:text>px</xsl:text>
        </xsl:attribute>  
        <xsl:if test="@align='right'">
          <xsl:attribute name="align">
            <xsl:text>right</xsl:text>
          </xsl:attribute>          
        </xsl:if>        
      </ti>      
  </xsl:if>
</xsl:template>

<!-- ******************************************* -->
<xsl:template match="field" mode="header">
  <!-- loop <ti x="(f,html,bold,open)Name(f,html,bold,close)" width="100px" />  -->
  <xsl:if test="@view_flag=1">
     <ti>
        <xsl:attribute name="x">
          <xsl:text></xsl:text><xsl:value-of select="@label" /><xsl:text></xsl:text>
        </xsl:attribute>
        <xsl:attribute name="width">
          <xsl:value-of select="@view_width_px" /><xsl:text>px</xsl:text>
        </xsl:attribute>  
        <xsl:if test="@align='right'">
          <xsl:attribute name="align">
            <xsl:text>right</xsl:text>
          </xsl:attribute>          
        </xsl:if>      
      </ti>      
  </xsl:if>
</xsl:template>

</xsl:stylesheet>

  