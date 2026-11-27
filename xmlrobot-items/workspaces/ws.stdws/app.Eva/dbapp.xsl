<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Copyright © [Write the year here ...] [Write your name here ...]. All rights reserved. -->
<xsl:param name="app_key" />
<xsl:param name="appid" />
<xsl:param name="request" />                      
<xsl:output method="text" indent="yes"/>


<!-- once for xsl starter -->
<xsl:template match="/">

<xsl:if test="contains($request,'delete')">DROP TABLE `prv<xsl:value-of select="$appid"/>`;
</xsl:if>    
    
<xsl:if test="contains($request,'createif')">CREATE TABLE IF NOT EXISTS `prv<xsl:value-of select="$appid"/>` (  `user` varchar(20) NOT NULL,  `pwd` varchar(1000) NOT NULL,  `role` varchar(10) DEFAULT NULL,  `privs` varchar(255) DEFAULT NULL,  `privsRoles` varchar(255) DEFAULT NULL,  `appPrivs` varchar(255) DEFAULT NULL,  `appPrivsRoles` varchar(255) DEFAULT NULL,  `privParas` varchar(255) DEFAULT NULL,  `privParasRoles` varchar(255) DEFAULT NULL,  `description` text,  PRIMARY KEY (`user`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;
</xsl:if>

<xsl:if test="contains($request,'createnorm')">CREATE TABLE  `prv<xsl:value-of select="$appid"/>` (  `user` varchar(20) NOT NULL,  `pwd` varchar(1000) NOT NULL,  `role` varchar(10) DEFAULT NULL,  `privs` varchar(255) DEFAULT NULL,  `privsRoles` varchar(255) DEFAULT NULL,  `appPrivs` varchar(255) DEFAULT NULL,  `appPrivsRoles` varchar(255) DEFAULT NULL,  `privParas` varchar(255) DEFAULT NULL,  `privParasRoles` varchar(255) DEFAULT NULL,  `description` text,  PRIMARY KEY (`user`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;
</xsl:if>


<xsl:if test="contains($request,'truncate')">TRUNCATE TABLE `prv<xsl:value-of select="$appid"/>`;
</xsl:if>    
    
<xsl:if test="contains($request,'select')">SELECT * FROM `formixat001`.`prv<xsl:value-of select="$appid"/>`;
</xsl:if>    
 
</xsl:template>

</xsl:stylesheet>
 