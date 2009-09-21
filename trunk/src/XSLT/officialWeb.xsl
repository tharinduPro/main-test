<?xml version="1.0" encoding="gbk"?>
<xsl:stylesheet version="2.0" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="html" version="1.0" indent="yes"/>
	<xsl:template match="xhtml:head"/>
	<xsl:template match="xhtml:body">
		<html>
			<body>
				<table>
					<tbody>
                        <xsl:apply-templates select="xhtml:table[2]/xhtml:tbody/xhtml:tr/xhtml:td[2]/xhtml:table[1]/xhtml:tbody/xhtml:tr[2]/xhtml:td/xhtml:table/xhtml:tbody/xhtml:tr/xhtml:td/xhtml:p/xhtml:a"/>
					</tbody>
				</table>
			</body>
		</html>
	</xsl:template>
    <xsl:template match="xhtml:table[2]/xhtml:tbody/xhtml:tr/xhtml:td[2]/xhtml:table[1]/xhtml:tbody/xhtml:tr[2]/xhtml:td/xhtml:table/xhtml:tbody/xhtml:tr/xhtml:td/xhtml:p/xhtml:a">
	</xsl:template>
</xsl:stylesheet>
