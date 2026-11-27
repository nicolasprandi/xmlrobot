<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <!-- -->
<xsl:param name="computer" />
<xsl:param name="os" />

<xsl:output method="xml" indent="yes"/>
<xsl:template match="/">

<steps>

      <xsl:apply-templates select="computers/computer[@computer=$computer]/programm" mode="tk_sel">    
        <xsl:sort select="@sort"/>
      </xsl:apply-templates> 
      
</steps>

<job id="sel_open_file">
  <xsl:attribute name="computer">
    <xsl:value-of select="$computer" />
  </xsl:attribute>
  <xsl:attribute name="os">
    <xsl:value-of select="$os" />
  </xsl:attribute>  
  <call_service job="sel_open_file_memo_in" />
  <tk label="Open File">
    <tk type="sel" id="sel" label="Select a Programm to Open the File" >
      <sel><xsl:text>System Default</xsl:text></sel>
      <xsl:apply-templates select="computers/computer[@computer=$computer]/programm" mode="tk_sel">    
        <xsl:sort select="@sort"/>
      </xsl:apply-templates> 
    </tk>
  </tk>
  <call_service job="sel_open_file_memo_out" />
  <xsl:call-template name="execution"/>

</job>

<job id="sel_open_file_typical">
  <xsl:attribute name="computer">
    <xsl:value-of select="$computer" />
  </xsl:attribute>
  <xsl:attribute name="os">
    <xsl:value-of select="$os" />
  </xsl:attribute>  
  <call_service job="sel_open_file_memo_in" />
  <xsl:call-template name="execution"/>
</job>  
</jobs>

</xsl:template>
<xsl:template name="execution">

  <choose>
      <xsl:apply-templates select="//computers/computer[@computer=$computer]/programm" mode="exe">    
        <xsl:sort select="@sort"/>
      </xsl:apply-templates>
      <when test="(f,eq,(prop,sel),System Default)">  
        <xsl:choose>
          <xsl:when test="$os='win'">
            <exe>
              <arg>explorer.exe</arg>
              <arg>(file,with)</arg>
            </exe>      
          </xsl:when>
          <xsl:when test="$os='mac'">
            <exe>
          	  <arg>open</arg>
          	  <arg>(file,with)</arg>
            </exe>	     
          </xsl:when>
          <xsl:when test="$os='linux'">
            <exe>
          	  <arg>nautilus</arg>
          	  <arg>(file,with)</arg>
            </exe>
          </xsl:when>
        </xsl:choose>
      </when>         
      <otherwise>
        <error>There is a serious problem</error>
      </otherwise>   
  </choose>
  
</xsl:template>

<xsl:template match="programm" mode="exe">
  <when>
    <xsl:attribute name="test">
      <xsl:text>(f,eq,(prop,sel),</xsl:text>
      <xsl:value-of select="@title" />
      <xsl:text>)</xsl:text>
    </xsl:attribute>   
    <xsl:choose>
      <xsl:when test="$os='win'">
        <exe>
          <arg><xsl:value-of select="@pgm_file" /></arg>
          <arg>(file,with)</arg>
        </exe>      
      </xsl:when>
      <xsl:when test="$os='mac'">
        <exe>
      	  <arg>open</arg>
      	  <arg>-a</arg>
      	  <arg><xsl:value-of select="@pgm_app" /></arg>
      	  <arg>(file,with)</arg>
        </exe>      
      </xsl:when>
      <xsl:when test="$os='linux'">
        <exe>
          <arg><xsl:value-of select="@pgm_file" /></arg>
          <arg>(file,with)</arg>
        </exe>  
      </xsl:when>
    </xsl:choose>
  </when>   
</xsl:template>

<xsl:template match="programm" mode="tk_sel">
  <sel><xsl:value-of select="@title" /></sel>
</xsl:template>

<!-- <job id="View Cities" level="app" >  -->
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
    <xsl:text>,node(entity:bo)(entity:bc)))</xsl:text>   
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
          
          <!-- loop <ti x="(f,html,b,o)Name(f,html,b,c)" width="100px" />  -->
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
        <xsl:text>(f,html,b,o)</xsl:text>
        <xsl:value-of select="//fields/@name_expression_loop" />
        <xsl:text>(f,html,b,c)</xsl:text>
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
  <!-- <ti x="(f,when,(f,exists,(loop,i,@name)),(f,substring,(loop,i,@name),0,30),?)" width="100px" />  
  <ti x="(f,when,(f,exists,(loop,i,@name)),(loop,i,@name),?)" width="100px" />
  -->
  <xsl:if test="@view_flag=1">
     <ti>
        <xsl:attribute name="x">
          <xsl:choose>
            <xsl:when test="string-length(@view_nbr_characters)>0">
              <xsl:text>(f,when,(f,exists,(loop,i,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>)),(f,substring,(loop,i,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>),0,</xsl:text>
              <xsl:value-of select="@view_nbr_characters" />
              <xsl:text>),?)</xsl:text>
            </xsl:when>
            <xsl:when test="string-length(@number_format)>0">
              <xsl:text>(f,when,(f,exists,(loop,i,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>)),(f,format,(loop,i,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>),</xsl:text>
              <xsl:value-of select="@number_format" />
              <xsl:text>),?)</xsl:text>
            </xsl:when>            
            <xsl:when test="@type='bool'"> 
              <xsl:text>(f,when,(f,exists,(loop,i,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>)),(f,when,(loop,i,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>),Yes,No),?)</xsl:text>          
            </xsl:when>   
            <xsl:otherwise>  
              <xsl:text>(f,when,(f,exists,(loop,i,@</xsl:text>
              <xsl:value-of select="@id" />
              <xsl:text>)),(loop,i,@</xsl:text>
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
  <!-- loop <ti x="(f,html,b,o)Name(f,html,b,c)" width="100px" />  -->
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

  