<xsl:stylesheet version="2.0" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="gb2312" indent="yes"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>
                今日中国
            </title>
            </head>
            <body>
                <table>
                    <xsl:apply-templates select="//xhtml:html/xhtml:body/xhtml:table[4]/xhtml:tr/xhtml:td[5]/xhtml:table[5]/xhtml:tr/xhtml:td[3]/xhtml:table[3]/xhtml:tr">
                    </xsl:apply-templates>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="//xhtml:html/xhtml:body/xhtml:table[4]/xhtml:tr/xhtml:td[5]/xhtml:table[5]/xhtml:tr/xhtml:td[3]/xhtml:table[3]/xhtml:tr">
        <tr>
            <td>
                    <xsl:apply-templates select="xhtml:td[2]/xhtml:a">
                    </xsl:apply-templates>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="xhtml:td[2]/xhtml:a">
        <a target="_blank">
            <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
            <xsl:value-of select="."/>
        </a>
    </xsl:template>

</xsl:stylesheet>
