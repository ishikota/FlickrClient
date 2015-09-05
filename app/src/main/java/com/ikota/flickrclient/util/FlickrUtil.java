package com.ikota.flickrclient.util;

import com.ikota.flickrclient.data.model.PhotoInfo;

public class FlickrUtil {

    public static PhotoInfo.Owner getKota() {
        PhotoInfo.Owner OWNER_KOTA = new PhotoInfo.Owner();
        OWNER_KOTA.nsid = "133363540@N06";
        OWNER_KOTA.username = "kota_ishimoto";
        OWNER_KOTA.realname = "Kota Ishimoto";
        OWNER_KOTA.iconfarm = "1";
        OWNER_KOTA.iconserver = "562";
        OWNER_KOTA.location = null;
        OWNER_KOTA.path_alias = "";
        return OWNER_KOTA;
    }

}
