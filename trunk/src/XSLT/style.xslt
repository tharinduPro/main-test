<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="gb2312" indent="yes"/>
	<xsl:template match="/">
	<div align="left">
				<table border="0">
					<xsl:for-each select="//channel/item[position()&lt;11]">
						<tr>
							<td>
								<li>
									<a target="_blank" href="{link}" class="news-item-title">
										<xsl:value-of select="title"/>
									</a>
									<span class="news-item-excerpt"/>
								</li>
							</td>
						</tr>
					</xsl:for-each>
				</table>
	</div>
	</xsl:template>
</xsl:stylesheet>
