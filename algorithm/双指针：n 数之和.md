# 4 数之和 & N 数之和

<img width="1150" alt="image" src="https://github.com/Outlast18363/the_archive/assets/108510344/1d9becba-c4ee-4a2a-bdf3-ba6dc9d85a1b">

<img width="362" alt="image" src="https://github.com/Outlast18363/the_archive/assets/108510344/ebad06ce-096b-4e91-8dca-85df45c99a30">

算法 $O(n^3)$

## 拆分问题
对于4 sum，将其拆分成找一个数 i 和一个 3 sum，使得那个 $i + 3 sum == target_4$ ($target_4$ 为 4 个数的目标和)

对于3 sum，将其拆分成找一个数 j 和一个 2 sum，使得那个 $j + 2 sum == target_3$

对于2 sum，对数组排序，然后l，r 两个指针往中间找 2 sum，使得 $l + r = target_2$


## 程序优化

在3 sum时，我们发现如果第三个k数加上最大的 l，r后还 小于 target 的话，那就说明可以直接换成下个更大的第3个数了。
$max_2 = a[n] + a[n-1]$ (左右指针都在最右边最大的地方)，如果： $a[i] + max_2 < target$ ， continue。

在3 sum时，我们发现如果第三个k数加上最大的 l，r后还 大于 target 的话，因为后面的新的a[i]只会越来越大，因此如果： $a[i] + min_2 > target$ ， break。
$min_2 = a[i+1] + a[i+2]$ (2sum 的两个指针都在最左面最小的位置)

---

在4 sum时，我们发现如果第三个数加上最大的 l，r后还 小于 target 的话，那就说明可以直接换成下个更大的第4个数了。
$max_3 = a[n-2] + max_2$ (左右指针都在最右边最大的地方)，如果： $a[j] + max_3 < target$ ，continue。

在4 sum时，我们发现如果第 4 个数a[j]加上最小的第3个数加上最小的 l，r后还大于 target 的话，按照同样推理，后面的a[j]肯定都不行了
$max_3 = a[j+1] + max_2$ (左右指针都在最右边最大的地方)，因此如果： $a[j] + max_3 > target$ ，break。

---

### 推广优化至 n 数之和
定义第 n sum的数字为a[x]

当 $a[x] + max_{n-1} < target$ 说明a[x] 不够大，直接换下一个a[x]，直接 continue。

当 $a[x] + min_{n-1} > target$ 说明a[x] 太大，但因为后面的a[x]只会更大，因此直接break。

## 四数之和 代码

