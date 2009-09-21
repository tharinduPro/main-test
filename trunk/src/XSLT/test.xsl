<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="html" version="1.0" indent="yes"/>
	<xsl:template match="xhtml:head"/>
	<xsl:template match="xhtml:body">
		<html>
			<body>
				<table>
					<tbody>
                        <xsl:apply-templates select="//xhtml:html/xhtml:body/xhtml:table/xhtml:tr/xhtml:td/xhtml:p/xhtml:a"/>
					</tbody>
				</table>
			</body>
		</html>
	</xsl:template>

    <xsl:template match="//xhtml:html/xhtml:body/xhtml:table/xhtml:tr/xhtml:td/xhtml:p/xhtml:a">
        <xsl:value-of select="node()" />
	</xsl:template>
</xsl:stylesheet>
