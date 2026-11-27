<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Copyright © [Write the year here ...] [Write your name here ...]. All rights reserved. -->
<xsl:param name="user_key" />
<xsl:param name="request" />
<xsl:param name="pwd" />  
<xsl:param name="tables" />                       
<xsl:output method="text" indent="yes"/>





<xsl:template match="split">
<xsl:variable name="lowercase" select="'abcdefghijklmnopqrstuvwxyz'" />
<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />


<xsl:if test="contains($tables,'app')">    
<xsl:if test="contains($request,'delete')">DELETE FROM `formixat001`.`prv<xsl:value-of select="@x"/>` WHERE  `user` = '<xsl:value-of select="translate(../../@userid,$uppercase, $lowercase)"/>';
</xsl:if></xsl:if>
<xsl:if test="contains($tables,'db') and (../../@db=1) and (position()=last())">     
<xsl:if test="contains($request,'delete')">DELETE FROM `formixat001`.`prvDbUsers` WHERE  `user` = '<xsl:value-of select="translate(../../@userid,$uppercase, $lowercase)"/>';
</xsl:if></xsl:if>

<xsl:if test="contains($tables,'app')">     
<xsl:if test="contains($request,'insert')">INSERT INTO `formixat001`.`prv<xsl:value-of select="@x"/>` (`user`, `pwd`, `role`, `privs`, `privsRoles`, `appPrivs`, `appPrivsRoles`, `privParas`, `privParasRoles`) VALUES ('<xsl:value-of select="translate(../../@userid,$uppercase, $lowercase)"/>', '<xsl:value-of select="$pwd"/>', '<xsl:value-of select="../../@role"/>', '<xsl:value-of select="../../@right_privs"/>', '<xsl:value-of select="../../@right_privsRoles"/>', '<xsl:value-of select="../../@right_appPrivs"/>', '<xsl:value-of select="../../@right_appPrivsRoles"/>', '<xsl:value-of select="../../@right_privParas"/>', '<xsl:value-of select="../../@right_privParasRoles"/>');
</xsl:if></xsl:if> 
<xsl:if test="contains($tables,'db') and (../../@db=1) and (position()=last())">     
<xsl:if test="contains($request,'insert')">INSERT INTO `formixat001`.`prvDbUsers` (`user`, `pwd`) VALUES ('<xsl:value-of select="translate(../../@userid,$uppercase, $lowercase)"/>', '<xsl:value-of select="$pwd"/>');
</xsl:if></xsl:if> 
<!-- 
<xsl:if test="contains($request,'insupd')">INSERT INTO `formixat001`.`prv<xsl:value-of select="@x"/>` (`user`, `pwd`, `role`, `privs`, `privsRoles`, `appPrivs`, `appPrivsRoles`, `privParas`, `privParasRoles`) VALUES 
('<xsl:value-of select="translate(../../@userid,$uppercase, $lowercase)"/>', '<xsl:value-of select="$pwd"/>', '<xsl:value-of select="../../@role"/>', '<xsl:value-of select="../../@right_privs"/>', '<xsl:value-of select="../../@right_privsRoles"/>', '<xsl:value-of select="../../@right_appPrivs"/>', '<xsl:value-of select="../../@right_appPrivsRoles"/>', '<xsl:value-of select="../../@right_privParas"/>', '<xsl:value-of select="../../@right_privParasRoles"/>') ON DUPLICATE KEY UPDATE;
</xsl:if>  
`pwd` = '<xsl:value-of select="$pwd"/>',
-->

<xsl:if test="contains($tables,'app')">     
<xsl:if test="contains($request,'update')">UPDATE `formixat001`.`prv<xsl:value-of select="@x"/>` SET  `role`= '<xsl:value-of select="../../@role"/>', `privs`= '<xsl:value-of select="../../@right_privs"/>', `privsRoles`= '<xsl:value-of select="../../@right_privsRoles"/>', `appPrivs`= '<xsl:value-of select="../../@right_appPrivs"/>', `appPrivsRoles`= '<xsl:value-of select="../../@right_appPrivsRoles"/>', `privParas`= '<xsl:value-of select="../../@right_privParas"/>', `privParasRoles`= '<xsl:value-of select="../../@right_privParasRoles"/>' WHERE (`user` = '<xsl:value-of select="translate(../../@userid,$uppercase, $lowercase)"/>');
</xsl:if></xsl:if>   
<!-- 
<xsl:if test="contains($tables,'db') and (../../@db=1) and (position()=last())">     
<xsl:if test="contains($request,'update')">UPDATE `formixat001`.`prvDbUsers` SET `pwd` = '<xsl:value-of select="$pwd"/>' WHERE (`user` = '<xsl:value-of select="translate(../../@userid,$uppercase, $lowercase)"/>');
</xsl:if></xsl:if>  --> 

<xsl:if test="contains($tables,'app')">     
<xsl:if test="contains($request,'select')">SELECT * FROM `formixat001`.`prv<xsl:value-of select="@x"/>` WHERE  `user` = '<xsl:value-of select="translate(../../@userid,$uppercase, $lowercase)"/>';
</xsl:if></xsl:if>  

<xsl:if test="contains($tables,'db') and (position()=last())">     
<xsl:if test="contains($request,'select')">SELECT * FROM `formixat001`.`prvDbUsers` WHERE  `user` = '<xsl:value-of select="translate(../../@userid,$uppercase, $lowercase)"/>';
</xsl:if></xsl:if>          

</xsl:template>

<xsl:template match="user">
    <xsl:apply-templates select="splits/split"/>



  
  
  
</xsl:template>

<!-- once for all rows -->
<xsl:template match="users">
  <xsl:choose>
    <xsl:when test="string-length($user_key)=0">
      <xsl:apply-templates select="user"/>
    </xsl:when>
    <xsl:otherwise>    
      <xsl:apply-templates select="user[@row=$user_key]"/>
    </xsl:otherwise>
  </xsl:choose>

</xsl:template>

<!-- once for xsl starter -->
<xsl:template match="/">
  <xsl:apply-templates select="node()"/>
</xsl:template>

</xsl:stylesheet>
 