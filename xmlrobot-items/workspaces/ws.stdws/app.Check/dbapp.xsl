<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- Copyright © [Write the year here ...] [Write your name here ...]. All rights reserved. -->
<xsl:param name="app_key" />
<xsl:param name="appid" />
<xsl:param name="request" />
<xsl:param name="conf_db" />
<xsl:param name="postgres_schema" />                      
<xsl:output method="text" indent="yes"/>


<!-- once for xsl starter -->
<xsl:template match="/">
<xsl:choose>

<xsl:when test="$conf_db='mysql'">

<xsl:if test="contains($request,'delete')">DROP TABLE `prv<xsl:value-of select="$appid"/>`;
</xsl:if>    
    
<xsl:if test="contains($request,'createif')">CREATE TABLE IF NOT EXISTS `prv<xsl:value-of select="$appid"/>` (  `user` varchar(20) NOT NULL,  `pwd` varchar(1000) NOT NULL,  `role` varchar(10) DEFAULT NULL,  `rights` varchar(255) DEFAULT NULL, `privs` varchar(255) DEFAULT NULL,  `privsRoles` varchar(255) DEFAULT NULL,  `appPrivs` varchar(255) DEFAULT NULL,  `appPrivsRoles` varchar(255) DEFAULT NULL,  `privParas` varchar(255) DEFAULT NULL,  `privParasRoles` varchar(255) DEFAULT NULL,   `description` text,  PRIMARY KEY (`user`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;
</xsl:if>

<xsl:if test="contains($request,'createnorm')">CREATE TABLE  `prv<xsl:value-of select="$appid"/>` (  `user` varchar(20) NOT NULL,  `pwd` varchar(1000) NOT NULL,  `role` varchar(10) DEFAULT NULL,  `rights` varchar(255) DEFAULT NULL, `privs` varchar(255) DEFAULT NULL,  `privsRoles` varchar(255) DEFAULT NULL,  `appPrivs` varchar(255) DEFAULT NULL,  `appPrivsRoles` varchar(255) DEFAULT NULL,  `privParas` varchar(255) DEFAULT NULL,  `privParasRoles` varchar(255) DEFAULT NULL,   `description` text,  PRIMARY KEY (`user`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;
</xsl:if>


<xsl:if test="contains($request,'truncate')">TRUNCATE TABLE `prv<xsl:value-of select="$appid"/>`;
</xsl:if>    
    
<xsl:if test="contains($request,'select')">SELECT * FROM `formixat001`.`prv<xsl:value-of select="$appid"/>`;
</xsl:if>    

</xsl:when>

<xsl:when test="$conf_db='postgres'">

<xsl:if test="contains($request,'delete')">DROP TABLE IF EXISTS <xsl:value-of select="$postgres_schema"/>.&quot;prv<xsl:value-of select="$appid"/>&quot;;
</xsl:if>    
    
<xsl:if test="contains($request,'createif')">CREATE TABLE IF NOT EXISTS <xsl:value-of select="$postgres_schema"/>.&quot;prv<xsl:value-of select="$appid"/>&quot;
(
    &quot;user&quot; VARCHAR(20) NOT NULL,
    &quot;pwd&quot; VARCHAR(1000) NOT NULL,
    &quot;role&quot; VARCHAR(10) NOT NULL,
    &quot;rights&quot; VARCHAR(255),    
    &quot;privs&quot; VARCHAR(255) NOT NULL,
    &quot;privsRoles&quot; VARCHAR(255) NOT NULL,
    &quot;appPrivs&quot; VARCHAR(255) NOT NULL,
    &quot;appPrivsRoles&quot; VARCHAR(255) NOT NULL,
    &quot;privParas&quot; VARCHAR(255) NOT NULL,
    &quot;privParasRoles&quot; VARCHAR(255) NOT NULL,
    &quot;description&quot; VARCHAR,
    CONSTRAINT pk_appuser_<xsl:value-of select="$appid"/> PRIMARY KEY (&quot;user&quot;)    
);
</xsl:if>
                                                                    
<xsl:if test="contains($request,'createnorm')">CREATE TABLE <xsl:value-of select="$postgres_schema"/>.&quot;prv<xsl:value-of select="$appid"/>&quot;
(
    &quot;user&quot; VARCHAR(20) NOT NULL,
    &quot;pwd&quot; VARCHAR(1000) NOT NULL,
    &quot;role&quot; VARCHAR(10) NOT NULL,
    &quot;rights&quot; VARCHAR(255),    
    &quot;privs&quot; VARCHAR(255) NOT NULL,
    &quot;privsRoles&quot; VARCHAR(255) NOT NULL,
    &quot;appPrivs&quot; VARCHAR(255) NOT NULL,
    &quot;appPrivsRoles&quot; VARCHAR(255) NOT NULL,
    &quot;privParas&quot; VARCHAR(255) NOT NULL,
    &quot;privParasRoles&quot; VARCHAR(255) NOT NULL,
    &quot;description&quot; VARCHAR,
    CONSTRAINT pk_appuser_<xsl:value-of select="$appid"/> PRIMARY KEY (&quot;user&quot;)    
);
</xsl:if>


<xsl:if test="contains($request,'truncate')">TRUNCATE <xsl:value-of select="$postgres_schema"/>.&quot;prv<xsl:value-of select="$appid"/>&quot;;
</xsl:if>    
    
<xsl:if test="contains($request,'select')">SELECT * FROM <xsl:value-of select="$postgres_schema"/>.&quot;prv<xsl:value-of select="$appid"/>&quot;;
</xsl:if>    

</xsl:when>

</xsl:choose>
 
</xsl:template>

</xsl:stylesheet>
 