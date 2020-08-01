package com.ynet.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by liuliwei on 2020/1/16.
 */
public class TestClass {

    public static void main(String[] args) {

        String originalData1 = "{\"Method\":\"POST\",\"Header\":\"{\\\"host\\\":\\\"111.204.125.243\\\",\\\"content-type\\\":\\\"application\\\\\\/x-www-form-urlencoded\\\",\\\"reqv\\\":\\\"1\\\",\\\"x-real-ip\\\":\\\"192.168.65.130\\\",\\\"connection\\\":\\\"close\\\",\\\"content-length\\\":\\\"1167\\\",\\\"x-forwarded-for\\\":\\\"58.101.149.167\\\",\\\"user-agent\\\":\\\"Dalvik\\\\\\/2.1.0 (Linux; U; Android 7.0; SM-G9250 Build\\\\\\/NRD90M)\\\",\\\"accept-encoding\\\":\\\"gzip\\\",\\\"spv\\\":\\\"QW5kcm9pZHxtcGFhc2RlbW98NC4zLjYuNnxudWxsfDEuMC4wLjE=\\\",\\\"reqt\\\":\\\"1579164710496\\\"}\",\"Args\":\"{\\\"sdkData\\\":\\\"H4sIAAAAAAAAAB2UQZbDMAhDrwQEDCwxmPsfadTp6yJNCTH6EifHpUf5PHH243PGPuJr1OJ2X Ro\\\\nvOx3 aUeu0fFfUg 3rE1y4knD58bp5Tm06e\\\\\\/XnO Q0cvLmzFvitxvPZ7aOpmFsFH8pH3dz6pe76O\\\\nlKcu6efd6 PG6VTs3E n9tD9vtFNQd0aPQ f4rxCt dgjHG1u2\\\\\\/x1HJ9l6LDaH1C LJjqLLPnvtH\\\\nTn7s1JNk1zJp\\\\\\/5xfl27nZHew9ZvVvr7q Rw1g3HZBk0YB367tRnXnKxx XjFn7PFd8zOlL z84g \\\\nzfudbVO iffQrlzUG5eaPW682JiofAYdlmJZ5NysQQNJ5ffJYShnPaHoY2Q1clVi5kIM03fbKw85\\\\npuLV23hjep JYhkaFi6R2n2CL4MYNxFrPVSyKNQFNpPvOq vHZlXaYfjab6wny3KvF0D3gjW9bSb\\\\n R2p L0kP2U5pEMdDm2OtfWRjU6RBau9hHEiFUwaEvBcpVTb6wFEH0W1D259UFCG YO9uC2gIUxY\\\\nBOrFI18lzlnZLEEFQk 7IOS8bn9Qo vL3I3rDSc3DCZRVUwwUMRrGJ4\\\\\\/snXUdQFE3YHiD1q2APlc\\\\nsQL M3vn T37DX6H61imnHP0izpfGpDpIgqgdh6PnnxjfCrnnACEkhbMsqWaRxfZIbkASWnfOMP2\\\\nTF0UaS8iN\\\\\\/ZeaUQODBEaJkNWYIQ1RTBlp8vuHfXWfXGIv59ZkgLn uIbmBEsWvWH7OovbIMykzV3\\\\nevwpOs57B\\\\\\/NzcSCTewC9FPdW6zb\\\\\\/jPlFtAOlQAGgRq4VD8dD euPbtpwRyiSNJmDpCDYkQwZyb\\\\\\/3\\\\noKpTcGNlJKrIwbXqQJZVuAW2utgaBWc8gnk2absNqOhiUiG4BmmXzsZ1m0T3egCeEiLs8sl1unX1\\\\ndKYtKQfgxVUrkGtMCeuPNGKkWbVX33iM2Dsw0tVrR4sNvGBBRkQSQY2feI7j9QcbJL3zQRBIX4TE\\\\nFzbK05KSXIdaFz1fUDLjH6RgHK668Kuf1A9b7yvDTkK8OLCzgi422W8mOPbhCOOnsEUWKxAAFpKz\\\\nuZ2P0zBRAV5iQdLDYgmsAxFm\\\\\\/PHmd26d\\\\\\/9r5sIbz\\\\\\/AHDBBxiwAUAAA==\\\\n\\\",\\\"sdkKey\\\":\\\"j3jD0G03TyEBz4j0\\\"}\",\"Now\":1579181586530,\"Ip\":\"192.168.65.130\"}";
        String originalData = "{\"Method\":\"POST\",\"Header\":\"{\\\"host\\\":\\\"111.204.125.243\\\",\\\"content-type\\\":\\\"application\\\\\\/x-www-form-urlencoded\\\",\\\"reqv\\\":\\\"1\\\",\\\"x-real-ip\\\":\\\"192.168.65.130\\\",\\\"connection\\\":\\\"close\\\",\\\"content-length\\\":\\\"1960\\\",\\\"x-forwarded-for\\\":\\\"115.236.30.75\\\",\\\"user-agent\\\":\\\"Dalvik\\\\\\/2.1.0 (Linux; U; Android 9; MIX 2S MIUI\\\\\\/V11.0.3.0.PDGCNXM)\\\",\\\"accept-encoding\\\":\\\"gzip\\\",\\\"spv\\\":\\\"QW5kcm9pZHxlY29tbWVyY2VkZW1vfDQuMy42LjZ8bnVsbHwxLjA=\\\",\\\"reqt\\\":\\\"1579159500718\\\"}\",\"Args\":\"{\\\"sdkData=H4sIAAAAAAAAAKVWy5FjMQhMCcRH6AgC5R\\\\\\/S9HMCe9gpH1xTkoCmPy7312l85d52Ta9juSczJ53tUvF2wbfUzrh5H8+S0CrqVds0W9TMD43d8DWDi9tOjs\\\\\\/beUNvmKfyVrpy7r5itNeZvWTTzrdwlmS9m+VFcXCGakVKj+OCLalJITqrPah97pzB\\\\\\/YhejVfo7OtTL83KxPpM9taYtd42To934zyydkdnw1668\\\\\\/B+h1qJSp\\\\\\/5dSO1OrksgljGSB5dxS3maOdTRDy28uhp8VvvPPVZfey8NfvxqrdJDnk7yWRxRY2ue+fu\\\\\\/VTuiHy1uFtl92XcXh3TO3NH4cOzqSreOi6x+4R0fWOjz+d8k9ve2S8KldrlKPobwhsLKF5MHec+Iat7rvZ9Leb7jCwiUcIWbd2+m090b1rPv5GeJ2XM4+eY+snaDnSv04sTtE4d2RX4d\\\\\\/qllXLGnCl7yUnM5MCbdKOhiYf9a7Hy1bc0LHqEJGuXUX0cCXQ6gO1ik2S31izH0krF0iTDAK7ctBrQanPxuw3GTaqCBpU9NzqzQL3IRk2s3OgcIlCCQCSz3XvnB01u8KJAa\\\\\\/QRicHQ7K71hh\\\\\\/Gdp0HnpxeauvjeJni9T1ih3FwF+GqLZ6YOjGhswFbzLmsaFccj\\\\\\/XZ21pAKVnd96KRDQ6\\\\\\/pDMzWneYfDWoTXlTsdSysekFCSk1SLtiAdJZ1JsXx94TeL6fEVMTdYNMQ7a+StO9DjdtauMNaKC\\\\\\/idw+6mWxDHIjAQGORZZG6ylgKa73GCsWGAE2UDQvrcxr294Gi8+BwjreAZFjPQmrBPc7twgBf92nZP\\\\\\/MQPQNVWCkKmbQGx++kOesOUAeNMGenz+MeNE3PaVEBRZftHnm2uOzbp5MBr\\\\\\/Y3HQ5Kzgqhhr0kUBsIGnsnkn2ej\\\\\\/JYBdxY9t9cSFnTdvSnA9723XhFGq9CKopJWG4mL6g7VFi7xWoHgQboG\\\\\\/2KAcRORVyhu6FBd6xUBWbaD5eKU84oYJ1lyae9MIGwt6cj2DKAQWUdaCj79v\\\\\\/+SO5K8G8YJLD3B8sDlvLZyLencuPeheWaxusjcKGl57laE\\\\\\/KVRIvQyISfPozDciLUB5Lj8P49o\\\\\\/6cNdrnw8dtgd2gVcndMFcRmCF0B2Eu5jlYZjnfcGhclzgWOckvC3ixbpmbcW+n0F3egf8BaZnD00nXM4aTDbHptfk2gFD0+FPaFfi0yKSZ5z8wmY50dqwfb4IeXTAmhAMgx6an5qAWDEJ71JHjFCdj1r3Jm01Dbrx2g0TwoTFCgDA5WDrBiliPFjkWwzLxZIAPKInQOwD3pAzMoa3JdDdC07wphXiw5IB6cO0DGNMgLCZby80JLxASMSCPsB\\\\\\/L5MJsubo58ptobAFJjS0yrHViKK1ISBQ+9yPcw9ggifywTvbfYNV8DcA0BDeQvbkAFxwDMFCMKqzVilG4fFwYOcn8iEaMOAn30ZptXS4DNZ+BcpxqiuakPJ8To0Nzvfn7jyUOPO95VBoglpQFXpRhoeYN+hMqv6FaRbMEdaKMVtzjg9yBaO8TEeR47wveoMC\\\\\\/B7B1AuJzAdnNvIUkb4a2EZXwIARJnBCgosvST6cwl8bv4QMeBJYB74yvLDW5778ZU8+PIKkPPCzQtAhCd+Xl+COHACMZLIXLyGiCKnL+5OBCNSLmuv71YAFYssbgUnwYxg0QhJiAVPvBln0IWfqOOa6jWTHbAFTAziN0I0vuRG0V5CHeCLUIAdAHwgycIULhq5wCJvvKfAqEAwrDZgqDtJde+Ml+2BRB9HhW2QfJeO91kF5mB3Gxi8IZO\\\\\\/V+gNhr95LoAkAAA==&sdkKey=jGTwU3U4EzDDT5k1\\\":true}\",\"Now\":1579161233467,\"Ip\":\"192.168.65.130\"}";
        JSONObject jsonObject = JSON.parseObject(originalData);

        String argsData = (String) jsonObject.get("Args");
//        System.out.println(argsData);

        JSONObject dataObject = JSONObject.parseObject(argsData);

        String data = (String) dataObject.keySet().toArray()[0];
        System.out.println("=============>" + data);
        String s = data;
        try {
            String a[] = data.split("&");
            System.out.println("11->" + a[0]);
            System.out.println("22->" + a[1]);
            String zipString = a[0].substring("sdkData=".length());
            String key = a[1].substring("sdkKey=".length());
            System.out.println("zipString:" + zipString);
            System.out.println("key:" + key);

            String unzipStr = ZipUtils.messageUnzip(zipString);
            System.out.println("解压的数据:" + unzipStr);
            String decryptStr = new String(AESEncrypt.ECBDecrypt(unzipStr, key));
            System.out.println("解密数据:" + decryptStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
