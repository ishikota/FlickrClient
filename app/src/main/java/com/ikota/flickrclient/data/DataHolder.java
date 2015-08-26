package com.ikota.flickrclient.data;


/** Sample json data of Flickr API */
public class DataHolder {

    // return of GET(https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&page=1&per_page=20&format=json&nojsoncallback=1&api_key=84434e44ac54eb2853b6b4492daf863e)
    public static final String LIST_JSON =
            "{\"photos\":{" +
                    "\"page\":1,\"pages\":25,\"perpage\":20,\"total\":500,\"photo\":" +
                    "[" +
                    "{\"id\":\"20623135501\",\"owner\":\"30179751@N06\",\"secret\":\"f755f50f34\",\"server\":\"686\",\"farm\":1,\"title\":\"Sundown on the Oregon Coast\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20436638719\",\"owner\":\"127665714@N08\",\"secret\":\"1b89366a3f\",\"server\":\"5746\",\"farm\":6,\"title\":\"Seat with a view\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20433576929\",\"owner\":\"125053471@N07\",\"secret\":\"ce5ba995aa\",\"server\":\"5713\",\"farm\":6,\"title\":\"Lead Singer... Center Stage :)\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20421255628\",\"owner\":\"8352571@N04\",\"secret\":\"7f6c35a4de\",\"server\":\"667\",\"farm\":1,\"title\":\"Guidepost\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20005411074\",\"owner\":\"68358198@N00\",\"secret\":\"de8487a943\",\"server\":\"616\",\"farm\":1,\"title\":\"Snowy Plover - 2M2K15-490\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20608589842\",\"owner\":\"80036114@N08\",\"secret\":\"959230ba72\",\"server\":\"628\",\"farm\":1,\"title\":\"Mandarin - Aix galericulata\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20443606160\",\"owner\":\"34234700@N08\",\"secret\":\"e54e8a9885\",\"server\":\"734\",\"farm\":1,\"title\":\"St Pauls Cathedral and some light streams\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20639914971\",\"owner\":\"116475418@N03\",\"secret\":\"36877737ac\",\"server\":\"621\",\"farm\":1,\"title\":\"calm waters\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20435139180\",\"owner\":\"73331227@N05\",\"secret\":\"6bb62032fb\",\"server\":\"648\",\"farm\":1,\"title\":\"Amtrak\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"19996441434\",\"owner\":\"36587311@N08\",\"secret\":\"e41bbc7e96\",\"server\":\"635\",\"farm\":1,\"title\":\"Beach\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20623472152\",\"owner\":\"41835982@N02\",\"secret\":\"88146174ef\",\"server\":\"686\",\"farm\":1,\"title\":\"Fantastic light in Kermorvan\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20427377958\",\"owner\":\"126889546@N08\",\"secret\":\"0a053b1324\",\"server\":\"679\",\"farm\":1,\"title\":\"Bloms\\u00f8ya hut\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20630662051\",\"owner\":\"67560866@N02\",\"secret\":\"b2ab3f4065\",\"server\":\"5718\",\"farm\":6,\"title\":\"Kirkjufell\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20009388703\",\"owner\":\"52725579@N05\",\"secret\":\"5d2f4b6e91\",\"server\":\"570\",\"farm\":1,\"title\":\"Straw Telegraph\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20610836792\",\"owner\":\"46059838@N04\",\"secret\":\"8744c40c31\",\"server\":\"598\",\"farm\":1,\"title\":\"Morning light is the photograph's blessing( Explore)\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20446803500\",\"owner\":\"17306541@N06\",\"secret\":\"f5f7d13204\",\"server\":\"702\",\"farm\":1,\"title\":\"Expanses\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20604753276\",\"owner\":\"74797585@N05\",\"secret\":\"190f1e3617\",\"server\":\"5645\",\"farm\":6,\"title\":\"Jewel of Industry\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20439052928\",\"owner\":\"32218103@N08\",\"secret\":\"e23e4dfdf1\",\"server\":\"613\",\"farm\":1,\"title\":\"Memory\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"19995248944\",\"owner\":\"96944030@N07\",\"secret\":\"b4a7c083e8\",\"server\":\"686\",\"farm\":1,\"title\":\"Field of dreams\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "{\"id\":\"20424042228\",\"owner\":\"100175587@N02\",\"secret\":\"31732d4f53\",\"server\":\"691\",\"farm\":1,\"title\":\"- It's a big world!  (1)\",\"ispublic\":1,\"isfriend\":0,\"isfamily\":0" +
                    "}" +
                    "]" +
                    "},\"stat\":\"ok\"}";

    public static final String LIST_JSON_ERROR = "{ \"photos\": { \"page\": 3, \"pages\": 21, \"perpage\": 24, \"total\": \"500\", \n" +
            "    \"photo\": [\n" +
            "      { \"id\": \"20682344538\", \"owner\": \"67258907@N02\", \"secret\": \"ef33f246c3\", \"server\": \"642\", \"farm\": 1, \"title\": \"Mc Donnell MD11 - FedEx\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20234226914\", \"owner\": \"51037533@N00\", \"secret\": \"d7f053e692\", \"server\": \"5772\", \"farm\": 6, \"title\": \"Blue Mosque Reflection\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20681199278\", \"owner\": \"51343158@N05\", \"secret\": \"81bd80fd96\", \"server\": \"709\", \"farm\": 1, \"title\": \"Flickan och havet (del två)\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20678808228\", \"owner\": \"45934115@N06\", \"secret\": \"e9f605dd3f\", \"server\": \"5711\", \"farm\": 6, \"title\": \"Caballito del diablo\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20256839063\", \"owner\": \"10549218@N04\", \"secret\": \"e44f08b3bb\", \"server\": \"679\", \"farm\": 1, \"title\": \"Nader's Nightmare\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20881699001\", \"owner\": \"96571908@N07\", \"secret\": \"4498e34576\", \"server\": \"5817\", \"farm\": 6, \"title\": \"Bella Venezia\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20867256801\", \"owner\": \"101624127@N07\", \"secret\": \"262e6e03e1\", \"server\": \"5663\", \"farm\": 6, \"title\": \"The Tahoe Queen\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20235990363\", \"owner\": \"35977661@N00\", \"secret\": \"3388fdcb6e\", \"server\": \"626\", \"farm\": 1, \"title\": \"Scale - We are Small Travelers in a Large World\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20876089915\", \"owner\": \"95577050@N02\", \"secret\": \"ea7140babc\", \"server\": \"5635\", \"farm\": 6, \"title\": \"Ich bin ein Berliner\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20684936068\", \"owner\": \"8999293@N03\", \"secret\": \"06a6120b74\", \"server\": \"5820\", \"farm\": 6, \"title\": \"withered\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20888135551\", \"owner\": \"85259079@N00\", \"secret\": \"e9c5c49205\", \"server\": \"600\", \"farm\": 1, \"title\": \"Scheldelaan\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20876364021\", \"owner\": \"66748278@N03\", \"secret\": \"e0ea9755d6\", \"server\": \"574\", \"farm\": 1, \"title\": \"Enseada do Canal Saquarema Sunset\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20868550702\", \"owner\": \"7379660@N05\", \"secret\": \"cd7f028064\", \"server\": \"727\", \"farm\": 1, \"title\": \"Sunset and Smoke\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20244679494\", \"owner\": \"111031505@N04\", \"secret\": \"288729d4c1\", \"server\": \"609\", \"farm\": 1, \"title\": \"Visby.\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20884299941\", \"owner\": \"63193389@N00\", \"secret\": \"6b7f03db08\", \"server\": \"762\", \"farm\": 1, \"title\": \"Coucher sur la Corniche à Urrugne\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20874034651\", \"owner\": \"57853578@N03\", \"secret\": \"58f39bac9d\", \"server\": \"5631\", \"farm\": 6, \"title\": \"Ballymorran Bay\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20246382234\", \"owner\": \"89254589@N03\", \"secret\": \"6c4c1bc78c\", \"server\": \"774\", \"farm\": 1, \"title\": \"The Black Hole.  Fondation Folon\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20239679674\", \"owner\": \"76360656@N08\", \"secret\": \"402f46c3a7\", \"server\": \"622\", \"farm\": 1, \"title\": \"Sunset at Lake Cospuden\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20692691998\", \"owner\": \"94902477@N05\", \"secret\": \"61b6bf940a\", \"server\": \"5750\", \"farm\": 6, \"title\": \"On the prairie\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20686312920\", \"owner\": \"118662564@N05\", \"secret\": \"e38d02a8bc\", \"server\": \"5624\", \"farm\": 6, \"title\": \"cala luna\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20853489272\", \"owner\": \"44915680@N05\", \"secret\": \"6b9ce56050\", \"server\": \"772\", \"farm\": 1, \"title\": \".\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20259286253\", \"owner\": \"54656712@N06\", \"secret\": \"f306ace932\", \"server\": \"771\", \"farm\": 1, \"title\": \"Night colors\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 },\n" +
            "      { \"id\": \"20832943556\", \"owner\": \"73746571@N00\", \"secret\": \"b59c96588d\", \"server\": \"640\", \"farm\": 1, \"title\": \"Us out\", \"ispublic\": 1, \"isfriend\": 0, \"isfamily\": 0 }\n" +
            "    ] }, \"stat\": \"ok\" }";

    public static final String LIST_ITEM_JSON =
            "{\"farm\":1,\"id\":\"20697268356\",\"isfamily\":0,\"isfriend\":0,\"ispublic\":1,\"owner\":\"123449478@N07\",\"secret\":\"c173eb3ebc\",\"server\":\"644\",\"title\":\"A Quiet Evening\"}";


    // return of GET(https://api.flickr.com/services/rest/?method=flickr.photos.getInfo&photo_id=20623135501&format=json&nojsoncallback=1&api_key=84434e44ac54eb2853b6b4492daf863e)
    public static final String DETAIL_JSON =
            "{\"photo\":" +
                    "{\"id\":\"20623135501\",\"secret\":\"f755f50f34\",\"server\":\"686\",\"farm\":1,\"dateuploaded\":\"1439709383\",\"isfavorite\":0,\"license\":\"0\",\"safety_level\":\"0\",\"rotation\":0,\"owner\":{\"nsid\":\"30179751@N06\",\"username\":\"Cole Chase Photography\",\"realname\":\"\",\"location\":\"Iowa City, USA\",\"iconserver\":\"3919\",\"iconfarm\":4,\"path_alias\":\"cannon_s5_is\"}," +
                    "\"title\":{\"_content\":\"Sundown on the Oregon Coast\"}," +
                    "\"description\":{\"_content\":\"I was treated to this brilliant sunset on my recent trip to Oregon.  This photo was captured in Ecola State Park which is one of my favorite places to visit on the coast. The deactivated Tillamook Rock Lighthouse is seen on the small island in the distance.  Comments &amp; views are always appreciated. Thanks for looking &amp; have a wonderful Sunday!\"}," +
                    "\"visibility\":{\"ispublic\":1,\"isfriend\":0,\"isfamily\":0}," +
                    "\"dates\":{\"posted\":\"1439709383\",\"taken\":\"2015-08-02 22:40:28\",\"takengranularity\":\"0\",\"takenunknown\":\"0\",\"lastupdate\":\"1439807189\"}," +
                    "\"views\":\"5369\",\"editability\":{\"cancomment\":0,\"canaddmeta\":0}," +
                    "\"publiceditability\":{\"cancomment\":1,\"canaddmeta\":0}," +
                    "\"usage\":{\"candownload\":0,\"canblog\":0,\"canprint\":0,\"canshare\":1}," +
                    "\"comments\":{\"_content\":\"58\"}," +
                    "\"notes\":{\"note\":[]}," +
                    "\"people\":{\"haspeople\":0}," +
                    "\"tags\":" +
                        "{\"tag\":[" +
                        "{\"id\":\"30134429-20623135501-32576\",\"author\":\"30179751@N06\",\"authorname\":\"Cole Chase Photography\",\"raw\":\"Cannon Beach\",\"_content\":\"cannonbeach\",\"machine_tag\":0}," +
                        "{\"id\":\"30134429-20623135501-38462370\",\"author\":\"30179751@N06\",\"authorname\":\"Cole Chase Photography\",\"raw\":\"Tillamook Rock Light\",\"_content\":\"tillamookrocklight\",\"machine_tag\":0}," +
                        "{\"id\":\"30134429-20623135501-121158\",\"author\":\"30179751@N06\",\"authorname\":\"Cole Chase Photography\",\"raw\":\"Ecola State Park\",\"_content\":\"ecolastatepark\",\"machine_tag\":0}," +
                        "{\"id\":\"30134429-20623135501-21939\",\"author\":\"30179751@N06\",\"authorname\":\"Cole Chase Photography\",\"raw\":\"Oregon Coast\",\"_content\":\"oregoncoast\",\"machine_tag\":0}," +
                        "{\"id\":\"30134429-20623135501-223\",\"author\":\"30179751@N06\",\"authorname\":\"Cole Chase Photography\",\"raw\":\"Sunset\",\"_content\":\"sunset\",\"machine_tag\":0}," +
                        "{\"id\":\"30134429-20623135501-17997\",\"author\":\"30179751@N06\",\"authorname\":\"Cole Chase Photography\",\"raw\":\"Pacific Northwest\",\"_content\":\"pacificnorthwest\",\"machine_tag\":0}," +
                        "{\"id\":\"30134429-20623135501-1382\",\"author\":\"30179751@N06\",\"authorname\":\"Cole Chase Photography\",\"raw\":\"Canon\",\"_content\":\"canon\",\"machine_tag\":0}," +
                        "{\"id\":\"30134429-20623135501-40314083\",\"author\":\"30179751@N06\",\"authorname\":\"Cole Chase Photography\",\"raw\":\"5 D Mark III\",\"_content\":\"5dmarkiii\",\"machine_tag\":0}," +
                        "{\"id\":\"30134429-20623135501-245\",\"author\":\"30179751@N06\",\"authorname\":\"Cole Chase Photography\",\"raw\":\"Summer\",\"_content\":\"summer\",\"machine_tag\":0}" +
                    "]}," +
                    "\"location\":{\"latitude\":\"45.918437\",\"longitude\":\"-123.975777\",\"accuracy\":\"14\",\"context\":\"0\"," +
                        "\"county\":{\"_content\":\"Clatsop\",\"place_id\":\"AadMDLJQUL_wGgEW_A\",\"woeid\":\"12589695\"}," +
                        "\"region\":{\"_content\":\"Oregon\",\"place_id\":\"nWffZiZTUb4mWuFa\",\"woeid\":\"2347596\"}," +
                        "\"country\":{\"_content\":\"United States\",\"place_id\":\"nz.gsghTUb4c2WAecA\",\"woeid\":\"23424977\"}," +
                        "\"place_id\":\"AadMDLJQUL_wGgEW_A\",\"woeid\":\"12589695\"}," +
                    "\"geoperms\":{\"ispublic\":1,\"iscontact\":0,\"isfriend\":0,\"isfamily\":0}," +
                    "\"urls\":" +
                        "{\"url\":" +
                            "[" +
                                "{\"type\":\"photopage\",\"_content\":\"https:\\/\\/www.flickr.com\\/photos\\/cannon_s5_is\\/20623135501\\/\"}" +
                            "]" +
                        "}," +
                    "\"media\":\"photo\"}" +
                    ",\"stat\":\"ok\"}";

    public static final String PEOPLE_INFO =
            "{\"person\":" +
                    "{\"id\":\"133363540@N06\"," +
                    "\"nsid\":\"133363540@N06\"," +
                    "\"ispro\":0," +
                    "\"can_buy_pro\":0," +
                    "\"iconserver\":\"562\"," +
                    "\"iconfarm\":1," +
                    "\"path_alias\":null," +
                    "\"has_stats\":\"0\"," +
                    "\"username\":" +
                            "{\"_content\":\"kota_ishimoto\"}," +
                    "\"realname\":" +
                            "{\"_content\":\"Kota Ishimoto\"}," +
                    "\"mbox_sha1sum\":" +
                            "{\"_content\":\"db7538a4b6c2c45fc9912725327d66c4b8bb2b27\"}," +
                    "\"location\":" +
                            "{\"_content\":\"\"}," +
                    "\"description\":" +
                            "{\"_content\":\"\"}," +
                    "\"photosurl\":" +
                            "{\"_content\":\"https:\\/\\/www.flickr.com\\/photos\\/133363540@N06\\/\"}," +
                    "\"profileurl\":" +
                            "{\"_content\":\"https:\\/\\/www.flickr.com\\/people\\/133363540@N06\\/\"}," +
                    "\"mobileurl\":" +
                            "{\"_content\":\"https:\\/\\/m.flickr.com\\/photostream.gne?id=133318218\"}," +
                    "\"photos\":" +
                            "{\"firstdatetaken\":" +
                                "{\"_content\":\"2007-07-09 09:16:08\"}," +
                                "\"firstdate\":{\"_content\":\"1434723851\"}," +
                                "\"count\":{\"_content\":53}," +
                                "\"views\":{\"_content\":\"0\"}" +
                                "}" +
                            "}" +
                    ",\"stat\":\"ok\"" +
                    "}";
}
