# 4 数之和 & N 数之和

<img width="1150" alt="image" src="https://github.com/Outlast18363/the_archive/assets/108510344/1d9becba-c4ee-4a2a-bdf3-ba6dc9d85a1b">

<img width="362" alt="image" src="https://github.com/Outlast18363/the_archive/assets/108510344/ebad06ce-096b-4e91-8dca-85df45c99a30">

算法 $O(n^3)$

## 拆分问题
对于4 sum，将其拆分成找一个数 i 和一个 3 sum，使得那个 $i + 3 sum == target_4$ ($target_4$ 为 4 个数的目标和)

对于3 sum，将其拆分成找一个数 j 和一个 2 sum，使得那个 $j + 2 sum == target_3$ （ $target_3$ 为 $target_4 - a[i]$ 的值，也就是给定 sum4 指针情况下需要达到的3数sum，以达成 $target_4$ ）。

对于2 sum，对数组排序，然后l，r 两个指针往中间找 2 sum，使得 $l + r = target_2$


## 程序优化

### 优化 1

在3 sum时，我们发现如果第三个k数加上最大的 l，r后还 小于 target 的话，那就说明可以直接换成下个更大的第3个数了。
$max_2 = a[n] + a[n-1]$ (左右指针都在最右边最大的地方)，如果： $a[i] + max_2 < target$ ， continue。

在3 sum时，我们发现如果第三个k数加上最小的 l，r后还 大于 target 的话，因为后面的新的a[i]只会越来越大，因此如果： $a[i] + min_2 > target$ ， break。
$min_2 = a[i+1] + a[i+2]$ (2sum 的两个指针都在最左面最小的位置)

---

在4 sum时，我们发现如果第三个数加上最大的 l，r后还 小于 target 的话，那就说明可以直接换成下个更大的第4个数了。
$max_3 = a[n-2] + max_2$ (左右指针都在最右边最大的地方)，如果： $a[j] + max_3 < target$ ，continue。

在4 sum时，我们发现如果第 4 个数a[j]加上最小的第3个数加上最小的 l，r后还大于 target 的话，按照同样推理，后面的a[j]肯定都不行了
$max_3 = a[j+1] + max_2$ (左右指针都在最右边最大的地方)，因此如果： $a[j] + max_3 > target$ ，break。

---
### 优化 2
当一个 sum 指针的数值已经试过并成功时，没有必要以它为准再试一遍，因此直接跳到下一次 value 不一样的时候。
比如：sum4 指针a[j] = 2，试过成功找到相对应的 sum3 以达到 target 后，则不需要再试一次sum4 的指针为 2 这个值的情况。（因为我们已经试过一次并知道它可行了）

## 四数之和 代码

```java
import java.io.*;
import java.util.*;
public class Main {
    static Kattio in;
    public static void main(String[] args) throws IOException {
        in = new Kattio();
        //n: length of array, t: target sum
        int n = in.nextInt(), t = in.nextInt();
        int a[] = new int[n];
        for(int i = 0; i < n; i++) a[i] = in.nextInt();
        fourSum(a, t);
        in.close();
    }
    static void fourSum(int[] a, int target) {
        int n = a.length, t = target;
        Arrays.sort(a);

        //4sum 部分
        for(int i = 0; i < n-3; i++){
            int max3 = a[n-1] + a[n-2] + a[n-3];
            int min3 = a[i+1] + a[i+2] + a[i+3];

            //优化1
            if(a[i] + max3 < t) continue;
            if(a[i] + min3 > t) break;

            int n1 = t - a[i];

            //3sum 部分
            for(int j = i+1; j < n-2; j++){
                int max2 = a[n-1] + a[n-2];
                int min2 = a[j+1] + a[j+2];

                //优化1
                if(a[j] + max2 < n1) continue;
                if(a[j] + min2 > n1) break;

                int n2 = n1 - a[j];

                //2sum 部分
                int l = j + 1; int r = n-1;
                while(r >= 0 && l < n && l < r){
                    if(n2 - (a[l] + a[r]) == 0) { //找到一组解
                        in.println(a[i] + " " + a[j] + " " + a[l] + " " +a[r]);
                        while(i < n-1 && a[i] == a[i+1]) i++; //优化 2：在成功的情况下移到数值不同的地方
                        while(j < n-1 && a[j] == a[j+1]) j++;
                        break;
                    }
                    else if(n2 - (a[l] + a[r]) > 0) l++;
                    else r--;
                }
            }
        }
    }
}

class Kattio extends PrintWriter {
    private BufferedReader r;
    private StringTokenizer st;
    // standard input
    public Kattio() { this(System.in,System.out); }
    public Kattio(InputStream i, OutputStream o) {
        super(o);
        r = new BufferedReader(new InputStreamReader(i));
    }
    // USACO-style file input
    public Kattio(String problemName) throws IOException {
        super(problemName+".out");
        r = new BufferedReader(new FileReader(problemName+".in"));
    }
    // returns null if no more input
    public String next() {
        try {
            while (st == null || !st.hasMoreTokens())
                st = new StringTokenizer(r.readLine());
            return st.nextToken();
        } catch (Exception e) {}
        return null;
    }
    public String nextLine() {
        try {return r.readLine();} catch (Exception e) {}
        return null;
    }
    public int nextInt() { return Integer.parseInt(next()); }
    public double nextDouble() { return Double.parseDouble(next()); }
    public long nextLong() { return Long.parseLong(next()); }
}
```

### 附加：推广优化至 n 数之和
定义第 n sum的数字为a[x]

当 $a[x] + max_{n-1} < target$ 说明a[x] 不够大，直接换下一个a[x]，直接 continue。

当 $a[x] + min_{n-1} > target$ 说明a[x] 太大，但因为后面的a[x]只会更大，因此直接break。

