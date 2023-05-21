package DataObject;

import java.util.Comparator;

public class Document implements Comparable<Document>
{
    private int id;
    private String content;
    private double rsv;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getRSV()
    {
        return this.rsv;
    }
    
    public void setRSV(double rsv)
    {
        this.rsv = rsv;
    }

    public Document() 
    {
        id = 0;
        content = "";
        rsv = 0;
    }

    public Document(int id, String content) 
    {
        this.id = id;
        this.content = content;
        this.rsv = 0;
    }

    public Document(int id, String content, double rsv) 
    {
        this.id = id;
        this.content = content;
        this.rsv = rsv;
    }

    @Override
    public int compareTo(Document o) 
    {
        return Double.compare(this.rsv, o.getRSV());
    }
}
