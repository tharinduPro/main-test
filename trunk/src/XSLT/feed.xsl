<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html" indent="yes" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"/>
	<xsl:template match="channel">
		<html>
			<head>
				<title></title>
			</head>
			<body>
				<xsl:apply-templates select="item"/>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="item">
		<xsl:if   test= "position()&lt;2 "   > 
			<a href="http://raychou.com/weather/rss.php?id=59289">
				<span style="font-size:10px"><xsl:value-of select="title"/></span>
			</a>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
