package org.example;


import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class Main {
    static void main()
    {
        try
        {
            var fos = new FileOutputStream("1.bin");
            String name = "Akane";
            byte[] bytes = name.getBytes(StandardCharsets.UTF_8);
            fos.write(bytes);
            fos.flush();

            //int test = 1234;
            //fos.write(test);

            double pi = 3.1415926;
            bytes = ByteBuffer.allocate(Double.BYTES).putDouble(pi).array();
            fos.write(bytes);

            fos.close();
        }
        catch(Exception e)
        {

        }
    }
}
