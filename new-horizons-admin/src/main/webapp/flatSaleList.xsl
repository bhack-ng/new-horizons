<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <html>
            <body>
                <h2>Продажа квартир</h2>
                <table cellspacing="5" cellpadding="10" border="1" width="100%">
                    <tr bgcolor="#9acd32">
                        <th>номер лота</th>
                        <th>Адрес</th>
                        <th>Метро</th>
                        <th>Цена</th>
                        <th>Площадь</th>
                        <th>Этаж</th>
                        <th>Телефон</th>
                        <th>Зметки</th>
                        <th>Фото</th>
                    </tr>
                    <xsl:for-each select="//offer">
                        <tr>
                            <td>
                                <a >
                                    <xsl:attribute name="href">
                                        flatSale.xml?id=<xsl:value-of select="id"/>
                                    </xsl:attribute>
                                    <xsl:value-of select="id"/>
                                </a>
                            </td>
                            <td>
                                <xsl:apply-templates select="address"/>
                            </td>
                            <td>
                                <xsl:apply-templates select="metro"/>
                            </td>
                            <td>
                                <xsl:apply-templates select="price"/>
                            </td>
                            <td>
                                <xsl:apply-templates select="area"/>
                            </td>
                            <td>
                                <xsl:apply-templates select="floor"/>
                            </td>
                            <td>
                                <xsl:value-of select="phone"/>
                            </td>
                            <td width="200">
                                <xsl:value-of select="note"/>
                            </td>
                            <td>
                                <xsl:apply-templates select="photo[1]"/>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
                Сгенерированно  системой "<a href="http://www.simplex-software.ru/">Новые горизонты</a>"
            </body>
        </html>
    </xsl:template>
    <xsl:template match="address">

        <xsl:value-of select="@locality"/>, <xsl:value-of select="@street"/>, <xsl:value-of select="@house_str"/>

    </xsl:template>

    <xsl:template match="metro">

        Id: <xsl:value-of select="@id"/>
        <br/><xsl:if test="@wtime">Пешком: <xsl:value-of select="@wtime"/></xsl:if>
        <br/><xsl:if test="@ttime">Транспортом: <xsl:value-of select="@ttime"/></xsl:if>

    </xsl:template>

    <xsl:template match="price">

        Цена: <xsl:value-of select="."/>
        <xsl:value-of select="@currency"/>

    </xsl:template>

    <xsl:template match="area">

        По комнатам: <xsl:value-of select="@rooms"/>
        <br/>
        Жилая: <xsl:value-of select="@living"/>
        <br/>
        Кухня: <xsl:value-of select="@kitchen"/>
        <br/>
        Общая: <xsl:value-of select="@total"/>


    </xsl:template>

    <xsl:template match="floor">

        Этаж <xsl:value-of select="."/> <br/>из <xsl:value-of select="@total"/> этажей

    </xsl:template>


    <xsl:template match="photo">


        <xsl:element name="img">
            <xsl:attribute name="width"> 200px</xsl:attribute>
            <xsl:attribute name="height">150px </xsl:attribute>
            <!--="200" height="150"-->
            <xsl:attribute name="src"> <xsl:value-of select="."/>  </xsl:attribute>
        </xsl:element>


    </xsl:template>

</xsl:stylesheet>