package tcp.client;

public class Message {
    String text;
    String name;
    String textAdmin;
    Message(String text,String name,String textAdmin){
        this.text=text;
        this.name=name;
        this.textAdmin=textAdmin;
    }
    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getTextAdmin() {
        return textAdmin;
    }

    @Override
    public String toString() {
        return "{text:"+this.text+",name:"+this.name+"}";
    }

}
