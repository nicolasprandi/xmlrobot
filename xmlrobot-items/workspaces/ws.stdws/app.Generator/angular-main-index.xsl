<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="supersort" />
<xsl:param name="pro_id" />
<xsl:output method="html" />
<xsl:template match="/">
<html lang="en">
<xsl:comment>License Information: You are allowed to update and change this generated html-file for your own purposes, but you are NOT allowed to remove or modify the links to (1) the generator site and (2) the xmlrobot.net website. These 2 links have to stay intact and remain visible to the user of this html-file.</xsl:comment>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
<title><xsl:value-of select="$pro_id" />:<xsl:value-of select="cs/@sheet_name" /> </title>

</head>

<frameset rows="50,*">

  <frame name="DIR" src="menu.html" frameborder="1" scrolling="auto" target="RUnten"/>
  <frame name="RUnten" src="index1.html" frameborder="0" scrolling="auto"/>

  <noframes>
  <body>
  <p>Diese Seite verwendet Frames. Frames werden von Ihrem Browser aber nicht
  unterstützt.</p>
  </body>
  </noframes>

</frameset>
</html>

  <!--
  <xsl:apply-templates select="dataset/cs"/>
  -->
</xsl:template>

<xsl:template match="coltype" mode="header">
  <!-- <th><xsl:value-of select="@name"/></th> -->
              <th>
                <a href="#">
                  <xsl:attribute name="ng-click">
                    <xsl:text>order('</xsl:text>
                    <xsl:value-of select="@name"/>
                    <xsl:text>')</xsl:text>
                  </xsl:attribute>
                  <xsl:value-of select="@name"/>
                  <span class="fa plus">
                    <xsl:attribute name="ng-show">
                      <xsl:text>sortType == '</xsl:text>
                      <xsl:value-of select="@name"/>
                      <xsl:text>' &amp;&amp; !sortReverse</xsl:text>
                    </xsl:attribute>
                  </span>
                  <span class="fa minus">
                    <xsl:attribute name="ng-show">
                      <xsl:text>sortType == '</xsl:text>
                      <xsl:value-of select="@name"/>
                      <xsl:text>' &amp;&amp; sortReverse</xsl:text>
                    </xsl:attribute>
                  </span>
                </a>
            </th><xsl:text>
</xsl:text>
</xsl:template>

<xsl:template match="coltype" mode="supersort">
  <th><input type="text"><xsl:attribute name="data-ng-model">search.<xsl:value-of select="@name"/></xsl:attribute></input></th>
<xsl:text>
</xsl:text>  
</xsl:template>

<xsl:template match="coltype" mode="topsort">
  <tr><td><xsl:value-of select="@name"/></td><td><input type="text"><xsl:attribute name="data-ng-model">search.<xsl:value-of select="@name"/></xsl:attribute></input></td></tr>
<xsl:text>
</xsl:text>  
</xsl:template>

<xsl:template match="coltype" mode="data">
  <td><xsl:text>{{row.</xsl:text><xsl:value-of select="@name"/><xsl:text>}}</xsl:text></td>
<xsl:text>
</xsl:text>
</xsl:template>

</xsl:stylesheet>