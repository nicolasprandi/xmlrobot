<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Basic template for entity-replacement xmlrobot -->
<xsl:template name="esc_xr">
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
  <xsl:call-template name="esc_xr">
    <xsl:with-param name="x" select="substring($x,$p+2)"/>
  </xsl:call-template>	
</xsl:template>

<xsl:template name="oloxo_s">
	<xsl:param name="x"/>
	<xsl:param name="p"/>
	<xsl:value-of select="substring($x,0,$p+1)"/>
	<xsl:text>(e:s)</xsl:text>
  <xsl:call-template name="esc_xr">
    <xsl:with-param name="x" select="substring($x,$p+2)"/>
  </xsl:call-template>
</xsl:template>

<xsl:template name="oloxo_c">
	<xsl:param name="x"/>
	<xsl:param name="p"/>
	<xsl:value-of select="substring($x,0,$p+1)"/>
	<xsl:text>(e:c)</xsl:text>
  <xsl:call-template name="esc_xr">
    <xsl:with-param name="x" select="substring($x,$p+2)"/>
  </xsl:call-template>
</xsl:template>


</xsl:stylesheet>

  