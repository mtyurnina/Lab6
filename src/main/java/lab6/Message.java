package lab6;

public class Message {
    private String[] serverList;

    public Message(String[] serverList) {
        this.serverList = serverList;
    }

    public void setServerList(String[] serverList) {
        this.serverList = serverList;
    }

    public String[] getServerList() {
        return serverList;
    }
}