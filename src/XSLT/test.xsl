<?xml version="1.0"?>

<xsl:stylesheet version="2.0"
  xmlns:xhtml="http://www.w3.org/1999/xhtml"
  xmlns="http://www.w3.org/1999/xhtml"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
  exclude-result-prefixes="xhtml xsl xs">

<xsl:output method="xml" version="1.0" encoding="UTF-8" doctype-public="-//W3C//DTD XHTML 1.1//EN" doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd" indent="yes"/>


<!-- template for the body section. Only needed if we want to change, delete or add nodes. In our case we need it to add a div element containing a menu of navigation. -->

<xsl:template match="xhtml:body">
  <xsl:copy>
    <div class="menu">
      <p><a href="home">Homepage</a> &gt; <strong>Test document</strong></p>
    </div>
    <xsl:call-template name="item"/>
  </xsl:copy>
</xsl:template>

	<xsl:template name="item">
		<xsl:value-of select="//xhtml:tbody/xhtml:tr/xhtml:td/xhtml:span"/>
	</xsl:template>
</xsl:stylesheet>

