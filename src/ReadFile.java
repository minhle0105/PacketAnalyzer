import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ReadFile {

    private static void printEther(int packetSize, String destination, String source, String etherType) {
        System.out.println("ETHER: ------Ether Header------");
        System.out.println("ETHER:");
        System.out.println("ETHER: \t Packet size = \t" + packetSize + " bytes");
        System.out.println("ETHER: \t Destination = \t" + destination + ",");
        System.out.println("ETHER: \t Source \t = \t" + source + ",");
        if (etherType.equals("0800")) {
            System.out.println("ETHER: \t Ethertype \t = \t" + etherType + " (IP)");
        }
        else {
            System.out.println("ETHER: \t Ethertype = UNKNOWN");
        }
        System.out.println("ETHER:");
    }

    private static void printIpHeader(int version, int headerLength, String typeOfService,
                                      int totalLength, String identification, String flags,
                                      int fragmentOffset, int timeToLive, int protocol,
                                      String headerCheckSum, String source, String destination) {
        System.out.println("IP: \t----- IP Header -----");
        System.out.println("IP:");
        System.out.println("IP: \tVersion = " + version);

        System.out.println("IP: \tHeader length = " + headerLength + " bytes");
        System.out.println("IP: \tType of service = " + typeOfService);
        System.out.println("IP: \tTotal length = " + totalLength + " bytes");
        System.out.println("IP: \tIdentification = " + identification);
        System.out.println("IP: \tFlags = " + flags);
        System.out.println("IP: \tFragment offset = " + fragmentOffset + " bytes");
        System.out.println("IP: \tTime to live = " + timeToLive + " seconds/hops");
        if (protocol == 1) {
            System.out.println("IP: \tProtocol = " + protocol + " (ICMP)");
        }
        else if (protocol == 6) {
            System.out.println("IP: \tProtocol = " + protocol + " (TCP)");
        }
        else if (protocol == 17) {
            System.out.println("IP: \tProtocol = " + protocol + " (UDP)");
        }
        System.out.println("IP : \tHeader checksum = " + headerCheckSum);
        System.out.println("IP : \tSource Address = " + source);
        System.out.println("IP : \tDestination Address = " + destination);
        System.out.println("IP : \t");
    }

    private static void printUdpHeader(String source, String destination, int length, String checksum, String[] UDPData) {
        System.out.println("UDP: \t----UDP Header----");
        System.out.println("UDP: \tSource port = " + source);
        System.out.println("UDP: \tDestination port = " + destination);
        System.out.println("UDP: \tLength = " + length);
        System.out.println("UDP: \tChecksum = " + checksum);
        System.out.println("UDP: \t");
        System.out.println("UDP: \tData: (first 64 bytes)");
        int n = UDPData.length;
        int numberOfRows = n / 16;
        int j = 0;
        for (int i = 0; i <= numberOfRows; i++) {
            System.out.print("UDP: \t");
            int threshold = j + 16;
            while (j < threshold) {
                try {
                    if (j % 2 == 0) {
                        System.out.print(UDPData[j]);
                    }
                    else {
                        System.out.print(UDPData[j] + " ");
                    }
                    j++;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }

            }
            System.out.println();
        }
    }

    private static void printTcpHeader(String source, String destination, String sequenceNumber,
                                       String acknowledgementNumber, int dataOffset, String flags,
                                       String window, String checksum, int urgentPointer, String[] TCPData) {
        System.out.println("TCP: \t----TCP Header----");
        System.out.println("TCP: \tSource port = " + source);
        System.out.println("TCP: \tDestination port = " + destination);
        System.out.println("TCP: \tSequence number = " + sequenceNumber);
        System.out.println("TCP: \tAcknowledgement number = " + acknowledgementNumber);
        System.out.println("TCP: \tData offset = " + dataOffset + " bytes");
        System.out.println("TCP: \tFlags = " + flags);
        System.out.println("TCP: \tWindow = " + window);
        System.out.println("TCP: \tChecksum = " + checksum);
        System.out.println("TCP: \tUrgent pointer = " + urgentPointer);
        System.out.println("TCP: \tNo options");
        System.out.println("TCP: \t");
        System.out.println("TCP: \tData: (first 64 bytes)");
        int n = TCPData.length;
        int numberOfRows = n / 16;
        int j = 0;
        for (int i = 0; i <= numberOfRows; i++) {
            System.out.print("TCP: \t");
            int threshold = j + 16;
            while (j < threshold) {
                try {
                    if (j % 2 == 0) {
                        System.out.print(TCPData[j]);
                    }
                    else {
                        System.out.print(TCPData[j] + " ");
                    }
                    j++;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }

            }
            System.out.println();
        }
    }

    private static void printICMPHeader(int type, int code, String checksum) {

    }

    public static void main(String[] args) throws IOException {
        String filePath = args[0];
        Path path = Paths.get(filePath);
        byte[] fileContents =  Files.readAllBytes(path);
        String[] data = new String[fileContents.length];
        int count = 0;
        for (byte b : fileContents) {
            String s = Integer.toHexString((b & 0xff)+256).substring(1);
            data[count++] = s;
        }
        int packetSize = data.length;
        StringBuilder destination = new StringBuilder();
        StringBuilder source = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            if (i != 5) {
                destination.append(data[i]).append(":");
            }
            else {
                destination.append(data[i]);
            }
        }
        for (int i = 6; i < 12; i++) {
            if (i != 11) {
                source.append(data[i]).append(":");
            }
            else {
                source.append(data[i]);
            }
        }

        int version = Integer.parseInt(String.valueOf(data[14].charAt(0)));
        int headerLength = Integer.parseInt(String.valueOf(data[14].charAt(1))) * 4;
        int totalLength = Integer.parseInt(data[16] + data[17], 16);
        String typeOfService = "0x" + data[15];
        String etherType = data[12] + data[13];
        String identification = Integer.parseInt(data[18] + data[19], 16) + "";
        String flags = "0x" + Integer.parseInt(data[20], 16);
        int fragmentOffset = Integer.parseInt(data[21], 16);
        int timeToLive = Integer.parseInt(data[22], 16);
        int protocol = Integer.parseInt(data[23], 16);
        String headerChecksum = "0x" + data[24] + data[25];
        StringBuilder src = new StringBuilder();
        StringBuilder dst = new StringBuilder();
        for (int i = 26; i < 30; i++) {
            if (i != 29) {
                src.append(Integer.parseInt(data[i], 16)).append(".");
            }
            else {
                src.append(Integer.parseInt(data[i], 16));
            }
        }
        for (int i = 30; i < 34; i++) {
            if (i != 33) {
                dst.append(Integer.parseInt(data[i], 16)).append(".");
            }
            else {
                dst.append(Integer.parseInt(data[i], 16));
            }
        }

        printEther(packetSize, destination.toString(), source.toString(), etherType);
        printIpHeader(version, headerLength, typeOfService, totalLength, identification, flags, fragmentOffset, timeToLive, protocol, headerChecksum, src.toString(), dst.toString());
        if (protocol == 17) {
            String UDPSource = Integer.parseInt(data[34] + data[35], 16) + "";
            String UDPDest = Integer.parseInt(data[36] + data[37], 16) + "";
            int length = Integer.parseInt(data[38] + data[39], 16);
            String UDPChecksum = "0x" + data[40] + data[41];
            String[] UDPData;
            if (34 + 64 > data.length) {
                UDPData = Arrays.copyOfRange(data, 34, data.length);
            }
            else {
                UDPData = Arrays.copyOfRange(data, 34, 34 + 64);
            }
            printUdpHeader(UDPSource, UDPDest, length, UDPChecksum, UDPData);

        }
        else if (protocol == 6) {
            String TCPSource = Integer.parseInt(data[34] + data[35], 16) + "";
            String TCPDest = Integer.parseInt(data[36] + data[37], 16) + "";
            StringBuilder sequenceNumberS = new StringBuilder();
            for (int i = 38; i < 42; i++) {
                sequenceNumberS.append(data[i]);
            }
            String sequenceNumber = Integer.parseInt(sequenceNumberS.toString(), 16) + "";
            StringBuilder acknowledgementNumberS = new StringBuilder();
            for (int i = 42; i < 46; i++) {
                acknowledgementNumberS.append(data[i]);
            }
            String acknowledgementNumber = acknowledgementNumberS.toString();
            int dataOffset = Integer.parseInt(String.valueOf(data[46].charAt(0)));
            String tcpFlag = data[46].charAt(1) + data[47];
            String window = Integer.parseInt(data[48] + data[49], 16) + "";
            String checkSum = "0x" + data[50] + data[51];
            int urgentPointer = Integer.parseInt(data[52],16);
            String[] tcpData;
            if (34 + 64 > data.length) {
                tcpData = Arrays.copyOfRange(data, 34, data.length);
            }
            else {
                tcpData = Arrays.copyOfRange(data, 34, 34 + 64);
            }
            printTcpHeader(TCPSource, TCPDest, sequenceNumber, acknowledgementNumber, dataOffset, tcpFlag, window, checkSum, urgentPointer, tcpData);
        }

    }
}
// 45 00 00 3d 54 33 40 00 40 11 9e ec 81 15 42 55 81 1503 11