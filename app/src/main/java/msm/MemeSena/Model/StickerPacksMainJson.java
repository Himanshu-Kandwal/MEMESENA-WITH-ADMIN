package msm.MemeSena.Model;

import java.util.ArrayList;

public class StickerPacksMainJson {
    private ArrayList<StickerPack> sticker_packs;

    private String ios_app_store_link = "https://play.google.com/store/apps/details?id=com.app.WAStickerApps.StickerMaker&hl=en";

    private String android_play_store_link;

    public ArrayList<StickerPack> getSticker_packs() {
        return sticker_packs;
    }

    public void setSticker_packs(ArrayList<StickerPack> sticker_packs) {
        this.sticker_packs = sticker_packs;
    }

    public String getIos_app_store_link() {
        return ios_app_store_link;
    }

    public void setIos_app_store_link(String ios_app_store_link) {
        this.ios_app_store_link = ios_app_store_link;
    }

    public String getAndroid_play_store_link() {
        return android_play_store_link;
    }

    public void setAndroid_play_store_link(String android_play_store_link) {
        this.android_play_store_link = android_play_store_link;
    }

    @Override
    public String toString() {
        return "ClassPojo [sticker_packs = " + sticker_packs + ", ios_app_store_link = " + ios_app_store_link + ", android_play_store_link = " + android_play_store_link + "]";
    }
}
