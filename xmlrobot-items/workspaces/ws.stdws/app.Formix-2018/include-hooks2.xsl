<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015 Nicolas Prandstaetter. All rights reserved. 






 -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="hook" mode="hook3">


  
  <xsl:choose>
    <xsl:when test="//conf/s1/@friendly='1'">
        <try_catch>
          <try>
            <trace>
    <xsl:text>Hook Station page=</xsl:text>
    <xsl:value-of select="@page"/>
    <xsl:text> station=</xsl:text>
    <xsl:value-of select="@station"/>
    <xsl:text> hook=</xsl:text>
    <xsl:value-of select="@hook"/>
    <xsl:text> hook_job=</xsl:text>
    <xsl:value-of select="@hook_job"/>    
  </trace>
          <xsl:choose>
            <xsl:when test="//conf/s2/@usage='app'">
              <call_job>
                <xsl:attribute name="job">
                  <xsl:value-of select="@hook_job" />
                </xsl:attribute>
              </call_job>
            </xsl:when>
            <xsl:when test="//conf/s2/@usage='service'">
              <call_service>
                <xsl:attribute name="job">
                  <xsl:value-of select="@hook_job" />
                </xsl:attribute>
              </call_service>
            </xsl:when>  
          </xsl:choose> 
          </try>
          <catch>
            <ti label="Hook Job must be implemented!">
                <xsl:attribute name="label">
                  <xsl:text>Hook must be implemented: </xsl:text>
                  <xsl:value-of select="//conf/s1/@singular_id"/>
                  <xsl:text>-</xsl:text>
                  <xsl:value-of select="@page"/>
                  <xsl:text>: </xsl:text>
                  <xsl:value-of select="@hook"/>            
                  <xsl:text>.</xsl:text>
                  <xsl:value-of select="@station"/>
                </xsl:attribute>      
            <ti>
          <xsl:text> hook_job=</xsl:text>
          <xsl:value-of select="@hook_job"/>      
              <xsl:text> hook=</xsl:text>
          <xsl:value-of select="@hook"/>      
                <xsl:text> page=</xsl:text>
                
          <xsl:value-of select="@page"/>
          <xsl:text> station=</xsl:text>
          <xsl:value-of select="@station"/>
          <xsl:text> object=</xsl:text>
          <xsl:value-of select="//conf/s1/@singular_id"/>
      
      
          </ti>
            </ti>
          </catch>
        </try_catch>    
    </xsl:when>
    <xsl:when test="//conf/s1/@friendly='0'">

        <try_catch>
          <try>
          <xsl:choose>
            <xsl:when test="//conf/s2/@usage='app'">
              <call_job>
                <xsl:attribute name="job">
                  <xsl:value-of select="@hook_job" />
                </xsl:attribute>
              </call_job>
            </xsl:when>
            <xsl:when test="//conf/s2/@usage='service'">
              <call_service>
                <xsl:attribute name="job">
                  <xsl:value-of select="@hook_job" />
                </xsl:attribute>
              </call_service>
            </xsl:when>  
          </xsl:choose> 
          </try>
          <catch>
            <ti label="Hook Job must be implemented!">
                <xsl:attribute name="label">
                  <xsl:text>Hook must be implemented: </xsl:text>
                  <xsl:value-of select="//conf/s1/@singular_id"/>
                  <xsl:text>-</xsl:text>
                  <xsl:value-of select="@page"/>
                  <xsl:text>: </xsl:text>
                  <xsl:value-of select="@hook"/>            
                  <xsl:text>.</xsl:text>
                  <xsl:value-of select="@station"/>
                </xsl:attribute>      
            <ti>
          <xsl:text> hook_job=</xsl:text>
          <xsl:value-of select="@hook_job"/>      
              <xsl:text> hook=</xsl:text>
          <xsl:value-of select="@hook"/>      
                <xsl:text> page=</xsl:text>
                
          <xsl:value-of select="@page"/>
          <xsl:text> station=</xsl:text>
          <xsl:value-of select="@station"/>
          <xsl:text> object=</xsl:text>
          <xsl:value-of select="//conf/s1/@singular_id"/>
      
      
          </ti>
            </ti>
            <tk type="msg" x="You have to implement the hook!"/>
            <throw_again/>
          </catch>
        </try_catch>     
    </xsl:when>
    
    <xsl:when test="//conf/s1/@friendly='3'">

          <xsl:choose>
            <xsl:when test="//conf/s2/@usage='app'">
              <call_job>
                <xsl:attribute name="job">
                  <xsl:value-of select="@hook_job" />
                </xsl:attribute>
              </call_job>
            </xsl:when>
            <xsl:when test="//conf/s2/@usage='service'">
              <call_service>
                <xsl:attribute name="job">
                  <xsl:value-of select="@hook_job" />
                </xsl:attribute>
              </call_service>
            </xsl:when>  
          </xsl:choose> 
  
    </xsl:when>    
  </xsl:choose>
      

</xsl:template>

<xsl:template name="hook2">
  <xsl:param name="page"/>
  <xsl:param name="station"/>
  <xsl:param name="hook"/>


  <xsl:apply-templates select="//conf/hooks/hook[@page=$page and @hook=$hook and @station=$station and @active='1']" mode="hook3">    
  </xsl:apply-templates> 
  <!-- 
      <xsl:attribute name="x">
        <xsl:text> </xsl:text>
      </xsl:attribute> 
      <xsl:attribute name="width">
        <xsl:value-of select="@width_px" />
        <xsl:text>px</xsl:text>
      </xsl:attribute>         
  -->      
</xsl:template>
</xsl:stylesheet>