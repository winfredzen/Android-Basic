# Socket数据与协议

如果直接使用Java Socket来进行通信，还是有些问题需要注意



## 发送与解析自定义模型数据

一般而言，需要将发送的数据，或者解析的数据，封装为某个bean，然后发送，但socket发送的是字节数据，所以，可能需要转换

参考：

+ [构建和解析自定义协议消息](https://wiki.jikexueyuan.com/project/java-socket/user-defined.html)



```java
import java.io.ByteArrayInputStream;  
import java.io.ByteArrayOutputStream;  
import java.io.DataInputStream;  
import java.io.DataOutputStream;  
import java.io.IOException;  

public class VoteMsgBinCoder implements VoteMsgCoder {  

  // manifest constants for encoding  
  public static final int MIN_WIRE_LENGTH = 4;  
  public static final int MAX_WIRE_LENGTH = 16;  
  public static final int MAGIC = 0x5400;  
  public static final int MAGIC_MASK = 0xfc00;  
  public static final int MAGIC_SHIFT = 8;  
  public static final int RESPONSE_FLAG = 0x0200;  
  public static final int INQUIRE_FLAG =  0x0100;  

  public byte[] toWire(VoteMsg msg) throws IOException {  
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();  
    DataOutputStream out = new DataOutputStream(byteStream); // converts ints  

    short magicAndFlags = MAGIC;  
    if (msg.isInquiry()) {  
      magicAndFlags |= INQUIRE_FLAG;  
    }  
    if (msg.isResponse()) {  
      magicAndFlags |= RESPONSE_FLAG;  
    }  
    out.writeShort(magicAndFlags);  
    // We know the candidate ID will fit in a short: it's > 0 && < 1000   
    out.writeShort((short) msg.getCandidateID());  
    if (msg.isResponse()) {  
      out.writeLong(msg.getVoteCount());  
    }  
    out.flush();  
    byte[] data = byteStream.toByteArray();  
    return data;  
  }  

  public VoteMsg fromWire(byte[] input) throws IOException {  
    // sanity checks  
    if (input.length < MIN_WIRE_LENGTH) {  
      throw new IOException("Runt message");  
    }  
    ByteArrayInputStream bs = new ByteArrayInputStream(input);  
    DataInputStream in = new DataInputStream(bs);  
    int magic = in.readShort();  
    if ((magic & MAGIC_MASK) != MAGIC) {  
      throw new IOException("Bad Magic #: " +  
                ((magic & MAGIC_MASK) >> MAGIC_SHIFT));  
    }  
    boolean resp = ((magic & RESPONSE_FLAG) != 0);  
    boolean inq = ((magic & INQUIRE_FLAG) != 0);  
    int candidateID = in.readShort();  
    if (candidateID < 0 || candidateID > 1000) {  
      throw new IOException("Bad candidate ID: " + candidateID);  
    }  
    long count = 0;  
    if (resp) {  
      count = in.readLong();  
      if (count < 0) {  
        throw new IOException("Bad vote count: " + count);  
      }  
    }  
    // Ignore any extra bytes  
    return new VoteMsg(resp, inq, candidateID, count);  
  }  
}  
```



## 粘包和分包

参考：

+ [Socket粘包问题的3种解决方案，最后一种最完美！](https://www.cnblogs.com/vipstone/p/14239160.html)



























