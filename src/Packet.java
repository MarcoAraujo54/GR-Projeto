public class Packet{
    private byte header[];
    private byte payload[];
    private byte trailer[];

    public Packet(byte[] header, byte[] payload, byte[] trailer) {
        this.header = header;
        this.payload = payload;
        this.trailer = trailer;
    }
    public Packet(Packet p) {
        this.header = p.header;
        this.payload = p.payload;
        this.trailer = p.trailer;
    }
    public Packet() {
        this.header= new byte[100];
        this.payload= new byte[100];
        this.trailer= new byte[100];
    }


    public byte[] getHeader() {
        return header;
    }
    public byte[] getPayload() {
        return payload;
    }
    public byte[] getTrailer() {
        return trailer;
    }
    public void setHeader(byte[] header){
        this.header = header;
    }

    public void setPayload(int S, int NS, String Q, int P, int Y, int NL, String L, int NR, String R) {
        // Construct the payload from the provided fields
        String payloadStr = S + "\0" + NS + "\0" + Q + "\0" + P + "\0" + Y + "\0" + NL + "\0" + L + "\0" + NR + "\0" + R + "\0";
        this.payload = payloadStr.getBytes();
    }

    public void setTrailer(byte[] trailer){
        this.trailer = trailer;
    }

    public byte[] getPacket(){
        int size = this.header.length + this.payload.length + this.trailer.length;
        byte[] packet = new byte[size];

        System.arraycopy(this.header, 0, packet, 0, this.header.length);
        System.arraycopy(this.payload, 0, packet, this.header.length, this.payload.length);
        System.arraycopy(this.trailer, 0, packet, this.header.length + this.payload.length, this.trailer.length);

        return packet;
    }
}