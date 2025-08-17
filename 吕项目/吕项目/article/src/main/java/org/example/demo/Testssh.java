//package org.example.demo;
//
//import com.jcraft.jsch.ChannelExec;
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.Session;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//
//
//public class Testssh {
//    @Test
//    public void Test() throws JSchException, IOException {
//        List<String>  a=execCmd("192.168.86.128",22,"root","362514","hadoop jar wordcount-1.0-SNAPSHOT.jar /test1 /result/file111");
//    }
//    public List<String> execCmd(String host, int port, String userName, String password, String cmd) throws IOException, JSchException {
////        创建jsch对象
//        JSch jsch = new JSch();
//        ChannelExec channelExec=null;
//        Session session=null;
//        InputStream inputStream=null;
////        输出结果到字符串数组
//        List<String> resultLines = new ArrayList<>();
////        创建session会话
//        try {
//            session = jsch.getSession(userName, host, port);
////            设置密码
//            session.setPassword(password);
////            创建一个session配置类
//            Properties sshConfig = new Properties();
////            跳过公钥检测
//            sshConfig.put("StrictHostKeyChecking", "no");
//            session.setConfig("LogLevel", "VERBOSE");
//            session.setConfig(sshConfig);
//            System.out.println("ok");
////            建立连接
//            session.connect();
//
////            session建立之后，我们就可以执行shell命令，或者上传下载文件了,下面我来执行shell命令
//            channelExec = (ChannelExec) session.openChannel("exec");
////            将shell传入command
//            channelExec.setCommand(cmd);
////            开始执行
//            channelExec.connect();
////            获取执行结果的输入流
//            inputStream = channelExec.getInputStream();
//            String result=null;
//            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
//            while ((result = in.readLine()) != null) {
//                resultLines.add(result);
//                System.out.println("命令返回信息");
//            }
//        }catch (Exception e){
//            ArrayList<String> errorMsg = new ArrayList<>();
//            errorMsg.add(e.getMessage());
//            return errorMsg;
//        }finally {
////            释放资源
//            if (channelExec != null) {
//                try {
//                    channelExec.disconnect();
//                } catch (Exception e) {
//                }
//            }
//            if (inputStream!=null){
//                try {
//                    inputStream.close();
//                } catch (Exception e) {
//                }
//            }
//        }
//        return resultLines;
//    }
//
//
//
//}
