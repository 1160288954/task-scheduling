#!/usr/bin/env python
# -*- encoding: utf-8 -*-
import matplotlib.pyplot as plt

GA = {0: 3680.0, 1: 539.0, 2: 750.0, 3: 1892.0, 4: 1608.0, 5: 1466.0, 6: 1424.0, 7: 1946.0, 8: 3091.0, 9: 6244.0}

ACOInt = {0: 3680.0, 1: 539.0, 2: 522.0, 3: 1422.0, 4: 1140.0, 5: 1167.0, 6: 1169.0, 7: 1469.0, 8: 2342.0, 9: 4554.0}

IWC = {0: 3680.0, 1: 539.0, 2: 743.0, 3: 1814.0, 4: 1371.0, 5: 1318.0, 6: 1351.0, 7: 1648.0, 8: 3091.0, 9: 5574.0}

PSOInt = {0: 3680.0, 1: 539.0, 2: 490.0, 3: 1327.0, 4: 866.0, 5: 1187.0, 6: 954.0, 7: 1234.0, 8: 1923.0, 9: 4106.0}

WOAInt = {0: 3680.0, 1: 539.0, 2: 604.0, 3: 1304.0, 4: 1437.0, 5: 974.0, 6: 1129.0, 7: 1415.0, 8: 2607.0, 9: 3814.0}

WOABLR = {0: 3680.0, 1: 539.0, 2: 452.0, 3: 1233.0, 4: 1092.0, 5: 897.0, 6: 937.0, 7: 1269.0, 8: 1906.0, 9: 3605.0}

ga = list(GA.values())
acoInt = list(ACOInt.values())
iwc = list(IWC.values())
psoInt = list(PSOInt.values())
woaInt = list(WOAInt.values())
woablr = list(WOABLR.values())
x = [100, 200, 300, 400, 500, 600, 700, 800, 900, 1000]

plt.plot(x, ga, linestyle="--", marker=",", linewidth='1', label="GA")
plt.plot(x, acoInt, linestyle="--", marker="+", linewidth='1', label="ACO")
plt.plot(x, iwc, linestyle="-.", marker="x", linewidth='1', label="IWC")
plt.plot(x, psoInt, linestyle="-", marker="v", linewidth='1', label="PSO")
plt.plot(x, woaInt, linestyle="-.", marker="*", linewidth='1', label="WOA")
plt.plot(x, woablr, linestyle="-", marker="o", linewidth='1', label="RTGS")

plt.xlabel('task number', fontsize=12)
plt.ylabel('final finish time gap', fontsize=12)
plt.legend(ncol=3, fontsize=10)
plt.savefig("50-svmad-svmm-svm-df-blr.jpg")
plt.show()
