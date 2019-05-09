package IO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class NIOTest {
    private ByteBuffer bbEchoBuffer = ByteBuffer.allocate(1024);

    public void startServer(){
        try {
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(8700));
            ssc.configureBlocking(false);
            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                int iNum = selector.select();
                Set<SelectionKey> stSelectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> itKey = stSelectionKeys.iterator();
                while (itKey.hasNext()) {
                    SelectionKey skKey = itKey.next();
                    if (skKey.isAcceptable()) {
                        acceptClient(selector, skKey);
                    } else if (skKey.isReadable()) {
                        readInfo(skKey);
                    }
                    itKey.remove();
                }
            }
        }
        catch (IOException e){
            System.out.println("error "+ e.getMessage());
        }


    }

    private void acceptClient(Selector selector, SelectionKey skKey){
        try {
            ServerSocketChannel sscNew = (ServerSocketChannel)skKey.channel();
            SocketChannel sc = sscNew.accept();
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);
            System.out.println("accept new client");
        }
        catch (IOException e){
            System.out.println("accept error " + e.getMessage());
        }
    }

    private void readInfo(SelectionKey skKey){
        try {
            SocketChannel sc = (SocketChannel)skKey.channel();
            String strReadInfo = "";
            int iTotalNum = 0;
            while (true) {
                bbEchoBuffer.clear();

                int iReadNum = sc.read(bbEchoBuffer);
                if (iReadNum <= 0) {
                    break;
                }
                bbEchoBuffer.flip();
                String strTemp = decode();
                strReadInfo += strTemp;
                iTotalNum += iReadNum;
            }
            if (iTotalNum == 0 || !sc.isOpen()) {
                sc.close();
            }
            else{
                ByteBuffer ret = convertStringToByte("receive:" + strReadInfo);
                sc.write(ret);
                System.out.println("recive " + strReadInfo + "   from " + sc);
            }
        }
        catch (IOException e){
            System.out.println("read error " + e.getMessage());
        }
    }

    private String decode(){
        Charset charset = Charset.forName("utf-8");
        return charset.decode(bbEchoBuffer).toString();
    }

    private ByteBuffer convertStringToByte(String content) throws UnsupportedEncodingException {
        return ByteBuffer.wrap(content.getBytes("utf-8"));
    }
}
