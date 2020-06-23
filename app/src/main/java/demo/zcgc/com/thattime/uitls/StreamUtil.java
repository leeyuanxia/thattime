package demo.zcgc.com.thattime.uitls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public class StreamUtil {
    /*流转字符串
        * @param inputStream  流对象
        * @return     流转换成的字符串
        */
    public static String streamToString(InputStream inputStream) {
        //1，再度取得过程中，将读取的内容存储值缓存中，然后一次性转换成字符串返回
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //2，读取流的操作，读到没有为止（for循环）
        byte[] buffer = new byte[1024];
        //3，记录读取内容的临时变量
        int temp = -1;
        try {
            while ((temp = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, temp);
            }
            //4,返回读取数据
            return byteArrayOutputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                byteArrayOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return null;
    }
}
