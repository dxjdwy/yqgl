package com.casic.yqgl.controller;

import com.casic.yqgl.model.Collector;
import com.casic.yqgl.model.Event;
import com.casic.yqgl.model.Instrument;
import com.casic.yqgl.service.CollectorService;
import com.casic.yqgl.service.EventService;
import com.casic.yqgl.service.InstrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class UdpController implements ApplicationRunner {
    private static final int SYNC_TIME_TO_COLLECTOR_INTERVAL = 12*60*60*1000;
    private static final int QUERY_DATA_FROM_COLLECTOR_INTERVAL = 30*60*1000;
    private static final int COLLECTOR_PORT = 8001;
    @Autowired
    private InstrService instrService;
    @Autowired
    private CollectorService collectorService;
    @Autowired
    private EventService eventService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

    }

    /**
     * Sync time to collector
     */
    public class SyncTimeToCollector implements Runnable{

        public void run(){
            while(true){
                try{
                    Thread.sleep(20000);

                    List<Collector> collectors = collectorService.getCollectorList();

                    for (Collector collector:collectors
                         ) {
                        if (isConnect(collector.getCollectorIp())==true){
                            syncTimeToCollectorByIp(collector.getCollectorIp());
                        }else {
                            System.out.println("中继器故障");
                        }
                    }
                    Thread.sleep(SYNC_TIME_TO_COLLECTOR_INTERVAL);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }
    public void syncTimeToCollectorByIp(String ip){
        try {
            byte[] cPassword = {(byte) 0x52,(byte) 0x10,(byte) 0x0d,(byte) 0x01,(byte) 0x00,(byte) 0x00,(byte) 0x00,
                    (byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,
                    (byte) 0xCE};

            byte[] cPasswordAns = sendToCollector(cPassword,ip);

            byte[] cTime = {(byte) 0x52,(byte) 0x0a,(byte) 0x07,
                    (byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x00,
                    (byte) 0x00,(byte) 0x00,(byte) 0x00,
                    (byte) 0x00};
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cTime[3] = toBCD(cal.get(Calendar.SECOND));
            cTime[4] = toBCD(cal.get(Calendar.MINUTE));
            cTime[5] = toBCD(cal.get(Calendar.HOUR_OF_DAY));
            cTime[6] = toBCD(cal.get(Calendar.DATE));

            cTime[7] = toBCD(cal.get(Calendar.MONTH)+1);
            cTime[8] = toBCD(cal.get(Calendar.WEEK_OF_MONTH));
            cTime[9] = toBCD(cal.get(Calendar.YEAR)%100);
            cTime[10] = checkHexSum(cTime);
            byte[] setTimesAns = sendToCollector(cTime,ip);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.printf(ip+":syncTimeToCollectorByIpError");
            e.printStackTrace();

        }

    }

    public class QueryDataFromCollector implements Runnable{

        public void run(){
            while(true){
                try{
                    Thread.sleep(3000);

                    List<Collector> collectors = collectorService.getCollectorList();
                    for (Collector collector:collectors
                            ) {
                        if (isConnect(collector.getCollectorIp())==true){
                            QueryDataFromCollectorByIp(collector.getCollectorIp());
                        }else {
                            System.out.println("中继器故障");
                        }
                    }
                    Thread.sleep(QUERY_DATA_FROM_COLLECTOR_INTERVAL);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
    }

    //获取集中器信息
    public void QueryDataFromCollectorByIp(String rp_ip){
        try {

            //终端数量
            byte[] sendDevCountBytes = {(byte) 0x52,(byte) 0x06,(byte) 0x65,(byte) 0xff,(byte) 0xff,(byte) 0x00,(byte) 0x69 };

            byte[] devCountBytes = sendToCollector(sendDevCountBytes,rp_ip);

//			byte[] devCountOnLineBytes = new byte[2];
//			System.arraycopy(devCountBytes, 5, devCountOnLineBytes, 0, 2);
//			Short devCountOnLine = bytesToShort(devCountOnLineBytes);
            byte[] devCountTotalBytes = new byte[2];
            System.arraycopy(devCountBytes, 3, devCountTotalBytes, 0, 2);
            Short devCountTotal = bytesToShort(devCountTotalBytes);
            //DevNoAndAddress
            List<byte[]> DevNoAndAddress = new ArrayList<byte[]>();

            for(int devCount = 0;devCount <= devCountTotal/15; devCount++){
                //终端序号+地址
                byte[] sendDevNoAndAddressBytes = {(byte) 0x52,(byte) 0x06,(byte) 0x65,(byte) 0x01,(byte) 0x00,(byte) 0xff,(byte) 0x00 };
                sendDevNoAndAddressBytes[3] = (byte) (1+devCount*15);
                byte sendDevNoAndAddressBytesEnd = checkHexSum(sendDevNoAndAddressBytes);
                sendDevNoAndAddressBytes[6] = sendDevNoAndAddressBytesEnd ;
                byte[] DevNoAndAddressBytes = sendToCollector(sendDevNoAndAddressBytes,rp_ip);
                if(byte2int(DevNoAndAddressBytes[5])!=0){
                    for(int i=0;i<byte2int(DevNoAndAddressBytes[5]);i++){
                        byte[] b =new byte[8];
                        System.arraycopy(DevNoAndAddressBytes, 16*i+6, b, 0, 8);
                        DevNoAndAddress.add(b);

                        String DevId = Integer.toHexString(byte2int(b[7]))+Integer.toHexString(byte2int(b[6]))+Integer.toHexString(byte2int(b[5]))+Integer.toHexString(byte2int(b[4]))+Integer.toHexString(byte2int(b[3]))+Integer.toHexString(byte2int(b[2]));
                        int DevIdNo = Integer.parseInt(DevId);
                        Instrument insL = instrService.getInstrByCollectorId(DevIdNo);
                        if(insL!=null){
                            if(byte2int(DevNoAndAddressBytes[16*i+16])==1){
                                insL.setInstrConnStatus(1+"");
                                instrService.saveOrUpdate(insL);
                            }
                            if(byte2int(DevNoAndAddressBytes[16*i+16])==2){

                            }

                        }
                    }
                }

            }
            //终端数据总数
            byte[] sendDevInfoCountBytesHead = {(byte) 0x52,(byte) 0x0b,(byte) 0x70};
            for(int i=0;i<DevNoAndAddress.size();i++){
                byte[] sendDevInfoCountBytes =new byte[12];
                System.arraycopy(sendDevInfoCountBytesHead, 0, sendDevInfoCountBytes, 0, sendDevInfoCountBytesHead.length);
                System.arraycopy(DevNoAndAddress.get(i), 0, sendDevInfoCountBytes, sendDevInfoCountBytesHead.length, 8);
                byte sendDevInfoCountBytesEnd = checkHexSum(sendDevInfoCountBytes);
                sendDevInfoCountBytes[11] = sendDevInfoCountBytesEnd ;

                byte[] DevInfoCountBytes = sendToCollector(sendDevInfoCountBytes,rp_ip);
                //总数
                byte[] DevInfoCount = new byte[2];
                System.arraycopy(DevInfoCountBytes, 3, DevInfoCount, 0, DevInfoCount.length);
                //当前位置
                byte[] DevInfoposition = new byte[2];
                System.arraycopy(DevInfoCountBytes, 5, DevInfoposition, 0, DevInfoposition.length);

                short DevInfoTotal = bytesToShort(DevInfoCount);

                //读数据
                byte[] sendDevInfoReadBytesHead = {(byte) 0x52,(byte) 0x0e,(byte) 0x70};

                //终端序号(相当于原IP地址)
                byte[] idBytes =new byte[2];
                System.arraycopy(DevNoAndAddress.get(i), 0, idBytes, 0, 2);
                short id = bytesToShort(idBytes);
                String Addr = Integer.toHexString(byte2int(DevNoAndAddress.get(i)[7]))+Integer.toHexString(byte2int(DevNoAndAddress.get(i)[6]))+Integer.toHexString(byte2int(DevNoAndAddress.get(i)[5]))+Integer.toHexString(byte2int(DevNoAndAddress.get(i)[4]))+Integer.toHexString(byte2int(DevNoAndAddress.get(i)[3]))+Integer.toHexString(byte2int(DevNoAndAddress.get(i)[2]));
                if(DevInfoTotal > 8){

                    for(int j = 0;j < DevInfoTotal/8; j++){
                        byte[] sendDevInfoReadBytes =new byte[15];
                        System.arraycopy(sendDevInfoReadBytesHead, 0, sendDevInfoReadBytes, 0, sendDevInfoReadBytesHead.length);
                        System.arraycopy(DevNoAndAddress.get(i), 0, sendDevInfoReadBytes, sendDevInfoReadBytesHead.length, 8);

                        short position = (short) (j*8);
                        //开始位置
                        byte[] sendDevInfoReadPositionBytes = new byte[2];
                        sendDevInfoReadPositionBytes = shortToBytes(position);
                        System.arraycopy(sendDevInfoReadPositionBytes, 0, sendDevInfoReadBytes, sendDevInfoReadBytesHead.length + 8, sendDevInfoReadPositionBytes.length);
                        //读几条
                        sendDevInfoReadBytes[13] = (byte)0x08;
                        byte sendDevInfoReadBytesEnd = checkHexSum(sendDevInfoReadBytes);
                        sendDevInfoReadBytes[14] = sendDevInfoReadBytesEnd;

                        byte[] DevInfoReadBytes = sendToCollector(sendDevInfoReadBytes,rp_ip);
                        readDevInfo(DevInfoReadBytes,Addr);
                    }
                    byte[] sendDevInfoReadBytes =new byte[15];
                    System.arraycopy(sendDevInfoReadBytesHead, 0, sendDevInfoReadBytes, 0, sendDevInfoReadBytesHead.length);
                    System.arraycopy(DevNoAndAddress.get(i), 0, sendDevInfoReadBytes, sendDevInfoReadBytesHead.length, 8);

                    short position = (short) ((DevInfoTotal/8)*8);
                    //开始位置
                    byte[] sendDevInfoReadPositionBytes = new byte[2];
                    sendDevInfoReadPositionBytes = shortToBytes(position);
                    System.arraycopy(sendDevInfoReadPositionBytes, 0, sendDevInfoReadBytes, sendDevInfoReadBytesHead.length + 8, sendDevInfoReadPositionBytes.length);
                    //读几条
                    sendDevInfoReadBytes[13] = (byte)(DevInfoTotal-position);
                    byte sendDevInfoReadBytesEnd = checkHexSum(sendDevInfoReadBytes);
                    sendDevInfoReadBytes[14] = sendDevInfoReadBytesEnd;

                    byte[] DevInfoReadBytes = sendToCollector(sendDevInfoReadBytes,rp_ip);
                    readDevInfo(DevInfoReadBytes,Addr);

                }else{
                    byte[] sendDevInfoReadBytes =new byte[15];
                    System.arraycopy(sendDevInfoReadBytesHead, 0, sendDevInfoReadBytes, 0, sendDevInfoReadBytesHead.length);
                    System.arraycopy(DevNoAndAddress.get(i), 0, sendDevInfoReadBytes, sendDevInfoReadBytesHead.length, 8);

                    //读几条
                    sendDevInfoReadBytes[13] = (byte)DevInfoTotal;

                    byte sendDevInfoReadBytesEnd = checkHexSum(sendDevInfoReadBytes);
                    sendDevInfoReadBytes[14] = sendDevInfoReadBytesEnd;
                    byte[] DevInfoReadBytes = sendToCollector(sendDevInfoReadBytes,rp_ip);
                    readDevInfo(DevInfoReadBytes,Addr);

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.printf(rp_ip+":queryDataError");
            e.printStackTrace();
        }

    }
    //解析数据
    public void readDevInfo(byte[] rq,String Addr){
        try {
            for(int l = 0;l<byte2int(rq[5]);l++){

                byte[] minsBytes =new byte[4];
                System.arraycopy(rq, 8+26*l, minsBytes, 0, minsBytes.length);
                byte[] ABytes =new byte[2];
                System.arraycopy(rq, 14+26*l, ABytes, 0, ABytes.length);
                byte[] UpdateTimeBytes =new byte[6];
                System.arraycopy(rq, 26+26*l, UpdateTimeBytes, 0, UpdateTimeBytes.length);


                String recDate = "20"+Integer.toHexString(byte2int(UpdateTimeBytes[5]))+"年"+Integer.toHexString(byte2int(UpdateTimeBytes[4]))+"月"+Integer.toHexString(byte2int(UpdateTimeBytes[3]))+"日";
                String recTime = recDate + Integer.toHexString(byte2int(UpdateTimeBytes[2])) +"时"+ Integer.toHexString(byte2int(UpdateTimeBytes[1])) +"分"+ Integer.toHexString(byte2int(UpdateTimeBytes[0]))+"秒";

                //  recTime:realtime
                //  Addr;collectorid
                Event event = new Event();
                event.setCollectorId(Integer.parseInt(Addr));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
                Date realDate = simpleDateFormat.parse(recTime);
                event.setEventRealtime(realDate);
                event.setIpDate(recTime+Addr);
                event.setEventA(Double.valueOf(Integer.toHexString(byte2int(ABytes[1]))+"."+Integer.toHexString(byte2int(ABytes[0]))));

                Integer res = eventService.saveOrUpdate(event);
//                    System.out.println("设备名称："+h.getIpDate()+"---记录时间:"+h.getHours()+"---电流："+h.getCurrentValue());
//                    String ss = "设备名称："+h.getIpDate()+"---记录时间:"+h.getHours()+"---电流："+h.getCurrentValue();
//                    File f = new File("C:\\Users\\ht2310\\Desktop\\Log.txt");
//                    FileOutputStream fs = new FileOutputStream(f,true);
//                    PrintStream ps = new PrintStream(fs);
//                    ps.println(ss);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public byte[] sendToCollector(byte[] rq,String ip){
        byte[] datasFromRP = null;
        byte[] buffer = new byte[2048];
        OutputStream os;
        try {
            Socket socket = new Socket(ip,COLLECTOR_PORT);
            os = socket.getOutputStream();
            os.write(rq);
            os.flush();
            socket.shutdownOutput();

            InputStream is = socket.getInputStream();

            is.read(buffer);
            is.close();
            os.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;

    }

    public static int byte2int(byte b ){
        int i = (int)b&0xFF;
        return i;
    }
    public static byte checkHexSum(byte[] data){
        int total = 0;
        int len = 0;
        for(int i = 1;i<data.length;i++){
            total += byte2int(data[i]);
        }
        int mod = total % 256;

//		String hex = Integer.toHexString(mod);
//		len = hex.length();
//		if(len < 2){
//			hex = "0" + hex;
//		}
        return (byte)mod;
    }
    public static byte toBCD(int i){
        int i0 = i%10;
        int i1 = i/10;
        int res = i1*16 + i0;

        return (byte)res;
    }
    public static byte[] shortToBytes(short value){
        byte[] src = new byte[2];
        src[1] = (byte)((value>>8)&0xFF);
        src[0] = (byte)(value&0xFF);
        return src;
    }
    public static short bytesToShort(byte[] value){
        short s = 0;
        short s0 = (short)(value[0]&0xFF);
        short s1 = (short)(value[1]&0xFF);
        s1 <<= 8;
        s = (short)(s0|s1);
        return s;
    }
    public boolean isConnect(String rp_ip){
        boolean connect = false;
        Runtime runtime = Runtime.getRuntime();
        Process process;
        try{
            process = runtime.exec("ping " + rp_ip);
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while((line = br.readLine())!=null){
                sb.append(line);
            }
            is.close();
            isr.close();
            br.close();

            if(null != sb&&!sb.toString().equals("")){
                if(sb.toString().indexOf("TTL")>0){
                    connect = true;

                }else{
                    connect = false;
                    System.out.println("集中器网络连接故障:"+rp_ip);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return connect;
    }
}
