<?xml version='1.0' encoding="utf-8"?>
<!-- $Id: log_categories.xslt 13732 2008-11-04 11:37:58Z jz $ -->

<xsl:stylesheet xmlns:xsl   = "http://www.w3.org/1999/XSL/Transform"
                xmlns:msxsl = "urn:schemas-microsoft-com:xslt"
                version     = "1.0">

    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~log_categories-->
    
    <xsl:template match="log_categories">

        <table cellspacing="0" cellpadding="0" width="100%">
            <tr>
                <td>
                    <h2>scheduler.log: log categories</h2>
                </td>
                <td style="text-align: right">
                    <img alt="logo" width="45" height="45" src="job_scheduler_rabbit_circle_45x45.gif"/>
                </td>
            </tr>
        </table>


        <table cellspacing="0" cellpadding="0" width="100%">
            <tr>
                <td>
                    <xsl:value-of select="@categories"/>
                </td>
                <td style="text-align: right">
                    Reset after
                    <input type="text" size="4" id="delay_input" value="86400"/>
                    <label class="standard_size" for="delay_input">seconds</label>


                    <span style="margin-left: 5ex"> </span>
                    <button onclick="reset_button_onclick()">Reset now</button>
                </td>
            </tr>
        </table>

        
        <table cellspacing="0" cellpadding="0">
            <thead>
                <th>
                </th>
                <th>
                    Used
                </th>
                <th>
                    Denied
                </th>
                <th>
                    Children
                </th>
                <th>
                    Default
                </th>
                <th>
                </th>
                <th>
                </th>
            </thead>
            <tbody>
                <xsl:for-each select="log_category">
                    <xsl:sort select="@path"/>

                    <tr>
                        <td>
                            <xsl:element name="input">
                                <xsl:attribute name="type">checkbox</xsl:attribute>
                                <xsl:attribute name="id">
                                    <xsl:text>checkbox_log_category__</xsl:text>
                                    <xsl:value-of select="@path"/>
                                </xsl:attribute>
                                <xsl:if test="@value='1'">
                                    <xsl:attribute name="checked">checked</xsl:attribute>
                                </xsl:if>
                                <xsl:attribute name="onclick">execute_input()</xsl:attribute>
                            </xsl:element>

                            <xsl:element name="label">
                                <xsl:attribute name="for">
                                    <xsl:text>checkbox_log_category__</xsl:text>
                                    <xsl:value-of select="@path"/>
                                </xsl:attribute>
                                <xsl:attribute name="class">standard_size</xsl:attribute>
                                <xsl:attribute name="style">
                                    <xsl:if test="@value='1'">
                                        <xsl:text>font-weight: bold; </xsl:text>
                                    </xsl:if>
                                </xsl:attribute>
                                <xsl:value-of select="@path"/>
                            </xsl:element>
                        </td>
                        <td style="text-align: right">
                            <xsl:if test="@used_counter != '0'">
                                <xsl:value-of select="@used_counter"/>
                            </xsl:if>
                        </td>
                        <td style="text-align: right">
                            <xsl:if test="@denied_counter != '0'">
                                <xsl:value-of select="@denied_counter"/>
                            </xsl:if>
                        </td>
                        <td>
                            <xsl:if test="@children_too='yes'">
                                <xsl:text>children too</xsl:text>
                            </xsl:if>
                        </td>
                        <td>
                            <xsl:if test="@has_default='yes'">
                                <xsl:choose>
                                    <xsl:when test="@default='yes'">
                                        default=on
                                    </xsl:when>
                                    <xsl:when test="@default='no'">
                                        default=off
                                    </xsl:when>
                                </xsl:choose>
                            </xsl:if>
                        </td>

                        <!--
                        <xsl:if test="//log_category [ @children_derived ] or //log_category [ @has_default ] or //log_category [ @default ]">
                        <!- - Zum Debuggen - Anfang - ->
                        <td class="small">
                            <xsl:if test="@children_too_derived='yes'">
                                <xsl:text>children too derived</xsl:text>
                            </xsl:if>
                        </td>
                        <td class="small">
                            <xsl:if test="@has_default='yes'">
                                <xsl:text>has_default</xsl:text>
                            </xsl:if>
                        </td>
                        <td class="small">
                            <xsl:value-of select="@default"/>
                        </td>
                        </xsl:if>
                        <!- - Zum Debuggen - Ende -->

                        <td>
                            <xsl:choose>
                                <xsl:when test="@mode='implicit'">
                                    <xsl:text>implicit</xsl:text>
                                </xsl:when>
                                <xsl:when test="@mode='explicit'">
                                    <xsl:text>explicit</xsl:text>
                                </xsl:when>
                            </xsl:choose>
                        </td>
                        <td>
                            &#160;
                            <xsl:value-of select="@title"/>
                        </td>
                    </tr>
                </xsl:for-each>
            </tbody>
        </table>

        <p class="small">
            <xsl:value-of select="@debug"/>
        </p>

        <!--button type="submit" onclick="submit_button_onclick()">Apply</button-->

        
    </xsl:template>
    
</xsl:stylesheet>
