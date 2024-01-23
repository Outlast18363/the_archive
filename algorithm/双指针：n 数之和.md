# 4 数之和 & N 数之和

<img width="1150" alt="image" src="https://github.com/Outlast18363/the_archive/assets/108510344/1d9becba-c4ee-4a2a-bdf3-ba6dc9d85a1b">

<img width="362" alt="image" src="https://github.com/Outlast18363/the_archive/assets/108510344/ebad06ce-096b-4e91-8dca-85df45c99a30">

算法 $O(n^3)$

## 拆分问题
对于4 sum，将其拆分成找一个数 i 和一个 3 sum，使得那个 $i + 3 sum == target_4$ ($target_4$ 为 4 个数的目标和)

对于3 sum，将其拆分成找一个数 j 和一个 2 sum，使得那个 $j + 2 sum == target_3$

对于2 sum，对数组排序，然后l，r 两个指针往中间找 2 sum，使得 $l + r = target_2$

## 代码
