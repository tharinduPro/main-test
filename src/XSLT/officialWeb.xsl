<?xml version="1.0" encoding="gb2312"?>
<xsl:stylesheet version="2.0" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
    <xsl:output method="html" version="1.0" indent="yes"/>
	<xsl:template match="/">
        <html>
			<body>
                    <xsl:value-of select="//xhtml:html/xhtml:head/xhtml:title"/>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>

