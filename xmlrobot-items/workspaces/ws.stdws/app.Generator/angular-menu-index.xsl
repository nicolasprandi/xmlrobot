<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="supersort" />
<xsl:param name="pro_id" />
<xsl:output method="html" />
<xsl:template match="/">
<html>
   <head>
      <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

      <title>Direktory</title>
      <base target="RUnten"/></head>
   <body>
      <a href="index1.html" target="RUnten">One Search-Field</a> | <a href="index2.html" target="RUnten">Table</a> | <a href="index3.html" target="RUnten">Form</a>  ___  <b><xsl:value-of select="$pro_id" />  ___  <xsl:value-of select="cs/@sheet_name" /></b>
  </body>
</html>

  <!--
  <xsl:apply-templates select="dataset/cs"/>
  -->
</xsl:template>

</xsl:stylesheet>