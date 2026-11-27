<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2019 Nicolas Prandstaetter. All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:param name="dest"/>
<xsl:output method="xml"/>

<!-- *****************          **************** -->
<xsl:template match="/">
  <jobsteps>
    <xsl:apply-templates select="//source"/>
  </jobsteps>
</xsl:template>

<!-- ******************************************* -->
<xsl:template match="source">

    <tp><xsl:text>Copying </xsl:text>
    	  <xsl:call-template name="oloxo">
	        <xsl:with-param name="x">
	        <xsl:value-of select="@name"/><xsl:text> </xsl:text><xsl:value-of select="@includes"/> <xsl:if test="string-length(@excludes) &gt; 0"><xsl:text> </xsl:text>, <xsl:text>excludes </xsl:text><xsl:value-of select="@excludes"/></xsl:if> <xsl:text>...</xsl:text>
	        </xsl:with-param>
	      </xsl:call-template></tp>
	       
    <prop id="dir_rel">
      <xsl:value-of select="@dir_rel" />
    </prop>

    <prop id="name">
        <xsl:call-template name="oloxo">
	        <xsl:with-param name="x"><xsl:value-of select="@name" /></xsl:with-param>
	      </xsl:call-template></prop>
    <try_catch>
    <try>    
    <ant>  
      <copy preservelastmodified="true">
        <xsl:attribute name="todir">
          <xsl:text>(f,dir,classic,</xsl:text>
          <xsl:value-of select="$dest"/>
          <xsl:text>(prop,name)</xsl:text>
          <xsl:text>)</xsl:text>
        </xsl:attribute>
        <fileset dir="(f,dir,classic,(prop,dir_rel))">        
          <xsl:if test="string-length(@excludes) &gt; 0">
            <xsl:copy-of select="@excludes"/>
            <xsl:attribute name="excludes">
              <xsl:call-template name="oloxo">
	             <xsl:with-param name="x"><xsl:value-of select="@excludes" /></xsl:with-param>
	            </xsl:call-template>            
            </xsl:attribute>            
          </xsl:if>
          <xsl:if test="string-length(@includes) &gt; 0">
            <xsl:attribute name="includes">
              <xsl:call-template name="oloxo">
	             <xsl:with-param name="x"><xsl:value-of select="@includes" /></xsl:with-param>
	            </xsl:call-template>  
            </xsl:attribute> 
          </xsl:if>          
        </fileset>        
      </copy>  
    </ant>
    </try>
    <catch>
      <ti label="Problem in doing copy">
          <ti label="Name:">(prop,name)</ti>
          <ti label="Directory:" type="dir" x="(prop,dir_rel)"/>
          <xsl:if test="string-length(@excludes) &gt; 0">
            <ti label="Excludes:">              <xsl:call-template name="oloxo">
	             <xsl:with-param name="x"><xsl:value-of select="@excludes" /></xsl:with-param>
	            </xsl:call-template>  </ti>
          </xsl:if>
          <xsl:if test="string-length(@includes) &gt; 0">
            <ti label="Includes:">              <xsl:call-template name="oloxo">
	             <xsl:with-param name="x"><xsl:value-of select="@includes" /></xsl:with-param>
	            </xsl:call-template>  </ti>
          </xsl:if> 
          <ti label="Error:"  x="(prop,catched_msg)"/> 
          <ti label="Error Class:"  x="(prop,catched_class)"/>          
      </ti>
    </catch>
    </try_catch>

</xsl:template>


<!-- Basic template for entity-replacement xmlrobot -->
<xsl:template name="oloxo">
	<xsl:param name="x"/>
		<xsl:if test="string-length($x) &gt; 0">
    	<xsl:call-template name="oloxo2">
    		<xsl:with-param name="x" select="$x"/>
    		<xsl:with-param name="o">
    		  <xsl:choose>
    		    <xsl:when test="contains($x, '(')">
    		      <xsl:value-of select="string-length(substring-before($x, '('))"/>
    		    </xsl:when>
    		    <xsl:otherwise>
    		      <xsl:text>-1</xsl:text>
    		    </xsl:otherwise>
    		  </xsl:choose>
    		</xsl:with-param>
    		<xsl:with-param name="c">
    		  <xsl:choose>
    		    <xsl:when test="contains($x, ')')">
    		      <xsl:value-of select="string-length(substring-before($x, ')'))"/>
    		    </xsl:when>
    		    <xsl:otherwise>
    		      <xsl:text>-1</xsl:text>
    		    </xsl:otherwise>
    		  </xsl:choose>
    		</xsl:with-param>    
    		<xsl:with-param name="s">
    		  <xsl:choose>
    		    <xsl:when test="contains($x, ',')">
    		      <xsl:value-of select="string-length(substring-before($x, ','))"/>
    		    </xsl:when>
    		    <xsl:otherwise>
    		      <xsl:text>-1</xsl:text>
    		    </xsl:otherwise>
    		  </xsl:choose>
    		</xsl:with-param>          		
    	</xsl:call-template>
		</xsl:if>	
</xsl:template>

<xsl:template name="oloxo2">
	<xsl:param name="x"/>
	<xsl:param name="o"/>
	<xsl:param name="c"/>
	<xsl:param name="s"/>
	<!-- 
	<xsl:text>x=</xsl:text><xsl:value-of select="$x"/>
	<xsl:text> o=</xsl:text><xsl:value-of select="$o"/>
	<xsl:text> s=</xsl:text><xsl:value-of select="$s"/>	
	<xsl:text> c=</xsl:text><xsl:value-of select="$c"/>-->
	
	<xsl:choose>
	 <xsl:when test="$o &gt; -1 and $s &gt; -1 and $c &gt; -1">
	   <!--<xsl:comment>case 1</xsl:comment>-->
	   <xsl:choose>
	     <xsl:when test="$o &lt; $s and $o &lt; $c ">
	       <xsl:call-template name="oloxo_o">
	         <xsl:with-param name="x" select="$x"/>
	         <xsl:with-param name="p" select="$o"/>
	       </xsl:call-template>
	     </xsl:when>
	     <xsl:when test="$s &lt; $o and $s &lt; $c ">
	       <xsl:call-template name="oloxo_s">
	         <xsl:with-param name="x" select="$x"/>
	         <xsl:with-param name="p" select="$s"/>
	       </xsl:call-template>
	     </xsl:when>	
	     <xsl:when test="$c &lt; $o and $c &lt; $s ">
	       <xsl:call-template name="oloxo_c">
	         <xsl:with-param name="x" select="$x"/>
	         <xsl:with-param name="p" select="$c"/>
	       </xsl:call-template>
	     </xsl:when>            
     	     
	   </xsl:choose>
	 </xsl:when>
	 <xsl:when test="$o &gt; -1 and $s &gt; -1 and $c = -1">
	   <!--<xsl:comment>case 2</xsl:comment>-->
	   <xsl:choose>
	     <xsl:when test="$o &lt; $s ">
	       <xsl:call-template name="oloxo_o">
	         <xsl:with-param name="x" select="$x"/>
	         <xsl:with-param name="p" select="$o"/>
	       </xsl:call-template>
	     </xsl:when>
	     <xsl:otherwise>
	       <xsl:call-template name="oloxo_s">
	         <xsl:with-param name="x" select="$x"/>
	         <xsl:with-param name="p" select="$s"/>
	       </xsl:call-template>
	     </xsl:otherwise>	          	     
	   </xsl:choose>	   
	 </xsl:when>	 
	 <xsl:when test="$o &gt; -1 and $s = -1 and $c &gt; -1">
	   <!--<xsl:comment>case 3</xsl:comment>-->
	   <xsl:choose>
	     <xsl:when test="$o &lt; $c ">
	       <xsl:call-template name="oloxo_o">
	         <xsl:with-param name="x" select="$x"/>
	         <xsl:with-param name="p" select="$o"/>
	       </xsl:call-template>
	     </xsl:when>
	     <xsl:otherwise>
	       <xsl:call-template name="oloxo_c">
	         <xsl:with-param name="x" select="$x"/>
	         <xsl:with-param name="p" select="$c"/>
	       </xsl:call-template>
	     </xsl:otherwise>	   	
      </xsl:choose>   
	 </xsl:when>	
	 <xsl:when test="$o &gt; -1 and $s = -1 and $c = -1">
	   <!--<xsl:comment>case 4</xsl:comment>-->
	       <xsl:call-template name="oloxo_o">
	         <xsl:with-param name="x" select="$x"/>
	         <xsl:with-param name="p" select="$o"/>
	       </xsl:call-template>   	   
	 </xsl:when>	 
	 <xsl:when test="$o = -1 and $s &gt; -1 and $c &gt; -1">
	   <!--<xsl:comment>case 5</xsl:comment>-->
	   <xsl:choose>
	     <xsl:when test="$s &lt; $c ">
	       <xsl:call-template name="oloxo_s">
	         <xsl:with-param name="x" select="$x"/>
	         <xsl:with-param name="p" select="$s"/>
	       </xsl:call-template>
	     </xsl:when>
	     <xsl:otherwise>
	       <xsl:call-template name="oloxo_c">
	         <xsl:with-param name="x" select="$x"/>
	         <xsl:with-param name="p" select="$c"/>
	       </xsl:call-template>
	     </xsl:otherwise>	   	
      </xsl:choose>   	   
	 </xsl:when>	 
	 <xsl:when test="$o = -1 and $s &gt; -1 and $c = -1">
	   <!--<xsl:comment>case 6</xsl:comment>-->
	       <xsl:call-template name="oloxo_s">
	         <xsl:with-param name="x" select="$x"/>
	         <xsl:with-param name="p" select="$s"/>
	       </xsl:call-template>	   
	 </xsl:when>	 
	 <xsl:when test="$o = -1 and $s = -1 and $c &gt; -1">
	   <!--<xsl:comment>case 7</xsl:comment>-->
	       <xsl:call-template name="oloxo_c">
	         <xsl:with-param name="x" select="$x"/>
	         <xsl:with-param name="p" select="$c"/>
	       </xsl:call-template>	  	   
	 </xsl:when>	 
	 <xsl:when test="$o = -1 and $s = -1 and $c = -1">
	   <!--<xsl:comment>case 8</xsl:comment>-->
	   <xsl:value-of select="$x"/>
	 </xsl:when>	                 
	
	</xsl:choose>

</xsl:template>

<xsl:template name="oloxo_o">
	<xsl:param name="x"/>
	<xsl:param name="p"/>
	<xsl:value-of select="substring($x,0,$p+1)"/>
	<xsl:text>(e:o)</xsl:text>
  <xsl:call-template name="oloxo">
    <xsl:with-param name="x" select="substring($x,$p+2)"/>
  </xsl:call-template>	
</xsl:template>

<xsl:template name="oloxo_s">
	<xsl:param name="x"/>
	<xsl:param name="p"/>
	<xsl:value-of select="substring($x,0,$p+1)"/>
	<xsl:text>(e:s)</xsl:text>
  <xsl:call-template name="oloxo">
    <xsl:with-param name="x" select="substring($x,$p+2)"/>
  </xsl:call-template>
</xsl:template>

<xsl:template name="oloxo_c">
	<xsl:param name="x"/>
	<xsl:param name="p"/>
	<xsl:value-of select="substring($x,0,$p+1)"/>
	<xsl:text>(e:c)</xsl:text>
  <xsl:call-template name="oloxo">
    <xsl:with-param name="x" select="substring($x,$p+2)"/>
  </xsl:call-template>
</xsl:template>

</xsl:stylesheet>

  