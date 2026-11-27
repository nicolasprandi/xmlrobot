<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="supersort" />
<xsl:param name="pro_id" />
<xsl:output method="html" />
<xsl:template match="/">
<html lang="en">
<xsl:comment>License Information: You are allowed to update and change this generated html-file for your own purposes, but you are NOT allowed to remove or modify the links to (1) the data-generator site and (2) the xmlrobot.net website. These 2 links have to stay intact and remain visible to the user of this html-file.</xsl:comment>
<head>
	<title><xsl:value-of select="$pro_id" />:<xsl:value-of select="cs/@sheet_name" /> </title>
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-COMPATIBLE" content="IE=edge,chrome=1"/>
	<meta name="viewport" content="width=device-width, user-scalable=no"/>
	<script src="angular.min.js"></script>
	<script src="app-data-gen.js"></script>

	<link rel="stylesheet" href="bootstrap.css"/>
        <link rel="stylesheet" href="nics.css"/>
</head>
<body>

	<div class="mainContainer" data-ng-app="TabviewApp">

                <xsl:if test="$supersort = '0'">
                <div  class="verti">
		<input type="text" data-ng-model="search.$"/>
                </div>
                </xsl:if>

		<div data-ng-controller="tabviewCtrl">
                  
      <xsl:if test="$supersort = '2'">
				<div class="verti"><table>
              <xsl:apply-templates select="cs/coltype" mode="topsort"/>
        </table></div>      
      </xsl:if>                      
			<table class="table table-striped">
                          
                      
      <xsl:if test="$supersort = '1'">
				<thead>
					<tr>
            <xsl:apply-templates select="cs/coltype" mode="supersort"/>
					</tr>
				</thead>      
      </xsl:if>
				<thead>
					<tr>
            <xsl:apply-templates select="cs/coltype" mode="header"/>
					</tr>
				</thead>
				<tbody>
					<tr data-ng-repeat="row in rows | filter:search | orderBy:sortType:sortReverse">
            <xsl:apply-templates select="cs/coltype" mode="data"/>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
<xsl:comment>License Information: You are allowed to update and change this generated html-file for your own purposes, but you are NOT allowed to remove or modify the links to (1) the data-generator site and (2) the xmlrobot.net website. These 2 links have to stay intact and remain visible to the user of this html-file.</xsl:comment>
<div class="verti"><p>Powered by <a href="https://www.xmlrobot.net/data-generator">Data-Generator</a>, <a href="https://www.xmlrobot.net">Xml-Robot</a></p></div>
<br/><br/><br/><br/><br/>
</body>
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