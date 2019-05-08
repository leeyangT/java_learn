package IO;

import java.io.*;

public class IOTest {
    public void runTest(){
    }

    /*
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public static void readFileByBytes(String inFile, String outFile) {
        InputStream in = null;
        OutputStream out = null;
        try {
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(inFile);
            out = new FileOutputStream(outFile);
            while ((byteread = in.read(tempbytes)) != -1) {
                out.write(tempbytes, 0, byteread);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
                try {
                    out.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}
