<?xml version="1.0" encoding="UTF-8"?>

<!-- Copyright © 2018-2020 Nicolas Prandstaetter. All rights reserved. -->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="dataset"/> 

<xsl:template match="/">

  
    <xsl:apply-templates select="all/dsis/dsi[@row=$dataset]">
      </xsl:apply-templates>
  <!--     <xsl:value-of select="$dataset"/>
      <xsl:apply-templates select="root/ats/at">
      </xsl:apply-templates>      -->
      
</xsl:template>
<xsl:template name="core-neu">
</xsl:template>

<xsl:template name="dsa-dbin-dreier">
  <xsl:param name="dso-objekttyp"/>
  <xsl:param name="dso-objektname"/>
  <xsl:param name="dsa-attributtyp"/>
  <xsl:param name="dsa-attributname"/>
  
  <field>
    <name><xsl:text>attr_typ_</xsl:text><xsl:value-of select="$dsa-attributname" /></name>
    <type>Integer</type>
    <format/>
    <currency/>
    <decimal/>
    <group/>
    <nullif><xsl:value-of select="$dsa-attributtyp" /></nullif>
    <length>-1</length>
    <precision>-1</precision>
  </field>
  
  <step>
    <name>attr: <xsl:value-of select="$dso-objektname" />.<xsl:value-of select="$dsa-attributname" /></name>
    <type>TableOutput</type>
    <description/>
    <distribute>Y</distribute>
    <copies>1</copies>
         <partitioning>
           <method>none</method>
           <schema_name/>
           </partitioning>
    <connection/>
    <schema>import</schema>
    <table>qk_attr_wert</table>
    <commit>1000</commit>
    <truncate>N</truncate>
    <ignore_errors>N</ignore_errors>
    <use_batch>Y</use_batch>
    <specify_fields>Y</specify_fields>
    <partitioning_enabled>N</partitioning_enabled>
    <partitioning_field/>
    <partitioning_daily>N</partitioning_daily>
    <partitioning_monthly>Y</partitioning_monthly>
    <tablename_in_field>N</tablename_in_field>
    <tablename_field/>
    <tablename_in_table>Y</tablename_in_table>
    <return_keys>N</return_keys>
    <return_field/>
    <fields>
        <field>
          <column_name>obj_id</column_name>
          <stream_name>next_obj_id_<xsl:value-of select="$dso-objektname" /></stream_name>
        </field>
        <field>
          <column_name>attr_typ</column_name>
          <stream_name>attr_typ_<xsl:value-of select="$dsa-attributname" /></stream_name>
        </field>
        <field>
          <column_name>inhalt</column_name>
          <stream_name><xsl:value-of select="$dsa-attributname" /></stream_name>
        </field>
        <field>
          <column_name>box_id</column_name>
          <stream_name>next_box_id</stream_name>
        </field>
    </fields>
     <cluster_schema/>
 <remotesteps>   <input>   </input>   <output>   </output> </remotesteps>    <GUI>

      
      <xloc><xsl:value-of select="659+position()*60" /></xloc>
      <yloc><xsl:value-of select="112+position()*50" /></yloc>
            
      <draw>Y</draw>
      </GUI>
    </step>  
</xsl:template>

<xsl:template name="dsa-dbin-zweier">
  <xsl:param name="dso-objekttyp"/>
  <xsl:param name="dsa-attributtyp"/>
  <xsl:call-template name="dsa-dbin-dreier">
    <xsl:with-param name="dso-objekttyp"><xsl:value-of select="$dso-objekttyp"/></xsl:with-param>
    <xsl:with-param name="dso-objektname"><xsl:value-of select="//ot[@obj_typ=$dso-objekttyp]/@tech_name"/></xsl:with-param>    
    <xsl:with-param name="dsa-attributtyp"><xsl:value-of select="$dsa-attributtyp"/></xsl:with-param>
    <xsl:with-param name="dsa-attributname"><xsl:value-of select="//at[@attr_typ=$dsa-attributtyp]/@tech_name"/></xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template match="dsa" mode="dbin">
  <xsl:call-template name="dsa-dbin-zweier">
    <xsl:with-param name="dso-objekttyp"><xsl:value-of select="../@objekttyp"/></xsl:with-param>
    <xsl:with-param name="dsa-attributtyp"><xsl:value-of select="@attributtyp"/></xsl:with-param>

  </xsl:call-template>
</xsl:template>

<xsl:template name="dso-dbin-dreier">
  <xsl:param name="dso-objekttyp"/>
  <xsl:param name="dso-objektname"/>
  
  <step>
    <name><xsl:text>obj: </xsl:text><xsl:value-of select="$dso-objektname" /></name>
    <type>TableOutput</type>
    <description/>
    <distribute>Y</distribute>
    <copies>1</copies>
         <partitioning>
           <method>none</method>
           <schema_name/>
           </partitioning>
    <connection/>
    <schema>import</schema>
    <table>obj</table>
    <commit>1000</commit>
    <truncate>N</truncate>
    <ignore_errors>N</ignore_errors>
    <use_batch>Y</use_batch>
    <specify_fields>Y</specify_fields>
    <partitioning_enabled>N</partitioning_enabled>
    <partitioning_field/>
    <partitioning_daily>N</partitioning_daily>
    <partitioning_monthly>Y</partitioning_monthly>
    <tablename_in_field>N</tablename_in_field>
    <tablename_field/>
    <tablename_in_table>Y</tablename_in_table>
    <return_keys>N</return_keys>
    <return_field/>
    <fields>
        <field>
          <column_name>obj_id</column_name>
          <stream_name>next_obj_id_<xsl:value-of select="$dso-objektname" /></stream_name>
        </field>
        <field>
          <column_name>obj_typ</column_name>
          <stream_name>obj_typ_<xsl:value-of select="$dso-objektname" /></stream_name>
        </field>
        <field>
          <column_name>box_id</column_name>
          <stream_name>next_box_id</stream_name>
        </field>
    </fields>
     <cluster_schema/>
 <remotesteps>   <input>   </input>   <output>   </output> </remotesteps>    <GUI>
      <xloc><xsl:value-of select="913+position()*60" /></xloc>
      <yloc><xsl:value-of select="114+position()*20" /></yloc>
      <draw>Y</draw>
      </GUI>
    </step>
</xsl:template>

<xsl:template name="dso-dbin-zweier">
  <xsl:param name="dso-objekttyp"/>
  <xsl:call-template name="dso-dbin-dreier">
    <xsl:with-param name="dso-objekttyp"><xsl:value-of select="@objekttyp"/></xsl:with-param>
    <xsl:with-param name="dso-objektname"><xsl:value-of select="//ot[@obj_typ=$dso-objekttyp]/@tech_name"/></xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template match="dso" mode="dbin">
  <xsl:call-template name="dso-dbin-zweier">
    <xsl:with-param name="dso-objekttyp"><xsl:value-of select="@objekttyp"/></xsl:with-param>

  </xsl:call-template>
</xsl:template>




<xsl:template name="dsa-dreier">
  <xsl:param name="dso-objekttyp"/>
  <xsl:param name="dsa-attributtyp"/>
  <xsl:param name="dsa-attributname"/>
  
  <field>
    <name><xsl:text>attr_typ_</xsl:text><xsl:value-of select="$dsa-attributname" /></name>
    <type>Integer</type>
    <format/>
    <currency/>
    <decimal/>
    <group/>
    <nullif><xsl:value-of select="$dsa-attributtyp" /></nullif>
    <length>-1</length>
    <precision>-1</precision>
  </field>
</xsl:template>

<xsl:template name="dsa-zweier">
  <xsl:param name="dso-objekttyp"/>
  <xsl:param name="dsa-attributtyp"/>
  <xsl:call-template name="dsa-dreier">
    <xsl:with-param name="dso-objekttyp"><xsl:value-of select="$dso-objekttyp"/></xsl:with-param>
    <xsl:with-param name="dsa-attributtyp"><xsl:value-of select="$dsa-attributtyp"/></xsl:with-param>
    <xsl:with-param name="dsa-attributname"><xsl:value-of select="//at[@attr_typ=$dsa-attributtyp]/@tech_name"/></xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template match="dsa" mode="einser">
  <xsl:call-template name="dsa-zweier">
    <xsl:with-param name="dso-objekttyp"><xsl:value-of select="../@objekttyp"/></xsl:with-param>
    <xsl:with-param name="dsa-attributtyp"><xsl:value-of select="@attributtyp"/></xsl:with-param>

  </xsl:call-template>
</xsl:template>

<xsl:template name="dso-dreier">
  <xsl:param name="dso-objekttyp"/>
  <xsl:param name="dso-objektname"/>
  
  <field>
    <name><xsl:text>obj_typ_</xsl:text><xsl:value-of select="$dso-objektname" /></name>
    <type>Integer</type>
    <format/>
    <currency/>
    <decimal/>
    <group/>
    <nullif><xsl:value-of select="$dso-objekttyp" /></nullif>
    <length>-1</length>
    <precision>-1</precision>
  </field>
</xsl:template>

<xsl:template name="dso-zweier">
  <xsl:param name="dso-objekttyp"/>
  <xsl:call-template name="dso-dreier">
    <xsl:with-param name="dso-objekttyp"><xsl:value-of select="@objekttyp"/></xsl:with-param>
    <xsl:with-param name="dso-objektname"><xsl:value-of select="//ot[@obj_typ=$dso-objekttyp]/@tech_name"/></xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template match="dso" mode="einser">
  <xsl:call-template name="dso-zweier">
    <xsl:with-param name="dso-objekttyp"><xsl:value-of select="@objekttyp"/></xsl:with-param>

  </xsl:call-template>
</xsl:template>
<xsl:template name="dsb-dbin-dreier">
  <xsl:param name="dsb-typ"/>
  <xsl:param name="dsb-name"/>
  
  <step>
    <name>bez: <xsl:value-of select="$dsb-name"/></name>
    <type>TableOutput</type>
    <description/>
    <distribute>Y</distribute>
    <copies>1</copies>
         <partitioning>
           <method>none</method>
           <schema_name/>
           </partitioning>
    <connection/>
    <schema>import</schema>
    <table>bez</table>
    <commit>1000</commit>
    <truncate>N</truncate>
    <ignore_errors>N</ignore_errors>
    <use_batch>Y</use_batch>
    <specify_fields>Y</specify_fields>
    <partitioning_enabled>N</partitioning_enabled>
    <partitioning_field/>
    <partitioning_daily>N</partitioning_daily>
    <partitioning_monthly>Y</partitioning_monthly>
    <tablename_in_field>N</tablename_in_field>
    <tablename_field/>
    <tablename_in_table>Y</tablename_in_table>
    <return_keys>N</return_keys>
    <return_field/>
    <fields>
        <field>
          <column_name>bez_typ</column_name>
          <stream_name>bez_typ_<xsl:value-of select="$dsb-name"/></stream_name>
        </field>
        <field>
          <column_name>obj_id_a</column_name>
          <stream_name>next_obj_id_XXX</stream_name>
        </field>
        <field>
          <column_name>obj_id_b</column_name>
          <stream_name>next_obj_id_YYY</stream_name>
        </field>
        <field>
          <column_name>box_id</column_name>
          <stream_name>next_box_id</stream_name>
        </field>
    </fields>
     <cluster_schema/>
 <remotesteps>   <input>   </input>   <output>   </output> </remotesteps>    <GUI>

      
      <xloc><xsl:value-of select="568+position()*60" /></xloc>
      <yloc><xsl:value-of select="218+position()*80" /></yloc>      
      <draw>Y</draw>
      </GUI>
    </step>
</xsl:template>

<xsl:template name="dsb-dbin-zweier">
  <xsl:param name="dsb-typ"/>
  <xsl:call-template name="dsb-dbin-dreier">
    <xsl:with-param name="dsb-typ"><xsl:value-of select="$dsb-typ"/></xsl:with-param>
    <xsl:with-param name="dsb-name"><xsl:value-of select="//bt[@bez_typ=$dsb-typ]/@tech_name"/></xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template match="dsb" mode="dbin">
  <xsl:call-template name="dsb-dbin-zweier">
    <xsl:with-param name="dsb-typ"><xsl:value-of select="@beztyp"/></xsl:with-param>

  </xsl:call-template>
</xsl:template>

<xsl:template name="dsb-dreier">
  <xsl:param name="dsb-typ"/>
  <xsl:param name="dsb-name"/>
  
  <field>
    <name><xsl:text>bez_typ_</xsl:text><xsl:value-of select="$dsb-name" /></name>
    <type>Integer</type>
    <format/>
    <currency/>
    <decimal/>
    <group/>
    <nullif><xsl:value-of select="$dsb-typ" /></nullif>
    <length>-1</length>
    <precision>-1</precision>
  </field>
</xsl:template>

<xsl:template name="dsb-zweier">
  <xsl:param name="dsb-typ"/>
  <xsl:call-template name="dsb-dreier">
    <xsl:with-param name="dsb-typ"><xsl:value-of select="$dsb-typ"/></xsl:with-param>
    <xsl:with-param name="dsb-name"><xsl:value-of select="//bt[@bez_typ=$dsb-typ]/@tech_name"/></xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template match="dsb" mode="einser">
  <xsl:call-template name="dsb-zweier">
    <xsl:with-param name="dsb-typ"><xsl:value-of select="@beztyp"/></xsl:with-param>

  </xsl:call-template>
</xsl:template>

<xsl:template name="fields">
  <fields>
      <xsl:apply-templates select="//all/dsis/dsi[@row=$dataset]/dso" mode="einser">
      </xsl:apply-templates>
      <xsl:apply-templates select="//all/dsis/dsi[@row=$dataset]/dso/dsa" mode="einser">
      </xsl:apply-templates>    
      <xsl:apply-templates select="//all/dsis/dsi[@row=$dataset]/dsb" mode="einser">
      </xsl:apply-templates>           
     <!-- 
     <xsl:call-template name="old-fields"/>
      alle objekte des datasets>
     <xsl:apply-templates select=""/>  -->
  </fields>
</xsl:template>

<xsl:template name="old-fields">

      <field>
        <name>obj_typ_staat</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>1</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_staat_name_en</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>1</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_staat_name_offiziell_en</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>2</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_staat_name_offiziell_fr</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>3</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_iso3166_1alpha2</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>4</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_iso3166_1alpha3</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>5</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_m49</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>6</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_itu</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>7</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_marc</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>8</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_wmo</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>9</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_ds</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>10</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_vorwahl</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>11</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_fifa</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>12</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_fips</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>13</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_gaul</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>14</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_ioc</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>15</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_iso4217_whg_alph_code</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>16</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_iso4217_whg_staat_name</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>17</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_iso4217_whg_name</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>18</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_iso4217_whg_num_code</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>19</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_staat_unabhaengig</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>20</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_stadt_name_en</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>21</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_kontinent_name_en</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>22</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_web</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>23</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_sprachen</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>24</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_geoname_id</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>25</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_edgar</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>26</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>obj_typ_whg</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>2</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>obj_typ_stadt</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>3</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>obj_typ_knt</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>4</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>attr_typ_codename1</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>28</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>bez_typ_landeswaehrung</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>7</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>bez_typ_hauptstadt</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>8</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>
      <field>
        <name>bez_typ_in</name>
        <type>Integer</type>
        <format/>
        <currency/>
        <decimal/>
        <group/>
        <nullif>9</nullif>
        <length>-1</length>
        <precision>-1</precision>
      </field>

</xsl:template>



<xsl:template name="core">
  <xsl:apply-templates select="//dsi[@row=$dataset]/dso" mode="dbin">
  </xsl:apply-templates>
  <xsl:apply-templates select="//dsi[@row=$dataset]/dso/dsa" mode="dbin">
  </xsl:apply-templates>  
  <xsl:apply-templates select="//dsi[@row=$dataset]/dsb" mode="dbin">
  </xsl:apply-templates>  
<step>
    <name>types of dataset <xsl:value-of select="@bez"/></name>
    <type>RowGenerator</type>
    <description/>
    <distribute>Y</distribute>
    <copies>1</copies>
         <partitioning>
           <method>none</method>
           <schema_name/>
           </partitioning>
    <xsl:call-template name="fields"/>

    <limit>1</limit>
     <cluster_schema/>
 <remotesteps>   <input>   </input>   <output>   </output> </remotesteps>    <GUI>
      <xloc>296</xloc>
      <yloc>90</yloc>
      <draw>Y</draw>
      </GUI>
    </step>

</xsl:template>

<xsl:template match="dsi">


<transformation>
  <info>
    <name><xsl:text>dataset </xsl:text> <xsl:value-of select="@bez"/></name>
    <description/>
    <extended_description/>
    <trans_version/>
    <trans_type>Normal</trans_type>
    <directory>&#47;</directory>
    <parameters>
    </parameters>
    <log>
<trans-log-table><connection/>
<schema/>
<table/>
<size_limit_lines/>
<interval/>
<timeout_days/>
<field><id>ID_BATCH</id><enabled>Y</enabled><name>ID_BATCH</name></field><field><id>CHANNEL_ID</id><enabled>Y</enabled><name>CHANNEL_ID</name></field><field><id>TRANSNAME</id><enabled>Y</enabled><name>TRANSNAME</name></field><field><id>STATUS</id><enabled>Y</enabled><name>STATUS</name></field><field><id>LINES_READ</id><enabled>Y</enabled><name>LINES_READ</name><subject/></field><field><id>LINES_WRITTEN</id><enabled>Y</enabled><name>LINES_WRITTEN</name><subject/></field><field><id>LINES_UPDATED</id><enabled>Y</enabled><name>LINES_UPDATED</name><subject/></field><field><id>LINES_INPUT</id><enabled>Y</enabled><name>LINES_INPUT</name><subject/></field><field><id>LINES_OUTPUT</id><enabled>Y</enabled><name>LINES_OUTPUT</name><subject/></field><field><id>LINES_REJECTED</id><enabled>Y</enabled><name>LINES_REJECTED</name><subject/></field><field><id>ERRORS</id><enabled>Y</enabled><name>ERRORS</name></field><field><id>STARTDATE</id><enabled>Y</enabled><name>STARTDATE</name></field><field><id>ENDDATE</id><enabled>Y</enabled><name>ENDDATE</name></field><field><id>LOGDATE</id><enabled>Y</enabled><name>LOGDATE</name></field><field><id>DEPDATE</id><enabled>Y</enabled><name>DEPDATE</name></field><field><id>REPLAYDATE</id><enabled>Y</enabled><name>REPLAYDATE</name></field><field><id>LOG_FIELD</id><enabled>Y</enabled><name>LOG_FIELD</name></field></trans-log-table>
<perf-log-table><connection/>
<schema/>
<table/>
<interval/>
<timeout_days/>
<field><id>ID_BATCH</id><enabled>Y</enabled><name>ID_BATCH</name></field><field><id>SEQ_NR</id><enabled>Y</enabled><name>SEQ_NR</name></field><field><id>LOGDATE</id><enabled>Y</enabled><name>LOGDATE</name></field><field><id>TRANSNAME</id><enabled>Y</enabled><name>TRANSNAME</name></field><field><id>STEPNAME</id><enabled>Y</enabled><name>STEPNAME</name></field><field><id>STEP_COPY</id><enabled>Y</enabled><name>STEP_COPY</name></field><field><id>LINES_READ</id><enabled>Y</enabled><name>LINES_READ</name></field><field><id>LINES_WRITTEN</id><enabled>Y</enabled><name>LINES_WRITTEN</name></field><field><id>LINES_UPDATED</id><enabled>Y</enabled><name>LINES_UPDATED</name></field><field><id>LINES_INPUT</id><enabled>Y</enabled><name>LINES_INPUT</name></field><field><id>LINES_OUTPUT</id><enabled>Y</enabled><name>LINES_OUTPUT</name></field><field><id>LINES_REJECTED</id><enabled>Y</enabled><name>LINES_REJECTED</name></field><field><id>ERRORS</id><enabled>Y</enabled><name>ERRORS</name></field><field><id>INPUT_BUFFER_ROWS</id><enabled>Y</enabled><name>INPUT_BUFFER_ROWS</name></field><field><id>OUTPUT_BUFFER_ROWS</id><enabled>Y</enabled><name>OUTPUT_BUFFER_ROWS</name></field></perf-log-table>
<channel-log-table><connection/>
<schema/>
<table/>
<timeout_days/>
<field><id>ID_BATCH</id><enabled>Y</enabled><name>ID_BATCH</name></field><field><id>CHANNEL_ID</id><enabled>Y</enabled><name>CHANNEL_ID</name></field><field><id>LOG_DATE</id><enabled>Y</enabled><name>LOG_DATE</name></field><field><id>LOGGING_OBJECT_TYPE</id><enabled>Y</enabled><name>LOGGING_OBJECT_TYPE</name></field><field><id>OBJECT_NAME</id><enabled>Y</enabled><name>OBJECT_NAME</name></field><field><id>OBJECT_COPY</id><enabled>Y</enabled><name>OBJECT_COPY</name></field><field><id>REPOSITORY_DIRECTORY</id><enabled>Y</enabled><name>REPOSITORY_DIRECTORY</name></field><field><id>FILENAME</id><enabled>Y</enabled><name>FILENAME</name></field><field><id>OBJECT_ID</id><enabled>Y</enabled><name>OBJECT_ID</name></field><field><id>OBJECT_REVISION</id><enabled>Y</enabled><name>OBJECT_REVISION</name></field><field><id>PARENT_CHANNEL_ID</id><enabled>Y</enabled><name>PARENT_CHANNEL_ID</name></field><field><id>ROOT_CHANNEL_ID</id><enabled>Y</enabled><name>ROOT_CHANNEL_ID</name></field></channel-log-table>
<step-log-table><connection/>
<schema/>
<table/>
<timeout_days/>
<field><id>ID_BATCH</id><enabled>Y</enabled><name>ID_BATCH</name></field><field><id>CHANNEL_ID</id><enabled>Y</enabled><name>CHANNEL_ID</name></field><field><id>LOG_DATE</id><enabled>Y</enabled><name>LOG_DATE</name></field><field><id>TRANSNAME</id><enabled>Y</enabled><name>TRANSNAME</name></field><field><id>STEPNAME</id><enabled>Y</enabled><name>STEPNAME</name></field><field><id>STEP_COPY</id><enabled>Y</enabled><name>STEP_COPY</name></field><field><id>LINES_READ</id><enabled>Y</enabled><name>LINES_READ</name></field><field><id>LINES_WRITTEN</id><enabled>Y</enabled><name>LINES_WRITTEN</name></field><field><id>LINES_UPDATED</id><enabled>Y</enabled><name>LINES_UPDATED</name></field><field><id>LINES_INPUT</id><enabled>Y</enabled><name>LINES_INPUT</name></field><field><id>LINES_OUTPUT</id><enabled>Y</enabled><name>LINES_OUTPUT</name></field><field><id>LINES_REJECTED</id><enabled>Y</enabled><name>LINES_REJECTED</name></field><field><id>ERRORS</id><enabled>Y</enabled><name>ERRORS</name></field><field><id>LOG_FIELD</id><enabled>N</enabled><name>LOG_FIELD</name></field></step-log-table>
    </log>
    <maxdate>
      <connection/>
      <table/>
      <field/>
      <offset>0.0</offset>
      <maxdiff>0.0</maxdiff>
    </maxdate>
    <size_rowset>10000</size_rowset>
    <sleep_time_empty>50</sleep_time_empty>
    <sleep_time_full>50</sleep_time_full>
    <unique_connections>N</unique_connections>
    <feedback_shown>Y</feedback_shown>
    <feedback_size>50000</feedback_size>
    <using_thread_priorities>Y</using_thread_priorities>
    <shared_objects_file/>
    <capture_step_performance>N</capture_step_performance>
    <step_performance_capturing_delay>1000</step_performance_capturing_delay>
    <step_performance_capturing_size_limit>100</step_performance_capturing_size_limit>
    <dependencies>
    </dependencies>
    <partitionschemas>
    </partitionschemas>
    <slaveservers>
    </slaveservers>
    <clusterschemas>
    </clusterschemas>
  <created_user>-</created_user>
  <created_date>2019&#47;07&#47;12 21:09:36.678</created_date>
  <modified_user>-</modified_user>
  <modified_date>2019&#47;07&#47;12 21:09:36.678</modified_date>
  </info>
  <notepads>
  </notepads>
  <order>
  </order>
  <!-- hier der step -->
      <xsl:call-template name="core">
      
      </xsl:call-template>
  

  <step_error_handling>
  </step_error_handling>
   <slave-step-copy-partition-distribution>
</slave-step-copy-partition-distribution>
   <slave_transformation>N</slave_transformation>
</transformation>


</xsl:template>



</xsl:stylesheet>

  
