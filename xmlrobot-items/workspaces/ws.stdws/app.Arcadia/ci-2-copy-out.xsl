<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2016 Nicolas Prandstaetter



THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
-->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml"/>

<xsl:template match="/">
  <script>
   <xsl:apply-templates select="dir/dir" mode="a"/>
  </script>
</xsl:template>

<xsl:template match="dir" mode="a">
       
<xsl:if test="file[text()='compiled-bin-original.jar']">    
<xsl:if test="storeinfo/auto/steps/p[@id='auto_zelix'][text()='1']">
<tp>Integrating: <xsl:value-of select="@libname"/></tp>
<ti>
    <xsl:attribute name="label">Integrating: <xsl:value-of select="@libname"/></xsl:attribute>
</ti>

<delete level="info">
  <xsl:attribute name="file"><xsl:value-of select="../@dir"/><xsl:value-of select="@ipath"/><xsl:text>compiled-bin.jar</xsl:text></xsl:attribute>
</delete>

<throw>
  <xsl:attribute name="test">(f,exists_file,<xsl:value-of select="../@dir"/><xsl:value-of select="@ipath"/><xsl:text>compiled-bin.jar)</xsl:text></xsl:attribute><xsl:text>Could not delete: </xsl:text><xsl:value-of select="../@dir"/><xsl:value-of select="@ipath"/><xsl:text>compiled-bin.jar</xsl:text></throw>

<copy level="info">
  <xsl:attribute name="tofile"><xsl:value-of select="../@dir"/><xsl:value-of select="@ipath"/><xsl:text>compiled-bin.jar</xsl:text></xsl:attribute>
  <xsl:attribute name="file"><xsl:text>(dir,zelix-output)</xsl:text><xsl:value-of select="@libname" /><xsl:text>.jar</xsl:text></xsl:attribute>
</copy>

<ti label="Copied Obfuriscated to" type="file">
  <xsl:attribute name="x"><xsl:value-of select="../@dir"/><xsl:value-of select="@ipath"/><xsl:text>compiled-bin.jar</xsl:text></xsl:attribute>
</ti>
  
<ti label="Copied from" type="file">
  <xsl:attribute name="x"><xsl:text>(dir,zelix-output)</xsl:text><xsl:value-of select="@libname" /><xsl:text>.jar</xsl:text></xsl:attribute>
    
</ti>

<choose>
  <when>
    <xsl:attribute name="test">(f,exists_file,<xsl:value-of select="../@dir"/><xsl:value-of select="@ipath"/><xsl:text>application-complete.jar</xsl:text>)</xsl:attribute>
    <try_catch>
      <try>
        <delete>
          <xsl:attribute name="file"><xsl:value-of select="../@dir"/><xsl:value-of select="@ipath"/><xsl:text>application-complete.jar</xsl:text></xsl:attribute>    
        </delete>
        
<throw>
  <xsl:attribute name="test">(f,exists_file,<xsl:value-of select="../@dir"/><xsl:value-of select="@ipath"/><xsl:text>application-complete.jar)</xsl:text></xsl:attribute><xsl:text>Could not delete: </xsl:text><xsl:value-of select="../@dir"/><xsl:value-of select="@ipath"/><xsl:text>application-complete.jar</xsl:text></throw>        
        <ti label="Did DELETE application-complete: ">
           <xsl:attribute name="x"><xsl:value-of select="@libname"/></xsl:attribute>
        </ti>
      </try>
      <catch>
        <ti label="PROBLEM in DELETING application-complete: ">
           <xsl:attribute name="x">[ <xsl:value-of select="@libname"/> ] </xsl:attribute>
        </ti>      
        <ti label="Problem">(prop,catched_msg)</ti>
        <tk type="msg">PROBLEM in DELETING application-complete of [ <xsl:value-of select="@libname"/> ] with message [ (prop,catched_msg) ]</tk>
      </catch>
    </try_catch>
  
  </when>
  <otherwise>
    <ti label="Ther is NO application-complete: ">
       <xsl:attribute name="x"><xsl:value-of select="@libname"/></xsl:attribute>
    </ti>  
  </otherwise>

</choose>

</xsl:if>
</xsl:if>
   <!-- <xsl:apply-templates select="dir/dir" mode="b"/> -->
  
</xsl:template>


</xsl:stylesheet>

  