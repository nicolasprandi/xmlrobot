<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="type_dir_file"/>
<xsl:param name="job_id"/>
<xsl:output method="xml" indent="yes"/>


<xsl:template match="/">
  <jobs>
    <xsl:comment>Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved.</xsl:comment>
    <job role="hidden">
      <xsl:attribute name="id">
        <xsl:value-of select="$job_id" />
      </xsl:attribute>
      
      <need>
        <xsl:text>(</xsl:text>
        <xsl:value-of select="$type_dir_file" />
        <xsl:text>,with)</xsl:text>
      </need>
      
      <try_catch>
        <try>   
          <xsl:call-template name="selection" />
          <xsl:call-template name="execution" />
           
        </try>
        <catch>
          <ti label="Error occured"/>
          <ti label="Activity:" x="(prop,selected_activity)" test="(f,exists,prop,selected_activity)"/>
          <ti label="Message:" x="(prop,catched_msg)" test="(f,exists,prop,catched_msg)"/>
          <ti label="Class:" x="(prop,catched_class)" test="(f,exists,prop,catched_class)"/>         
          <throw_again/>
        </catch>
      </try_catch>           
      
    
    </job>
  </jobs>
</xsl:template>

<xsl:template name="execution">
  <choose>

          <xsl:choose>
            <xsl:when test="$type_dir_file='dir'">
              <xsl:apply-templates select="//activities/dir_activities/activity[@active=1]" mode="execution">
                <xsl:sort select="@sort" data-type="number"/>
              </xsl:apply-templates>            
            </xsl:when>
            <xsl:when test="$type_dir_file='file'">
              <xsl:apply-templates select="//activities/file_activities/activity[@active=1]" mode="execution">
                <xsl:sort select="@sort" data-type="number"/>
              </xsl:apply-templates>              
            </xsl:when>         
          </xsl:choose>     
    <otherwise>
      <ti>You decided to do nothing. Your wish is our command!</ti>
    </otherwise>
  
  </choose>
</xsl:template>

<xsl:template name="selection">

    <file id="joe-in-store">(dir,status)pano-<xsl:value-of select="$type_dir_file" />-ic.lsx</file>  
    <call_service job="go-joe-read"/>
  
      <tk>
        <xsl:attribute name="label">
        <!-- 
          <xsl:value-of select="$type_dir_file" />
                    <xsl:text> ?</xsl:text>
                    -->
          <xsl:text>Operation Selection</xsl:text>
        </xsl:attribute>
        
        <tk id="selected_activity" type="sel" flag="nono">
          <xsl:attribute name="label">
            <xsl:text>(</xsl:text>
            <xsl:value-of select="$type_dir_file" />
            <xsl:text>,with)</xsl:text>          
          </xsl:attribute>

          <xsl:choose>
            <xsl:when test="$type_dir_file='dir'">
              <xsl:apply-templates select="//activities/dir_activities/activity[@active=1]" mode="selection">
                <xsl:sort select="@sort" data-type="number"/>
              </xsl:apply-templates>            
            </xsl:when>
            <xsl:when test="$type_dir_file='file'">
              <xsl:apply-templates select="//activities/file_activities/activity[@active=1]" mode="selection">
                <xsl:sort select="@sort" data-type="number"/>
              </xsl:apply-templates>              
            </xsl:when>         
          </xsl:choose>   

        </tk>
        

      </tk>

    <prop id="joe-in-regex">selected_activity</prop>
    <call_service job="go-joe-write-srv"/>
      
</xsl:template>

<xsl:template match="activity" mode="selection">
  <sel>
    <xsl:text>()</xsl:text>
    <xsl:value-of select="@title"/>
    <xsl:text>()</xsl:text>
  </sel>
</xsl:template>

<xsl:template match="activity" mode="execution">
          
  <when>
    <xsl:attribute name="test">
      <xsl:text>(f,eq,(prop,selected_activity),()</xsl:text>
      <xsl:value-of select="@title" />
      <xsl:text>())</xsl:text>          
    </xsl:attribute>
    <ti>
      <xsl:attribute name="label">
        <xsl:text>(f,html,bold,open)()</xsl:text>
        <xsl:value-of select="@title" />
        <xsl:text>()(f,html,bold,close):</xsl:text>          
      </xsl:attribute> 
      <xsl:attribute name="type">
        <xsl:choose>
          <xsl:when test="name(parent::node())='dir_activities'">
            <xsl:text>dir</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:text>file</xsl:text>
          </xsl:otherwise>
        </xsl:choose>        
      </xsl:attribute>  
      <xsl:choose>
        <xsl:when test="name(parent::node())='dir_activities'">
          <xsl:text>(dir,with)</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>(file,with)</xsl:text>
        </xsl:otherwise>
      </xsl:choose>                
    </ti>
    <ti_hr/>
    <ti_break/>
    <call_service>
      <xsl:attribute name="job">
        <xsl:text>()</xsl:text>
        <xsl:value-of select="@service_job" />
        <xsl:text>()</xsl:text>        
      </xsl:attribute>    
    </call_service>         
    
  </when>
</xsl:template>



</xsl:stylesheet>

  