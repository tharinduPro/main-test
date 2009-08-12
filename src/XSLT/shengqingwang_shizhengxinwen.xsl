<?xml version="1.0" encoding="gb2312"?>
<xsl:stylesheet version="2.0" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
    <xsl:output method="html" version="1.0" indent="yes"/>
	<xsl:template match="/">
	<div align="left">
				<table border="0">
                    <xsl:for-each select="//body/table[2]/tr[1]/td[2]/table[2]/tr[1]/td[1]/table[2]/tr[2]/td[1]/table[1]">
						<tr>
							<td>
                                <font color="#ff9900">·</font> 
                                <xsl:value-of select="tr/td[1]/a[1]/font"/>
                                <xsl:value-of select="tr/td[1]/a[2]/font"/>
							</td>
						</tr>
					</xsl:for-each>
				</table>
	</div>
	</xsl:template>
</xsl:stylesheet>

