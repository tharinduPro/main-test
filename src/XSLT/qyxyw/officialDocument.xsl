<xsl:stylesheet version="2.0" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="gb2312" indent="yes"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>
                    公文公报
                </title>
                <link type="text/css" rel="stylesheet" href="css/style.css"/>
                <link type="text/css" rel="stylesheet" href="css/admin.css"/>
            </head>
            <body>
                <table>
					<tbody>
                        <div class="news">
                            <ul>
                                <xsl:apply-templates select="/xhtml:html/xhtml:body/xhtml:table[4]/xhtml:tr/xhtml:td[5]/xhtml:table[6]/xhtml:tr/xhtml:td[1]/xhtml:table[3]/xhtml:tr">
                                </xsl:apply-templates>
                            </ul>
                        </div>
					</tbody>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="/xhtml:html/xhtml:body/xhtml:table[4]/xhtml:tr/xhtml:td[5]/xhtml:table[6]/xhtml:tr/xhtml:td[1]/xhtml:table[3]/xhtml:tr">
	<xsl:if test="position()&lt;6">
            <xsl:apply-templates select="xhtml:td[2]/xhtml:a">
            </xsl:apply-templates>
        </xsl:if>
    </xsl:template>

    <xsl:template match="xhtml:td[2]/xhtml:a">
            <li>*
                <a target="_blank">
                    <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
                    <xsl:value-of select="substring(.,1,25)"/>
                </a>
            </li>
    </xsl:template>

</xsl:stylesheet>
