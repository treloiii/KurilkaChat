package tcp.client;

public class Message {
    String text;
    String name;

    Message(String text,String name){
        this.text=text;
        this.name=name;
    }
    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "{text:"+this.text+",name:"+this.name+"}";
    }

}
