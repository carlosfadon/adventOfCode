B = A/ 2^combo
print(B%8)


Program:
2,4, ---- B = A MOD 8  ------------> B goes from 0 to 7
1,2, ---- B = B XOR 2 -------------> B XOR 2
7,5, ---- C = A/2^B
4,5, ---- B = B XOR C
0,3, ----- A = A/8    ------------->
1,7,  ---- B = B XOR 7
5,5,  ---- B MOD 8 ====> PRINT
3,0   ---- Vuelve al inicio



En la ultima iteracion A == [22160 - 22167], B == 1
B = A XOR 2    (B is [0-7])                  [A=22160, B=2 | A=22161, B=3 | A=22162, B=0 | A=22163, B=1 | A=22164, B=6 | A=22165, B=7 | A=22166, B=4 | A=22167, B=5]
B = B XOR A/2^B                              [B=4582      | B=2787      | B=22162       | B=10200     |   B=335        | B=170        | B=1321      | B=645]
A = A/8  ====> 346 ---> should be 346
B = B XOR 7   ----> should be finished in 101                          [2770] A == 2770
PRINT B MOD 8 ----> should be 5

En la ultima iteracion A == [2768 - 2775], B == 7
B = A XOR 2    (B is [0-7])                  [A=2768, B=2 | A=2769, B=3 | A=2770, B=0 | A=2771, B=1 | A=2772, B=6 | A=2773, B=7 | A=2774, B=4 | A=2775, B=5]
B = B XOR A/2^B                              [B=688      | B=337      | B=2770       | B=1395     |   B=45        | B=16        | B=168      | B=91]
A = A/8  ====> 346 ---> should be 346
B = B XOR 7   ----> should be finished in 101                          [2770] A == 2770
PRINT B MOD 8 ----> should be 5


                En la ultima iteracion A == [344 - 351], B == 5
                B = A XOR 2    (B is [0-7])                  [A=344, B=2 | A=345, B=3 | A=346, B=0 | A=347, B=1 | A=348, B=6 | A=349, B=7 | A=350, B=4 | A=351, B=5]
                B = B XOR A/2^B                              [B=84        | B=40      | B=346       | B=172     |   B=3      | B=5         | B=17        | B=15]
                A = A/8  ====> 43 ---> should be 43
                B = B XOR 7   ----> should be finished in 101                          [] A == 346
                PRINT B MOD 8 ----> should be 5


                En la ultima iteracion A == [376 - 383], B == 5
                B = A XOR 2    (B is [0-7])                  [A=376, B=2 | A=377, B=3 | A=378, B=0 | A=379, B=1 | A=380, B=6 | A=381, B=7 | A=382, B=4 | A=383, B=5]
                B = B XOR A/2^B                              [B=92        | B=44      | B=378       | B=188     |   B=3      | B=5         | B=31        | B=15]
                A = A/8  ====> 47 ---> should be 47
                B = B XOR 7   ----> should be finished in 101            NO RESULTS
                PRINT B MOD 8 ----> should be 5


En la ultima iteracion A==[40-47], B == 5
B = A XOR 2    (B is [0-7])                  [A=40, B=2 | A=41, B=3 | A=42, B=0 | A=43, B=1 | A=44, B=6 | A=45, B=7 | A=46, B=4 | A=47, B=5]
B = B XOR A/2^B                              [B=8      | B=6        | B=42      | B=20      | B=6      | B=7        | B=6        | B=4]
A = A/8  ====> 5 ---> should be 5
B = B XOR 7   ----> should be finished in 011                                     [B=3]-- A=43                                    [B=3] --> A == 47
PRINT B MOD 8 ----> should be 3


En la ultima iteracion A==[0,7], B == 3
B = A XOR 2    (B is [0-7])                  [A=0, B=2 | A=1, B=3 | A=2, B=0 | A=3, B=1 | A=4, B=6 | A=5, B=7 | A=6, B=4 | A=7, B=5]
B = B XOR A/2^B                              [B=2      | B=3      | B=2      | B=0      | B=6      | B=7      | B=4      | B=5]
A = A/8  ====> 0 ---> should be 0
B = B XOR 7   ----> should be finished in 000                                                       [B=0] A=5
PRINT B MOD 8 ----> should be 0



B == X




ins7:                                     A=0;0,B ends in 000,C?
ins6: B % 8 == 0 --------> 0              A=0;B ends in 000;C?
ins5: B XOR 7  (B should be 8k+7)         A=0;B=ends in 111;C?
ins4: A / 8                               A=[0,7],B ends in 111;C?
ins3: B = B XOR C
ins2: C = A / 2^B
ins1: B = B XOR 101
==========================

print statement: A=6, B = 3, C= 6
B XOR 7:         A=6, B = 4, C= 6
A / 8 :          A=48, B=4,  C= 6
B = B XOR C:     A=48, B=5,  C= 6
C = A / 2^B:     A=48, B=5,  C= 7
B = B XOR 101:   A=48, B=102, C=8

B =====> 3
B



B = B XOR C
C = A / 2^B

Equations:
C = A / 2^B
4 = B XOR C
when A >= 8, A < 64









bbb = X % 8
bbb xor 010
1388000000
