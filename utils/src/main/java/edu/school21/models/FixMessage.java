package edu.school21.models;

import edu.school21.exceptions.InvalidFixMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixMessage {

    private String fixMessage;

    private String instrument;
    private String type;
    private int quantity;
    private float price;
    private String targetId;
    private String senderId;
    private Checksum checksum;
    private int status;
    private String orderId;

    private static String HEADER = "8=FIX.4.4|";
    private static String SEPARATOR = "";
    private static String BODY_LENGTH;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");

    // <8=FIX.4.0 | 9=[body length] | 35=D | 49=[Broker ID] | 56=[Market ID] | 52=[TIME STAMP]|>           Header
    // <11=[Client ORDER ID] |39=[status] |55=[Instrument] | 54=1(buy) | 38=[Quantity] | 44=[Price] |>     Body
    // <10=[checksum]>                                                                                     Trailer
    // 39=[Status] (1 - New, 8 - Rejected, 2 - Executed (filled)

    public FixMessage(String input) {
        //0 - instrument, 1 - type, 2 - quantity, 3 - price, 4 - targetId, 5 - senderId
        String[] parts = input.split("#");
        this.instrument = parts[0];
        this.type = parts[1];
        this.quantity = Integer.parseInt(parts[2]);
        this.price = Float.parseFloat(parts[3]);
        this.targetId = parts[4];
        this.senderId = parts[5];
        this.status = 1;
        this.orderId = UUID.randomUUID().toString().replace("-","").substring(0,8);
    }

    public FixMessage(String fixMessage, boolean isValid) {
        String[] parts = fixMessage.split("\\|");
        this.fixMessage = fixMessage;
        this.orderId = parts[6].substring(parts[6].indexOf("=") + 1);
        this.status = Integer.parseInt(parts[7].substring(parts[7].indexOf("=") + 1));
        this.instrument = parts[8].substring(parts[8].indexOf("=") + 1);
        this.type = parts[9].substring(parts[9].indexOf("=") + 1);
        this.quantity = Integer.parseInt(parts[10].substring(parts[10].indexOf("=") + 1));
        this.price = Float.parseFloat(parts[11].substring(parts[11].indexOf("=") + 1));
        this.targetId = parts[4].substring(parts[4].indexOf("=") + 1);
        this.senderId = parts[3].substring(parts[3].indexOf("=") + 1);
    }

    public void parseFixMessage() throws NullPointerException {
        StringBuilder body = new StringBuilder();

        body.append("11=" + orderId + "|");
        body.append("39=" + status + "|");
        body.append("55=" + instrument + "|");

        if (type.equals("buy") || type.equals("sell") || type.equals("1") || type.equals("2")) {
            body.append("54=" + (type.equals("buy") ? "1" : "2") + "|");
        } else {
            throw new InvalidFixMessage("Wrong type of transaction");
        }

        body.append("38=" + quantity + "|");
        body.append("44=" + price + "|");

        BODY_LENGTH = "9=" + body.length() + "|";
        LocalDateTime now = LocalDateTime.now();
        fixMessage =
                HEADER + BODY_LENGTH + "35=D|" + "49=" + senderId + "|" + "56=" + targetId + "|" + "52=" + dtf.format(now) + "|"
                + body;
        this.checksum = new CRC32();
        this.checksum.update(fixMessage.getBytes());
        long checksumMod = checksum.getValue() % 256;

        fixMessage += "10=" + String.format("%03d", checksumMod);
    }

    public void verifyChecksum() {
        String[] fixMessageWithoutChecksum = fixMessage.split("10=");
        Checksum checksum = new CRC32();
        checksum.update(fixMessageWithoutChecksum[0].getBytes());
        long checksumMod = checksum.getValue() % 256;
        if (checksumMod != Integer.parseInt(fixMessageWithoutChecksum[1])) {
            throw new InvalidFixMessage("checksum do not match");
        }
    }
}
