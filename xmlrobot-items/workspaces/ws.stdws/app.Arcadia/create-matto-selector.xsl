<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2016 Nicolas Prandstaetter



THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
-->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">
   <xsl:apply-templates mode="a"/>
</xsl:template>

<xsl:template match="dir" mode="a">
  <sel>
    <xsl:apply-templates select="dir" mode="b"/>
    <!-- <sel label="- Individual Location">-</sel> -->
  </sel>
</xsl:template>

<xsl:template match="dir" mode="b">
  <xsl:if test="starts-with(@name,'xmlrobot-items')">
  <sel>
    <xsl:value-of select="@name" />
    
  </sel>
  </xsl:if>
  <!-- 
  
       -->
</xsl:template>



</xsl:stylesheet>

  