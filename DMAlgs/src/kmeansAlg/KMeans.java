package kmeansAlg;
import java.util.*;
public class KMeans {
	public static void main(String []args)throws Exception
	{
		//the dataset
		int [][] array={{2,10,0},{2,5,0},{8,4,0},{5,8,0},{7,5,0},{6,4,0},{1,2,0},{4,9,0},{7,3,0},{1,3,0},{3,9,0}};
		//the initial center array
		double [][] init={{2,10},{5,8},{1,2}};
		//implement the kmeans algorithm
		Kmeans(array,init);
		System.out.println("Enter any key to exit.");
		int b=System.in.read();
	}
/**
 * 主函数
 * @param arr ---输入数据
 * @param init---初始中心点
 */
	public static void Kmeans(int[][] arr, double[][] init) {
		double[][] temp = { { 0, 0 }, { 0, 0 }, { 0, 0 } };
		while (!isChanged(init, temp)) {
			System.arraycopy(init, 0, temp, 0, init.length);
			for (int i = 0; i < arr.length; i++) {
				double min = Double.MAX_VALUE;
				int tag = 0;
				for (int j = 0; j < init.length; j++) {
					double dis = distance(arr[i], init[j]);
					if (dis < min) {
						min = dis;
						tag = j;
					}
				}
				arr[i][2] = tag;
			}
			for (int i = 0; i < init.length; i++) {
				init[i] = getCenter(arr, i);
			}

		}
		display(arr);
	}
/**
 * 判断停止条件
 * @param arr---当前聚类过的输入数组
 * @param brr---新的中心点
 * @return 只有所有中心点不变的时候  返回TRUE
 */
public static boolean isChanged(double [][]arr,double [][]brr)
{
	if(arr.length!=brr.length)
	{
		System.out.println("Same demension required.");
		System.exit(0);
	}
	boolean tag=true;
	for(int i=0;i<arr.length;i++)
	{
		if(Math.abs(arr[i][0]-brr[i][0])<Double.MIN_VALUE &&(Math.abs(arr[i][1]-brr[i][1])<Double.MIN_VALUE))
		{
			tag&=true;
		}else{
			tag&=false;
		}
		
	}
	return(tag);
}
/**
 * 显示类标签及其成员
 * @param result
 */
public static void display(int [][] result)
{
	HashMap<Integer ,String> map=new HashMap<Integer,String>();
	for(int i=0;i<result.length;i++)
	{
		Integer t=new Integer(result[i][2]);
		String temp="("+Integer.toString(result[i][0])+","+Integer.toString(result[i][1])+")";
		if(!map.containsKey(t))
		{
			
			map.put(t,temp);
		}else{
		map.put(t, map.get(t).concat(temp));
			
		}
	}
	
for(Integer t:map.keySet())
{
	System.out.println("cluster"+(t+1)+": {"+map.get(t)+"}");
}
}
/**
 * 计算中心点
 * @param arr 经过分类的输入数据 第三列为类标签
 * @param tag 类标签【0,1...k-1】 k classes
 * @return 新的中心点
 */
public static double [] getCenter(int [][] arr,int tag)
{
	double [] a={0,0};
	double x=0.0;
	double y=0.0;
	int count=0;
	for(int i=0;i<arr.length;i++)
	{
		if(arr[i][2]==tag)
		{
			x+=arr[i][0];
			y+=arr[i][1];
			count++;
		}
	}
	a[0]=x/count;
	a[1]=y/count;
	return(a);
}
/**
 * 计算两点间的距离
 * @param arr ---x点
 * @param brr---y点
 * @return  distance
 */
public static double distance(int []arr,double []brr)
{
	return(Math.sqrt((arr[0]-brr[0])*(arr[0]-brr[0])+(arr[1]-brr[1])*(arr[1]-brr[1])));
}
}
