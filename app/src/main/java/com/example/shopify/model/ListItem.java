package com.example.shopify.model;

import java.util.ArrayList;

public class ListItem {
    public ArrayList<Item> item;

    public ListItem(){
        item = new ArrayList<>();
        item.add(Compuss);
        item.add(Supermen);
        item.add(King);
        item.add(Orea);
        item.add(Religion);
        item.add(Queen);
        item.add(Wakanda);
        item.add(MetalBoy);
        item.add(Naiki);
        item.add(Adidos);
        item.add(Traveler);
        item.add(Verkah);
    }

    public static final Item Compuss = new Item ("Compuss", "Shoes", 567000, "https://id-live-05.slatic.net/p/8eec48555a892244e74ddecc1de7e84a.jpg_720x720q80.jpg_.webp");
    public static final Item Supermen = new Item ("Supermen", "Jacket", 900000, "https://www.aplusleather.com/wp-content/uploads/2020/08/Superman_Man_of_Steel_Jacket__50538_zoom.jpg");
    public static final Item King = new Item ("King", "Shirt", 699000, "https://cf.shopee.ph/file/32afcf11aee3e196c872bc0cbcacb98f");
    public static final Item Orea = new Item ("Orea", "Bag", 810000, "https://ih1.redbubble.net/image.864294724.3530/drawstring_bag,x750-pad,750x1000,f8f8f8.u1.jpg");
    public static final Item Religion = new Item ("Religion", "Shirt", 999999, "https://evolution3sixty.com/wp-content/uploads/2017/07/all-releigions-sht.jpg");
    public static final Item Queen = new Item ("Queen", "Shirt", 9660000, "https://ae01.alicdn.com/kf/H042badd867234aa081b643515c44b601w/We-Will-Rock-You-Women-T-Shirt-Summer-cotton-Queen-Rock-Band-T-shirt-loose-streetwear.jpg");
    public static final Item Wakanda = new Item ("Wakanda", "Shirt", 1200000, "https://cf.shopee.co.id/file/259ff4fb0c0ac2ed8fa572067a78de10");
    public static final Item MetalBoy = new Item ("Metal Boy", "Shirt", 880000, "https://s3.bukalapak.com/img/8610852353/large/Fall_Out_Boy_T_Shirt_FOB_Heavy_Metal_Broke_My_Heart.jpg");
    public static final Item Naiki = new Item ("Naiki", "Shoes", 2350000,"https://static.nike.com/a/images/c_limit,w_318,f_auto/t_product_v1/ed1b459f-e09a-4b64-9b29-fb0c5d78b49f/air-force-1-07-high-mens-shoe-XTJLXX.png");
    public static final Item Adidos = new Item ("Adidos", "Shoes", 1750000,"https://i.pinimg.com/originals/3e/5f/6e/3e5f6e90eb0a7cbb62fc5546ab31c809.jpg");
    public static final Item Traveler = new Item ("Traveler", "Bag", 770000,"https://images.tokopedia.net/img/cache/500-square/VqbcmM/2020/12/27/c5ea01f4-6da4-46ce-94e6-b98dbd1ca686.jpg");
    public static final Item Verkah = new Item ("Verkah", "Jacket", 580000,"https://wooleyshop.com/images/WATERMARK/201906/146676/baju-koko-lengan-panjang-anak-laki-laki-w-1833-15110713925.jpg");
}
