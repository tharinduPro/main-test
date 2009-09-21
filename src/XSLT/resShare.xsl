<?xml version="1.0" encoding="gb2312"?>
<xsl:stylesheet version="2.0" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="html" version="1.0" indent="yes"/>
	<xsl:template match="xhtml:head"/>
	<xsl:template match="xhtml:body">
		<html>
			<body>
				<table>
					<tbody>
						<xsl:apply-templates select='//xhtml:tbody/xhtml:tr/xhtml:th[@class="common"]'/>
					</tbody>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template match='//xhtml:tbody/xhtml:tr/xhtml:th'>
		<xsl:if test="position()&lt;9">
			<tr>
				<td>
					<a>
						<xsl:attribute name="href"><xsl:value-of select="xhtml:span[@id]/xhtml:a/@href"/></xsl:attribute>
						<xsl:value-of select="xhtml:span[@id]"/>
					</a>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
