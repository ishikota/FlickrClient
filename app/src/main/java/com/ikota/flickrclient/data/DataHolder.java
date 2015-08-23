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
