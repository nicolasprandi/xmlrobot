<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved. -->

<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="computer" />
<xsl:param name="os" />
<xsl:output method="xml" indent="yes"/>
<xsl:template match="/">

<jobs>
<job id="sel_open_file">
  <xsl:attribute name="computer">
    <xsl:value-of select="$computer" />
  </xsl:attribute>
  <xsl:attribute name="os">
    <xsl:value-of select="$os" />
  </xsl:attribute>  
  <need default="0">(prop,from_open_web)</need>
  <call_service job="sel_open_file_memo_in" />
  <tk label="Open: (f,name_of_file,(file,with))">
    <tk type="sel" id="sel" label="Select a programm" flag="nono" x="default">
      <xsl:choose>
        <xsl:when test="$os='win'">
          <sel label="Windows Default Programm"><xsl:text>default</xsl:text></sel>
          <sel label="Notepad"><xsl:text>editor</xsl:text></sel>           
        </xsl:when>
        <xsl:when test="$os='mac'">
          <sel label="Mac Default Programm"><xsl:text>default</xsl:text></sel>
          <sel label="Texteditor"><xsl:text>editor</xsl:text></sel> 
          <sel label="Safari"><xsl:text>browser</xsl:text></sel>       
        </xsl:when>
        <xsl:when test="$os='linux'">
          <sel label="Linux Default Programm"><xsl:text>default</xsl:text></sel>
          <sel label="Gedit Texteditor"><xsl:text>editor</xsl:text></sel>
	  <sel label="Firefox"><xsl:text>browser</xsl:text></sel>         
        </xsl:when>                
      </xsl:choose>
   
      <xsl:apply-templates select="computers/computer[@computer=$computer]/programm" mode="tk_sel">    
        <xsl:sort select="@sort"/>
      </xsl:apply-templates> 
      <sel label="Open Folder ..."><xsl:text>openfolder</xsl:text></sel>
    </tk>
  </tk>
  <call_service job="sel_open_file_memo_out" />


      <xsl:call-template name="execution">
        <xsl:with-param name="problem">e</xsl:with-param>>
      </xsl:call-template>
</job>

<job id="sel_open_file_typical">
  <xsl:attribute name="computer">
    <xsl:value-of select="$computer" />
  </xsl:attribute>
  <xsl:attribute name="os">
    <xsl:value-of select="$os" />
  </xsl:attribute>  
  <need default="0">(prop,from_open_web)</need>
  <call_service job="sel_open_file_memo_in" />
  <xsl:call-template name="execution">
    <xsl:with-param name="problem">j</xsl:with-param>>
  </xsl:call-template>
</job>  
</jobs>

</xsl:template>
<xsl:template name="execution">
  <xsl:param name="problem"/>

  <choose>
      <xsl:apply-templates select="//computers/computer[@computer=$computer]/programm" mode="exe">    
        <xsl:sort select="@sort"/>
      </xsl:apply-templates>
      <when test="(f,not,(f,has,prop,sel))">  
        <xsl:choose>
          <xsl:when test="$os='win'">
            <exe>
              <arg>explorer.exe</arg>
              <arg>(f,file,classic,(file,with))</arg>
            </exe>      
          </xsl:when>
          <xsl:when test="$os='mac'">
            <exe>
          	  <arg>open</arg>
        		  <arg>-a</arg>
        		  <arg>Finder</arg>		  
          	  <arg>(f,file,classic,(file,with))</arg>
            </exe>	     
          </xsl:when>
          <xsl:when test="$os='linux'">
            <exe>
          	  <arg>xdg-open</arg>
          	  <arg>(f,file,classic,(file,with))</arg>
            </exe>
          </xsl:when>
        </xsl:choose>
      </when>         
      <when test="(f,eq,(f,vob,prop,sel),default)">  
        <xsl:choose>
          <xsl:when test="$os='win'">
            <exe>
              <arg>explorer.exe</arg>
              <arg>(f,file,classic,(file,with))</arg>
            </exe>      
          </xsl:when>
          <xsl:when test="$os='mac'">
            <exe>
          	  <arg>open</arg>
        		  <arg>-a</arg>
        		  <arg>Finder</arg>		  
          	  <arg>(f,file,classic,(file,with))</arg>
            </exe>	     
          </xsl:when>
          <xsl:when test="$os='linux'">
            <exe>
          	  <arg>xdg-open</arg>
          	  <arg>(f,file,classic,(file,with))</arg>
            </exe>
          </xsl:when>
        </xsl:choose>
      </when>       
      <when test="(f,eq,(f,vob,prop,sel),editor)">
        <xsl:choose>
          <xsl:when test="$os='win'">
              <exe>
                <arg>notepad.exe</arg>
                <arg>(f,file,classic,(file,with))</arg>
              </exe>    
          </xsl:when>
          <xsl:when test="$os='mac'">
              <exe>
        			  <arg>/usr/bin/open</arg>
        			  <arg>-a</arg>
        			  <arg>TextEdit</arg>
        			  <arg>(f,file,classic,(file,with))</arg>
        			</exe>    
          </xsl:when>
          <xsl:when test="$os='linux'">
        			<exe>
            		<arg>gedit</arg>
            		<arg>(f,file,classic,(file,with))</arg>
        	    </exe>
          </xsl:when>
        </xsl:choose>
      </when>       
      <when test="(f,eq,(f,vob,prop,sel),openfolder)">    
        <xsl:choose>
          <xsl:when test="$os='win'">
              <exe>
                <arg>explorer.exe</arg>
                <arg>&quot;(f,dir_of_file,(file,with))&quot;</arg>
              </exe>  
          </xsl:when>
          <xsl:when test="$os='mac'">
              <exe>
        			  <arg>/usr/bin/open</arg>
        			  <arg>(f,dir_of_file,(file,with))</arg>
        			</exe>   
          </xsl:when>
          <xsl:when test="$os='linux'">
        			<exe>
                <arg>nautilus</arg>
                <arg>(file,with)</arg>
              </exe>
          </xsl:when>
        </xsl:choose>
      </when>
      <when test="(f,eq,(f,vob,prop,sel),browser)">    
        <xsl:choose>
          <!-- 
          <xsl:when test="$os='win'">
              <exe>
                <arg>explorer.exe</arg>
                <arg>&quot;(f,dir_of_file,(file,with))&quot;</arg>
              </exe>  
          </xsl:when>
          -->
          <xsl:when test="$os='mac'">
      			<exe>
      			  <arg>/usr/bin/open</arg>
      			  <arg>-a</arg>
      			  <arg>Safari</arg>
      			  <arg>(f,file,classic,(file,with))</arg>
      			</exe>
          </xsl:when>
          <xsl:when test="$os='linux'">
        			<exe>
                <arg>firefox</arg>
                <arg>(f,file,classic,(file,with))</arg>
              </exe>
          </xsl:when>
        </xsl:choose>
      </when>          
      <otherwise>
      	<xsl:choose>
      	  <xsl:when test="$problem='j'">
      	    <call_service job="sel_open_file" />  
      	  </xsl:when>
      	  <xsl:otherwise>
      	    <error>There is a serious problem</error>
      	  </xsl:otherwise>
      	</xsl:choose>
      </otherwise>   
    </choose>
  
</xsl:template>

<xsl:template match="programm" mode="exe">
  <when>
    <xsl:attribute name="test">
      <xsl:text>(f,eq,(f,vob,prop,sel),()</xsl:text>
      <xsl:value-of select="@title" />
      <xsl:text>())</xsl:text>
    </xsl:attribute>   
    <xsl:choose>
      <xsl:when test="$os='win'">
        <exe>
          <arg>(f,file,classic,(f,can_file,<xsl:value-of select="@pgm_file" />))</arg>
          <arg>(f,when,(prop,from_open_web),(file,with),(f,replace,(f,file,classic,(f,can_file,(file,with))),/,\))</arg>
        </exe>      
      </xsl:when>
      <xsl:when test="$os='mac'">
        <exe>
      	  <arg>open</arg>
      	  <arg>-a</arg>
      	  <arg><xsl:value-of select="@pgm_app" /></arg>
      	  <arg>(f,when,(prop,from_open_web),(file,with),(f,file,classic,(f,can_file,(file,with))))</arg>
        </exe>      
      </xsl:when>
      <xsl:when test="$os='linux'">
        <exe>
          <arg><xsl:value-of select="@pgm_app" /></arg>
      	  <arg>(f,when,(prop,from_open_web),(file,with),(f,file,classic,(f,can_file,(file,with))))</arg>
        </exe>  
      </xsl:when>
    </xsl:choose>
  </when>   
</xsl:template>

<xsl:template match="programm" mode="tk_sel">
  <sel>()<xsl:value-of select="@title" />()</sel>
</xsl:template>


  <!-- 
  <choose>
    <when test="(f,or,(f,eq,browser,(prop,sel)),(f,eq,default,(prop,sel)),(f,eq,editor,(prop,sel)),(f,eq,openfolder,(prop,sel)))">
      <xsl:comment>Standard</xsl:comment>
      <xsl:choose>
        <xsl:when test="$os='win'">
          <choose>
            <when test="(f,eq,default,(prop,sel))">
              <exe>
                <arg>explorer.exe</arg>
                <arg>(file,with)</arg>
              </exe>
            </when>
            <when test="(f,eq,editor,(prop,sel))">
              <exe>
                <arg>notepad.exe</arg>
                <arg>(file,with)</arg>
              </exe>
            </when>  
            <when test="(f,eq,openfolder,(prop,sel))">
              <exe>
                <arg>explorer.exe</arg>
                <arg>&quot;(f,dir_of_file,(file,with))&quot;</arg>
              </exe>
            </when>                       
          </choose>
          
        </xsl:when>
        <xsl:when test="$os='mac'">
        
          <choose>
            <when test="(f,eq,browser,(prop,sel))">
        			<exe>
        			  <arg>/usr/bin/open</arg>
        			  <arg>-a</arg>
        			  <arg>Safari</arg>
        			  <arg>(file,with)</arg>
        			</exe>
            </when>          
            <when test="(f,eq,default,(prop,sel))">
        			<exe>
        			  <arg>/usr/bin/open</arg>
        			  <arg>-a</arg>
        			  <arg>Finder</arg>			  
        			  <arg>(file,with)</arg>
        			</exe>
            </when>
            <when test="(f,eq,editor,(prop,sel))">
        			<exe>
        			  <arg>/usr/bin/open</arg>
        			  <arg>-a</arg>
        			  <arg>TextEdit</arg>
        			  <arg>(file,with)</arg>
        			</exe>
            </when>  
            <when test="(f,eq,openfolder,(prop,sel))">
        			<exe>
        			  <arg>/usr/bin/open</arg>
        			  <arg>(f,dir_of_file,(file,with))</arg>
        			</exe>
            </when>                       
          </choose>        
        
        </xsl:when>
        <xsl:when test="$os='linux'">
          <choose>
            <when test="(f,eq,default,(prop,sel))">
        	    <exe>
            		<arg>xdg-open</arg>
            		<arg>(file,with)</arg>
        	    </exe>
            </when>
            <when test="(f,eq,editor,(prop,sel))">
        	    <exe>
            		<arg>gedit</arg>
            		<arg>(file,with)</arg>
        	    </exe>
            </when>  
            <when test="(f,eq,openfolder,(prop,sel))">
              <exe>
                <arg>nautilus</arg>
                <arg>(file,with)</arg>
              </exe>
            </when>                       
          </choose>        
        </xsl:when>                
      </xsl:choose>        
    </when>
    <otherwise>
      <xsl:comment>Computer individual</xsl:comment>

    </otherwise>
  </choose>
  -->
</xsl:stylesheet>

  