<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="supersort" />
<xsl:param name="pro_id" />
<xsl:output method="html" />
<xsl:template match="/">

<html xmlns="http://www.w3.org/1999/xhtml">
<xsl:comment>License Information: You are allowed to update and change this generated html-file for your own purposes, but you are NOT allowed to remove or modify the links to (1) the data-generator site and (2) the xmlrobot.net website. These 2 links have to stay intact and remain visible to the user of this html-file.</xsl:comment>
<head>
<title><xsl:value-of select="$pro_id" />:<xsl:value-of select="cs/@sheet_name" /> </title>
<script src="angular.min.js"></script>
<script>
var app = angular.module("AngularfilterApp", []);
app.controller("filterctrl", function ($scope) {

$scope.rows = |||###JSON###|||;

$scope.sort = {
        column: '',
        descending: false
    };    
    
    $scope.changeSorting = function(column) {

        var sort = $scope.sort;

        if (sort.column == column) {
            sort.descending = !sort.descending;
        } else {
            sort.column = column;
            sort.descending = false;
        }
    };
        
    $scope.selectedCls = function(column) {
        return column == scope.sort.column &amp;&amp; 'sort-' + scope.sort.descending;
    };
    
    $scope.sum = function( index ) {    
            var result = 0;
            for (var i = $scope.rows.length - 1; i >= 0; i--) {
              var value = parseFloat($scope.rows[i][index]);
                if (!isNaN(value)) { 
                  result += value;
                }              
            }
            return result;     
    }
    
    $scope.count = function( index ) {    
            var result = 0;
            for (var i = $scope.rows.length - 1; i >= 0; i--) {
              var value = parseFloat($scope.rows[i][index]);
                if (!isNaN(value)) { 
                  result++;
                }              
            }
            return result;     
    }    
    
    $scope.avg = function( index ) {    
            var result_sum = 0;
            var result_count = 0;
            for (var i = $scope.rows.length - 1; i >= 0; i--) {
              var value = parseFloat($scope.rows[i][index]);
                if (!isNaN(value)) { 
                  result_sum += value;
                  result_count++;
                }              
            }
            return result_sum/result_count;     
    }      
});


</script>
<link rel="stylesheet" href="bootstrap.css"/>
<link rel="stylesheet" href="nics.css"/>
</head>


<body ng-app="AngularfilterApp">

	<div ng-controller="filterctrl">

                <xsl:if test="$supersort = '0'">
                <div  class="verti">
    All to Filter:<input ng-model="searchtxt.$" type="text" placeholder="Enter Anything"/>           
                </div>
                </xsl:if>


      <!-- TBD#1   -->           
      <xsl:if test="$supersort = '2'">
				<div class="verti"><table>
              <xsl:apply-templates select="cs/coltype" mode="topsort"/>
        </table></div>      
      </xsl:if>         
                 
			<table class="table table-striped">
                          
      <!-- TBD#2  -->              
      <xsl:if test="$supersort = '1'">
				<thead>
					<tr>
            <xsl:apply-templates select="cs/coltype" mode="supersort"/>
					</tr>
				</thead>      
      </xsl:if>
      
      <!-- TBD#3   -->
				<thead>
					<tr>
            <xsl:apply-templates select="cs/coltype" mode="header"/>
					</tr>
				</thead>
      
				<tbody>
          <!-- TBD#4    --> 
          <xsl:call-template name="do_aggregations">
            <xsl:with-param name="problem">e</xsl:with-param>
          </xsl:call-template>      
           
					<tr ng-repeat="row in rows | filter : searchtxt | orderBy:sort.column:sort.descending">
            <xsl:apply-templates select="cs/coltype" mode="data"/>
					</tr>
          <!-- TBD#5  
          <xsl:call-template name="do_aggregations">
            <xsl:with-param name="problem">e</xsl:with-param>
          </xsl:call-template>    -->
				</tbody>
			</table>

	</div>
<xsl:comment>License Information: You are allowed to update and change this generated html-file for your own purposes, but you are NOT allowed to remove or modify the links to (1) the data-generator site and (2) the xmlrobot.net website. These 2 links have to stay intact and remain visible to the user of this html-file.</xsl:comment>
<div class="verti"><p>Powered by <a href="https://www.xmlrobot.net/data-generator">Generator</a>, <a href="https://www.xmlrobot.net">Xml-Robot</a></p></div>
<br/><br/><br/><br/><br/>
</body>
</html>

  <!--
  <xsl:apply-templates select="dataset/cs"/>
  -->
</xsl:template>

<xsl:template name="do_aggregations">
		  <tr>
      <xsl:apply-templates select="cs/coltype" mode="aggregation_sum"/>
		  </tr>
		  <tr>
      <xsl:apply-templates select="cs/coltype" mode="aggregation_avg"/>
		  </tr>   
      <!-- 
		  <tr>
      <xsl:apply-templates select="cs/coltype" mode="aggregation_cnt"/>
		  </tr>      -->     
</xsl:template>

<xsl:template match="coltype" mode="aggregation_sum">

  <xsl:call-template name="coltype_data_agg"> 
    <xsl:with-param name="thename"><xsl:value-of select="@name"/></xsl:with-param>
  </xsl:call-template>
  
</xsl:template>

<xsl:template match="coltype" mode="aggregation_avg">

  <xsl:call-template name="coltype_data_agg2"> 
    <xsl:with-param name="thename"><xsl:value-of select="@name"/></xsl:with-param>
  </xsl:call-template>
  
</xsl:template>

<xsl:template match="coltype" mode="aggregation_cnt">

  <xsl:call-template name="coltype_data_agg3"> 
    <xsl:with-param name="thename"><xsl:value-of select="@name"/></xsl:with-param>
  </xsl:call-template>
  
</xsl:template>

<xsl:template name="aggregation_sum2">                                       
   <xsl:param name="thename"/>
</xsl:template>   

<xsl:template match="coltype" mode="aggregation">
  <tr>
  <xsl:call-template name="coltype_data_agg"> 
    <xsl:with-param name="thename"><xsl:value-of select="@name"/></xsl:with-param>
  </xsl:call-template>
  </tr>

  <tr>
  <xsl:call-template name="coltype_data_agg2"> 
    <xsl:with-param name="thename"><xsl:value-of select="@name"/></xsl:with-param>
  </xsl:call-template>
  </tr>
  <!--
  <tr>
  <xsl:call-template name="coltype_data_agg3"> 
    <xsl:with-param name="thename"><xsl:value-of select="@name"/></xsl:with-param>
  </xsl:call-template>
  </tr>  
   
  <td><xsl:text>{{row.</xsl:text><xsl:value-of select="@name"/><xsl:text>}}</xsl:text></td>
  -->
<xsl:text>
</xsl:text>
</xsl:template>

<!--
 <td align="right"><b>{{resultValue|sumByKey:'Einnahmen'| number:2}}</b></td>
-->

<xsl:template name="coltype_data_agg">                                       
   <xsl:param name="thename"/>
   <xsl:choose>
    <xsl:when test="/cs/tips/summable/split[@x=$thename]">
      <td align="right"><b><xsl:text>{{ sum('</xsl:text><xsl:value-of select="$thename"/><xsl:text>') | number:2 }}</xsl:text></b></td>
      
      <!--   
      {{row | map:'</xsl:text><xsl:value-of select="$thename"/><xsl:text>' | sum }}
      {{ users | map: 'amount' | sum }}
      <td align="right"><b><xsl:text>{{resultValue|sumByKey:'</xsl:text><xsl:value-of select="$thename"/><xsl:text>' | number:2 }}</xsl:text></b></td>   -->
      
    </xsl:when>
    <xsl:otherwise>
      <td><!--<xsl:text>{{row.</xsl:text><xsl:value-of select="$thename"/><xsl:text>}}</xsl:text>--></td>
    </xsl:otherwise>
   </xsl:choose>
   
</xsl:template>

<xsl:template name="coltype_data_agg2">                                       
   <xsl:param name="thename"/>
   <xsl:choose>
    <xsl:when test="/cs/tips/summable/split[@x=$thename]">
      <td align="right">= <b><xsl:text>{{ count('</xsl:text><xsl:value-of select="$thename"/><xsl:text>') | number:0 }}</xsl:text></b> x <b><xsl:text>{{avg('</xsl:text><xsl:value-of select="$thename"/><xsl:text>') | number:2 }}</xsl:text></b></td>
      
      <!--
      <td align="right">= <b><xsl:text>{{resultValue|countByKey:'</xsl:text><xsl:value-of select="$thename"/><xsl:text>' | number:0 }}</xsl:text></b> x <b><xsl:text>{{resultValue|avgByKey:'</xsl:text><xsl:value-of select="$thename"/><xsl:text>' | number:2 }}</xsl:text></b></td>      
      
      -->
    </xsl:when>
    <xsl:otherwise>
      <td><!--<xsl:text>{{row.</xsl:text><xsl:value-of select="$thename"/><xsl:text>}}</xsl:text>--></td>
    </xsl:otherwise>
   </xsl:choose>
   
</xsl:template>

<xsl:template name="coltype_data_agg2b">                                       
   <xsl:param name="thename"/>
   <xsl:choose>
    <xsl:when test="/cs/tips/summable/split[@x=$thename]">
      <td align="right">= <b><xsl:text>{{ avg('</xsl:text><xsl:value-of select="$thename"/><xsl:text>') | number:2 }}</xsl:text></b></td>
      <!-- 
      -->
    </xsl:when>
    <xsl:otherwise>
      <td><!--<xsl:text>{{row.</xsl:text><xsl:value-of select="$thename"/><xsl:text>}}</xsl:text>--></td>
    </xsl:otherwise>
   </xsl:choose>
   
</xsl:template>

<xsl:template name="coltype_data_agg3">                                       
   <xsl:param name="thename"/>
   <xsl:choose>
    <xsl:when test="/cs/tips/numeric/split[@x=$thename]">
      <td align="right">x <b><xsl:text>{{ count('</xsl:text><xsl:value-of select="$thename"/><xsl:text>') | number:0 }}</xsl:text></b></td>
    </xsl:when>
    <xsl:otherwise>
      <td><!--<xsl:text>{{row.</xsl:text><xsl:value-of select="$thename"/><xsl:text>}}</xsl:text>--></td>
    </xsl:otherwise>
   </xsl:choose>
   
</xsl:template>

<xsl:template match="coltype" mode="header">
  <!-- <th><xsl:value-of select="@name"/></th>
   <xsl:param name="thename"/>
   <xsl:choose>
    <xsl:when test="/cs/tips/numeric/split[@x=$thename]">
      <td align="right"><b><xsl:text>{{resultValue|sumByKey:'</xsl:text><xsl:value-of select="$thename"/><xsl:text>' | number:2 }}</xsl:text></b></td>
    </xsl:when>
    <xsl:otherwise>
      <td><xsl:text>{{row.</xsl:text><xsl:value-of select="$thename"/><xsl:text>}}</xsl:text></td>
    </xsl:otherwise>
   </xsl:choose>  
  
   -->

  <xsl:call-template name="header2"> 
    <xsl:with-param name="thename"><xsl:value-of select="@name"/></xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template name="header2">
<xsl:param name="thename"/>    
   <!-- 

   <xsl:choose>
    <xsl:when test="/cs/tips/numeric/split[@x=$thename]">
      <td align="right"><b><xsl:text>{{resultValue|sumByKey:'</xsl:text><xsl:value-of select="$thename"/><xsl:text>' | number:2 }}</xsl:text></b></td>
    </xsl:when>
    <xsl:otherwise>
      <td><xsl:text>{{row.</xsl:text><xsl:value-of select="$thename"/><xsl:text>}}</xsl:text></td>
    </xsl:otherwise>
   </xsl:choose>           <xsl:when test="/cs/tips/numeric/split[@x=$thename]">
    -->
    
          <th >
            <xsl:attribute name="ng-class">
                    <xsl:text>selectedCls('</xsl:text>
                    <xsl:value-of select="@name"/>
                    <xsl:text>')</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="ng-click">
                    <xsl:text>changeSorting('</xsl:text>
                    <xsl:value-of select="@name"/>
                    <xsl:text>')</xsl:text>
            </xsl:attribute>            
                  
            <xsl:value-of select="@name"/><i  ng-show="sort.descending" class="fa plus"></i>
            <i  ng-show="!sort.descending" class="fa minus"></i>
          </th>
          <!--     
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
</xsl:text>       -->
</xsl:template>

<xsl:template match="coltype" mode="supersort">
  <th><input type="text"><xsl:attribute name="ng-model">searchtxt.<xsl:value-of select="@name"/></xsl:attribute></input></th>
<xsl:text>
</xsl:text>  
</xsl:template>

<xsl:template match="coltype" mode="topsort">
  <tr><td><xsl:value-of select="@name"/></td><td><input type="text"><xsl:attribute name="ng-model">searchtxt.<xsl:value-of select="@name"/></xsl:attribute></input></td></tr>
<xsl:text>
</xsl:text>  
</xsl:template>

<xsl:template match="coltype" mode="data">

  <xsl:call-template name="coltype_data"> 
    <xsl:with-param name="thename"><xsl:value-of select="@name"/></xsl:with-param>
  </xsl:call-template>

  <!-- 
  <td><xsl:text>{{row.</xsl:text><xsl:value-of select="@name"/><xsl:text>}}</xsl:text></td>
  -->
<xsl:text>
</xsl:text>
</xsl:template>

<xsl:template name="coltype_data">                                       
   <xsl:param name="thename"/>
   <xsl:choose>
    <xsl:when test="/cs/tips/numeric/split[@x=$thename]">
      <td align="right"><xsl:text>{{row.</xsl:text><xsl:value-of select="$thename"/><xsl:text> | number:2 }}</xsl:text></td>
    </xsl:when>
    <xsl:otherwise>
      <td><xsl:text>{{row.</xsl:text><xsl:value-of select="$thename"/><xsl:text>}}</xsl:text></td>
    </xsl:otherwise>
   </xsl:choose>
   
</xsl:template>

</xsl:stylesheet>