public class Packet{
    private byte header[];
    private byte payload[];
    private byte trailer[];

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

    public void setPayload(byte[] payload){
        this.payload = payload;
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