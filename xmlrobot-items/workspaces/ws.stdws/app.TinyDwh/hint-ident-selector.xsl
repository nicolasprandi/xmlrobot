<?xml version="1.0" encoding="UTF-8"?>

<!-- Copyright © 2023 Red-Shark-Formation. All rights reserved. -->


<xsl:stylesheet version="1.0"                                                            
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="sel"/>                                    
<xsl:output method="xml"/>
<xsl:template match="/">
<sels>
<xsl:apply-templates select="attrs/attr"/> 
</sels>
</xsl:template>

<xsl:template match="attr">
  <sel>
    <xsl:attribute name="label">
      <xsl:call-template name="attr-name-by-typ">
        <xsl:with-param name="attr_typ"><xsl:value-of select="@attr_typ"/></xsl:with-param>
      </xsl:call-template>
      <xsl:if test="@ident = '1'">
        <xsl:text> *</xsl:text>
      </xsl:if>
      <xsl:text> = [ </xsl:text>
      <xsl:choose>
        <xsl:when test="@final_typ=3 or @final_typ=4">
          <!--
          <xsl:value-of select="@inhalt"/><xsl:text> = </xsl:text>
          <xsl:value-of select="substring(@inhalt,1,16)"/>
          <xsl:text>|</xsl:text><xsl:value-of select="substring(@inhalt,17)"/><xsl:text> =&gt; </xsl:text>      -->
          <xsl:call-template name="vorkomma">
            <xsl:with-param name="vorkomma"><xsl:value-of select="substring(@inhalt,1,16)"/></xsl:with-param>
          </xsl:call-template>       
          <xsl:call-template name="nachkomma">
            <xsl:with-param name="nachkomma"><xsl:value-of select="substring(@inhalt,17)"/></xsl:with-param>
          </xsl:call-template>            
        
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="@inhalt" />
        </xsl:otherwise>
      
      </xsl:choose>
      
      <xsl:text> ] </xsl:text></xsl:attribute><xsl:value-of select="@wert_id" />
  </sel>
</xsl:template>

<xsl:template name="vorkomma">
<xsl:param name="vorkomma"/>  
      <xsl:choose>
        <xsl:when test="starts-with($vorkomma,'+000000000000000')">
          <xsl:text>0</xsl:text>
        </xsl:when>
        <xsl:when test="starts-with($vorkomma,'+00000000000000')">
          <xsl:value-of select="substring($vorkomma,16)"/>
        </xsl:when>    
        <xsl:when test="starts-with($vorkomma,'+0000000000000')">
          <xsl:value-of select="substring($vorkomma,15)"/>
        </xsl:when>    
        <xsl:when test="starts-with($vorkomma,'+000000000000')">
          <xsl:value-of select="substring($vorkomma,14)"/>
        </xsl:when>    
        <xsl:when test="starts-with($vorkomma,'+00000000000')">
          <xsl:value-of select="substring($vorkomma,13)"/>
        </xsl:when>     
        <xsl:when test="starts-with($vorkomma,'+0000000000')">
          <xsl:value-of select="substring($vorkomma,12)"/>
        </xsl:when>   
        <xsl:when test="starts-with($vorkomma,'+000000000')">
          <xsl:value-of select="substring($vorkomma,11)"/>
        </xsl:when>   
        <xsl:when test="starts-with($vorkomma,'+00000000')">
          <xsl:value-of select="substring($vorkomma,10)"/>
        </xsl:when>          
        <xsl:when test="starts-with($vorkomma,'+0000000')">
          <xsl:value-of select="substring($vorkomma,9)"/>
        </xsl:when>    
        <xsl:when test="starts-with($vorkomma,'+000000')">
          <xsl:value-of select="substring($vorkomma,8)"/>
        </xsl:when>   
        <xsl:when test="starts-with($vorkomma,'+00000')">
          <xsl:value-of select="substring($vorkomma,7)"/>
        </xsl:when>  
        <xsl:when test="starts-with($vorkomma,'+0000')">
          <xsl:value-of select="substring($vorkomma,6)"/>
        </xsl:when>    
        <xsl:when test="starts-with($vorkomma,'+000')">
          <xsl:value-of select="substring($vorkomma,5)"/>
        </xsl:when>  
        <xsl:when test="starts-with($vorkomma,'+00')">
          <xsl:value-of select="substring($vorkomma,4)"/>
        </xsl:when>         
        <xsl:when test="starts-with($vorkomma,'+0')">
          <xsl:value-of select="substring($vorkomma,3)"/>
        </xsl:when>    
        <xsl:when test="starts-with($vorkomma,'+')">
          <xsl:value-of select="substring($vorkomma,2)"/>
        </xsl:when>                            
        <xsl:when test="starts-with($vorkomma,'-000000000000000')">
          <xsl:text>0</xsl:text>
        </xsl:when>
        <xsl:when test="starts-with($vorkomma,'-00000000000000')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,16)"/>
        </xsl:when>    
        <xsl:when test="starts-with($vorkomma,'-0000000000000')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,15)"/>
        </xsl:when>    
        <xsl:when test="starts-with($vorkomma,'-000000000000')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,14)"/>
        </xsl:when>    
        <xsl:when test="starts-with($vorkomma,'-00000000000')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,13)"/>
        </xsl:when>     
        <xsl:when test="starts-with($vorkomma,'-0000000000')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,12)"/>
        </xsl:when>   
        <xsl:when test="starts-with($vorkomma,'-000000000')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,11)"/>
        </xsl:when>   
        <xsl:when test="starts-with($vorkomma,'-00000000')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,10)"/>
        </xsl:when>          
        <xsl:when test="starts-with($vorkomma,'-0000000')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,9)"/>
        </xsl:when>    
        <xsl:when test="starts-with($vorkomma,'-000000')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,8)"/>
        </xsl:when>   
        <xsl:when test="starts-with($vorkomma,'-00000')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,7)"/>
        </xsl:when>  
        <xsl:when test="starts-with($vorkomma,'-0000')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,6)"/>
        </xsl:when>    
        <xsl:when test="starts-with($vorkomma,'-000')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,5)"/>
        </xsl:when>  
        <xsl:when test="starts-with($vorkomma,'-00')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,4)"/>
        </xsl:when>         
        <xsl:when test="starts-with($vorkomma,'-0')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,3)"/>
        </xsl:when>    
        <xsl:when test="starts-with($vorkomma,'-')">
          <xsl:text>-</xsl:text><xsl:value-of select="substring($vorkomma,2)"/>
        </xsl:when>                                                                  
        <xsl:otherwise>
          <xsl:value-of select="$vorkomma"/>
        </xsl:otherwise>
      </xsl:choose>   
</xsl:template>


<xsl:template name="nachkomma">
<xsl:param name="nachkomma"/>
  
      <xsl:choose>
        <xsl:when test="substring($nachkomma,1,5) = '.0000'">
        </xsl:when>   
        <xsl:when test="substring($nachkomma,3) = '000'">
           <xsl:value-of select="substring($nachkomma,1,2)"/>
        </xsl:when>  
        <xsl:when test="substring($nachkomma,4) = '00'">
           <xsl:value-of select="substring($nachkomma,1,3)"/>
        </xsl:when>                
        <xsl:when test="substring($nachkomma,5) = '0'">
           <xsl:value-of select="substring($nachkomma,1,4)"/>
        </xsl:when>     
     
                    
        <!-- 
        <xsl:when test="ends-with($nachkomma,'0000')">
        </xsl:when>
        <xsl:when test="ends-with($nachkomma,'000')">
          <xsl:value-of select="substring($nachkomma,1,2)"/>
        </xsl:when>    
        <xsl:when test="ends-with($nachkomma,'00')">
          <xsl:value-of select="substring($nachkomma,1,3)"/>
        </xsl:when>     
        <xsl:when test="ends-with($nachkomma,'0')">
          <xsl:value-of select="substring($nachkomma,1,4)"/>
        </xsl:when>     
        -->                  
        <xsl:otherwise>
          <xsl:value-of select="$nachkomma"/>
        </xsl:otherwise>
      </xsl:choose>   
</xsl:template>

<xsl:template name="attr-name-by-typ">
  <xsl:param name="attr_typ"/>
  <xsl:apply-templates select="document($sel)/sel/sel[text()=$attr_typ]" mode="titel"/>
</xsl:template>

<xsl:template match="sel" mode="titel">
  <xsl:value-of select="@label" />
</xsl:template>

</xsl:stylesheet>

  