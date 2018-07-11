package com.casic.yqgl.controller;

import com.casic.yqgl.model.Collector;
import com.casic.yqgl.model.Instrument;
import com.casic.yqgl.service.CollectorService;
import com.casic.yqgl.uitls.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.List;

@Controller
public class collectorController implements ApplicationRunner {
    /**
     * 设置UDP IP，端口
     */
    @Value("${udp.receivePort}")
    private int receivePort;

    @Autowired
    private CollectorService collectorService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(new ReceiveThread()).start();

    }
    @RequestMapping("collector")
    public String collector(Model model){

        return "collector";
    }
    @RequestMapping("getCollectorListPage")
    @ResponseBody
    public PageHelper<Collector> getCollectorListPage(Collector collector, HttpServletRequest request){
        PageHelper<Collector> pageHelper = new PageHelper<Collector>();
        Integer total = collectorService.getTotal();
        List<Collector> collectorListPage = collectorService.getCollectorListPage(collector);
        pageHelper.setTotal(total);
        pageHelper.setRows(collectorListPage);

        return pageHelper;
    }
    /**
     * 接受融合数据
     */
    public class ReceiveThread implements Runnable {

        public void run() {
            DatagramSocket socket = null;
            try {
                //组播
                socket = new DatagramSocket(receivePort);
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte[] bytes = new byte[1024];
            DatagramPacket packet = new DatagramPacket(bytes,bytes.length);
            while (true) {
                try {
                    socket.receive(packet);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                byte[] data = packet.getData();


            }

//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date1);



        }
    }

    /**
     * set collertor ip/port/gateway/server ip/server port
     * @throws Exception
     */
    private void setCollector(String collectorIp,Integer collectorPort,String serverIp,Integer serverPort) throws Exception {
        DatagramSocket setCollectorSocket = null;
        try {
            setCollectorSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte[] headBytes =  new byte[]{0x52,0x15,0x50};


        if(setCollectorSocket!=null){
            setCollectorSocket.close();
        }
    }
    /**
     * get collertor version
     * @throws Exception
     */
    private void getCollectorVersion(String sendIp,Integer sendPort) throws Exception {
        DatagramSocket setCollectorSocket = null;
        try {
            setCollectorSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte[] headBytes =  new byte[]{0x52,0x03,0x0F};
        byte[] csBytes = new byte[]{Byte.parseByte(makeCheckSum(new String(headBytes)))};

        byte[] sendBytes = byteMerger(headBytes,csBytes);
        try {
            send(setCollectorSocket,sendIp,sendPort,sendBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }


        if(setCollectorSocket!=null){
            setCollectorSocket.close();
        }
    }
    /**
     * UDP发送数据
     * @param bytes
     * @throws Exception
     */
    private void send(DatagramSocket socket,String ip,Integer port,byte[] bytes) throws Exception {
        InetAddress IPaddr = InetAddress.getByName(ip);
        DatagramPacket packet;
        packet = new DatagramPacket(bytes,bytes.length,IPaddr,port);
        socket.send(packet);
    }
    /**
     * byteMerger
     * @param bytes1
     * @param bytes2
     * @return
     */
    private static byte[] byteMerger(byte[] bytes1,byte[] bytes2){
        byte[] bytes3 = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1,0,bytes3,0,bytes1.length);
        System.arraycopy(bytes2,0,bytes3,bytes1.length,bytes2.length);
        return bytes3;
    }
    /** 计算校验位 ，返回十六进制校验位 */
    private static String makeCheckSum(String data) {
        int dSum = 0;
        int length = data.length();
        int index = 0;
        // 遍历十六进制，并计算总和
        while (index < length) {
            String s = data.substring(index, index + 2); // 截取2位字符
            dSum += Integer.parseInt(s, 16); // 十六进制转成十进制 , 并计算十进制的总和
            index = index + 2;
        }

        int mod = dSum % 256; // 用256取余，十六进制最大是FF，FF的十进制是255
        String checkSumHex = Integer.toHexString(mod); // 余数转成十六进制
        length = checkSumHex.length();
        if (length < 2) {
            checkSumHex = "0" + checkSumHex;  // 校验位不足两位的，在前面补0
        }
        return checkSumHex;
    }
    /**
     * 将ip字符串转为byte数组,注意:ip不可以是域名,否则会进行域名解析
     * @param ip
     * @return byte[]
     * @throws UnknownHostException
     */
    private static byte[] ipToBytes(String ip) throws UnknownHostException {
        return InetAddress.getByName(ip).getAddress();
    }
}
