import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        String etherType = data[12] + data[13];
        printEther(packetSize, destination.toString(), source.toString(), etherType);
    }
}
// 45 00 00 3d 54 33 40 00 40 11 9e ec 81 15 42 55 81 1503 11