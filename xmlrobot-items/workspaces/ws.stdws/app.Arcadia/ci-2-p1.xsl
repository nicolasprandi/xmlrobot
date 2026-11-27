<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2016 Nicolas Prandstaetter



THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
-->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="text"/>

<xsl:template match="/">
   <xsl:apply-templates select="dir/dir" mode="a"/>
</xsl:template>

<xsl:template match="dir" mode="a">
       
<xsl:if test="file[text()='3rd-party-bin.jar']">    
<xsl:if test="storeinfo/auto/steps/p[@id='auto_zelix'][text()='1']">
<xsl:text>&quot;</xsl:text><xsl:value-of select="substring-after(../@dir,'file:///')"/><xsl:value-of select="@ipath"/><xsl:text>3rd-party-bin.jar&quot;
</xsl:text>
</xsl:if>
</xsl:if>
   <!-- <xsl:apply-templates select="dir/dir" mode="b"/> -->
  
</xsl:template>

<xsl:template match="dir" mode="b">
  <sel>
  <xsl:text>_x</xsl:text>
    <!-- <xsl:attribute name="label"><xsl:value-of select="substring(@name,5)" /></xsl:attribute>  -->
    <xsl:value-of select="../@name" />
    <xsl:text>:</xsl:text>
    <xsl:value-of select="@name" />
    
    
  </sel>
</xsl:template>

<xsl:template match="appconf">

  <sel>
    <xsl:attribute name="label"><xsl:value-of select="@eclipseproject" /></xsl:attribute>
    <xsl:value-of select="@row" />
  </sel>
</xsl:template>

</xsl:stylesheet>

  