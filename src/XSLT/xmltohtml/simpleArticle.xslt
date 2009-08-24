<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:db="http://ananas.org/2002/docbook/subset">
	<xsl:output method="html"/>
	<xsl:template match="db:article">
		<html>
			<head>
				<title>
					<xsl:value-of select="db:article/db:title"/>
				</title>
			</head>
			<body bgcolor="#FF0000">
				<xsl:apply-templates/>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="db:para">
		<p>
			<xsl:apply-templates/>
		</p>
	</xsl:template>
	<xsl:template match="db:ulink">
		<a href="{@url}">
			<xsl:apply-templates/>
		</a>
	</xsl:template>
	<xsl:template match="db:article/db:title">
		<h1>
			<xsl:apply-templates/>
		</h1>
	</xsl:template>
	<xsl:template match="db:title">
		<h2>
			<xsl:apply-templates/>
		</h2>
	</xsl:template>
	<xsl:template match="db:emphasis[@role='bold']">
		<b>
			<xsl:apply-templates/>
		</b>
	</xsl:template>
	<xsl:template match="db:emphasis">
		<i>
			<xsl:apply-templates/>
		</i>
	</xsl:template>
	<xsl:template match="db:test">
		<center>
			<xsl:apply-templates/>
		</center>	
	</xsl:template>
</xsl:stylesheet>
