<?xml version="1.0" encoding="UTF-8"?>

<!-- Copyright © 2015-2017 Nicolas Prandstaetter.  All rights reserved.
 -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
<xsl:param name="obj_typ" />
<xsl:param name="obj_tech_name2"/>
<xsl:param name="view_prefix" />
<xsl:param name="mv_prefix" />
<xsl:param name="mv_style" />
<xsl:param name="dwh_prefix" />
<xsl:param name="dbms" />

<xsl:output method="xml"/>

<xsl:include href="include-mv-naming.xsl" />

<xsl:template match="/">
<us>
<xsl:apply-templates select="root/ots/ot[@obj_typ=$obj_typ]"/>
</us> 
</xsl:template>

<xsl:template match="ot">
  <xsl:call-template name="ot2">
    <xsl:with-param name="obj_tech_name" select="@tech_name"/>
    <xsl:with-param name="obj_typ" select="$obj_typ"/>
  </xsl:call-template>
</xsl:template>

<xsl:template name="ot2">
  <xsl:param name="obj_tech_name"/>
  <xsl:param name="obj_typ"/>
  <u>DROP VIEW IF EXISTS <xsl:value-of select="$view_prefix" />1_<xsl:call-template name="get_name_mv" /> CASCADE;</u>
  <u>CREATE VIEW <xsl:value-of select="$view_prefix" />1_<xsl:call-template name="get_name_mv" /> AS select
  o.obj_id,
<xsl:apply-templates select="//au[@obj_typ=$obj_typ]" mode="anton"/>
  from (
    select 
      obj.obj_id
    from <xsl:value-of select="$dwh_prefix" />obj
    where obj_typ=<xsl:value-of select="$obj_typ" /> ) o
    <xsl:apply-templates select="//au[@obj_typ=$obj_typ]" mode="berta"/>
    <xsl:text>;</xsl:text></u>                    
    <u>DROP VIEW IF EXISTS <xsl:value-of select="$view_prefix" />2_<xsl:call-template name="get_name_mv" /> CASCADE;</u>
    <u>CREATE VIEW <xsl:value-of select="$view_prefix" />2_<xsl:call-template name="get_name_mv" /> AS select obj_id,
    <xsl:apply-templates select="//au[@obj_typ=$obj_typ]" mode="mv1"/>
      from <xsl:value-of select="$view_prefix" />1_<xsl:call-template name="get_name_mv" />;
    </u>
  
  <xsl:choose>
    <xsl:when test="$dbms='postgres'">
      <!-- postgres -->
      <u>DROP MATERIALIZED VIEW IF EXISTS <xsl:value-of select="$mv_prefix" /><xsl:call-template name="get_name_mv" /> CASCADE;</u> 
      
      <u>CREATE MATERIALIZED VIEW <xsl:value-of select="$mv_prefix" /><xsl:call-template name="get_name_mv" /> AS
      select
        obj_id,
      <xsl:apply-templates select="//au[@obj_typ=$obj_typ]" mode="mv_B"/>
        from <xsl:value-of select="$view_prefix" />2_<xsl:call-template name="get_name_mv" />;</u>
    </xsl:when>
    <xsl:otherwise>
      <!-- mysql -->
      <u>DROP TABLE IF EXISTS <xsl:value-of select="$mv_prefix" /><xsl:call-template name="get_name_mv" /> CASCADE;</u>   
      
      <u>CREATE TABLE <xsl:value-of select="$mv_prefix" /><xsl:call-template name="get_name_mv" />(
        obj_id INTEGER NOT NULL,
      <xsl:apply-templates select="//au[@obj_typ=$obj_typ]" mode="mv_C"/>
      <xsl:text>,
   PRIMARY KEY (obj_id));</xsl:text></u>
      <u><xsl:text>insert into </xsl:text><xsl:value-of select="$mv_prefix" /><xsl:call-template name="get_name_mv" /><xsl:text> select * from </xsl:text><xsl:value-of select="$view_prefix" />2_<xsl:call-template name="get_name_mv" /><xsl:text>;</xsl:text></u>         
    </xsl:otherwise>
  </xsl:choose>
    

</xsl:template>  

<!-- mv_C -->

<xsl:template match="au" mode="mv_C">
 <xsl:call-template name="mvcol_1_C">
  <xsl:with-param name="attr_typ"><xsl:value-of select="@attr_typ"/></xsl:with-param>
 </xsl:call-template>   
 <xsl:if test="not (position()=last())"><xsl:text>,
</xsl:text></xsl:if>
</xsl:template>

<xsl:template name="mvcol_1_C">
  <xsl:param name="attr_typ"/>
  <xsl:apply-templates select="//at[@attr_typ=$attr_typ]" mode="mv2_C"/>
</xsl:template>



<xsl:template match="at" mode="mv2_C">
 <xsl:call-template name="mvcol_2_C">
  <xsl:with-param name="attr_tech_name"><xsl:call-template name="get_name_field" /></xsl:with-param>
  <xsl:with-param name="final_typ"><xsl:value-of select="@final_typ"/></xsl:with-param>
  <xsl:with-param name="fp1"><xsl:value-of select="@fp1"/></xsl:with-param>
  <xsl:with-param name="fp2"><xsl:value-of select="@fp2"/></xsl:with-param>
  <xsl:with-param name="vartype"><xsl:value-of select="@vartype"/></xsl:with-param>
 </xsl:call-template>  
</xsl:template>

<xsl:template name="mvcol_2_C">
  <xsl:param name="attr_tech_name"/>
  <xsl:param name="final_typ"/>
  <xsl:param name="fp1"/>
  <xsl:param name="fp2"/> 
  <xsl:param name="vartype"/>     
  <xsl:choose>
    <xsl:when test="string-length($final_typ)&gt;0">

    <xsl:call-template name="ft_mv3_C">
      <xsl:with-param name="attr_tech_name"><xsl:value-of select="$attr_tech_name"/></xsl:with-param>
      <xsl:with-param name="fp1"><xsl:value-of select="$fp1"/></xsl:with-param>
      <xsl:with-param name="fp2"><xsl:value-of select="$fp2"/></xsl:with-param>
      <xsl:with-param name="vartype"><xsl:value-of select="$vartype"/></xsl:with-param>
    </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="$attr_tech_name"/><xsl:text>  VARCHAR(60)</xsl:text>      
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="ft_mv3_C">
  <xsl:param name="attr_tech_name"/>
  <xsl:param name="fp1"/>
  <xsl:param name="fp2"/>
  <xsl:param name="vartype"/> 
  
  <xsl:choose>
    <xsl:when test="$dbms='postgres'">    
    </xsl:when>  
    <xsl:otherwise>
      <!-- mysql isso-->
      
  <xsl:value-of select="$attr_tech_name"/>
    <xsl:text> </xsl:text>       
      <xsl:choose>
        <xsl:when test="$vartype='char' and string-length($fp1)&gt;0">
          <xsl:text>  CHAR(</xsl:text><xsl:value-of select="$fp1"/><xsl:text>)</xsl:text>
        </xsl:when>    
        <xsl:when test="$vartype='char'">
          <xsl:text>  CHAR(60)</xsl:text>
        </xsl:when> 
        <xsl:when test="$vartype='varchar' and string-length($fp1)&gt;0">
          <xsl:text>  VARCHAR(</xsl:text><xsl:value-of select="$fp1"/><xsl:text>)</xsl:text>
        </xsl:when>  
        <xsl:when test="$vartype='varchar'">
          <xsl:text>  VARCHAR(60)</xsl:text>
        </xsl:when>  
       
        <xsl:when test="$vartype='integer'">
          <xsl:text>  DECIMAL (15,0)</xsl:text>  <!-- war mal INT aber das hat maria-probleme -->
        </xsl:when>   
        <xsl:when test="$vartype='bool'">
          <xsl:text>  TINYINT</xsl:text>
        </xsl:when>      
        <xsl:when test="$vartype='number'">  
          <xsl:text>  DECIMAL (15,4)</xsl:text>
        </xsl:when>    
        <xsl:when test="$vartype='date'">  
          <xsl:text>  DATE</xsl:text>
        </xsl:when>   
        
        <xsl:when test="$vartype='datetime'">  
          <xsl:text>  DATETIME</xsl:text>
        </xsl:when>   
        
                
            
        <xsl:otherwise>
          <xsl:text>  VARCHAR(60)</xsl:text>
        </xsl:otherwise>              
        
      </xsl:choose>      
    </xsl:otherwise>
  </xsl:choose>



</xsl:template>

<!-- mv_B -->

<xsl:template match="au" mode="mv_B">
 <xsl:call-template name="mvcol_1_B">
  <xsl:with-param name="attr_typ"><xsl:value-of select="@attr_typ"/></xsl:with-param>
 </xsl:call-template>   
 <xsl:if test="not (position()=last())"><xsl:text>,
</xsl:text></xsl:if>
</xsl:template>

<xsl:template name="mvcol_1_B">
  <xsl:param name="attr_typ"/>
  <xsl:apply-templates select="//at[@attr_typ=$attr_typ]" mode="mv2_B"/>
</xsl:template>

<xsl:template match="at" mode="mv2_B">
  <xsl:text>  </xsl:text><xsl:value-of select="@tech_name"/>  
</xsl:template>



<!-- mv1 -->

<xsl:template match="au" mode="mv1">
 <xsl:call-template name="mvcol_1">
  <xsl:with-param name="attr_typ"><xsl:value-of select="@attr_typ"/></xsl:with-param>
 </xsl:call-template>   
 <xsl:if test="not (position()=last())"><xsl:text>,
</xsl:text></xsl:if>
</xsl:template>

<xsl:template name="mvcol_1">
  <xsl:param name="attr_typ"/>
  <xsl:apply-templates select="//at[@attr_typ=$attr_typ]" mode="mv2"/>
</xsl:template>

<xsl:template match="at" mode="mv2">
 <xsl:call-template name="mvcol_2">
  <xsl:with-param name="attr_tech_name"><xsl:value-of select="@tech_name"/></xsl:with-param>
  <xsl:with-param name="final_typ"><xsl:value-of select="@final_typ"/></xsl:with-param>
  <xsl:with-param name="fp1"><xsl:value-of select="@fp1"/></xsl:with-param>
  <xsl:with-param name="fp2"><xsl:value-of select="@fp2"/></xsl:with-param>
  <xsl:with-param name="vartype"><xsl:value-of select="@vartype"/></xsl:with-param>
 </xsl:call-template>  
</xsl:template>

<xsl:template name="mvcol_2">
  <xsl:param name="attr_tech_name"/>
  <xsl:param name="final_typ"/>
  <xsl:param name="fp1"/>
  <xsl:param name="fp2"/>
  <xsl:param name="vartype"/>      
  <xsl:choose>
    <xsl:when test="string-length($final_typ)&gt;0">
    <xsl:call-template name="ft_mv3">
      <xsl:with-param name="attr_tech_name"><xsl:value-of select="$attr_tech_name"/></xsl:with-param>
      <xsl:with-param name="fp1"><xsl:value-of select="$fp1"/></xsl:with-param>
      <xsl:with-param name="fp2"><xsl:value-of select="$fp2"/></xsl:with-param>
      <xsl:with-param name="vartype"><xsl:value-of select="$vartype"/></xsl:with-param>
    </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
      <xsl:text>  </xsl:text><xsl:call-template name="get_name_field" />
      <xsl:text> as </xsl:text>
      <xsl:call-template name="get_name_field" />      
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="ft_mv3">
  <xsl:param name="attr_tech_name"/>
  <xsl:param name="fp1"/>
  <xsl:param name="fp2"/>                    
  <xsl:param name="vartype"/>
  
  <xsl:choose>
    <xsl:when test="$dbms='postgres'">
      <xsl:choose>
        <xsl:when test="$vartype='char' and string-length($fp1)&gt;0">
          <xsl:text>  left(</xsl:text><xsl:call-template name="get_name_field" /><xsl:text>,</xsl:text><xsl:value-of select="$fp1"/><xsl:text>)</xsl:text>
        </xsl:when>    
        <xsl:when test="$vartype='char'">
          <xsl:text>  </xsl:text><xsl:call-template name="get_name_field" />
        </xsl:when> 
        <xsl:when test="$vartype='varchar' and string-length($fp1)&gt;0">
          <xsl:text>  trim(from left(</xsl:text><xsl:call-template name="get_name_field" /><xsl:text>,</xsl:text><xsl:value-of select="$fp1"/><xsl:text>))</xsl:text>
        </xsl:when>  
        <xsl:when test="$vartype='varchar'">
          <xsl:text>  trim(from </xsl:text><xsl:call-template name="get_name_field" /><xsl:text>)</xsl:text>
        </xsl:when>  
        <xsl:when test="$vartype='integer'">
          <xsl:text>  cast(floor(cast(</xsl:text><xsl:call-template name="get_name_field" /><xsl:text> as double precision)) as integer)</xsl:text>
        </xsl:when>  
        <xsl:when test="$vartype='bool'">
          <xsl:text>  cast(floor(cast(</xsl:text><xsl:call-template name="get_name_field" /><xsl:text> as double precision)) as integer)</xsl:text>
        </xsl:when>      
        <xsl:when test="$vartype='number'">  
          <xsl:text>  to_number (</xsl:text><xsl:call-template name="get_name_field" /><xsl:text> ,&apos;S000000000000000.0000&apos;)</xsl:text>
        </xsl:when>    
        <xsl:when test="$vartype='date'">  
          <xsl:text>  to_date(</xsl:text><xsl:call-template name="get_name_field" /><xsl:text>,'YYYY-MM-DD')</xsl:text>
        </xsl:when>   
        <xsl:when test="$vartype='datetime'">  
          <xsl:text>  to_timestamp(</xsl:text><xsl:call-template name="get_name_field" /><xsl:text>,'YYYY-MM-DD HH24:MI:SS.MS')</xsl:text>
        </xsl:when>           
            
        <xsl:otherwise>
          <xsl:text>  </xsl:text><xsl:call-template name="get_name_field" />
        </xsl:otherwise>              
        
      </xsl:choose>    
    </xsl:when>  
    <xsl:otherwise>
      <!-- mysql isso-->
      <xsl:choose>
        <xsl:when test="$vartype='char' and string-length($fp1)&gt;0">
          <xsl:text>  LEFT(</xsl:text><xsl:call-template name="get_name_field" /><xsl:text>,</xsl:text><xsl:value-of select="$fp1"/><xsl:text>)</xsl:text>
        </xsl:when>    
        <xsl:when test="$vartype='char'">
          <xsl:text>  </xsl:text><xsl:call-template name="get_name_field" />
        </xsl:when> 
        <xsl:when test="$vartype='varchar' and string-length($fp1)&gt;0">
          <xsl:text>  TRIM(LEFT(</xsl:text><xsl:call-template name="get_name_field" /><xsl:text>,</xsl:text><xsl:value-of select="$fp1"/><xsl:text>))</xsl:text>
        </xsl:when>  
        <xsl:when test="$vartype='varchar'">
          <xsl:text>  TRIM(</xsl:text><xsl:call-template name="get_name_field" /><xsl:text>)</xsl:text>
        </xsl:when>  
         
        <xsl:when test="$vartype='integer'">
          <xsl:text>  CAST(SUBSTRING(</xsl:text><xsl:call-template name="get_name_field" /><xsl:text>,1,21) AS DECIMAL)</xsl:text>
        </xsl:when>   
        <xsl:when test="$vartype='bool'">
          <xsl:text>  CAST(SUBSTRING(</xsl:text><xsl:call-template name="get_name_field" /><xsl:text>,1,1) AS UNSIGNED)</xsl:text>
        </xsl:when>      
        
        <xsl:when test="$vartype='number'">  

          <xsl:text>  CAST(SUBSTRING(</xsl:text><xsl:call-template name="get_name_field" /><xsl:text>,1,21) AS DECIMAL)</xsl:text>
        
        </xsl:when>    
        
        <xsl:when test="$vartype='date'">  
          <xsl:text>  CAST(</xsl:text><xsl:call-template name="get_name_field" /><xsl:text> AS DATE)</xsl:text>
        </xsl:when>
         
        <xsl:when test="$vartype='datetime'">  
          <xsl:text>  CAST(SUBSTRING(</xsl:text><xsl:call-template name="get_name_field" /><xsl:text>,1,19) AS DATETIME)</xsl:text>
        </xsl:when>   
                
            
        <xsl:otherwise>
          <xsl:text>  </xsl:text><xsl:call-template name="get_name_field" />
        </xsl:otherwise>              
        
      </xsl:choose>      
    </xsl:otherwise>
  </xsl:choose>

  <xsl:text> as </xsl:text>
  <xsl:call-template name="get_name_field" />

</xsl:template>


<xsl:template match="au" mode="anton">
  <xsl:text>  </xsl:text><xsl:call-template name="get_name_field_au" />.inhalt as <xsl:call-template name="get_name_field_au" /><xsl:if test="not (position()=last())">,
</xsl:if>
</xsl:template>

<xsl:template match="au" mode="berta">
  left outer join (
    select
      wert.inhalt,
      attr.obj_id
    from 
      <xsl:value-of select="$dwh_prefix" />attr as attr,
      <xsl:value-of select="$dwh_prefix" />wert as wert
    where
      attr.attr_typ = <xsl:value-of select="@attr_typ" /> and
      attr.attr_id = wert.attr_id and
      wert.ist_top = 1 ) <xsl:call-template name="get_name_field_au" />
    on 
      ( o.obj_id = <xsl:call-template name="get_name_field_au" />.obj_id )</xsl:template>


</xsl:stylesheet>

  
