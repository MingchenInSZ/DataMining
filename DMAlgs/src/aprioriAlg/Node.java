package aprioriAlg;

public class Node {
    public String name;
    public int count;
    public Node()
    {
    	
    }
    public Node(String name)
    {
    	this.name=name;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
    public void addCount()
    {
    	this.count++;
    }
}
