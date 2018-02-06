package com.gt.mall;

import com.gt.api.util.KeysUtil;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2018/2/2 0002
 * Time : 16:27
 */
public class TestMain {

    public static void main( String[] args ) throws Exception {
        KeysUtil k = new KeysUtil();
        System.out.println( "ss = " + k.getDesStr(
            "80771459766d5013c9b1893ce16871a74aba1a8ecce4089af715237319609278ceb073641093293da02a95fe963aaacb29c886a3c7564232fe4b026955fa40f7baac4856dfd284a45c250cee818e94db6844e9470199c8461c9f985ab7cf9700889a56d676bd0f5ff587f9e3bb1221ebd83b5a4714d6af76719c4915665068e754dfde7dc4918342e71418a1a05fbab97f6ec8979fe6bcfe477c9d54be4dbb398c370dd0ba4a57bd128bd6b37c80cda7ed65f914ae6d88ff4202e8d3dfb93794f960923d374180e43268b5e7acfe65365fd63f8c4f8bb12187a35a68faff7bd2671f248bcab93d20d7cb23fda096271e756e5c19c336701c27271e103002d945fbc616a6500448f79e278a6b503cfa50f715237319609278ceb073641093293da02a95fe963aaacb9fe9ed8fdd819b5c9d0a7f74fe6039c3a6de15baf2ccec998632cfe5e6376aa2c4df3ed2e6f9a0129b3416dd1849c08ee1f0e750709e8b68" ) );
    }
}
