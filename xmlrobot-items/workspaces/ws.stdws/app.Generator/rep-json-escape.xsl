<?xml version="1.0" encoding="UTF-8"?>

<!--
  Copyright (c) 2006,2008 Doeke Zanstra
  All rights reserved.

  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this 
  list of conditions and the following disclaimer. Redistributions in binary 
  form must reproduce the above copyright notice, this list of conditions and the 
  following disclaimer in the documentation and/or other materials provided with 
  the distribution.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
  OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
  THE POSSIBILITY OF SUCH DAMAGE.
-->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias">
  <xsl:template name="get-rep">
  <xsl:comment>Copyright (c) 2006,2008 Doeke Zanstra
  All rights reserved.

  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this 
  list of conditions and the following disclaimer. Redistributions in binary 
  form must reproduce the above copyright notice, this list of conditions and the 
  following disclaimer in the documentation and/or other materials provided with 
  the distribution.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
  OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF 
  THE POSSIBILITY OF SUCH DAMAGE.</xsl:comment>
  <axsl:template name="escape-string">
    <xsl:comment>Main template for escaping strings; used by above template and for object-properties 
       Responsibilities: placed quotes around string, and chain up to next filter, escape-bs-string</xsl:comment>

    <axsl:param name="s"/>
    <axsl:text>"</axsl:text>
    <axsl:call-template name="escape-bs-string">
      <axsl:with-param name="s" select="$s"/>
    </axsl:call-template>
    <axsl:text>"</axsl:text>
  </axsl:template>

  <!-- Escape the backslash (\) before everything else. -->
  <xsl:comment>Escape the backslash (\) before everything else.</xsl:comment>
  <axsl:template name="escape-bs-string">
    <axsl:param name="s"/>
    <axsl:choose>
      <axsl:when test="contains($s,'\')">
        <axsl:call-template name="escape-quot-string">
          <axsl:with-param name="s" select="concat(substring-before($s,'\'),'\\')"/>
        </axsl:call-template>
        <axsl:call-template name="escape-bs-string">
          <axsl:with-param name="s" select="substring-after($s,'\')"/>
        </axsl:call-template>
      </axsl:when>
      <axsl:otherwise>
        <axsl:call-template name="escape-quot-string">
          <axsl:with-param name="s" select="$s"/>
        </axsl:call-template>
      </axsl:otherwise>
    </axsl:choose>
  </axsl:template>
  
  <!-- Escape the double quote ("). -->
  <xsl:comment>Escape the double quote (").</xsl:comment>
  <axsl:template name="escape-quot-string">
    <axsl:param name="s"/>
    <axsl:choose>
      <axsl:when test="contains($s,'&quot;')">
        <axsl:call-template name="encode-string">
          <axsl:with-param name="s" select="concat(substring-before($s,'&quot;'),'\&quot;')"/>
        </axsl:call-template>
        <axsl:call-template name="escape-quot-string">
          <axsl:with-param name="s" select="substring-after($s,'&quot;')"/>
        </axsl:call-template>
      </axsl:when>
      <axsl:otherwise>
        <axsl:call-template name="encode-string">
          <axsl:with-param name="s" select="$s"/>
        </axsl:call-template>
      </axsl:otherwise>
    </axsl:choose>
  </axsl:template>
  
  <!-- Replace tab, line feed and/or carriage return by its matching escape code. Can't escape backslash
       or double quote here, because they don't replace characters (&#x0; becomes \t), but they prefix 
       characters (\ becomes \\). Besides, backslash should be seperate anyway, because it should be 
       processed first. This function can't do that. -->
  <xsl:comment>Replace tab, line feed and/or carriage return by its matching escape code. Can't escape backslash
       or double quote here, because they don't replace characters (&amp;#x0; becomes \t), but they prefix 
       characters (\ becomes \\). Besides, backslash should be seperate anyway, because it should be 
       processed first. This function can't do that.</xsl:comment>
  <axsl:template name="encode-string">
    <axsl:param name="s"/>
    <axsl:choose>
      <xsl:comment>tab</xsl:comment>
      <!-- tab -->
      <axsl:when test="contains($s,'&#x9;')">
        <axsl:call-template name="encode-string">
          <axsl:with-param name="s" select="concat(substring-before($s,'&#x9;'),'\t',substring-after($s,'&#x9;'))"/>
        </axsl:call-template>
      </axsl:when>
      <xsl:comment>line feed</xsl:comment>
      <!-- line feed -->
      <axsl:when test="contains($s,'&#xA;')">
        <axsl:call-template name="encode-string">
          <axsl:with-param name="s" select="concat(substring-before($s,'&#xA;'),'\n',substring-after($s,'&#xA;'))"/>
        </axsl:call-template>
      </axsl:when>
      <xsl:comment>carriage return</xsl:comment>
      <!-- carriage return -->
      <axsl:when test="contains($s,'&#xD;')">
        <axsl:call-template name="encode-string">
          <axsl:with-param name="s" select="concat(substring-before($s,'&#xD;'),'\r',substring-after($s,'&#xD;'))"/>
        </axsl:call-template>
      </axsl:when>
      <axsl:otherwise><axsl:value-of select="$s"/></axsl:otherwise>
    </axsl:choose>
  </axsl:template> 
  </xsl:template> 

</xsl:stylesheet>

  
