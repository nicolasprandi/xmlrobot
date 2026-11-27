<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">


<xsl:output method="xml"/>

<xsl:template match="/">

  <result>    

  <count_files>
    <xsl:value-of select="count(dir/file)"/>
  </count_files>  
  
  <count_files_open>                  
    <xsl:value-of select="count(dir/file[ not( substring (@pre, string-length(@pre) - string-length('-encrypted') + 1 ) = '-encrypted' ) ])"/>
  </count_files_open>    
  
  <count_dirs>
    <xsl:value-of select="count(dir/dir)"/>
  </count_dirs>      
    
    
  </result>
  

</xsl:template>


</xsl:stylesheet>

  