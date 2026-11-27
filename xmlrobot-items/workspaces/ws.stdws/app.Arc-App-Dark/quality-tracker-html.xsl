<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="html" />
<xsl:param name="title"/>
<xsl:param name="apphome"/>
<xsl:template match="/">
<html>
<head>
<title><xsl:value-of select="$title" /></title>
</head>
<body>
<h1>Application: <xsl:value-of select="$title" /></h1>
<h3>1. Items</h3>

<table border="0">

  <tr>
    <td>File</td><td>Job</td><td>calls<br/>=go+ti</td><td>calledbys<br/>=go+ti</td>  
  </tr>
  
<xsl:apply-templates select="track/jobfiles/file" mode="head"/> 
</table>
<hr/>
<h3>2. Cross Reference</h3>
<table border="0">

  <tr>
    <td>File</td><td>Job</td><td colspan="3">References</td><td><i>File</i></td>  
  </tr>
  
<xsl:apply-templates select="track/jobfiles/file" mode="details"/> 
</table>

<hr/>
<h3>3. Best to dereference</h3>
<table border="0">
<tr><td>job</td><td>calledbys</td><td>calls_go</td><td><i>File</i></td></tr>
<xsl:apply-templates select="track/graph/job[@calledbys_ti='0']" mode="best">
<xsl:sort select="@calledbys" order="descending" data-type="number"/>
</xsl:apply-templates> 
</table>
</body>
</html>

</xsl:template>

<xsl:template match="job" mode="best">
<xsl:if test="@calledbys &gt; 0">
<tr><td>
<a>  <xsl:attribute name="href"><xsl:text>#</xsl:text>
    <xsl:value-of select="@id" />
  </xsl:attribute> <xsl:value-of select="@id" /></a>   </td>
<td><xsl:value-of select="@calledbys" /></td> <td><xsl:value-of select="@calls_go" /></td> 

    <td><i>              
      <xsl:call-template name="get-jobfile">
        <xsl:with-param name="job" select="@id" />
      </xsl:call-template></i>   
    </td> 
  </tr>
</xsl:if>  
</xsl:template>

<xsl:template match="file" mode="head">

  <tr><td>
<a>
  <xsl:attribute name="href">
    <xsl:text>#PROLOG: </xsl:text>
    <xsl:value-of select="@name" />
  </xsl:attribute>
  <xsl:value-of select="@name" />
  </a>
  </td></tr>  
  
  <xsl:call-template name="file2-head">
    <xsl:with-param name="name" select="@name" />
  </xsl:call-template>   
      
   
</xsl:template>

<xsl:template name="file2-head" >
    <xsl:param name="name"/>

  <xsl:apply-templates select="/track/graph/job[@file=$name]" mode="jobhead"/>
      
</xsl:template>  

<xsl:template match="job" mode="jobhead">
<tr>
<td></td>
<td>   <b>
<a>
  <xsl:attribute name="href">
  <xsl:text>#</xsl:text>
    <xsl:value-of select="@id" />
  </xsl:attribute>
  
  <xsl:value-of select="@id" />
</a>   </b>
</td> 
<td><b><xsl:value-of select="@calls" /></b><xsl:text>=</xsl:text><xsl:value-of select="@calls_go" /><xsl:text>+</xsl:text><xsl:value-of select="@calls_ti" /></td>
<td><b><xsl:value-of select="@calledbys" /></b><xsl:text>=</xsl:text><xsl:value-of select="@calledbys_go" /><xsl:text>+</xsl:text><xsl:value-of select="@calledbys_ti" /></td>
</tr>


</xsl:template>

<xsl:template match="file" mode="details">

  <tr><td>
<a>
  <xsl:attribute name="id">
    <xsl:text>PROLOG: </xsl:text>
    <xsl:value-of select="@name" />
  </xsl:attribute>
  <xsl:attribute name="href">
    <xsl:value-of select="$apphome" />
    <xsl:value-of select="@name" />
  </xsl:attribute>  
  <xsl:value-of select="@name" />
  </a>
  </td></tr>  
  
  <xsl:call-template name="file2-details">
    <xsl:with-param name="name" select="@name" />
  </xsl:call-template>   
      
   
</xsl:template>

<xsl:template name="file2-details" >
    <xsl:param name="name"/>

  <xsl:apply-templates select="/track/graph/job[@file=$name]" mode="jobmain"/>
      
</xsl:template>    
    
<xsl:template match="job" mode="jobmain">
<tr>
<td></td>
<td>   <b>
<a>
  <xsl:attribute name="id">
    <xsl:value-of select="@id" />
  </xsl:attribute>
  
  <xsl:value-of select="@id" />
</a>   </b>
</td> 
</tr>


<xsl:if test="@calls &gt; 0">

  <xsl:apply-templates select="calls/call" />
  


</xsl:if>

<xsl:if test="@calledbys &gt; 0">
  <xsl:apply-templates select="calledbys/calledby" />
</xsl:if>

   
</xsl:template>

<xsl:template match="call">
  <tr>
    <td></td><td></td>
    <td><xsl:if test="position()=1">Calls:</xsl:if></td>
    <td><a>  <xsl:attribute name="href"><xsl:text>#</xsl:text>
    <xsl:value-of select="@job" />
  </xsl:attribute> <xsl:value-of select="@job" /></a></td><td><xsl:if test="@how='ti'"><xsl:text>TI</xsl:text></xsl:if></td>  
  
    <td><i>              
      <xsl:call-template name="get-jobfile">
        <xsl:with-param name="job" select="@job" />
      </xsl:call-template></i>   
    </td>  
  </tr>
</xsl:template>

<xsl:template match="calledby">
  <tr>
    <td></td><td></td>
    <td><xsl:if test="position()=1">Called by:</xsl:if></td>
    <td><a>  <xsl:attribute name="href"><xsl:text>#</xsl:text>
    <xsl:value-of select="@job" />
  </xsl:attribute> <xsl:value-of select="@job" /></a></td><td><xsl:if test="@how='ti'"><xsl:text>TI</xsl:text></xsl:if></td> 
    <td><i>              
      <xsl:call-template name="get-jobfile">
        <xsl:with-param name="job" select="@job" />
      </xsl:call-template></i>   
    </td>       
  </tr>
</xsl:template>

<xsl:template name="job-j2" >
    <xsl:param name="job"/>
    <xsl:param name="file"/>
  <job>
    <xsl:attribute name="id">
      <xsl:value-of select="$job" />
    </xsl:attribute>  
    <xsl:attribute name="file">
      <xsl:value-of select="$file" />
    </xsl:attribute>      
    <xsl:if test="string-length(@role) &gt; 0">
      <xsl:attribute name="role">
        <xsl:value-of select="@role" />
      </xsl:attribute>     
    </xsl:if>
    <calls>
      <xsl:apply-templates select="/jobfiles/jobfile[@name=$file]/job[@id=$job]//call_job" mode="cj1"/>
      <xsl:apply-templates select="/jobfiles/jobfile[@name=$file]/job[@id=$job]//ti[@type='call_job']" mode="cj2"/>
    </calls>
    
    <calledbys>
      <xsl:apply-templates select="/jobfiles/jobfile/job//call_job[@job=$job]" mode="cbj1"/>
      <xsl:apply-templates select="/jobfiles/jobfile/job//ti[@type='call_job' and @job=$job]" mode="cbj2"/> 
    </calledbys>    
  </job>
</xsl:template>
<xsl:template match="ti" mode="cbj2">
    <calledby>
    <xsl:attribute name="job">
      <xsl:value-of select="ancestor::job/@id" />
    </xsl:attribute>     
    <xsl:attribute name="how"><xsl:text>ti</xsl:text></xsl:attribute>
      
    </calledby>
</xsl:template>
<xsl:template match="call_job" mode="cbj1">
    <calledby>
    <xsl:attribute name="job">
      <xsl:value-of select="ancestor::job/@id" />
    </xsl:attribute>     
    <xsl:attribute name="how"><xsl:text>go</xsl:text></xsl:attribute>
      
    </calledby>
</xsl:template>

<xsl:template match="call_job" mode="cj1">
    <call>
    <xsl:attribute name="job">
      <xsl:value-of select="@job" />
    </xsl:attribute>     
    <xsl:attribute name="how"><xsl:text>go</xsl:text></xsl:attribute>
      
    </call>
</xsl:template>
<xsl:template match="ti" mode="cj2">
    <call>
    <xsl:attribute name="job">
      <xsl:value-of select="@job" />
    </xsl:attribute>      
    <xsl:attribute name="how"><xsl:text>ti</xsl:text></xsl:attribute> 
    </call>
</xsl:template>


<xsl:template name="get-jobfile" >
  <xsl:param name="job"/>
  <xsl:value-of select="/track/graph/job[@id=$job]/@file" />
      
</xsl:template> 

</xsl:stylesheet>

  