<xsl:stylesheet version="2.0" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="gb2312" indent="yes"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>
                   国内新闻
                </title>
                <link type="text/css" rel="stylesheet" href="css/style.css"/>
                <link type="text/css" rel="stylesheet" href="css/admin.css"/>
            </head>
            <body>
                <div class="news">
                    <ul>
                        <xsl:apply-templates select="//xhtml:html/xhtml:body/xhtml:form/xhtml:table/xhtml:tr[4]/xhtml:td/xhtml:table/xhtml:tr[1]/xhtml:td[2]/xhtml:table/xhtml:tr[9]/xhtml:td/xhtml:table/xhtml:tr">
                        </xsl:apply-templates>
                    </ul>
                </div>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="//xhtml:html/xhtml:body/xhtml:form/xhtml:table/xhtml:tr[4]/xhtml:td/xhtml:table/xhtml:tr[1]/xhtml:td[2]/xhtml:table/xhtml:tr[9]/xhtml:td/xhtml:table/xhtml:tr">
                    <xsl:apply-templates select="xhtml:td[2]/xhtml:a">
                    </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="xhtml:td[2]/xhtml:a">
        <li>*
            <a target="_blank">
                <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
                <xsl:value-of select="."/>
            </a>
        </li>
    </xsl:template>

</xsl:stylesheet>
