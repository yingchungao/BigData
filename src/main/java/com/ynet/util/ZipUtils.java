package com.ynet.util;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.apache.commons.lang.StringUtils.isEmpty;


/**
 * @Description: Gzip 压缩 base64编码
 */
public class ZipUtils {


    /**
     * gzip压缩 base64编码
     */
    public static String messageZip(String message) throws IOException {
        String baseMessage = null;
        if (!isEmpty(message)) {
            byte[] gzipMessage = ZipUtils.compressForGzip(message);

            baseMessage = new String(Base64.encode(gzipMessage));
            if (isEmpty(baseMessage)) {
                return "";
            }
        }
        return baseMessage;
    }

    /**
     * base64解码 gzip解压缩
     */
    public static String messageUnzip(String message) {
        if (isEmpty(message)) {
            return null;
        }
        try {
            byte[] base64Message = Base64.decode(message);
            String gzipMessage = ZipUtils.decompressForGzip(base64Message);
            if (isEmpty(gzipMessage)) {
                return message;
            }
            return gzipMessage;
        } catch (Exception e) {
            return message;
        }
    }

    /**
     * Gzip 压缩数据
     */
    public static byte[] compressForGzip(String unGzipStr) throws IOException {
        if (isEmpty(unGzipStr)) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(baos);
        gzip.write(unGzipStr.getBytes());
        gzip.close();
        byte[] encode = baos.toByteArray();
        baos.flush();
        baos.close();
        return encode;
    }


    /**
     * Gzip解压数据
     */
    public static String decompressForGzip(byte[] gzipStr) {
        ByteArrayOutputStream out = null;
        ByteArrayInputStream in = null;
        GZIPInputStream gzip = null;
        try {
            if (gzipStr == null || gzipStr.length <= 0) {
                return null;
            }
            out = new ByteArrayOutputStream();
            in = new ByteArrayInputStream(gzipStr);
            // android 对GZIPInputStream有调整，严格模式下会报错
            gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n = 0;
            while ((n = gzip.read(buffer, 0, buffer.length)) > 0) {
                out.write(buffer, 0, n);
            }
            return out.toString();
        } catch (Throwable throwable) {
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }
}
