<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright © 2015-2016 Nicolas Prandstaetter


THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
-->


<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:param name="waid"/>
<!-- <xsl:param name="takeoff"/>   -->
<xsl:param name="public"/>
<xsl:param name="transport"/>
<xsl:param name="login"/>
<xsl:param name="thisapp"/>
<xsl:param name="project_dir"/>
<xsl:param name="clone_project_dir_on"/>


<xsl:param name="uri_wf_bits"/>
<xsl:param name="uri_wf_prov_pro"/>
<xsl:param name="uri_rf_bits"/>
<xsl:param name="uri_rf_prov_app"/>
<xsl:param name="uri_rf_prov_pro"/>
<xsl:param name="uri_wd_bits"/>
<xsl:param name="uri_wd_prov_pro"/>
<xsl:param name="uri_rd_bits"/>
<xsl:param name="uri_rd_prov_app"/>
<xsl:param name="uri_rd_prov_pro"/>

<xsl:param name="firstjob"/>
<xsl:param name="header"/>
<xsl:param name="header_job"/>
<xsl:param name="footer"/>
<xsl:param name="footer_job"/>

<xsl:param name="css"/>


<xsl:param name="trace_on"/>
<xsl:param name="role"/>
<xsl:param name="bits"/>
<xsl:param name="ext_ons"/>
<xsl:param name="ext_ebits"/>                            
<xsl:param name="ext_pro_check_string"/>
<xsl:param name="ext_pro_load_string"/>
<xsl:param name="ext_password_string"/>
<xsl:param name="ext_passwords"/>
<xsl:param name="ext_mediathek_string"/>
<xsl:param name="ext_runtime_string"/>
<xsl:param name="ext_ext_embedjs_string"/>
<xsl:param name="ext_mydata_string"/>
<xsl:param name="ext_guiprivs"/>
<xsl:param name="ext_file_load_string"/>
<xsl:param name="ext_tk_string"/>
<xsl:param name="ext_interpreter_string"/>
<xsl:param name="ext_ws_string"/>


<xsl:output method="xml" indent="yes"/>

<xsl:template match="/">
   <xsl:apply-templates/>
</xsl:template>

<xsl:template match="appconfs">

    <xsl:apply-templates select="appconf[@row=$waid]"/>

</xsl:template>
<xsl:template name="get-context-param" >
    <xsl:param name="para_name"/>
    <xsl:param name="para_value"/>
    
    <xsl:if test="string-length($para_value) &gt; 0">
	     <context-param>
        <param-name><xsl:value-of select="$para_name" /></param-name>
        <param-value><xsl:value-of select="$para_value" /></param-value>
       </context-param>    
    </xsl:if>
    
</xsl:template>    
    
<xsl:template match="appconf">
<web-app id="WebApp_ID" version="2.4" xmlns="http://java.sun.com/xml/ns/j2ee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">
	<display-name><xsl:value-of select="@displayname" /></display-name>
  
  <xsl:if test="string-length($login) &gt; 0">
      <xsl:if test="not($login='none')">

        <security-role>
        <role-name><xsl:value-of select="$login"/></role-name>
       </security-role>
      
       <login-config>
        <auth-method>BASIC</auth-method>
       </login-config>
      
       <security-constraint>
        <web-resource-collection>
         <web-resource-name>CONFITEST</web-resource-name>
         <http-method>POST</http-method>
         <http-method>GET</http-method>
         <url-pattern>/</url-pattern>
        </web-resource-collection>
        <auth-constraint>
         <role-name><xsl:value-of select="$login"/></role-name>
        </auth-constraint>
        <user-data-constraint>
         <!-- transport-guarantee can be CONFIDENTIAL, INTEGRAL, or NONE -->
         <transport-guarantee><xsl:value-of select="$transport"/></transport-guarantee>
        </user-data-constraint>
       </security-constraint>
    

    </xsl:if>
  </xsl:if>
  
  <listener>
    <listener-class>
      org.apache.commons.fileupload.servlet.FileCleanerCleanup
    </listener-class>
  </listener>
  
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">app</xsl:with-param>
    <xsl:with-param name="para_value" select="@app"/>
  </xsl:call-template> 
  

  <!-- 
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">takeoff</xsl:with-param>
    <xsl:with-param name="para_value" select="$takeoff"/>
  </xsl:call-template>  
  -->
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">public</xsl:with-param>
    <xsl:with-param name="para_value" select="$public"/>
  </xsl:call-template> 
     
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">project_dir</xsl:with-param>
    <xsl:with-param name="para_value" select="$project_dir"/>
  </xsl:call-template>  
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">clone_project_dir_on</xsl:with-param>
    <xsl:with-param name="para_value" select="$clone_project_dir_on"/>
  </xsl:call-template> 

  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">css</xsl:with-param>
    <xsl:with-param name="para_value" select="$css"/>
  </xsl:call-template>  


  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">first_job</xsl:with-param>
    <xsl:with-param name="para_value" select="$firstjob"/>
  </xsl:call-template>     
  
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">firstjob</xsl:with-param>
    <xsl:with-param name="para_value" select="$firstjob"/>
  </xsl:call-template>  
    
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">header</xsl:with-param>
    <xsl:with-param name="para_value" select="$header"/>
  </xsl:call-template> 
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">header_job</xsl:with-param>
    <xsl:with-param name="para_value" select="$header_job"/>
  </xsl:call-template>  
  
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">footer</xsl:with-param>
    <xsl:with-param name="para_value" select="$footer"/>
  </xsl:call-template> 
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">footer_job</xsl:with-param>
    <xsl:with-param name="para_value" select="$footer_job"/>
  </xsl:call-template>     
 

  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">trace_on</xsl:with-param>
    <xsl:with-param name="para_value" select="$trace_on"/>
  </xsl:call-template>  
  
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">role</xsl:with-param>
    <xsl:with-param name="para_value" select="$role"/>
  </xsl:call-template> 
  
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">bits</xsl:with-param>
    <xsl:with-param name="para_value" select="$bits"/>
  </xsl:call-template>   
  
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">uri_wf_bits</xsl:with-param>
    <xsl:with-param name="para_value" select="$uri_wf_bits"/>
  </xsl:call-template> 
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">uri_wf_prov_pro</xsl:with-param>
    <xsl:with-param name="para_value" select="$uri_wf_prov_pro"/>
  </xsl:call-template>
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">uri_rf_bits</xsl:with-param>
    <xsl:with-param name="para_value" select="$uri_rf_bits"/>
  </xsl:call-template>
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">uri_rf_prov_app</xsl:with-param>
    <xsl:with-param name="para_value" select="$uri_rf_prov_app"/>
  </xsl:call-template>
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">uri_rf_prov_pro</xsl:with-param>
    <xsl:with-param name="para_value" select="$uri_rf_prov_pro"/>
  </xsl:call-template>
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">uri_wd_bits</xsl:with-param>
    <xsl:with-param name="para_value" select="$uri_wd_bits"/>
  </xsl:call-template>    
 
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">uri_wd_prov_pro</xsl:with-param>
    <xsl:with-param name="para_value" select="$uri_wd_prov_pro"/>
  </xsl:call-template>
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">uri_rd_bits</xsl:with-param>
    <xsl:with-param name="para_value" select="$uri_rd_bits"/>
  </xsl:call-template>
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">uri_rd_prov_app</xsl:with-param>
    <xsl:with-param name="para_value" select="$uri_rd_prov_app"/>
  </xsl:call-template> 
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">uri_rd_prov_pro</xsl:with-param>
    <xsl:with-param name="para_value" select="$uri_rd_prov_pro"/>
  </xsl:call-template>        
      

  
  <!-- asdf -->
  
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_ons</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_ons"/>
  </xsl:call-template> 
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_ebits</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_ebits"/>
  </xsl:call-template>       
   
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_pro_check_string</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_pro_check_string"/>
  </xsl:call-template>   
  
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_pro_load_string</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_pro_load_string"/>
  </xsl:call-template>  
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_password_string</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_password_string"/>
  </xsl:call-template>    
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_passwords</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_passwords"/>
  </xsl:call-template>    

  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_mediathek_string</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_mediathek_string"/>
  </xsl:call-template>    
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_runtime_string</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_runtime_string"/>
  </xsl:call-template>    
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_ext_embedjs_string</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_ext_embedjs_string"/>
  </xsl:call-template>  
  
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_mydata_string</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_mydata_string"/>
  </xsl:call-template> 
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_guiprivs</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_guiprivs"/>
  </xsl:call-template>   
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_file_load_string</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_file_load_string"/>
  </xsl:call-template>    
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_tk_string</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_tk_string"/>
  </xsl:call-template>    
  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_interpreter_string</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_interpreter_string"/>
  </xsl:call-template>    

  <xsl:call-template name="get-context-param">
    <xsl:with-param name="para_name">ext_ws_string</xsl:with-param>
    <xsl:with-param name="para_value" select="$ext_ws_string"/>
  </xsl:call-template>    
  
               
    		
    <servlet>
        <servlet-name>MasterServlet</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.MasterServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>MasterServlet</servlet-name>
        <url-pattern>/go</url-pattern>
    </servlet-mapping>  
 
     <servlet>
        <servlet-name>ServletWishUploadPerformed</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletWishUploadPerformed</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>ServletWishUploadPerformed</servlet-name>
        <url-pattern>/performUpload</url-pattern>
        
    </servlet-mapping> 
    
      <servlet>
        <servlet-name>ServletWishUpload</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletWishUpload</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>ServletWishUpload</servlet-name>
        <url-pattern>/tkWishUpload</url-pattern>
    </servlet-mapping>    
 
    <servlet>
        <servlet-name>ServletWishChoose</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletWishChoose</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>ServletWishChoose</servlet-name>
        <url-pattern>/tkWishChoose</url-pattern>
    </servlet-mapping>    
    
    <servlet>
    <servlet-name>ProjectDecrypt</servlet-name>
    <servlet-class>at.nicolas.x.web.servlet.ServletDecrypt</servlet-class>
    </servlet>
    
    <servlet-mapping>
    <servlet-name>ProjectDecrypt</servlet-name>
    <url-pattern>/decrypt</url-pattern>
    </servlet-mapping>    
    
    <servlet>
    <servlet-name>ProjectToolbox</servlet-name>
    <servlet-class>at.nicolas.x.web.servlet.ServletToolbox</servlet-class>
    </servlet>
    
    <servlet-mapping>
    <servlet-name>ProjectToolbox</servlet-name>
    <url-pattern>/toolbox</url-pattern>
    </servlet-mapping>    
    
    <servlet>
    <servlet-name>ProjectSimplelogin</servlet-name>
    <servlet-class>at.nicolas.x.web.servlet.ServletSimplelogin</servlet-class>
    </servlet>
    
    <servlet-mapping>
    <servlet-name>ProjectSimplelogin</servlet-name>
    <url-pattern>/simplelogin</url-pattern>
    </servlet-mapping>       
               
        
    <servlet>
        <servlet-name>ServletClick</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletClick</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>ServletClick</servlet-name>
        <url-pattern>/click</url-pattern>
    </servlet-mapping>  
    
    <servlet>
        <servlet-name>ProjectDb</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletProjectDb</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>ProjectDb</servlet-name>
        <url-pattern>/projectDb</url-pattern>
    </servlet-mapping>    
    
    <servlet>
        <servlet-name>ProjectDbCheckin</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletProjectDbCheckin</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>ProjectDbCheckin</servlet-name>
        <url-pattern>/projectDbCheckin</url-pattern>
    </servlet-mapping>  
    
    <servlet>
        <servlet-name>ProjectDbLogin</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletLogin2</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>ProjectDbLogin</servlet-name>
        <url-pattern>/projectDbLogin</url-pattern>
    </servlet-mapping>      
    
    <servlet>
        <servlet-name>ProjectDbCheckout</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletProjectDbCheckout</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>ProjectDbCheckout</servlet-name>
        <url-pattern>/projectDbCheckout</url-pattern>
    </servlet-mapping>           
    
    <servlet>
        <servlet-name>TkOk</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletTkOk</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>TkOk</servlet-name>
        <url-pattern>/tkOk</url-pattern>
    </servlet-mapping>  
    
    <servlet>
        <servlet-name>TkRequestFast</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletTkRequestFast</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>TkRequestFast</servlet-name>
        <url-pattern>/tkRequestFast</url-pattern>
    </servlet-mapping>    
    
    <servlet>
        <servlet-name>TkRequestSequence</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletTkRequestSequence</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>TkRequestSequence</servlet-name>
        <url-pattern>/tkRequestSequence</url-pattern>
    </servlet-mapping>        
    
    <servlet>
        <servlet-name>TkCancel</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletTkCancel</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>TkCancel</servlet-name>
        <url-pattern>/tkCancel</url-pattern>
    </servlet-mapping>     
    
    <servlet>
        <servlet-name>Quit1</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletQuit1</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>Quit1</servlet-name>
        <url-pattern>/quit1</url-pattern>
    </servlet-mapping>      
    
    <servlet>
        <servlet-name>Quit2</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletQuit2</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>Quit2</servlet-name>
        <url-pattern>/quit2</url-pattern>
    </servlet-mapping>    
    
    <!-- edit -->
    <servlet>
        <servlet-name>TkEdit</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletEdit</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>TkEdit</servlet-name>
        <url-pattern>/tkEdit</url-pattern>
    </servlet-mapping>   
    
    <!-- lauratius -->
    <servlet>
        <servlet-name>TkLogin</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletLogin</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>TkLogin</servlet-name>
        <url-pattern>/PdBLogin</url-pattern>
    </servlet-mapping>             
    
    <servlet>
        <servlet-name>TkEditOk</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletTkOkEdit</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>TkEditOk</servlet-name>
        <url-pattern>/tkOkEdit</url-pattern>
    </servlet-mapping>    
    
    <servlet>
        <servlet-name>TkEditCancel</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletTkCancelEdit</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>TkEditCancel</servlet-name>
        <url-pattern>/tkCancelEdit</url-pattern>
    </servlet-mapping>            
    
    <!-- ip -->
    <servlet>
        <servlet-name>TkOkIp</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletTkOkIp</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>TkOkIp</servlet-name>
        <url-pattern>/tkOkIp</url-pattern>
    </servlet-mapping>  
    
    <servlet>
        <servlet-name>TkCancelIp</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletTkCancelIp</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>TkCancelIp</servlet-name>
        <url-pattern>/tkCancelIp</url-pattern>
    </servlet-mapping>      
    
    <!-- info -->
    
    <servlet>
        <servlet-name>Info</servlet-name>
        <servlet-class>at.nicolas.x.web.servlet.ServletInfo</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>Info</servlet-name>
        <url-pattern>/info</url-pattern>
    </servlet-mapping>  
        
	<listener>
        <listener-class>at.nicolas.x.web.listener.MoralesTrapProjectHttpSession</listener-class> 
    </listener>	
    <listener>
          <listener-class>at.nicolas.x.web.listener.TrapSystemServletContext</listener-class>
 	</listener>    

	<welcome-file-list>
	<welcome-file>go</welcome-file>
		<welcome-file>index.html</welcome-file>
		<welcome-file>index.htm</welcome-file>
		<welcome-file>index.jsp</welcome-file>
		<welcome-file>default.html</welcome-file>
		<welcome-file>default.htm</welcome-file>
		<welcome-file>default.jsp</welcome-file>
	</welcome-file-list>


</web-app>
</xsl:template>

</xsl:stylesheet>

  