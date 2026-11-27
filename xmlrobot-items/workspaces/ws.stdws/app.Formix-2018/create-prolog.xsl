<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015 Nicolas Prandstaetter. All rights reserved. 






 -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes"/>
<xsl:include href="include-hooks2.xsl" />

<xsl:template match="/">
   <xsl:apply-templates/>
</xsl:template>

<xsl:template match="conf">
  <prolog>
<xsl:comment>Copyright &#169; 2015-2019 Nicolas Prandstaetter. All rights reserved.</xsl:comment>
        <xsl:call-template name="hook2">
          <xsl:with-param name="page">prolog</xsl:with-param>
          <xsl:with-param name="hook">begin</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>
          

        <xsl:call-template name="hook2">
          <xsl:with-param name="page">prolog</xsl:with-param>
          <xsl:with-param name="hook">0</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>
        
<file>
  <xsl:attribute name="id">
    <xsl:value-of select="s1/@plural_id" />
  </xsl:attribute>
  <xsl:value-of select="s2/@datafile" />
</file>
<when>
  <xsl:attribute name="test">
    <xsl:text>(f,not,(f,exists_file,(file,</xsl:text>
    <xsl:value-of select="s1/@plural_id" />
    <xsl:text>)))</xsl:text>
  </xsl:attribute>
  <create_file>
    <xsl:attribute name="out">
      <xsl:text>(file,</xsl:text>
      <xsl:value-of select="s1/@plural_id" />
      <xsl:text>)</xsl:text>
    </xsl:attribute>
    <xsl:text>&lt;</xsl:text>
    <xsl:value-of select="s1/@plural_id" />  
    <xsl:text>/&gt;</xsl:text></create_file>
  <i>
    <xsl:text>Created: </xsl:text>
    <xsl:text>(file,</xsl:text>
    <xsl:value-of select="s1/@plural_id" />
    <xsl:text>)</xsl:text>
  </i>
</when>

<!-- <prop id="cities_offset">england/</prop> 
<xsl:comment>The offset defines a node in the xml-data-file under which all the data is found. use eg 'england' or 'country[id='england']</xsl:comment>
-->
<prop>
  <xsl:attribute name="id">
    <xsl:value-of select="s1/@plural_id" /><xsl:text>_offset</xsl:text>
  </xsl:attribute>
  <xsl:text>./</xsl:text>
  <!-- <xsl:text>england/</xsl:text> -->
</prop>

<!-- <prop id="cities_offset_label"> in England</prop> 
<xsl:comment>The offset label is communicated to the user, it verbalizes the offset</xsl:comment>
-->
<prop>
  <xsl:attribute name="id">
    <xsl:value-of select="s1/@plural_id" /><xsl:text>_offset_label</xsl:text>
  </xsl:attribute>
  <xsl:text></xsl:text>
  <!-- <xsl:text> in England</xsl:text> -->  
</prop>

<!-- <prop id="cities_offset_new_key">england/</prop> 
<xsl:comment>the offset_new_key is used whenever a new id for an added record is needed. this is a prefix for an xpath-expression</xsl:comment>
-->
<prop>
  <xsl:attribute name="id">
    <xsl:value-of select="s1/@plural_id" /><xsl:text>_offset_new_key</xsl:text>
  </xsl:attribute>
  <xsl:text>//</xsl:text>
</prop>

<!-- <xsl:comment>sort_attribute defines the attributes to be used for the view-sort</xsl:comment>   -->
<prop>
  <xsl:attribute name="id">
    <xsl:value-of select="s1/@plural_id" />
    <xsl:text>_sort_attribute</xsl:text>
  </xsl:attribute>
  <xsl:text></xsl:text>
</prop>

<!-- <xsl:comment>the numeric attributes in the sort</xsl:comment>  -->
<prop>
  <xsl:attribute name="id">
    <xsl:value-of select="s1/@plural_id" />
    <xsl:text>_sort_number</xsl:text>
  </xsl:attribute>
  <xsl:text></xsl:text>
</prop>

<!-- <xsl:comment>the ignore-case attributes in the sort</xsl:comment>  -->
<prop>
  <xsl:attribute name="id">
    <xsl:value-of select="s1/@plural_id" />
    <xsl:text>_sort_ignore_case</xsl:text>
  </xsl:attribute>
  <xsl:text></xsl:text>
</prop>

<!-- <xsl:comment>the desc attributes in the sort</xsl:comment>    -->
<prop>
  <xsl:attribute name="id">
    <xsl:value-of select="s1/@plural_id" />
    <xsl:text>_sort_desc</xsl:text>
  </xsl:attribute>
  <xsl:text></xsl:text>
</prop>

<!-- <xsl:comment>a where claused, this is a post to the xpath-expression, use for several view-types</xsl:comment>  -->
<prop>
  <xsl:attribute name="id">
    <xsl:value-of select="s1/@plural_id" />
    <xsl:text>_where</xsl:text>
  </xsl:attribute>
  <xsl:text></xsl:text>
</prop>

<!-- <xsl:comment>return job to be called after changes (new,delete,edit)</xsl:comment>  -->
<prop>
  <xsl:attribute name="id">
    <xsl:value-of select="s2/@return_job" />
  </xsl:attribute>
  <xsl:value-of select="s2/@job_city_view" />
</prop>

        <xsl:call-template name="hook2">
          <xsl:with-param name="page">prolog</xsl:with-param>
          <xsl:with-param name="hook">end</xsl:with-param>
          <xsl:with-param name="station">0</xsl:with-param>
        </xsl:call-template>  
  </prolog>
</xsl:template>

</xsl:stylesheet>

  