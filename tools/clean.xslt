<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:svg="http://www.w3.org/2000/svg" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:output method="xml" indent="no" omit-xml-declaration="yes" />
	<xsl:strip-space elements="x" />

	<xsl:template match="svg:*">
		<xsl:element name="{local-name()}">
			<xsl:copy-of select="@*[namespace-uri()='' or namespace-uri()='http://www.w3.org/1999/xlink']" />
			<xsl:apply-templates />
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="comment()|processing-instruction()" />
	<xsl:template match="svg:metadata" />

</xsl:stylesheet>