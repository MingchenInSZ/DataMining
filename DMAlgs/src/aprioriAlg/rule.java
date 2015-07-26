package aprioriAlg;

public class rule {
	String head;
	String tail;
	double conf;
	public rule()
	{		
	}
	public rule(String head,String tail)
	{
		this.head=head;
		this.tail=tail;
	}
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	public String getTail() {
		return tail;
	}
	public void setTail(String tail) {
		this.tail = tail;
	}
    public void setConf(double conf)
    {
    	this.conf=conf;
    }
    public double getConf()
    {
    	return(this.conf);
    }
}
