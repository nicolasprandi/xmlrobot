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
<copy>
  <xsl:attribute name="file"><xsl:value-of select="../@dir"/><xsl:value-of select="@ipath"/><xsl:text>compiled-bin-original.jar</xsl:text></xsl:attribute>
  <xsl:attribute name="tofile"><xsl:text>(dir,zelix-input)</xsl:text><xsl:value-of select="@libname" /><xsl:text>.jar</xsl:text></xsl:attribute>
</copy>

</xsl:if>
</xsl:if>
   <!-- <xsl:apply-templates select="dir/dir" mode="b"/> -->
  
</xsl:template>


</xsl:stylesheet>

  