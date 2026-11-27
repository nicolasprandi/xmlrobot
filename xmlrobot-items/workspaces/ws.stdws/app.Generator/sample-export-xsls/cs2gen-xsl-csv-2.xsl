<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias">
<xsl:namespace-alias stylesheet-prefix="axsl" result-prefix="xsl"/>  
<xsl:output method="xml" indent="no"/> 

<xsl:template match="/">

  <axsl:stylesheet version="1.0"> 
    <axsl:output method="text"/><xsl:call-template name="lf"/>
    <axsl:template match="/">
      <axsl:apply-templates select="root"/>
    </axsl:template><xsl:call-template name="lf"/>
    <axsl:template match="root">
      <!-- <xsl:element name="{translate(cs/@sheet_name,' ','')}"> -->
      <axsl:text><xsl:apply-templates select="cs/coltype" mode="first"/></axsl:text><axsl:text><xsl:text>
</xsl:text></axsl:text>      
        <axsl:apply-templates select="row"/>    
    </axsl:template><xsl:call-template name="lf"/>      
    <xsl:comment>Begin of line</xsl:comment><xsl:call-template name="lf"/>  
    <axsl:template match="row">  
      <xsl:apply-templates select="cs/coltype" mode="second"/><axsl:text><xsl:text>
</xsl:text></axsl:text>

</axsl:template><xsl:call-template name="lf"/>   
    <xsl:comment>esc_csv, personal field value, is is done for every value: value or null</xsl:comment><xsl:call-template name="lf"/>    
    <axsl:template name="esc_csv"><xsl:call-template name="lf"/>     
    <xsl:text>  </xsl:text>  <axsl:param name="x"/><xsl:call-template name="lf"/>       
    <xsl:text>  </xsl:text>  <axsl:choose><xsl:call-template name="lf"/>       
    <xsl:text>    </xsl:text>  <axsl:when test="string-length($x)&gt;0"><xsl:call-template name="lf"/>
	<xsl:text>      </xsl:text>  <axsl:choose><xsl:call-template name="lf"/>
	<xsl:text>      </xsl:text>  <axsl:when test="contains($x,'&quot;') or contains($x,'&#xA;') or contains($x,',')"><xsl:call-template name="lf"/>
    <xsl:text>      </xsl:text>  <xsl:text>  </xsl:text><axsl:text>&quot;</axsl:text><xsl:call-template name="lf"/>
    <xsl:text>        </xsl:text><axsl:call-template name="rep"><xsl:call-template name="lf"/>
    <xsl:text>          </xsl:text><axsl:with-param name="s" select="$x"/><xsl:call-template name="lf"/>
    <xsl:text>          </xsl:text><axsl:with-param name="r">&quot;</axsl:with-param><xsl:call-template name="lf"/>
    <xsl:text>          </xsl:text><axsl:with-param name="w">&quot;&quot;</axsl:with-param><xsl:call-template name="lf"/>
    <xsl:text>        </xsl:text>  </axsl:call-template><xsl:call-template name="lf"/>
    <xsl:text>        </xsl:text>  <axsl:text>&quot;</axsl:text><xsl:call-template name="lf"/>
    <xsl:text>      </xsl:text></axsl:when><xsl:call-template name="lf"/>
	<xsl:text>      </xsl:text>  <axsl:otherwise><xsl:call-template name="lf"/>
	<xsl:text>      </xsl:text><axsl:value-of select="$x"/><xsl:call-template name="lf"/>	
	<xsl:text>      </xsl:text>  </axsl:otherwise><xsl:call-template name="lf"/>
	<xsl:text>      </xsl:text>  </axsl:choose><xsl:call-template name="lf"/>
    <xsl:text>    </xsl:text></axsl:when><xsl:call-template name="lf"/>
    <xsl:text>    </xsl:text><axsl:otherwise><axsl:text></axsl:text></axsl:otherwise><xsl:call-template name="lf"/>
    <xsl:text>  </xsl:text></axsl:choose><xsl:call-template name="lf"/>      
</axsl:template><xsl:call-template name="lf"/>
    
    <!-- Named template r for replacement -->
    <xsl:call-template name="get-rep" />

  
  </axsl:stylesheet>
</xsl:template>
<!-- here comes the rep template -->
<xsl:include href="rep-template.xsl"/>

<xsl:template name="lf">
<xsl:text>
</xsl:text>
</xsl:template>

<xsl:template match="coltype" mode="first">
  <xsl:value-of select="@name"/>
  <xsl:if test="not(position()=last())">
    <xsl:text>,</xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="coltype" mode="second">
<xsl:text>
</xsl:text>
  <xsl:comment><xsl:value-of select="@name"/></xsl:comment>
  <axsl:call-template name="esc_csv">
      <axsl:with-param name="x">
        <xsl:attribute name="select">@<xsl:value-of select="@name"/></xsl:attribute>
      </axsl:with-param>                             
  </axsl:call-template>  
  <xsl:if test="not(position()=last())"><axsl:text>,</axsl:text></xsl:if>
</xsl:template>

</xsl:stylesheet>

  
