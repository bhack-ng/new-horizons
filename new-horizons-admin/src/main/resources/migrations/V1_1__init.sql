--crate tables

CREATE TABLE IF NOT EXISTS  abstractarea
(
  id bigint NOT NULL,
  rooms character varying(255),
  roomscount integer,
  total double precision
);

CREATE TABLE IF NOT EXISTS abstractbuilding (
    id bigint NOT NULL,
    floor integer,
    floortotal integer
);



CREATE TABLE IF NOT EXISTS abstractoptions (
    id bigint NOT NULL
);



CREATE TABLE IF NOT EXISTS address (
    id bigint NOT NULL,
    admin_area bigint NOT NULL,
    district character varying(255),
    house_str character varying(255),
    kvartira character varying(255),
    locality character varying(255),
    fiasaddressvector_id bigint,
    geocoordinates_id bigint,
    street_id bigint
);


CREATE TABLE IF NOT EXISTS agency (
    id bigint NOT NULL,
    defaultupdateinterval integer NOT NULL,
    name character varying(255),
    phone character varying(255),
    defaultaddress_id bigint,
    watermark_id bigint,
    CONSTRAINT agency_defaultupdateinterval_check CHECK ((defaultupdateinterval >= 1))
);



CREATE TABLE IF NOT EXISTS  agent (
    login character varying(255) NOT NULL,
    blocked boolean NOT NULL,
    email character varying(255),
    externalid bigint NOT NULL,
    fio character varying(255),
    password character varying(255) NOT NULL,
    phone character varying(255),
    photo oid,
    photocontenttype character varying(255),
    role character varying(255),
    version integer NOT NULL,
    agency_id bigint,
    filter_id bigint
);

CREATE TABLE IF NOT EXISTS cianadminarea (
    id bigint NOT NULL,
    cianid integer NOT NULL,
    fiasaoguid character varying(255),
    name character varying(255)
);


CREATE TABLE IF NOT EXISTS comment (
    id bigint NOT NULL,
    created timestamp without time zone,
    text character varying(1024) NOT NULL,
    author_login character varying(255),
    realtyobject_id bigint
);


CREATE TABLE IF NOT EXISTS commerce (
    contracttype character varying(255),
    special_offer_id character varying(255),
    type character varying(255),
    id bigint NOT NULL
);



CREATE TABLE IF NOT EXISTS commercearea (
    min integer NOT NULL,
    id bigint NOT NULL
);

CREATE TABLE IF NOT EXISTS commercebuilding (
    area_b integer,
    buildingclass integer,
    buildingtype character varying(255),
    ceiling character varying(255),
    enter integer,
    livetype integer,
    name_bc character varying(255),
    status_b integer,
    id bigint NOT NULL
);

CREATE TABLE IF NOT EXISTS commerceoptions (
    add_phones boolean,
    canal boolean,
    elect boolean,
    gas boolean,
    heat boolean,
    internet boolean,
    lift boolean,
    mebel integer,
    parking boolean,
    phones integer,
    security boolean,
    status integer,
    water boolean,
    id bigint NOT NULL
);


CREATE TABLE IF NOT EXISTS commerceprice (
    period character varying(255),
    id bigint NOT NULL
);

CREATE TABLE IF NOT EXISTS commission (
    id bigint NOT NULL,
    agencycommission double precision NOT NULL,
    buyercommission double precision NOT NULL,
    salescommission double precision NOT NULL
);


CREATE TABLE IF NOT EXISTS competitors (
    id bigint NOT NULL,
    bl_f character varying(255),
    ch_f character varying(255),
    int_f character varying(255),
    r_f character varying(255)
);



CREATE TABLE IF NOT EXISTS contactsofowner (
    id bigint NOT NULL,
    name character varying(255),
    phone character varying(255),
    realtyobject_id bigint
);


CREATE TABLE IF NOT EXISTS dbfile (
    id bigint NOT NULL,
    contenttype character varying(255),
    length bigint NOT NULL,
    content_id bigint
);


CREATE TABLE IF NOT EXISTS dbfilecontent (
    id bigint NOT NULL,
    data oid
);



CREATE TABLE IF NOT EXISTS dwellinghouse (
    buildingname character varying(255),
    buildingtype integer,
    seria character varying(255),
    id bigint NOT NULL
);



CREATE TABLE IF NOT EXISTS externalagency (
    id bigint NOT NULL,
    enable boolean NOT NULL,
    name character varying(255),
    site character varying(255),
    updateperiod integer NOT NULL,
    agency_id bigint
);

CREATE TABLE IF NOT EXISTS externalobectext (
    id bigint NOT NULL,
    externalid character varying(255),
    updated timestamp without time zone,
    feed_id bigint
);


CREATE TABLE IF NOT EXISTS externalobectext_externalphoto (
    externalobectext_id bigint NOT NULL,
    externalphoto character varying(255)
);



CREATE TABLE IF NOT EXISTS feed (
    id bigint NOT NULL,
    allfeedsizelimit bigint NOT NULL,
    description character varying(255),
    enable boolean NOT NULL,
    feedtype character varying(255),
    lasterror character varying(255),
    lastupdate timestamp without time zone,
    offercountlimit bigint NOT NULL,
    offersizelimit bigint NOT NULL,
    updateinterval integer NOT NULL,
    url character varying(255),
    externalagency_id bigint
);


CREATE TABLE IF NOT EXISTS fiasaddressvector (
    id bigint NOT NULL,
    level1_aoid character varying(255),
    level2_aoid character varying(255),
    level3_aoid character varying(255),
    level4_aoid character varying(255),
    level5_aoid character varying(255),
    level6_aoid character varying(255),
    level7_aoid character varying(255)
);


CREATE TABLE IF NOT EXISTS fiasobject (
    aoid character varying(255) NOT NULL,
    actstatus integer NOT NULL,
    aoguid character varying(255),
    aolevel integer NOT NULL,
    areacode character varying(255),
    autocode character varying(255),
    cadnum character varying(255),
    centstatus integer NOT NULL,
    citycode character varying(255),
    code character varying(255),
    ctarcode character varying(255),
    currstatus integer NOT NULL,
    divtype character varying(255),
    enddate date,
    extrcode character varying(255),
    formalname character varying(255),
    ifnsfl character varying(255),
    ifnsul character varying(255),
    livestatus integer NOT NULL,
    nextid character varying(255),
    normdoc character varying(255),
    offname character varying(255),
    okato character varying(255),
    oktmo character varying(255),
    operstatus integer NOT NULL,
    parentguid character varying(255),
    placecode character varying(255),
    plaincode character varying(255),
    plancode character varying(255),
    postalcode character varying(255),
    previd character varying(255),
    regioncode character varying(2),
    sextcode character varying(255),
    shortname character varying(255),
    startdate date,
    streetcode character varying(255),
    terrifnsfl character varying(255),
    terrifnsul character varying(255),
    updatedate date
);

CREATE TABLE IF NOT EXISTS fiassocr (
    id character varying(255) NOT NULL,
    fullname character varying(255),
    level integer NOT NULL,
    socrname character varying(255)
);



CREATE TABLE IF NOT EXISTS geocoordinates (
    id bigint NOT NULL,
    latitude double precision,
    longitude double precision
);


CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE IF NOT EXISTS livearea (
    balconycount integer,
    bedroomcount integer,
    cloakroomcount integer,
    fullbathroomcount integer,
    kitchen double precision,
    liftcount integer,
    living double precision,
    recessedbalconycount integer,
    splitbathroomcount integer,
    wetunitcount integer,
    id bigint NOT NULL
);

CREATE TABLE liveleaserealty (
    ownerstatus character varying(255),
    id bigint NOT NULL,
    competitors_id bigint,
    nameofbuilding_id bigint
);

CREATE TABLE IF NOT EXISTS liveoptions (
    balcony boolean,
    children boolean,
    cloakroom boolean,
    concierge boolean,
    fencedterritory boolean,
    mebel boolean,
    mebelinkitchen boolean,
    parking boolean,
    pets boolean,
    phone boolean,
    refrigerator boolean,
    security boolean,
    smoke boolean,
    tv boolean,
    washmashine boolean,
    id bigint NOT NULL
);


CREATE TABLE liverentprice (
    deposit boolean,
    payform character varying(255),
    prepay integer,
    id bigint NOT NULL
);



CREATE TABLE IF NOT EXISTS livesaleoptions (
    homemortgage boolean,
    phone boolean,
    windowsposition integer,
    id bigint NOT NULL
);


CREATE TABLE IF NOT EXISTS livesalerealty (
    buildingtypesale character varying(255),
    saletype character varying(255),
    id bigint NOT NULL
);


CREATE TABLE IF NOT EXISTS logmessage (
    id bigint NOT NULL,
    creationdate timestamp without time zone,
    message character varying(255),
    priority character varying(255)
);



CREATE TABLE IF NOT EXISTS metrolocation (
    id bigint NOT NULL,
    anytime integer,
    wtimeselected boolean NOT NULL,
    metrostation_id bigint
);


CREATE TABLE IF NOT EXISTS metrostation (
    id bigint NOT NULL,
    stationname character varying(255)
);


CREATE TABLE IF NOT EXISTS nameofbuilding (
    id bigint NOT NULL,
    buildingname character varying(255)
);


CREATE TABLE IF NOT EXISTS objectstatus (
    id bigint NOT NULL,
    color character varying(255),
    name character varying(255)
);


CREATE TABLE IF NOT EXISTS photo (
    id bigint NOT NULL,
    advertise boolean NOT NULL,
    contenttype character varying(255),
    facad boolean NOT NULL,
    main boolean NOT NULL,
    name character varying(255),
    path character varying(255),
    plan boolean NOT NULL,
    data_id bigint,
    preview_id bigint,
    realtyobject_id bigint,
    stampedcontent_id bigint
);


CREATE TABLE IF NOT EXISTS photodata (
    id bigint NOT NULL,
    data oid
);

CREATE TABLE IF NOT EXISTS realtyfilter (
    id bigint NOT NULL,
    buildingnumber character varying(255),
    buildingtype character varying(255),
    calltoday boolean DEFAULT false,
    currency character varying(255),
    external boolean NOT NULL,
    floor integer,
    period character varying(255),
    phone character varying(255),
    pricemax integer,
    pricemin integer,
    realtyid bigint,
    realtyobjecttype character varying(255) NOT NULL,
    roomcount integer,
    sortfield character varying(255),
    agenttofilter_login character varying(255),
    externalagency_id bigint,
    fiasaddressvector_id bigint,
    metrostation_id bigint,
    objectstatus_id bigint,
    street_id bigint
);


CREATE TABLE IF NOT EXISTS realtyobject (
    id bigint NOT NULL,
    changed timestamp without time zone,
    linktocian character varying(255),
    nextcall date,
    note character varying(4096),
    olddbidentifier bigint,
    premiumincian boolean NOT NULL,
    publishcian boolean NOT NULL,
    publishexternal boolean NOT NULL,
    publishsite boolean NOT NULL,
    realtyobjecttype character varying(255) NOT NULL,
    address_id bigint,
    agent_login character varying(255),
    area_id bigint,
    building_id bigint,
    commission_id bigint,
    externalobectext_id bigint,
    metrolocation_id bigint,
    objectstatus_id bigint,
    options_id bigint,
    price_id bigint
);

CREATE TABLE IF NOT EXISTS recommendation (
    id bigint NOT NULL,
    email character varying(255),
    fio character varying(255),
    phone character varying(255),
    reward character varying(255),
    realtyobject_id bigint
);

CREATE TABLE IF NOT EXISTS rentprice (
    id bigint NOT NULL
);



CREATE TABLE IF NOT EXISTS simpleprice (
    id bigint NOT NULL,
    currency character varying(255),
    value integer
);

CREATE TABLE IF NOT EXISTS street (
    id bigint NOT NULL,
    streetname character varying(255)
);

ALTER TABLE ONLY abstractarea
    ADD CONSTRAINT abstractarea_pkey PRIMARY KEY (id);


--
-- TOC entry 2101 (class 2606 OID 250359)
-- Name: abstractbuilding_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY abstractbuilding
    ADD CONSTRAINT abstractbuilding_pkey PRIMARY KEY (id);


--
-- TOC entry 2103 (class 2606 OID 250364)
-- Name: abstractoptions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY abstractoptions
    ADD CONSTRAINT abstractoptions_pkey PRIMARY KEY (id);


--
-- TOC entry 2105 (class 2606 OID 250372)
-- Name: address_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address
    ADD CONSTRAINT address_pkey PRIMARY KEY (id);


--
-- TOC entry 2107 (class 2606 OID 250381)
-- Name: agency_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY agency
    ADD CONSTRAINT agency_pkey PRIMARY KEY (id);


--
-- TOC entry 2109 (class 2606 OID 250389)
-- Name: agent_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY agent
    ADD CONSTRAINT agent_pkey PRIMARY KEY (login);


--
-- TOC entry 2111 (class 2606 OID 250397)
-- Name: cianadminarea_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cianadminarea
    ADD CONSTRAINT cianadminarea_pkey PRIMARY KEY (id);


--
-- TOC entry 2113 (class 2606 OID 250405)
-- Name: comment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comment
    ADD CONSTRAINT comment_pkey PRIMARY KEY (id);


--
-- TOC entry 2115 (class 2606 OID 250413)
-- Name: commerce_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY commerce
    ADD CONSTRAINT commerce_pkey PRIMARY KEY (id);


--
-- TOC entry 2117 (class 2606 OID 250418)
-- Name: commercearea_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY commercearea
    ADD CONSTRAINT commercearea_pkey PRIMARY KEY (id);


--
-- TOC entry 2119 (class 2606 OID 250426)
-- Name: commercebuilding_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY commercebuilding
    ADD CONSTRAINT commercebuilding_pkey PRIMARY KEY (id);


--
-- TOC entry 2121 (class 2606 OID 250431)
-- Name: commerceoptions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY commerceoptions
    ADD CONSTRAINT commerceoptions_pkey PRIMARY KEY (id);


--
-- TOC entry 2123 (class 2606 OID 250436)
-- Name: commerceprice_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY commerceprice
    ADD CONSTRAINT commerceprice_pkey PRIMARY KEY (id);


--
-- TOC entry 2125 (class 2606 OID 250441)
-- Name: commission_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY commission
    ADD CONSTRAINT commission_pkey PRIMARY KEY (id);


--
-- TOC entry 2127 (class 2606 OID 250449)
-- Name: competitors_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY competitors
    ADD CONSTRAINT competitors_pkey PRIMARY KEY (id);


--
-- TOC entry 2129 (class 2606 OID 250457)
-- Name: contactsofowner_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY contactsofowner
    ADD CONSTRAINT contactsofowner_pkey PRIMARY KEY (id);


--
-- TOC entry 2131 (class 2606 OID 250462)
-- Name: dbfile_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dbfile
    ADD CONSTRAINT dbfile_pkey PRIMARY KEY (id);


--
-- TOC entry 2133 (class 2606 OID 250467)
-- Name: dbfilecontent_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dbfilecontent
    ADD CONSTRAINT dbfilecontent_pkey PRIMARY KEY (id);


--
-- TOC entry 2135 (class 2606 OID 250475)
-- Name: dwellinghouse_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dwellinghouse
    ADD CONSTRAINT dwellinghouse_pkey PRIMARY KEY (id);


--
-- TOC entry 2137 (class 2606 OID 250483)
-- Name: externalagency_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY externalagency
    ADD CONSTRAINT externalagency_pkey PRIMARY KEY (id);


--
-- TOC entry 2139 (class 2606 OID 250488)
-- Name: externalobectext_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY externalobectext
    ADD CONSTRAINT externalobectext_pkey PRIMARY KEY (id);


--
-- TOC entry 2141 (class 2606 OID 250499)
-- Name: feed_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY feed
    ADD CONSTRAINT feed_pkey PRIMARY KEY (id);


--
-- TOC entry 2143 (class 2606 OID 250507)
-- Name: fiasaddressvector_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY fiasaddressvector
    ADD CONSTRAINT fiasaddressvector_pkey PRIMARY KEY (id);


--
-- TOC entry 2147 (class 2606 OID 250515)
-- Name: fiasobject_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY fiasobject
    ADD CONSTRAINT fiasobject_pkey PRIMARY KEY (aoid);


--
-- TOC entry 2150 (class 2606 OID 250523)
-- Name: fiassocr_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY fiassocr
    ADD CONSTRAINT fiassocr_pkey PRIMARY KEY (id);


--
-- TOC entry 2152 (class 2606 OID 250528)
-- Name: geocoordinates_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY geocoordinates
    ADD CONSTRAINT geocoordinates_pkey PRIMARY KEY (id);


--
-- TOC entry 2154 (class 2606 OID 250533)
-- Name: livearea_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY livearea
    ADD CONSTRAINT livearea_pkey PRIMARY KEY (id);


--
-- TOC entry 2156 (class 2606 OID 250538)
-- Name: liveleaserealty_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY liveleaserealty
    ADD CONSTRAINT liveleaserealty_pkey PRIMARY KEY (id);


--
-- TOC entry 2158 (class 2606 OID 250543)
-- Name: liveoptions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY liveoptions
    ADD CONSTRAINT liveoptions_pkey PRIMARY KEY (id);


--
-- TOC entry 2160 (class 2606 OID 250548)
-- Name: liverentprice_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY liverentprice
    ADD CONSTRAINT liverentprice_pkey PRIMARY KEY (id);


--
-- TOC entry 2162 (class 2606 OID 250553)
-- Name: livesaleoptions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY livesaleoptions
    ADD CONSTRAINT livesaleoptions_pkey PRIMARY KEY (id);


--
-- TOC entry 2164 (class 2606 OID 250561)
-- Name: livesalerealty_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY livesalerealty
    ADD CONSTRAINT livesalerealty_pkey PRIMARY KEY (id);


--
-- TOC entry 2166 (class 2606 OID 250569)
-- Name: logmessage_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY logmessage
    ADD CONSTRAINT logmessage_pkey PRIMARY KEY (id);


--
-- TOC entry 2168 (class 2606 OID 250574)
-- Name: metrolocation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY metrolocation
    ADD CONSTRAINT metrolocation_pkey PRIMARY KEY (id);


--
-- TOC entry 2170 (class 2606 OID 250579)
-- Name: metrostation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY metrostation
    ADD CONSTRAINT metrostation_pkey PRIMARY KEY (id);


--
-- TOC entry 2172 (class 2606 OID 250584)
-- Name: nameofbuilding_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY nameofbuilding
    ADD CONSTRAINT nameofbuilding_pkey PRIMARY KEY (id);


--
-- TOC entry 2174 (class 2606 OID 250592)
-- Name: objectstatus_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY objectstatus
    ADD CONSTRAINT objectstatus_pkey PRIMARY KEY (id);


--
-- TOC entry 2176 (class 2606 OID 250600)
-- Name: photo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY photo
    ADD CONSTRAINT photo_pkey PRIMARY KEY (id);


--
-- TOC entry 2178 (class 2606 OID 250605)
-- Name: photodata_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY photodata
    ADD CONSTRAINT photodata_pkey PRIMARY KEY (id);


--
-- TOC entry 2180 (class 2606 OID 250614)
-- Name: realtyfilter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyfilter
    ADD CONSTRAINT realtyfilter_pkey PRIMARY KEY (id);


--
-- TOC entry 2182 (class 2606 OID 250622)
-- Name: realtyobject_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyobject
    ADD CONSTRAINT realtyobject_pkey PRIMARY KEY (id);


--
-- TOC entry 2184 (class 2606 OID 250630)
-- Name: recommendation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY recommendation
    ADD CONSTRAINT recommendation_pkey PRIMARY KEY (id);


--
-- TOC entry 2186 (class 2606 OID 250635)
-- Name: rentprice_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rentprice
    ADD CONSTRAINT rentprice_pkey PRIMARY KEY (id);


--
-- TOC entry 2188 (class 2606 OID 250640)
-- Name: simpleprice_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY simpleprice
    ADD CONSTRAINT simpleprice_pkey PRIMARY KEY (id);


--
-- TOC entry 2190 (class 2606 OID 250645)
-- Name: street_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY street
    ADD CONSTRAINT street_pkey PRIMARY KEY (id);


--
-- TOC entry 2144 (class 1259 OID 250786)
-- Name: aoguid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX aoguid_idx ON fiasobject USING btree (aoguid);


--
-- TOC entry 2145 (class 1259 OID 250787)
-- Name: aolevel_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX aolevel_idx ON fiasobject USING btree (aolevel);


--
-- TOC entry 2148 (class 1259 OID 250788)
-- Name: parentguid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX parentguid_idx ON fiasobject USING btree (parentguid);

--
-- TOC entry 2224 (class 2606 OID 250814)
-- Name: fk2b43oatselb5b24w14cgjvcl6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY liverentprice
    ADD CONSTRAINT fk2b43oatselb5b24w14cgjvcl6 FOREIGN KEY (id) REFERENCES rentprice(id);


--
-- TOC entry 2202 (class 2606 OID 250701)
-- Name: fk31814q2teottdrevpsn5vmow7; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY commercebuilding
    ADD CONSTRAINT fk31814q2teottdrevpsn5vmow7 FOREIGN KEY (id) REFERENCES abstractbuilding(id);


--
-- TOC entry 2234 (class 2606 OID 250864)
-- Name: fk39yyvv3i6anruw06l3ci8po6u; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyfilter
    ADD CONSTRAINT fk39yyvv3i6anruw06l3ci8po6u FOREIGN KEY (fiasaddressvector_id) REFERENCES fiasaddressvector(id);


--
-- TOC entry 2249 (class 2606 OID 250939)
-- Name: fk3fk2mlof4lpkmyjd5v087damm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY rentprice
    ADD CONSTRAINT fk3fk2mlof4lpkmyjd5v087damm FOREIGN KEY (id) REFERENCES simpleprice(id);


--
-- TOC entry 2214 (class 2606 OID 250761)
-- Name: fk3kuaaxg98wcxm4blcga5sbtag; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY fiasaddressvector
    ADD CONSTRAINT fk3kuaaxg98wcxm4blcga5sbtag FOREIGN KEY (level3_aoid) REFERENCES fiasobject(aoid);


--
-- TOC entry 2211 (class 2606 OID 250746)
-- Name: fk4m5m4oifb2i4enplumwesvu5p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY feed
    ADD CONSTRAINT fk4m5m4oifb2i4enplumwesvu5p FOREIGN KEY (externalagency_id) REFERENCES externalagency(id);


--
-- TOC entry 2217 (class 2606 OID 250776)
-- Name: fk5191v4v4b48figxm8eru6lk8h; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY fiasaddressvector
    ADD CONSTRAINT fk5191v4v4b48figxm8eru6lk8h FOREIGN KEY (level6_aoid) REFERENCES fiasobject(aoid);


--
-- TOC entry 2203 (class 2606 OID 250706)
-- Name: fk5a3ty671s4grbnqydp1gvxc50; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY commerceoptions
    ADD CONSTRAINT fk5a3ty671s4grbnqydp1gvxc50 FOREIGN KEY (id) REFERENCES abstractoptions(id);


--
-- TOC entry 2200 (class 2606 OID 250691)
-- Name: fk5i127qxh1p55057pg0pchpkpy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY commerce
    ADD CONSTRAINT fk5i127qxh1p55057pg0pchpkpy FOREIGN KEY (id) REFERENCES realtyobject(id);


--
-- TOC entry 2225 (class 2606 OID 250819)
-- Name: fk6fu7yv90v6ock4w31ley55sxe; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY livesaleoptions
    ADD CONSTRAINT fk6fu7yv90v6ock4w31ley55sxe FOREIGN KEY (id) REFERENCES abstractoptions(id);


--
-- TOC entry 2241 (class 2606 OID 250899)
-- Name: fk84teng8o70sf5l4ugbjvqfvfy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyobject
    ADD CONSTRAINT fk84teng8o70sf5l4ugbjvqfvfy FOREIGN KEY (building_id) REFERENCES abstractbuilding(id);


--
-- TOC entry 2208 (class 2606 OID 250731)
-- Name: fk93opeiqcjopy8ktejpdkqa5bl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY externalagency
    ADD CONSTRAINT fk93opeiqcjopy8ktejpdkqa5bl FOREIGN KEY (agency_id) REFERENCES agency(id);


--
-- TOC entry 2235 (class 2606 OID 250869)
-- Name: fk9aqwbfo5ur2hjjmfmokjtt3cj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyfilter
    ADD CONSTRAINT fk9aqwbfo5ur2hjjmfmokjtt3cj FOREIGN KEY (metrostation_id) REFERENCES metrostation(id);


--
-- TOC entry 2238 (class 2606 OID 250884)
-- Name: fk9ewwh91mma1qwvbucg6koguh1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyobject
    ADD CONSTRAINT fk9ewwh91mma1qwvbucg6koguh1 FOREIGN KEY (address_id) REFERENCES address(id);


--
-- TOC entry 2216 (class 2606 OID 250771)
-- Name: fk9l94c3kg58oxm7kkjt6vgp9ey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY fiasaddressvector
    ADD CONSTRAINT fk9l94c3kg58oxm7kkjt6vgp9ey FOREIGN KEY (level5_aoid) REFERENCES fiasobject(aoid);


--
-- TOC entry 2230 (class 2606 OID 250844)
-- Name: fk9o16ka31s7itk9f7wm09dooth; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY photo
    ADD CONSTRAINT fk9o16ka31s7itk9f7wm09dooth FOREIGN KEY (realtyobject_id) REFERENCES realtyobject(id);


--
-- TOC entry 2223 (class 2606 OID 250809)
-- Name: fk9qu8x4jp7uplwl5r5yx53auhf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY liveoptions
    ADD CONSTRAINT fk9qu8x4jp7uplwl5r5yx53auhf FOREIGN KEY (id) REFERENCES abstractoptions(id);


--
-- TOC entry 2206 (class 2606 OID 250721)
-- Name: fkb8vuxvx1wj1puvkjsiqlss8ub; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dbfile
    ADD CONSTRAINT fkb8vuxvx1wj1puvkjsiqlss8ub FOREIGN KEY (content_id) REFERENCES dbfilecontent(id);


--
-- TOC entry 2199 (class 2606 OID 250686)
-- Name: fkb9pbrmn9risq7ggps49ccds3r; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comment
    ADD CONSTRAINT fkb9pbrmn9risq7ggps49ccds3r FOREIGN KEY (realtyobject_id) REFERENCES realtyobject(id);


--
-- TOC entry 2196 (class 2606 OID 250671)
-- Name: fkc1smcbvskj0c07m2ig284fqln; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY agent
    ADD CONSTRAINT fkc1smcbvskj0c07m2ig284fqln FOREIGN KEY (agency_id) REFERENCES agency(id);


--
-- TOC entry 2237 (class 2606 OID 250879)
-- Name: fkc4gx9jn2ouic1fojv83kgnudj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyfilter
    ADD CONSTRAINT fkc4gx9jn2ouic1fojv83kgnudj FOREIGN KEY (street_id) REFERENCES street(id);


--
-- TOC entry 2193 (class 2606 OID 250656)
-- Name: fke7k5sl2b8uk1op43p9wqsk37l; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address
    ADD CONSTRAINT fke7k5sl2b8uk1op43p9wqsk37l FOREIGN KEY (street_id) REFERENCES street(id);


--
-- TOC entry 2192 (class 2606 OID 250651)
-- Name: fkexsslw6onncekh5pid2mlr2b0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address
    ADD CONSTRAINT fkexsslw6onncekh5pid2mlr2b0 FOREIGN KEY (geocoordinates_id) REFERENCES geocoordinates(id);


--
-- TOC entry 2197 (class 2606 OID 250676)
-- Name: fkf1rqecs754xj5ctec7lc5fuxj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY agent
    ADD CONSTRAINT fkf1rqecs754xj5ctec7lc5fuxj FOREIGN KEY (filter_id) REFERENCES realtyfilter(id);


--
-- TOC entry 2222 (class 2606 OID 250804)
-- Name: fkg8i6u017rrojdau0nyrmj72n6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY liveleaserealty
    ADD CONSTRAINT fkg8i6u017rrojdau0nyrmj72n6 FOREIGN KEY (id) REFERENCES realtyobject(id);


--
-- TOC entry 2212 (class 2606 OID 250751)
-- Name: fkge53rfy06gvnp1f9vvc0qh15t; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY fiasaddressvector
    ADD CONSTRAINT fkge53rfy06gvnp1f9vvc0qh15t FOREIGN KEY (level1_aoid) REFERENCES fiasobject(aoid);


--
-- TOC entry 2205 (class 2606 OID 250716)
-- Name: fkhged17rdhb9u2171lmxeg6tnp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY contactsofowner
    ADD CONSTRAINT fkhged17rdhb9u2171lmxeg6tnp FOREIGN KEY (realtyobject_id) REFERENCES realtyobject(id);


--
-- TOC entry 2204 (class 2606 OID 250711)
-- Name: fkhkco9mi73uwphhxytq6jwbvlk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY commerceprice
    ADD CONSTRAINT fkhkco9mi73uwphhxytq6jwbvlk FOREIGN KEY (id) REFERENCES simpleprice(id);


--
-- TOC entry 2246 (class 2606 OID 250924)
-- Name: fkhuau76gojobonlflwljd58c1w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyobject
    ADD CONSTRAINT fkhuau76gojobonlflwljd58c1w FOREIGN KEY (options_id) REFERENCES abstractoptions(id);


--
-- TOC entry 2226 (class 2606 OID 250824)
-- Name: fki57c2ut9etuw71y6nxry1wryn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY livesalerealty
    ADD CONSTRAINT fki57c2ut9etuw71y6nxry1wryn FOREIGN KEY (id) REFERENCES realtyobject(id);


--
-- TOC entry 2194 (class 2606 OID 250661)
-- Name: fkiibi7khahwl2aey6c7bnshcdl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY agency
    ADD CONSTRAINT fkiibi7khahwl2aey6c7bnshcdl FOREIGN KEY (defaultaddress_id) REFERENCES fiasaddressvector(id);


--
-- TOC entry 2236 (class 2606 OID 250874)
-- Name: fkirok4kpjiev5jqfhq9dwejxk1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyfilter
    ADD CONSTRAINT fkirok4kpjiev5jqfhq9dwejxk1 FOREIGN KEY (objectstatus_id) REFERENCES objectstatus(id);


--
-- TOC entry 2229 (class 2606 OID 250839)
-- Name: fkivir2jql8mnggbmrb9rxh9psk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY photo
    ADD CONSTRAINT fkivir2jql8mnggbmrb9rxh9psk FOREIGN KEY (preview_id) REFERENCES photodata(id);


--
-- TOC entry 2209 (class 2606 OID 250736)
-- Name: fkk6dg8o5f5un5lausl4yxnl4ky; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY externalobectext
    ADD CONSTRAINT fkk6dg8o5f5un5lausl4yxnl4ky FOREIGN KEY (feed_id) REFERENCES feed(id);


--
-- TOC entry 2232 (class 2606 OID 250854)
-- Name: fkk9olu8pvbxhc49mas5l8lbhi1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyfilter
    ADD CONSTRAINT fkk9olu8pvbxhc49mas5l8lbhi1 FOREIGN KEY (agenttofilter_login) REFERENCES agent(login);


--
-- TOC entry 2244 (class 2606 OID 250914)
-- Name: fkku38qr925u79g4oisgorexlay; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyobject
    ADD CONSTRAINT fkku38qr925u79g4oisgorexlay FOREIGN KEY (metrolocation_id) REFERENCES metrolocation(id);


--
-- TOC entry 2231 (class 2606 OID 250849)
-- Name: fkl41plldd210anjw6pksvec0xr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY photo
    ADD CONSTRAINT fkl41plldd210anjw6pksvec0xr FOREIGN KEY (stampedcontent_id) REFERENCES photodata(id);


--
-- TOC entry 2242 (class 2606 OID 250904)
-- Name: fkm0weqgrmvk19gu6jicghm4mic; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyobject
    ADD CONSTRAINT fkm0weqgrmvk19gu6jicghm4mic FOREIGN KEY (commission_id) REFERENCES commission(id);


--
-- TOC entry 2220 (class 2606 OID 250794)
-- Name: fkmd1h3neg7jd6a19fh4hyw2uv4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY liveleaserealty
    ADD CONSTRAINT fkmd1h3neg7jd6a19fh4hyw2uv4 FOREIGN KEY (competitors_id) REFERENCES competitors(id);


--
-- TOC entry 2227 (class 2606 OID 250829)
-- Name: fkmx5vvlbdcdb6s63wka4y8nebf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY metrolocation
    ADD CONSTRAINT fkmx5vvlbdcdb6s63wka4y8nebf FOREIGN KEY (metrostation_id) REFERENCES metrostation(id);


--
-- TOC entry 2213 (class 2606 OID 250756)
-- Name: fkn8esi6mxwmr3koldajbp0rjfw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY fiasaddressvector
    ADD CONSTRAINT fkn8esi6mxwmr3koldajbp0rjfw FOREIGN KEY (level2_aoid) REFERENCES fiasobject(aoid);


--
-- TOC entry 2239 (class 2606 OID 250889)
-- Name: fknarnxjrfhebhem1wfe1v3gcut; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyobject
    ADD CONSTRAINT fknarnxjrfhebhem1wfe1v3gcut FOREIGN KEY (agent_login) REFERENCES agent(login);


--
-- TOC entry 2228 (class 2606 OID 250834)
-- Name: fkno6i85l0u6i0t7o12elalx10m; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY photo
    ADD CONSTRAINT fkno6i85l0u6i0t7o12elalx10m FOREIGN KEY (data_id) REFERENCES photodata(id);


--
-- TOC entry 2233 (class 2606 OID 250859)
-- Name: fknur48mi1ci39o2tgt8me76ll1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyfilter
    ADD CONSTRAINT fknur48mi1ci39o2tgt8me76ll1 FOREIGN KEY (externalagency_id) REFERENCES externalagency(id);


--
-- TOC entry 2218 (class 2606 OID 250781)
-- Name: fko4asmt5ohp2q4udi5rgnsxe3d; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY fiasaddressvector
    ADD CONSTRAINT fko4asmt5ohp2q4udi5rgnsxe3d FOREIGN KEY (level7_aoid) REFERENCES fiasobject(aoid);


--
-- TOC entry 2207 (class 2606 OID 250726)
-- Name: fko6b19adi7m36rmjg8tovwks8f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY dwellinghouse
    ADD CONSTRAINT fko6b19adi7m36rmjg8tovwks8f FOREIGN KEY (id) REFERENCES abstractbuilding(id);


--
-- TOC entry 2195 (class 2606 OID 250666)
-- Name: fkot3g3hody0s49hux333hxa80x; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY agency
    ADD CONSTRAINT fkot3g3hody0s49hux333hxa80x FOREIGN KEY (watermark_id) REFERENCES dbfile(id);


--
-- TOC entry 2198 (class 2606 OID 250681)
-- Name: fkpayw0n3rxe8alel3ceac6plxr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY comment
    ADD CONSTRAINT fkpayw0n3rxe8alel3ceac6plxr FOREIGN KEY (author_login) REFERENCES agent(login);


--
-- TOC entry 2221 (class 2606 OID 250799)
-- Name: fkphej06s6kjty1erau883n6r47; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY liveleaserealty
    ADD CONSTRAINT fkphej06s6kjty1erau883n6r47 FOREIGN KEY (nameofbuilding_id) REFERENCES nameofbuilding(id);


--
-- TOC entry 2215 (class 2606 OID 250766)
-- Name: fkpmms2gkdbnanynwtfwio8b4dw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY fiasaddressvector
    ADD CONSTRAINT fkpmms2gkdbnanynwtfwio8b4dw FOREIGN KEY (level4_aoid) REFERENCES fiasobject(aoid);


--
-- TOC entry 2245 (class 2606 OID 250919)
-- Name: fkq1m95wb1ac8whsqfeylcpqasw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyobject
    ADD CONSTRAINT fkq1m95wb1ac8whsqfeylcpqasw FOREIGN KEY (objectstatus_id) REFERENCES objectstatus(id);


--
-- TOC entry 2247 (class 2606 OID 250929)
-- Name: fkqeeqvyprj5jim0wjbq8rix3pi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyobject
    ADD CONSTRAINT fkqeeqvyprj5jim0wjbq8rix3pi FOREIGN KEY (price_id) REFERENCES simpleprice(id);


--
-- TOC entry 2248 (class 2606 OID 250934)
-- Name: fkqru0toj2ujobe0v63190i45x0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY recommendation
    ADD CONSTRAINT fkqru0toj2ujobe0v63190i45x0 FOREIGN KEY (realtyobject_id) REFERENCES realtyobject(id);


--
-- TOC entry 2201 (class 2606 OID 250696)
-- Name: fkqt2meq4y1awhs1xua4471onut; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY commercearea
    ADD CONSTRAINT fkqt2meq4y1awhs1xua4471onut FOREIGN KEY (id) REFERENCES abstractarea(id);


--
-- TOC entry 2219 (class 2606 OID 250789)
-- Name: fkqy43yeini17ds3l1w3hwgi3b8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY livearea
    ADD CONSTRAINT fkqy43yeini17ds3l1w3hwgi3b8 FOREIGN KEY (id) REFERENCES abstractarea(id);


--
-- TOC entry 2191 (class 2606 OID 250646)
-- Name: fkreqg8gotp25gs7j5wb1g4atbq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY address
    ADD CONSTRAINT fkreqg8gotp25gs7j5wb1g4atbq FOREIGN KEY (fiasaddressvector_id) REFERENCES fiasaddressvector(id);


--
-- TOC entry 2243 (class 2606 OID 250909)
-- Name: fkri2v3va4456qx8559sjiorea4; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyobject
    ADD CONSTRAINT fkri2v3va4456qx8559sjiorea4 FOREIGN KEY (externalobectext_id) REFERENCES externalobectext(id);


--
-- TOC entry 2240 (class 2606 OID 250894)
-- Name: fksmha9jbjh0xa2589byhw9tnfp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY realtyobject
    ADD CONSTRAINT fksmha9jbjh0xa2589byhw9tnfp FOREIGN KEY (area_id) REFERENCES abstractarea(id);


--
-- TOC entry 2210 (class 2606 OID 250741)
-- Name: fkt3kv0ahnphe1xsp8n4ma6dymv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY externalobectext_externalphoto
    ADD CONSTRAINT fkt3kv0ahnphe1xsp8n4ma6dymv FOREIGN KEY (externalobectext_id) REFERENCES externalobectext(id);
