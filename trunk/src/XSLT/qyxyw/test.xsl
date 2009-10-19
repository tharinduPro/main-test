<xsl:stylesheet version="2.0" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" version="1.0" encoding="gb2312" indent="yes"/>
    <xsl:template match="/">
        <html>
            <head>
                <title>
                   国内新闻
                </title>
                <link type="text/css" rel="stylesheet" href="../css/style.css"/>
                <link type="text/css" rel="stylesheet" href="../css/admin.css"/>
                <script type="text/javascript" src="test.js"></script>
            </head>
            <body>
                <div style="display:none">
                    <form id="form1" method="post" target="_blank">
                        <input type="hidden" name="url" id="url"/>
                        <input type="hidden" name="xslt" id="xslt" />
                    </form>
                </div>
                <div class="news">
                    <ul>
                        <xsl:apply-templates select="//xhtml:html/xhtml:body/xhtml:form/xhtml:table/xhtml:tr[4]/xhtml:td/xhtml:table/xhtml:tr[1]/xhtml:td[2]/xhtml:table/xhtml:tr[12]/xhtml:td/xhtml:table/xhtml:tr">
                        </xsl:apply-templates>
                    </ul>
                </div>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="//xhtml:html/xhtml:body/xhtml:form/xhtml:table/xhtml:tr[4]/xhtml:td/xhtml:table/xhtml:tr[1]/xhtml:td[2]/xhtml:table/xhtml:tr[12]/xhtml:td/xhtml:table/xhtml:tr">
                    <xsl:apply-templates select="xhtml:td[2]/xhtml:a">
                    </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="xhtml:td[2]/xhtml:a">
        <li>*
            <a>
                <xsl:attribute name="onclick">test('<xsl:value-of select="@href"/>', 'test.xslt');return false</xsl:attribute>
                <xsl:attribute name="href"></xsl:attribute>
                <xsl:value-of select="."/>
            </a>
        </li>
    </xsl:template>

</xsl:stylesheet>
