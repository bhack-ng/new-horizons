<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xls="http://www.w3.org/1999/XSL/Transform"
                xmlns:my="some.uri" exclude-result-prefixes="my">


    <xsl:template match="/">
        <html>
            <head>

            </head>

            <body>
                <h2>Продажа кватиры/ Номер лота
                    <xsl:apply-templates select="/flats_for_sale/offer[1]/id"/>
                </h2>

                <table align="center">
                    <tr>
                        <td>
                            <table >

                                <tr>
                                    <td>Адрес:</td>
                                    <td>
                                        <xsl:apply-templates select="/flats_for_sale/offer[1]/address"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Метро:</td>
                                    <td></td>
                                    <td>
                                        <xsl:apply-templates select="/flats_for_sale/offer[1]/metro"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Площадь:</td>
                                    <td>
                                        <xsl:apply-templates select="/flats_for_sale/offer[1]/area"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Этаж/Этажность:</td>
                                    <td>
                                        <xsl:apply-templates select="/flats_for_sale/offer[1]/floor"/>
                                    </td>
                                </tr>
                                <!-- тип здания (панельнй ,кирпичный ...)   -->
                                <tr>
                                    <xsl:apply-templates select="/flats_for_sale/offer[1]/floor/@type"/>
                                </tr>

                                <tr>
                                    <td>Серия дома</td>
                                    <td></td>
                                    <td>
                                        <xsl:value-of select="/flats_for_sale/offer[1]/floor/@seria"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>Объявление:</td>
                                    <td></td>
                                    <td>
                                        <xsl:value-of select="/flats_for_sale/offer[1]/note"/>
                                    </td>

                                </tr>

                                <tr>
                                    <td>Цена:</td>
                                    <td><xsl:apply-templates select="/flats_for_sale/offer[1]/price"/></td>
                                </tr>


                                <tr>
                                    <td>Кнтрактные телефоны:</td>
                                    <td></td>
                                    <td>
                                        <xsl:apply-templates select="/flats_for_sale/offer[1]/phone"/>
                                    </td>
                                </tr>

                            </table>


                            <table>
                                <tr>
                                    <xsl:apply-templates select="/flats_for_sale/offer[1]/options"/>
                                </tr>

                                <tr>
                                    <td>Комнаты:</td>
                                    <td>
                                        <xsl:apply-templates select="/flats_for_sale/offer[1]/rooms_num"/>
                                    </td>
                                </tr>

                            </table>

                        </td>
                        <td>
                            <table >

                                <tr>

                                    <td>
                                        <xsl:apply-templates select="/flats_for_sale/offer[1]/photo"/>
                                    </td>
                                </tr>

                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>Сгенерированно системой <a href="http://www.simplex-software.ru/">Новые горизонты</a></td>
                    </tr>
                </table>




            </body>
        </html>
    </xsl:template>
    <xsl:template match="address">
        <td>
            <xsl:value-of select="document('')/*/my:admin_area/area[@id=$adminAreaId]"/>,
            <xsl:value-of select="@locality"/>, <xsl:value-of select="@street"/>,
            <xsl:value-of select="@house_str"/>


        </td>
    </xsl:template>

    <xsl:template match="metro">
        <td>
            Id:
            <xsl:value-of select="@id"/>
            <br/>
            <xsl:if test="@wtime">Пешком:
                <xsl:value-of select="@wtime"/>
            </xsl:if>
            <br/>
            <xsl:if test="@ttime">Транспортом:
                <xsl:value-of select="@ttime"/>
            </xsl:if>
        </td>
    </xsl:template>

    <xsl:template match="price">
        <td>
            <xsl:value-of select="."/>
            <xsl:value-of select="@currency"/>
        </td>
    </xsl:template>

    <xsl:template match="area">
        <td>
            По комнатам:
            <xsl:value-of select="@rooms"/>
            <br/>
            Жилая:
            <xsl:value-of select="@living"/>
            <br/>
            Кухня:
            <xsl:value-of select="@kitchen"/>
            <br/>
            Общая:
            <xsl:value-of select="@total"/>

        </td>
    </xsl:template>
    <xls:template match="options">
        <tr>
            <td><br/>Наличие телефона:</td>
            <td>
                <xsl:value-of select="document('')/*/my:boolType/entry[@key=$hasPhone]"/>
            </td>
        </tr>

        <tr>
            <td>Колличеств балконов:</td>
            <td>
                <xsl:value-of select="@balcon"/>
            </td>
        </tr>
        <tr>
            <td>Колличество грузовых лифтов:</td>
            <td>
                <xsl:value-of select="@lift_g"/>
            </td>
        </tr>
        <tr>
            <td>Колличество пассажирских лифотв:</td>
            <td>
                <xsl:value-of select="@lift_p"/>
            </td>
        </tr>
        <tr>
            <td>Количество совмещенных с/у :</td>
            <td>
                <xsl:value-of select="@su_s"/>
            </td>
        </tr>
        <tr>
            <td>Количество раздельных с/у:</td>
            <td>
                <xsl:value-of select="@su_r"/>
            </td>
        </tr>
        <tr>
            <td>Возможность ипотеки:</td>
            <td><xsl:value-of select="document('')/*/my:OneOrNullType/entry[@key=$hasIpoteka]"/></td>
        </tr>
        <tr>
            <td>Тип жилья:</td>
            <td><xsl:value-of select="document('')/*/my:objectTypeMap/liveType[@key=$objectTypeId]"/></td>
        </tr>
        <tr>
            <td>Окна выхдят на:</td>
            <td><xsl:value-of select="document('')/*/my:windowsMap/entry[@key=$windowsId]"/></td>
        </tr>
        <tr>
            <td>Тип продажи</td>
            <td><xsl:value-of select="document('')/*/my:saleTypeMap/saleType[@key=$saleTypeId]"/></td>
        </tr>
    </xls:template>

    <xsl:template match="floor">
        <td>
            Этаж
            <xsl:value-of select="."/>
            <br/>из
            <xsl:value-of select="@total"/> этажей

        </td>
    </xsl:template>

    <xsl:template match="@type">
        <td>Тип здания:</td>
        <td></td>
        <td><xsl:value-of select="document('')/*/my:buildingType/entry[@key=$buildingType]"/></td>


    </xsl:template>

    <xsl:template match="photo">

        <xsl:element name="img">
            <xsl:attribute name="width">300px</xsl:attribute>
            <xsl:attribute name="height">250px</xsl:attribute>
            <!--="200" height="150"-->
            <xsl:attribute name="src">
                <xsl:value-of select="."/>
            </xsl:attribute>

        </xsl:element>

        <xsl:choose>
            <xsl:when test="(position() mod 2) != 1">
                <br/>
            </xsl:when>
        </xsl:choose>

    </xsl:template>




    <xsl:template match="rooms_num">
        <output>
            <xsl:value-of select="document('')/*/my:map/entry[@key=$roomCount]"/>
        </output>
    </xsl:template>

    <xsl:template match="metro">
        <output>
            <xsl:value-of select="document('')/*/my:metro/location[@id=$metroId]"/>
        </output>
    </xsl:template> 

    <xsl:variable name="metroId" ><xsl:value-of select="/flats_for_sale/offer[1]/metro/@id"/></xsl:variable>
    <xsl:variable name="roomCount" ><xsl:value-of select="/flats_for_sale/offer[1]/rooms_num"/></xsl:variable>
    <xsl:variable name="adminAreaId"><xsl:value-of select="/flats_for_sale/offer[1]/address/@admin_area"/></xsl:variable>
    <xsl:variable name="buildingType"><xsl:value-of select="/flats_for_sale/offer[1]/floor/@type"/></xsl:variable>
    <xsl:variable name="hasPhone"><xsl:value-of select="/flats_for_sale/offer[1]/options/@phone"/></xsl:variable>
    <xsl:variable name="objectTypeId"><xsl:value-of select="/flats_for_sale/offer[1]/options/@object_type"/> </xsl:variable>
    <xsl:variable name="saleTypeId"><xsl:value-of select="/flats_for_sale/offer[1]/options/@sale_type"/> </xsl:variable>
    <xsl:variable name="windowsId"><xsl:value-of select="/flats_for_sale/offer[1]/options/@windows"/></xsl:variable>
    <xsl:variable name="hasIpoteka"><xsl:value-of select="/flats_for_sale/offer[1]/options/@ipoteka"/></xsl:variable>

    <my:admin_area>
        <area id="1">Москва</area>
        <area id="2">Московская область</area>
        <area id="10">Санкт-Петербург</area>
        <area id="11">Ленинградская область</area>
        <area id="3">Владимирская область</area>
        <area id="4">Калужская область</area>
        <area id="5">Рязанская область</area>
        <area id="9">Смоленская область</area>
        <area id="6">Тверская область</area>
        <area id="7">Тульская область</area>
        <area id="8">Ярославская область</area>
        <area id="12">Амурская область</area>
        <area id="13">Архангельская область</area>
        <area id="14">Астраханская область</area>
        <area id="15">Белгородская область</area>
        <area id="16">Брянская область</area>
        <area id="17">Волгоградская область</area>
        <area id="18">Вологодская область</area>
        <area id="19">Воронежская область</area>
        <area id="20">Ивановская область</area>
        <area id="21">Иркутская область</area>
        <area id="22">Калининградская область</area>
        <area id="85">Камчатский край</area>
        <area id="23">Кемеровская область</area>
        <area id="24">Кировская область</area>
        <area id="25">Костромская область</area>
        <area id="26">Курганская область</area>
        <area id="27">Курская область</area>
        <area id="28">Липецкая область</area>
        <area id="29">Магаданская область</area>
        <area id="30">Мурманская область</area>
        <area id="31">Нижегородская область</area>
        <area id="32">Новгородская область</area>
        <area id="33">Новосибирская область</area>
        <area id="34">Омская область</area>
        <area id="35">Оренбургская область</area>
        <area id="36">Орловская область</area>
        <area id="37">Пензенская область</area>
        <area id="84">Пермский край</area>
        <area id="38">Псковская область</area>
        <area id="39">Ростовская область</area>
        <area id="40">Самарская область</area>
        <area id="41">Саратовская область</area>
        <area id="42">Сахалинская область</area>
        <area id="43">Свердловская область</area>
        <area id="44">Тамбовская область</area>
        <area id="45">Томская область</area>
        <area id="46">Тюменская область</area>
        <area id="47">Ульяновская область</area>
        <area id="48">Челябинская область</area>
        <area id="49">Забайкальский край</area>
        <area id="71">Алтайский край</area>
        <area id="53">Республика Алтай</area>
        <area id="50">Республика Адыгея</area>
        <area id="51">Республика Башкортостан</area>
        <area id="52">Республика Бурятия</area>
        <area id="54">Республика Дагестан</area>
        <area id="55">Республика Ингушетия</area>
        <area id="56">Кабардино-Балкарская Республика</area>
        <area id="57">Республика Калмыкия</area>
        <area id="58">Карачаево-Черкесская Республика</area>
        <area id="59">Республика Карелия</area>
        <area id="60">Республика Коми</area>
        <area id="61">Республика Марий эл</area>
        <area id="62">Республика Мордовия</area>
        <area id="63">Республика Саха (Якутия)</area>
        <area id="64">Республика Северная Осетия-Алания</area>
        <area id="65">Республика Татарстан</area>
        <area id="66">Республика Тыва</area>
        <area id="67">Удмуртская Республика</area>
        <area id="68">Республика Хакасия</area>
        <area id="69">Чеченская Республика</area>
        <area id="70">Чувашская Республика</area>
        <area id="72">Краснодарский край</area>
        <area id="73">Красноярский край</area>
        <area id="87">Крым и Севастополь</area>
        <area id="74">Приморский край</area>
        <area id="75">Ставропольский край</area>
        <area id="76">Хабаровский край</area>
        <area id="77">Еврейская автономная область</area>
        <area id="78">НАО</area>
        <area id="80">ХМАО</area>
        <area id="81">Чукотский автономный округ</area>
        <area id="82">ЯНАО</area>
    </my:admin_area>

    <my:metro>
        <location id="85">Авиамоторная</location>
        <location id="13">Автозаводская</location>
        <location id="97">Академическая</location>
        <location id="53">Александровский сад</location>
        <location id="105">Алексеевская</location>
        <location id="213">Алма-Атинская</location>
        <location id="135">Алтуфьево</location>
        <location id="156">Аннино</location>
        <location id="50">Арбатская</location>
        <location id="5">Аэропорт</location>
        <location id="109">Бабушкинская</location>
        <location id="57">Багратионовская</location>
        <location id="71">Баррикадная</location>
        <location id="47">Бауманская</location>
        <location id="69">Беговая</location>
        <location id="7">Белорусская</location>
        <location id="93">Беляево</location>
        <location id="131">Бибирево</location>
        <location id="30">Библиотека им. Ленина</location>
        <location id="222">Битцевский парк</location>
        <location id="207">Борисово</location>
        <location id="120">Боровицкая</location>
        <location id="107">Ботанический сад</location>
        <location id="145">Братиславская</location>
        <location id="193">Бульвар Адмирала Ушакова</location>
        <location id="164">Бульвар Дмитрия Донского</location>
        <location id="40">Бульвар Рокоссовского</location>
        <location id="195">Бунинская аллея</location>
        <location id="16">Варшавская</location>
        <location id="106">ВДНХ</location>
        <location id="112">Владыкино</location>
        <location id="2">Водный стадион</location>
        <location id="3">Войковская</location>
        <location id="77">Волгоградский проспект</location>
        <location id="142">Волжская</location>
        <location id="203">Волоколамская</location>
        <location id="157">Воробьевы горы</location>
        <location id="198">Выставочная</location>
        <location id="218">Выставочный центр</location>
        <location id="81">Выхино</location>
        <location id="217">Деловой центр</location>
        <location id="6">Динамо</location>
        <location id="115">Дмитровская</location>
        <location id="132">Добрынинская</location>
        <location id="21">Домодедовская</location>
        <location id="205">Достоевская</location>
        <location id="140">Дубровка</location>
        <location id="216">Жулебино</location>
        <location id="209">Зябликово</location>
        <location id="43">Измайловская</location>
        <location id="94">Калужская</location>
        <location id="18">Кантемировская</location>
        <location id="17">Каховская</location>
        <location id="15">Каширская</location>
        <location id="52">Киевская</location>
        <location id="74">Китай-город</location>
        <location id="144">Кожуховская</location>
        <location id="14">Коломенская</location>
        <location id="35">Комсомольская</location>
        <location id="92">Коньково</location>
        <location id="226">Котельники</location>
        <location id="22">Красногвардейская</location>
        <location id="133">Краснопресненская</location>
        <location id="36">Красносельская</location>
        <location id="34">Красные ворота</location>
        <location id="139">Крестьянская застава</location>
        <location id="29">Кропоткинская</location>
        <location id="62">Крылатское</location>
        <location id="73">Кузнецкий мост</location>
        <location id="79">Кузьминки</location>
        <location id="60">Кунцевская</location>
        <location id="48">Курская</location>
        <location id="55">Кутузовская</location>
        <location id="98">Ленинский проспект</location>
        <location id="215">Лермонтовский проспект</location>
        <location id="223">Лесопарковая</location>
        <location id="32">Лубянка</location>
        <location id="143">Люблино</location>
        <location id="87">Марксистская</location>
        <location id="204">Марьина роща</location>
        <location id="146">Марьино</location>
        <location id="8">Маяковская</location>
        <location id="110">Медведково</location>
        <location id="197">Международная</location>
        <location id="117">Менделеевская</location>
        <location id="196">Митино</location>
        <location id="61">Молодежная</location>
        <location id="202">Мякинино</location>
        <location id="124">Нагатинская</location>
        <location id="125">Нагорная</location>
        <location id="126">Нахимовский проспект</location>
        <location id="82">Новогиреево</location>
        <location id="210">Новокосино</location>
        <location id="11">Новокузнецкая</location>
        <location id="134">Новослободская</location>
        <location id="89">Новоясеневская</location>
        <location id="95">Новые черемушки</location>
        <location id="100">Октябрьская</location>
        <location id="67">Октябрьское поле</location>
        <location id="20">Орехово</location>
        <location id="111">Отрадное</location>
        <location id="31">Охотный Ряд</location>
        <location id="12">Павелецкая</location>
        <location id="28">Парк Культуры</location>
        <location id="165">Парк Победы</location>
        <location id="44">Партизанская</location>
        <location id="42">Первомайская</location>
        <location id="83">Перово</location>
        <location id="113">Петровско-Разумовская</location>
        <location id="141">Печатники</location>
        <location id="59">Пионерская</location>
        <location id="63">Планерная</location>
        <location id="86">Площадь Ильича</location>
        <location id="49">Площадь революции</location>
        <location id="68">Полежаевская</location>
        <location id="121">Полянка</location>
        <location id="130">Пражская</location>
        <location id="38">Преображенская площадь</location>
        <location id="76">Пролетарская</location>
        <location id="24">Проспект Вернадского</location>
        <location id="136">Проспект Мира</location>
        <location id="96">Профсоюзная</location>
        <location id="72">Пушкинская</location>
        <location id="214">Пятницкое шоссе</location>
        <location id="1">Речной вокзал</location>
        <location id="104">Рижская</location>
        <location id="138">Римская</location>
        <location id="228">Румянцево</location>
        <location id="80">Рязанский проспект</location>
        <location id="116">Савеловская</location>
        <location id="229">Саларьево</location>
        <location id="108">Свиблово</location>
        <location id="127">Севастопольская</location>
        <location id="45">Семеновская</location>
        <location id="122">Серпуховская</location>
        <location id="201">Славянский бульвар</location>
        <location id="51">Смоленская</location>
        <location id="4">Сокол</location>
        <location id="37">Сокольники</location>
        <location id="224">Спартак</location>
        <location id="26">Спортивная</location>
        <location id="206">Сретенский бульвар</location>
        <location id="200">Строгино</location>
        <location id="54">Студенческая</location>
        <location id="102">Сухаревская</location>
        <location id="64">Сходненская</location>
        <location id="75">Таганская</location>
        <location id="9">Тверская</location>
        <location id="10">Театральная</location>
        <location id="78">Текстильщики</location>
        <location id="220">Телецентр</location>
        <location id="91">Теплый стан</location>
        <location id="227">Технопарк</location>
        <location id="114">Тимирязевская</location>
        <location id="88">Третьяковская</location>
        <location id="225">Тропарево</location>
        <location id="199">Трубная</location>
        <location id="123">Тульская</location>
        <location id="103">Тургеневская</location>
        <location id="65">Тушинская</location>
        <location id="70">Улица 1905 года</location>
        <location id="219">Улица Академика Королева</location>
        <location id="155">Улица Академика Янгеля</location>
        <location id="194">Улица Горчакова</location>
        <location id="221">Улица Милашенкова</location>
        <location id="211">Улица Сергея Эйзенштейна</location>
        <location id="192">Улица Скобелевская</location>
        <location id="212">Улица Старокачаловская</location>
        <location id="25">Университет</location>
        <location id="58">Филевский парк</location>
        <location id="56">Фили</location>
        <location id="27">Фрунзенская</location>
        <location id="19">Царицыно</location>
        <location id="118">Цветной бульвар</location>
        <location id="39">Черкизовская</location>
        <location id="128">Чертановская</location>
        <location id="119">Чеховская</location>
        <location id="33">Чистые пруды</location>
        <location id="137">Чкаловская</location>
        <location id="99">Шаболовская</location>
        <location id="208">Шипиловская</location>
        <location id="84">Шоссе энтузиастов</location>
        <location id="41">Щелковская</location>
        <location id="66">Щукинская</location>
        <location id="46">Электрозаводская</location>
        <location id="23">Юго-Западная</location>
        <location id="129">Южная</location>
        <location id="90">Ясенево</location>
    </my:metro>
    <my:map>
        <entry key = "1">Одна комната</entry>
        <entry key = "2">Две комнаты </entry>
        <entry key = "3">Три комнаты</entry>
        <entry key = "4">Четыре комнаты </entry>
        <entry key = "5">Пять комнат</entry>
        <entry key = "ROOMS_MANY">Многокомнатная</entry>
        <entry key = "ROOM_ONLY">Сдается комната </entry>
    </my:map>

    <my:buildingType>
        <entry key="1">Панельный</entry>
        <entry key="2">Кирпичный</entry>
        <entry key="3">Монолитный</entry>
        <entry key="4">Кирпично-монолитный</entry>
        <entry key="5">Блочный</entry>
        <entry key="6">Деревянный</entry>
        <entry key="7">"Сталинский"</entry>
        <entry key="9">Старый фонд</entry>
    </my:buildingType>

    <my:boolType>
        <entry key="yes">Да</entry>
        <entry key="no">Нет</entry>
    </my:boolType>



    <my:oneOrNullType>
        <entry key="1">Да</entry>
        <entry key="0">Нет</entry>
    </my:oneOrNullType>

    <my:saleTypeMap>
        <saleType key="F">Свободная продажа</saleType>
        <saleType key="A">Альтернатива</saleType>
        <saleType key="ddu">Договор долевого участия</saleType>
        <saleType key="zhsk">Договор ЖСК</saleType>
        <saleType key="pereustupka">Договор уступки прав требования</saleType>
        <saleType key="pdkp">Предварительный договор купли-продажи</saleType>
        <saleType key="invest"> Договор инвестирования</saleType>
        <saleType key="free">Свободная <продажа></продажа></saleType>
        <saleType key="alt">Альтернатива</saleType>
    </my:saleTypeMap>

    <my:objectTypeMap>
        <liveType key ="2">Новостройка</liveType>
        <liveType key ="1">Вторичное жилье</liveType>
    </my:objectTypeMap>

    <my:windowsMap>
        <entry key = "1" >Двор</entry>
        <entry key = "2" >Улицу</entry>
        <entry key = "3" >Двор и улицу</entry>
    </my:windowsMap>
</xsl:stylesheet>


