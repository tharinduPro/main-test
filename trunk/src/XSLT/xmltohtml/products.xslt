<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!--indent="no" 属性告诉 XSLT 处理器不要缩排 
        HTML文档，这通常会生成一个较小的 HTML 文件，
	    可以使下载更快。开发调试通常为yes-->
	<xsl:output method="html" indent="yes"/>
	<xsl:template match="products">
		<html>
			<head>
				<title>Cascading Style Sheet</title>
				<link rel="stylesheet" type="text/css" href="table.css" title="Style"/>
			</head>
			<body>
				<table>
					<tr class="header">
						<td>Name</td>
						<td>Price</td>
						<td>Description</td>
					</tr>
					<xsl:apply-templates/>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="product">
		<tr class="even">
			<xsl:call-template name="name_price_desc"/>
		</tr>
	</xsl:template>
	<xsl:template match="product[position() mod 2 = 1]">
		<tr class="odd">
			<xsl:call-template name="name_price_desc"/>
		</tr>
	</xsl:template>
	<xsl:template match="product/name" name="price">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="product/name" name="name">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="product/name" name="description">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template name="name_price_desc">
		<td>
			<xsl:value-of select="name"/>
		</td>
		<td>
			<xsl:value-of select="price"/>
		</td>
		<td>
			<xsl:value-of select="description"/>
		</td>
	</xsl:template>
</xsl:stylesheet>
