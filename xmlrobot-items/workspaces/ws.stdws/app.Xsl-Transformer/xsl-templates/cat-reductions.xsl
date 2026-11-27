<?xml version="1.0" encoding="UTF-8"?>

<!-- Copyright © 2015-2016 Nicolas Prandstaetter



THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
-->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="type"/>
<xsl:param name="redu_list"/>  

<xsl:output method="xml"/>

<xsl:template match="dir">
  <dir>
    <xsl:copy-of select="@*"/>
    <xsl:apply-templates/>
  </dir>
</xsl:template>

<xsl:template match="file">

  <xsl:call-template name="do_file">
    <xsl:with-param name="g"><xsl:value-of select="@g"/></xsl:with-param>
  </xsl:call-template>

</xsl:template>

<xsl:template name="do_file">
  <xsl:param name="g"/>

  <xsl:choose>
    <xsl:when test="$type=1">
      <!-- positive filter, evaluate @g for group of file-->
      <xsl:if test="count(document($redu_list)/redus/redu[text()=$g])&gt;0">
        <file>
          <xsl:copy-of select="@*|text()"/>
        </file>
      </xsl:if>
    </xsl:when>
    <xsl:when test="$type=2">
      <!-- positive filter, evaluate @g for group of file-->
      <xsl:if test="not(count(document($redu_list)/redus/redu[text()=$g])&gt;0)">
        <file>
          <xsl:copy-of select="@*|text()"/>
        </file>
      </xsl:if>
    </xsl:when>    
  </xsl:choose>

</xsl:template>

</xsl:stylesheet>

  