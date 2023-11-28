package com.housaire;

import java.util.regex.Pattern;

/**
 * Desc:
 *
 * @author: Zhang Kai
 * @email: <a href="mailto:zhangkai@kltb.com.cn">zhangkai@kltb.com.cn</a>
 * @date: 2020/4/22
 */
public class PatternTest {

    public static void main(String[] args) {
        String text = "{\"encSeq\":\"4098037427\",\"transType\":\"0001\",\"signature\":\"kT9R8g2VID+0a2pO1Q0FDSsUKff0OEOCHxLJV+ghKw7BdQ7lxv7xhXdBBRAOFO9+IzkFaFofFm03poXGFt/fuRqNom6be7vkSp1jBxYYItV+p0o1L4A2DAA4SW/mxpciQ3M2kTxirvC+rAONTQhxxRWtOqiOccQwK/YMXelQK4Y=\"," +
                "\"digitalEnve\":\"ZE/ksI0tS0kfMzvWKVGDZHasp/Wbc105gph0g9Dv+Aenlr92gFTtzjSVXQLJxxZeeRdiOPV3WWoM07zk8/nTf+YIQV/Bb7t1vueKRXSOQS+eyBjTHRpvxaec4HXZKmnXuiLRodeiYRZMHRXuuxBLhO0Dxj8ivpxJWuJa5ks8L51QdNfGUgqXQK5uxybO/o8SsH6sbAsNKkfaa1IhH9LrmGvDDFI3FRHRW9SQZ3tAqeG5aiM+yQ1qNVlLsVOxRKgW+/KRVlfWE5F/GyvgHxLanaNYj6cCxNvqipVgWRdrfu/5TJFCW0DTxzRQdYw5SXXiACArLXjcTh6lLEz7J0F2Gg==\"," +
                "\"sgnSeq\":\"b573b3dca1d4d1b7\",\"msgKey\":\"30000000123116\",\"cprInsCode\":\"9731000001\"," +
                "\"transData\":\"{\\\"clientName\\\":\\\"vV2N6lSnAMmw8otFeJMUOw==\\\",\\\"cprProNbr\\\":\\\"Test0910\\\"," +
                "\\\"boundAcc\\\":\\\"B0dSZGldA1nK8mrTe51TlQ==\\\",\\\"certifId\\\":\\\"LO/ndF4/NZHy6sIsSwY6MeF0teoI0tVfmIfaPsiFsrE=\\\"," +
                "\\\"boundMobile\\\":\\\"0K6v5Aue24FHxdONeQZIDA==\\\",\\\"accFlag\\\":\\\"3\\\",\\\"accTag\\\":\\\"C\\\"}\",\"version\":\"1.0\"}";
        Pattern pattern = Pattern.compile("\\{\"encSeq\":\"4098037427\",\"transType\":\"0001\",\"signature\":\".*\",\"digitalEnve\":\".*\",\"sgnSeq\":\"b573b3dca1d4d1b7\",\"msgKey\":\".*\",\"cprInsCode\":\".*\",\"transData\":\"\\{\\\\\"clientName\\\\\":\\\\\".*\\\\\",\\\\\"cprProNbr\\\\\":\\\\\".*\\\\\",\\\\\"boundAcc\\\\\":\\\\\".*\\\\\",\\\\\"certifId\\\\\":\\\\\".*\\\\\",\\\\\"boundMobile\\\\\":\\\\\".*\\\\\",\\\\\"accFlag\\\\\":\\\\\"3\\\\\",\\\\\"accTag\\\\\":\\\\\"C\\\\\"\\}\",\"version\":\"1.0\"\\}");
        System.out.println(pattern.toString());
        System.out.println(pattern.matcher(text).matches());
    }

}
